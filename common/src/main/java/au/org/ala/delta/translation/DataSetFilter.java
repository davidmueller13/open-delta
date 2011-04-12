package au.org.ala.delta.translation;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.VariantItem;

/**
 * The DataSetFilter is responsible for determining whether elements of a DeltaDataSet
 * should be included in a translation operation.
 */
public class DataSetFilter {

	/** Configuration for the translation */
	private DeltaContext _context;
	
	/**
	 * Creates a new DataSetFilter
	 * @param context
	 */
	public DataSetFilter(DeltaContext context) {
		_context = context;
	}
	
	/**
	 * Filters the supplied Item.
	 * @param item the Item to filter.
	 * @return true if the item should be included in the translation.
	 */
	public boolean filter(Item item) {
		return !_context.isExcluded(item.getItemNumber());
	}
	
	
	/**
	 * Filters the supplied Attribute.
	 * @param attribute the Attribute to filter.
	 * @return true if the attribute should be included in the translation.
	 */
	public boolean filter(Item item, Character character) {
		
		if (!item.hasAttribute(character)) {
			return false;
		}
		Attribute attribute = item.getAttribute(character);
		
		if (item.isVariant()) {
			return outputVariantAttribute((VariantItem)item, character);
		}

		if (attribute.isImplicit()) {
			return outputImplictValue(attribute);
		}
		return true;
	}
	
	/**
	 * If the INSERT REDUNDANT VARIANT ATTRIBUTES directive has been given
	 * return true. If the OMIT REDUNDANT VARIANT ATTRIBUTES directive has been
	 * given return true only if: 1) The attribute has been coded. 2) The coded
	 * value is different to the value of the attribute in the master Item. If
	 * neither of these directives have been given, return true if the character
	 * has been added.
	 * 
	 * @return true if the attribute should be output.
	 */
	private boolean outputVariantAttribute(VariantItem item, Character character) {
		
		
		Boolean omitRedundantVariantAttributes = _context.getOmitRedundantVariantAttributes();
		if (omitRedundantVariantAttributes == null) {
			if (item.isInherited(character) &&
			    (_context.isCharacterAdded(item.getItemNumber(), character.getCharacterId()) == false)) {
				// Don't output this attribute
				return false;
			}
			System.out.println("attribute: "+item.getItemNumber()+", "+character.getCharacterId()+" not inherited");
		} else if (omitRedundantVariantAttributes == true) {
			
			if (item.isInherited(character) && _context.isCharacterAdded(item.getItemNumber(), character.getCharacterId()) == false) {
				// Don't output this attribute
				return false;
			}
			Attribute attribute = item.getAttribute(character);
			return !(attribute.getValue().equals(item.getParentAttribute(character).getValue()));
				
		}
		
		return true;
	}
	
	private int isIncluded(Item item, Character character) {
		int result = 1;
		int characterNum = character.getCharacterId();
		if (_context.isExcluded(characterNum)) {
			result = 0;
			// if _context.isCharacterAdded(int item, int character) ||
			// _context.isEmphasized(int item, int character) {
			// result = 2;
			// }
		}

		return result;
	}
	
	private boolean outputImplictValue(Attribute attribute) {
		if (isIncluded(attribute.getItem(), attribute.getCharacter()) == 1) {
		    return _context.insertImplicitValues();
		}
		return true;
	}
}
