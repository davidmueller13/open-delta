package au.org.ala.delta.intkey.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.FloatRange;

import au.org.ala.delta.intkey.model.specimen.CharacterValue;
import au.org.ala.delta.intkey.model.specimen.IntegerValue;
import au.org.ala.delta.intkey.model.specimen.MultiStateValue;
import au.org.ala.delta.intkey.model.specimen.RealValue;
import au.org.ala.delta.intkey.model.specimen.Specimen;
import au.org.ala.delta.intkey.model.specimen.TextValue;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.AttributeFactory;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.IntegerAttribute;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealAttribute;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextAttribute;
import au.org.ala.delta.model.TextCharacter;

/**
 * Utility methods used to compare character values. Used by the DIFFERENCES,
 * SIMILARITIES and USE directives, among others.
 * 
 * @author ChrisF
 * 
 */
public class DiffUtils {

    /**
     * Compare the values coded for the supplied character for each of the
     * supplied taxa. If supplied, also include the specimen in the comparison
     * 
     * @param dataset
     *            the currently loaded dataset
     * @param ch
     *            the character
     * @param taxa
     *            the list of taxa
     * @param specimen
     *            the specimen
     * @param matchUnknowns
     *            true if unknown matches any value
     * @param matchInapplicables
     *            true if inapplicable matches any value
     * @param matchType
     *            the match type - exact, subset or overlap
     * @return true if the values coded for the specified character match for
     *         all of the supplied taxa, as well as for the specimen if it was
     *         supplied.
     */
    public static boolean compareForTaxa(IntkeyDataset dataset, Character ch, List<Item> taxa, Specimen specimen, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        int countUnknown = 0;
        int countNotApplicable = 0;
        int countTaxaWithCharacterCoded = 0;

        List<Attribute> allAttrsForChar = dataset.getAttributesForCharacter(ch.getCharacterId());
        List<Attribute> attrs = new ArrayList<Attribute>();

        // get number of states present in taxa to be compared
        for (Item t : taxa) {
            Attribute attr = (Attribute) allAttrsForChar.get(t.getItemNumber() - 1);
            if (attr.isUnknown()) {
                if (attr.isInapplicable()) {
                    countNotApplicable++;
                } else {
                    countUnknown++;
                }
            } else {
                countTaxaWithCharacterCoded++;
                attrs.add(attr);
            }
        }

        if (specimen != null) {
            if (!specimen.hasValueFor(ch)) {
                if (specimen.isCharacterInapplicable(ch)) {
                    countNotApplicable++;
                } else {
                    countUnknown++;
                }
            } else {
                countTaxaWithCharacterCoded++;

                // create an attribute containing the same value as the specimen
                // value. this is done to simplify the
                // comparison code as only the one datatype then needs to be
                // handled.
                attrs.add(createAttributeForSpecimenValue(specimen, ch));
            }
        }

        if (countUnknown > 0 && !matchUnknowns) {
            return false;
        } else if (countNotApplicable > 0 && !matchInapplicables) {
            return false;
        } else if (countTaxaWithCharacterCoded > 1) {
            if (ch instanceof MultiStateCharacter || ch instanceof IntegerCharacter) {
                return doCompareMultistateOrIntegerForTaxa(attrs, matchUnknowns, matchInapplicables, matchType);
            } else if (ch instanceof RealCharacter) {
                List<RealAttribute> realAttrs = new ArrayList<RealAttribute>();
                for (Attribute attr : attrs) {
                    realAttrs.add((RealAttribute) attr);
                }
                return doCompareRealForTaxa(realAttrs, matchUnknowns, matchInapplicables, matchType);
            } else if (ch instanceof TextCharacter) {
                List<TextAttribute> textAttrs = new ArrayList<TextAttribute>();
                for (Attribute attr : attrs) {
                    textAttrs.add((TextAttribute) attr);
                }
                return doCompareTextForTaxa(textAttrs, matchUnknowns, matchInapplicables, matchType);
            } else {
                throw new RuntimeException("Unrecognised character type");
            }
        } else {
            return true;
        }
    }

    // comparator used to sort attributes by some value, e.g. number of states
    // or text length
    private static class AttributeValueSizeComparator implements Comparator<Attribute> {

        private Map<Attribute, Double> _valueSizes;

        public AttributeValueSizeComparator(Map<Attribute, Double> valueSizes) {
            _valueSizes = valueSizes;
        }

        @Override
        public int compare(Attribute a1, Attribute a2) {
            double a1ValueSize = _valueSizes.get(a1);
            double a2ValueSize = _valueSizes.get(a2);

            if (a1ValueSize < a2ValueSize) {
                return -1;
            } else if (a1ValueSize == a2ValueSize) {
                return 0;
            } else {
                return 1;
            }
        }

    }

    // Helper method to compare lists of multistate or integer attributes.
    // "attrs" list must contain all multistates or all integers, otherwise
    // a class cast exception will be thrown.
    private static boolean doCompareMultistateOrIntegerForTaxa(List<Attribute> attrs, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {

        boolean typeMultistate = attrs.get(0) instanceof MultiStateAttribute;

        // sort attributes in ascending order based on the number of states
        // present
        Map<Attribute, Double> attrNumberOfStates = new HashMap<Attribute, Double>();

        for (Attribute attr : attrs) {
            if (typeMultistate) {
                MultiStateAttribute msAttr = (MultiStateAttribute) attr;
                attrNumberOfStates.put(msAttr, (double) msAttr.getPresentStates().size());
            } else {
                IntegerAttribute intAttr = (IntegerAttribute) attr;
                attrNumberOfStates.put(intAttr, (double) intAttr.getPresentValues().size());
            }
        }

        Collections.sort(attrs, new AttributeValueSizeComparator(attrNumberOfStates));

        if (matchType == MatchType.EXACT || matchType == MatchType.SUBSET) {

            boolean diff = false;

            for (int i = 0; i < attrs.size() - 1; i++) {

                if (typeMultistate) {
                    MultiStateAttribute a1 = (MultiStateAttribute) attrs.get(i);
                    MultiStateAttribute a2 = (MultiStateAttribute) attrs.get(i + 1);
                    diff = !compareMultistate(a1, a2, matchUnknowns, matchInapplicables, matchType);
                } else {
                    IntegerAttribute a1 = (IntegerAttribute) attrs.get(i);
                    IntegerAttribute a2 = (IntegerAttribute) attrs.get(i + 1);
                    diff = !compareInteger(a1, a2, matchUnknowns, matchInapplicables, matchType);
                }

                if (diff) {
                    break;
                }
            }

            return !diff;

        } else {
            // overlap - taxa must have at least one state in common

            // convert state data into arrays for efficiency
            int numAttrs = attrs.size();
            int[][] attrStates = new int[numAttrs][];

            for (int i = 0; i < numAttrs; i++) {

                Attribute attr = attrs.get(i);
                List<Integer> states;
                if (typeMultistate) {
                    states = new ArrayList<Integer>(((MultiStateAttribute) attr).getPresentStates());
                } else {
                    states = new ArrayList<Integer>(((IntegerAttribute) attr).getPresentValues());
                }
                int[] statesArray = new int[states.size()];

                for (int j = 0; j < states.size(); j++) {
                    int stateVal = states.get(j);
                    statesArray[j] = stateVal;
                }

                attrStates[i] = statesArray;
            }

            // compare the states for each pair of attributes. Note that each
            // pair only needs to be compared once.
            for (int i = 0; i < numAttrs; i++) {
                int[] a1States = attrStates[i];

                for (int j = i + 1; j < numAttrs; j++) {
                    boolean overlap = false;

                    int[] a2States = attrStates[j];

                    for (int k = 0; k < a1States.length && !overlap; k++) {
                        int a1StateVal = a1States[k];

                        for (int l = 0; l < a2States.length; l++) {
                            int a2StateVal = a2States[l];

                            if (a1StateVal == a2StateVal) {
                                overlap = true;
                                break;
                            }
                        }
                    }
                    if (!overlap) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static boolean doCompareRealForTaxa(List<RealAttribute> attrs, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        // sort attributes in ascending order according to range length
        Map<Attribute, Double> attrRangeLengths = new HashMap<Attribute, Double>();

        for (RealAttribute attr : attrs) {
            FloatRange range = attr.getPresentRange();
            attrRangeLengths.put(attr, (double) (range.getMaximumFloat() - range.getMinimumFloat()));
        }

        Collections.sort(attrs, new AttributeValueSizeComparator(attrRangeLengths));

        if (matchType == MatchType.EXACT || matchType == MatchType.SUBSET) {
            boolean diff = false;

            for (int i = 0; i < attrs.size() - 1; i++) {

                RealAttribute a1 = attrs.get(i);
                RealAttribute a2 = attrs.get(i + 1);
                diff = !compareReal(a1, a2, matchUnknowns, matchInapplicables, matchType);

                if (diff) {
                    break;
                }
            }

            return !diff;
        } else {
            // overlap - taxa must have at least one point in common
            // progressively calculate area of overlap
            int numAttrs = attrs.size();

            for (int i = 0; i < numAttrs; i++) {
                RealAttribute a1 = attrs.get(i);
                FloatRange a1Range = a1.getPresentRange();

                for (int j = i + 1; j < numAttrs; j++) {
                    boolean overlap = false;

                    RealAttribute a2 = attrs.get(j);
                    FloatRange a2Range = a2.getPresentRange();

                    overlap = a2Range.overlapsRange(a1Range);

                    if (!overlap) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static boolean doCompareTextForTaxa(List<TextAttribute> attrs, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        // sort attributes in ascending order according to text length
        Map<Attribute, Double> attrTextLengths = new HashMap<Attribute, Double>();

        for (TextAttribute attr : attrs) {
            String attrText = attr.getText();
            attrTextLengths.put(attr, (double) attrText.length());
        }

        Collections.sort(attrs, new AttributeValueSizeComparator(attrTextLengths));

        boolean diff = false;

        for (int i = 0; i < attrs.size() - 1; i++) {

            TextAttribute a1 = attrs.get(i);
            TextAttribute a2 = attrs.get(i + 1);
            diff = !compareText(a1, a2, matchUnknowns, matchInapplicables, matchType);

            if (diff) {
                break;
            }
        }

        return !diff;

    }

    // Helper method to create an attribute containing the value held in a specimen for a particular character. This is used to simplify 
    // the comparsion methods, allowing them to only handle the one data type (attributes)
    private static Attribute createAttributeForSpecimenValue(Specimen specimen, Character ch) {
        boolean unknown = specimen.hasValueFor(ch);
        boolean inapplicable = specimen.isCharacterInapplicable(ch);

        IntkeyAttributeData attrData = new IntkeyAttributeData(unknown, inapplicable);

        if (!unknown && !inapplicable) {
            CharacterValue val = specimen.getValueForCharacter(ch);
            if (val instanceof MultiStateValue) {
                attrData.setPresentStateOrIntegerValues(new HashSet<Integer>(((MultiStateValue) val).getStateValues()));
            } else if (val instanceof IntegerValue) {
                attrData.setPresentStateOrIntegerValues(new HashSet<Integer>(((IntegerValue) val).getValues()));
            } else if (val instanceof RealValue) {
                attrData.setRealRange(((RealValue) val).getRange());
            } else if (val instanceof TextValue) {
                String joinedString = StringUtils.join(((TextValue) val).getValues(), '/');
                attrData.setValueFromString(joinedString);
            } else {
                throw new RuntimeException("Unrecognised character value type");
            }
        }

        return AttributeFactory.newAttribute(ch, attrData);
    }

    private static boolean compareMultistate(MultiStateAttribute attr1, MultiStateAttribute attr2, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        Set<Integer> attr1Values = attr1.getPresentStates();
        Set<Integer> attr2Values = attr2.getPresentStates();
        boolean attr1Unknown = attr1.isUnknown();
        boolean attr2Unknown = attr2.isUnknown();
        boolean attr1Inapplicable = attr1.isInapplicable();
        boolean attr2Inapplicable = attr2.isInapplicable();

        return doCompareMultiStateOrInteger(attr1Values, attr2Values, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    /**
     * Compare a multistate specimen value with a multistate attribute. Both
     * values must correspond to the same character
     * 
     * @param specimen
     *            the specimen
     * @param val
     *            the specimen value
     * @param attr
     *            the attribute
     * @param matchUnknowns
     *            true if unknown matches any value
     * @param matchInapplicables
     *            true if inapplicable matches any value
     * @param matchType
     *            the match type - exact, subset or overlap
     * @return true if the specimen value matches the attribute
     */
    public static boolean compareMultistate(Specimen specimen, MultiStateValue val, MultiStateAttribute attr, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        Set<Integer> attr1Values = new HashSet<Integer>(val.getStateValues());
        Set<Integer> attr2Values = attr.getPresentStates();
        boolean attr1Unknown = specimen.hasValueFor(val.getCharacter());
        boolean attr2Unknown = attr.isUnknown();
        boolean attr1Inapplicable = specimen.isCharacterInapplicable(val.getCharacter());
        boolean attr2Inapplicable = attr.isInapplicable();

        return doCompareMultiStateOrInteger(attr1Values, attr2Values, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    private static boolean compareInteger(IntegerAttribute attr1, IntegerAttribute attr2, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        Set<Integer> attr1Values = attr1.getPresentValues();
        Set<Integer> attr2Values = attr2.getPresentValues();
        boolean attr1Unknown = attr1.isUnknown();
        boolean attr2Unknown = attr2.isUnknown();
        boolean attr1Inapplicable = attr1.isInapplicable();
        boolean attr2Inapplicable = attr2.isInapplicable();

        return doCompareMultiStateOrInteger(attr1Values, attr2Values, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    /**
     * Compare an integer specimen value with an integer attribute. Both values
     * must correspond to the same character
     * 
     * @param specimen
     *            the specimen
     * @param val
     *            the specimen value
     * @param attr
     *            the attribute
     * @param matchUnknowns
     *            true if unknown matches any value
     * @param matchInapplicables
     *            true if inapplicable matches any value
     * @param matchType
     *            the match type - exact, subset or overlap
     * @return true if the specimen value matches the attribute
     */
    public static boolean compareInteger(Specimen specimen, IntegerValue val, IntegerAttribute attr, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        Set<Integer> attr1Values = new HashSet<Integer>(val.getValues());
        Set<Integer> attr2Values = attr.getPresentValues();
        boolean attr1Unknown = specimen.hasValueFor(val.getCharacter());
        boolean attr2Unknown = attr.isUnknown();
        boolean attr1Inapplicable = specimen.isCharacterInapplicable(val.getCharacter());
        boolean attr2Inapplicable = attr.isInapplicable();

        return doCompareMultiStateOrInteger(attr1Values, attr2Values, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    private static boolean doCompareMultiStateOrInteger(Set<Integer> attr1Values, Set<Integer> attr2Values, boolean attr1Unknown, boolean attr2Unknown, boolean attr1Inapplicable,
            boolean attr2Inapplicable, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        if ((attr1Unknown && attr1Inapplicable) || (attr1Unknown && attr1Inapplicable)) {
            return matchInapplicables;
        }

        if ((attr1Unknown && !attr1Inapplicable) || (attr2Unknown && !attr2Inapplicable)) {
            return matchUnknowns;
        }

        boolean match = false;

        switch (matchType) {
        case EXACT:
            match = attr1Values.equals(attr2Values);
            break;
        case SUBSET:
            match = attr1Values.containsAll(attr2Values);
            break;
        case OVERLAP:
            for (int stateVal : attr1Values) {
                if (attr2Values.contains(stateVal)) {
                    match = true;
                    break;
                }
            }
            break;
        default:
            throw new RuntimeException(String.format("Unrecognized match type %s", matchType.toString()));
        }

        return match;
    }

    /**
     * Compare a real specimen value with a real attribute. Both values must
     * correspond to the same character
     * 
     * @param specimen
     *            the specimen
     * @param val
     *            the specimen value
     * @param attr
     *            the attribute
     * @param matchUnknowns
     *            true if unknown matches any value
     * @param matchInapplicables
     *            true if inapplicable matches any value
     * @param matchType
     *            the match type - exact, subset or overlap
     * @return true if the specimen value matches the attribute
     */
    public static boolean compareReal(Specimen specimen, RealValue val, RealAttribute attr, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        FloatRange attr1Range = val.getRange();
        FloatRange attr2Range = attr.getPresentRange();
        boolean attr1Unknown = specimen.hasValueFor(val.getCharacter());
        boolean attr2Unknown = attr.isUnknown();
        boolean attr1Inapplicable = specimen.isCharacterInapplicable(val.getCharacter());
        boolean attr2Inapplicable = attr.isInapplicable();

        return doCompareRange(attr1Range, attr2Range, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    private static boolean compareReal(RealAttribute attr1, RealAttribute attr2, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        FloatRange attr1Range = attr1.getPresentRange();
        FloatRange attr2Range = attr2.getPresentRange();
        boolean attr1Unknown = attr1.isUnknown();
        boolean attr2Unknown = attr2.isUnknown();
        boolean attr1Inapplicable = attr1.isInapplicable();
        boolean attr2Inapplicable = attr2.isInapplicable();

        return doCompareRange(attr1Range, attr2Range, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    private static boolean doCompareRange(FloatRange attr1Range, FloatRange attr2Range, boolean attr1Unknown, boolean attr2Unknown, boolean attr1Inapplicable, boolean attr2Inapplicable,
            boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        if ((attr1Unknown && attr1Inapplicable) || (attr1Unknown && attr1Inapplicable)) {
            return matchInapplicables;
        }

        if ((attr1Unknown && !attr1Inapplicable) || (attr2Unknown && !attr2Inapplicable)) {
            return matchUnknowns;
        }

        boolean match = false;

        switch (matchType) {
        case EXACT:
            match = attr1Range.equals(attr2Range);
            break;
        case SUBSET:
            match = attr1Range.containsRange(attr2Range);
            break;
        case OVERLAP:
            match = attr1Range.overlapsRange(attr2Range);
            break;
        default:
            throw new RuntimeException(String.format("Unrecognized match type %s", matchType.toString()));
        }

        return match;
    }

    /**
     * Compare a real specimen value with a real attribute. Both values must
     * correspond to the same character The following rules apply:
     * 
     * 1. MATCH INAPPLICABLE and MATCH UNKNOWN are ignored. Inapplicables and
     * unknowns are treated as a mismatch. 2. The text to be found may consist
     * of a number of sub-strings separated by '/'. In the cases of MATCH EXACT
     * and MATCH SUBSET, each sub-string must exist separately in the searched
     * text. For MATCH OVERLAP, the presence of any sub-string will result in a
     * match.
     * 
     * @param specimen
     *            the specimen
     * @param val
     *            the specimen value
     * @param attr
     *            the attribute
     * @param matchUnknowns
     *            true if unknown matches any value
     * @param matchInapplicables
     *            true if inapplicable matches any value
     * @param matchType
     *            the match type - exact, subset or overlap
     * @return true if the specimen value matches the attribute
     */
    public static boolean compareText(Specimen specimen, TextValue val, TextAttribute attr, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        List<String> specimenTextValues = val.getValues();
        String attrText = attr.getText();

        boolean attr1Unknown = specimen.hasValueFor(val.getCharacter());
        boolean attr2Unknown = attr.isUnknown();
        boolean attr1Inapplicable = specimen.isCharacterInapplicable(val.getCharacter());
        boolean attr2Inapplicable = attr.isInapplicable();

        return doCompareText(specimenTextValues, attrText, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    private static boolean compareText(TextAttribute attr1, TextAttribute attr2, boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {
        String attr1Text = attr1.getText();
        String attr2Text = attr2.getText();
        boolean attr1Unknown = attr1.isUnknown();
        boolean attr2Unknown = attr2.isUnknown();
        boolean attr1Inapplicable = attr1.isInapplicable();
        boolean attr2Inapplicable = attr2.isInapplicable();

        // The text for the first attribute passed in may be multiple values set
        // for a text character in a specimen, delimited by
        // "/".
        List<String> attr1Values = Arrays.asList(attr1Text.split("/"));

        return doCompareText(attr1Values, attr2Text, attr1Unknown, attr2Unknown, attr1Inapplicable, attr2Inapplicable, matchUnknowns, matchInapplicables, matchType);
    }

    /**
     * compares two text characters applying the following rules - 1. MATCH
     * INAPPLICABLE and MATCH UNKNOWN are ignored. Inapplicables and unknowns
     * are treated as a mismatch. 2. The text to be found may consist of a
     * number of sub-strings separated by '/'. In the cases of MATCH EXACT and
     * MATCH SUBSET, each sub-string must exist separately in the searched text.
     * For MATCH OVERLAP, the presence of any sub-string will result in a match.
     * 
     * @param val
     * @param attr
     * @return
     */
    private static boolean doCompareText(List<String> attr1Values, String attr2Value, boolean attr1Unknown, boolean attr2Unknown, boolean attr1Inapplicable, boolean attr2Inapplicable,
            boolean matchUnknowns, boolean matchInapplicables, MatchType matchType) {

        // Unknown and inapplicable always equate to no match for text
        // attributes
        if ((attr1Unknown && attr1Inapplicable) || (attr1Unknown && attr1Inapplicable)) {
            return false;
        }

        if ((attr1Unknown && !attr1Inapplicable) || (attr2Unknown && !attr2Inapplicable)) {
            return false;
        }

        boolean match = false;

        // Remove surrounding angle brackets from attribute text.
        attr2Value = attr2Value.substring(1, attr2Value.length() - 1).toLowerCase();

        switch (matchType) {
        case EXACT:
        case SUBSET:
            match = true;
            for (String txtVal : attr1Values) {
                if (!attr2Value.contains(txtVal.toLowerCase())) {
                    match = false;
                    break;
                }
            }
            break;
        case OVERLAP:
            for (String txtVal : attr1Values) {
                if (attr2Value.contains(txtVal.toLowerCase())) {
                    match = true;
                    break;
                }
            }
            break;
        default:
            throw new RuntimeException(String.format("Unrecognized match type %s", matchType.toString()));
        }

        return match;
    }
}
