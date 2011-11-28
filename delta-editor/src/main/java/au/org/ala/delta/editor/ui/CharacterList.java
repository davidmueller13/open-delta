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
package au.org.ala.delta.editor.ui;

import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;

import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.model.format.CharacterFormatter;

/**
 * A specialized list for displaying DELTA Characters.
 * 
 * The CharacterList also supports an extended selection model whereby it can respond to double clicks
 * or the Enter key. To respond to this kind of selection event, register an Action using the 
 * setSelectionAction method.
 */
public class CharacterList extends SelectionList {

	private static final long serialVersionUID = -5233281885631132020L;

	
	class CharacterListModel extends AbstractListModel {

		private CharacterFormatter _formatter = new CharacterFormatter();
		private static final long serialVersionUID = 6573565854830718124L;

		private EditorViewModel _dataSet;
		
		public CharacterListModel(EditorViewModel dataSet) {
			_dataSet = dataSet;
		}
		
		@Override
		public int getSize() {
			return _dataSet.getNumberOfCharacters();
		}

		@Override
		public Object getElementAt(int index) {
			return _formatter.formatCharacterDescription(_dataSet.getCharacter(index+1));
		}
		
	}
	
	public CharacterList() {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	/**
	 * Creates an ItemList backed by the supplied dataSet.
	 * @param dataSet the data set to act as the model for this List.
	 */
	public CharacterList(EditorViewModel dataSet) {
		this();
		setModel(dataSet);
	}
	
	public void setModel(EditorViewModel dataSet) {
		setModel(new CharacterListModel(dataSet));
		
	}
}
