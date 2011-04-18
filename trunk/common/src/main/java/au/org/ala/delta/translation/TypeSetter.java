package au.org.ala.delta.translation;

import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.rtf.RTFUtils;
import au.org.ala.delta.translation.Words.Word;

/**
 * The TypeSetter is responsible for formatting output produced by the NaturalLanguageTranslator.
 */
public class TypeSetter {

	private int _printWidth = 80;
	private PrintStream _output;
	private int _currentIndent;
	private boolean _capitalise;
	
	private StringBuilder _outputBuffer;
	private boolean _indented;
	
	public TypeSetter(PrintStream output, int lineWidth) {
		_output = output;
		_outputBuffer = new StringBuilder();
		_indented = false;
		_printWidth = lineWidth;

	}

	public void insertTypeSettingMarks(int number) {

	}

	public void setIndent(int numSpaces) {

		_currentIndent = numSpaces;
	}

	/**
	 * Indents the default amount for the current output type. (takes a luntype
	 * and a number of spaces)
	 */
	public void indent() {
		if (_indented) {
			return;
		}
		if (_currentIndent <= (Math.abs(_printWidth) - 20) ) {
			for (int i = 0; i < _currentIndent; i++) {
				_outputBuffer.append(' ');
			}
		}
		_indented = true;
	}

	public void endLine() {
		printBufferLine(false);
	}
	
	public void writeBlankLines(int numLines, int requiredNumLinesLeftOnPage) {
		
		printBufferLine();
		for (int i = 0; i < numLines; i++) {
			_output.println();
		}
	}
	
	public void printBufferLine() {
		printBufferLine(false);
	}
	
	public void printBufferLine(boolean indentNewLine) {
	
		_output.println(_outputBuffer.toString());
		_indented = false;
		_outputBuffer = new StringBuilder();
		if (indentNewLine) {
			indent();
		}
	}
	
	public void writeJustifiedText(String text, int completionAction, boolean inHtmlRtf) {
		
		text = text.trim();
		
		if (_capitalise) {
			text = capitaliseFirstWord(text);
		}
		
		if (willFitOnLine() == false) {
			printBufferLine();
		}
		
		// Insert a space if one is required.
		if (_outputBuffer.length() > 0 && lastCharInBuffer() != ' ' && !isPunctuationMark(text)) {
			_outputBuffer.append(' ');
		}
		
		_outputBuffer.append(text);
		
		while (willFitOnLine() == false) {
			int numSpaces = numLeadingSpaces(_outputBuffer);
			int wrappingPos = _outputBuffer.lastIndexOf(" ", _printWidth);
			if (wrappingPos <= numSpaces) {
				wrappingPos = _printWidth;
			}
			
			String trailingText = _outputBuffer.substring(wrappingPos);
			_outputBuffer.delete(wrappingPos, _outputBuffer.length());
			printBufferLine();
			
			_outputBuffer.append(trailingText.trim());
		}
		complete(completionAction);
		
	}
	
	private boolean isPunctuationMark(String text) {
		return ".".equals(text) || ",".equals(text) || ";".equals(text);
	}
	
	private void complete(int completionAction) {
		if ((completionAction == 0) && (willFitOnLine())) {
			_outputBuffer.append(' ');
		}
		else if (completionAction > 0) {
			writeBlankLines(completionAction, 0);
		}
	}
	
	/**
	 * Returns the number of leading spaces in the supplied text.
	 * @param text the text to count leading spaces of.
	 * @return the number of leading spaces in the supplied text or zero if the parameter is null.
	 */
	private int numLeadingSpaces(CharSequence text) {
		if ((text == null) || (text.length() == 0)) {
			return 0;
		}
		int numSpaces = 0;
		while (text.charAt(numSpaces) == ' ') {
			numSpaces++;
		}
		return numSpaces;
	}

	/**
	 *  Capitalises the first word in the supplied text (which may contain RTF markup)
	 *	the first letter of the word is preceded by a '|'.
	 * @param text the text to capitalise.
	 * @return the text with the first word capitalised.
	 */
	public String capitaliseFirstWord(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		//
		StringBuilder tmp = new StringBuilder();
		tmp.append(text);
		int index = 0;
		if (tmp.charAt(0) == '\\') {
			
			if (tmp.length() > 1) {
				index++;
				char next = tmp.charAt(index);
				if (next != '\\' && next != '-') {
					
					while (index < text.length() && Character.isLetterOrDigit(tmp.charAt(index))) {
						index++;
					}
				}
			}
		}
		while (index < text.length() && !Character.isLetterOrDigit(tmp.charAt(index))) {
			index++;
		}
		if (index < text.length() && Character.isLetter(tmp.charAt(index))) {
			if ((index == 0) || (tmp.charAt(index-1) != '|')) {
				tmp.setCharAt(index, Character.toUpperCase(tmp.charAt(index)));
			}
			else if (tmp.charAt(index-1) == '|') {
				tmp.deleteCharAt(index-1);
			}
		}
		_capitalise = false;
		return tmp.toString();
	}
	
	
	protected void newLine() {
		_output.println();
	}


	public void newParagraph() {
		newLine();
		indent();
		
	}
	enum TypeSetting {
		ADD_TYPESETTING_MARKS, DO_NOTHING, REMOVE_EXISTING_TYPESETTINGMARKS
	};

	private TypeSetting _typeSettingMode = TypeSetting.DO_NOTHING;
	
	public void writeJustifiedOutput(String text, int completionAction, boolean inHtmlRtf, boolean encodeXmlBrackets) {
		if (_typeSettingMode == TypeSetting.REMOVE_EXISTING_TYPESETTINGMARKS) {
			text = RTFUtils.stripFormatting(text);
		}
		else if (_typeSettingMode == TypeSetting.ADD_TYPESETTING_MARKS) {
			// Convert number ranges to use an en dash.
			
			// replace < and > with &lt; and %gt;
			
		}
		
		writeJustifiedText(text, completionAction, inHtmlRtf);
		
	}
	
	private int bufferIndex() {
		return _outputBuffer.length()-1;
	}
	
	private boolean willFitOnLine() {
		return bufferIndex() < Math.abs(_printWidth);
	}
	
	
	private char lastCharInBuffer() {
		if (_outputBuffer.length() == 0) {
			return 0;
		}
		return _outputBuffer.charAt(_outputBuffer.length()-1);
	}
	
	public void captialiseNextWord() {
		_capitalise = true;
		
	}

	public void insertPunctuationMark(Word word) {
		
		String punctuationMark = Words.word(word);
		assert punctuationMark.length() == 1;
		
		if (lastCharInBuffer() != punctuationMark.charAt(0)) {
			writeFromVocabulary(word, -1);
		}
	}
	
	public void writeFromVocabulary(Word word, int completionAction) {
		writeJustifiedText(Words.word(word), completionAction, false);
	}

}
