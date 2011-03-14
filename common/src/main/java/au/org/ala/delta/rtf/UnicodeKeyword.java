package au.org.ala.delta.rtf;

import java.io.IOException;

/**
 * A unicode keyword knows how to handle an RTF keyword of the form:
 * 
 * <code>\\u<code point><ascii representation></code>
 * e.g. <code>\u3456? 
 * 
 * It is supposed to skip n characters after reading the parameter where n is the current value
 * of the \\uc keyword.  For this first cut, I am saying n=1, which is the default and should work for most
 * cases.
 */
public class UnicodeKeyword extends SpecialKeyword {
	public UnicodeKeyword(String keyword) {
		super(keyword);
	}
	
	/**
	 * Converts the supplied param to a code point then reads and discards the next character from the stream.
	 * Note that the RTF spec expects that parameters to keywords are 16 bit signed integers.  This means
	 * that a negative parameter is used to represent code points above 0x7FFF.  
	 * Not sure what happens to code points above FFFF though, probably not supported by RTF).
	 */
	public char[] process(int param, RTFReader reader) throws IOException {
		
		
		if ((param < 0) && (param >= Short.MIN_VALUE)) { 
			// in this case the value has been written as a signed 16 bit number.  We need to convert to an
			// unsigned value as negative code points are invalid.
			param = (short)param & 0xFFFF;
		}
		
		// Convert the code point to one or more characters.
		char[] characters = Character.toChars(param);
		
		// skip the next character.
		reader.read();
		
		return characters;
		
	}
}
