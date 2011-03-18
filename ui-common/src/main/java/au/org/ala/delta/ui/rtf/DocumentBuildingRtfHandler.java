package au.org.ala.delta.ui.rtf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import au.org.ala.delta.rtf.AttributeValue;
import au.org.ala.delta.rtf.CharacterAttributeType;
import au.org.ala.delta.rtf.RTFHandlerAdapter;

/**
 * This class handles events from the RTFReader to build a StyledDocument suitable for
 * display by the StyledEditorKit. 
 */
public class DocumentBuildingRtfHandler extends RTFHandlerAdapter {

	/** Buffers text until it is ready to be inserted into the Document */
	private StringBuilder _textBuffer;
	/** The set of attributes to be applied to the text in the _textBuffer */
	private MutableAttributeSet _currentAttributes;
	
	/** The document we are building */
	private DefaultStyledDocument _document;
	
	/** Handles for RTF attributes */
	private Map<String, AttributeHandler> _attributeHandlers = new HashMap<String, AttributeHandler>();
	
	public void configureAttributeHandlers() {
		_attributeHandlers.put(CharacterAttributeType.Bold.keyword(), new AttributeHandler(StyleConstants.Bold));
		_attributeHandlers.put(CharacterAttributeType.Italics.keyword(), new AttributeHandler(StyleConstants.Italic));
		_attributeHandlers.put(CharacterAttributeType.Underline.keyword(), new AttributeHandler(StyleConstants.Underline));
		_attributeHandlers.put(CharacterAttributeType.Subscript.keyword(), new AttributeHandler(StyleConstants.Subscript));
		_attributeHandlers.put(CharacterAttributeType.Superscript.keyword(), new AttributeHandler(StyleConstants.Superscript));
	}
	
	/**
	 * Knows how to convert an RTF character attribute into a StyledDocument attribute.
	 */
	public static class AttributeHandler {
		private Object _styleAttribute;
		public AttributeHandler(Object styleAttribute) {
			_styleAttribute = styleAttribute;
		}
		
		public void handleAttribute(AttributeValue attr, MutableAttributeSet newAttributes) {
			newAttributes.addAttribute(_styleAttribute, Boolean.valueOf(!attr.hasParam()));			
		}
	}
	
	@Override
	public void onKeyword(String keyword, boolean hasParam, int param) {
		if (keyword.equals("par")) {
			_textBuffer.append("\n");
		}
	}
	
	public DocumentBuildingRtfHandler(DefaultStyledDocument document) {
		configureAttributeHandlers();
		_currentAttributes = new SimpleAttributeSet();
		_document = document;
		_textBuffer = new StringBuilder();
	}

	private char _previousChar;
	@Override
	public void onTextCharacter(char ch) {

		if (ch == 0) {
			return;
		}
		// Convert \r to \n as the editor pane ignores \r.  Not sure what is 
		// happening to the \n's... they don't seem to be coming through.
		if (ch != '\r' ) {
			if (_previousChar == '\r' && ch != '\n') {
				_textBuffer.append('\n');
			}
			_textBuffer.append(ch);
		}
		_previousChar = ch;
	}
		
	@Override
	public void endParse() {
		
		trimTrailingWhitespace();
		appendToDocument();
	}


	private void trimTrailingWhitespace() {
		
		int pos = _textBuffer.length()-1;
		
		while ((pos >= 0) && Character.isWhitespace(_textBuffer.charAt(pos))) {
			_textBuffer.deleteCharAt(pos);
			pos--;
		}
	}

	@Override
	public void onCharacterAttributeChange(List<AttributeValue> values) {
		
		MutableAttributeSet newAttributes = new SimpleAttributeSet();
		newAttributes.addAttributes(_currentAttributes);
		
		handleAttributeChanges(values, newAttributes);
		if (!newAttributes.equals(_currentAttributes)){
			appendToDocument();
			_currentAttributes = newAttributes;
		}
	}
	
	/**
	 * Attempts to find a handler for each AttributeValue in the list.
	 * @param values the AttributeValues that have changed.
	 * @param newAttributes a container for any changes to the StyledDocument attributes that 
	 * should be applied.
	 */
	private void handleAttributeChanges(List<AttributeValue> values, MutableAttributeSet newAttributes) {
		
		for (AttributeValue attributeValue : values) {
			AttributeHandler handler = _attributeHandlers.get(attributeValue.getKeyword());
			if (handler != null) {
				handler.handleAttribute(attributeValue, newAttributes);
			}
		}
	}
	
	/**
	 * Appends the current text buffer to the end of the Document we are building with the 
	 * set of current attributes.
	 */
	private void appendToDocument() {
		appendToDocument(_textBuffer.toString());
	}

	/**
	 * Appends the supplied text to the end of the Document we are building with the 
	 * set of current attributes.
	 */
	private void appendToDocument(String text) {
		try {
			_document.insertString(_document.getLength(), text, _currentAttributes);
			_textBuffer = new StringBuilder();
		}
		catch (BadLocationException e) {
			throw new RuntimeException("Parsing the RTF document failed!", e);
		}
	}
	
}
