package au.org.ala.delta.key.directives;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.AbstractIntegerDirective;
import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.directives.validation.IntegerValidator;
import au.org.ala.delta.directives.validation.PositiveIntegerValidator;

public class KeyPrintWidthDirective extends AbstractIntegerDirective {

    public KeyPrintWidthDirective() {
        super("print", "width");

    }

    @Override
    protected void processInteger(DeltaContext context, int value) throws Exception {
        if (value < 0) {
            throw new IllegalArgumentException("Print width cannot be negative");
        }
        context.getOutputFileSelector().setOutputWidth(value);
    }

    @Override
    public int getArgType() {
        return DirectiveArgType.DIRARG_INTEGER;
    }

    @Override
    protected IntegerValidator createValidator(DeltaContext context) {
        return new PositiveIntegerValidator();
    }
}
