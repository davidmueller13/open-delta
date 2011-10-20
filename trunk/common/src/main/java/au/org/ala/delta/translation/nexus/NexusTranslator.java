package au.org.ala.delta.translation.nexus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.DeltaContext.HeadingType;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.IdentificationKeyCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericAttribute;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.FilteredCharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.DataSetTranslator;
import au.org.ala.delta.translation.FilteredDataSet;
import au.org.ala.delta.translation.FilteredItem;
import au.org.ala.delta.translation.PrintFile;
import au.org.ala.delta.translation.naturallanguage.NaturalLanguageDataSetFilter;

/**
 * Implements the translation into Nexus format as specified using the TRANSLATE
 * INTO NEXUS FORMAT directive.
 */
public class NexusTranslator implements DataSetTranslator {

	private enum PARAMETER {
		ASSUMPTIONS("#ASSUMPTIONS"), CHARLABELS("#CHARLABELS"), DATA("#DATA"), DIMENSIONS("#DIMENSIONS"), END("#END"), FORMAT(
				"#FORMAT"), HEADING("#HEADING"), STATELABELS("#STATELABELS"), MATRIX("#MATRIX"), NEXUS("#NEXUS"), TYPESET(
				"#TYPESET"), WTSET("#WTSET");

		private String _name;

		private PARAMETER(String name) {
			_name = name.substring(1, 3).toUpperCase();
		}

		public boolean matchesName(String name) {
			name = name.toUpperCase();
			if (name.length() < 3) {
				throw new IllegalArgumentException("Parameters must be at least 2 characters long");
			}
			return _name.equals(name.substring(1, 3));
		}

		public static PARAMETER fromName(String name) {
			for (PARAMETER param : PARAMETER.values()) {
				if (param.matchesName(name)) {
					return param;
				}
			}
			throw new IllegalArgumentException("Invalid paramater: " + name);
		}
	};

	private DeltaContext _context;
	private PrintFile _outputFile;
	private FilteredDataSet _dataSet;
	private CharacterFormatter _characterFormatter;
	private ItemFormatter _itemFormatter;

	public NexusTranslator(DeltaContext context, PrintFile outputFile, 
			CharacterFormatter characterFormatter, ItemFormatter itemFormatter) {
		_context = context;
		_dataSet = new FilteredDataSet(context, new NaturalLanguageDataSetFilter(context));
		_outputFile = outputFile;
		_characterFormatter = characterFormatter;
		_itemFormatter = itemFormatter;
	}

	@Override
	public void translateCharacters() {
	}

	@Override
	public void translateItems() {
	}

	/**
	 * The output parameters accepted by a Nexus translation are:
	 * <ul>
	 * <li>ASSUMPTIONS</li>
	 * <li>CHARLABELS</li>
	 * <li>DATA</li>
	 * <li>DIMENSIONS</li>
	 * <li>END</li>
	 * <li>FORMAT</li>
	 * <li>HEADING</li>
	 * <li>STATELABELS</li>
	 * <li>MATRIX</li>
	 * <li>NEXUS</li>
	 * <li>TYPESET</li>
	 * <li>WTSET</li>
	 * </ul>
	 */
	@Override
	public void translateOutputParameter(String parameterName) {
		System.out.println("Parameter: " + parameterName);

		if (!parameterName.startsWith("#")) {
			_outputFile.outputLine(parameterName);
			return;
		}
		ParameterTranslator translator = null;
		PARAMETER param = PARAMETER.fromName(parameterName);
		switch (param) {
		case ASSUMPTIONS:
			translator = new Command("BEGIN ASSUMPTIONS;");
			break;
		case CHARLABELS:
			translator = new CharLabels();
			break;
		case DATA:
			translator = new Command("BEGIN DATA");
			break;
		case DIMENSIONS:
			translator = new Dimensions();
			break;
		case END:
			translator = new Command("END");
			break;
		case FORMAT:
			translator = new Format();
			break;
		case HEADING:
			translator = new Heading();
			break;
		case STATELABELS:
			translator = new StateLabels();
			break;
		case MATRIX:
			translator = new Matrix();
			break;
		case NEXUS:
			translator = new Literal("#NEXUS", 1);
			break;
		case TYPESET:
			translator = new TypeSet();
			break;
		case WTSET:
			translator = new WtSet();
			break;
		default:
			throw new IllegalArgumentException("Unsupported parameter: " + parameterName);
		}
		
		translator.translateParameter(parameterName);
	}

	abstract class ParameterTranslator {
		public abstract void translateParameter(String parameter);
		
		protected String comment(String comment) {
			StringBuilder commentBuffer = new StringBuilder();
			commentBuffer.append("[").append(comment).append("]");
			return commentBuffer.toString();
		}
		
		protected void command(String command) {
			_outputFile.outputLine(command+";");
		}
	}
	private static final int MAX_LENGTH = 30;
	private String truncate(String value) {
		if (value.length() < MAX_LENGTH) {
			return value;
		}
		else {
			return value.substring(0, MAX_LENGTH);
		}
	}
	class CharLabels extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {

			_outputFile.outputLine("CHARLABELS");
			Iterator<IdentificationKeyCharacter> characters = _dataSet.identificationKeyCharacterIterator();
			while(characters.hasNext()) {
				outputCharacter(characters.next());
			}
			_outputFile.outputLine(";");
		}
		
		private void outputCharacter(IdentificationKeyCharacter character) {
			FilteredCharacterFormatter _formatter = new FilteredCharacterFormatter();
			CharacterFormatter charFormatter = new CharacterFormatter();
			String description = charFormatter.formatCharacterDescription(character.getCharacter());
			StringBuilder charOut = new StringBuilder();
			charOut.append(comment(_formatter.formatCharacterNumber(character)));
			charOut.append(" ");
			charOut.append("'").append(truncate(description)).append("'");
			_outputFile.outputLine(charOut.toString());
			
		}
	}

	class Data extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {

		}
	}

	class Dimensions extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {
			StringBuilder dimensions = new StringBuilder();
			dimensions.append("DIMENSIONS NTAX=").append(_dataSet.getNumberOfFilteredItems());
			dimensions.append(" NCHAR=").append(_dataSet.getNumberOfFilteredCharacters());
			dimensions.append(";");
			
			_outputFile.outputLine(dimensions.toString());
		}
	}
	
	class Literal extends ParameterTranslator {
		private String _value;
		private int _trailingLines;
		
		public Literal(String value, int trailingLines) {
			_value = value;
			_trailingLines = trailingLines;
		}
		
		@Override
		public void translateParameter(String parameter) {
			_outputFile.outputLine(_value);
			_outputFile.writeBlankLines(_trailingLines, 0);
		}
	}

	class Command extends ParameterTranslator {
		private String _value;
		private int _trailingLines;
		public Command(String value) {
			this(value, 0);
		}
		public Command(String value, int trailingBlankLines) {
			_value = value;
			_trailingLines = trailingBlankLines;
		}
		@Override
		public void translateParameter(String parameter) {
			command(_value);
			_outputFile.writeBlankLines(_trailingLines, 0);
		}
	}

	class Format extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {
			StringBuilder format = new StringBuilder();
			format.append("FORMAT MISSING=? GAP=- SYMBOLS=");
			
			_outputFile.outputLine(format.toString());
		}
	}

	class Heading extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {
			_outputFile.outputLine(comment("!"+_context.getHeading(HeadingType.HEADING)));
		}
	}

	class StateLabels extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {
			_outputFile.outputLine("STATELABELS");
			Iterator<IdentificationKeyCharacter> characters = _dataSet.identificationKeyCharacterIterator();
			while(characters.hasNext()) {
				outputCharacterStates(characters.next());
			}
			_outputFile.outputLine(";");
		}
		
		private void outputCharacterStates(IdentificationKeyCharacter character) {
			StringBuilder states = new StringBuilder();
			states.append(character.getFilteredCharacterNumber());
			states.append(" ");
			boolean hasKeyStates = !character.getStates().isEmpty();
			for (int i=1; i<=character.getNumberOfStates(); i++) {
				states.append("'");
				if (hasKeyStates) {
					states.append(character.getKeyState(i));
				}
				else {
					states.append(((MultiStateCharacter)character.getCharacter()).getState(i));
				}
				states.append("'");
				if (i != character.getNumberOfStates()) {
					states.append(" ");
				}
			}
			states.append(",");
			_outputFile.outputLine(states.toString());
		}
	}

	class Matrix extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {
			Iterator<FilteredItem> items = _dataSet.filteredItems();
			while (items.hasNext()) {
				Item item = items.next().getItem();
				Iterator<IdentificationKeyCharacter> characters = _dataSet.identificationKeyCharacterIterator();
				StringBuilder statesOut = new StringBuilder();
				writeItem(item);
				while (characters.hasNext()) {
					IdentificationKeyCharacter character = characters.next();
					Attribute attribute = item.getAttribute(character.getCharacter());
					List<Integer> states = new ArrayList<Integer>();
					if (attribute instanceof MultiStateAttribute) {
					    states.addAll(character.getPresentStates((MultiStateAttribute)attribute));
					}
					else if (attribute instanceof NumericAttribute) {
						states.addAll(character.getPresentStates((NumericAttribute)attribute));
					}
					addStates(statesOut, states);
				}
				_outputFile.outputLine(statesOut.toString());
			}
		}
		
		private void writeItem(Item item) {
			StringBuilder itemOut = new StringBuilder();
			itemOut.append("'");
			itemOut.append(_itemFormatter.formatItemDescription(item));
			itemOut.append("'");
			_outputFile.outputLine(itemOut.toString());
		}
		
		private void addStates(StringBuilder statesOut, List<Integer> states) {
			if (states.size() == 0) {
				statesOut.append("?");
			}
			else if (states.size() > 1) {
				statesOut.append("(");
			}
			for (int state : states) {
				statesOut.append(state);
			}
			if (states.size() > 1) {
				statesOut.append(")");
			}
		}
	}
	
	

	class Nexus extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {

		}
	}

	class TypeSet extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {

		}
	}

	class WtSet extends ParameterTranslator {
		@Override
		public void translateParameter(String parameter) {

		}
	}

}
