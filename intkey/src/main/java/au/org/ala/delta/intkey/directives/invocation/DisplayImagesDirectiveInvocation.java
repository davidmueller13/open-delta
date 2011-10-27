package au.org.ala.delta.intkey.directives.invocation;

import java.awt.Color;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import au.org.ala.delta.intkey.model.DisplayImagesReportType;
import au.org.ala.delta.intkey.model.ImageDisplayMode;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.format.Formatter.AngleBracketHandlingMode;
import au.org.ala.delta.model.format.Formatter.CommentStrippingMode;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.rtf.RTFBuilder;

public class DisplayImagesDirectiveInvocation extends IntkeyDirectiveInvocation {

    private ImageDisplayMode _displayMode;
    private DisplayImagesReportType _reportType;

    private ItemFormatter _itemFormatter = new ItemFormatter(false, CommentStrippingMode.STRIP_ALL, AngleBracketHandlingMode.REMOVE, true, false, false);

    public void setDisplayMode(ImageDisplayMode displayMode) {
        this._displayMode = displayMode;
    }

    public void setReportType(DisplayImagesReportType reportType) {
        this._reportType = reportType;
    }

    @Override
    public boolean execute(IntkeyContext context) throws IntkeyDirectiveInvocationException {
        if (_displayMode != null) {
            context.setImageDisplayMode(_displayMode);
        }

        if (_reportType != null) {
            RTFBuilder builder = new RTFBuilder();
            builder.startDocument();

            switch (_reportType) {
            case MISSING_IMAGE_LIST:
                generateMissingImageList(context, builder);
                break;
            case CHARACTER_IMAGE_LIST:
                generateCharacterImageList(context, builder);
                break;
            case TAXON_IMAGE_LIST:
                generateTaxonImageList(context, builder);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized display image report type");
            }

            builder.endDocument();
            context.getUI().displayRTFReport(builder.toString(), "Display");
        }

        return true;
    }

    private void generateMissingImageList(IntkeyContext context, RTFBuilder builder) {
        builder.setTextColor(Color.RED);

        ImageSettings imgSettings = context.getImageSettings();
        List<Character> characters = context.getDataset().getCharacters();
        List<Item> taxa = context.getDataset().getTaxa();

        for (Character ch : characters) {
            List<Image> images = ch.getImages();
            for (Image image : images) {
                String fileName = image.getFileName();
                URL fileURL = imgSettings.findFileOnResourcePath(fileName, true);
                if (fileURL == null) {
                    builder.appendText(MessageFormat.format("Image file ''{0}'' for character {1} does not exist.", fileName, ch.getCharacterId()));
                }
            }
        }

        for (Item taxon : taxa) {
            List<Image> images = taxon.getImages();
            for (Image image : images) {
                String fileName = image.getFileName();
                URL fileURL = imgSettings.findFileOnResourcePath(fileName, true);
                if (fileURL == null) {
                    builder.appendText(MessageFormat.format("Image file ''{0}'' for {1} does not exist.", fileName, _itemFormatter.formatItemDescription(taxon)));
                }
            }
        }
    }

    private void generateCharacterImageList(IntkeyContext context, RTFBuilder builder) {
        List<Character> characters = context.getDataset().getCharacters();

        for (Character ch : characters) {
            List<Image> images = ch.getImages();
            for (Image image : images) {
                String fileName = image.getFileName();
                builder.appendText(fileName);
            }
        }
    }

    private void generateTaxonImageList(IntkeyContext context, RTFBuilder builder) {
        List<Item> taxa = context.getDataset().getTaxa();

        for (Item taxon : taxa) {
            List<Image> images = taxon.getImages();
            for (Image image : images) {
                String fileName = image.getFileName();
                builder.appendText(fileName);
            }
        }
    }

}
