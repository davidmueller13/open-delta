package au.org.ala.delta.intkey.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import au.org.ala.delta.best.Best;
import au.org.ala.delta.intkey.directives.SetReliabilitiesDirective;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.util.Pair;

public class SetReliabilitiesTest extends IntkeyDatasetTestCase {
    @Test
    public void testSetReliabilities() throws Exception {
        IntkeyContext context = loadDataset("/dataset/sample/intkey.ink");
        
        List<Character> characters = context.getDataset().getCharactersAsList();

        new SetReliabilitiesDirective().parseAndProcess(context, "all,7");
        
        for (Character ch: characters) {
            assertEquals(7f, ch.getReliability());
        }
    }
    
    @Test
    public void testSetReliabilitiesThenDoBest() throws Exception {
        IntkeyContext context = loadDataset("/dataset/sample/intkey.ink");
        
        new SetReliabilitiesDirective().parseAndProcess(context, "85,5");
        
        Character ch = context.getDataset().getCharacter(85);
        
        Pair<List<Integer>, List<Integer>> availableCharactersAndTaxaNumbers = getCharacterAndTaxonNumbersForBest(context);
        List<Integer> availableCharacterNumbers = availableCharactersAndTaxaNumbers.getFirst();
        List<Integer> availableTaxaNumbers = availableCharactersAndTaxaNumbers.getSecond();
        Map<Character, Double> bestMap = Best.orderBest(context.getDataset(), availableCharacterNumbers, availableTaxaNumbers, context.getRBase(), context.getVaryWeight());
        List<Character> orderedCharList = new ArrayList<Character>(bestMap.keySet());
        
        assertEquals(ch, orderedCharList.get(0));
    }
}
