package au.org.ala.delta.directives.args;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.IntRange;

import au.org.ala.delta.directives.AbstractDeltaContext;
import au.org.ala.delta.directives.AbstractStreamParser;

/**
 * The DirectiveArgsParser provides methods for parsing common formats
 * used in DELTA directives.
 */
public abstract class DirectiveArgsParser extends AbstractStreamParser {

	protected static final char MARK_IDENTIFIER = '#';
	protected static final char VALUE_SEPARATOR = ',';
	public static final char SET_VALUE_SEPARATOR = ':';
	
	protected DirectiveArguments _args;
	protected int _markedInt;
	
	public DirectiveArgsParser(AbstractDeltaContext context, Reader reader) {
		super(context, reader);
	}
	
	public DirectiveArguments getDirectiveArgs() {
		return _args;
	}
	
	protected String readFully() throws Exception {
		int next = readNext();
		StringBuilder text = new StringBuilder();
		while (next != -1) {
			text.append((char)next);
			next = readNext();
		}
		return text.toString();
	}
	
	protected void expect(char token) throws ParseException {
		expect(token, false);
	}
	
	protected void expect(char token, boolean allowEndOfStream) throws ParseException {
		if (allowEndOfStream && _currentInt < 0) {
			return;
		}
		if (_currentChar != token) {
			throw new ParseException("Invalid character found.  Expected: "+token+" Found: "+_currentChar, _position-1);
		}
	}
	
	protected BigDecimal readValue() throws ParseException {
		int startPosition = _position;
		try {
			String value = readToNextWhiteSpaceOrEnd();
			return new BigDecimal(value);
		}
		catch (Exception e) {
			throw new ParseException("Failed to read value: "+e.getMessage(), startPosition-1);
		}
	}
	
	
	protected String readToNextWhiteSpaceOrEnd() throws ParseException {
		
		StringBuilder text = new StringBuilder();
		while (_currentInt >= 0 && !Character.isWhitespace(_currentChar)) {
			text.append(_currentChar);
			readNext();
		}
		
		return text.toString();
	}
	
	/**
	 * Reads from the stream up the next character of the specified type or until the
	 * end of the stream is reached.
	 * @param character the character to read up to.
	 * @return the contents of the stream up to (but not including) the supplied character.
	 * @throws Exception if there is an error reading from the stream.
	 */
	protected String readToNext(char next) throws ParseException {
		
		StringBuilder text = new StringBuilder();
		while (_currentInt >= 0 && _currentChar != next) {
			text.append(_currentChar);
			readNext();
		}
		
		return text.toString();
	}
	
	

	protected IntRange readIds() throws ParseException {
		try {
			int first = readInteger();
			if (_currentChar == '-') {
				readNext();
				int last = readInteger();
				return new IntRange(first, last);
			}
			return new IntRange(first);
		}
		catch (Exception e) {
			throw new ParseException(e.getMessage(), _position-1);
		}
	}
	
	protected List<Integer> readSet() throws ParseException {
		List<Integer> values = new ArrayList<Integer>();
		while (_currentInt > 0 && !Character.isWhitespace(_currentChar)) {
			if (_currentChar == SET_VALUE_SEPARATOR) {
				readNext();
			}
			IntRange ids = readIds();
			for (int i : ids.toArray()) {
				values.add(i);
			}
		}
		
		return values;
	}
	
	protected void readValueSeparator() throws ParseException {
		expect(VALUE_SEPARATOR);
		// consume the comma.
		readNext();
	}
	
	protected String readItemDescription() throws ParseException {
		expect(MARK_IDENTIFIER);
		
		readNext();
		char previousChar = (char)0;
		StringBuilder id = new StringBuilder();
		while (!(previousChar == '/' && (_currentChar == ' ' || _currentChar == '\r' || _currentChar == '\n'))) {
			id.append(_currentChar);
			previousChar = _currentChar;
			readNext();
		}
		// Delete the '/'
		if (id.charAt(id.length()-1) != '/') {
			throw new ParseException("Unterminated item description", _position);
		}
		id.deleteCharAt(id.length()-1);
		
	    return id.toString().trim();
	}
	
	protected int readListId() throws ParseException {
		expect(MARK_IDENTIFIER);
		
		readNext();
		
		int id = readInteger();
		
		expect('.');
	    readNext();  // consume the . character.
	    return id;
	}
	
	
	protected void mark() throws ParseException {
		try {
			_reader.mark(2);
			_markedInt = _currentInt;
		}
		catch (IOException e) {
			throw new ParseException(e.getMessage(), _position);
		}
	}
	
	protected void reset() throws ParseException {
		try {
			_reader.reset();
			_currentChar = (char)_markedInt;
			_currentInt = _markedInt;
		}
		catch (IOException e) {
			throw new ParseException(e.getMessage(), _position);
		}
	}
}
