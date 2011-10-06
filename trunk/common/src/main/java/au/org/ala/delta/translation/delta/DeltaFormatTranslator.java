package au.org.ala.delta.translation.delta;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericCharacter;
import au.org.ala.delta.model.TextAttribute;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.AbstractDataSetTranslator;
import au.org.ala.delta.translation.Printer;
import au.org.ala.delta.translation.attribute.AttributeParser;
import au.org.ala.delta.translation.attribute.CommentedValueList;
import au.org.ala.delta.translation.attribute.CommentedValueList.Values;
import au.org.ala.delta.util.Utils;

/**
 * The DeltaFormatTranslator can reformat the output of the 
 * ITEM DESCRIPTIONS and CHARACTER LIST directives.  It is used when the
 * TRANSLATE INTO DELTA FORMAT directive is specified.
 */
public class DeltaFormatTranslator extends AbstractDataSetTranslator {

	protected Printer _printer;
	protected ItemFormatter _itemFormatter;
	protected CharacterFormatter _characterFormatter;
	protected AttributeParser _parser;
	
	public DeltaFormatTranslator(
			DeltaContext context, 
			Printer printer, 
			ItemFormatter itemFormatter,
			CharacterFormatter characterFormatter) {
		super(context, new DeltaFormatDataSetFilter(context));
		
		_printer = printer;
		_printer.setIndentOnLineWrap(true);
		_itemFormatter = itemFormatter;
		_characterFormatter = characterFormatter;
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
			CommentedValueList parsedAttribute = _parser.parse(value);
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
		if (attribute instanceof TextAttribute) {
			if (!value.startsWith("<")) {
				value = "<"+value+">";
			}
		}
		if (attribute instanceof MultiStateAttribute) {
            MultiStateAttribute msAttr = (MultiStateAttribute) attribute;
            if (msAttr.isImplicit()) {
                value = Integer.toString(msAttr.getImplicitValue());
            }
        }
		return Utils.despaceRtf(value, false);
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
	
	protected void outputLine(String line) {
		_printer.outputLine(line);
	}
	
	public void afterCharacter(Character character) {
		_printer.writeBlankLines(1, 0);
	}
	
	protected void outputCharacterStates(MultiStateCharacter character) {
		_printer.setIndent(7);
		for (int i=1; i<=character.getNumberOfStates(); i++) {
			outputState(character, i);
		}
	}
	
	protected void outputUnits(NumericCharacter<? extends Number> character) {
		if (character.hasUnits()) {
			_printer.setIndent(7);
			outputLine(_characterFormatter.formatUnits(character)+"/");
		}
	}
	
	protected void outputState(MultiStateCharacter character, int stateNumber) {
		outputLine(_characterFormatter.formatState(character, stateNumber)+"/");
	}
	
	@Override
	public void translateCharacters() {
		_printer.setLineWrapIndent(10);
		super.translateCharacters();
	}
	
	@Override 
	public void translateItems() {
		_printer.setLineWrapIndent(0);
		super.translateItems();
	}
}
