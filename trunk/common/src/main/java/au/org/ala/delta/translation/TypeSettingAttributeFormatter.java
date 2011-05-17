package au.org.ala.delta.translation;

import au.org.ala.delta.model.format.AttributeFormatter;

/**
 * Extends the AttributeFormatter to recognise numeric ranges in comments
 * and format the separator correctly.
 */
public class TypeSettingAttributeFormatter extends AttributeFormatter {

	public TypeSettingAttributeFormatter() {
		super(false, false);
	}

	@Override
	public String formatComment(String comment) {
		comment = super.formatComment(comment);
		comment = comment.replaceAll("(\\d)-(\\d)", "$1\u2013$2");
		
		return comment;
	}
}
