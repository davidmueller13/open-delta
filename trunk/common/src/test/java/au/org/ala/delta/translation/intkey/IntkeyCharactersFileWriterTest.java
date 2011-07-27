package au.org.ala.delta.translation.intkey;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.DeltaContext.HeadingType;
import au.org.ala.delta.intkey.IntkeyFile;
import au.org.ala.delta.intkey.WriteOnceIntkeyCharsFile;
import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.DefaultDataSetFactory;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.TypeSettingMark;
import au.org.ala.delta.model.TypeSettingMark.CharacterNoteMarks;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageInfo;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageType;
import au.org.ala.delta.model.image.OverlayType;
import au.org.ala.delta.model.impl.DefaultDataSet;
import au.org.ala.delta.translation.Words;
import au.org.ala.delta.translation.Words.Word;

/**
 * Tests the IntkeyCharactersFileWriter class.
 */
public class IntkeyCharactersFileWriterTest extends TestCase {

	private IntkeyCharactersFileWriter _charsFileWriter;
	private WriteOnceIntkeyCharsFile _charsFile;
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
		
		_charsFile = new WriteOnceIntkeyCharsFile(2, null, BinFileMode.FM_TEMPORARY);
		_charsFileWriter = new IntkeyCharactersFileWriter(_context, _charsFile);
	}
	
	@After
	public void tearDown() throws Exception {
		_charsFile.close();
	}
	
	/**
	 * Tests character features are written correctly to the intkey 
	 * chars file.
	 */
	@Test
	public void testWriteFeatures() throws Exception {
		
		_charsFileWriter.writeCharacterFeatures();
		
		// First record should be the header.
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES);
		
		// first record should be our feature index, then feature length
		// of first feature, then text of first feature.

		int offset = _charsFile.readInt();
		assertEquals(3, offset);
		assertEquals(5, _charsFile.readInt());
		
		MultiStateCharacter multiStateChar = (MultiStateCharacter)_dataSet.getCharacter(1);
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*2);
		int total = 0;
		StringBuilder text = new StringBuilder();
		int length = _charsFile.readInt();
		assertEquals(multiStateChar.getDescription().length(), length);
		total += length;
		text.append(multiStateChar.getDescription());
		length = _charsFile.readInt();
		assertEquals(multiStateChar.getState(1).length(), length);
		total += length;
		text.append(multiStateChar.getState(1));
		length = _charsFile.readInt();	
		assertEquals(multiStateChar.getState(2).length(), length);
		total += length;
		text.append(multiStateChar.getState(2));
		length = _charsFile.readInt();
		assertEquals(multiStateChar.getState(3).length(), length);
		total += length;
		text.append(multiStateChar.getState(3));
		
		byte[] textBytes = new byte[total];
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*3);
		
		_charsFile.readBytes(textBytes);
		assertEquals(text.toString(), new String(textBytes));
		
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*4);
		TextCharacter textChar = (TextCharacter)_dataSet.getCharacter(2);
		String description = textChar.getDescription();
		assertEquals(description.length(), _charsFile.readInt());
		
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*5);
		textBytes = new byte[description.length()];
		_charsFile.readBytes(textBytes);
		assertEquals(description, new String(textBytes));
		
		// This should also have written the num states record.
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*6);
		int numStates = _charsFile.readInt();
		assertEquals(3, numStates);
		assertEquals(0, _charsFile.readInt());
		
	}
	
	/**
	 * Tests character notes are written correctly to the intkey 
	 * chars file.
	 */
	@Test
	public void testWriteNotes() throws Exception {
		
		
		String notes1 = "notes 1";
		String notes2 = "this is notes 2";
		
		au.org.ala.delta.model.Character character = _dataSet.getCharacter(1);
		character.setNotes(notes1);
		
		character = _dataSet.getCharacter(2);
		character.setNotes(notes2);
		
		_charsFileWriter.writeCharacterNotes();
		
		// First record should be the header.
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES);
		
		assertEquals(3, _charsFile.readInt());
		assertEquals(5, _charsFile.readInt());
		
		assertEquals(notes1.length(), readInt(3));
		assertEquals(notes1, readString(4, notes1.length()));
		
		assertEquals(notes2.length(), readInt(5));
		assertEquals(notes2, readString(6, notes2.length()));
	}
	
	/**
	 * Tests the character notes format is written correctly to the intkey 
	 * chars file.
	 */
	@Test
	public void testWriteCharacterNotesFormat() {
		String format = "format!";
		TypeSettingMark mark = new TypeSettingMark(CharacterNoteMarks.CHARACTER_NOTES_FORMAT.getId(), format, true);
		_context.addFormattingMark(mark);
		_charsFileWriter.writeCharacterNotesFormat();
		assertEquals(format.length(), readInt(2));	
		assertEquals(format,readString(3, format.length()));
	}
	
	/**
	 * Tests the character notes help format is written correctly to the intkey 
	 * chars file.
	 */
	@Test
	public void testWriteCharacterNotesHelpFormat() {
		String format = "format!";
		TypeSettingMark mark = new TypeSettingMark(CharacterNoteMarks.CHARACTER_NOTES_HELP_FORMAT.getId(), format, true);
		_context.addFormattingMark(mark);
		_charsFileWriter.writeCharacterNotesHelpFormat();
		assertEquals(format.length(), readInt(2));	
		assertEquals(format,readString(3, format.length()));
	}
	
	@Test
	public void testWriteCharacterImages() {
		
		String image1 = "test.jpg <@feature x=1 y=2 w=3 h=4>";
		
		au.org.ala.delta.model.Character character = _dataSet.getCharacter(1);
		Image image = character.addImage("test.jpg", "");
		ImageOverlay overlay = new ImageOverlay(OverlayType.OLFEATURE, 
				(short)1 ,(short)2, (short)3, (short)4);
		
		image.addOverlay(overlay);
		_charsFileWriter.writeCharacterImages();
		
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES);
		assertEquals(3, _charsFile.readInt());
		assertEquals(5, _charsFile.readInt());
		
		assertEquals(image1.length()+System.getProperty("line.separator").length(), readInt(3));
		assertEquals(image1, readString(4, image1.length()).trim());
	}
	
	@Test
	public void testWriteStartupImages() {
		
		List<ImageInfo> images = createImages();
		_context.setImages(ImageType.IMAGE_STARTUP, images);
		
		_charsFileWriter.writeStartupImages();
		
		checkImages();
	}

	private void checkImages() {
		String startupImages = "file1.jpg <@feature x=1 y=2 w=3 h=4> "+
	    "<@text x=1 y=2 w=3 h=4>";
		System.out.println(startupImages.length());
		
		//assertEquals(startupImages.length()+System.getProperty("line.separator").length(), readInt(2));
		startupImages = startupImages.replace(System.getProperty("line.separator"), " ");
		
		String actual = readString(3, startupImages.length()).trim();
		
		actual = actual.replace(System.getProperty("line.separator"), " ");
		System.out.println(actual);
		assertEquals(startupImages, actual);
	}

	private List<ImageInfo> createImages() {
		ImageOverlay overlay = new ImageOverlay(OverlayType.OLFEATURE, 
				(short)1 ,(short)2, (short)3, (short)4);
		List<ImageOverlay> overlays = new ArrayList<ImageOverlay>();
		overlays.add(overlay);
		overlays.add(new ImageOverlay(OverlayType.OLTEXT, (short)1, (short)2, (short)3, (short)4));
		ImageInfo image = new ImageInfo(0, ImageType.IMAGE_STARTUP, "file1.jpg", overlays);
		List<ImageInfo> images = new ArrayList<ImageInfo>();
		images.add(image);
		return images;
	}
	
	@Test
	public void testWriteCharacterKeyImages() {
		
		List<ImageInfo> images = createImages();
		_context.setImages(ImageType.IMAGE_CHARACTER_KEYWORD, images);
		
		_charsFileWriter.writeCharacterKeyImages();
		
		checkImages();
	}
	
	@Test
	public void testWriteHeading() {
		String heading = "this is a heading";
		_context.setHeading(HeadingType.HEADING, heading);
		_charsFileWriter.writeHeading();

		assertEquals(heading.length(), readInt(2));
		assertEquals(heading, readString(3, heading.length()));
	}
	
	@Test
	public void testWriteSubHeading() {
		String heading = "this is a subheading";
		_context.setHeading(HeadingType.REGISTRATION_SUBHEADING, heading);
		_charsFileWriter.writeSubHeading();

		assertEquals(heading.length(), readInt(2));
		assertEquals(heading, readString(3, heading.length()));
	}
	
	@Test
	public void testWriteValidationString() {
		String validationString = "this is a validation string";
		
		_charsFile.writeValidationString(validationString);

		assertEquals(validationString.length(), readInt(2));
		assertEquals(validationString, readString(3, validationString.length()));
	}
	
	@Test
	public void testWriteCharacterMask() {
		BitSet charMask = new BitSet(2);
		charMask.set(0);
		charMask.set(1);
		
		_charsFile.writeCharacterMask(2, charMask);
		assertEquals(2, readInt(2));
		assertEquals(3, _charsFile.readInt());
	}
	
	@Test
	public void testWriteOrWord() {
		String orWord = "or";
		Words.setWord(Word.OR, orWord);
		
		_charsFileWriter.writeOrWord();

		assertEquals(orWord.length(), readInt(2));
		assertEquals(orWord, readString(3, orWord.length()));
	}
	
	@Test
	public void testWriteFonts() {
		List<String> fonts = new ArrayList<String>();
		String font1 = "font 1";
		String font2 = "font 2 with extra";
		String font3 = "font number 3";
		fonts.add(font1);
		fonts.add(font2);
		fonts.add(font3);
		
		_charsFile.writeFonts(fonts);
		
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES);
		assertEquals(fonts.size(), _charsFile.readInt());
		
		
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*2);
		assertEquals(font1.length(), _charsFile.readInt());
		assertEquals(font2.length(), _charsFile.readInt());
		assertEquals(font3.length(), _charsFile.readInt());
		
		assertEquals(font1+font2+font3, readString(4, font1.length()+font2.length()+font3.length()));
	}
	
	@Test
	public void testWriteItemSubHeadings() {
		List<String> itemSubHeadings = new ArrayList<String>();
		String heading1 = "heading1";
		String heading2 = "heading2";
		itemSubHeadings.add(heading1);
		itemSubHeadings.add(heading2);
		
		_charsFile.writeItemSubheadings(itemSubHeadings);
		
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES);
		assertEquals(3, _charsFile.readInt());
		assertEquals(5, _charsFile.readInt());
		
		assertEquals(heading1.length(), readInt(3));
		assertEquals(heading1, readString(4, heading1.length()));
		
		assertEquals(heading2.length(), readInt(5));
		assertEquals(heading2, readString(6, heading2.length()));
	}
	
	
	private String readString(int recordNum, int lengthInBytes) {
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*(recordNum-1));
		byte[] formatBytes = new byte[lengthInBytes];
		_charsFile.readBytes(formatBytes);
		
		return new String(formatBytes);
	}
	
	private int[] readInts(int recordNum, int numInts) {
		_charsFile.seek(IntkeyFile.RECORD_LENGTH_BYTES*(recordNum-1));
		
		int[] ints = new int[numInts];
		for (int i=0; i<numInts; i++) {
			ints[i] = _charsFile.readInt();
		}
		return ints;
	}
	
	private int readInt(int recordNum) {
		return readInts(recordNum, 1)[0];
	}
}

