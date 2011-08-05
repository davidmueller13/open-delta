package au.org.ala.delta.translation.intkey;

import junit.framework.TestCase;

import org.apache.commons.lang.NotImplementedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.intkey.IntkeyFile;
import au.org.ala.delta.intkey.WriteOnceIntkeyItemsFile;
import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.DefaultDataSetFactory;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.impl.DefaultDataSet;

/**
 * Tests the IntkeyCharactersFileWriter class.
 */
public class IntkeyItemsFileWriterTest extends TestCase {

	private IntkeyItemsFileWriter _itemsFileWriter;
	private WriteOnceIntkeyItemsFile _itemsFile;
	private DeltaContext _context;
	private DefaultDataSet _dataSet;
	
	@Before
	public void setUp() throws Exception {
		DefaultDataSetFactory factory = new DefaultDataSetFactory();
		_dataSet = (DefaultDataSet)factory.createDataSet("test");
		_context = new DeltaContext(_dataSet);
		
		MultiStateCharacter char1 = (MultiStateCharacter)_dataSet.addCharacter(CharacterType.UnorderedMultiState);
		char1.setDescription("character 1 description");
		char1.setNumberOfStates(3);
		char1.setState(1, "state 1");
		char1.setState(2, "This is state 2");
		char1.setState(3, "3");
		
		TextCharacter char2 = (TextCharacter)_dataSet.addCharacter(CharacterType.Text);
		char2.setDescription("this is character 2 description");
		
		IntegerCharacter char3 = (IntegerCharacter)_dataSet.addCharacter(CharacterType.IntegerNumeric);
		char3.setDescription("Char 3 is an integer character");
		
		RealCharacter char4 = (RealCharacter)_dataSet.addCharacter(CharacterType.RealNumeric);
		char4.setDescription("Char 4 is a real character");
		
		_context.setNumberOfCharacters(4);
		
		Item item1 = _dataSet.addItem();
		item1.setDescription("Item 1 description");
		_dataSet.addAttribute(1, 1).setValueFromString("<attribute 1,1 comment>1&3");
		_dataSet.addAttribute(1, 2).setValueFromString("<text character>");
		_dataSet.addAttribute(1, 3).setValueFromString("(1-)2-3/6-8");
		_dataSet.addAttribute(1, 4).setValueFromString("<text character>4.4");
		
		
		Item item2 = _dataSet.addItem();
		item2.setDescription("Description of item 2");
		_dataSet.addAttribute(2, 1).setValueFromString("<attribute 2,1 comment>1-2");
		_dataSet.addAttribute(2, 2).setValueFromString("<attribute 2,2 text character>");
		_dataSet.addAttribute(2, 3).setValueFromString("4");
		_dataSet.addAttribute(2, 4).setValueFromString("5.1-7.9");
		
		Item item3 = _dataSet.addItem();
		item3.setDescription("Item 3 has a great description");
		
		_itemsFile = new WriteOnceIntkeyItemsFile(4, 3, null, BinFileMode.FM_TEMPORARY);
		_itemsFileWriter = new IntkeyItemsFileWriter(_context, _itemsFile);
	}
	
	@After
	public void tearDown() throws Exception {
		_itemsFile.close();
	}
	
	@Test 
	public void testWriteItemDescrptions() {
		
		_itemsFileWriter.writeItemDescrptions();
		
		String item1 = _dataSet.getItem(1).getDescription();
		String item2 = _dataSet.getItem(2).getDescription();
		String item3 = _dataSet.getItem(3).getDescription();
		
		int totalLength = item1.length()+item2.length()+item3.length();
		int offset = readInt(2);
		assertEquals(0, offset);
		assertEquals(item1.length(), _itemsFile.readInt());
		assertEquals(item1.length()+item2.length(), _itemsFile.readInt());
		assertEquals(totalLength, _itemsFile.readInt());
	
		assertEquals(item1+item2+item3, readString(3, totalLength));	
		
	}
	
	@Test 
	public void testWriteCharacterSpecs() {
		
		_context.setCharacterReliability(1, 10.1);
		_context.setCharacterReliability(2, 3.3);
		
		_itemsFileWriter.writeCharacterSpecs();
		
		int[] charTypes = readInts(2, 2);
		assertEquals(1, charTypes[0]);
		assertEquals(5, charTypes[1]);
		
		int[] numStates = readInts(3, 2);
		assertEquals(3, numStates[0]);
		assertEquals(0, numStates[1]);
		
		float[] reliabilities = readFloats(4, 2);
		assertEquals(10.1f, reliabilities[0]);
		assertEquals(3.3f, reliabilities[1]);
		
		
	}
	
	@Test 
	public void zztestwriteCharacterDependencies() {
		throw new NotImplementedException();
	}
	
	@Test 
	public void testWriteAttributeData() {
		_itemsFileWriter.writeAttributeData();
		int[] indicies = readInts(2, 4);
		// Record where char 1 attributes are encoded.
		assertEquals(3, indicies[0]);
		// Record where char 2 attributes are encoded.
		assertEquals(4, indicies[1]);
		// Record where char 3 attributes are encoded.
		assertEquals(7, indicies[2]);
		// Record where char 4 attributes are encoded.
		assertEquals(8, indicies[3]);
		// Record where ...
		
		int[] multistateAttributes = readInts(3, 1);
		
		// Expect
		// attribute 1:  0101
		// attribute 2:  0011
		assertEquals(53, multistateAttributes[0]);
		
		
		// Text character (character 2)
		int[] inappliableBits = readInts(4, 1);
		assertEquals(0, inappliableBits[0]);
		
		String attribute12 = _dataSet.getAttribute(1, 2).getValueAsString();
		String attribute22 = _dataSet.getAttribute(2, 2).getValueAsString();
		String attribute32 = "";
		
		int totalLength = attribute12.length()+attribute22.length()+attribute32.length();
		int offset = readInt(5);
		assertEquals(0, offset);
		assertEquals(attribute12.length(), _itemsFile.readInt());
		assertEquals(attribute12.length()+attribute22.length(), _itemsFile.readInt());
		assertEquals(totalLength, _itemsFile.readInt());
	
		assertEquals(attribute12+attribute22+attribute32, readString(6, totalLength));	
		
		// Integer character (character 3)
		// attribute 1 is "(1-)2-3/6-8", attribute 2 is "4", attribute 3 null.
		// because we aren't using normal values, the range of values is 1-8.
		// hence our number of bits per attribute is 8-1+3=10.
		// attribute 1: 0111001110
		// attribute 2: 0000010000
		// attribute 3: 0000000000
		int[] intAttributeBits = readInts(7, 1);
		assertEquals(16846, intAttributeBits[0]);
		
		
		// Real character (character 4)
		// attribute 1 is "4.4".
		// attribute 2 is "5.1-7.9". 
		// attribute 3 is null.
		inappliableBits = readInts(8, 1);
		assertEquals(0, inappliableBits[0]);
		float[] values = readFloats(9, 6);
		float[] expected = {4.4f, 4.4f, 5.1f, 7.9f, Float.MAX_VALUE, -Float.MAX_VALUE};
		for (int i=0;i<expected.length; i++) {
			assertEquals(expected[i], values[i]);
		}
		
		// Integer min/max values are stored next.
		// only character 3 is an integer character, it's range is 1-8
		int[] mins = readInts(10, 4);
		int[] max = readInts(11, 4);
		int[] expectedMins = {0, 0, 1, 0};
		int[] expectedMaxes = {0, 0, 8, 0};
		for (int i=0; i<expectedMins.length; i++) {
			assertEquals(expectedMins[i], mins[i]);
			assertEquals(expectedMaxes[i], max[i]);
		}
	}
	
	
	@Test
	public void zztestwriteTaxonImages() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteEnableDeltaOutput() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteChineseFormat() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteCharacterSynonomy() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteOmitOr() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteUseControllingFirst() {
		throw new NotImplementedException();
	}

	@Test
	public void zztestwriteTaxonLinks() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteOmitPeriod() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteNewParagraph() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteNonAutoControllingChars() {
		throw new NotImplementedException();
	}
	
	@Test
	public void zztestwriteSubjectForOutputFiles() {
		throw new NotImplementedException();
	}
	
	
	private String readString(int recordNum, int lengthInBytes) {
		_itemsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*(recordNum-1));
		byte[] formatBytes = new byte[lengthInBytes];
		_itemsFile.readBytes(formatBytes);
		
		return new String(formatBytes);
	}
	
	private int[] readInts(int recordNum, int numInts) {
		_itemsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*(recordNum-1));
		
		int[] ints = new int[numInts];
		for (int i=0; i<numInts; i++) {
			ints[i] = _itemsFile.readInt();
		}
		return ints;
	}
	
	private float[] readFloats(int recordNum, int numFloats) {
		_itemsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*(recordNum-1));
		
		float[] floats = new float[numFloats];
		for (int i=0; i<numFloats; i++) {
			floats[i] = _itemsFile.readFloat();
		}
		return floats;
	}
	
	private int readInt(int recordNum) {
		return readInts(recordNum, 1)[0];
	}
}

