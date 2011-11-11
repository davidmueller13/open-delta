package au.org.ala.delta.directives;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.model.format.DirectiveTextFormatter;

/**
 * Processes the PRINT COMMENT directive.
 * @see http://delta-intkey.com/www/uguide.htm#_*PRINT_COMMENT_
 */
public class PrintComment extends AbstractTextDirective {

	public PrintComment() {
		super("print", "comment");
	}
	
	@Override
	public void process(DeltaContext context, DirectiveArguments directiveArguments) throws Exception {
		
		String comment = directiveArguments.getFirstArgumentText();
		comment = DirectiveTextFormatter.newLinesToSpace(comment);
		context.print(comment+System.getProperty("line.separator"));
	}

}
