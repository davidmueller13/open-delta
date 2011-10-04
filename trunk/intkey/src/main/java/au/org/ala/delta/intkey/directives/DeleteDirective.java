package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.DeleteDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class DeleteDirective extends NewIntkeyDirective {
    public DeleteDirective() {
        super("delete");
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        arguments.add(new CharacterListArgument("characters", null, SelectionMode.KEYWORD, true));
        return arguments;
    }

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList(IntkeyContext context) {
        List<IntkeyDirectiveFlag> flags = new ArrayList<IntkeyDirectiveFlag>();
        flags.add(new IntkeyDirectiveFlag('M', "suppressUnusedCharacterWarning", false));
        return flags;
    }

    @Override
    protected IntkeyDirectiveInvocation buildCommandObject() {
        return new DeleteDirectiveInvocation();
    }
}
