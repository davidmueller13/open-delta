package au.org.ala.delta.intkey.directives;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.intkey.directives.invocation.IncludeCharactersDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class IncludeCharactersDirective extends IntkeyDirective {
    
    public IncludeCharactersDirective() {
        super("include", "characters");
    }

    @Override
    public int getArgType() {
        return DirectiveArgType.DIRARG_INTKEY_CHARLIST;
    }
    
    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        Set<Integer> includeCharacterNumbers = new HashSet<Integer>();
        
        if (StringUtils.isBlank(data)) {
            List<au.org.ala.delta.model.Character> selectedCharacters = context.getDirectivePopulator().promptForCharacters("INCLUDE CHARACTERS");
            for (au.org.ala.delta.model.Character ch: selectedCharacters) {
                includeCharacterNumbers.add(ch.getCharacterId());
            }
        } else {
            throw new NotImplementedException();
        }
        
        if (includeCharacterNumbers.size() == 0) {
            return null;
        }
        
        return new IncludeCharactersDirectiveInvocation(includeCharacterNumbers);
    }
}
