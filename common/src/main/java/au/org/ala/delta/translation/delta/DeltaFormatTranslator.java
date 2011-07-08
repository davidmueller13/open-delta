package au.org.ala.delta.translation.delta;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericCharacter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.AbstractDataSetTranslator;
import au.org.ala.delta.translation.Printer;
import au.org.ala.delta.translation.attribute.AttributeParser;
import au.org.ala.delta.translation.attribute.ParsedAttribute;
import au.org.ala.delta.translation.attribute.ParsedAttribute.Values;

/**
 * The DeltaFormatTranslator can reformat the output of the 
 * ITEM DESCRIPTIONS and CHARACTER LIST directives.  It is used when the
 * TRANSLATE INTO DELTA FORMAT directive is specified.
 */
public class DeltaFormatTranslator extends AbstractDataSetTranslator {

	private Printer _printer;
	private ItemFormatter _itemFormatter;
	private CharacterFormatter _characterFormatter;
	private AttributeParser _parser;
	
	public DeltaFormatTranslator(DeltaContext context, Printer printer, ItemFormatter itemFormatter) {
		super(context, new DeltaFormatDataSetFilter(context));
		
		_printer = printer;
		_itemFormatter = itemFormatter;
		_characterFormatter = new CharacterFormatter();
		 _parser = new AttributeParser();
	}
	
	@Override
	public void beforeFirstItem() {
		outputLine("*ITEM DESCRIPTIONS");
		_printer.writeBlankLines(2, 0);
	}

	@Override
	public void beforeItem(Item item) {
		StringBuilder itemDescription = new StringBuilder();
		itemDescription.append("# ");
		itemDescription.append(_itemFormatter.formatItemDescription(item));
		itemDescription.append("/");
		outputLine(itemDescription.toString());
	}

	@Override
	public void afterItem(Item item) {
		_printer.printBufferLine();
		_printer.writeBlankLines(1, 0);
	}

	@Override
	public void beforeAttribute(Attribute attribute) {
		
		StringBuilder attributeValue = new StringBuilder();
		
		au.org.ala.delta.model.Character character = attribute.getCharacter();
		attributeValue.append(Integer.toString(character.getCharacterId()));
		
		String value = getAttributeValue(attribute);
	    value = _itemFormatter.defaultFormat(value);
		if (StringUtils.isNotEmpty(value)) {
			ParsedAttribute parsedAttribute = _parser.parse(value);
			String charComment = parsedAttribute.getCharacterComment();
			attributeValue.append(charComment);
			
			if (!value.equals(charComment)) {
				attributeValue.append(",");
				attributeValue.append(value.substring(charComment.length()));
			}
			
		}
		else {
			attributeValue.append(",");
		}
		// This is here for CONFOR compatibility, which makes testing
		// easier.
		attributeValue.append(" ");
		
		output(attributeValue.toString());
	}

	private String getAttributeValue(Attribute attribute) {
		String value = attribute.getValueAsString();
		if (attribute instanceof MultiStateAttribute) {
            MultiStateAttribute msAttr = (MultiStateAttribute) attribute;
            if (msAttr.isImplicit()) {
                value = Integer.toString(msAttr.getImplicitValue());
            }
        }
		return value;
	}

	@Override
	public void afterAttribute(Attribute attribute) {}

	@Override
	public void afterLastItem() {}

	@Override
	public void attributeComment(String comment) {}

	@Override
	public void attributeValues(Values values) {}

	
	private void output(String value) {
		_printer.writeJustifiedText(value, -1);
	}
	
	private void outputLine(String value) {
		_printer.indent();
		_printer.writeJustifiedText(value, -1);
		_printer.printBufferLine();
	}

	@Override
	public void beforeFirstCharacter() {
		outputLine("*CHARACTER LIST");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforeCharacter(Character character) {
		_printer.setIndent(0);
		StringBuilder charDescription = new StringBuilder();
		charDescription.append("#");
		charDescription.append(_characterFormatter.formatCharacterDescription(character));
		charDescription.append("/");
		outputLine(charDescription.toString());
		
		if (character.getCharacterType().isMultistate()) {
			outputCharacterStates((MultiStateCharacter)character);
		}
		else if (character.getCharacterType().isNumeric()) {
			outputUnits((NumericCharacter<? extends Number>)character);
		}
	}
	
	public void afterCharacter(Character character) {
		_printer.writeBlankLines(1, 0);
	}
	
	private void outputCharacterStates(MultiStateCharacter character) {
		_printer.setIndent(7);
		for (int i=1; i<=character.getNumberOfStates(); i++) {
			outputLine(_characterFormatter.formatState(character, i)+"/");
		}
	}
	
	private void outputUnits(NumericCharacter<? extends Number> character) {
		_printer.setIndent(7);
		outputLine(_characterFormatter.formatUnits(character)+"/");
	}
}
