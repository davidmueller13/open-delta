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
package au.org.ala.delta.model;

import au.org.ala.delta.model.impl.CharacterData;

public class CharacterFactory {

	public static Character newCharacter(CharacterType type, CharacterData impl) {
		Character ch = null;
		switch (type) {
			case IntegerNumeric:
				ch = new IntegerCharacter();
				break;
			case OrderedMultiState:
				ch = new OrderedMultiStateCharacter();
				break;
			case RealNumeric:
				ch = new RealCharacter();
				break;
			case Text:
				ch = new TextCharacter();
				break;
			case UnorderedMultiState:
				ch = new UnorderedMultiStateCharacter();
				break;
			case Unknown:
				ch = new UnknownCharacter();
				break;
			default:
				throw new RuntimeException("Unhandled character type: " + type);
		}
		ch.setImpl(impl);
		return ch;
	}

}
