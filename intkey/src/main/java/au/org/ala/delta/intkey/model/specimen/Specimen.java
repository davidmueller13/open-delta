package au.org.ala.delta.intkey.model.specimen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.org.ala.delta.intkey.model.DiffUtils;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.intkey.model.MatchType;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterDependency;
import au.org.ala.delta.model.IntegerAttribute;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.RealAttribute;
import au.org.ala.delta.model.TextAttribute;

public class Specimen {

    private IntkeyDataset _dataset;

    boolean _matchInapplicables;
    boolean _matchUnknowns;
    MatchType _matchType;

    // Use a linked hash map so that character can be returned in the
    // order that they were used.
    private LinkedHashMap<Character, CharacterValue> _characterValues;

    /**
     * Keeps a count of the number of times a character has been made
     * inapplicable - this is needed because a character can have more that one
     * character controlling it. If there is no entry for a character in this
     * map, or its entry is zero, then is it not inapplicable
     */
    private Map<Character, Integer> _characterInapplicabilityCounts;

    private Map<Item, Set<Character>> _taxonDifferences;

    public Specimen(IntkeyDataset dataset, boolean matchInapplicables, boolean matchUnknowns, MatchType matchType) {
        _characterValues = new LinkedHashMap<Character, CharacterValue>();

        _dataset = dataset;

        _characterInapplicabilityCounts = new HashMap<Character, Integer>();

        _matchInapplicables = matchInapplicables;
        _matchUnknowns = matchUnknowns;
        _matchType = matchType;

        // initialise the taxon differences table
        _taxonDifferences = new HashMap<Item, Set<Character>>();
        for (Item taxon : _dataset.getTaxa()) {
            _taxonDifferences.put(taxon, new HashSet<Character>());
        }
    }

    // Used to copy values out of an existing specimen and apply new match
    // settings
    public Specimen(IntkeyDataset dataset, boolean matchInapplicables, boolean matchUnknowns, MatchType matchType, Specimen oldSpecimen) {
        this(dataset, matchInapplicables, matchUnknowns, matchType);

        for (Character ch : oldSpecimen.getUsedCharacters()) {
            setValueForCharacter(ch, oldSpecimen.getValueForCharacter(ch));
        }
    }

    public boolean hasValueFor(Character ch) {
        return _characterValues.containsKey(ch);
    }

    public CharacterValue getValueForCharacter(Character ch) {
        return _characterValues.get(ch);
    }

    public void removeValueForCharacter(Character ch) {
        CharacterValue valToRemove = _characterValues.get(ch);

        // Do nothing if no value recorded for the supplied character
        if (valToRemove != null) {

            // IMPORTANT - differences table must be updated first, if
            // _characterValues
            // is modified first then the differences table will be updated
            // incorrectly!
            updateDifferencesTable(valToRemove, true);

            _characterValues.remove(ch);

            // If this is a controlling character, also need to remove values
            // for
            // any dependent characters
            for (CharacterDependency cd : ch.getDependentCharacters()) {
                // This is a controlling character, so that value to remove must
                // be multistate
                MultiStateValue msVal = (MultiStateValue) valToRemove;

                for (int dependentCharId : cd.getDependentCharacterIds()) {
                    Character dependentCharacter = _dataset.getCharacter(dependentCharId);
                    removeValueForCharacter(dependentCharacter);

                    // If this character was inapplicable due to its value,
                    // update the inapplicablity count for it
                    // and its dependants
                    if (cd.getStates().containsAll(msVal.getStateValues())) {
                        processPreviouslyInapplicableCharacter(dependentCharacter);
                    }
                }
            }
        }
    }

    /**
     * @return a list of characters that have been used, in the order that they
     *         were used.
     */
    public List<Character> getUsedCharacters() {
        List<Character> usedCharacters = new ArrayList<Character>(_characterValues.keySet());
        return usedCharacters;
    }

    public void setValueForCharacter(Character ch, CharacterValue value) {
        if (!ch.equals(value.getCharacter())) {
            throw new IllegalArgumentException(String.format("Invalid value for character %s", ch.toString()));
        }

        if (isCharacterInapplicable(ch)) {
            throw new IllegalArgumentException(String.format("Cannot set character %s - this character is inapplicable", ch.toString()));
        }

        // do nothing if the supplied value is identical to the current value
        // for
        // the character.
        if (hasValueFor(ch) && getValueForCharacter(ch).equals(value)) {
            return;
        }

        if (hasValueFor(ch)) {
            removeValueForCharacter(ch);
        }

        // if there are controlling characters, check that their values have
        // been set.
        for (CharacterDependency cd : ch.getControllingCharacters()) {
            Character controllingChar = _dataset.getCharacter(cd.getControllingCharacterId());
            if (!hasValueFor(controllingChar)) {
                throw new IllegalStateException(String.format("Cannot set value for character %s - controlling character %s has not been set", ch.getCharacterId(), controllingChar.getCharacterId()));
            }
        }

        _characterValues.put(ch, value);

        for (CharacterDependency cd : ch.getDependentCharacters()) {
            // ch is a controlling character and therefore value must be a
            // multistate value
            MultiStateValue msVal = (MultiStateValue) value;

            if (cd.getStates().containsAll(msVal.getStateValues())) {
                for (int depCharId : cd.getDependentCharacterIds()) {
                    Character dependentChar = _dataset.getCharacter(depCharId);
                    removeValueForCharacter(dependentChar);
                    processNewlyInapplicableCharacter(dependentChar);
                }
            }
        }

        updateDifferencesTable(value, false);
    }

    private void processPreviouslyInapplicableCharacter(Character ch) {
        if (_characterInapplicabilityCounts.containsKey(ch)) {
            int newCount = _characterInapplicabilityCounts.get(ch) - 1;

            if (newCount == 0) {
                _characterInapplicabilityCounts.remove(ch);
            } else {
                _characterInapplicabilityCounts.put(ch, newCount);
            }
        } else {
            throw new IllegalStateException(String.format("Character %s not inapplicable", ch.getCharacterId()));
        }

        for (CharacterDependency cd : ch.getDependentCharacters()) {
            for (int depCharId : cd.getDependentCharacterIds()) {
                processPreviouslyInapplicableCharacter(_dataset.getCharacter(depCharId));
            }
        }
    }

    private void processNewlyInapplicableCharacter(Character ch) {
        if (_characterInapplicabilityCounts.containsKey(ch)) {
            _characterInapplicabilityCounts.put(ch, _characterInapplicabilityCounts.get(ch) + 1);
        } else {
            _characterInapplicabilityCounts.put(ch, 1);
        }

        for (CharacterDependency cd : ch.getDependentCharacters()) {
            for (int depCharId : cd.getDependentCharacterIds()) {
                processNewlyInapplicableCharacter(_dataset.getCharacter(depCharId));
            }
        }
    }

    private void updateDifferencesTable(CharacterValue val, boolean removed) {
        List<Attribute> attrs = _dataset.getAttributesForCharacter(val.getCharacter().getCharacterId());

        for (Item taxon : _dataset.getTaxa()) {
            boolean match = false;

            // Subtract 1 as taxa are 1 indexed in the dataset
            Attribute attr = attrs.get(taxon.getItemNumber() - 1);

            if (val instanceof MultiStateValue) {
                MultiStateValue msVal = (MultiStateValue) val;
                MultiStateAttribute msAttr = (MultiStateAttribute) attr;
                match = DiffUtils.compareMultistate(this, msVal, msAttr, _matchUnknowns, _matchInapplicables, _matchType);
            } else if (val instanceof IntegerValue) {
                IntegerValue intVal = (IntegerValue) val;
                IntegerAttribute intAttr = (IntegerAttribute) attr;
                match = DiffUtils.compareInteger(this, intVal, intAttr, _matchUnknowns, _matchInapplicables, _matchType);
            } else if (val instanceof RealValue) {
                RealValue realVal = (RealValue) val;
                RealAttribute realAttr = (RealAttribute) attr;
                match = DiffUtils.compareReal(this, realVal, realAttr, _matchUnknowns, _matchInapplicables, _matchType);
            } else if (val instanceof TextValue) {
                TextValue txtVal = (TextValue) val;
                TextAttribute txtAttr = (TextAttribute) attr;
                match = DiffUtils.compareText(this, txtVal, txtAttr, _matchUnknowns, _matchInapplicables, _matchType);
            } else {
                throw new RuntimeException(String.format("Unrecognised CharacterValue subtype %s", val.getClass().getName()));
            }

            // int currentDiffCount = 0;
            // if (_taxonDifferences.containsKey(taxon)) {
            // currentDiffCount = _taxonDifferences.get(taxon);
            // }

            Set<Character> differingCharacters = _taxonDifferences.get(taxon);

            if (removed && !match) {
                // _taxonDifferences.put(taxon, Math.max(0, currentDiffCount -
                // 1));
                differingCharacters.remove(val.getCharacter());
            } else if (!removed && !match) {
                // _taxonDifferences.put(taxon, currentDiffCount + 1);
                differingCharacters.add(val.getCharacter());
            }
        }
    }

    // No defensive copy for efficiency reasons. The returned map should not be
    // modified
    public Map<Item, Set<Character>> getTaxonDifferences() {
        return _taxonDifferences;
    }

    public Set<Character> getInapplicableCharacters() {
        return new HashSet<Character>(_characterInapplicabilityCounts.keySet());
    }

    public boolean isCharacterInapplicable(Character ch) {
        return _characterInapplicabilityCounts.containsKey(ch) && _characterInapplicabilityCounts.get(ch) > 0;
    }
}
