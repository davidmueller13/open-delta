package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.FileOutputDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class FileOutputDirective extends NewIntkeyDirective {

    public FileOutputDirective() {
        super(false, "file", "output");
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        arguments.add(new FileArgument("file", "Files (*.out)", null, Arrays.asList(new String[] { "out" }), true));
        return arguments;
    }

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList() {
        return null;
    }

    @Override
    protected IntkeyDirectiveInvocation buildCommandObject() {
        return new FileOutputDirectiveInvocation();
    }

}
