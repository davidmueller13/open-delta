package au.org.ala.delta.directives.validation;

import java.text.ParseException;

public class DirectiveException extends ParseException {

	private static final long serialVersionUID = -1730496606394420737L;

	public DirectiveException(String s, long errorOffset) {
		super(s, (int)errorOffset);
	}
	
	public DirectiveException(DirectiveError error, long errorOffset) {
		super(error.getMessage(), (int)errorOffset);
	}
	
	public boolean isFatal() {
		return true;
	}

	
}
