package au.org.ala.delta.intkey.directives;

import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.intkey.model.IntkeyCharacterOrder;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class DisplayCharacterOrderBestDirective extends IntkeyDirective {

    public DisplayCharacterOrderBestDirective() {
        super("display", "characterorder", "best");
    }

    @Override
    public int getArgType() {
        return DirectiveArgType.DIRARG_NONE;
    }

    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        return new DisplayCharacterOrderBestDirectiveInvocation();
    }

    private class DisplayCharacterOrderBestDirectiveInvocation implements IntkeyDirectiveInvocation {

        @Override
        public boolean execute(IntkeyContext context) {
            context.setCharacterOrder(IntkeyCharacterOrder.BEST);
            return true;
        }
    }

}
