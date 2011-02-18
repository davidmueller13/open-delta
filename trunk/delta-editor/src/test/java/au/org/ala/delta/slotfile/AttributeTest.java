/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.slotfile;

import java.util.Arrays;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * Tests the Attribute class.
 * 
 */
public class AttributeTest extends TestCase {

	private Mockery context = new Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};
	
	private VOCharBaseDesc fakeVO;
	
	@Test public void testParseIntegerChar() {
		fakeVO = setupExpectationsForIntegerCharacter();
		String value = "12";
		testNumber(value, 12f, 0);
	}
	
	@Test public void testParseRealChar() {
		fakeVO = setupExpectationsForRealCharacter();
		String value = "12.444";
		testNumber(value, 12.444f, 3);
	}
	
	@Test public void testParseRealCharWithComments() {
		fakeVO = setupExpectationsForRealCharacter();
		String value = "12.444<or thereabouts>";
		
		Attribute attribute = new Attribute(value, fakeVO);
		
		assertEquals(2, attribute.getNChunks());
		
		byte[] data = attribute.getData();
		
		assertEquals("chunk type", (byte)ChunkType.CHUNK_NUMBER, data[1]);
		
		DeltaNumber deltaNumber = new DeltaNumber();
		deltaNumber.fromBinary(data, 2);
		
		assertEquals(12.444f, deltaNumber.asFloat());
		assertEquals(3, deltaNumber.getDecimal());
		
		
		value = "<about>3.3333";
		attribute = new Attribute(value, fakeVO);
		assertEquals(2, attribute.getNChunks());
		
		data = attribute.getData();
		
		assertEquals("chunk type", (byte)ChunkType.CHUNK_TEXT, data[1]);
		
	}
	
	private void testNumber(String numberStr, float expectedValue, int expectedNumDecimalPlaces) {
		
		Attribute attribute = new Attribute(numberStr, fakeVO);
		
		assertEquals(1, attribute.getNChunks());
		
		byte[] data = attribute.getData();
		
		assertEquals("chunk type", (byte)ChunkType.CHUNK_NUMBER, data[1]);
		
		DeltaNumber deltaNumber = new DeltaNumber();
		deltaNumber.fromBinary(data, 2);
		
		assertEquals(expectedValue, deltaNumber.asFloat());
		assertEquals(expectedNumDecimalPlaces, deltaNumber.getDecimal());
	}
	

	@Test public void testParseShortCommentWithRTF() {
		// The parsing is counting the "}" and not the "{" so it blows up at the end.
		// Since I am not sure exactly what is supposed to happen here I'm going to leave this for a 
		// bit.
		String attributeText = 
			"\\i{}Agraulus\\i0{} P. Beauv., \\i{}Agrestis\\i0{} Bub., \\i{}Anomalotis\\i0{}\n"+
			"Steud., \\i{}Bromidium\\i0{} Nees, \\i{}Candollea\\i0{} Steud.,\n"+
			"\\i{}Chaetotropis\\i0{} Kunth, \\i{}Decandolea\\i0{} Batard, \\i{}Didymochaeta\\i0{}\n"+
			"Steud., \\i{}Lachnagrostis\\i0{} Trin., \\i{}Neoschischkinia\\i0{} Tsvelev,\n"+
			"\\i{}Notonema\\i0{} Raf., \\i{}Pentatherum\\i0{} Nabelek, \\i{}Podagrostis\\i0{}\n"+
			"(Griseb.) Scribn., \\i{}Senisetum\\i0{} Koidz., \\i{}Trichodium\\i0{} Michaux,\n"+
			"\\i{}Vilfa\\i0{} Adans.";
		testShortTextAttribute(attributeText);
	}
	
	/**
	 * Tests the parsing of a text attribute containing only a comment.
	 * {@link au.org.ala.delta.slotfile.Attribute#parse(java.lang.String, au.org.ala.delta.slotfile.VOCharBaseDesc, boolean)}
	 * .
	 */
	@Test
	public void testParseShortCommentOnlyAttribute() {

		String attributeText = "Just a comment with no RTF";
		testShortTextAttribute(attributeText);
	}

	/**
	 * Tests the parsing of a text attribute containing only a comment.
	 * {@link au.org.ala.delta.slotfile.Attribute#parse(java.lang.String, au.org.ala.delta.slotfile.VOCharBaseDesc, boolean)}
	 * .
	 */
	@Test
	public void testParseShortNestedCommentAttribute() {

		String attributeTextWithNestedComment = "Just a comment with no RTF<Now Nested>";
		testShortTextAttribute(attributeTextWithNestedComment);
	}

	private void testShortTextAttribute(String attributeText) {
		fakeVO = setupExpectationsForTextCharacter();

		Attribute attribute = new Attribute("<"+attributeText+">", fakeVO);
		
		assertEquals(1, attribute.getNChunks());
		
		byte[] data = attribute.getData();
		
		assertEquals("chunk type", (byte)ChunkType.CHUNK_TEXT, data[1]);
		
		// We only have ASCII characters so string length should be the same as byte length
		// Also we are little endian so the least significant byte will be first in the array
		assertEquals("chunk length", attributeText.length(), data[2]);
		assertEquals("chunk length", 0, data[3]);
		
		byte[] subData = Arrays.copyOfRange(data, 4, data.length);
		assertEquals("attribute text", attributeText, new String(subData));
	}
	
	/**
	 * @return
	 */
	private VOCharBaseDesc setupExpectationsForTextCharacter() {
		return setupExpectationsForCharacter(CharType.TEXT);
	}
	private VOCharBaseDesc setupExpectationsForIntegerCharacter() {
		return setupExpectationsForCharacter(CharType.INTEGER);
	}
	private VOCharBaseDesc setupExpectationsForRealCharacter() {
		return setupExpectationsForCharacter(CharType.REAL);
	}
	
	private VOCharBaseDesc setupExpectationsForCharacter(int charType) {
		fakeVO = context.mock(VOCharBaseDesc.class);
		final int finalCharType = charType;
		
		context.checking(new Expectations() {
			{
				atLeast(1).of(fakeVO).getUniId(); will(returnValue(1));
				atLeast(1).of(fakeVO).getCharType(); will(returnValue(finalCharType));
				atLeast(1).of(fakeVO).uniIdFromStateNo(1); will(returnValue(3));
				atLeast(1).of(fakeVO).testCharFlag((byte)1); will(returnValue(true));
				
			}
		});
		return fakeVO;
	}
	
	
}
