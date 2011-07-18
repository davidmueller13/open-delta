package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.math.IntRange;

import au.org.ala.delta.intkey.directives.invocation.DifferencesDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.intkey.model.MatchType;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;

public class DifferencesDirective extends IntkeyDirective {

    public DifferencesDirective() {
        super("differences");
    }

    @Override
    public int getArgType() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        IntkeyDataset ds = context.getDataset();

        // Match settings default to those set on the context
        boolean matchUnknowns = context.getMatchUnkowns();
        boolean matchInapplicables = context.getMatchInapplicables();
        MatchType matchType = context.getMatchType();

        boolean omitTextCharacters = false;

        List<Item> taxa = new ArrayList<Item>();
        List<Character> characters = new ArrayList<Character>();

        List<String> taxaTokens = new ArrayList<String>();
        List<String> characterTokens = new ArrayList<String>();

        boolean includeSpecimen = false;

        if (data != null && data.trim().length() > 0) {
            List<String> tokens = ParsingUtils.tokenizeDirectiveCall(data);

            boolean processingTaxa = true;
            boolean inBracket = false;

            for (String token : tokens) {
                if (token.equalsIgnoreCase("/O")) {
                    matchType = MatchType.OVERLAP;
                } else if (token.equalsIgnoreCase("/S")) {
                    matchType = MatchType.SUBSET;
                } else if (token.equalsIgnoreCase("/E")) {
                    matchType = MatchType.EXACT;
                } else if (token.equalsIgnoreCase("/U")) {
                    matchUnknowns = true;
                } else if (token.equalsIgnoreCase("/I")) {
                    matchInapplicables = true;
                } else if (token.equalsIgnoreCase("/X")) {
                    omitTextCharacters = true;
                } else if (token.equals("(")) {
                    if (processingTaxa) {
                        inBracket = true;
                    } else {
                        // throw exception
                    }
                } else if (token.equals(")")) {
                    if (processingTaxa && inBracket) {
                        inBracket = false;
                        processingTaxa = false;
                    } else {
                        // throw exception
                    }
                } else {
                    if (processingTaxa) {
                        taxaTokens.add(token);
                    } else {
                        characterTokens.add(token);
                    }
                }
            }

            for (String taxonToken : taxaTokens) {
                IntRange range = ParsingUtils.parseIntRange(taxonToken);
                if (range != null) {
                    for (int i : range.toArray()) {
                        Item t = ds.getTaxon(i);
                        taxa.add(t);
                    }
                } else {
                    if (taxonToken.equalsIgnoreCase("specimen")) {
                        includeSpecimen = true;
                    } else {
                        List<Item> keywordTaxa = context.getTaxaForKeyword(taxonToken);
                        taxa.addAll(keywordTaxa);
                    }
                }
            }

            for (String characterToken : characterTokens) {
                IntRange range = ParsingUtils.parseIntRange(characterToken);
                if (range != null) {
                    for (int i : range.toArray()) {
                        Character c = ds.getCharacter(i);
                        characters.add(c);
                    }
                } else {
                    List<Character> keywordCharacters = context.getCharactersForKeyword(characterToken);
                    characters.addAll(keywordCharacters);
                }
            }
        }
        
        if (taxa.size() == 0) {
            //prompt for taxa
        }

        if (taxa.size() < 2) {
            throw new IllegalStateException("At least two taxa required for comparison");
        }
        
        if (characters.size() == 0) {
            //prompt for characters
        }
        
        Collections.sort(taxa);
        Collections.sort(characters);

        DifferencesDirectiveInvocation invoc = new DifferencesDirectiveInvocation(matchUnknowns, matchInapplicables, matchType, omitTextCharacters, includeSpecimen, characters, taxa);

        return invoc;
    }
}
