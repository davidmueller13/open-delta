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

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.DeltaContext.HeadingType;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.io.OutputFileSelector;

public class ListingFile extends AbstractTextDirective {
	
	public ListingFile() {
		super("listing", "file");
	}
	
	@Override
	public void process(DeltaContext context, DirectiveArguments args) throws Exception {
		
		String data = args.getFirstArgumentText();
		String filename = data.trim();
		
		OutputFileSelector fileManager = context.getOutputFileSelector();
		fileManager.setListingFileName(filename);
		startFile(context);
	}
	
	protected void startFile(DeltaContext context) {
		
		OutputFileSelector fileManager = context.getOutputFileSelector();
		String credits = context.getCredits(); 
		if (StringUtils.isNotEmpty(credits)) {
			fileManager.listMessage(credits);
		}
		String heading = context.getHeading(HeadingType.HEADING);
		if (heading != null) {
			fileManager.listMessage(heading);
		}
		
		heading = context.getHeading(HeadingType.SHOW);
		if (heading != null) {
			fileManager.listMessage(heading);
		}
	}

}
