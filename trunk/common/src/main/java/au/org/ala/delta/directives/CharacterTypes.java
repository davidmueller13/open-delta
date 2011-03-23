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

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.Logger;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.MultiStateCharacter;

public class CharacterTypes extends AbstractCharacterListDirective<CharacterType> {

	/** 
	 * Tracks the number last character that was created to allow defaults to be created 
	 * for characters not explicitly specified
	 */
	private int _lastCharacterNumber = 0;
	
	public CharacterTypes() {
		super("character", "types");
	}

	@Override
	protected CharacterType interpretRHS(DeltaContext context, String rhs) {
		return CharacterType.parse(rhs);
	}

	@Override
	protected void processCharacter(DeltaContext context, int charNumber, CharacterType type) {
		Logger.debug("Setting type for character %d to %s", charNumber, type);
		
		DeltaDataSet dataSet = context.getDataSet();
		
		// CG - this is making an assumption that character types are in ascending numerical order.
		// I am not sure if this is valid....
		for (int i=_lastCharacterNumber+1; i<charNumber; i++) {
			createDefaultCharacter(dataSet, i);
		}
		
		dataSet.addCharacter(charNumber, type);
		_lastCharacterNumber = charNumber;
	}
	
	private void createDefaultCharacter(DeltaDataSet dataSet, int number) {
		dataSet.addCharacter(number, CharacterType.UnorderedMultiState);
	}
}
