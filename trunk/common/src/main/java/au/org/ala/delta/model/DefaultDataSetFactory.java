package au.org.ala.delta.model;

import java.io.File;
import java.util.Set;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.ConforDirectiveFileParser;
import au.org.ala.delta.model.impl.DefaultAttributeData;
import au.org.ala.delta.model.impl.DefaultCharacterData;
import au.org.ala.delta.model.impl.DefaultCharacterDependencyData;
import au.org.ala.delta.model.impl.DefaultDataSet;
import au.org.ala.delta.model.impl.DefaultItemData;
import au.org.ala.delta.model.impl.ItemData;


/**
 * Creates DeltaDataSets backed by in-memory model objects.
 */
public class DefaultDataSetFactory implements DeltaDataSetFactory {

	public static MutableDeltaDataSet load(File file) throws Exception {
		DeltaContext context = new DeltaContext();
		ConforDirectiveFileParser parser = ConforDirectiveFileParser.createInstance();
		parser.parse(file, context);
		return context.getDataSet();
	}
	
	@Override
	public MutableDeltaDataSet createDataSet(String name) {
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
        Attribute attribute = AttributeFactory.newAttribute(character, new DefaultAttributeData(character));
        attribute.setItem(item);
        return attribute;
    }

	@Override
	public CharacterDependency createCharacterDependency(
			MultiStateCharacter owningCharacter, Set<Integer> states,
			Set<Integer> dependentCharacters) {

		DefaultCharacterDependencyData impl = new DefaultCharacterDependencyData(
				owningCharacter.getCharacterId(), states, dependentCharacters);
		
		return new CharacterDependency(impl);
	}
    
    
    

}
