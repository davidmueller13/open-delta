package au.org.ala.delta.intkey.directives;

import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.SetMatchDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.MatchType;

public class SetMatchDirective extends IntkeyDirective {

    public SetMatchDirective() {
        super("set", "match");
    }

    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {

        boolean matchUnknowns = false;
        boolean matchInapplicables = false;
        MatchType matchType = MatchType.EXACT;

        if (data == null) {
            List<Object> matchSettings = context.getDirectivePopulator().promptForMatchSettings();
            if (matchSettings == null) {
                // Null list indicates that the user cancelled the operation
                return null;
            } else {
                matchUnknowns = (Boolean) matchSettings.get(0);
                matchInapplicables = (Boolean) matchSettings.get(1);
                matchType = (MatchType) matchSettings.get(2);
            }
        } else {
            for (char c : data.toCharArray()) {
                switch (c) {
                case 'o':
                case 'O':
                    matchType = MatchType.OVERLAP;
                    break;
                case 's':
                case 'S':
                    matchType = MatchType.SUBSET;
                    break;
                case 'e':
                case 'E':
                    matchUnknowns = false;
                    matchInapplicables = false;
                    break;
                case 'u':
                case 'U':
                    matchUnknowns = true;
                    break;
                case 'i':
                case 'I':
                    matchInapplicables = true;
                    break;
                default:
                    if (!Character.isWhitespace(c)) {
                        context.getUI().displayErrorMessage("Invalid option for SET MATCH: " + c);
                        return null;
                    }
                }
            }
        }

        return new SetMatchDirectiveInvocation(matchInapplicables, matchUnknowns, matchType);
    }
}
