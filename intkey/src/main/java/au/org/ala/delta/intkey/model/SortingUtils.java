package au.org.ala.delta.intkey.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.FloatRange;

import au.org.ala.delta.intkey.model.specimen.Specimen;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.IntegerAttribute;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealAttribute;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextCharacter;

public class SortingUtils {
    
    private static double minimumSeparatingPower = 0.0001;

    // TODO change arguments, pass in something other than the entire context.
    public static LinkedHashMap<au.org.ala.delta.model.Character, Double> orderBest(IntkeyContext context) {
        IntkeyDataset dataset = context.getDataset();
        Specimen specimen = context.getSpecimen();

        List<Character> allCharacters = dataset.getCharacters();

        // TODO refactor so that this array does not need to be final
        final double[] suVals = new double[allCharacters.size()];
        double[] sepVals = new double[allCharacters.size()];

        double[] charCosts = new double[allCharacters.size()];

        for (Character ch : dataset.getCharacters()) {
            double charCost = Math.pow(context.getRBase(), 5.0 - Math.min(10.0, ch.getReliability()));
            charCosts[ch.getCharacterId() - 1] = charCost;
        }

        double varw = (1 - context.getVaryWeight()) / context.getVaryWeight();

        // Build list of available characters
        List<Character> availableCharacters = new ArrayList<Character>(specimen.getAvailableCharacters());
        List<Character> ignoredCharacters = new ArrayList<Character>();
        for (Character ch : availableCharacters) {

            // TODO ignore EXACT characters that have been eliminated
            // TODO ignore characters not "masked in" - excluded characters?

            // Ignore character if its reliability is zero
            if (ch.getReliability() == 0) {
                ignoredCharacters.add(ch);
            }

            // Ignore character if it is a text character
            if (ch instanceof TextCharacter) {
                ignoredCharacters.add(ch);
            }

            // Ignore real characters if there are no key states
            // for real characters
            if (ch instanceof RealCharacter && !context.getDataset().realCharacterKeyStateBoundariesPresent()) {
                ignoredCharacters.add(ch);
            }
        }
        availableCharacters.removeAll(ignoredCharacters);

        // Build list of remaining taxa
        int numAvailableTaxa = 0;
        Map<Item, Boolean> taxaAvailability = new HashMap<Item, Boolean>();

        // TODO this line throws exception if no characters have been USEd yet
        Map<Item, Integer> taxonDifferences = specimen.getTaxonDifferences();
        for (Item taxon : dataset.getTaxa()) {

            boolean ignore = false;

            if (taxonDifferences != null && taxonDifferences.get(taxon) > context.getTolerance()) {
                ignore = true;
                ;
            }

            // TODO skip if taxon is not included

            // TODO skip if there are EXACT characters and this taxon has
            // been eliminated

            if (ignore) {
                taxaAvailability.put(taxon, false);
            } else {
                numAvailableTaxa++;
                taxaAvailability.put(taxon, true);
            }

        }

        // sort available characters by reliability (descending)
        Collections.sort(availableCharacters, new ReliabilityComparator());

        // minimum cost - this will always be the cost of the available
        // character with the greatest reliability
        double cmin = charCosts[availableCharacters.get(0).getCharacterId() - 1];

        List<Character> unsuitableCharacters = new ArrayList<Character>();

        for (Character ch : availableCharacters) {

            int sumNumTaxaInSubgroups = 0;
            double sumSubgroupsFrequencies = 0;
            int numSubgroupsSameSizeAsOriginalGroup = 0;
            double sup0 = 0; // theoretical partition component of sup.
            double dupf = 0; // arbitrary intra-taxon variability component of
                             // sup.
            double sep = 0; // separating power of the character
            double sup = 0; // total partition component of su. sup = sup0 +
                            // dupf
            double su = 0; // character suitability

            // NOTE: to simplify the algorithm, all characters are treated as
            // multistate characters. Integer and real
            // characters are converted into multistate representations.

            // Determine the total available states for each character
            int totalNumStates = 0;
            if (ch instanceof MultiStateCharacter) {
                totalNumStates = ((MultiStateCharacter) ch).getNumberOfStates();
            } else if (ch instanceof IntegerCharacter) {
                // for an integer character, 1 state for each value between
                // the minimum and
                // maximum (inclusive), 1 state for all values below the
                // minimum, and 1 state for
                // all values above the maximum
                IntegerCharacter intChar = (IntegerCharacter) ch;
                totalNumStates = intChar.getMaximumValue() - intChar.getMinimumValue() + 3;
            } else if (ch instanceof RealCharacter) {
                // the real character's key state boundaries are used to convert
                // a real value into a
                // multistate value (see below). The total number of possible
                // states is equal to the number of
                // key state boundaries.
                totalNumStates = ((RealCharacter) ch).getKeyStateBoundaries().size();
            } else {
                throw new RuntimeException("Invalid character type " + ch.toString());
            }

            // number of taxa in character subgroups
            int[] subgroupsNumTaxa = new int[totalNumStates];

            // frequency of character subgroups
            double[] subgroupFrequencies = new double[totalNumStates];

            List<Attribute> charAttributes = dataset.getAttributesForCharacter(ch.getCharacterId());

            for (Attribute attr : charAttributes) {
                Item taxon = attr.getItem();

                // Skip any attributes that pertain to taxa that are not
                // available
                if (!taxaAvailability.get(taxon)) {
                    continue;
                }

                // has a boolean value for each character state. A true value
                // designates the presence of the corresponding character state
                // for the attribute.
                boolean[] statePresence = new boolean[totalNumStates];

                int numStatesPresent = 0;

                // determine which character states are present for the
                // attribute.
                if (attr.isUnknown()) {
                    // Treat attribute unknown as variable
                    Arrays.fill(statePresence, true);
                    numStatesPresent = totalNumStates;
                } else {
                    Arrays.fill(statePresence, false);

                    if (ch.getCharacterType() == CharacterType.OrderedMultiState || ch.getCharacterType() == CharacterType.UnorderedMultiState) {
                        MultiStateAttribute multiStateAttr = (MultiStateAttribute) attr;
                        Set<Integer> attrPresentStates = multiStateAttr.getPresentStates();

                        for (int i = 0; i < totalNumStates; i++) {
                            if (attrPresentStates.contains(i + 1)) {
                                statePresence[i] = true;
                                numStatesPresent++;
                            }
                        }

                    } else if (ch.getCharacterType() == CharacterType.IntegerNumeric) {
                        IntegerCharacter intChar = (IntegerCharacter) ch;
                        IntegerAttribute intAttr = (IntegerAttribute) attr;

                        // for an integer character, 1 state for each value
                        // between
                        // the minimum and
                        // maximum (inclusive), 1 state for all values below the
                        // minimum, and 1 state for
                        // all values above the maximum

                        Set<Integer> attrPresentStates = intAttr.getPresentValues();

                        int offset = intChar.getMinimumValue() - 1;

                        for (int i = 0; i < totalNumStates; i++) {
                            if (attrPresentStates.contains(i + offset)) {
                                statePresence[i] = true;
                                numStatesPresent++;
                            }
                        }

                    } else if (ch.getCharacterType() == CharacterType.RealNumeric) {
                        RealCharacter realChar = (RealCharacter) ch;
                        RealAttribute realAttr = (RealAttribute) attr;
                        FloatRange presentRange = realAttr.getPresentRange();

                        // convert real value into multistate value.
                        numStatesPresent = generateKeyStatesForRealCharacter(realChar, presentRange, statePresence);
                    } else {
                        throw new RuntimeException("Invalid character type " + ch.toString());
                    }
                }

                // work out size of character subgroups.
                for (int i = 0; i < totalNumStates; i++) {

                    if (statePresence[i] == true) {
                        subgroupsNumTaxa[i]++;

                        // frequency of items with current state of current
                        // character
                        double stateFrequency = 1.0 / (double) numStatesPresent;
                        stateFrequency += subgroupFrequencies[i];
                        subgroupFrequencies[i] = stateFrequency;
                    }
                }

            }

            // total number of non-empty character subgroups
            int totalNumSubgroups = 0;

            // work out sum of subgroup sizes and frequencies
            for (int i = 0; i < totalNumStates; i++) {
                sumNumTaxaInSubgroups += subgroupsNumTaxa[i];
                sumSubgroupsFrequencies += subgroupFrequencies[i];

                if (subgroupsNumTaxa[i] > 0) {
                    totalNumSubgroups++;
                }
            }

            // character is unsuitable if it divides the characters into a
            // single
            // subgroup
            boolean allTaxaInOneGroup = false;
            for (int i = 0; i < totalNumStates; i++) {
                int numTaxaInSubgroup = subgroupsNumTaxa[i];

                if (numTaxaInSubgroup == sumNumTaxaInSubgroups) {
                    allTaxaInOneGroup = true;
                    break;
                } else {
                    if (numTaxaInSubgroup == numAvailableTaxa) {
                        numSubgroupsSameSizeAsOriginalGroup++;
                    }

                    if (subgroupsNumTaxa[i] > 0) {
                        sup0 += (subgroupFrequencies[i] * log2(subgroupsNumTaxa[i]));
                    }
                }
            }

            // A character is unsuitable if it has the same value for all
            // available characters
            if (allTaxaInOneGroup) {
                unsuitableCharacters.add(ch);
                continue;
            }

            boolean isControllingChar = !ch.getDependentCharacters().isEmpty();
            // TODO what is this test for???
            if (!isControllingChar && (totalNumSubgroups == numSubgroupsSameSizeAsOriginalGroup || (sumNumTaxaInSubgroups > numAvailableTaxa && numSubgroupsSameSizeAsOriginalGroup == totalNumStates))) {
                unsuitableCharacters.add(ch);
                continue;
            }

            sup0 = sup0 / sumSubgroupsFrequencies;

            if (numAvailableTaxa > 1 && sumNumTaxaInSubgroups > numAvailableTaxa) {
                dupf = varw * (1 + 100 * numSubgroupsSameSizeAsOriginalGroup) * (sumNumTaxaInSubgroups - numAvailableTaxa) * ((numAvailableTaxa + 8) / (numAvailableTaxa * log2(numAvailableTaxa)));
            } else {
                dupf = 0;
            }

            sep = -sup0 + log2(numAvailableTaxa);

            // TODO handle rounding errors?
            if (Math.abs(sep) <= minimumSeparatingPower) {
                sep = 0.0;
            }

            // TODO don't display controlling characters with 0 separation
            if (isControllingChar && sep == 0) {
                unsuitableCharacters.add(ch);
                continue;
            }

            sup = sup0 + dupf;

            su = charCosts[ch.getCharacterId() - 1] + cmin * sup;

            sepVals[ch.getCharacterId() - 1] = sep;
            suVals[ch.getCharacterId() - 1] = su;
        }

        availableCharacters.removeAll(unsuitableCharacters);

        List<Character> sortedChars = new ArrayList<Character>(availableCharacters);
        Collections.sort(sortedChars, new Comparator<Character>() {

            @Override
            public int compare(Character c1, Character c2) {
                // TODO had to make suMap final - dodgy

                // float c1Su = (float) suVals[c1.getCharacterId() - 1];
                // float c2Su = (float) suVals[c2.getCharacterId() - 1];

                // return Float.valueOf(c1Su).compareTo(Float.valueOf(c2Su));

                return Double.valueOf(suVals[c1.getCharacterId() - 1]).compareTo(Double.valueOf(suVals[c2.getCharacterId() - 1]));
            }
        });

        LinkedHashMap<Character, Double> retMap = new LinkedHashMap<Character, Double>();

        for (Character ch : sortedChars) {
            retMap.put(ch, sepVals[ch.getCharacterId() - 1]);
        }

        return retMap;
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * Helper method for orderBest. Takes a real character's value and converts
     * it into a multistate value using the real character's key state
     * boundaries
     * 
     * @param realChar
     *            The real character
     * @param realValue
     *            The value for the real character
     * @param statePresence
     *            This array will be filled by the method. A true value in this
     *            array indicates that the corresponding state value is present
     * @return the number of key states present for the supplied real value
     */
    private static int generateKeyStatesForRealCharacter(RealCharacter realChar, FloatRange realValue, boolean[] statePresence) {
        int numStatesPresent = 0;

        List<Float> boundaries = realChar.getKeyStateBoundaries();

        float rangeMin = realValue.getMinimumFloat();
        float rangeMax = realValue.getMaximumFloat();

        int i = 0;
        for (; i < boundaries.size(); i++) {
            if (rangeMin <= boundaries.get(i)) {
                statePresence[i] = true;
                numStatesPresent++;
                break;
            }
        }

        for (; i < boundaries.size() - 1; i++) {
            if (rangeMax > boundaries.get(i)) {
                statePresence[i + 1] = true;
                numStatesPresent++;
            }
        }

        return numStatesPresent;
    }

    private static class ReliabilityComparator implements Comparator<Character> {

        @Override
        public int compare(Character c1, Character c2) {

            int compareResult = Float.valueOf(c1.getReliability()).compareTo(Float.valueOf(c2.getReliability()));

            // multiply by -1 to get descending order
            return compareResult * -1;
        }
    }
}
