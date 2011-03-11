package au.org.ala.delta.slotfile.model;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.model.impl.CharacterData;
import au.org.ala.delta.slotfile.TextType;
import au.org.ala.delta.slotfile.VOCharBaseDesc;
import au.org.ala.delta.slotfile.VOCharTextDesc;

public class VOCharacterAdaptor implements CharacterData {
	/** If they've been specified, units are stored as state text for state number 1. */
	private static final int UNITS_TEXT_STATE_NUMBER = 1;
	
	private VOCharBaseDesc _charDesc;
	private VOCharTextDesc _textDesc;
	
	public VOCharacterAdaptor(VOCharBaseDesc charBase, VOCharTextDesc textDesc) {
		_charDesc = charBase;
		_textDesc = textDesc;
	}

	
	@Override
	public String getName() {
		List<String> states = new ArrayList<String>();
		String[] text = _textDesc.ReadAllText(TextType.RTF, states);
		return text[0];
	}

	
	@Override
	public String getDescription() {
		return _textDesc.readFeatureText(TextType.RTF);
	}
	
	@Override
	public boolean isExclusive() {
		return _charDesc.testCharFlag(VOCharBaseDesc.CHAR_EXCLUSIVE);
	}

	@Override
	public boolean isMandatory() {
		return _charDesc.testCharFlag(VOCharBaseDesc.CHAR_MANDATORY);
	}


	public VOCharBaseDesc getCharBaseDesc() {
		return _charDesc;
	}

	@Override
	public String getUnits() {
		String units = "";
		if (_charDesc.getNStatesUsed() >= UNITS_TEXT_STATE_NUMBER) {
			units = getStateText(UNITS_TEXT_STATE_NUMBER);
		}
		return units;
	}
	
	@Override
	public String getStateText(int stateNumber) {
		int stateId = _charDesc.uniIdFromStateNo(stateNumber);
		return _textDesc.readStateText(stateId, TextType.UTF8);
	}


	@Override
	public int getNumberOfStates() {
		// Trying to read past the number of states actually used yields an error
		return _charDesc.getNStatesUsed();
		// return _charDesc.getNStates();
	}

	
	
}
