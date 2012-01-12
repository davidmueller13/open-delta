package au.org.ala.delta.key;

import java.util.HashMap;
import java.util.Map;

public class ConfirmatoryCharacter {

    private int _characterNumber;
    private int _mainCharacterNumber;
    private Map<Integer, Integer> _mainToConfirmatoryStateMap;

    public ConfirmatoryCharacter(int characterNumber, int mainCharacterNumber, Map<Integer, Integer> mainToConfirmatoryStateMap) {
        _characterNumber = characterNumber;
        _mainCharacterNumber = mainCharacterNumber;
        _mainToConfirmatoryStateMap = new HashMap<Integer, Integer>(mainToConfirmatoryStateMap);
    }

    public int getConfirmatoryCharacterNumber() {
        return _characterNumber;
    }

    public int getMainCharacterNumber() {
        return _mainCharacterNumber;
    }

    public int getConfirmatoryStateNumber(int mainCharacterState) {
        if (_mainToConfirmatoryStateMap.containsKey(mainCharacterState)) {
            return _mainToConfirmatoryStateMap.get(mainCharacterState);
        } else {
            return -1;
        }
    }
}
