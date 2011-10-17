package au.org.ala.delta.intkey.directives;

//TODO need to prompt user for reliability value for each subcommand if the 
//value is not supplied by the user.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.SetReliabilitiesDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.model.Character;

public class SetReliabilitiesDirective extends IntkeyDirective {

    public SetReliabilitiesDirective() {
        super("set", "reliabilities");
    }

    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        String directiveName = StringUtils.join(getControlWords(), " ").toUpperCase();

        IntkeyDataset dataset = context.getDataset();

        Map<Character, Float> reliabilitiesMap = new HashMap<Character, Float>();

        if (data == null) {
            List<Character> characters = context.getDirectivePopulator().promptForCharactersByKeyword(directiveName, true, false);
            Float reliability = Float.parseFloat(context.getDirectivePopulator().promptForString("Enter reliability value", null, directiveName));
            for (Character ch : characters) {
                reliabilitiesMap.put(ch, reliability);
            }
        } else {
            List<String> subCmds = ParsingUtils.tokenizeDirectiveCall(data);

            for (String subCmd : subCmds) {
                String[] tokens = subCmd.split(",");

                String strCharacters = tokens[0];
                String strReliability = tokens[1];

                List<Character> characters = new ArrayList<Character>();

                IntRange charRange = ParsingUtils.parseIntRange(strCharacters);
                if (charRange != null) {
                    for (int index : charRange.toArray()) {
                        characters.add(dataset.getCharacter(index));
                    }
                } else {
                    characters = context.getCharactersForKeyword(strCharacters);
                }

                float reliability = Float.parseFloat(strReliability);

                for (Character ch : characters) {
                    reliabilitiesMap.put(ch, reliability);
                }
            }
        }

        return new SetReliabilitiesDirectiveInvocation(reliabilitiesMap);
    }
}
