package au.org.ala.delta.model;

import au.org.ala.delta.model.impl.DefaultAttributeData;
import au.org.ala.delta.model.impl.DefaultCharacterData;
import au.org.ala.delta.model.impl.DefaultDataSet;
import au.org.ala.delta.model.impl.DefaultItemData;
import au.org.ala.delta.model.impl.ItemData;


/**
 * Creates DeltaDataSets backed by in-memory model objects.
 */
public class DefaultDataSetFactory implements DeltaDataSetFactory {

	@Override
	public DeltaDataSet createDataSet(String name) {
		return new DefaultDataSet(this);
	}

	@Override
	public Item createItem(int number) {
		ItemData defaultData = new DefaultItemData();
		Item item = new Item(defaultData, number);
		
		return item;
	}
	
	@Override
	public Item createVariantItem(Item parent, int itemNumber) {
		ItemData defaultData = new DefaultItemData();
		Item item = new VariantItem(parent, defaultData, itemNumber);
		
		return item;
	}

	@Override
	public Character createCharacter(CharacterType type, int number) {
		Character character = CharacterFactory.newCharacter(type, number);
		character.setImpl(new DefaultCharacterData());
		
		return character;
	}

    @Override
    public Attribute createAttribute(Character character, Item item) {
        Attribute attribute = AttributeFactory.newAttribute(character, new DefaultAttributeData());
        attribute.setItem(item);
        return attribute;
    }

}
