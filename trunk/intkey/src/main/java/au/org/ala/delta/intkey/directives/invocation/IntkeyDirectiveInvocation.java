package au.org.ala.delta.intkey.directives.invocation;

import au.org.ala.delta.intkey.model.IntkeyContext;

/**
 * Command pattern. Represents a call to one of the Intkey
 * directives with specific arguments.
 * @author Chris
 *
 */
public interface IntkeyDirectiveInvocation {

    void setStringRepresentation(String stringRepresentation);
    boolean execute(IntkeyContext context) throws IntkeyDirectiveInvocationException;
    
}
