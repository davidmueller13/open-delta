package au.org.ala.delta.intkey.directives;

import java.text.MessageFormat;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.SetMatchDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.MatchType;
import au.org.ala.delta.intkey.ui.UIUtils;

public class SetMatchDirective extends IntkeyDirective {

    private static final String OVERLAP_LETTER = "o";
    private static final String OVERLAP_WORD = "overlap";
    private static final String SUBSET_LETTER = "s";
    private static final String SUBSET_WORD = "subset";
    private static final String EXACT_LETTER = "e";
    private static final String EXACT_WORD = "exact";
    private static final String UNKNOWNS_LETTER = "u";
    private static final String UNKNOWNS_WORD = "unknowns";
    private static final String INAPPLICABLES_LETTER = "i";
    private static final String INAPPLICABLES_WORD = "inapplicables";

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
            List<String> tokens = ParsingUtils.tokenizeDirectiveCall(data);
            for (String token : tokens) {
                if (token.equalsIgnoreCase(OVERLAP_LETTER) || token.equalsIgnoreCase(OVERLAP_WORD)) {
                    matchType = MatchType.OVERLAP;
                } else if (token.equalsIgnoreCase(SUBSET_LETTER) || token.equalsIgnoreCase(SUBSET_WORD)) {
                    matchType = MatchType.SUBSET;
                } else if (token.equalsIgnoreCase(EXACT_LETTER) || token.equalsIgnoreCase(EXACT_WORD)) {
                    matchType = MatchType.EXACT;
                    matchUnknowns = false;
                    matchInapplicables = false;
                } else if (token.equalsIgnoreCase(UNKNOWNS_LETTER) || token.equalsIgnoreCase(UNKNOWNS_WORD)) {
                    matchUnknowns = true;
                } else if (token.equalsIgnoreCase(INAPPLICABLES_LETTER) || token.equalsIgnoreCase(INAPPLICABLES_WORD)) {
                    matchInapplicables = true;
                } else {
                    context.getUI().displayErrorMessage(MessageFormat.format(UIUtils.getResourceString("InvalidSetMatchOption.error"), token));
                    return null;
                }
            }
        }

        return new SetMatchDirectiveInvocation(matchInapplicables, matchUnknowns, matchType);
    }
}
