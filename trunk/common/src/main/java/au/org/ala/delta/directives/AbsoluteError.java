package au.org.ala.delta.directives;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.directives.args.DirectiveArguments;

/**
 * Processes the ABSOLUTE ERROR directive.
 * @see http://delta-intkey.com/www/uguide.htm#_*ABSOLUTE_ERROR_
 */
public class AbsoluteError extends AbstractCharacterListDirective<DeltaContext, Double> {

	public AbsoluteError() {
		super("absolute", "error");
	}
	
	@Override
	protected void addArgument(DirectiveArguments args, int charIndex, String value) {
		args.addNumericArgument(charIndex, value);
	}

	@Override
	protected Double interpretRHS(DeltaContext context, String rhs) {
		return Double.parseDouble(rhs);
	}

	@Override
	protected void processCharacter(DeltaContext context, int charIndex, Double error) {
		context.setAbsoluteError(charIndex, error);
	}

	@Override
	public int getArgType() {
		return DirectiveArgType.DIRARG_CHARREALLIST;
	}
	
	@Override
    public int getOrder() {
    	return 4;
    }

}
