package au.org.ala.delta.translation.intkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.intkey.WriteOnceIntkeyItemsFile;
import au.org.ala.delta.io.OutputFileSelector;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterDependency;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.IdentificationKeyCharacter;
import au.org.ala.delta.model.IntegerAttribute;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericAttribute;
import au.org.ala.delta.model.NumericRange;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.translation.FilteredCharacter;
import au.org.ala.delta.translation.FilteredDataSet;
import au.org.ala.delta.translation.FilteredItem;

/**
 * Writes the intkey items file using the data in a supplied DeltaContext and
 * associated data set.
 */
public class IntkeyItemsFileWriter {

	static final int INTEGER_RANGE_WARNING_THRESHOLD = 64;
	static final int INTEGER_RANGE_MAX_THRESHOLD = 200;
	
	private WriteOnceIntkeyItemsFile _itemsFile;
	private FilteredDataSet _dataSet;
	private DeltaContext _context;
	
	public IntkeyItemsFileWriter(DeltaContext context, FilteredDataSet dataSet, WriteOnceIntkeyItemsFile itemsFile) {
		_itemsFile = itemsFile;
		_dataSet = dataSet;
		_context = context;
	}
	
	public void writeAll() {
		writeItemDescriptions();
		writeCharacterSpecs();
		writeCharacterDependencies();
		writeAttributeData();
		writeTaxonImages();
		writeEnableDeltaOutput();
		writeChineseFormat();
		writeCharacterSynonomy();
		writeOmitOr();
		writeUseControllingFirst();
		writeTaxonLinks();
		writeOmitPeriod();
		writeNewParagraph();
		writeNonAutoControllingChars();
		writeSubjectForOutputFiles();
		_itemsFile.writeHeader();
	}
	
	public void writeItemDescriptions() {
		
		List<String> descriptions = new ArrayList<String>(_dataSet.getMaximumNumberOfItems());
		Iterator<FilteredItem> items = _dataSet.filteredItems();
		while (items.hasNext()) {
			descriptions.add(items.next().getItem().getDescription());
		}
		_itemsFile.writeItemDescriptions(descriptions);
	}
	
	public void writeCharacterSpecs() {
		
		List<Integer> types = new ArrayList<Integer>(_dataSet.getNumberOfCharacters());
		List<Integer> states = new ArrayList<Integer>(_dataSet.getNumberOfCharacters());
		List<Float> reliabilities = new ArrayList<Float>(_dataSet.getNumberOfCharacters());
		
	    Iterator<IdentificationKeyCharacter> iterator = _dataSet.identificationKeyCharacterIterator();
		while(iterator.hasNext()) {
			IdentificationKeyCharacter character = iterator.next();
			types.add(typeToInt(character.getCharacterType()));
			states.add(numStates(character));
			reliabilities.add((float)_context.getCharacterReliability(character.getCharacterNumber()));
			
		}
		_itemsFile.writeCharacterSpecs(types, states, reliabilities);	
	}
	
	private int typeToInt(CharacterType type) {
		switch (type) {
		case UnorderedMultiState:
			return 1;
		case OrderedMultiState:
			return 2;
		case IntegerNumeric:
			return 3;
		case RealNumeric:
			return 4;
		case Text:
			return 5;
		}
		
		throw new IllegalArgumentException("Invalid character type: "+type);
	}
	
	private int numStates(IdentificationKeyCharacter character) {
		if (character.getCharacterType().isMultistate()) {
			return character.getNumberOfStates();
		}
		else if (character.getCharacterType().isNumeric()) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public void writeCharacterDependencies() {
		
		Integer[] characters = new Integer[_dataSet.getNumberOfCharacters()];
		Arrays.fill(characters, 0);
		List<Integer> dependencyData = new ArrayList<Integer>(Arrays.asList(characters));
		List<Integer> invertedDependencyData = new ArrayList<Integer>(Arrays.asList(characters));
		
		Iterator<FilteredCharacter> filteredChars = _dataSet.filteredCharacters();
		while (filteredChars.hasNext()) {
			FilteredCharacter filteredChar = filteredChars.next();
			Character character = filteredChar.getCharacter();
			if (character.getCharacterType().isMultistate()) {
				
				MultiStateCharacter multiStateCharacter = (MultiStateCharacter)character;
				addDependencyData(filteredChar.getCharacterNumber(), dependencyData, multiStateCharacter);
			}
			
			addInvertedDependencyData(filteredChar.getCharacterNumber(), invertedDependencyData, character);
			
		}
		_itemsFile.writeCharacterDependencies(dependencyData, invertedDependencyData);
		
	}

	private void addDependencyData(int filteredCharNumber, List<Integer> dependencyData, MultiStateCharacter multiStateCharacter) {
		List<CharacterDependency> dependentCharacters = multiStateCharacter.getDependentCharacters();
		if (dependentCharacters != null && dependentCharacters.size() > 0) {
			dependencyData.set(filteredCharNumber-1, dependencyData.size());
			int numStates = multiStateCharacter.getNumberOfStates();
			int statesOffset = dependencyData.size();
			for (int state=0; state<numStates; state++) {
				dependencyData.add(0);
			}
			for (CharacterDependency dependency : dependentCharacters) {
				int dataOffset = dependencyData.size();
				List<Integer> dependentCharacterNumbers = toRangeList(dependency.getDependentCharacterIds());
				dependencyData.add(dependentCharacterNumbers.size()/2);
				dependencyData.addAll(dependentCharacterNumbers);
				for (int state : dependency.getStates()) {
					dependencyData.set(statesOffset+state-1, dataOffset);
				}
			}
			
		}
	}
	
	private void addInvertedDependencyData(int filteredCharNumber, List<Integer> invertedDependencyData, Character character) {
		List<CharacterDependency> dependencies = character.getControllingCharacters();
		if (dependencies == null || dependencies.size() == 0) {
			return;
		}
		invertedDependencyData.set(filteredCharNumber-1, invertedDependencyData.size());
		invertedDependencyData.add(dependencies.size());
		for (CharacterDependency dependency : dependencies) {
			invertedDependencyData.add(dependency.getControllingCharacterId());
		}
	}
	
	private List<Integer> toRangeList(Set<Integer> values) {
		List<Integer> rangeList = new ArrayList<Integer>();
		List<Integer> list = new ArrayList<Integer>(values);
		if (list.size() == 1) {
			
			rangeList.add(list.get(0));
			rangeList.add(list.get(0));
			return rangeList;
		}
		
		Collections.sort(list);
		
		int first = 0;
		for (int i=1; i<list.size(); i++) {
			if (list.get(i) != list.get(i-1)+1) {
				rangeList.add(list.get(first));
				rangeList.add(list.get(i-1));
				first = i;
			}
		}
		rangeList.add(list.get(first));
		rangeList.add(list.get(list.size()-1));
		return rangeList;
	}
	
	public void writeAttributeData() {
		
		Iterator<IdentificationKeyCharacter> keyChars = _dataSet.identificationKeyCharacterIterator();
		List<IntRange> intRanges = new ArrayList<IntRange>();
		List<List<Float>> keyStateBoundaries = new ArrayList<List<Float>>();
		while (keyChars.hasNext()) {
			IdentificationKeyCharacter keyChar = keyChars.next();
			IntRange minMax = new IntRange(0);
			Set<Float> floats = new HashSet<Float>();
			if (keyChar.getCharacterType().isMultistate()) {
				writeMultiStateAttributes(keyChar);
			}	
			else if (keyChar.getCharacterType() == CharacterType.IntegerNumeric) {
				minMax = writeIntegerAttributes(keyChar.getFilteredCharacterNumber(), keyChar.getCharacter());
			}
			else if (keyChar.getCharacterType() == CharacterType.RealNumeric) {
				
				List<FloatRange> ranges = writeRealAttributes(keyChar.getFilteredCharacterNumber(), keyChar.getCharacter());
				for (FloatRange range : ranges) {
					if (range.getMinimumFloat() != Float.MAX_VALUE) {
						floats.add(range.getMinimumFloat());
					}
					if (range.getMaximumFloat() != Float.MAX_VALUE) {
						floats.add(range.getMaximumFloat());
					}
				}
			}
			else {
				writeTextAttributes(keyChar.getFilteredCharacterNumber(), keyChar.getCharacter());
			}
			intRanges.add(minMax);
			List<Float> floatList = new ArrayList<Float>(floats);
			Collections.sort(floatList);
			keyStateBoundaries.add(floatList);
		}
		_itemsFile.writeMinMaxValues(intRanges);
		_itemsFile.writeKeyStateBoundaries(keyStateBoundaries);
	}
	
	private void writeMultiStateAttributes(IdentificationKeyCharacter character) {
		
		int charNumber = character.getFilteredCharacterNumber();
		int numStates = character.getNumberOfStates();
		List<BitSet> attributes = new ArrayList<BitSet>();
		Iterator<FilteredItem> items = _dataSet.filteredItems();
		while (items.hasNext()) {
			int itemNum = items.next().getItem().getItemNumber();
			MultiStateAttribute attribute = (MultiStateAttribute)_dataSet.getAttribute(itemNum, character.getCharacterNumber());
		
			List<Integer> states = character.getPresentStates(attribute);
			
			// Turn into bitset.
			BitSet bits = new BitSet();
			for (int state : states) {
				bits.set(state-1);
			}
			
			if (attribute.isInapplicable()) {
				bits.set(numStates);
			}
			attributes.add(bits);
		}
		
		_itemsFile.writeAttributeBits(charNumber, attributes, numStates+1);
	}
	
	private IntRange writeIntegerAttributes(int filteredCharacterNumber, Character character) {
		IntRange characterRange = determineIntegerRange(character);
		if (characterRange == null) {
			// The range was too large - treat this character as a real.
			writeRealAttributes(filteredCharacterNumber, character);
			characterRange = new IntRange(0);
		}
		else {
			int charNumber = character.getCharacterId();
			int numStates = characterRange.getMaximumInteger()-characterRange.getMinimumInteger();
			List<BitSet> attributes = new ArrayList<BitSet>();
			for (int i=1; i<=_dataSet.getMaximumNumberOfItems(); i++) {
				
				// Turn into bitset.
				BitSet bits = new BitSet();
				IntegerAttribute attribute = (IntegerAttribute)_dataSet.getAttribute(i, charNumber);
				if (attribute.isUnknown()) {
					attributes.add(bits);
					continue;
				}
				
				if (attribute.isInapplicable()) {
					bits.set(numStates+2);
				}
				
				List<NumericRange> ranges = attribute.getNumericValue();
				
				for (NumericRange range : ranges) {
					Range usedRange;
					if (_context.getUseNormalValues()) {
						usedRange = range.getNormalRange();
					}
					else {
						usedRange = range.getFullRange();
					}
					
					for (int j=usedRange.getMinimumInteger(); j<=usedRange.getMaximumInteger(); j++) {
						if (j<characterRange.getMinimumInteger()) {
							bits.set(0);
						}
						else if (j<characterRange.getMaximumInteger()) {
							bits.set(j - characterRange.getMinimumInteger()+1);
						}
						else {
							bits.set(numStates+1);
						}
					}
				}
				attributes.add(bits);
				
			}
			
			_itemsFile.writeAttributeBits(filteredCharacterNumber, attributes, numStates+3);
		}
		return characterRange;
	}
	
	
	private IntRange determineIntegerRange(Character intChar) {
		
		Set<Integer> values = new HashSet<Integer>();
		boolean hasMultiRangeAttribute = populateValues(intChar.getCharacterId(), values);
		
		List<Integer> orderedValues = new ArrayList<Integer>(values);
		
		if (orderedValues.size() == 0) {
			return new IntRange(0);
		}
		Collections.sort(orderedValues);
		
		int min = orderedValues.get(0);
		int max = orderedValues.get(values.size()-1);
		
		int upperLimit = hasMultiRangeAttribute ? INTEGER_RANGE_MAX_THRESHOLD : INTEGER_RANGE_WARNING_THRESHOLD;
		if (max-min > upperLimit) {
			int index = Collections.binarySearch(orderedValues, min+upperLimit);
			if (index < 0) {
				index = -(index+1);
			}
			
			if (index > values.size()/2) {
				return null;
			}
			max = orderedValues.get(index);
		}
		return new IntRange(min, max);
	}

	/**
	 * Puts all of the values of an integer character into a List as a 
	 * part of the process of converting an integer character into the
	 * equivalent of a multistate character.  Note that CONFOR uses a
	 * fixed sized array of 200 values and starts discarding values after
	 * this which we are not doing.
	 * @param characterNumber the character to use.
	 * @param values the values of all of the attributes using the supplied
	 * character.
	 * @return true if one or more of the attributes have more than a single
	 * range of values encoded.
	 */
	private boolean populateValues(int characterNumber, Set<Integer> values) {
		boolean useNormalValues = _context.getUseNormalValues();
		
		boolean hasMultiRangeAttribute = false;
		for (int i=1; i<_dataSet.getMaximumNumberOfItems(); i++) {
		
			IntegerAttribute attribute = (IntegerAttribute)_dataSet.getAttribute(i, characterNumber);
			if (attribute == null || attribute.isUnknown() || attribute.isInapplicable() || attribute.isVariable()) {
				continue;
			}
			List<NumericRange> ranges = attribute.getNumericValue();
			if (ranges.size() > 1) {
				hasMultiRangeAttribute = true;
			}
			
			for (NumericRange range : ranges) {
				if (!useNormalValues) {
					if (range.hasExtremeLow()) {
						values.add(range.getExtremeLow().intValue());
					}
					if (range.hasExtremeHigh()) {
						values.add(range.getExtremeHigh().intValue());
					}
					
				}
				values.add(range.getNormalRange().getMinimumInteger());
				values.add(range.getNormalRange().getMaximumInteger());
				if (range.hasMiddleValue()) {
					values.add(range.getMiddle().intValue());
				}
			}
			
		}
		return hasMultiRangeAttribute;
	}
	
	private List<FloatRange> writeRealAttributes(int filteredCharNumber, Character realChar) {
		boolean useNormalValues = _context.getUseNormalValues();
		int characterNumber = realChar.getCharacterId();
		
		List<FloatRange> values = new ArrayList<FloatRange>();
		BitSet inapplicableBits = new BitSet();
		for (int i=1; i<=_dataSet.getMaximumNumberOfItems(); i++) {
			
			NumericAttribute attribute = (NumericAttribute)_dataSet.getAttribute(i, characterNumber);
			if (attribute == null || attribute.isUnknown() || attribute.isInapplicable() || attribute.isVariable()) {
				FloatRange range = new FloatRange(Float.MAX_VALUE);
				values.add(range);
				if (attribute.isInapplicable()) {
					inapplicableBits.set(i-1);
				}
				continue;
			}
			List<NumericRange> ranges = attribute.getNumericValue();
			
			Range useRange;
			for (NumericRange range : ranges) {
				if (_context.hasAbsoluteError(characterNumber)) {
					range.setAbsoluteError(_context.getAbsoluteError(characterNumber));
				}
				else if (_context.hasPercentageError(characterNumber)) {
					range.setPercentageError(_context.getPercentageError(characterNumber));
				}
				if (useNormalValues) {
					useRange = range.getNormalRange();
				}
				else {
					useRange = range.getFullRange();
				}
				FloatRange floatRange = new FloatRange(useRange.getMinimumFloat(), useRange.getMaximumFloat());
				values.add(floatRange);
			}
		}
		_itemsFile.writeAttributeFloats(filteredCharNumber, inapplicableBits, values);
		return values;
	}
	
	private void writeTextAttributes(int filteredCharNumber, Character textChar) {
		int characterNumber = textChar.getCharacterId();
		
		List<String> values = new ArrayList<String>();
		BitSet inapplicableBits = new BitSet();
		Iterator<FilteredItem> items = _dataSet.filteredItems();
		while (items.hasNext()) {
			FilteredItem item = items.next();
			Attribute attribute = _dataSet.getAttribute(item.getItem().getItemNumber(), characterNumber);
			
			if (attribute == null || attribute.isUnknown()) {
				values.add("");
				continue;
			}
			if (attribute.isInapplicable()) {
				inapplicableBits.set(item.getItemNumber()-1);
				values.add("");
			}
			else {
				values.add(attribute.getValueAsString());
			}
		}
		_itemsFile.writeAttributeStrings(filteredCharNumber, inapplicableBits, values);
	}
	
	public void writeTaxonImages() {
		List<String> imageList = new ArrayList<String>(_dataSet.getMaximumNumberOfItems());
	
		IntkeyImageWriter imageWriter = new IntkeyImageWriter();
		Iterator<FilteredItem> items = _dataSet.filteredItems();
		while (items.hasNext()) {
			Item item = items.next().getItem();
			List<Image> images = item.getImages();
			imageList.add(imageWriter.imagesToString(images, item));
			
		}
		_itemsFile.writeTaxonImages(imageList);
	}
	
	
	public void writeEnableDeltaOutput() {
		_itemsFile.writeEnableDeltaOutput(_context.isDeltaOutputDisabled());
	}
	
	public void writeChineseFormat() {
		_itemsFile.writeChineseFormat(_context.isChineseFormat());
	}
	
	public void writeCharacterSynonomy() {
		
		List<Boolean> charsForSynonymy = charactersToBooleans(_context.getCharactersForSynonymy());
		_itemsFile.writeCharacterSynonymy(charsForSynonymy);
	}
	
	private List<Boolean> charactersToBooleans(Set<Integer> charNumbers) {
		List<Boolean> booleans = new ArrayList<Boolean>(_dataSet.getNumberOfCharacters());
		Iterator<FilteredCharacter> characters = _dataSet.filteredCharacters();
		while (characters.hasNext()) {
			booleans.add(charNumbers.contains(characters.next().getCharacterNumber()));
		}
		return booleans;
	}
	
	public void writeOmitOr() {
		List<Boolean> booleans = new ArrayList<Boolean>(_dataSet.getNumberOfCharacters());
		Iterator<FilteredCharacter> characters = _dataSet.filteredCharacters();
		while (characters.hasNext()) {
			booleans.add(_context.isOrOmmitedForCharacter(characters.next().getCharacterNumber()));
		}
		_itemsFile.writeOmitOr(booleans);
	}
	
	public void writeUseControllingFirst() {
		Set<Integer> values = new HashSet<Integer>(_dataSet.getNumberOfCharacters());
		Iterator<FilteredCharacter> characters = _dataSet.filteredCharacters();
		while (characters.hasNext()) {
			if (_context.isUseControllingCharacterFirst(characters.next().getCharacterNumber())) {
				values.add(characters.next().getCharacterNumber());
			}
		}
		_itemsFile.writeUseControllingFirst(values);
	}

	public void writeTaxonLinks() {
		int numItems = _dataSet.getMaximumNumberOfItems();
		List<String> taxonLinksList = new ArrayList<String>(numItems);
		for (int i=1; i<=numItems; i++) {
			String taxonLinks = _context.getTaxonLinks(i);
			if (taxonLinks == null) {
				taxonLinks = "";	
			}
			taxonLinksList.add(taxonLinks);
		}
		
		_itemsFile.writeTaxonLinks(0, taxonLinksList);
	}
	
	public void writeOmitPeriod() {
		Set<Integer> values = new HashSet<Integer>(_dataSet.getNumberOfCharacters());
		Iterator<FilteredCharacter> characters = _dataSet.filteredCharacters();
		while (characters.hasNext()) {
			int filteredItemNum = characters.next().getCharacterNumber();
			if (_context.getOmitPeriodForCharacter(filteredItemNum)) {
				values.add(filteredItemNum);
			}
		}
		_itemsFile.writeOmitPeriod(values);
	}
	
	public void writeNewParagraph() {
		_itemsFile.writeNewParagraph(_context.getNewParagraphCharacters());
	}
	
	public void writeNonAutoControllingChars() {
		Set<Integer> values = new HashSet<Integer>(_dataSet.getNumberOfCharacters());
		Iterator<FilteredCharacter> characters = _dataSet.filteredCharacters();
		while (characters.hasNext()) {
			int filteredItemNum = characters.next().getCharacterNumber();
			if (_context.getNonautomaticControllingCharacter(filteredItemNum)) {
				values.add(filteredItemNum);
			}
		}
		_itemsFile.writeNonAutoControllingChars(values);
	}
	
	public void writeSubjectForOutputFiles() {
		OutputFileSelector outputFileSelector = _context.getOutputFileSelector();
		String subject = outputFileSelector.getSubjectForOutputFiles();
		if (StringUtils.isEmpty(subject)) {
			return;
		}
		
		int numItems = _dataSet.getMaximumNumberOfItems();
		List<String> taxonLinksList = new ArrayList<String>(numItems);
		for (int i=1; i<=numItems; i++) {
			StringBuffer text = new StringBuffer();
			
			String outputFile = outputFileSelector.getItemOutputFile(i);
			if (StringUtils.isNotEmpty(outputFile)) {
				text.append(outputFile).append(" ");
				text.append("<@subject ").append(subject).append(">");
			}
			
			taxonLinksList.add(text.toString());
		}
		
		_itemsFile.writeTaxonLinks(1, taxonLinksList);
	}
}
