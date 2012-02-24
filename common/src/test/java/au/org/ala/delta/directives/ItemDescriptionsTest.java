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
package au.org.ala.delta.directives;

import java.io.InputStream;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.validation.DirectiveException;
import au.org.ala.delta.model.DefaultDataSetFactory;
import au.org.ala.delta.model.MutableDeltaDataSet;

public class ItemDescriptionsTest extends TestCase {

	private MutableDeltaDataSet _dataSet;
	private DeltaContext _context;
	// private ItemDescriptions _itemDescriptions;

	@Before
	public void setUp() {
		DefaultDataSetFactory factory = new DefaultDataSetFactory();
		_dataSet = factory.createDataSet("test");
		_context = new DeltaContext(_dataSet);
		_context.setMaximumNumberOfItems(1);
		// _itemDescriptions = new ItemDescriptions();
	}
	
	private void confor(String scriptName, int expectedError) throws Exception {
		try {
			String directives = getConforResoure(scriptName);
			ConforDirectiveFileParser parser = ConforDirectiveFileParser.createInstance();
			ConforDirectiveParserObserver observer = new ConforDirectiveParserObserver(_context);
			parser.registerObserver(observer);
			parser.parse(new StringReader(directives), _context);
			fail(String.format("Exception (error %d) expected", expectedError));
		} catch (DirectiveException ex) {
			assertEquals(expectedError, ex.getErrorNumber());
		}
		
	}
	
	private String getConforResoure(String name) throws Exception {
		InputStream is = ItemDescriptionsTest.class.getResourceAsStream(String.format("/confor/%s", name));
		return StringUtils.join(IOUtils.readLines(is), "\n");
	}

	@Test
	public void testParsing() throws Exception {		
		confor("prereqtest", 36);
	}
	
	@Test
	public void test1() throws Exception {
		confor("test1", 12);
	}
	
	@Test
	public void test2() throws Exception {
		confor("test2", 46);
	}

}
