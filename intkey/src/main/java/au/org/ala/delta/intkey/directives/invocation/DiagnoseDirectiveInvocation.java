package au.org.ala.delta.intkey.directives.invocation;

import java.util.List;

import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.util.Pair;

public class DiagnoseDirectiveInvocation extends IntkeyDirectiveInvocation {

    private List<Item> _taxa;
    private List<Character> _characters;

    public void setSelectedTaxaSpecimen(Pair<List<Item>, Boolean> pair) {
        this._taxa = pair.getFirst();
        // the SPECIMEN cannot be selected with the taxa for this directive.
        // Simply ignore
        // it if "SPECIMEN" is supplied
    }

    public void setCharacters(List<Character> characters) {
        this._characters = characters;
    }

    @Override
    public boolean execute(IntkeyContext context) {
        // TODO Auto-generated method stub
        return false;
    }

}
