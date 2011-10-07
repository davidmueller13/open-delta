package au.org.ala.delta.intkey.directives;

import java.util.Queue;

import au.org.ala.delta.intkey.model.IntkeyContext;

public class IntegerArgument extends IntkeyDirectiveArgument<Integer> {

    public IntegerArgument(String name, String promptText, int initialValue) {
        super(name, promptText, initialValue);
    }

    @Override
    public Integer parseInput(Queue<String> inputTokens, IntkeyContext context, String directiveName, StringBuilder stringRepresentationBuilder) throws IntkeyDirectiveParseException {
        String token = inputTokens.poll();

        if (token == null || token.equals(DEFAULT_DIALOG_WILDCARD)) {
            token = context.getDirectivePopulator().promptForString(_promptText, Integer.toString(_initialValue), directiveName);
        }

        if (token != null) {
            try {
                int parsedInteger = Integer.parseInt(token);
                stringRepresentationBuilder.append(" ");
                stringRepresentationBuilder.append(parsedInteger);
                return parsedInteger;
            } catch (NumberFormatException ex) {
                throw new IntkeyDirectiveParseException("Integer value required", ex);
            }
        }
        return null;
    }
}
