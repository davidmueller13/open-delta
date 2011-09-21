package au.org.ala.delta.intkey.directives.invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.SearchUtils;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.TextAttribute;
import au.org.ala.delta.model.format.AttributeFormatter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.rtf.RTFBuilder;

public class FindTaxaDirectiveInvocation implements IntkeyDirectiveInvocation {

    private String _searchText;

    public void setSearchText(String searchText) {
        this._searchText = searchText;
    }

    @Override
    public boolean execute(IntkeyContext context) {

        ItemFormatter taxonFormatter = new ItemFormatter(false, false, true, false, false, false);
        CharacterFormatter characterFormatter = new CharacterFormatter(false, true, false, false, false);
        AttributeFormatter attributeFormatter = new AttributeFormatter(false, false, true, true, false);

        RTFBuilder builder = new RTFBuilder();
        builder.startDocument();

        Map<Item, List<TextAttribute>> taxaSynonymyAttributes = context.getDataset().getSynonymyAttributesForTaxa();

        for (Item taxon : context.getIncludedTaxa()) {
            List<TextAttribute> taxonSynonymyAttributes = taxaSynonymyAttributes.get(taxon);

            if (SearchUtils.taxonMatches(_searchText, taxon, SearchUtils.getSynonymyStringsForTaxon(taxon, taxaSynonymyAttributes))) {
                builder.appendText(taxonFormatter.formatItemDescription(taxon));
                builder.increaseIndent();
                for (TextAttribute attr : taxonSynonymyAttributes) {
                    builder.appendText(attributeFormatter.formatAttribute(attr));
                }
                builder.decreaseIndent();
            }
        }

        builder.endDocument();

        context.getUI().displayRTFReport(builder.toString(), "Taxa");

        return true;
    }

}
