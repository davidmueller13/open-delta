package au.org.ala.delta.intkey.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.org.ala.delta.io.BinFile;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.TextAttribute;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.model.format.Formatter.AngleBracketHandlingMode;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageSettings.FontInfo;

public class IntkeyDataset {

    private File _charactersFile;
    private File _itemsFile;
    private ItemsFileHeader _itemsFileHeader;
    private CharactersFileHeader _charactersFileHeader;
    private List<Character> _characters;
    private List<Item> _taxa;
    private BinFile _itemsBinFile;

    private String _heading;
    private String _subHeading;
    private String _validationString;

    private String _mainCharNotesFormattingInfo;
    private String _helpCharNotesFormattingInfo;

    private String _orWord;

    private List<Image> _startupImages;
    private List<Image> _characterKeywordImages;
    private List<Image> _taxonKeywordImages;
    private List<FontInfo> _overlayFonts;

    private boolean _deltaOutputPermitted;
    private boolean _chineseFormat;
    private boolean _itemSubheadingsPresent;

    /**
     * A list of text characters that contain synonmy information. The values of
     * such characters for each taxon contain synonyms for that taxon.
     */
    private List<TextCharacter> _synonymyCharacters;

    public IntkeyDataset() {
        _characters = new ArrayList<Character>();
        _taxa = new ArrayList<Item>();
        _startupImages = new ArrayList<Image>();
        _characterKeywordImages = new ArrayList<Image>();
        _taxonKeywordImages = new ArrayList<Image>();
        _overlayFonts = new ArrayList<FontInfo>();
        _synonymyCharacters = new ArrayList<TextCharacter>();
    }

    public File getCharactersFile() {
        return _charactersFile;
    }

    public File getItemsFile() {
        return _itemsFile;
    }

    public ItemsFileHeader getItemsFileHeader() {
        return _itemsFileHeader;
    }

    public CharactersFileHeader getCharactersFileHeader() {
        return _charactersFileHeader;
    }

    public List<au.org.ala.delta.model.Character> getCharacters() {
        // defensive copy
        return new ArrayList<Character>(_characters);
    }

    public List<Item> getTaxa() {
        // defensive copy
        return new ArrayList<Item>(_taxa);
    }

    public String getHeading() {
        return _heading;
    }

    public String getSubHeading() {
        return _subHeading;
    }

    public String getValidationString() {
        return _validationString;
    }

    void setCharactersFile(File charactersFile) {
        this._charactersFile = charactersFile;
    }

    void setItemsFile(File itemsFile) {
        this._itemsFile = itemsFile;
    }

    void setItemsFileHeader(ItemsFileHeader itemsFileHeader) {
        this._itemsFileHeader = itemsFileHeader;
    }

    void setCharactersFileHeader(CharactersFileHeader charactersFileHeader) {
        this._charactersFileHeader = charactersFileHeader;
    }

    void setCharacters(List<au.org.ala.delta.model.Character> characters) {
        this._characters = characters;
    }

    void setTaxa(List<Item> taxa) {
        this._taxa = taxa;
    }

    void setHeading(String heading) {
        this._heading = heading;
    }

    void setSubHeading(String subHeading) {
        this._subHeading = subHeading;
    }

    void setValidationString(String validationString) {
        this._validationString = validationString;
    }

    public String getMainCharNotesFormattingInfo() {
        return _mainCharNotesFormattingInfo;
    }

    void setMainCharNotesFormattingInfo(String mainCharNotesFormattingInfo) {
        this._mainCharNotesFormattingInfo = mainCharNotesFormattingInfo;
    }

    public String getHelpCharNotesFormattingInfo() {
        return _helpCharNotesFormattingInfo;
    }

    void setHelpCharNotesFormattingInfo(String helpCharNotesFormattingInfo) {
        this._helpCharNotesFormattingInfo = helpCharNotesFormattingInfo;
    }

    public String getOrWord() {
        return _orWord;
    }

    void setOrWord(String orWord) {
        this._orWord = orWord;
    }

    public List<Image> getStartupImages() {
        // defensive copy
        return new ArrayList<Image>(_startupImages);

    }

    void setStartupImages(List<Image> startupImages) {
        _startupImages = new ArrayList<Image>(startupImages);
    }

    public List<Image> getCharacterKeywordImages() {
        // defensive copy
        return new ArrayList<Image>(_characterKeywordImages);
    }

    void setCharacterKeywordImages(List<Image> characterKeywordImages) {
        this._characterKeywordImages = new ArrayList<Image>(characterKeywordImages);
    }

    public List<Image> getTaxonKeywordImages() {
        // defensive copy
        return new ArrayList<Image>(_taxonKeywordImages);
    }

    void setTaxonKeywordImages(List<Image> taxonKeywordImages) {
        this._taxonKeywordImages = new ArrayList<Image>(taxonKeywordImages);
    }

    public List<FontInfo> getOverlayFonts() {
        // return defensive copy
        return new ArrayList<FontInfo>(_overlayFonts);
    }

    void setOverlayFonts(List<FontInfo> overlayFonts) {
        this._overlayFonts = overlayFonts;
    }

    public boolean isDeltaOutputPermitted() {
        return _deltaOutputPermitted;
    }

    void setDeltaOutputPermitted(boolean deltaOutputPermitted) {
        this._deltaOutputPermitted = deltaOutputPermitted;
    }

    public boolean isChineseFormat() {
        return _chineseFormat;
    }

    void setChineseFormat(boolean chineseFormat) {
        this._chineseFormat = chineseFormat;
    }

    public Character getCharacter(int charNum) {
        if (charNum < 1 || charNum > _characters.size()) {
            throw new IllegalArgumentException("Invalid character number " + charNum);
        }
        return _characters.get(charNum - 1);
    }

    public int getNumberOfCharacters() {
        return _characters.size();
    }

    public Item getTaxon(int taxonNum) {
        if (taxonNum < 1 || taxonNum > _taxa.size()) {
            throw new IllegalArgumentException("Invalid taxon number " + taxonNum);
        }
        return _taxa.get(taxonNum - 1);
    }

    public int getNumberOfTaxa() {
        return _taxa.size();
    }

    void setItemsBinFile(BinFile itemsBinFile) {
        _itemsBinFile = itemsBinFile;
    }

    public List<Attribute> getAttributesForCharacter(int charNo) {
        List<Attribute> attrList = IntkeyDatasetFileReader.readAttributesForCharacter(_itemsFileHeader, _itemsBinFile, _characters, _taxa, charNo);
        return attrList;
    }

    public Attribute getAttribute(int itemNo, int charNo) {
        List<Attribute> attrList = IntkeyDatasetFileReader.readAttributesForCharacter(_itemsFileHeader, _itemsBinFile, _characters, _taxa, charNo);
        return attrList.get(itemNo - 1);
    }

    public boolean realCharacterKeyStateBoundariesPresent() {
        return _itemsFileHeader.getLSbnd() > 0;
    }

    public List<TextCharacter> getSynonymyCharacters() {
        // defensive copy
        return new ArrayList<TextCharacter>(_synonymyCharacters);
    }

    public void setSynonymyCharacters(List<TextCharacter> synonymyCharacters) {
        _synonymyCharacters = new ArrayList<TextCharacter>(synonymyCharacters);
    }

    public Item getTaxonByName(String taxonName) {
        ItemFormatter formatter = new ItemFormatter(false, true, AngleBracketHandlingMode.RETAIN, true, false);
        for (Item taxon : _taxa) {
            // String comments, RTF etc. from taxon description
            String formattedTaxonName = formatter.formatItemDescription(taxon);

            if (formattedTaxonName.equalsIgnoreCase(taxonName)) {
                return taxon;
            }
        }
        return null;
    }

    public Map<Item, List<TextAttribute>> getSynonymyAttributesForTaxa() {
        Map<Item, List<TextAttribute>> taxonSynonymyAttributes = new HashMap<Item, List<TextAttribute>>();

        for (Item taxon : _taxa) {
            List<TextAttribute> synonymyAttributesList = new ArrayList<TextAttribute>();
            taxonSynonymyAttributes.put(taxon, synonymyAttributesList);
        }

        for (TextCharacter ch : _synonymyCharacters) {
            List<Attribute> attrs = getAttributesForCharacter(ch.getCharacterId());

            for (Attribute attr : attrs) {
                TextAttribute textAttr = (TextAttribute) attr;

                Item taxon = attr.getItem();
                List<TextAttribute> synonymyStringList = taxonSynonymyAttributes.get(taxon);
                synonymyStringList.add(textAttr);
            }
        }

        return taxonSynonymyAttributes;
    }

    public boolean itemSubheadingsPresent() {
        return _itemSubheadingsPresent;
    }

    void setItemSubheadingsPresent(boolean itemSubheadingsPresent) {
        this._itemSubheadingsPresent = itemSubheadingsPresent;
    }
    
    /**
     * Called prior to application shutdown.
     */
    public void cleanup() {
        _itemsBinFile.close();
    }
}
