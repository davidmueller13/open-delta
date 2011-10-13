package au.org.ala.delta.intkey.directives.invocation;

import au.org.ala.delta.intkey.model.DiagType;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class SetDiagTypeTaxaDirectiveInvocation extends IntkeyDirectiveInvocation {

    @Override
    public boolean execute(IntkeyContext context) {
        context.setDiagType(DiagType.TAXA);
        return true;
    }
}