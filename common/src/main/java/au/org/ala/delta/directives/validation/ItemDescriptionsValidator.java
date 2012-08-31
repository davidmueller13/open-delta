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
package au.org.ala.delta.directives.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.image.ImageInfo;

/**
 * Validates item descriptions used in directives that use them to 
 * identify items, for example INDEX HEADINGS.
 */
public class ItemDescriptionsValidator {
	
	public void validate(DeltaContext context, String[] directiveName, Iterator<String> descriptions) {
		
		DeltaDataSet dataSet = context.getDataSet();
		
		List<String> invalidDescriptions = new ArrayList<String>();
		while (descriptions.hasNext()) {
			String description = descriptions.next();
			checkForInvalidDescription(descriptions, dataSet, invalidDescriptions, description);
		}		
		
		addWarnings(context, directiveName, invalidDescriptions);
	}

	protected void checkForInvalidDescription(Iterator<?> descriptions, DeltaDataSet dataSet,
			List<String> invalidDescriptions, String description) {
		Item item = dataSet.itemForDescription(description);
		
		if (item == null) {
			invalidDescriptions.add(description);
			descriptions.remove();
		}
	}

	protected void addWarnings(DeltaContext context, String[] directiveName, List<String> invalidDescriptions) {
		if (invalidDescriptions.size() > 0) {
			StringBuilder invalidList = new StringBuilder();
			for (String description : invalidDescriptions) {
				invalidList.append(System.getProperty("line.separator"));
				invalidList.append(description);
			}
			DirectiveError warning = new DirectiveError(
					DirectiveError.Warning.TAXON_NAMES_DUPLICATED_OR_UNMATCHED, 
					0,
					StringUtils.join(directiveName, " ").toUpperCase(),
					invalidList.toString());
			context.addError(warning);
		}
	}
	
	public void validateImages(DeltaContext context, String[] directiveName, Iterator<ImageInfo> images) {
		DeltaDataSet dataSet = context.getDataSet();
		
		List<String> invalidDescriptions = new ArrayList<String>();
		while (images.hasNext()) {
			ImageInfo image = images.next();
			
			checkForInvalidDescription(images, dataSet, invalidDescriptions, (String)image.getId());
		}		
		
		addWarnings(context, directiveName, invalidDescriptions);
	}

}
