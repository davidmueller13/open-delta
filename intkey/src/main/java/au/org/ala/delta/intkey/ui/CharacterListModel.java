package au.org.ala.delta.intkey.ui;

import au.org.ala.delta.model.Character;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import au.org.ala.delta.model.format.CharacterFormatter;

public class CharacterListModel extends AbstractListModel {

    protected List<Character> _characters;
    protected CharacterFormatter _formatter;

    public CharacterListModel(List<Character> characters) {
        _characters = new ArrayList<Character>(characters);
        _formatter = new CharacterFormatter(false, false, false, true, true);
    }

    @Override
    public int getSize() {
        return _characters.size();
    }

    @Override
    public Object getElementAt(int index) {
        return _formatter.formatCharacterDescription(_characters.get(index));
    }

    public Character getCharacterAt(int index) {
        return _characters.get(index);
    }

}
