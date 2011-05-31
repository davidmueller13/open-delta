package au.org.ala.delta.editor.slotfile.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.ala.delta.editor.slotfile.DeltaVOP;
import au.org.ala.delta.editor.slotfile.VOCharBaseDesc;
import au.org.ala.delta.editor.slotfile.VOControllingDesc;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.impl.CharacterDependencyData;

/**
 * Implements the CharacterDependencyData interface using the VOControllingDesc
 * read / write methods.
 */
public class VOControllingAdapter implements CharacterDependencyData {

	private VOControllingDesc _controllingDesc;
	private DeltaVOP _vop;
	
	public VOControllingAdapter(DeltaVOP vop, VOControllingDesc controllingDesc) {
		_vop = vop;
		_controllingDesc = controllingDesc;
	}
	
	public int getId() {
		return _controllingDesc.getUniId();
	}
	
	@Override
	public void addDependentCharacter(Character character) {
		
		int charId = getCharacterId(character);
		_controllingDesc.addControlledChar(charId);
	}

	@Override
	public void removeDependentCharacter(Character character) {
		int charId = getCharacterId(character);
		_controllingDesc.removeControlledChar(charId);
	}

	@Override
	public void setDescription(String description) {
		_controllingDesc.writeLabel(description);
	}

	@Override
	public String getDescription() {
		return _controllingDesc.readLabel();
	}

	@Override
	public Set<Integer> getStates() {
		List<Integer> stateIds = _controllingDesc.readStateIds();
		Set<Integer> stateNumbers = new HashSet<Integer>(stateIds.size());
		VOCharBaseDesc charBase = charBaseForId(_controllingDesc.getCharId());
		for (int id : stateIds) {
			stateNumbers.add(charBase.stateNoFromUniId(id));
		}
		
		return stateNumbers;
	}

	@Override
	public Set<Integer> getDependentCharacterIds() {
		List<Integer> charIds = _controllingDesc.readControlledChars();
		Set<Integer> characterNumbers = new HashSet<Integer>(charIds.size());
		
		for (int id : charIds) {
			characterNumbers.add(_vop.getDeltaMaster().charNoFromUniId(id));
		}
		
		return characterNumbers;
	}

	public int getControllingCharacterId() {
		int charId =_controllingDesc.getCharId();
		return _vop.getDeltaMaster().charNoFromUniId(charId);
	}
	
	private VOCharBaseDesc charBaseForId(int id) {
		return (VOCharBaseDesc)_vop.getDescFromId(id);
	}
	
	private int getCharacterId(Character character) {
		return _vop.getDeltaMaster().uniIdFromCharNo(character.getCharacterId());
	}

}
