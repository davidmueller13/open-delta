package au.org.ala.delta.translation.naturallanguage;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.VariantItem;
import au.org.ala.delta.translation.AbstractDataSetFilter;
import au.org.ala.delta.translation.DataSetFilter;

/**
 * The DataSetFilter is responsible for determining whether elements of a DeltaDataSet
 * should be included in a translation operation.
 */
public class NaturalLanguageDataSetFilter extends AbstractDataSetFilter implements DataSetFilter {

	/**
	 * Creates a new DataSetFilter
	 * @param context
	 */
	public NaturalLanguageDataSetFilter(DeltaContext context) {
		_context = context;
	}
	
	@Override
	public boolean filter(Item item) {
		return !_context.isItemExcluded(item.getItemNumber());
	}
	
	@Override
	public boolean filter(Item item, Character character) {
		
		if (isIncluded(item, character) == 0) {
			return false;
		}
		
		Attribute attribute = item.getAttribute(character);

		
		if (attribute.isUnknown()) { 
			return false;
		}
		if (attribute.isExclusivelyInapplicable()) {
			return false;
		}
		if (item.isVariant()) {
			return outputVariantAttribute((VariantItem)item, character);
		}
		
		if (attribute instanceof MultiStateAttribute && ((MultiStateAttribute)attribute).isImplicit()) {
			return outputImplictValue(attribute);
		}
		
		
		if (!item.hasAttribute(character)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean filter(Character character) {
		return !_context.isCharacterExcluded(character.getCharacterId());
	}
}
