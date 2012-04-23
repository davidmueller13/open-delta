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
package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.model.Item;

public class TaxonListArgument extends AbstractTaxonListArgument<List<Item>> {

	/**
	 * @param name
	 *            Name of the argument
	 * @param promptText
	 *            Text used to prompt the user for a value
	 * @param defaultSelectionMode
	 *            default selection mode when user is prompted for a value
	 * @param selectFromAll
	 *            When prompting, allow selection from all
	 */
	public TaxonListArgument(String name, String promptText, boolean selectFromAll, boolean noneSelectionPermitted) {
		super(name, promptText, selectFromAll, noneSelectionPermitted);
	}

	@Override
	public List<Item> parseInput(Queue<String> inputTokens, IntkeyContext context, String directiveName, StringBuilder stringRepresentationBuilder) throws IntkeyDirectiveParseException {
		boolean overrideExcludedTaxa = false;

		String token = inputTokens.poll();
		if (token != null && token.equalsIgnoreCase(OVERRIDE_EXCLUDED_TAXA)) {
			overrideExcludedTaxa = true;
			token = inputTokens.poll();
		}

		overrideExcludedTaxa = overrideExcludedTaxa || _selectFromAll;

		List<Item> taxa = null;

		SelectionMode selectionMode = context.displayKeywords() ? SelectionMode.KEYWORD : SelectionMode.LIST;

		if (token != null) {
			if (token.equalsIgnoreCase(DEFAULT_DIALOG_WILDCARD)) {
				// do nothing - default selection mode is already set above.
			} else if (token.equalsIgnoreCase(KEYWORD_DIALOG_WILDCARD)) {
				selectionMode = SelectionMode.KEYWORD;
			} else if (token.equalsIgnoreCase(LIST_DIALOG_WILDCARD)) {
				selectionMode = SelectionMode.LIST;
			} else if (token.equalsIgnoreCase(LIST_DIALOG_AUTO_SELECT_SOLE_ITEM_WILDCARD)) {
				selectionMode = SelectionMode.LIST_AUTOSELECT_SINGLE_VALUE;
			} else {
				taxa = new ArrayList<Item>();

				while (token != null) {
					try {
						taxa.addAll(ParsingUtils.parseTaxonToken(token, context));
						token = inputTokens.poll();

					} catch (IllegalArgumentException ex) {
						throw new IntkeyDirectiveParseException(String.format("Unrecognized taxon keyword %s", token), ex);
					}
				}

				if (!overrideExcludedTaxa) {
					taxa.retainAll(context.getIncludedTaxa());
				}
			}
		}

		if (taxa == null) {
			DirectivePopulator populator = context.getDirectivePopulator();
			if (selectionMode == SelectionMode.KEYWORD) {
				taxa = populator.promptForTaxaByKeyword(directiveName, !overrideExcludedTaxa, _noneSelectionPermitted, false, null);
			} else {
				boolean autoSelectSingleValue = (selectionMode == SelectionMode.LIST_AUTOSELECT_SINGLE_VALUE);
				taxa = populator.promptForTaxaByList(directiveName, !overrideExcludedTaxa, autoSelectSingleValue, false, false, null);
			}
		}

		if (taxa != null) {
			stringRepresentationBuilder.append(" ");
			for (int i = 0; i < taxa.size(); i++) {
				Item taxon = taxa.get(i);
				stringRepresentationBuilder.append(taxon.getItemNumber());
				if (i < taxa.size() - 1) {
					stringRepresentationBuilder.append(" ");
				}
			}

			if (taxa.size() == 0 && !_noneSelectionPermitted) {
				throw new IntkeyDirectiveParseException("NoTaxaInSet.error");
			}

			Collections.sort(taxa);
		}

		return taxa;
	}

}
