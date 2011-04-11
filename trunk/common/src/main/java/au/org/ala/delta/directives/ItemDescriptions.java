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

import java.io.Reader;
import java.io.StringReader;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.Logger;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.StateValue;
import au.org.ala.delta.model.TextCharacter;

public class ItemDescriptions extends ConforDirective {

	
	public ItemDescriptions() {
		super("item", "descriptions");
	}

	@Override
	public void process(DeltaContext context, String data) throws Exception {
		StringReader reader = new StringReader(data);
		ItemsParser parser = new ItemsParser(context, reader);
		parser.parse();
	}

}

class ItemsParser extends AbstractStreamParser {
	
	/** The last Item parsed that is not a variant Item */
	private int _lastMaster;
	
	public ItemsParser(DeltaContext context, Reader reader) {
		super(context, reader);
	}

	@Override
	public void parse() throws Exception {

		_context.initializeMatrix();

		int itemIndex = 1;
		while (skipTo('#') && _currentInt >= 0) {
			parseItem(itemIndex);
			itemIndex++;
		}
	}

	private void parseItem(int itemIndex) throws Exception {

		assert _currentChar == '#';
		readNext();

		Item item = null;
	
		item = createItem(itemIndex);
		
		String itemName = readToNextEndSlashSpace();
		Logger.debug("Parsing Item %s", itemName);
	
		item.setDescription(itemName);
		skipWhitespace();
		while (_currentChar != '#' && _currentInt >= 0) {
			int charIdx = readInteger();
			au.org.ala.delta.model.Character ch = _context.getCharacter(charIdx);
			String strValue = null;
			String comment = null;
			if (ch instanceof TextCharacter) {
				strValue = readComment();
			} else {

				if (_currentChar == '<') {
					comment = readComment();
				}
				if (_currentChar == ',') {
					readNext();
					strValue = readStateValue(ch);
				} else if (isWhiteSpace(_currentChar)) {
					strValue = "U";
				} else {
					throw new RuntimeException(String.format("Expected a ',' for state values (Character %d is not a Text character)", charIdx));
				}

			}
			assert strValue != null;
			StateValue stateValue = new StateValue(ch, item, strValue);
			if (comment != null) {
				stateValue.setComment(comment);
			}
			item.addAttribute(ch, strValue);
			
			_context.getMatrix().setValue(charIdx, itemIndex, stateValue);
			Logger.debug("  %d. %s", charIdx, stateValue);
			skipWhitespace();
		}
	}

	private Item createItem(int itemIndex) throws Exception {
		Item item;
		if (_currentChar == '+') {
			if (itemIndex == 1) {
				throw new RuntimeException("The first item cannot be a variant item!");
			}
		    item = _context.getDataSet().addVariantItem(_lastMaster, itemIndex);
			readNext();
		}
		else {
			item = _context.getDataSet().addItem(itemIndex);
			_lastMaster = itemIndex;
		}
		return item;
	}

	private String readStateValue(Character character) throws Exception {
		String value = readToNextSpaceComments();
		return value;
	}
}
