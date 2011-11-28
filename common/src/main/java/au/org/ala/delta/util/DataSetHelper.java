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
package au.org.ala.delta.util;

import java.util.List;

import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.MutableDeltaDataSet;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.image.ImageInfo;

/**
 * Contains helper methods for working with a DeltaDataSet. These methods could
 * belong in the dataset itself but is is already quite cluttered.
 */
public class DataSetHelper {

	private MutableDeltaDataSet _dataSet;

	public DataSetHelper(MutableDeltaDataSet dataSet) {
		assert (dataSet != null);
		_dataSet = dataSet;
	}

	/**
	 * Returns the next Item in the Data Set that has an Image associated 
	 * with it.
	 * @param startFrom the Item to start from when searching for the "next"
	 * Item.
	 * @return the first Item found with an item number greater than the
	 * starting Item which also has at least one image.  If no such Item
	 * exists, null will be returned.
	 */
	public Item getNextItemWithImage(Item startFrom) {

		int itemNumber = startFrom.getItemNumber();

		for (int i = itemNumber + 1; i <= _dataSet.getMaximumNumberOfItems(); i++) {
			Item next = _dataSet.getItem(i);
			if (next.getImageCount() > 0) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Returns the next Item in the Data Set that has an Image associated 
	 * with it.
	  * @param startFrom the Item to start from when searching for the 
	 * "previous" Item.
	 * @return the first Item found with an item number less than the
	 * starting Item which also has at least one image.  If no such 
	 * Item exists, null will be returned.
	 */
	public Item getPreviousItemWithImage(Item startFrom) {

		int itemNumber = startFrom.getItemNumber();

		for (int i = itemNumber - 1; i > 0; i--) {
			Item next = _dataSet.getItem(i);
			if (next.getImageCount() > 0) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Returns the next Character in the Data Set that has an Character 
	 * associated with it.
	 * @param startFrom the Character to start from when searching for the 
	 * "next" Character.
	 * @return the first Character found with an item number greater than the
	 * starting Character which also has at least one image.  If no such 
	 * Character exists, null will be returned.
	 */
	public Character getNextCharacterWithImage(Character startFrom) {

		int characterNumber = startFrom.getCharacterId();

		for (int i = characterNumber + 1; i <= _dataSet.getNumberOfCharacters(); i++) {
			Character next = _dataSet.getCharacter(i);
			if (next.getImageCount() > 0) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Returns the previous Character in the Data Set that has an Character 
	 * associated with it.
	 * @param startFrom the Character to start from when searching for the 
	 * "previous" Character.
	 * @return the first Character found with an item number less than the
	 * starting Character which also has at least one image.  If no such 
	 * Character exists, null will be returned.
	 */
	public Character getPreviousCharacterWithImage(Character startFrom) {

		int characterNumber = startFrom.getCharacterId();

		for (int i = characterNumber - 1; i > 0; i--) {
			Character next = _dataSet.getCharacter(i);
			if (next.getImageCount() > 0) {
				return next;
			}
		}
		return null;
	}
	

	public void addItemImages(List<ImageInfo> images) {
		if (images == null) {
			return;
		}
		for (ImageInfo imageInfo : images) {
			String description = (String)imageInfo.getId();
			Item item = _dataSet.itemForDescription(description);
			if (item != null) {
				imageInfo.addOrUpdate(item);
			}
			else {
				throw new IllegalArgumentException("No such item: "+description);
			}
		}
	}
}
