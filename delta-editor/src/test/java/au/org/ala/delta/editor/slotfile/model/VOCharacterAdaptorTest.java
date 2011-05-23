package au.org.ala.delta.editor.slotfile.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaTestCase;
import au.org.ala.delta.editor.slotfile.DeltaVOP;
import au.org.ala.delta.editor.slotfile.VOCharBaseDesc;
import au.org.ala.delta.editor.slotfile.VOCharTextDesc;

/**
 * Tests the VOCharacterAdaptor class.
 */
public class VOCharacterAdaptorTest extends DeltaTestCase {

	/** Holds the instance of the class we are testing */
	private DeltaVOP _vop;
	
	@Before
	public void setUp() throws Exception {
		File f = copyURLToFile("/SAMPLE.DLT");
			
		_vop = new DeltaVOP(f.getAbsolutePath(), false);
	}

	@After
	public void tearDown() throws Exception {
		if (_vop != null) {
			_vop.close();
		}
		super.tearDown();
	}
	
	/**
	 * Tests the addState operation.
	 */
	@Test
	public void testAddState() {
		VOCharacterAdaptor adaptor = getCharacterAdaptor(79);
		assertEquals(6, adaptor.getNumberOfStates());
		
		adaptor.addState();
		assertEquals(7, adaptor.getNumberOfStates());
		
		assertEquals("", adaptor.getStateText(7));
	}
	
	/**
	 * Tests the moveState operation.
	 */
	@Test
	public void testMoveState() {
		VOCharacterAdaptor adaptor = getCharacterAdaptor(79);
		assertEquals(6, adaptor.getNumberOfStates());
		
		List<String> states = new ArrayList<String>();
		for (int i=0; i<adaptor.getNumberOfStates(); i++) {
			states.add(adaptor.getStateText(i+1));
		}
		
		adaptor.moveState(6, 4);
		
		for (int i=0; i<3; i++) {
			assertEquals(states.get(i), adaptor.getStateText(i+1));
		}
		assertEquals(states.get(3), adaptor.getStateText(5));
		assertEquals(states.get(4), adaptor.getStateText(6));
		assertEquals(states.get(5), adaptor.getStateText(4));
	}
	
	/**
	 * Creates and returns a VOCharacterAdaptor for the supplied character number.
	 */
	protected VOCharacterAdaptor getCharacterAdaptor(int charNumber) {
		
		return new VOCharacterAdaptor(_vop, getCharBaseDesc(charNumber), getCharTextDesc(charNumber));
	}
	
	protected VOCharBaseDesc getCharBaseDesc(int charNumber) {
		int id = _vop.getDeltaMaster().uniIdFromCharNo(charNumber);
		VOCharBaseDesc charBaseDesc = (VOCharBaseDesc)_vop.getDescFromId(id);
		
		return charBaseDesc;
	}
	
	protected VOCharTextDesc getCharTextDesc(int charNumber) {
		
		int id = _vop.getDeltaMaster().uniIdFromCharNo(charNumber);
		VOCharBaseDesc charBaseDesc = (VOCharBaseDesc)_vop.getDescFromId(id);
		
		VOCharTextDesc textDesc = charBaseDesc.readCharTextInfo(0, (short) 0);
		
		return textDesc;
	}
	
	
	
}
