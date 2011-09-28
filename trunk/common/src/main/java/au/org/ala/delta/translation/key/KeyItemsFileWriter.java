package au.org.ala.delta.translation.key;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.io.BinaryKeyFileEncoder;
import au.org.ala.delta.key.WriteOnceKeyItemsFile;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.IdentificationKeyCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.NumericAttribute;
import au.org.ala.delta.model.format.Formatter.CommentStrippingMode;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.FilteredDataSet;
import au.org.ala.delta.util.Pair;

/**
 * Writes the key items file using the data in a supplied DeltaContext and
 * associated data set.
 */
public class KeyItemsFileWriter {

	private WriteOnceKeyItemsFile _itemsFile;
	private FilteredDataSet _dataSet;
	private DeltaContext _context;
	private ItemFormatter _itemFormatter;
	private BinaryKeyFileEncoder _encoder;
	
	
	public KeyItemsFileWriter(
			DeltaContext context, 
			FilteredDataSet dataSet,
			ItemFormatter itemFormatter,
			WriteOnceKeyItemsFile itemsFile) {
		_itemsFile = itemsFile;
		_dataSet = dataSet;
		_context = context;
		_itemFormatter = itemFormatter;
		_encoder = new BinaryKeyFileEncoder();
		
	}
	
	public void writeAll() {
		
		writeItems();
		writeCharacterDependencies();
		
		// Need to write the header last as it is updated as each section 
		// is written.
		_itemsFile.writeHeader();
	}
	
	protected void writeCharacterDependencies() {
		List<Integer> dependencyData = _encoder.encodeCharacterDependencies(_dataSet);
		
		_itemsFile.writeCharacterDependencies(dependencyData);
	}
	
	protected void writeItems() {
		
		List<Pair<String, List<BitSet>>> items = new ArrayList<Pair<String,List<BitSet>>>();
		for (int i=1; i<_dataSet.getMaximumNumberOfItems(); i++) {
			items.add(writeItem(_dataSet.getItem(i)));
		}
		_itemsFile.writeItems(items);
	}
	
	protected Pair<String, List<BitSet>> writeItem(Item item) {
		
		String description = _itemFormatter.formatItemDescription(item, CommentStrippingMode.STRIP_ALL);
		List<BitSet> attributes = new ArrayList<BitSet>();
		Iterator<IdentificationKeyCharacter> keyChars = _dataSet.identificationKeyCharacterIterator();
		while (keyChars.hasNext()) {
			IdentificationKeyCharacter keyChar = keyChars.next();
			Attribute attribute = item.getAttribute(keyChar.getCharacter());
			
			List<Integer> states = new ArrayList<Integer>();
			if (keyChar.getCharacterType().isMultistate()) {
				states = keyChar.getPresentStates((MultiStateAttribute)attribute);
			}
			else if (keyChar.getCharacterType().isNumeric()) {
				states = keyChar.getPresentStates((NumericAttribute)attribute);
				 
			}
			attributes.add(_encoder.encodeAttributeStates(states));
		}
		return new Pair<String, List<BitSet>>(description, attributes);
		
	}
	
}
