package au.org.ala.delta.intkey.directives;

import java.util.Queue;

import au.org.ala.delta.intkey.model.IntkeyContext;


public abstract class AbstractTaxonListArgument<T> extends IntkeyDirectiveArgument<T> {

    protected static final String OVERRIDE_EXCLUDED_TAXA = "/T";

    protected SelectionMode _defaultSelectionMode;
    protected boolean _selectFromAll;
    
    public AbstractTaxonListArgument(String name, String promptText, SelectionMode defaultSelectionMode, boolean selectFromAll) {
        super(name, promptText, null);
        _defaultSelectionMode = defaultSelectionMode;
        _selectFromAll = selectFromAll;
    }


}
