package au.org.ala.delta.intkey.directives;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.lang.math.IntRange;

import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.intkey.directives.invocation.DefineTaxaDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.ui.UIUtils;
import au.org.ala.delta.model.Item;

public class DefineTaxaDirective extends IntkeyDirective {

    public DefineTaxaDirective() {
        super("define", "taxa");
    }

    @Override
    public int getArgType() {
        return DirectiveArgType.DIRARG_KEYWORD_CHARLIST;
    }

    @Override
    protected IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        String keyword = null;
        Set<Integer> taxonNumbers = new HashSet<Integer>();
        if (data != null) {
            List<String> tokens = ParsingUtils.tokenizeDirectiveCall(data);

            for (int i = 0; i < tokens.size(); i++) {
                String token = tokens.get(i);

                if (i == 0) {
                    keyword = ParsingUtils.removeEnclosingQuotes(token);
                } else {
                    IntRange r = ParsingUtils.parseIntRange(token);
                    if (r != null) {
                        for (int charNum : r.toArray()) {
                            taxonNumbers.add(charNum);
                        }
                    } else {
                        try {
                            List<Item> taxonList = context.getTaxaForKeyword(token);
                            for (Item taxon : taxonList) {
                                taxonNumbers.add(taxon.getItemNumber());
                            }
                        } catch (IllegalArgumentException ex) {
                            throw new IntkeyDirectiveParseException(String.format("Invalid taxon keyword %s", token));
                        }
                    }
                }
            }
        }
        
        if (keyword == null) {
            keyword = "foo";
        }

        if (taxonNumbers.size() == 0) {
            List<Item> taxa = context.getDirectivePopulator().promptForTaxa("DEFINE TAXA");
            System.out.println(taxa);
        }

        return new DefineTaxaDirectiveInvocation(keyword, taxonNumbers);
    }
}
