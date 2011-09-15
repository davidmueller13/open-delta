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
package au.org.ala.delta.ui;

import au.org.ala.delta.model.SearchDirection;

/**
 * Immutable Transfer Object for the options for a textual search and replace
 * 
 * @author baird
 *
 */
public class SearchOptions {
	
	private boolean wrappedSearch;
	private boolean caseSensitive;
	private SearchDirection searchDirection;
	
	public SearchOptions(SearchDirection searchDirection, boolean caseSensitive, boolean wrapSearch) {
		this.wrappedSearch = wrapSearch;
		this.caseSensitive = caseSensitive;
		this.searchDirection = searchDirection;
	}
	
	public boolean isWrappedSearch() {
		return wrappedSearch;
	}
	
	public SearchDirection getSearchDirection() {
		return this.searchDirection;
	}
	
	public boolean isCaseSensitive() {
		return this.caseSensitive;
	}

}
