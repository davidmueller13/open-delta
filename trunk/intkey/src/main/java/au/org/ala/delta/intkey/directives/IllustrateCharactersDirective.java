package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.IllustrateCharactersDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.ImageDisplayMode;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class IllustrateCharactersDirective extends NewIntkeyDirective {

    public IllustrateCharactersDirective() {
        super(true, "illustrate", "characters");
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        arguments.add(new CharacterListArgument("characters", null, false, true));
        return arguments;
    }

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList() {
        return null;
    }

    @Override
    protected IntkeyDirectiveInvocation buildCommandObject() {
        return new IllustrateCharactersDirectiveInvocation();
    }

}
