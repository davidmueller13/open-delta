package au.org.ala.delta.intkey.directives;

import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.intkey.model.IntkeyContext;

public abstract class IntkeyDirective extends AbstractDirective<IntkeyContext> {
    
    public IntkeyDirective(String... controlWords) {
        super(controlWords);
    }

    @Override
    public final void process(IntkeyContext context, String data) throws Exception {
        IntkeyDirectiveInvocation invoc = doProcess(context, data);
        
        if (invoc != null) {
            context.executeDirective(invoc);
        }
    }
    
    
    
    protected abstract IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception;

}
