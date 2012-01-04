package au.org.ala.delta.key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import au.org.ala.delta.best.Best;
import au.org.ala.delta.best.Best.OrderingType;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.util.Pair;

/**
 * Utilities for sorting characters using the BEST algorithm
 * 
 * @author ChrisF
 * 
 */
public class KeyBest {

    /**
     * A separating power below this is treated as zero
     */
    private static double minimumSeparatingPower = 0.0001;

    /**
     * Determines best order and separating power of all characters in the
     * supplied context's dataset
     * 
     * @param context
     *            the application's global state
     * @return a map of characters to their separating powers. The best order of
     *         the characters can be obtained by getting the keyset of the
     *         supplied map
     */
    public static LinkedHashMap<au.org.ala.delta.model.Character, Double> orderBest(DeltaDataSet dataset, List<Integer> availableCharacterNumbers, List<Integer> availableTaxaNumbers, double rBase,
            double varyWt) {
        LinkedHashMap<Character, Double> retMap = new LinkedHashMap<Character, Double>();

        if (availableCharacterNumbers.isEmpty() || availableTaxaNumbers.isEmpty()) {
            // no available characters or taxa - just return an empty map
            return retMap;
        }

        int numAvailableTaxa = availableTaxaNumbers.size();

        final double[] suVals = new double[dataset.getNumberOfCharacters()];
        double[] sepVals = new double[dataset.getNumberOfCharacters()];

        double[] charCosts = new double[dataset.getNumberOfCharacters()];

        for (int i = 0; i < dataset.getNumberOfCharacters(); i++) {
            Character ch = dataset.getCharacter(i + 1);
            double charCost = Math.pow(rBase, 5.0 - Math.min(10.0, ch.getReliability()));
            charCosts[ch.getCharacterId() - 1] = charCost;
        }

        double varw = (1 - varyWt) / Math.max(varyWt, 0.01);

        List<Character> availableCharacters = new ArrayList<Character>();
        for (int availableCharNum : availableCharacterNumbers) {
            availableCharacters.add(dataset.getCharacter(availableCharNum));
        }

        Set<Item> availableTaxa = new HashSet<Item>();
        for (int availableTaxonNum : availableTaxaNumbers) {
            availableTaxa.add(dataset.getItem(availableTaxonNum));
        }

        // sort available characters by reliability (descending)
        Collections.sort(availableCharacters, new Best.ReliabilityComparator());

        // minimum cost - this will always be the cost of the available
        // character with the greatest reliability
        double cmin = charCosts[availableCharacters.get(0).getCharacterId() - 1];

        List<Character> unsuitableCharacters = new ArrayList<Character>();

        charLoop: for (Character ch : availableCharacters) {

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

            List<Attribute> charAttributes = dataset.getAllAttributesForCharacter(ch.getCharacterId());

            // examine taxon to be diagnosed or separated first
            boolean[] taxonToSeparateStatePresence = new boolean[totalNumStates];
            int ndgSum = 1;

            for (Attribute attr : charAttributes) {
                Item taxon = attr.getItem();

                // Skip any attributes that pertain to taxa that are not
                // available
                if (!availableTaxa.contains(taxon)) {
                    continue;
                }

                if (attr.isUnknown() || attr.isInapplicable()) {
                    unsuitableCharacters.add(ch);
                    continue charLoop;
                }
                
                if (ch.getCharacterId() == 291) {
                    System.out.println(attr);
                }

                Pair<boolean[], Integer> statePresencePair = Best.getStatePresenceForAttribute(attr, totalNumStates, OrderingType.BEST, null);

                boolean[] statePresence = statePresencePair.getFirst();
                int numStatesPresent = statePresencePair.getSecond();

                // work out size of character subgroups.
                for (int i = 0; i < totalNumStates; i++) {
                    if (statePresence[i] == true) {
                        subgroupsNumTaxa[i]++;

                        // frequency of items with current state of current
                        // character
                        // double stateFrequency = 1.0 / (double)
                        // numStatesPresent;
                        // stateFrequency += subgroupFrequencies[i];
                        // subgroupFrequencies[i] = stateFrequency;
                        subgroupFrequencies[i]++;
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

            for (int i = 0; i < totalNumStates; i++) {
                int numTaxaInSubgroup = subgroupsNumTaxa[i];

                if (numTaxaInSubgroup == sumNumTaxaInSubgroups) {
                    // character is unsuitable if it divides the characters
                    // into a
                    // single
                    // subgroup
                    unsuitableCharacters.add(ch);
                    continue;
                } else {
                    if (numTaxaInSubgroup == numAvailableTaxa) {
                        numSubgroupsSameSizeAsOriginalGroup++;
                    }

                    if (subgroupsNumTaxa[i] > 0) {
                        sup0 += (subgroupFrequencies[i] * Best.log2(subgroupsNumTaxa[i]));
                    }
                }
            }

            boolean isControllingChar = !ch.getDependentCharacters().isEmpty();
            // TODO what is this test for???
            if (!isControllingChar && (totalNumSubgroups == numSubgroupsSameSizeAsOriginalGroup || (sumNumTaxaInSubgroups > numAvailableTaxa && numSubgroupsSameSizeAsOriginalGroup == totalNumStates))) {
                unsuitableCharacters.add(ch);
                continue;
            }

            sup0 = sup0 / sumSubgroupsFrequencies;

            if (numAvailableTaxa > 1 && sumNumTaxaInSubgroups > numAvailableTaxa) {
                dupf = varw * (1 + 100 * numSubgroupsSameSizeAsOriginalGroup) * (sumNumTaxaInSubgroups - numAvailableTaxa) * ((numAvailableTaxa + 8) / (numAvailableTaxa * Best.log2(numAvailableTaxa)));
            } else {
                dupf = 0;
            }

            sep = -sup0 + Best.log2(numAvailableTaxa);

            // handle rounding errors
            if (Math.abs(sep) <= minimumSeparatingPower) {
                sep = 0.0;
            }

            // don't display controlling characters with 0 separation
            if (isControllingChar && sep == 0) {
                unsuitableCharacters.add(ch);
                continue;
            }

            sup = sup0 + dupf;

            su = charCosts[ch.getCharacterId() - 1] + cmin * sup;

            if (ch.getCharacterId() == 17) {
                System.out.println("Character: " + ch.getCharacterId());
                System.out.println("su:" + su);
                System.out.println("cost: " + charCosts[ch.getCharacterId() - 1]);
                System.out.println("cmin " + cmin);
                System.out.println("sup " + sup);
                System.out.println("sup0 " + sup0);
                System.out.println("dupf " + dupf);
                System.out.println("numsubgroupssamesizeasoriginalgroup: " + numSubgroupsSameSizeAsOriginalGroup);
                System.out.println("sumtaxainsubgroups: " + sumNumTaxaInSubgroups);
                System.out.println("numavailabletaxa: " + numAvailableTaxa);
                System.out.println("subgroupfrequencies " + ArrayUtils.toString(subgroupFrequencies));
                System.out.println("subgroupsNumTaxa " + ArrayUtils.toString(subgroupsNumTaxa));
                System.out.println("sumsubgroupsfrequencies " + sumSubgroupsFrequencies);
                System.out.println("sumnumtaxainsubgroups " + sumNumTaxaInSubgroups);
                System.out.println();
            }

            sepVals[ch.getCharacterId() - 1] = sep;
            suVals[ch.getCharacterId() - 1] = su;
        }

        availableCharacters.removeAll(unsuitableCharacters);

        List<Character> sortedChars = new ArrayList<Character>(availableCharacters);
        Collections.sort(sortedChars, new Comparator<Character>() {

            @Override
            public int compare(Character c1, Character c2) {
                double suValC1 = suVals[c1.getCharacterId() - 1];
                double suValC2 = suVals[c2.getCharacterId() - 1];

                if (suValC1 == suValC2) {
                    return Integer.valueOf(c1.getCharacterId()).compareTo(Integer.valueOf(c2.getCharacterId()));
                } else {
                    return Double.valueOf(suValC1).compareTo(Double.valueOf(suValC2));
                }
            }
        });

        for (Character ch : sortedChars) {
            retMap.put(ch, suVals[ch.getCharacterId() - 1]);
        }

        return retMap;
    }

}
