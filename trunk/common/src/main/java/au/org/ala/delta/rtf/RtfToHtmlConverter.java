package au.org.ala.delta.rtf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Converts RTF formatting to the HTML equivalent.
 */
public class RtfToHtmlConverter implements RTFHandler {
	
	
	private Map<String, String> _rtfToHtml;

    private static final String[] RTF_CHARACTER_ATTRIBUTE_CONTROL_WORDS = {
    		"b", "i", "u", "sub", "super"
    };
    
    private static final String[] RTF_KEYWORDS = {
    		 "~", "_", "-", "'", 
    		 "ldblquote", "rdblquote", "lquote", "rquote", 
    		 "endash", "emdash", "line",
    };
    
    private static final String[] HTML_CHARACTER_ATTRIBUTE_SUBSTITUTIONS = {
    		"B", "I", "", "SUB", "SUP"
    };
    
    private static final String[] HTML_KEYWORD_SUBSTITUTIONS = {
    		"&nbsp;", "-", "", "", "", 
    		"&#145;", "&#146;", "&#145;", "&#146;", 
    		"&#150;", "&#151;", "<BR>"
    };
	
	private StringBuilder _buffer;
	
	public RtfToHtmlConverter() {
		
		// These are separated as the single substitution chars arern't
		// necessarily surrounded by <>.
		_rtfToHtml = new HashMap<String, String>();
		for (int i=0; i<RTF_CHARACTER_ATTRIBUTE_CONTROL_WORDS.length; i++) {
			_rtfToHtml.put(RTF_CHARACTER_ATTRIBUTE_CONTROL_WORDS[i], HTML_CHARACTER_ATTRIBUTE_SUBSTITUTIONS[i]);
		}
		for (int i=0; i<RTF_KEYWORDS.length; i++) {
			_rtfToHtml.put(RTF_KEYWORDS[i], HTML_KEYWORD_SUBSTITUTIONS[i]);
		}
		_buffer = new StringBuilder();
	}

	@Override
	public void startParse() {
	}

	@Override
	public void onKeyword(String keyword, boolean hasParam, int param) {
		_buffer.append(_rtfToHtml.get(keyword));
	}

	@Override
	public void onHeaderGroup(String keyword, String content) {
	}

	@Override
	public void onTextCharacter(char ch) {
		_buffer.append(ch);
	}

	@Override
	public void endParse() {
	}

	public String getText() {
		return _buffer.toString();
	}

	@Override
	public void onCharacterAttributeChange(List<AttributeValue> values) {
		handleAttributeChange(values);
	}

	@Override
	public void onParagraphAttributeChange(List<AttributeValue> values) {
		handleAttributeChange(values);
	}

	private void handleAttributeChange(List<AttributeValue> values) {
		for (AttributeValue attribute : values) {
			if (attribute.hasParam()) {
				attributeEnd(attribute.getKeyword());
			}
			else {
				attributeStart(attribute.getKeyword());
			}
		}
		
	}
	
	private void attributeStart(String keyword) {
		String html = _rtfToHtml.get(keyword);
		_buffer.append("<").append(html).append(">");
	}
	
	private void attributeEnd(String keyword) {
		String html = _rtfToHtml.get(keyword);
		_buffer.append("</").append(html).append(">");
	}

	@Override
	public void startParagraph() {
	}

	@Override
	public void endParagraph() {
		_buffer.append("<p/>");
	}
}
