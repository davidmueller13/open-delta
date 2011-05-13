package au.org.ala.delta.model.impl;

import java.util.HashMap;
import java.util.Map;

import au.org.ala.delta.model.AbstractObservableDataSet;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.DeltaDataSetFactory;
import au.org.ala.delta.model.Item;


/**
 * Default in-memory implementation of a DeltaDataset.
 * Note that this class is not thread safe.
 *
 */
public class DefaultDataSet extends AbstractObservableDataSet {

	/** The name of this data set */
	private String _name;
	
	private Map<Integer, Item> _items;
	
	private Map<Integer, Character> _characters;
	
	private DeltaDataSetFactory _factory;
	
	private boolean _modified;
	
	public DefaultDataSet(DeltaDataSetFactory factory) {
		_factory = factory;
		_items = new HashMap<Integer, Item>();
		_characters = new HashMap<Integer, Character>();
		_modified = false;
	}
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@Override
	public Item doGetItem(int number) {
		return _items.get(number);
	}

	@Override
	public String getAttributeAsString(int itemNumber, int characterNumber) {
		return getItem(itemNumber).getAttribute(getCharacter(characterNumber)).getValue();
	}

	@Override
	public Character doGetCharacter(int number) {
		return 	_characters.get(number);
	}

	@Override
	public int getNumberOfCharacters() {
		return _characters.size();
	}

	@Override
	public int getMaximumNumberOfItems() {
		return _items.size();
	}

	@Override
	public void close() {
		// Do nothing.
	}
	
	@Override
	protected Character doAddCharacter(int characterNumber, CharacterType type) {
		
		Character character = _factory.createCharacter(type, characterNumber);
		_characters.put(characterNumber, character);
		
		return character;
	}
	
	@Override
	protected Item doAddItem(int itemNumber) {
		
		Item item = _factory.createItem(itemNumber);
		_items.put(itemNumber, item);

		return item;	
	}
	

	@Override
	protected Item doAddVariantItem(int parentItemNumber, int itemNumber) {
		Item parent = getItem(parentItemNumber);
		
		Item item = _factory.createVariantItem(parent, itemNumber);
		_items.put(itemNumber, item);

		return item;	
	}

	@Override
	public boolean isModified() {
		return _modified;
	}
	
	public void setModified(boolean modified) {
		_modified = modified;
	}
	
	@Override
	public void deleteItem(Item item) {
		
		int numItems = _items.size();
		_items.remove(item);
		
		renumberItems(item.getItemNumber()+1, numItems, -1);
		
		fireItemDeleted(item);
	}

	@Override
	public void moveItem(Item item, int newItemNumber) {
		int oldItemNumber = item.getItemNumber();
		
		_items.remove(oldItemNumber);
		if (newItemNumber < oldItemNumber) {
			renumberItems(newItemNumber, oldItemNumber-1, 1);
		}
		else if (newItemNumber > oldItemNumber ){
			renumberItems(oldItemNumber+1, newItemNumber, -1);
		}
		item.setItemNumber(newItemNumber);
		_items.put(newItemNumber,item);
		
	}
	
	private void renumberItems(int from, int to, int change) {
		Map<Integer, Item> tempItems = new HashMap<Integer, Item>();
		for (int i=from; i<=to; i++) {
			
			Item tmp = doGetItem(i);
			tmp.setItemNumber(i+change);
			_items.remove(i);
			tempItems.put(i+change, tmp);		
		}
		_items.putAll(tempItems);
	}
}
