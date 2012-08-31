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
import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.directives.validation.IntegerValidator;
import au.org.ala.delta.directives.validation.ItemNumberValidator;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements the INCLUDE ITEMS directive.
 */
public class IncludeItems extends AbstractRangeListDirective<DeltaContext> {
	
	public static final String[] CONTROL_WORDS = {"include", "items"};
	
	private Set<Integer> _includedItems;
	
	public IncludeItems() {
		super(CONTROL_WORDS);
	}
	
	@Override
	public int getArgType() {
		return DirectiveArgType.DIRARG_ITEMLIST;
	}
	
	
	@Override
	public void process(DeltaContext context, DirectiveArguments directiveArguments) throws Exception {
		context.includeAllItems();
		_includedItems = new HashSet<Integer>();
		super.process(context, directiveArguments);
		
		for (int i=1; i<=context.getMaximumNumberOfItems(); i++) {
			if (!_includedItems.contains(i)) {
				context.excludeItem(i);
			}
		}
	}

	@Override
	protected void processNumber(DeltaContext context, int number) {
		
		_includedItems.add(number);
	}
	
	@Override
	public int getOrder() {
		return 4;
	}

    @Override
    protected IntegerValidator createValidator(DeltaContext context) {
        return new ItemNumberValidator(context);
    }
}
