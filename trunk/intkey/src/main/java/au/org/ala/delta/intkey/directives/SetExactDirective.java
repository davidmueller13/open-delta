package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.SetExactDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class SetExactDirective extends NewIntkeyDirective {

    public SetExactDirective() {
        super("set", "exact");
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        arguments.add(new CharacterListArgument("characters", null, SelectionMode.KEYWORD, false));
        return arguments;
    }

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList(IntkeyContext context) {
        return null;
    }

    @Override
    protected IntkeyDirectiveInvocation buildCommandObject() {
        return new SetExactDirectiveInvocation();
    }

}
