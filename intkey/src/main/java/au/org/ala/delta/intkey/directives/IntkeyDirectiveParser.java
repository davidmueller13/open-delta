package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.Logger;
import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.directives.DirectiveParser;
import au.org.ala.delta.directives.DirectiveSearchResult;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.ui.UIUtils;

public class IntkeyDirectiveParser extends DirectiveParser<IntkeyContext> {

    // private constructor - use factory method to get an instance.
    private IntkeyDirectiveParser() {
        // Intkey needs to be able to match supplied control words of less
        // than the standard 3 characters, e.g. "INCLUDE T" should match the
        // "INCLUDE TAXA" directive.
        getDirectiveTree().setMatchFirstThreeCharacters(false);
    }

    public static IntkeyDirectiveParser createInstance() {
        IntkeyDirectiveParser instance = new IntkeyDirectiveParser();

        instance.registerDirective(new FileCharactersDirective());
        instance.registerDirective(new FileTaxaDirective());
        instance.registerDirective(new FileInputDirective());
        instance.registerDirective(new NewDatasetDirective());
        instance.registerDirective(new UseDirective());
        instance.registerDirective(new DefineCharactersDirective());
        instance.registerDirective(new DefineTaxaDirective());
        instance.registerDirective(new DefineNamesDirective());
        instance.registerDirective(new DefineButtonDirective());
        instance.registerDirective(new DefineInformationDirective());
        instance.registerDirective(new RestartDirective());
        instance.registerDirective(new ChangeDirective());
        instance.registerDirective(new SetRBaseDirective());
        instance.registerDirective(new SetReliabilitiesDirective());
        instance.registerDirective(new SetVaryWtDirective());
        instance.registerDirective(new DisplayCharacterOrderBestDirective());
        instance.registerDirective(new BestDirective());
        instance.registerDirective(new DisplayCharacterOrderNaturalDirective());
        instance.registerDirective(new DisplayCharacterOrderSeparateDirective());
        instance.registerDirective(new DifferencesDirective());
        instance.registerDirective(new SetToleranceDirective());
        instance.registerDirective(new IncludeCharactersDirective());
        instance.registerDirective(new IncludeTaxaDirective());
        instance.registerDirective(new ExcludeCharactersDirective());
        instance.registerDirective(new ExcludeTaxaDirective());
        instance.registerDirective(new SetImagePathDirective());
        instance.registerDirective(new SetInfoPathDirective());
        instance.registerDirective(new SetMatchDirective());
        instance.registerDirective(new SetDiagLevelDirective());
        instance.registerDirective(new CharactersDirective());
        instance.registerDirective(new FileOutputDirective());
        instance.registerDirective(new SetFixDirective());
        instance.registerDirective(new DeleteDirective());
        instance.registerDirective(new IllustrateCharactersDirective());
        instance.registerDirective(new IllustrateTaxaDirective());
        instance.registerDirective(new DescribeDirective());
        instance.registerDirective(new ContentsDirective());
        instance.registerDirective(new FileDisplayDirective());
        instance.registerDirective(new SetAutoToleranceDirective());
        instance.registerDirective(new SetDiagTypeSpecimensDirective());
        instance.registerDirective(new SetDiagTypeTaxaDirective());
        instance.registerDirective(new SetExactDirective());
        instance.registerDirective(new SetStopBestDirective());
        instance.registerDirective(new TaxaDirective());
        instance.registerDirective(new FindCharactersDirective());
        instance.registerDirective(new FindTaxaDirective());
        instance.registerDirective(new DisplayNumberingDirective());
        instance.registerDirective(new DisplayCommentsDirective());
        instance.registerDirective(new DisplayUnknownsDirective());
        instance.registerDirective(new DisplayInapplicablesDirective());
        instance.registerDirective(new DisplayLogDirective());
        instance.registerDirective(new FileJournalDirective());
        instance.registerDirective(new FileLogDirective());
        instance.registerDirective(new FileCloseDirective());
        instance.registerDirective(new CommentDirective());
        instance.registerDirective(new QuitDirective());
        instance.registerDirective(new ShowDirective());
        instance.registerDirective(new SummaryDirective());
        instance.registerDirective(new SimilaritiesDirective());
        instance.registerDirective(new DiagnoseDirective());
        instance.registerDirective(new InformationDirective());
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AbstractDirective<IntkeyContext> processDirective(StringBuilder data, IntkeyContext context) {
        if (data.length() > 0) {
            char ch = data.charAt(0);
            if (Character.isDigit(ch)) {
                DirectiveSearchResult r = getDirectiveTree().findDirective(new ArrayList<String>(Arrays.asList("use")));
                IntkeyDirective useDirective = (IntkeyDirective) r.getDirective();
                try {
                    useDirective.parseAndProcess(context, data.toString());
                } catch (Exception ex) {
                    handleDirectiveProcessingException(context, useDirective, ex);
                }
                return useDirective;
            } else {
                return super.processDirective(data, context);
            }
        }
        return null;
    }

    @Override
    protected void handleUnrecognizedDirective(IntkeyContext context, List<String> controlWords) {
        // TODO eventually all unrecognized directives need to be properly
        // handled. This is here so that
        // intkey dataset can be used with milestone release without implemented
        // directives causing
        // errors
        Logger.log(String.format("Ignoring unrecognized directive: %s ", StringUtils.join(controlWords, " ")));
    }

    @Override
    protected void handleDirectiveProcessingException(IntkeyContext context, AbstractDirective<IntkeyContext> d, Exception ex) {
        ex.printStackTrace();
        String msg;
        if (ex instanceof IntkeyDirectiveParseException) {
            msg = ex.getMessage();
        } else {
            msg = String.format("Error occurred while processing '%s' command: %s", StringUtils.join(d.getControlWords(), " ").toUpperCase(), ex.getMessage());
            Logger.error(ex);
        }

        Logger.log(msg);

        if (!context.isProcessingInputFile()) {
            context.getUI().displayErrorMessage(msg);
        }
    }

}
