package au.org.ala.delta.directives;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.TypeSettingMark;

/**
 * Tests the TypesettingMarks class.
 */
public class TypesettingMarksTest extends TestCase {

	private DeltaContext _context;
	private TypeSettingMarks _directive;
	
	@Before
	public void setUp() {
		_context = new DeltaContext();
		_directive = new TypeSettingMarks();
	}
	
	/**
	 * Tests processing of the directive with correct data and delimiter.
	 */
	@Test
	public void testSingleMarkWithDelimiter() throws Exception {
		String data = " ! \n #1. <test> !mark 1!";
		_directive.process(_context, data);
		
		TypeSettingMark mark = _context.getTypeSettingMark(1);
		assertEquals(1, mark.getMarkNumber());
		assertEquals("mark 1", mark.getMark());
		assertEquals(false, mark.getAllowLineBreaks());
	}
	
	/**
	 * Tests processing of the directive with correct data and delimiter.
	 */
	@Test
	public void testMultipleMarksWithDelimiter() throws Exception {
		String data = " ! \n #1. <test> !mark 1!\n#2. ! mark 2!\n";
		_directive.process(_context, data);
		
		TypeSettingMark mark = _context.getTypeSettingMark(1);
		assertEquals(1, mark.getMarkNumber());
		assertEquals("mark 1", mark.getMark());
		assertEquals(false, mark.getAllowLineBreaks());
		
		mark = _context.getTypeSettingMark(2);
		assertEquals(2, mark.getMarkNumber());
		assertEquals("mark 2", mark.getMark());
		assertEquals(true, mark.getAllowLineBreaks());
	}
	
	/**
	 * Tests processing of the directive with DELTA special chars inside the mark.
	 */
	@Test
	public void testSpecialCharactersInMark() throws Exception {
		String data = " ! \n #1. <test> !mark <comment> #1 1!\n#2. ! mark 2!\n";
		_directive.process(_context, data);
		
		TypeSettingMark mark = _context.getTypeSettingMark(1);
		assertEquals(1, mark.getMarkNumber());
		assertEquals("mark <comment> #1 1", mark.getMark());
		assertEquals(false, mark.getAllowLineBreaks());
		
		mark = _context.getTypeSettingMark(2);
		assertEquals(2, mark.getMarkNumber());
		assertEquals("mark 2", mark.getMark());
		assertEquals(true, mark.getAllowLineBreaks());
	}
	
	/**
	 * Tests processing of the directive with correct data and no delimiter.
	 */
	@Test
	public void testMultipleMarksWithNoDelimiter() throws Exception {
		String data = "\n #1. <test> mark 1\n#2.  mark 2\n";
		_directive.process(_context, data);
		
		TypeSettingMark mark = _context.getTypeSettingMark(1);
		assertEquals(1, mark.getMarkNumber());
		assertEquals("mark 1", mark.getMark());
		assertEquals(false, mark.getAllowLineBreaks());
		
		mark = _context.getTypeSettingMark(2);
		assertEquals(2, mark.getMarkNumber());
		assertEquals("mark 2", mark.getMark());
		assertEquals(false, mark.getAllowLineBreaks());
	}
	
	
	/**
	 * Tests processing of the directive with correct data and an invalid
	 * delimiter.
	 */
	@Test
	public void testTypeSettingMarksWithInvalidDelimiter() throws Exception {

		String[] invalidDelimiters = new String[] { "*", "#", "<", ">" };

		for (String delimeter : invalidDelimiters) {
			String data = " " + delimeter + "\n #1. ";

			try {
				_directive.process(_context, data);
				fail("Invalid delimeter should have caused an exception");
			} catch (Exception e) {
			}

		}

	}
	
}
