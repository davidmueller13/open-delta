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
package au.org.ala.delta.editor.directives;

import au.org.ala.delta.editor.slotfile.Directive;
import au.org.ala.delta.editor.slotfile.directive.ConforDirType;
import au.org.ala.delta.editor.slotfile.directive.DirOutTaxonImages;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.OverlayType;

/**
 * Tests the DirOutTaxonImages class.
 */
public class DirOutTaxonImagesTest extends DirOutImageOverlayTest {

	protected Directive getDirective() {
		return ConforDirType.ConforDirArray[ConforDirType.TAXON_IMAGES];
	}
	
	protected void initialiseDataSet() {

		for (int i=0; i<10; i++) {
			Item item = _dataSet.addItem();
			item.setDescription("item "+(i+1));
		}
	}
	
	protected Image addImage(String fileName, int toItem) {
		Item item = _dataSet.getItem(toItem);
		Image image = item.addImage(fileName, "");
		return image;
	}
	
	/**
	 * Tests the export of a single image with a subject overlay to a 
	 * TAXON IMAGES directive using our sample dataset.
	 */
	public void testDirOutCharImagesSubjectOverlay() throws Exception {
		
		initialiseDataSet();
		
		Image image = addImage("image 1", 3);
		ImageOverlay overlay = addOverlay(image, OverlayType.OLSUBJECT);
		overlay.overlayText="Subject";
		DirOutTaxonImages dirOut = new DirOutTaxonImages();
		
		dirOut.process(_state);
		
		assertEquals("*TAXON IMAGES\n# item 3/\n"+
				"     image 1\n"+
				"          <@subject Subject>\n", output());
	}
	
	/**
	 * Tests the export of a single image with a sound overlay to a 
	 * TAXON IMAGES directive using our sample dataset.
	 */
	public void testDirOutCharImagesSoundOverlay() throws Exception {
		
		initialiseDataSet();
		
		Image image = addImage("image 1", 3);
		ImageOverlay overlay = addOverlay(image, OverlayType.OLSOUND);
		overlay.overlayText="sound file name.wav";
		DirOutTaxonImages dirOut = new DirOutTaxonImages();
		
		dirOut.process(_state);
		
		assertEquals("*TAXON IMAGES\n# item 3/\n"+
				"     image 1\n"+
				"          <@sound sound file name.wav>\n", output());
	}
	
}
