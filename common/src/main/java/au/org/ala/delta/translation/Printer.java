package au.org.ala.delta.translation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.rtf.RTFUtils;
import au.org.ala.delta.translation.Words.Word;
import au.org.ala.delta.util.Utils;

/**
 * The TypeSetter is responsible for formatting output produced by the
 * NaturalLanguageTranslator.
 */
public class Printer {

    private int _printWidth = 80;
    private PrintStream _output;
    private int _paragraphIndent;
    private int _lineWrapIndent = 0;
    private boolean _useParagraphIndentOnLineWrap = false;
    private boolean _capitalise;

    private StringBuilder _outputBuffer;
    private boolean _indented;
    private boolean _indentOnLineWrap;
    private boolean _softWrap;

    public Printer(final StringBuilder buffer) {

        _output = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                buffer.append((char) b);
            }
        });
        initialise();
    }

    /**
     * Creates a new Printer that will print to the supplied PrintStream.
     * 
     * @param output
     *            the output stream to print to.
     * @param lineWidth
     *            the position at which line wrapping should occur. a lineWidth
     *            of zero means no line wrapping.
     */
    public Printer(PrintStream output, int lineWidth) {
        _output = output;
        _printWidth = lineWidth;
        initialise();
    }

    private void initialise() {
        _outputBuffer = new StringBuilder();
        _indented = false;

        _indentOnLineWrap = false;
        _softWrap = false;
    }

    public void setSoftWrap(boolean softWrap) {
        _softWrap = softWrap;
    }

    public void insertTypeSettingMarks(int number) {

    }

    public void close() {
        _output.close();
    }

    /**
     * If the indentOnLineWrap property is set to true if a statement is
     * automatically wrapped, its continuation will be indented.
     */
    public void setIndentOnLineWrap(boolean indent) {
        _indentOnLineWrap = indent;
    }

    public void setIndent(int numSpaces) {

        _paragraphIndent = numSpaces;
    }

    public void indent() {
    	indent(_paragraphIndent);
    }
    
    /**
     * Indents the default amount for the current output type. (takes a luntype
     * and a number of spaces)
     */
    public void indent(int indent) {
        if (_indented) {
            return;
        }
        if (_paragraphIndent <= (Math.abs(_printWidth) - 20)) {
            for (int i = 0; i < _paragraphIndent; i++) {
                _outputBuffer.append(' ');
            }
        }
        _indented = true;
    }

    public void endLine() {
        printBufferLine(false);
    }

    public void writeBlankLines(int numLines, int requiredNumLinesLeftOnPage) {

        if (_outputBuffer.length() > 0) {
            printBufferLine();
        }
        for (int i = 0; i < numLines; i++) {
            _output.println();
        }
    }

    public void printBufferLine() {
        printBufferLine(false);
    }

    public void printBufferLine(boolean indentNewLine) {

        int i = _outputBuffer.length() - 1;
        while (i > 0 && _outputBuffer.charAt(i) == ' ') {
            i--;
        }
        if (_outputBuffer.length() > 0) {
            _output.println(_outputBuffer.substring(0, i + 1));
            _indented = false;
            _outputBuffer = new StringBuilder();
        
            if (indentNewLine) {
            	int lineWrap = _lineWrapIndent;
            	if (_useParagraphIndentOnLineWrap) {
            		lineWrap = _paragraphIndent;
            	}
	            for (int j = 0; j < lineWrap; j++) {
	                _outputBuffer.append(' ');
	            }
            }
        }
    }

    public void writeJustifiedText(String text, int completionAction) {
        text = text.trim();
        writeJustifiedText(text, completionAction, true);
    }

    private boolean _omitNextTrailingSpace = false;

    public void writeJustifiedText(String text, int completionAction, boolean addSpaceIfRequired) {

        if (_capitalise) {
            text = capitaliseFirstWord(text);
        }

        if (willFitOnLine() == false) {
            printBufferLine(_indentOnLineWrap);
        }

        // Insert a space if one is required.
        if (addSpaceIfRequired) {
            insertTrailingSpace();
        }

        _outputBuffer.append(text);

        while (willFitOnLine() == false) {

            int wrappingPos = findWrapPosition();

            String trailingText = _outputBuffer.substring(wrappingPos);
            _outputBuffer.delete(wrappingPos, _outputBuffer.length());
            printBufferLine(_indentOnLineWrap);

            _outputBuffer.append(trailingText.trim());
        }
        complete(completionAction);
    }

    private int findWrapPosition() {
        int numSpaces = numLeadingSpaces(_outputBuffer);
        int wrappingPos = _outputBuffer.lastIndexOf(" ", _printWidth);

        if (wrappingPos <= numSpaces) {
            if (_softWrap) {
                wrappingPos = _outputBuffer.indexOf(" ", _printWidth);
                if (wrappingPos < 0) {
                    wrappingPos = _outputBuffer.length();
                }
            } else {
                wrappingPos = _printWidth;
            }
        }
        int newLinePos = _outputBuffer.indexOf("\n");
        if (newLinePos >=0 && newLinePos < wrappingPos) {
        	wrappingPos = _outputBuffer.indexOf("\n");
        	_outputBuffer.delete(wrappingPos, wrappingPos);
        }
        return wrappingPos;
    }

    public void writeTypeSettingMark(String mark) {
        writeJustifiedText(mark, -1, false);
        _omitNextTrailingSpace = true;
    }

    private void insertTrailingSpace() {
        if (_omitNextTrailingSpace) {
            _omitNextTrailingSpace = false;
            return;
        }
        if (_outputBuffer.length() > 0 && lastCharInBuffer() != ' ') {
            _outputBuffer.append(' ');
        }
    }

    private void complete(int completionAction) {
        if ((completionAction == 0) && (willFitOnLine())) {
            _outputBuffer.append(' ');
        } else if (completionAction > 0) {
            writeBlankLines(completionAction, 0);
        }
    }

    /**
     * Returns the number of leading spaces in the supplied text.
     * 
     * @param text
     *            the text to count leading spaces of.
     * @return the number of leading spaces in the supplied text or zero if the
     *         parameter is null.
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
     * Capitalises the first word in the supplied text (which may contain RTF
     * markup) the first letter of the word is preceded by a '|'.
     * 
     * @param text
     *            the text to capitalise.
     * @return the text with the first word capitalised.
     */
    public String capitaliseFirstWord(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        _capitalise = false;
        return Utils.capitaliseFirstWord(text);
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
        } else if (_typeSettingMode == TypeSetting.ADD_TYPESETTING_MARKS) {
            // Convert number ranges to use an en dash.

            // replace < and > with &lt; and %gt;

        }

        writeJustifiedText(text, completionAction);

    }

    private int bufferIndex() {
        return _outputBuffer.length() - 1;
    }

    private boolean willFitOnLine() {
        if (_printWidth == 0) {
            return true;
        }
        return bufferIndex() < Math.abs(_printWidth);
    }

    private char lastCharInBuffer() {
        if (_outputBuffer.length() == 0) {
            return 0;
        }
        return _outputBuffer.charAt(_outputBuffer.length() - 1);
    }

    public void capitaliseNextWord() {
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
    
    public void outputLine(int indent, String value, int numTrailingBlanks) {
		setIndent(indent);
		outputLine(value, numTrailingBlanks);
	}
	
    public void outputLine(String value) {
		outputLine(value, 0);
	}
	
    public void outputLine(String value, int numTrailingBlanks) {
		indent();
		writeJustifiedText(value, -1);
		printBufferLine();
		if (numTrailingBlanks > 0) {
			writeBlankLines(numTrailingBlanks, 0);
		}
	}
    
    /**
     * If a line is wrapped during output, the line wrap indent will be
     * applied if the setIndentOnLineWrap(true) method has been invoked.
     * @param indent the indent to apply in addition to the paragraph indent 
     * if a line is wrapped.
     */
    public void setLineWrapIndent(int indent) {
    	_lineWrapIndent = indent;
    }
    
    public void useParagraphIndentOnLineWrap() {
    	_useParagraphIndentOnLineWrap = true;
    }
}
