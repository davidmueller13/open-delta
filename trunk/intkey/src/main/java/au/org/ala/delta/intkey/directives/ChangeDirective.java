package au.org.ala.delta.intkey.directives;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

/**
 * The CHANGE directive - allows the user to change previously entered
 * information about the specimen being identified.
 * 
 * @author ChrisF
 * 
 */
public class ChangeDirective extends IntkeyDirective {

    public ChangeDirective() {
        super(true, "change");
    }

    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        return new UseDirective().doProcess(context, data, true);
    }

}
