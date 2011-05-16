package au.org.ala.delta.translation.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.model.format.AttributeFormatter;
import au.org.ala.delta.translation.attribute.ParsedAttribute.CommentedValues;
import au.org.ala.delta.translation.attribute.ParsedAttribute.Values;

/**
 * Translates an Attribute into natural language.
 */
public abstract class AttributeTranslator {

	
	private Map<String, String> _separators;
	protected StringBuilder _translatedValue;
	protected AttributeFormatter _attributeFormatter;
	
	public AttributeTranslator(AttributeFormatter formatter) {
		_separators = new HashMap<String, String>();
		_separators.put("&", "and");
		_separators.put("-", "to");
		_attributeFormatter = formatter;
	}
	
	/**
	 * Translates the supplied ParsedAttribute into a natural language description of the attribute.
	 * @param attribute the attribute to translate.
	 * @return a String containing the natural language description of the attribute.
	 */
	public String translate(ParsedAttribute attribute) {
		_translatedValue = new StringBuilder();
		_translatedValue.append(characterComment(attribute.getCharacterComment()));
		
		List<CommentedValues> commentedValues = attribute.getCommentedValues();
		boolean valueOutput = false;
		for (int i=0; i<commentedValues.size(); i++) {
			String nextValue = commentedValues(commentedValues.get(i));
			
			if (valueOutput && StringUtils.isNotEmpty(nextValue)) {
				_translatedValue.append(", or ");
			}
			valueOutput = valueOutput | StringUtils.isNotEmpty(nextValue);
			_translatedValue.append(nextValue);
			
		}
		
		return _translatedValue.toString().trim();
	}
	
	public String commentedValues(CommentedValues commentedValues) {
	
		StringBuilder output = new StringBuilder();
		
		// Special case - if an attribute is marked not applicable and has a comment
		// the "not applicable" is omitted from the description.
		if (!isInapplicableWithComment(commentedValues)) {
			 output.append(values(commentedValues.getValues()));
		}
		output.append(comment(commentedValues.getComment()));
		return output.toString();
	}
	
	private boolean isInapplicableWithComment(CommentedValues commentedValues) {
		
		Values values = commentedValues.getValues();
		String comment = commentedValues.getComment();
		return(StringUtils.isNotEmpty(comment) && 
				values.getNumValues() == 1 && "-".equals(values.getValue(0)));
			
	}
	
	public String characterComment(String comment) {
		StringBuilder output = new StringBuilder();
		comment = _attributeFormatter.formatComment(comment);
		if (StringUtils.isNotEmpty(comment)) {
			output.append(comment);
			output.append(" ");
		}
		return output.toString();
	}
	
	public String comment(String comment) {
		comment = _attributeFormatter.formatComment(comment);
		StringBuilder output = new StringBuilder();
		if (StringUtils.isNotEmpty(comment)) {
			output.append(" ");
			output.append(comment);
		}
		return output.toString();
	}
	
	protected String values(Values values) {
		if (values == null) {
			return "";
		}
		List<String> valueStrings = values.getValues();
		if (valueStrings.isEmpty()) {
			return "";
		}

		StringBuilder output = new StringBuilder();
		
		if ("&".equals(values.getSeparator())) {
			
			output.append(translateAndSeparatedValues(valueStrings));
		} 
		else if ("-".equals(values.getSeparator())) {
			output.append(translateRangeSeparatedValues(valueStrings));
		}
		else {
			String value = getTranslatedValue(valueStrings.get(0));
			
			if (StringUtils.isNotEmpty(value)) {
				output.append(value);
			}
		}
		return output.toString();
		
	}

	protected String translateRangeSeparatedValues(List<String> valueStrings) {
		
		StringBuilder output = new StringBuilder();
		List<String> translatedValues = translateValues(valueStrings);
		
		if (!translatedValues.isEmpty()) {
			for (int i=0; i<translatedValues.size()-1; i++) {
				String value = translatedValues.get(i);
				
				output.append(value);
				
				output.append(rangeSeparator());
			}
			output.append(translatedValues.get(translatedValues.size()-1));
		}
		return output.toString();
	}

	protected String translateAndSeparatedValues(List<String> valueStrings) {
		
		StringBuilder output = new StringBuilder();
		
		List<String> translatedValues = translateValues(valueStrings);
		if (!translatedValues.isEmpty()) {
			output.append(translatedValues.get(0));
			for (int i=1; i<translatedValues.size(); i++) {
				
				if (translatedValues.size() > 2) {
					output.append(",");
				}
				output.append(" ");
				if (i == translatedValues.size()-1) {
					output.append("and ");
				}
				output.append(translatedValues.get(i));
				
			}
		}
		return output.toString();
	}
	
	protected List<String> translateValues(List<String> valueStrings) {
		List<String> translatedValues = new ArrayList<String>();
		for (int i=0; i<valueStrings.size(); i++) {
			String value = getTranslatedValue(valueStrings.get(i));
			if (StringUtils.isNotEmpty(value))	{
				translatedValues.add(value);
			}
		}
		return translatedValues;
	}
	
	protected String getTranslatedValue(String value) {
		
		if ("V".equals(value)) {
			return "variable";
		}
		if ("U".equals(value)) {
			return "unknown";
		}
		if ("-".equals(value)) {
			return "not applicable";
		}
		return translateValue(value);
	}
	
	public abstract String rangeSeparator();
	
	public abstract String translateValue(String value);
	
}
