/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.intkey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;

import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.FontUIResource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.Logger;
import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.intkey.directives.ChangeDirective;
import au.org.ala.delta.intkey.directives.CharactersDirective;
import au.org.ala.delta.intkey.directives.CommentDirective;
import au.org.ala.delta.intkey.directives.ContentsDirective;
import au.org.ala.delta.intkey.directives.DefineButtonDirective;
import au.org.ala.delta.intkey.directives.DefineCharactersDirective;
import au.org.ala.delta.intkey.directives.DefineEndIdentifyDirective;
import au.org.ala.delta.intkey.directives.DefineInformationDirective;
import au.org.ala.delta.intkey.directives.DefineNamesDirective;
import au.org.ala.delta.intkey.directives.DefineSubjectsDirective;
import au.org.ala.delta.intkey.directives.DefineTaxaDirective;
import au.org.ala.delta.intkey.directives.DescribeDirective;
import au.org.ala.delta.intkey.directives.DiagnoseDirective;
import au.org.ala.delta.intkey.directives.DifferencesDirective;
import au.org.ala.delta.intkey.directives.DisplayCharacterOrderBestDirective;
import au.org.ala.delta.intkey.directives.DisplayCharacterOrderNaturalDirective;
import au.org.ala.delta.intkey.directives.DisplayCharacterOrderSeparateDirective;
import au.org.ala.delta.intkey.directives.DisplayCommentsDirective;
import au.org.ala.delta.intkey.directives.DisplayContinuousDirective;
import au.org.ala.delta.intkey.directives.DisplayEndIdentifyDirective;
import au.org.ala.delta.intkey.directives.DisplayImagesDirective;
import au.org.ala.delta.intkey.directives.DisplayInapplicablesDirective;
import au.org.ala.delta.intkey.directives.DisplayInputDirective;
import au.org.ala.delta.intkey.directives.DisplayKeywordsDirective;
import au.org.ala.delta.intkey.directives.DisplayLogDirective;
import au.org.ala.delta.intkey.directives.DisplayNumberingDirective;
import au.org.ala.delta.intkey.directives.DisplayScaledDirective;
import au.org.ala.delta.intkey.directives.DisplayUnknownsDirective;
import au.org.ala.delta.intkey.directives.ExcludeCharactersDirective;
import au.org.ala.delta.intkey.directives.ExcludeTaxaDirective;
import au.org.ala.delta.intkey.directives.FileCharactersDirective;
import au.org.ala.delta.intkey.directives.FileDisplayDirective;
import au.org.ala.delta.intkey.directives.FileInputDirective;
import au.org.ala.delta.intkey.directives.FileJournalDirective;
import au.org.ala.delta.intkey.directives.FileLogDirective;
import au.org.ala.delta.intkey.directives.FileOutputDirective;
import au.org.ala.delta.intkey.directives.FindCharactersDirective;
import au.org.ala.delta.intkey.directives.FindTaxaDirective;
import au.org.ala.delta.intkey.directives.IllustrateCharactersDirective;
import au.org.ala.delta.intkey.directives.IllustrateTaxaDirective;
import au.org.ala.delta.intkey.directives.IncludeCharactersDirective;
import au.org.ala.delta.intkey.directives.IncludeTaxaDirective;
import au.org.ala.delta.intkey.directives.InformationDirective;
import au.org.ala.delta.intkey.directives.IntkeyDirectiveParseException;
import au.org.ala.delta.intkey.directives.NewDatasetDirective;
import au.org.ala.delta.intkey.directives.OutputCharactersDirective;
import au.org.ala.delta.intkey.directives.OutputCommentDirective;
import au.org.ala.delta.intkey.directives.OutputDescribeDirective;
import au.org.ala.delta.intkey.directives.OutputDiagnoseDirective;
import au.org.ala.delta.intkey.directives.OutputDifferencesDirective;
import au.org.ala.delta.intkey.directives.OutputSimilaritiesDirective;
import au.org.ala.delta.intkey.directives.OutputSummaryDirective;
import au.org.ala.delta.intkey.directives.OutputTaxaDirective;
import au.org.ala.delta.intkey.directives.PreferencesDirective;
import au.org.ala.delta.intkey.directives.RestartDirective;
import au.org.ala.delta.intkey.directives.SetAutoToleranceDirective;
import au.org.ala.delta.intkey.directives.SetDemonstrationDirective;
import au.org.ala.delta.intkey.directives.SetDiagLevelDirective;
import au.org.ala.delta.intkey.directives.SetDiagTypeSpecimensDirective;
import au.org.ala.delta.intkey.directives.SetDiagTypeTaxaDirective;
import au.org.ala.delta.intkey.directives.SetExactDirective;
import au.org.ala.delta.intkey.directives.SetFixDirective;
import au.org.ala.delta.intkey.directives.SetImagePathDirective;
import au.org.ala.delta.intkey.directives.SetInfoPathDirective;
import au.org.ala.delta.intkey.directives.SetMatchDirective;
import au.org.ala.delta.intkey.directives.SetRBaseDirective;
import au.org.ala.delta.intkey.directives.SetReliabilitiesDirective;
import au.org.ala.delta.intkey.directives.SetStopBestDirective;
import au.org.ala.delta.intkey.directives.SetToleranceDirective;
import au.org.ala.delta.intkey.directives.SetVaryWtDirective;
import au.org.ala.delta.intkey.directives.ShowDirective;
import au.org.ala.delta.intkey.directives.SimilaritiesDirective;
import au.org.ala.delta.intkey.directives.StatusAllDirective;
import au.org.ala.delta.intkey.directives.StatusDisplayDirective;
import au.org.ala.delta.intkey.directives.StatusExcludeCharactersDirective;
import au.org.ala.delta.intkey.directives.StatusExcludeTaxaDirective;
import au.org.ala.delta.intkey.directives.StatusFilesDirective;
import au.org.ala.delta.intkey.directives.StatusIncludeCharactersDirective;
import au.org.ala.delta.intkey.directives.StatusIncludeTaxaDirective;
import au.org.ala.delta.intkey.directives.StatusSetDirective;
import au.org.ala.delta.intkey.directives.SummaryDirective;
import au.org.ala.delta.intkey.directives.TaxaDirective;
import au.org.ala.delta.intkey.directives.UseDirective;
import au.org.ala.delta.intkey.model.DisplayImagesReportType;
import au.org.ala.delta.intkey.model.ImageDisplayMode;
import au.org.ala.delta.intkey.model.IntkeyCharacterOrder;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.intkey.model.SearchUtils;
import au.org.ala.delta.intkey.model.StartupFileData;
import au.org.ala.delta.intkey.model.StartupUtils;
import au.org.ala.delta.intkey.ui.AllowMismatchMessagePanel;
import au.org.ala.delta.intkey.ui.AttributeCellRenderer;
import au.org.ala.delta.intkey.ui.BestCharacterCellRenderer;
import au.org.ala.delta.intkey.ui.BusyGlassPane;
import au.org.ala.delta.intkey.ui.CharacterCellRenderer;
import au.org.ala.delta.intkey.ui.CharacterImageDialog;
import au.org.ala.delta.intkey.ui.CharacterKeywordSelectionDialog;
import au.org.ala.delta.intkey.ui.CharacterSelectionDialog;
import au.org.ala.delta.intkey.ui.ContentsDialog;
import au.org.ala.delta.intkey.ui.DefineButtonDialog;
import au.org.ala.delta.intkey.ui.DirectiveAction;
import au.org.ala.delta.intkey.ui.DirectivePopulator;
import au.org.ala.delta.intkey.ui.DirectivePopulatorInterceptor;
import au.org.ala.delta.intkey.ui.DisplayImagesDialog;
import au.org.ala.delta.intkey.ui.EditDatasetIndexDialog;
import au.org.ala.delta.intkey.ui.FindInCharactersDialog;
import au.org.ala.delta.intkey.ui.FindInTaxaDialog;
import au.org.ala.delta.intkey.ui.ImageDialog;
import au.org.ala.delta.intkey.ui.ImageUtils;
import au.org.ala.delta.intkey.ui.IntKeyDialogController;
import au.org.ala.delta.intkey.ui.IntegerInputDialog;
import au.org.ala.delta.intkey.ui.IntkeyUI;
import au.org.ala.delta.intkey.ui.IntkeyUIInterceptor;
import au.org.ala.delta.intkey.ui.MenuBuilder;
import au.org.ala.delta.intkey.ui.MessagePanel;
import au.org.ala.delta.intkey.ui.MultiStateInputDialog;
import au.org.ala.delta.intkey.ui.OnOffPromptDialog;
import au.org.ala.delta.intkey.ui.OpenDataSetDialog;
import au.org.ala.delta.intkey.ui.ReExecuteDialog;
import au.org.ala.delta.intkey.ui.RealInputDialog;
import au.org.ala.delta.intkey.ui.RtfReportDisplayDialog;
import au.org.ala.delta.intkey.ui.SetMainWindowSizeDialog;
import au.org.ala.delta.intkey.ui.SetMatchPromptDialog;
import au.org.ala.delta.intkey.ui.TaxonCellRenderer;
import au.org.ala.delta.intkey.ui.TaxonImageDialog;
import au.org.ala.delta.intkey.ui.TaxonInformationDialog;
import au.org.ala.delta.intkey.ui.TaxonKeywordSelectionDialog;
import au.org.ala.delta.intkey.ui.TaxonSelectionDialog;
import au.org.ala.delta.intkey.ui.TaxonWithDifferenceCountCellRenderer;
import au.org.ala.delta.intkey.ui.TextInputDialog;
import au.org.ala.delta.intkey.ui.ToolbarHelpDialog;
import au.org.ala.delta.intkey.ui.UIUtils;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MatchType;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.Specimen;
import au.org.ala.delta.model.TextAttribute;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.format.Formatter.AngleBracketHandlingMode;
import au.org.ala.delta.model.format.Formatter.CommentStrippingMode;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.rtf.RTFBuilder;
import au.org.ala.delta.rtf.RTFUtils;
import au.org.ala.delta.ui.AboutBox;
import au.org.ala.delta.ui.DeltaSingleFrameApplication;
import au.org.ala.delta.ui.help.HelpController;
import au.org.ala.delta.ui.rtf.SimpleRtfEditorKit;
import au.org.ala.delta.ui.util.IconHelper;
import au.org.ala.delta.util.Pair;

import com.l2fprod.common.swing.JFontChooser;

/**
 * Main UI Class
 * 
 * @author ChrisF
 * 
 */
public class Intkey extends DeltaSingleFrameApplication implements IntkeyUI, DirectivePopulator {

    // HELP IDs
    public static final String HELPSET_PATH = "help/Intkey";

    public static final String HELP_ID_TOPICS = "topics";
    public static final String HELP_ID_COMMANDS = "commands";

    public static final String HELP_ID_NO_MATCHING_TAXA_REMAIN = "no_taxa_match_the_specimen";
    public static final String HELP_ID_IDENTIFICATION_COMPLETE = "checking_an_identification";
    public static final String HELP_ID_NO_CHARACTERS_REMAINING = "not_enough_characters_for_identification";

    public static final String HELP_ID_CHARACTERS_TOOLBAR_RESTART = "characters_toolbar_restart";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_BEST = "characters_toolbar_best";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_SEPARATE = "characters_toolbar_separate";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_NATURAL = "characters_toolbar_natural";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_DIFF_SPECIMEN_REMAINING = "characters_toolbar_diff_specimen_remaining";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_TOLERANCE = "characters_toolbar_tolerance";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_SET_MATCH = "characters_toolbar_set_match";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_SUBSET_CHARACTERS = "characters_toolbar_subset_characters";
    public static final String HELP_ID_CHARACTERS_TOOLBAR_FIND_CHARACTERS = "characters_toolbar_find_characters";

    public static final String HELP_ID_TAXA_TOOLBAR_INFO = "taxa_toolbar_info";
    public static final String HELP_ID_TAXA_TOOLBAR_DIFF_TAXA = "taxa_toolbar_diff_taxa";
    public static final String HELP_ID_TAXA_TOOLBAR_SUBSET_TAXA = "taxa_toolbar_subset_taxa";
    public static final String HELP_ID_TAXA_TOOLBAR_FIND_TAXA = "taxa_toolbar_find_taxa";

    // Resource strings
    @Resource
    String windowTitleWithDatasetTitle;

    @Resource
    String availableCharactersCaption;

    @Resource
    String bestCharactersCaption;

    @Resource
    String separateCharactersCaption;

    @Resource
    String usedCharactersCaption;

    @Resource
    String remainingTaxaCaption;

    @Resource
    String eliminatedTaxaCaption;

    @Resource
    String calculatingBestCaption;

    @Resource
    String displayingReportCaption;

    @Resource
    String identificationCompleteCaption;

    @Resource
    String availableCharactersCannotSeparateCaption;

    @Resource
    String noMatchingTaxaRemainCaption;

    @Resource
    String charactersExcludedCannotSeparateCaption;

    @Resource
    String mismatchesAllowCannotSeparateCaption;

    @Resource
    String selectCharacterKeywordsCaption;

    @Resource
    String selectTaxonKeywordsCaption;

    @Resource
    String logDialogTitle;

    @Resource
    String errorDlgTitle;

    @Resource
    String informationDlgTitle;

    @Resource
    String badlyFormedRTFContentMessage;

    @Resource
    String separateInformationMessage;

    @Resource
    String noHelpAvailableCaption;

    @Resource
    String saveReportToFilePrompt;

    @Resource
    String errorWritingToFileError;

    @Resource
    String errorReadingRTFFileError;

    @Resource
    String rtfFileTooLargeError;

    // GUI components
    private JPanel _rootPanel;
    private JSplitPane _rootSplitPane;
    private JSplitPane _innerSplitPaneRight;
    private JSplitPane _innerSplitPaneLeft;
    private JTextField _txtFldCmdBar;

    private Map<String, JMenu> _cmdMenus;

    private IntkeyContext _context;
    private JList _listAvailableCharacters;
    private JList _listUsedCharacters;
    private JList _listRemainingTaxa;
    private JList _listEliminatedTaxa;

    private CharacterCellRenderer _availableCharactersListCellRenderer;
    private AttributeCellRenderer _usedCharactersListCellRenderer;
    private TaxonCellRenderer _availableTaxaCellRenderer;
    private TaxonWithDifferenceCountCellRenderer _eliminatedTaxaCellRenderer;

    private DefaultListModel _availableCharacterListModel;
    private DefaultListModel _usedCharacterListModel;
    private DefaultListModel _availableTaxaListModel;
    private DefaultListModel _eliminatedTaxaListModel;

    private JLabel _lblNumAvailableCharacters;
    private JLabel _lblNumUsedCharacters;

    private BusyGlassPane _busyGlassPane = null;
    private Component _defaultGlassPane;

    private List<Character> _foundAvailableCharacters = null;
    private List<Character> _foundUsedCharacters = null;
    private List<Item> _foundAvailableTaxa = null;
    private List<Item> _foundEliminatedTaxa = null;

    private List<JButton> _advancedModeOnlyDynamicButtons;
    private List<JButton> _normalModeOnlyDynamicButtons;
    private List<JButton> _activeOnlyWhenCharactersUsedButtons;
    private Map<JButton, String> _dynamicButtonsFullHelp;

    private JLabel _lblNumRemainingTaxa;
    private JLabel _lblEliminatedTaxa;
    private JButton _btnRestart;
    private JButton _btnBestOrder;
    private JButton _btnSeparate;
    private JButton _btnNaturalOrder;
    private JButton _btnDiffSpecimenTaxa;
    private JButton _btnSetTolerance;
    private JButton _btnSetMatch;
    private JButton _btnSubsetCharacters;
    private JButton _btnFindCharacter;
    private JButton _btnTaxonInfo;
    private JButton _btnDiffTaxa;
    private JButton _btnSubsetTaxa;
    private JButton _btnFindTaxon;
    private JButton _btnContextHelp;
    private JPanel _pnlAvailableCharacters;
    private JPanel _pnlAvailableCharactersButtons;
    private JPanel _pnlUsedCharacters;
    private JScrollPane _sclPnUsedCharacters;
    private JPanel _pnlUsedCharactersHeader;
    private JPanel _pnlRemainingTaxa;
    private JScrollPane _sclPnRemainingTaxa;
    private JPanel _pnlRemainingTaxaHeader;
    private JPanel _pnlRemainingTaxaButtons;
    private JPanel _pnlEliminatedTaxa;
    private JScrollPane _sclPnEliminatedTaxa;
    private JPanel _pnlEliminatedTaxaHeader;
    private JPanel _globalOptionBar;
    private JScrollPane _sclPaneAvailableCharacters;
    private JPanel _pnlAvailableCharactersHeader;
    private JPanel _pnlDynamicButtons;

    private RtfReportDisplayDialog _logDialog;

    private ItemFormatter _taxonformatter;

    private HelpController _helpController;

    /**
     * The resource path where icons are located
     */
    private static String INTKEY_ICON_PATH = "/au/org/ala/delta/intkey/resources/icons";

    /**
     * True if the user interface is in advanced mode
     */
    private boolean _advancedMode = false;

    /**
     * The initialization file to open on startup, as supplied on the command
     * line
     */
    private String _datasetInitFileToOpen = null;

    /**
     * The preferences file to execute on startup, as supplied on the command
     * line
     */
    private String _startupPreferencesFile = null;

    /**
     * If true, dataset startup images should not be displayed.
     */
    private boolean _suppressStartupImages = false;

    /**
     * The directory containing the startup file for the last dataset that was
     * opened.
     */
    private File _lastOpenedDatasetDirectory = null;

    /**
     * Calls Desktop.getDesktop on a background thread as it's slow to
     * initialise
     */
    private SwingWorker<Desktop, Void> _desktopWorker;

    /**
     * Main method.
     * 
     * @param args
     *            Command line arguments:<br/>
     *            1. filename Specifies the name of an initialization file (for
     *            example, C:\\ANGIO\\INTKEY.INI). The corresponding data set is
     *            then automatically loaded when the program starts, and the
     *            data-set selection box is not displayed.<br/>
     *            2. -A Sets Advanced mode.<br/>
     *            3. -I Suppresses display of startup images.<br/>
     *            4. -P=filename Specifies the name of a preferences file - see
     *            help for the "Preferences" command. The default directory is
     *            the one containing the program.
     */
    public static void main(String[] args) {
        setupMacSystemProperties(Intkey.class);
        launch(Intkey.class, args);
    }

    /**
     * ctor - Not called directly. This is called by the swing application
     * framework
     */
    public Intkey() {
        // Update resources bundle with desired look and feel before the swing
        // application framework can set the defaults.
        setLookAndFeel();
    }

    /**
     * Perform initialization before the GUI is contstructed. This method is
     * called by the swing application framework
     * 
     * @param args
     *            - Command line arguments from the main method
     */
    @Override
    protected void initialize(String[] args) {
        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectFields(this);

        // Define and parse command line arguments
        Options options = new Options();
        options.addOption("A", false, "Startup in advanced mode.");
        options.addOption("I", false, "Suppress display of startup images.");
        Option preferencesOption = OptionBuilder.withArgName("filename").hasArg().withDescription("Use the specified file as the preferences file.").create("P");
        options.addOption(preferencesOption);

        boolean cmdLineParseSuccess = true;
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine cmdLine = parser.parse(options, args, false);

            if (cmdLine.hasOption("A")) {
                _advancedMode = true;
            }

            if (cmdLine.hasOption("I")) {
                _suppressStartupImages = true;
            }

            if (cmdLine.hasOption("P")) {
                _startupPreferencesFile = cmdLine.getOptionValue("P");
                if (StringUtils.isEmpty(_startupPreferencesFile)) {
                    cmdLineParseSuccess = false;
                }
            }

            if (cmdLine.getArgList().size() == 1) {
                _datasetInitFileToOpen = (String) cmdLine.getArgList().get(0);
            }

            if (cmdLine.getArgList().size() > 1) {
                cmdLineParseSuccess = false;
            }

        } catch (ParseException ex) {
            cmdLineParseSuccess = false;
        }

        if (!cmdLineParseSuccess) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Intkey [dataset-init-file] [options]", options);
            System.exit(0);
        }

        // If _startupInAdvancedMode has not already been set to true using the
        // "-A" command line option (see above),
        // Check saved application state for the mode (advanced or basic) that
        // was last used in the application.
        if (!_advancedMode) {
            _advancedMode = UIUtils.getPreviousApplicationMode();
        }

        // Get location of last opened dataset from saved application state
        _lastOpenedDatasetDirectory = UIUtils.getSavedLastOpenedDatasetDirectory();
    }

    /**
     * Creates and shows the GUI. Called by the swing application framework
     */
    @Override
    protected void startup() {
        final JFrame mainFrame = getMainFrame();
        _defaultGlassPane = mainFrame.getGlassPane();
        mainFrame.setTitle("Intkey");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setIconImages(IconHelper.getRedIconList());

        _helpController = new HelpController(HELPSET_PATH);

        _taxonformatter = new ItemFormatter(false, CommentStrippingMode.STRIP_ALL, AngleBracketHandlingMode.REMOVE, true, false, true);
        _context = new IntkeyContext(new IntkeyUIInterceptor(this), new DirectivePopulatorInterceptor(this));

        _advancedModeOnlyDynamicButtons = new ArrayList<JButton>();
        _normalModeOnlyDynamicButtons = new ArrayList<JButton>();
        _activeOnlyWhenCharactersUsedButtons = new ArrayList<JButton>();
        _dynamicButtonsFullHelp = new HashMap<JButton, String>();

        ActionMap actionMap = getContext().getActionMap();

        _rootPanel = new JPanel();
        _rootPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        _rootPanel.setBackground(SystemColor.control);
        _rootPanel.setLayout(new BorderLayout(0, 0));

        _globalOptionBar = new JPanel();
        _globalOptionBar.setBorder(new EmptyBorder(0, 5, 0, 5));
        _rootPanel.add(_globalOptionBar, BorderLayout.NORTH);
        _globalOptionBar.setLayout(new BorderLayout(0, 0));

        _pnlDynamicButtons = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) _pnlDynamicButtons.getLayout();
        flowLayout_1.setVgap(0);
        flowLayout_1.setHgap(0);
        _globalOptionBar.add(_pnlDynamicButtons, BorderLayout.WEST);

        _btnContextHelp = new JButton();
        _btnContextHelp.setMinimumSize(new Dimension(30, 30));
        _btnContextHelp.setMaximumSize(new Dimension(30, 30));
        _btnContextHelp.setAction(actionMap.get("btnContextHelp"));
        _btnContextHelp.setPreferredSize(new Dimension(30, 30));
        _btnContextHelp.setMargin(new Insets(2, 5, 2, 5));
        _btnContextHelp.addActionListener(actionMap.get("btnContextHelp"));
        _globalOptionBar.add(_btnContextHelp, BorderLayout.EAST);

        _rootSplitPane = new JSplitPane();
        _rootSplitPane.setDividerSize(3);
        _rootSplitPane.setResizeWeight(0.5);
        _rootSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        _rootSplitPane.setContinuousLayout(true);
        _rootPanel.add(_rootSplitPane);

        _innerSplitPaneLeft = new JSplitPane();
        _innerSplitPaneLeft.setMinimumSize(new Dimension(25, 25));
        _innerSplitPaneLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
        _innerSplitPaneLeft.setDividerSize(3);
        _innerSplitPaneLeft.setResizeWeight(0.5);

        _innerSplitPaneLeft.setContinuousLayout(true);
        _innerSplitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
        _rootSplitPane.setLeftComponent(_innerSplitPaneLeft);

        _pnlAvailableCharacters = new JPanel();
        _innerSplitPaneLeft.setLeftComponent(_pnlAvailableCharacters);
        _pnlAvailableCharacters.setLayout(new BorderLayout(0, 0));

        _sclPaneAvailableCharacters = new JScrollPane();
        _pnlAvailableCharacters.add(_sclPaneAvailableCharacters, BorderLayout.CENTER);

        _listAvailableCharacters = new JList();
        // _listAvailableCharacters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _listAvailableCharacters.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        _listAvailableCharacters.setCellRenderer(_availableCharactersListCellRenderer);
        _listAvailableCharacters.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int selectedIndex = _listAvailableCharacters.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        try {
                            Character ch = (Character) _availableCharacterListModel.getElementAt(selectedIndex);
                            executeDirective(new UseDirective(), Integer.toString(ch.getCharacterId()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        _sclPaneAvailableCharacters.setViewportView(_listAvailableCharacters);

        _pnlAvailableCharactersHeader = new JPanel();
        _pnlAvailableCharacters.add(_pnlAvailableCharactersHeader, BorderLayout.NORTH);
        _pnlAvailableCharactersHeader.setLayout(new BorderLayout(0, 0));

        _lblNumAvailableCharacters = new JLabel();
        _lblNumAvailableCharacters.setBorder(new EmptyBorder(0, 5, 0, 0));
        _lblNumAvailableCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblNumAvailableCharacters.setText(MessageFormat.format(availableCharactersCaption, 0));
        _pnlAvailableCharactersHeader.add(_lblNumAvailableCharacters, BorderLayout.WEST);

        _pnlAvailableCharactersButtons = new JPanel();
        FlowLayout flowLayout = (FlowLayout) _pnlAvailableCharactersButtons.getLayout();
        flowLayout.setVgap(2);
        flowLayout.setHgap(2);
        _pnlAvailableCharactersHeader.add(_pnlAvailableCharactersButtons, BorderLayout.EAST);

        // All toolbar buttons should be disabled until a dataset is loaded.
        _btnRestart = new JButton();
        _btnRestart.setAction(actionMap.get("btnRestart"));
        _btnRestart.setPreferredSize(new Dimension(30, 30));
        _btnRestart.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnRestart);

        _btnBestOrder = new JButton();
        _btnBestOrder.setAction(actionMap.get("btnBestOrder"));
        _btnBestOrder.setPreferredSize(new Dimension(30, 30));
        _btnBestOrder.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnBestOrder);

        _btnSeparate = new JButton();
        _btnSeparate.setAction(actionMap.get("btnSeparate"));
        _btnSeparate.setVisible(_advancedMode);
        _btnSeparate.setPreferredSize(new Dimension(30, 30));
        _btnSeparate.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnSeparate);

        _btnNaturalOrder = new JButton();
        _btnNaturalOrder.setAction(actionMap.get("btnNaturalOrder"));
        _btnNaturalOrder.setPreferredSize(new Dimension(30, 30));
        _btnNaturalOrder.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnNaturalOrder);

        _btnDiffSpecimenTaxa = new JButton();
        _btnDiffSpecimenTaxa.setAction(actionMap.get("btnDiffSpecimenTaxa"));
        _btnDiffSpecimenTaxa.setEnabled(false);
        _btnDiffSpecimenTaxa.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnDiffSpecimenTaxa);

        _btnSetTolerance = new JButton();
        _btnSetTolerance.setAction(actionMap.get("btnSetTolerance"));
        _btnSetTolerance.setPreferredSize(new Dimension(30, 30));
        _btnSetTolerance.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnSetTolerance);

        _btnSetMatch = new JButton();
        _btnSetMatch.setAction(actionMap.get("btnSetMatch"));
        _btnSetMatch.setVisible(_advancedMode);
        _btnSetMatch.setPreferredSize(new Dimension(30, 30));
        _btnSetMatch.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnSetMatch);

        _btnSubsetCharacters = new JButton();
        _btnSubsetCharacters.setAction(actionMap.get("btnSubsetCharacters"));
        _btnSubsetCharacters.setPreferredSize(new Dimension(30, 30));
        _btnSubsetCharacters.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnSubsetCharacters);

        _btnFindCharacter = new JButton();
        _btnFindCharacter.setAction(actionMap.get("btnFindCharacter"));
        _btnFindCharacter.setPreferredSize(new Dimension(30, 30));
        _btnFindCharacter.setEnabled(false);
        _pnlAvailableCharactersButtons.add(_btnFindCharacter);

        _pnlAvailableCharactersButtons.setEnabled(false);

        _pnlUsedCharacters = new JPanel();
        _innerSplitPaneLeft.setRightComponent(_pnlUsedCharacters);
        _pnlUsedCharacters.setLayout(new BorderLayout(0, 0));

        _sclPnUsedCharacters = new JScrollPane();
        _pnlUsedCharacters.add(_sclPnUsedCharacters, BorderLayout.CENTER);

        _listUsedCharacters = new JList();
        _listUsedCharacters.setCellRenderer(_usedCharactersListCellRenderer);
        _listUsedCharacters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _listUsedCharacters.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int selectedIndex = _listUsedCharacters.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        try {
                            Attribute attr = (Attribute) _usedCharacterListModel.getElementAt(selectedIndex);

                            if (_context.charactersFixed() && _context.getFixedCharactersList().contains(attr.getCharacter().getCharacterId())) {
                                return;
                            }

                            executeDirective(new ChangeDirective(), Integer.toString(attr.getCharacter().getCharacterId()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        _sclPnUsedCharacters.setViewportView(_listUsedCharacters);

        _pnlUsedCharactersHeader = new JPanel();
        _pnlUsedCharacters.add(_pnlUsedCharactersHeader, BorderLayout.NORTH);
        _pnlUsedCharactersHeader.setLayout(new BorderLayout(0, 0));

        _lblNumUsedCharacters = new JLabel();
        _lblNumUsedCharacters.setBorder(new EmptyBorder(7, 5, 7, 0));
        _lblNumUsedCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblNumUsedCharacters.setText(MessageFormat.format(usedCharactersCaption, 0));
        _pnlUsedCharactersHeader.add(_lblNumUsedCharacters, BorderLayout.WEST);

        _innerSplitPaneRight = new JSplitPane();
        _innerSplitPaneRight.setMinimumSize(new Dimension(25, 25));
        _innerSplitPaneRight.setDividerSize(3);
        _innerSplitPaneRight.setResizeWeight(0.5);
        _innerSplitPaneRight.setContinuousLayout(true);
        _innerSplitPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
        _rootSplitPane.setRightComponent(_innerSplitPaneRight);

        _pnlRemainingTaxa = new JPanel();
        _innerSplitPaneRight.setLeftComponent(_pnlRemainingTaxa);
        _pnlRemainingTaxa.setLayout(new BorderLayout(0, 0));

        _sclPnRemainingTaxa = new JScrollPane();
        _pnlRemainingTaxa.add(_sclPnRemainingTaxa, BorderLayout.CENTER);

        _listRemainingTaxa = new JList();

        _listRemainingTaxa.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    displayInfoForSelectedTaxa();
                }
            }
        });

        _sclPnRemainingTaxa.setViewportView(_listRemainingTaxa);

        _pnlRemainingTaxaHeader = new JPanel();
        _pnlRemainingTaxa.add(_pnlRemainingTaxaHeader, BorderLayout.NORTH);
        _pnlRemainingTaxaHeader.setLayout(new BorderLayout(0, 0));

        _lblNumRemainingTaxa = new JLabel();
        _lblNumRemainingTaxa.setBorder(new EmptyBorder(0, 5, 0, 0));
        _lblNumRemainingTaxa.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblNumRemainingTaxa.setText(MessageFormat.format(remainingTaxaCaption, 0));
        _pnlRemainingTaxaHeader.add(_lblNumRemainingTaxa, BorderLayout.WEST);

        _pnlRemainingTaxaButtons = new JPanel();
        FlowLayout fl_pnlRemainingTaxaButtons = (FlowLayout) _pnlRemainingTaxaButtons.getLayout();
        fl_pnlRemainingTaxaButtons.setVgap(2);
        fl_pnlRemainingTaxaButtons.setHgap(2);
        _pnlRemainingTaxaHeader.add(_pnlRemainingTaxaButtons, BorderLayout.EAST);

        // All toolbar buttons should be disabled until a dataset is loaded.
        _btnTaxonInfo = new JButton();
        _btnTaxonInfo.setAction(actionMap.get("btnTaxonInfo"));
        _btnTaxonInfo.setPreferredSize(new Dimension(30, 30));
        _btnTaxonInfo.setEnabled(false);
        _pnlRemainingTaxaButtons.add(_btnTaxonInfo);

        _btnDiffTaxa = new JButton();
        _btnDiffTaxa.setAction(actionMap.get("btnDiffTaxa"));
        _btnDiffTaxa.setPreferredSize(new Dimension(30, 30));
        _btnDiffTaxa.setEnabled(false);
        _pnlRemainingTaxaButtons.add(_btnDiffTaxa);

        _btnSubsetTaxa = new JButton();
        _btnSubsetTaxa.setAction(actionMap.get("btnSubsetTaxa"));
        _btnSubsetTaxa.setPreferredSize(new Dimension(30, 30));
        _btnSubsetTaxa.setEnabled(false);
        _pnlRemainingTaxaButtons.add(_btnSubsetTaxa);

        _btnFindTaxon = new JButton();
        _btnFindTaxon.setAction(actionMap.get("btnFindTaxon"));
        _btnFindTaxon.setPreferredSize(new Dimension(30, 30));
        _btnFindTaxon.setEnabled(false);
        _pnlRemainingTaxaButtons.add(_btnFindTaxon);

        _pnlEliminatedTaxa = new JPanel();
        _innerSplitPaneRight.setRightComponent(_pnlEliminatedTaxa);
        _pnlEliminatedTaxa.setLayout(new BorderLayout(0, 0));

        _sclPnEliminatedTaxa = new JScrollPane();
        _pnlEliminatedTaxa.add(_sclPnEliminatedTaxa, BorderLayout.CENTER);

        _listEliminatedTaxa = new JList();

        _listEliminatedTaxa.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    displayInfoForSelectedTaxa();
                }
            }
        });

        _sclPnEliminatedTaxa.setViewportView(_listEliminatedTaxa);

        _pnlEliminatedTaxaHeader = new JPanel();
        _pnlEliminatedTaxa.add(_pnlEliminatedTaxaHeader, BorderLayout.NORTH);
        _pnlEliminatedTaxaHeader.setLayout(new BorderLayout(0, 0));

        _lblEliminatedTaxa = new JLabel();
        _lblEliminatedTaxa.setBorder(new EmptyBorder(7, 5, 7, 0));
        _lblEliminatedTaxa.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblEliminatedTaxa.setText(MessageFormat.format(eliminatedTaxaCaption, 0));
        _pnlEliminatedTaxaHeader.add(_lblEliminatedTaxa, BorderLayout.WEST);

        JMenuBar menuBar = buildMenus(_advancedMode);
        getMainView().setMenuBar(menuBar);

        _txtFldCmdBar = new JTextField();
        _txtFldCmdBar.setCaretColor(Color.WHITE);
        _txtFldCmdBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String cmdStr = _txtFldCmdBar.getText();

                cmdStr = cmdStr.trim();
                if (_cmdMenus.containsKey(cmdStr)) {
                    JMenu cmdMenu = _cmdMenus.get(cmdStr);
                    cmdMenu.doClick();
                } else {
                    _context.parseAndExecuteDirective(cmdStr);
                }
                _txtFldCmdBar.setText(null);
            }
        });

        _txtFldCmdBar.setFont(new Font("Courier New", Font.BOLD, 13));
        _txtFldCmdBar.setForeground(SystemColor.text);
        _txtFldCmdBar.setBackground(Color.BLACK);
        _txtFldCmdBar.setOpaque(true);
        _txtFldCmdBar.setVisible(_advancedMode);
        _rootPanel.add(_txtFldCmdBar, BorderLayout.SOUTH);
        _txtFldCmdBar.setColumns(10);

        _logDialog = new RtfReportDisplayDialog(getMainFrame(), new SimpleRtfEditorKit(null), null, logDialogTitle);

        // Set context-sensitive help keys for toolbar buttons
        _helpController.setHelpKeyForComponent(_btnRestart, HELP_ID_CHARACTERS_TOOLBAR_RESTART);
        _helpController.setHelpKeyForComponent(_btnBestOrder, HELP_ID_CHARACTERS_TOOLBAR_BEST);
        _helpController.setHelpKeyForComponent(_btnSeparate, HELP_ID_CHARACTERS_TOOLBAR_SEPARATE);
        _helpController.setHelpKeyForComponent(_btnNaturalOrder, HELP_ID_CHARACTERS_TOOLBAR_NATURAL);
        _helpController.setHelpKeyForComponent(_btnDiffSpecimenTaxa, HELP_ID_CHARACTERS_TOOLBAR_DIFF_SPECIMEN_REMAINING);
        _helpController.setHelpKeyForComponent(_btnSetTolerance, HELP_ID_CHARACTERS_TOOLBAR_TOLERANCE);
        _helpController.setHelpKeyForComponent(_btnSetMatch, HELP_ID_CHARACTERS_TOOLBAR_SET_MATCH);
        _helpController.setHelpKeyForComponent(_btnSubsetCharacters, HELP_ID_CHARACTERS_TOOLBAR_SUBSET_CHARACTERS);
        _helpController.setHelpKeyForComponent(_btnFindCharacter, HELP_ID_CHARACTERS_TOOLBAR_FIND_CHARACTERS);

        _helpController.setHelpKeyForComponent(_btnTaxonInfo, HELP_ID_TAXA_TOOLBAR_INFO);
        _helpController.setHelpKeyForComponent(_btnDiffTaxa, HELP_ID_TAXA_TOOLBAR_DIFF_TAXA);
        _helpController.setHelpKeyForComponent(_btnSubsetTaxa, HELP_ID_TAXA_TOOLBAR_SUBSET_TAXA);
        _helpController.setHelpKeyForComponent(_btnFindTaxon, HELP_ID_TAXA_TOOLBAR_FIND_TAXA);

        // This mouse listener on the default glasspane is to assist with
        // context senstive help. It intercepts the mouse events,
        // determines what component was being clicked on, then takes the
        // appropriate action to provide help for the component
        _defaultGlassPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // Determine what point has been clicked on
                Point glassPanePoint = e.getPoint();
                Point containerPoint = SwingUtilities.convertPoint(getMainFrame().getGlassPane(), glassPanePoint, getMainFrame().getContentPane());
                Component component = SwingUtilities.getDeepestComponentAt(getMainFrame().getContentPane(), containerPoint.x, containerPoint.y);

                // Get the java help ID for this component. If none has been
                // defined, this will be null
                String helpID = _helpController.getHelpKeyForComponent(component);

                // change the cursor back to the normal one and take down the
                // classpane
                mainFrame.setCursor(Cursor.getDefaultCursor());
                mainFrame.getGlassPane().setVisible(false);

                // If a help ID was found, display the related help page in the
                // help viewer
                if (_helpController.getHelpKeyForComponent(component) != null) {
                    _helpController.helpAction().actionPerformed(new ActionEvent(component, 0, null));
                    _helpController.displayHelpTopic(mainFrame, helpID);
                } else {
                    // If a dynamically-defined toolbar button was clicked, show
                    // the help for this button in the ToolbarHelpDialog.
                    if (component instanceof JButton) {
                        JButton button = (JButton) component;
                        if (_dynamicButtonsFullHelp.containsKey(button)) {
                            String fullHelpText = _dynamicButtonsFullHelp.get(button);
                            if (fullHelpText == null) {
                                fullHelpText = noHelpAvailableCaption;
                            }
                            RTFBuilder builder = new RTFBuilder();
                            builder.startDocument();
                            builder.appendText(fullHelpText);
                            builder.endDocument();
                            ToolbarHelpDialog dlg = new ToolbarHelpDialog(mainFrame, builder.toString(), button.getIcon());
                            show(dlg);
                        }
                    }
                }
            }
        });

        show(_rootPanel);
    }

    /**
     * Performs additional tasks after the GUI has been constructed and shown.
     * Called by the swing application framework
     */
    @Override
    protected void ready() {
        super.ready();
        _rootSplitPane.setDividerLocation(2.0 / 3.0);
        _innerSplitPaneLeft.setDividerLocation(2.0 / 3.0);
        _innerSplitPaneRight.setDividerLocation(2.0 / 3.0);

        loadDesktopInBackground();

        if (_advancedMode) {
            _context.setImageDisplayMode(ImageDisplayMode.MANUAL);
            _context.setCharacterOrderNatural();
            _context.setDisplayEndIdentify(false);
        }

        // If a dataset was supplied on the command line, load it
        if (_datasetInitFileToOpen != null) {
            // Need to surround file path in quotes, otherwise it may be broken
            // up into more than 1 token.
            executeDirective(new NewDatasetDirective(), "\"" + _datasetInitFileToOpen + "\"");
        }

        // If a preferences file was supplied on the command line, process it
        if (_startupPreferencesFile != null) {
            // Need to surround file path in quotes, otherwise it may be broken
            // up into more than 1 token.
            executeDirective(new PreferencesDirective(), "\"" + _startupPreferencesFile + "\"");
        }

        // Show the dataset index on startup if no dataset was supplied on the
        // command line
        if (_datasetInitFileToOpen == null) {
            executeDirective(new NewDatasetDirective(), null);
        }
    }

    /**
     * Performs cleanup and related tasks before the application is shutdown
     */
    @Override
    protected void shutdown() {
        UIUtils.savePreviousApplicationMode(_advancedMode);
        UIUtils.saveLastOpenedDatasetDirectory(_lastOpenedDatasetDirectory);
        _context.cleanupForShutdown();
        super.shutdown();
    }

    /**
     * Build the main GUI window's menus
     * 
     * @param advancedMode
     *            true if the application is in advanced mode
     * @return a JMenuBar containing the menus
     */
    private JMenuBar buildMenus(boolean advancedMode) {

        _cmdMenus = new HashMap<String, JMenu>();

        ActionMap actionMap = getContext().getActionMap();

        JMenuBar menuBar = new JMenuBar();

        menuBar.add(buildFileMenu(true, actionMap));

        if (advancedMode) {
            menuBar.add(buildQueriesMenu(actionMap));
            menuBar.add(buildBrowsingMenu(actionMap));
            menuBar.add(buildSettingsMenu(actionMap));
            menuBar.add(buildReExecuteMenu(actionMap));
        }
        menuBar.add(buildWindowMenu(actionMap));
        menuBar.add(buildHelpMenu(advancedMode, actionMap));

        return menuBar;
    }

    /**
     * Build the file menu
     * 
     * @param advancedMode
     *            true if the application is in advanced mode
     * @param actionMap
     *            The action map for the main GUI window
     * @return a JMenu for the file menu
     */
    private JMenu buildFileMenu(boolean advancedMode, ActionMap actionMap) {
        MenuBuilder mnuFileBuilder = new MenuBuilder("mnuFile", _context);

        // Some menus/menu items should be disabled if no dataset is loaded.
        boolean isDatasetLoaded = _context.getDataset() != null;

        mnuFileBuilder.addDirectiveMenuItem("mnuItNewDataSet", new NewDatasetDirective(), true);
        mnuFileBuilder.addPreconfiguredJMenu(buildRecentFilesMenu());

        if (_advancedMode) {
            mnuFileBuilder.addDirectiveMenuItem("mnuItPreferences", new PreferencesDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItContents", new ContentsDirective(), isDatasetLoaded);

            mnuFileBuilder.addSeparator();

            mnuFileBuilder.startSubMenu("mnuFileCmds", true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileInput", new FileInputDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileOutput", new FileOutputDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileDisplay", new FileDisplayDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileLog", new FileLogDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileJournal", new FileJournalDirective(), true);
            // mnuFileBuilder.addDirectiveMenuItem("mnuItFileClose", new
            // FileCloseDirective()); ** File Close is now a NO-OP
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileCharacters", new FileCharactersDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItFileTaxa", new FileCharactersDirective(), true);
            mnuFileBuilder.endSubMenu();

            mnuFileBuilder.startSubMenu("mnuOutputCmds", true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputCharacters", new OutputCharactersDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputTaxa", new OutputTaxaDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputDescribe", new OutputDescribeDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputSummary", new OutputSummaryDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputDiagnose", new OutputDiagnoseDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputDifferences", new OutputDifferencesDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputSimilarities", new OutputSimilaritiesDirective(), isDatasetLoaded);
            mnuFileBuilder.addDirectiveMenuItem("mnuItOutputComment", new OutputCommentDirective(), isDatasetLoaded);
            mnuFileBuilder.endSubMenu();
            mnuFileBuilder.addSeparator();

            mnuFileBuilder.addDirectiveMenuItem("mnuItComment", new CommentDirective(), true);
            mnuFileBuilder.addDirectiveMenuItem("mnuItShow", new ShowDirective(), true);

            mnuFileBuilder.addSeparator();

            mnuFileBuilder.addActionMenuItem(actionMap.get("mnuItNormalMode"), true);
        } else {
            mnuFileBuilder.addSeparator();
            mnuFileBuilder.addActionMenuItem(actionMap.get("mnuItAdvancedMode"), true);
        }

        if (_advancedMode) {
            mnuFileBuilder.addSeparator();
            mnuFileBuilder.addActionMenuItem(actionMap.get("mnuItEditDataSetIndex"), true);
        }

        mnuFileBuilder.addSeparator();

        mnuFileBuilder.addActionMenuItem(actionMap.get("mnuItExitApplication"), true);

        return mnuFileBuilder.getMenu();
    }

    /**
     * Build a menu of recently opened datasets
     * 
     * @return A JMenu of recently opened datasets
     */
    private JMenu buildRecentFilesMenu() {
        Map<String, String> datasetIndexMap = UIUtils.getDatasetIndexAsMap();

        JMenu mnuFileRecents = new JMenu();
        mnuFileRecents.setName("mnuFileRecents");

        List<Pair<String, String>> recentFiles = UIUtils.getPreviouslyUsedFiles();

        for (int i = 0; i < recentFiles.size(); i++) {
            Pair<String, String> recentFile = recentFiles.get(i);
            final String filePath = recentFile.getFirst();

            int fileNumber = i + 1;

            String title;

            // If the dataset at the path as listed in the most recently used
            // datasets is listed in the index,
            // use the description listed in the dataset index.
            if (datasetIndexMap.containsKey(filePath)) {
                title = fileNumber + ". " + datasetIndexMap.get(filePath);
            } else {
                title = fileNumber + ". " + recentFile.getSecond();
            }

            JMenuItem mnuItRecentFile = new JMenuItem(title);
            mnuItRecentFile.setToolTipText(filePath);

            mnuItRecentFile.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    openPreviouslyOpenedFile(filePath);
                }
            });

            mnuFileRecents.add(mnuItRecentFile);
        }

        return mnuFileRecents;
    }

    /**
     * Build the queries menu
     * 
     * @param actionMap
     *            The action map for the main GUI window
     * @return The JMenu for the queries menu
     */
    private JMenu buildQueriesMenu(ActionMap actionMap) {
        // Some menus/menu items should be disabled if no dataset is loaded.
        boolean isDatasetLoaded = _context.getDataset() != null;

        JMenu mnuQueries = new JMenu();
        mnuQueries.setName("mnuQueries");

        JMenuItem mnuItRestart = new JMenuItem(new DirectiveAction(new RestartDirective(), _context));
        mnuItRestart.setName("mnuItRestart");
        mnuItRestart.setEnabled(isDatasetLoaded);
        mnuQueries.add(mnuItRestart);

        mnuQueries.addSeparator();

        JMenuItem mnuItDescribe = new JMenuItem(new DirectiveAction(new DescribeDirective(), _context));
        mnuItDescribe.setName("mnuItDescribe");
        mnuItDescribe.setEnabled(isDatasetLoaded);
        mnuQueries.add(mnuItDescribe);

        JMenuItem mnuItDiagnose = new JMenuItem(new DirectiveAction(new DiagnoseDirective(), _context));
        mnuItDiagnose.setName("mnuItDiagnose");
        mnuItDiagnose.setEnabled(isDatasetLoaded);
        mnuQueries.add(mnuItDiagnose);

        mnuQueries.addSeparator();

        JMenuItem mnuItDifferences = new JMenuItem(new DirectiveAction(new DifferencesDirective(), _context));
        mnuItDifferences.setName("mnuItDifferences");
        mnuItDifferences.setEnabled(isDatasetLoaded);
        mnuQueries.add(mnuItDifferences);
        JMenuItem mnuItSimilarities = new JMenuItem(new DirectiveAction(new SimilaritiesDirective(), _context));
        mnuItSimilarities.setName("mnuItSimilarities");
        mnuItSimilarities.setEnabled(isDatasetLoaded);
        mnuQueries.add(mnuItSimilarities);

        mnuQueries.addSeparator();

        JMenuItem mnuItSummary = new JMenuItem(new DirectiveAction(new SummaryDirective(), _context));
        mnuItSummary.setName("mnuItSummary");
        mnuItSummary.setEnabled(isDatasetLoaded);
        mnuQueries.add(mnuItSummary);

        return mnuQueries;
    }

    /**
     * Build the browsing menu
     * 
     * @param actionMap
     *            The action map for the main GUI window
     * @return The JMenu for the browsing menu
     */
    private JMenu buildBrowsingMenu(ActionMap actionMap) {
        // Some menus/menu items should be disabled if no dataset is loaded.
        boolean isDatasetLoaded = _context.getDataset() != null;

        JMenu mnuBrowsing = new JMenu();
        mnuBrowsing.setName("mnuBrowsing");

        JMenuItem mnuItCharacters = new JMenuItem(new DirectiveAction(new CharactersDirective(), _context));
        mnuItCharacters.setName("mnuItCharacters");
        mnuItCharacters.setEnabled(isDatasetLoaded);
        mnuBrowsing.add(mnuItCharacters);
        JMenuItem mnuItTaxa = new JMenuItem(new DirectiveAction(new TaxaDirective(), _context));
        mnuItTaxa.setName("mnuItTaxa");
        mnuItTaxa.setEnabled(isDatasetLoaded);
        mnuBrowsing.add(mnuItTaxa);

        mnuBrowsing.addSeparator();

        JMenu mnuFind = new JMenu();
        mnuFind.setName("mnuFind");
        JMenuItem mnuItFindCharacters = new JMenuItem(new DirectiveAction(new FindCharactersDirective(), _context));
        mnuItFindCharacters.setName("mnuItFindCharacters");
        mnuItFindCharacters.setEnabled(isDatasetLoaded);
        mnuFind.add(mnuItFindCharacters);
        JMenuItem mnuItFindTaxa = new JMenuItem(new DirectiveAction(new FindTaxaDirective(), _context));
        mnuItFindTaxa.setName("mnuItFindTaxa");
        mnuItFindTaxa.setEnabled(isDatasetLoaded);
        mnuFind.setEnabled(isDatasetLoaded);
        mnuFind.add(mnuItFindTaxa);

        mnuBrowsing.add(mnuFind);

        mnuBrowsing.addSeparator();

        JMenu mnuIllustrate = new JMenu();
        mnuIllustrate.setName("mnuIllustrate");
        JMenuItem mnuItIllustrateCharacters = new JMenuItem(new DirectiveAction(new IllustrateCharactersDirective(), _context));
        mnuItIllustrateCharacters.setName("mnuItIllustrateCharacters");
        mnuItIllustrateCharacters.setEnabled(isDatasetLoaded);
        mnuIllustrate.add(mnuItIllustrateCharacters);
        JMenuItem mnuItIllustrateTaxa = new JMenuItem(new DirectiveAction(new IllustrateTaxaDirective(), _context));
        mnuItIllustrateTaxa.setName("mnuItIllustrateTaxa");
        mnuItIllustrateTaxa.setEnabled(isDatasetLoaded);
        mnuIllustrate.setEnabled(isDatasetLoaded);
        mnuIllustrate.add(mnuItIllustrateTaxa);

        mnuBrowsing.add(mnuIllustrate);

        mnuBrowsing.addSeparator();

        JMenuItem mnuItInformation = new JMenuItem(new DirectiveAction(new InformationDirective(), _context));
        mnuItInformation.setName("mnuItInformation");
        mnuItInformation.setEnabled(isDatasetLoaded);
        mnuBrowsing.add(mnuItInformation);

        return mnuBrowsing;
    }

    /**
     * Build the settings menu
     * 
     * @param actionMap
     *            The action map for the main GUI window
     * @return The JMenu for the settings menu
     */
    private JMenu buildSettingsMenu(ActionMap actionMap) {
        // Some menus/menu items should be disabled if no dataset is loaded.
        boolean isDatasetLoaded = _context.getDataset() != null;

        JMenu mnuSettings = new JMenu();
        mnuSettings.setName("mnuSettings");

        // "Set" submenu
        MenuBuilder mnuSetBuilder = new MenuBuilder("mnuSet", _context);

        mnuSetBuilder.startSubMenu("mnuAutotolerance", true);
        mnuSetBuilder.addOnOffDirectiveMenuItem("mnuItAutotoleranceOn", new SetAutoToleranceDirective(), true);
        mnuSetBuilder.addOnOffDirectiveMenuItem("mnuItAutotoleranceOff", new SetAutoToleranceDirective(), false);
        mnuSetBuilder.endSubMenu();

        mnuSetBuilder.startSubMenu("mnuDemonstration", true);
        mnuSetBuilder.addOnOffDirectiveMenuItem("mnuItDemonstrationOn", new SetDemonstrationDirective(), true);
        mnuSetBuilder.addOnOffDirectiveMenuItem("mnuItDemonstrationOff", new SetDemonstrationDirective(), false);
        mnuSetBuilder.endSubMenu();

        mnuSetBuilder.addDirectiveMenuItem("mnuItDiagLevel", new SetDiagLevelDirective(), true);

        mnuSetBuilder.startSubMenu("mnuDiagType", true);
        mnuSetBuilder.addDirectiveMenuItem("mnuItDiagTypeSpecimens", new SetDiagTypeSpecimensDirective(), true);
        mnuSetBuilder.addDirectiveMenuItem("mnuItDiagTypeTaxa", new SetDiagTypeTaxaDirective(), true);
        mnuSetBuilder.endSubMenu();

        mnuSetBuilder.addDirectiveMenuItem("mnuItExact", new SetExactDirective(), true);

        mnuSetBuilder.startSubMenu("mnuFix", true);
        mnuSetBuilder.addOnOffDirectiveMenuItem("mnuItFixOn", new SetFixDirective(), true);
        mnuSetBuilder.addOnOffDirectiveMenuItem("mnuItFixOff", new SetFixDirective(), false);
        mnuSetBuilder.endSubMenu();

        mnuSetBuilder.addDirectiveMenuItem("mnuItImagePath", new SetImagePathDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItInfoPath", new SetInfoPathDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItMatch", new SetMatchDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItRbase", new SetRBaseDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItReliabilities", new SetReliabilitiesDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItStopBest", new SetStopBestDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItTolerance", new SetToleranceDirective(), true);

        mnuSetBuilder.addDirectiveMenuItem("mnuItVaryWt", new SetVaryWtDirective(), true);

        mnuSettings.add(mnuSetBuilder.getMenu());

        // "Display" submenu
        MenuBuilder mnuDisplayBuilder = new MenuBuilder("mnuDisplay", _context);
        mnuDisplayBuilder.startSubMenu("mnuCharacterOrder", true);
        mnuDisplayBuilder.addDirectiveMenuItem("mnuItCharacterOrderBest", new DisplayCharacterOrderBestDirective(), true);
        mnuDisplayBuilder.addDirectiveMenuItem("mnuItCharacterOrderNatural", new DisplayCharacterOrderNaturalDirective(), true);
        mnuDisplayBuilder.addDirectiveMenuItem("mnuItCharacterOrderSeparate", new DisplayCharacterOrderSeparateDirective(), true);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuComments", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItCommentsOn", new DisplayCommentsDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItCommentsOff", new DisplayCommentsDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuContinuous", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItContinuousOn", new DisplayContinuousDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItContinuousOff", new DisplayContinuousDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuEndIdentify", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItEndIdentifyOn", new DisplayEndIdentifyDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItEndIdentifyOff", new DisplayEndIdentifyDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.addDirectiveMenuItem("mnuItImages", new DisplayImagesDirective(), true);

        mnuDisplayBuilder.startSubMenu("mnuInapplicables", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItInapplicablesOn", new DisplayInapplicablesDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItInapplicablesOff", new DisplayInapplicablesDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuInput", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItInputOn", new DisplayInputDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItInputOff", new DisplayInputDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuKeywords", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItKeywordsOn", new DisplayKeywordsDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItKeywordsOff", new DisplayKeywordsDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuLog", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItLogOn", new DisplayLogDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItLogOff", new DisplayLogDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuNumbering", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItNumberingOn", new DisplayNumberingDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItNumberingOff", new DisplayNumberingDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuScaled", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItScaledOn", new DisplayScaledDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItScaledOff", new DisplayScaledDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuDisplayBuilder.startSubMenu("mnuUnknowns", true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItUnknownsOn", new DisplayUnknownsDirective(), true);
        mnuDisplayBuilder.addOnOffDirectiveMenuItem("mnuItUnknownsOff", new DisplayUnknownsDirective(), false);
        mnuDisplayBuilder.endSubMenu();

        mnuSettings.add(mnuDisplayBuilder.getMenu());

        // "Define" submenu
        MenuBuilder mnuDefineBuilder = new MenuBuilder("mnuDefine", _context);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineCharacters", new DefineCharactersDirective(), isDatasetLoaded);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineTaxa", new DefineTaxaDirective(), isDatasetLoaded);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineNames", new DefineNamesDirective(), isDatasetLoaded);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineButton", new DefineButtonDirective(), isDatasetLoaded);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineEndIdentify", new DefineEndIdentifyDirective(), isDatasetLoaded);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineInformation", new DefineInformationDirective(), isDatasetLoaded);
        mnuDefineBuilder.addDirectiveMenuItem("mnuItDefineSubjects", new DefineSubjectsDirective(), isDatasetLoaded);

        JMenu mnuDefine = mnuDefineBuilder.getMenu();
        mnuDefine.setEnabled(isDatasetLoaded);
        mnuSettings.add(mnuDefineBuilder.getMenu());

        // "Include" submenu
        MenuBuilder mnuIncludeBuilder = new MenuBuilder("mnuInclude", _context);
        mnuIncludeBuilder.addDirectiveMenuItem("mnuItIncludeCharacters", new IncludeCharactersDirective(), isDatasetLoaded);
        mnuIncludeBuilder.addDirectiveMenuItem("mnuItIncludeTaxa", new IncludeTaxaDirective(), isDatasetLoaded);
        JMenu mnuInclude = mnuIncludeBuilder.getMenu();
        mnuInclude.setEnabled(isDatasetLoaded);
        mnuSettings.add(mnuInclude);

        // "Exclude" submenu
        MenuBuilder mnuExcludeBuilder = new MenuBuilder("mnuExclude", _context);
        mnuExcludeBuilder.addDirectiveMenuItem("mnuItExcludeCharacters", new ExcludeCharactersDirective(), isDatasetLoaded);
        mnuExcludeBuilder.addDirectiveMenuItem("mnuItExcludeTaxa", new ExcludeTaxaDirective(), isDatasetLoaded);
        JMenu mnuExclude = mnuExcludeBuilder.getMenu();
        mnuExclude.setEnabled(isDatasetLoaded);
        mnuSettings.add(mnuExclude);

        // "Status" submenu
        MenuBuilder mnuStatusBuilder = new MenuBuilder("mnuStatus", _context);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusDisplay", new StatusDisplayDirective(), isDatasetLoaded);

        mnuStatusBuilder.startSubMenu("mnuStatusInclude", true);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusIncludeCharacters", new StatusIncludeCharactersDirective(), isDatasetLoaded);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusIncludeTaxa", new StatusIncludeTaxaDirective(), isDatasetLoaded);
        mnuStatusBuilder.endSubMenu();

        mnuStatusBuilder.startSubMenu("mnuStatusExclude", true);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusExcludeCharacters", new StatusExcludeCharactersDirective(), isDatasetLoaded);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusExcludeTaxa", new StatusExcludeTaxaDirective(), isDatasetLoaded);
        mnuStatusBuilder.endSubMenu();

        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusFiles", new StatusFilesDirective(), isDatasetLoaded);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusSet", new StatusSetDirective(), isDatasetLoaded);
        mnuStatusBuilder.addDirectiveMenuItem("mnuItStatusAll", new StatusAllDirective(), isDatasetLoaded);
        mnuSettings.add(mnuStatusBuilder.getMenu());

        return mnuSettings;
    }

    /**
     * Build the re-execute menu
     * 
     * @param actionMap
     *            The action map for the main GUI window
     * @return The JMenu for the re-execute menu
     */
    private JMenu buildReExecuteMenu(ActionMap actionMap) {
        JMenu mnuReExecute = new JMenu();
        mnuReExecute.setName("mnuReExecute");

        JMenuItem mnuItReExecute = new JMenuItem();
        mnuItReExecute.setAction(actionMap.get("mnuItReExecute"));
        mnuReExecute.add(mnuItReExecute);

        return mnuReExecute;
    }

    /**
     * Build the window menu
     * 
     * @param actionMap
     *            The action map for the main GUI window
     * @return The JMenu for the window menu
     */
    private JMenu buildWindowMenu(ActionMap actionMap) {
        JMenu mnuWindow = new JMenu();
        mnuWindow.setName("mnuWindow");

        JMenuItem mnuItCascade = new JMenuItem();
        mnuItCascade.setAction(actionMap.get("mnuItCascadeWindows"));
        mnuItCascade.setEnabled(true);
        mnuWindow.add(mnuItCascade);

        JMenuItem mnuItTile = new JMenuItem();
        mnuItTile.setAction(actionMap.get("mnuItTileWindows"));
        mnuItTile.setEnabled(true);
        mnuWindow.add(mnuItTile);

        mnuWindow.addSeparator();

        JMenuItem mnuItCloseAll = new JMenuItem();
        mnuItCloseAll.setAction(actionMap.get("mnuItCloseAllWindows"));
        mnuItCloseAll.setEnabled(true);
        mnuWindow.add(mnuItCloseAll);

        mnuWindow.addSeparator();

        JMenu mnuLF = new JMenu();
        mnuLF.setName("mnuLF");
        mnuWindow.add(mnuLF);

        JMenuItem mnuItMetalLF = new JMenuItem();
        mnuItMetalLF.setAction(actionMap.get("metalLookAndFeel"));
        mnuLF.add(mnuItMetalLF);

        JMenuItem mnuItWindowsLF = new JMenuItem();
        mnuItWindowsLF.setAction(actionMap.get("systemLookAndFeel"));
        mnuLF.add(mnuItWindowsLF);

        try {
            // Nimbus L&F was added in update java 6 update 10.
            Class.forName("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel").newInstance();
            JMenuItem mnuItNimbusLF = new JMenuItem();
            mnuItNimbusLF.setAction(actionMap.get("nimbusLookAndFeel"));
            mnuLF.add(mnuItNimbusLF);
        } catch (Exception e) {
            // The Nimbus L&F is not available, no matter.
        }

        JMenuItem mnuItSetFont = new JMenuItem();
        mnuItSetFont.setAction(actionMap.get("chooseFont"));
        mnuItSetFont.setEnabled(true);
        mnuWindow.add(mnuItSetFont);

        JMenuItem mnuItSetMainWindowSize = new JMenuItem();
        mnuItSetMainWindowSize.setAction(actionMap.get("mnuItSetMainWindowSize"));
        mnuItSetMainWindowSize.setEnabled(true);
        mnuWindow.add(mnuItSetMainWindowSize);

        return mnuWindow;
    }

    /**
     * Build the help menu
     * 
     * @param actionMap
     *            The action map for the main GUI window
     * @return The JMenu for the help menu
     */
    private JMenu buildHelpMenu(boolean advancedMode, ActionMap actionMap) {
        JMenu mnuHelp = new JMenu();
        mnuHelp.setName("mnuHelp");
        JMenuItem mnuItHelpTopics = new JMenuItem();
        mnuItHelpTopics.setName("mnuItHelpTopics");
        mnuItHelpTopics.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                UIUtils.displayHelpTopic(HELP_ID_TOPICS, getMainFrame(), e);
            }
        });
        mnuHelp.add(mnuItHelpTopics);

        if (advancedMode) {
            JMenuItem mnuItHelpCommands = new JMenuItem();
            mnuItHelpCommands.setName("mnuItHelpCommands");
            mnuItHelpCommands.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    UIUtils.displayHelpTopic(HELP_ID_COMMANDS, getMainFrame(), e);
                }

            });
            mnuHelp.add(mnuItHelpCommands);
        }

        if (isMac()) {
            configureMacAboutBox(actionMap.get("mnuItHelpAbout"));
        } else {
            JMenuItem mnuItAbout = new JMenuItem();
            mnuItAbout.setAction(actionMap.get("mnuItHelpAbout"));
            mnuHelp.add(mnuItAbout);
        }

        return mnuHelp;
    }

    // ============== File menu actions ==============================

    /**
     * Edit the dataset index
     */
    @Action
    public void mnuItEditDataSetIndex() {
        EditDatasetIndexDialog dlg = new EditDatasetIndexDialog(getMainFrame(), UIUtils.readDatasetIndex());
        show(dlg);
        List<Pair<String, String>> modifiedDatasetIndex = dlg.getModifiedDatasetIndex();
        if (modifiedDatasetIndex != null) {
            UIUtils.writeDatasetIndex(modifiedDatasetIndex);
        }

        // Rebuild menus as need to refresh the recent datasets menu so that the
        // descriptions of the datasets match the
        // descriptions of the corresponding datasets in the dataset index, if
        // applicable.
        JMenuBar menuBar = buildMenus(_advancedMode);
        getMainFrame().setJMenuBar(menuBar);
        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectComponents(getMainFrame());
    }

    /**
     * Switch to normal mode
     */
    @Action
    public void mnuItNormalMode() {
        toggleAdvancedMode();
    }

    /**
     * Switch to advanced mode
     */
    @Action
    public void mnuItAdvancedMode() {
        toggleAdvancedMode();
    }

    /**
     * Toggles between normal and advanced mode
     */
    private void toggleAdvancedMode() {
        _advancedMode = !_advancedMode;

        if (_advancedMode) {
            JMenuBar menuBar = buildMenus(true);
            getMainFrame().setJMenuBar(menuBar);
            _btnSeparate.setVisible(true);
            _btnSetMatch.setVisible(true);
            _txtFldCmdBar.setVisible(true);
            _context.setImageDisplayMode(ImageDisplayMode.MANUAL);
            _context.setDisplayEndIdentify(false);
            btnNaturalOrder();
        } else {
            JMenuBar menuBar = buildMenus(false);
            getMainFrame().setJMenuBar(menuBar);
            _btnSeparate.setVisible(false);
            _btnSetMatch.setVisible(false);
            _txtFldCmdBar.setVisible(false);
            _context.setImageDisplayMode(ImageDisplayMode.AUTO);
            _context.setDisplayEndIdentify(true);
            btnBestOrder();
        }

        // Need to update available characters because character separating
        // powers
        // are only shown in best ordering when in advanced mode.
        if (_context.getDataset() != null) {
            updateAvailableCharacters();
        }

        // Update button toolbar - some buttons are only shown in normal or
        // advanced mode
        updateDynamicButtons();

        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectComponents(getMainFrame());
        _rootPanel.revalidate();
    }

    /**
     * Exits the application
     */
    @Action
    public void mnuItExitApplication() {
        exit();
    }

    // ============================ ReExecute menu actions
    // ===========================

    /**
     * Opens the re-execute dialog
     */
    @Action
    public void mnuItReExecute() {
        ReExecuteDialog dlg = new ReExecuteDialog(getMainFrame(), _context.getExecutedDirectives(), _context);
        dlg.setVisible(true);
    }

    // ============================= Window menu actions
    // ==============================

    /**
     * Cascade windows
     */
    @Action
    public void mnuItCascadeWindows() {
        IntKeyDialogController.cascadeWindows();
    }

    /**
     * Tile windows
     */
    @Action
    public void mnuItTileWindows() {
        IntKeyDialogController.tileWindows();
    }

    /**
     * Close all windows
     */
    @Action
    public void mnuItCloseAllWindows() {
        IntKeyDialogController.closeWindows();
    }

    // ====================== Help menu actions
    // ====================================

    /**
     * Displays the about box
     */
    @Action
    public void mnuItHelpAbout() {
        AboutBox aboutBox = new AboutBox(getMainFrame(), IconHelper.createRed32ImageIcon());
        show(aboutBox);
    }

    // ============================== Global option buttons
    // ================================

    /**
     * Set up the GUI to launch contextual help for the next widget that is
     * clicked on by the user
     */
    @Action
    public void btnContextHelp() {
        // Get the HelpOnItemCursor. This is installed by the java help library.
        Cursor onItemCursor = (Cursor) UIManager.get("HelpOnItemCursor");
        getMainFrame().setCursor(onItemCursor);

        // display the (default) glasspane. This is used to intercept mouse
        // events and determine how to handle help
        // for the component that has been clicked on. See the lister defined
        // for the default glasspane in the
        // startup() method
        getMainFrame().getGlassPane().setVisible(true);
    }

    // ========================= Character toolbar button actions
    // ===================

    /**
     * Restart the investigation
     */
    @Action
    public void btnRestart() {
        executeDirective(new RestartDirective(), null);
    }

    /**
     * Switch to best character ordering
     */
    @Action
    public void btnBestOrder() {
        executeDirective(new DisplayCharacterOrderBestDirective(), null);
    }

    /**
     * Switch to separate character ordering for the currently selected taxon
     */
    @Action
    public void btnSeparate() {
        Object[] selectedRemainingTaxa = _listRemainingTaxa.getSelectedValues();
        if (selectedRemainingTaxa.length != 1) {
            displayInformationMessage(separateInformationMessage);
            return;
        }

        Item selectedTaxon = (Item) selectedRemainingTaxa[0];

        executeDirective(new DisplayCharacterOrderSeparateDirective(), Integer.toString(selectedTaxon.getItemNumber()));
    }

    /**
     * Switch to natural character ordering
     */
    @Action
    public void btnNaturalOrder() {
        executeDirective(new DisplayCharacterOrderNaturalDirective(), null);
    }

    /**
     * Display a differences report between the current specimen and the remaining taxa
     */
    @Action
    public void btnDiffSpecimenTaxa() {
        executeDirective(new DifferencesDirective(), "/E (specimen remaining) all");
    }

    /**
     * Set the tolerance
     */
    @Action
    public void btnSetTolerance() {
        executeDirective(new SetToleranceDirective(), null);
    }

    /**
     * Set the match settings
     */
    @Action
    public void btnSetMatch() {
        executeDirective(new SetMatchDirective(), null);
    }

    /**
     * Set the included characters
     */
    @Action
    public void btnSubsetCharacters() {
        executeDirective(new IncludeCharactersDirective(), null);
    }

    /**
     * Launches the find characters dialog
     */
    @Action
    public void btnFindCharacter() {
        new FindInCharactersDialog(this, _context).setVisible(true);
    }

    // ============================= Taxon toolbar button actions
    // ===========================

    /**
     * Launches the taxon information dialog for the currently selected taxa
     */
    @Action
    public void btnTaxonInfo() {
        displayInfoForSelectedTaxa();
    }

    /**
     * Launches the taxon information dialog for the currently selected taxa
     */
    private void displayInfoForSelectedTaxa() {
        List<Item> selectedTaxa = new ArrayList<Item>();

        for (int i : _listRemainingTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _availableTaxaListModel.getElementAt(i));
        }

        for (int i : _listEliminatedTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _eliminatedTaxaListModel.getElementAt(i));
        }

        // If no taxa were selected, show the information for all available taxa
        if (selectedTaxa.isEmpty()) {
            selectedTaxa.addAll(_context.getAvailableTaxa());
        }

        TaxonInformationDialog dlg = new TaxonInformationDialog(getMainFrame(), selectedTaxa, _context, _context.getImageDisplayMode() != ImageDisplayMode.OFF);
        show(dlg);
    }

    /**
     * Display a differences report for two or more selected taxa
     */
    @Action
    public void btnDiffTaxa() {
        List<Item> selectedTaxa = new ArrayList<Item>();

        for (int i : _listRemainingTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _availableTaxaListModel.getElementAt(i));
        }

        for (int i : _listEliminatedTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _eliminatedTaxaListModel.getElementAt(i));
        }

        // Ensure that at least two taxa are selected
        if (selectedTaxa.size() >= 2) {
            StringBuilder directiveTextBuilder = new StringBuilder();
            directiveTextBuilder.append("/E /I /U /X (");
            for (Item taxon : selectedTaxa) {
                directiveTextBuilder.append(" ");
                directiveTextBuilder.append(taxon.getItemNumber());
            }
            directiveTextBuilder.append(") all");

            executeDirective(new DifferencesDirective(), directiveTextBuilder.toString());
        } else {
            displayInformationMessage(UIUtils.getResourceString("SelectTwoOrMoreTaxa.caption"));
        }

    }

    /**
     * Set the included taxa
     */
    @Action
    public void btnSubsetTaxa() {
        executeDirective(new IncludeTaxaDirective(), null);
    }

    /**
     * Launches the find taxa dialog
     */
    @Action
    public void btnFindTaxon() {
        new FindInTaxaDialog(this).setVisible(true);
    }

    // =========================================================================================

    /**
     * Load the Desktop in the background.
     * We do this because Desktop.getDesktop() can be very slow
     */
    private void loadDesktopInBackground() {
        _desktopWorker = new SwingWorker<Desktop, Void>() {

            protected Desktop doInBackground() {
                if (Desktop.isDesktopSupported()) {
                    return Desktop.getDesktop();
                } else {
                    return null;
                }
            }
        };
        _desktopWorker.execute();
    }

    /**
     * Execute a directive
     * @param dir the directive to execute
     * @param data the data (arguments) for the directive
     */
    private void executeDirective(AbstractDirective<IntkeyContext> dir, String data) {
        try {
            dir.parseAndProcess(_context, data);
        } catch (Exception ex) {
            Logger.error(ex);
            String msg;
            if (ex instanceof IntkeyDirectiveParseException) {
                msg = ex.getMessage();
            } else {
                msg = UIUtils.getResourceString("ErrorWhileProcessingCommand.error", StringUtils.join(dir.getControlWords()).toUpperCase(), ex.getMessage());
            }
            displayErrorMessage(msg);
            Logger.error(msg);
        }
    }

    /**
     * Called to initialize a new identification
     */
    private void initializeIdentification() {
        handleUpdateAll();
    }

    /**
     * Update the view of available characters
     */
    private void updateAvailableCharacters() {

        IntkeyCharacterOrder charOrder = _context.getCharacterOrder();

        Item taxonToSeparate = null;
        String formattedTaxonToSeparateName = null;

        switch (charOrder) {
        case SEPARATE:
            taxonToSeparate = _context.getDataset().getItem(_context.getTaxonToSeparate());
            formattedTaxonToSeparateName = _taxonformatter.formatItemDescription(taxonToSeparate);
            if (!_context.getAvailableTaxa().contains(taxonToSeparate)) {
                _listAvailableCharacters.setModel(new DefaultListModel());
                _lblNumAvailableCharacters.setText(MessageFormat.format(separateCharactersCaption, formattedTaxonToSeparateName, 0));
                break;
            }

            // If taxon to separate has not been eliminated, drop through and
            // display the best characters for taxon separation
        case BEST:
            LinkedHashMap<Character, Double> bestCharactersMap = _context.getBestOrSeparateCharacters();
            if (bestCharactersMap != null) {
                if (charOrder == IntkeyCharacterOrder.BEST) {
                    _lblNumAvailableCharacters.setText(MessageFormat.format(bestCharactersCaption, bestCharactersMap.keySet().size()));
                } else {
                    _lblNumAvailableCharacters.setText(MessageFormat.format(separateCharactersCaption, formattedTaxonToSeparateName, bestCharactersMap.keySet().size()));
                }
                if (bestCharactersMap.isEmpty()) {
                    handleNoAvailableCharacters();
                    return;
                } else {
                    _availableCharacterListModel = new DefaultListModel();
                    for (Character ch : bestCharactersMap.keySet()) {
                        _availableCharacterListModel.addElement(ch);
                    }
                    _availableCharacterListModel.copyInto(bestCharactersMap.keySet().toArray());

                    // Only display character separating powers if in advanced
                    // mode.
                    if (_advancedMode) {
                        _availableCharactersListCellRenderer = new BestCharacterCellRenderer(bestCharactersMap, _context.displayNumbering());
                    } else {
                        _availableCharactersListCellRenderer = new CharacterCellRenderer(_context.displayNumbering());
                    }
                    _listAvailableCharacters.setCellRenderer(_availableCharactersListCellRenderer);
                    _listAvailableCharacters.setModel(_availableCharacterListModel);
                }
            } else {
                _availableCharacterListModel = null;

                // The best characters list is not cached and needs to be
                // calculated. This is a
                // long-running operation so use a
                // SwingWorker to do it on a different thread, and update
                // the
                // available characters list when
                // it is complete.
                GetBestCharactersWorker worker = new GetBestCharactersWorker(_context);
                worker.execute();

                // Show the busy glass pane with a message if worker has not
                // completed within
                // 250 milliseconds. This avoids "flickering" of the
                // glasspane
                // when it takes a
                // very short time to calculate the best characters.
                try {
                    Thread.sleep(250);
                    if (!worker.isDone()) {
                        displayBusyMessage(calculatingBestCaption);
                    }
                } catch (InterruptedException ex) {
                    // do nothing
                }

                return;
            }

            break;
        case NATURAL:
            int lastSelectedIndex = _listAvailableCharacters.getSelectedIndex();

            List<Character> availableCharacters = new ArrayList<Character>(_context.getAvailableCharacters());
            _lblNumAvailableCharacters.setText(MessageFormat.format(availableCharactersCaption, availableCharacters.size()));
            if (availableCharacters.size() == 0) {
                handleNoAvailableCharacters();
                return;
            } else {
                _availableCharacterListModel = new DefaultListModel();
                for (Character ch : availableCharacters) {
                    _availableCharacterListModel.addElement(ch);
                }
                _availableCharactersListCellRenderer = new CharacterCellRenderer(_context.displayNumbering());
                _listAvailableCharacters.setCellRenderer(_availableCharactersListCellRenderer);
                _listAvailableCharacters.setModel(_availableCharacterListModel);

                // Select the same index that was previously selected. This will
                // have the effect of selecting the character after the
                // previously used character.
                _listAvailableCharacters.setSelectedIndex(lastSelectedIndex);
            }
            break;
        default:
            throw new RuntimeException("Unrecognized character order");
        }

        // The viewport of the available characters scroll pane may be
        // displaying a
        // message due to an investigation finishing, or no characters being
        // available
        // previously. Ensure that the available characters list is now
        // displayed again.
        if (!_sclPaneAvailableCharacters.getViewport().getView().equals(_listAvailableCharacters)) {
            _sclPaneAvailableCharacters.setViewportView(_listAvailableCharacters);
            _sclPaneAvailableCharacters.revalidate();
        }
    }

    /**
     * Called to handle the case that no characters are available
     */
    private void handleNoAvailableCharacters() {
        String message = null;

        if (_context.getIncludedCharacters().size() < _context.getDataset().getNumberOfCharacters()) { // characters
            message = charactersExcludedCannotSeparateCaption;
        } else {
            if (_context.getTolerance() > 0) {
                message = mismatchesAllowCannotSeparateCaption;
            } else {
                message = availableCharactersCannotSeparateCaption;
            }
        }

        MessagePanel messagePanel = new MessagePanel(message, HELP_ID_NO_CHARACTERS_REMAINING);
        _sclPaneAvailableCharacters.setViewportView(messagePanel);
        _sclPaneAvailableCharacters.revalidate();
    }

    /**
     * Used to calculate the best characters in a separate thread, then update
     * the UI accordingly when the operation is finished
     * 
     * @author ChrisF
     * 
     */
    private class GetBestCharactersWorker extends SwingWorker<Void, Void> {

        private IntkeyContext _context;

        public GetBestCharactersWorker(IntkeyContext context) {
            super();
            _context = context;
        }

        @Override
        protected Void doInBackground() throws Exception {
            _context.calculateBestOrSeparateCharacters();
            return null;
        }

        @Override
        protected void done() {
            updateAvailableCharacters();
            removeBusyMessage();

            // select the first character every time best (or separate)
            // characters are selected.
            _listAvailableCharacters.setSelectedIndex(0);
        }
    }

    /**
     * Update the view of used characters
     */
    private void updateUsedCharacters() {

        Specimen specimen = _context.getSpecimen();
        List<Character> usedCharacters = specimen.getUsedCharacters();

        List<Attribute> usedCharacterValues = new ArrayList<Attribute>();
        for (Character ch : usedCharacters) {
            usedCharacterValues.add(specimen.getAttributeForCharacter(ch));
        }

        _usedCharacterListModel = new DefaultListModel();
        for (Attribute attr : usedCharacterValues) {
            _usedCharacterListModel.addElement(attr);
        }
        _usedCharactersListCellRenderer = new AttributeCellRenderer(_context.displayNumbering(), _context.getDataset().getOrWord());
        _listUsedCharacters.setCellRenderer(_usedCharactersListCellRenderer);
        _listUsedCharacters.setModel(_usedCharacterListModel);

        _lblNumUsedCharacters.setText(MessageFormat.format(usedCharactersCaption, _usedCharacterListModel.getSize()));
    }

    /**
     * Update the view of available taxa
     * @param availableTaxa the available taxa
     * @param taxaDifferingCharacters The differing characters for each taxa. Used when the tolerance is greater than zero to display a 
     * count of differing characters against each taxon 
     */
    private void updateAvailableTaxa(List<Item> availableTaxa, Map<Item, Set<Character>> taxaDifferingCharacters) {
        _availableTaxaListModel = new DefaultListModel();

        if (_context.getTolerance() > 0 && taxaDifferingCharacters != null) {
            // sort available taxa by difference count
            Collections.sort(availableTaxa, new DifferenceCountComparator(taxaDifferingCharacters));
            _availableTaxaCellRenderer = new TaxonWithDifferenceCountCellRenderer(taxaDifferingCharacters, _context.displayNumbering(), _context.displayComments());
        } else {
            _availableTaxaCellRenderer = new TaxonCellRenderer(_context.displayNumbering(), _context.displayComments());
        }

        for (Item taxon : availableTaxa) {
            _availableTaxaListModel.addElement(taxon);
        }

        _listRemainingTaxa.setCellRenderer(_availableTaxaCellRenderer);
        _listRemainingTaxa.setModel(_availableTaxaListModel);

        _lblNumRemainingTaxa.setText(MessageFormat.format(remainingTaxaCaption, _availableTaxaListModel.getSize()));

        _listRemainingTaxa.repaint();
    }

    /**
     * Update the view of available taxa
     * @param availableTaxa the available taxa
     * @param taxaDifferingCharacters The differing characters for each taxa. Used when the tolerance is greater than zero to display a 
     * count of differing characters against each taxon 
     */
    private void updateEliminatedTaxa(List<Item> eliminatedTaxa, Map<Item, Set<Character>> taxaDifferingCharacters) {
        // sort eliminated taxa by difference count
        Collections.sort(eliminatedTaxa, new DifferenceCountComparator(taxaDifferingCharacters));

        _eliminatedTaxaListModel = new DefaultListModel();

        for (Item taxon : eliminatedTaxa) {
            _eliminatedTaxaListModel.addElement(taxon);
        }

        _eliminatedTaxaCellRenderer = new TaxonWithDifferenceCountCellRenderer(taxaDifferingCharacters, _context.displayNumbering(), _context.displayComments());

        _listEliminatedTaxa.setCellRenderer(_eliminatedTaxaCellRenderer);
        _listEliminatedTaxa.setModel(_eliminatedTaxaListModel);

        _lblEliminatedTaxa.setText(MessageFormat.format(eliminatedTaxaCaption, _eliminatedTaxaListModel.getSize()));

        _listEliminatedTaxa.repaint();
    }

    // ================================== IntkeyUI methods
    // ===========================================================

    @Override
    public void handleNewDataset(IntkeyDataset dataset) {
        _lastOpenedDatasetDirectory = _context.getDatasetStartupFile().getParentFile();

        getMainFrame().setTitle(MessageFormat.format(windowTitleWithDatasetTitle, dataset.getHeading()));

        // enable toolbar buttons
        IntkeyCharacterOrder characterOrder = _context.getCharacterOrder();
        _btnRestart.setEnabled(true);
        _btnBestOrder.setEnabled(characterOrder != IntkeyCharacterOrder.BEST);
        _btnSeparate.setEnabled(true);
        _btnNaturalOrder.setEnabled(characterOrder != IntkeyCharacterOrder.NATURAL);
        _btnSetTolerance.setEnabled(true);
        _btnSetMatch.setEnabled(true);
        _btnSubsetCharacters.setEnabled(true);
        _btnFindCharacter.setEnabled(true);

        _btnTaxonInfo.setEnabled(true);
        _btnDiffTaxa.setEnabled(true);
        _btnSubsetTaxa.setEnabled(true);
        _btnFindTaxon.setEnabled(true);

        // display startup images
        if (!_suppressStartupImages) {
            List<Image> startupImages = dataset.getStartupImages();
            if (!startupImages.isEmpty()) {
                ImageUtils.displayStartupScreen(startupImages, _context.getImageSettings(), getMainFrame());
            }
        }

        // Need to refresh the menus as some menus/menu items are disabled when
        // the dataset is not loaded. Also the
        // list of recent datasets may need to be refreshed after the closing of
        // the previous dataset (if applicable)
        JMenuBar menuBar = buildMenus(_advancedMode);
        getMainFrame().setJMenuBar(menuBar);
        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectComponents(getMainFrame());

        initializeIdentification();

        _rootPanel.revalidate();
    }

    @Override
    public void handleDatasetClosed() {
        if (_context.getDataset() != null) {
            saveCurrentlyOpenedDataset();
        }
    }

    @Override
    public void handleUpdateAll() {
        if (_context.getDataset() != null) { // Only update if we have a dataset
                                             // loaded.
            List<Item> availableTaxa = _context.getAvailableTaxa();
            List<Item> eliminatedTaxa = _context.getEliminatedTaxa();

            _btnDiffSpecimenTaxa.setEnabled(availableTaxa.size() > 0 && eliminatedTaxa.size() > 0);

            // Disable button for selected best or natural order.
            _btnNaturalOrder.setEnabled(true);
            _btnBestOrder.setEnabled(true);
            switch (_context.getCharacterOrder()) {
            case NATURAL:
                _btnNaturalOrder.setEnabled(false);
                break;
            case BEST:
                _btnBestOrder.setEnabled(false);
                break;
            case SEPARATE:
                // do nothing
                break;
            default:
                throw new RuntimeException("Unrecognized character order");
            }

            // Need to display a message in place of the list of available
            // characters
            // if there are no remaining taxa (no matching taxa remain), or only
            // 1
            // remaining taxon (identification complete)
            if (availableTaxa.size() > 1) {
                updateAvailableCharacters();
            } else {
                JPanel messagePanel = null;

                if (availableTaxa.size() == 0) {
                    messagePanel = new AllowMismatchMessagePanel(noMatchingTaxaRemainCaption, HELP_ID_NO_MATCHING_TAXA_REMAIN, _context);
                } else {
                    // 1 available taxon
                    messagePanel = new MessagePanel(identificationCompleteCaption, HELP_ID_IDENTIFICATION_COMPLETE);
                }

                _sclPaneAvailableCharacters.setViewportView(messagePanel);
                _sclPaneAvailableCharacters.revalidate();

                switch (_context.getCharacterOrder()) {
                case NATURAL:
                    _lblNumAvailableCharacters.setText(MessageFormat.format(availableCharactersCaption, 0));
                    break;
                case BEST:
                    _lblNumAvailableCharacters.setText(MessageFormat.format(bestCharactersCaption, 0));
                    break;
                case SEPARATE:
                    Item taxonToSeparate = _context.getDataset().getItem(_context.getTaxonToSeparate());
                    String formattedTaxonName = _taxonformatter.formatItemDescription(taxonToSeparate);
                    _lblNumAvailableCharacters.setText(MessageFormat.format(separateCharactersCaption, formattedTaxonName, 0));
                    break;
                default:
                    throw new RuntimeException("Unrecognized character order");
                }
            }

            updateUsedCharacters();
            updateAvailableTaxa(availableTaxa, _context.getSpecimen().getTaxonDifferences());
            updateEliminatedTaxa(eliminatedTaxa, _context.getSpecimen().getTaxonDifferences());

            updateDynamicButtons();
        }
    }

    @Override
    public void handleIdentificationRestarted() {
        _btnDiffSpecimenTaxa.setEnabled(false);
        handleUpdateAll();
        if (_context.isDemonstrationMode()) {
            IntKeyDialogController.closeWindows();
        }
    }

    @Override
    public void displayRTFReportFromFile(File rtfFile, String title) {
        long mbInBytes = 1024 * 1024;

        long fileSizeInMB = rtfFile.length() / mbInBytes;

        // give the user the option of saving the file instead if the file is
        // 5MB or more in size
        if (fileSizeInMB >= 5) {
            boolean saveFile = promptForYesNoOption(MessageFormat.format(saveReportToFilePrompt, fileSizeInMB));
            if (saveFile) {
                List<String> fileExtensions = new ArrayList<String>();
                fileExtensions.add("rtf");
                try {
                    File destinationFile = promptForFile(fileExtensions, UIUtils.getResourceString("RtfReportDisplayDialog.fileFilterDescription"), true);
                    if (destinationFile == null) {
                        // user hit cancel.
                        return;
                    }
                    FileUtils.copyFile(rtfFile, destinationFile);
                } catch (IOException ex) {
                    displayErrorMessage(errorWritingToFileError);
                }
                return;
            }
        }

        // If the file has not been saved, display its contents.
        try {
            String rtfSource = FileUtils.readFileToString(rtfFile);
            displayRTFReport(rtfSource, title);
        } catch (OutOfMemoryError err) {
            displayErrorMessage(rtfFileTooLargeError);
        } catch (Exception ex) {
            displayErrorMessage(errorWritingToFileError);
        }
    }

    @Override
    public void displayRTFReport(String rtfSource, String title) {
        DisplayRTFWorker worker = new DisplayRTFWorker(rtfSource, title);
        worker.execute();

        // Show the busy glass pane with a message if worker has not
        // completed within
        // 250 milliseconds. This avoids "flickering" of the glasspane
        // when it takes a
        // very short time to display the RTF report
        try {
            Thread.sleep(250);
            if (!worker.isDone()) {
                displayBusyMessageAllowCancelWorker(displayingReportCaption, worker);
            }
        } catch (InterruptedException ex) {
            // do nothing
        }
    }

    private class DisplayRTFWorker extends SwingWorker<Void, Void> {

        private String _rtfSource;
        private String _title;
        private RtfReportDisplayDialog _dlg;

        public DisplayRTFWorker(String rtfSource, String title) {
            _rtfSource = rtfSource;
            _title = title;
        }

        @Override
        public Void doInBackground() {
            _dlg = new RtfReportDisplayDialog(getMainFrame(), new SimpleRtfEditorKit(null), _rtfSource, _title);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                Intkey.this.show(_dlg);
            } catch (CancellationException ex) {
                // display of RTF content was cancelled - no action required.
            } catch (Exception ex) {
                // A runtime exception is thrown by the RtfReportDisplayDialog
                // if
                // the RTF was invalid. This will result in the dialog being
                // null.
                displayErrorMessage(badlyFormedRTFContentMessage);
            } finally {
                removeBusyMessage();
            }
        }

    }

    @Override
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(getMainFrame(), message, errorDlgTitle, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void displayInformationMessage(String message) {
        JOptionPane.showMessageDialog(getMainFrame(), message, informationDlgTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void displayBusyMessage(String message) {
        if (_busyGlassPane == null) {
            _busyGlassPane = new BusyGlassPane(message);
            getMainFrame().setGlassPane(_busyGlassPane);
            _busyGlassPane.setVisible(true);
            getMainFrame().validate();
            _busyGlassPane.paintImmediately(getMainFrame().getBounds());
        } else {
            _busyGlassPane.setMessage(message);
            _busyGlassPane.paintImmediately(getMainFrame().getBounds());
        }
    }

    @Override
    public void displayBusyMessageAllowCancelWorker(String message, SwingWorker<?, ?> worker) {
        displayBusyMessage(message);
        _busyGlassPane.setWorkerForCancellation(worker);
    }

    @Override
    public void removeBusyMessage() {
        if (_busyGlassPane != null) {
            _busyGlassPane.setVisible(false);
            _busyGlassPane = null;
            getMainFrame().setGlassPane(_defaultGlassPane);
        }
    }

    @Override
    public void displayTaxonInformation(List<Item> taxa, String imagesAutoDisplayText, String otherItemsAutoDisplayText, boolean closePromptAfterAutoDisplay) {
        TaxonInformationDialog dlg = new TaxonInformationDialog(getMainFrame(), taxa, _context, _context.getImageDisplayMode() != ImageDisplayMode.OFF);

        if (imagesAutoDisplayText != null) {
            dlg.displayImagesWithTextInSubject(imagesAutoDisplayText);
        }

        if (otherItemsAutoDisplayText != null) {
            dlg.displayOtherItemsWithTextInDescription(otherItemsAutoDisplayText);
        }

        // Don't bother showing the information dialog if it is just going to be
        // closed again straight away.
        if (!closePromptAfterAutoDisplay || (imagesAutoDisplayText == null && otherItemsAutoDisplayText == null)) {
            show(dlg);
        }
    }

    @Override
    public void addToolbarButton(boolean advancedModeOnly, boolean normalModeOnly, boolean inactiveUnlessUsedCharacters, String imageFileName, List<String> commands, String shortHelp, String fullHelp) {
        Icon icon = null;

        // Is the image file an absolute file?
        File iconFile = new File(imageFileName);
        if (iconFile.exists() && iconFile.isAbsolute()) {
            try {
                icon = readImageIconFromFile(iconFile);
            } catch (IOException ex) {
                displayErrorMessage(UIUtils.getResourceString("ErrorReadingIconImageFromFile.error", iconFile.getAbsolutePath()));
            }
        }

        // Is the image file relative to the dataset directory?
        if (icon == null) {
            File relativeIconFile = new File(_context.getDatasetDirectory(), imageFileName);
            if (relativeIconFile.exists() && relativeIconFile.isAbsolute()) {
                try {
                    icon = readImageIconFromFile(relativeIconFile);
                } catch (IOException ex) {
                    displayErrorMessage(UIUtils.getResourceString("ErrorReadingIconImageFromFile.error", iconFile.getAbsolutePath()));
                }
            }
        }

        // try getting an icon with the exact image name from the icon resources
        if (icon == null) {
            try {
                icon = IconHelper.createImageIconFromAbsolutePath(INTKEY_ICON_PATH + "/" + imageFileName);
            } catch (Exception ex) {
                // do nothing
            }
        }

        if (icon == null && imageFileName.toLowerCase().endsWith(".bmp")) {
            // try substituting ".bmp" for ".png" and reading from the icon
            // resources. All the default
            // icons that come with Intkey have been convert to png format.
            String modifiedImageFileName = imageFileName.replaceFirst(".bmp$", ".png");

            try {
                icon = IconHelper.createImageIconFromAbsolutePath(INTKEY_ICON_PATH + "/" + modifiedImageFileName);
            } catch (Exception ex) {
                // do nothing
            }
        }

        if (icon == null) {
            displayErrorMessage(UIUtils.getResourceString("CouldNotFromImage.error", imageFileName));
            return;
        }

        JButton button = new JButton(icon);
        button.setToolTipText(shortHelp);
        button.setMargin(new Insets(0, 0, 0, 0));
        _pnlDynamicButtons.add(button);

        final List<String> commandsCopy = new ArrayList<String>(commands);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (String command : commandsCopy) {
                    _context.parseAndExecuteDirective(command);
                }

            }
        });

        if (advancedModeOnly && !normalModeOnly) {
            _advancedModeOnlyDynamicButtons.add(button);
        }

        if (normalModeOnly && !advancedModeOnly) {
            _normalModeOnlyDynamicButtons.add(button);
        }

        if (inactiveUnlessUsedCharacters) {
            _activeOnlyWhenCharactersUsedButtons.add(button);
        }

        _dynamicButtonsFullHelp.put(button, fullHelp);

        updateDynamicButtons();
    }

    private void updateDynamicButtons() {
        for (JButton b : _advancedModeOnlyDynamicButtons) {
            b.setVisible(_advancedMode);
        }

        for (JButton b : _normalModeOnlyDynamicButtons) {
            b.setVisible(!_advancedMode);
        }

        for (JButton b : _activeOnlyWhenCharactersUsedButtons) {
            if (_usedCharacterListModel != null) {
                b.setEnabled(_usedCharacterListModel.size() > 0);
            } else {
                b.setEnabled(false);
            }
        }

        _rootPanel.revalidate();
    }

    private ImageIcon readImageIconFromFile(File iconFile) throws IOException {
        BufferedImage img = ImageIO.read(iconFile);
        ImageIcon imgIcon = new ImageIcon(img);
        return imgIcon;
    }

    @Override
    public void addToolbarSpace() {
        JPanel spacerPanel = new JPanel();
        spacerPanel.setMinimumSize(new Dimension(20, 1));
        _pnlDynamicButtons.add(spacerPanel);
        _rootPanel.revalidate();
    }

    @Override
    public void clearToolbar() {
        _advancedModeOnlyDynamicButtons.clear();
        _normalModeOnlyDynamicButtons.clear();
        _activeOnlyWhenCharactersUsedButtons.clear();
        _pnlDynamicButtons.removeAll();
        _rootPanel.revalidate();
    }

    @Override
    public void illustrateCharacters(List<Character> characters) {
        if (_context.getImageDisplayMode() == ImageDisplayMode.OFF) {
            displayErrorMessage(UIUtils.getResourceString("ImageDisplayDisabled.error"));
        } else {
            try {
                CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), characters, null, _context.getImageSettings(), false, false, _context.displayScaled());
                dlg.displayImagesForCharacter(characters.get(0));
                show(dlg);

            } catch (IllegalArgumentException ex) {
                // Display error message if unable to display
                displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayImage.error", ex.getMessage()));
            }
        }
    }

    @Override
    public void illustrateTaxa(List<Item> taxa) {
        if (_context.getImageDisplayMode() == ImageDisplayMode.OFF) {
            displayErrorMessage(UIUtils.getResourceString("ImageDisplayDisabled.error"));
        } else {
            try {
                TaxonImageDialog dlg = new TaxonImageDialog(getMainFrame(), _context.getImageSettings(), taxa, false, !_context.displayContinuous(), _context.displayScaled(),
                        _context.getImageSubjects(), this);
                dlg.displayImagesForTaxon(taxa.get(0), 0);
                show(dlg);
            } catch (IllegalArgumentException ex) {
                // Display error message if unable to display
                displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayImage.error", ex.getMessage()));
            }
        }
    }

    @Override
    public void displayContents(LinkedHashMap<String, String> contentsMap) {
        final ContentsDialog dlg = new ContentsDialog(getMainFrame(), contentsMap, _context);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                show(dlg);
            }
        });
    }

    @Override
    public void displayFile(URL fileURL, String description) {
        try {
            UIUtils.displayFileFromURL(fileURL, description, _desktopWorker.get());
        } catch (UnsupportedOperationException ex) {
            Logger.error(ex);
            promptForString(UIUtils.getResourceString("CouldNotDisplayFileDesktopError.error", fileURL.toString()), fileURL.toString(), "");
        } catch (Exception ex) {
            Logger.error(ex);
            displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayFile.error", fileURL.toString()));
        }
    }

    @Override
    public boolean isLogVisible() {
        return _logDialog.isVisible();
    }

    @Override
    public void setLogVisible(boolean visible) {
        if (visible) {
            show(_logDialog);
        } else {
            _logDialog.setVisible(false);
        }
    }

    @Override
    public void updateLog() {
        List<String> logEntries = _context.getLogEntries();
        RTFBuilder builder = new RTFBuilder();
        builder.startDocument();
        builder.setFont(1);
        for (String line : logEntries) {
            // directive calls are identified by a leading asterisk. They should
            // be colored blue.
            // All other lines should be colored red.
            if (line.trim().startsWith("*")) {
                builder.setTextColor(Color.BLUE);
            } else {
                builder.setTextColor(Color.RED);
            }
            String escapedLine = RTFUtils.escapeRTF(line);
            builder.appendText(escapedLine);
        }
        builder.endDocument();

        _logDialog.setContent(builder.toString());
    }

    @Override
    public void quitApplication() {
        exit();
    }

    @Override
    public List<Item> getSelectedTaxa() {
        List<Item> retList = new ArrayList<Item>();

        for (Object oTaxon : _listRemainingTaxa.getSelectedValues()) {
            retList.add((Item) oTaxon);
        }

        for (Object oTaxon : _listEliminatedTaxa.getSelectedValues()) {
            retList.add((Item) oTaxon);
        }

        return retList;
    }

    @Override
    public List<Character> getSelectedCharacters() {
        List<Character> retList = new ArrayList<Character>();

        for (Object oCh : _listAvailableCharacters.getSelectedValues()) {
            retList.add((Character) oCh);
        }

        for (Object oAttr : _listUsedCharacters.getSelectedValues()) {
            retList.add(((Attribute) oAttr).getCharacter());
        }

        return retList;
    }

    @Override
    public void setDemonstrationMode(boolean demonstrationMode) {
        if (demonstrationMode) {
            // If in advanced mode, switch to basic mode
            if (_advancedMode) {
                toggleAdvancedMode();
            }
        }

        getMainFrame().getJMenuBar().setVisible(!demonstrationMode);
    }

    @Override
    public void displayHelpTopic(String topicID) {
        _helpController.displayHelpTopic(getMainFrame(), topicID);
    }

    @Override
    public boolean isAdvancedMode() {
        return _advancedMode;
    }

    // ================================== DirectivePopulator methods
    // ===================================================================

    @Override
    public List<Character> promptForCharactersByKeyword(String directiveName, boolean permitSelectionFromIncludedCharactersOnly, boolean noneKeywordAvailable, List<String> returnSelectedKeywords) {
        List<Image> characterKeywordImages = _context.getDataset().getCharacterKeywordImages();
        if (_context.getImageDisplayMode() == ImageDisplayMode.AUTO && characterKeywordImages != null && !characterKeywordImages.isEmpty()) {
            ImageDialog dlg = new ImageDialog(getMainFrame(), _context.getImageSettings(), true, _context.displayScaled());
            dlg.setImages(characterKeywordImages);
            dlg.showImage(0);
            dlg.setTitle(MessageFormat.format(selectCharacterKeywordsCaption, directiveName));

            show(dlg);

            if (!dlg.okButtonPressed()) {
                // user cancelled
                return null;
            }

            Set<String> keywords = dlg.getSelectedKeywords();

            if (!noneKeywordAvailable) {
                keywords.remove(IntkeyContext.CHARACTER_KEYWORD_NONE);
            }

            List<Character> selectedCharacters = new ArrayList<Character>();

            for (String keyword : keywords) {
                selectedCharacters.addAll(_context.getCharactersForKeyword(keyword));
                returnSelectedKeywords.add(keyword);
            }

            if (permitSelectionFromIncludedCharactersOnly) {
                selectedCharacters.retainAll(_context.getIncludedCharacters());
            }

            return selectedCharacters;
        } else {
            CharacterKeywordSelectionDialog dlg = new CharacterKeywordSelectionDialog(getMainFrame(), _context, directiveName.toUpperCase(), permitSelectionFromIncludedCharactersOnly);
            show(dlg);
            returnSelectedKeywords.addAll(dlg.getSelectedKeywords());
            return dlg.getSelectedCharacters();
        }
    }

    @Override
    public List<Character> promptForCharactersByList(String directiveName, boolean selectFromAvailableCharactersOnly, List<String> returnSelectedKeywords) {
        List<Character> charactersToSelect;

        String keyword = null;
        if (selectFromAvailableCharactersOnly) {
            charactersToSelect = _context.getCharactersForKeyword(IntkeyContext.CHARACTER_KEYWORD_AVAILABLE);
            keyword = IntkeyContext.CHARACTER_KEYWORD_AVAILABLE;
        } else {
            charactersToSelect = _context.getCharactersForKeyword(IntkeyContext.CHARACTER_KEYWORD_ALL);
            keyword = IntkeyContext.CHARACTER_KEYWORD_ALL;

        }
        CharacterSelectionDialog dlg = new CharacterSelectionDialog(getMainFrame(), charactersToSelect, directiveName.toUpperCase(), keyword, _context.getImageSettings(), _context.displayNumbering(),
                _context);
        show(dlg);
        returnSelectedKeywords.addAll(dlg.getSelectedKeywords());
        return dlg.getSelectedCharacters();
    }

    @Override
    public List<Item> promptForTaxaByKeyword(String directiveName, boolean permitSelectionFromIncludedTaxaOnly, boolean noneKeywordAvailable, boolean includeSpecimenAsOption,
            MutableBoolean returnSpecimenSelected, List<String> returnSelectedKeywords) {

        List<Image> taxonKeywordImages = _context.getDataset().getTaxonKeywordImages();
        if (_context.getImageDisplayMode() == ImageDisplayMode.AUTO && taxonKeywordImages != null && !taxonKeywordImages.isEmpty()) {
            ImageDialog dlg = new ImageDialog(getMainFrame(), _context.getImageSettings(), true, _context.displayScaled());
            dlg.setImages(taxonKeywordImages);
            dlg.setTitle(MessageFormat.format(selectTaxonKeywordsCaption, directiveName));
            dlg.showImage(0);
            show(dlg);

            if (!dlg.okButtonPressed()) {
                // user cancelled
                return null;
            }

            Set<String> keywords = dlg.getSelectedKeywords();

            if (!noneKeywordAvailable) {
                keywords.remove(IntkeyContext.TAXON_KEYWORD_NONE);
            }

            List<Item> selectedTaxa = new ArrayList<Item>();

            for (String keyword : keywords) {
                selectedTaxa.addAll(_context.getTaxaForKeyword(keyword));
                returnSelectedKeywords.add(keyword);
            }

            if (permitSelectionFromIncludedTaxaOnly) {
                selectedTaxa.retainAll(_context.getIncludedTaxa());
            }

            return selectedTaxa;
        } else {
            TaxonKeywordSelectionDialog dlg = new TaxonKeywordSelectionDialog(getMainFrame(), _context, directiveName.toUpperCase(), permitSelectionFromIncludedTaxaOnly, includeSpecimenAsOption,
                    returnSpecimenSelected);
            show(dlg);
            returnSelectedKeywords.addAll(dlg.getSelectedKeywords());
            return dlg.getSelectedTaxa();
        }
    }

    @Override
    public List<Item> promptForTaxaByList(String directiveName, boolean selectFromRemainingTaxaOnly, boolean autoSelectSingleValue, boolean singleSelect, boolean includeSpecimenAsOption,
            MutableBoolean returnSpecimenSelected, List<String> returnSelectedKeywords) {
        List<Item> taxaToSelect;

        String keyword = null;
        if (selectFromRemainingTaxaOnly) {
            taxaToSelect = _context.getTaxaForKeyword(IntkeyContext.TAXON_KEYWORD_REMAINING);
            keyword = IntkeyContext.TAXON_KEYWORD_REMAINING;
        } else {
            taxaToSelect = _context.getTaxaForKeyword(IntkeyContext.TAXON_KEYWORD_ALL);
            keyword = IntkeyContext.TAXON_KEYWORD_ALL;
        }

        if (taxaToSelect.size() == 1 && autoSelectSingleValue) {
            return taxaToSelect;
        } else {
            TaxonSelectionDialog dlg = new TaxonSelectionDialog(getMainFrame(), taxaToSelect, directiveName.toUpperCase(), keyword, _context.displayNumbering(), singleSelect, _context,
                    includeSpecimenAsOption, returnSpecimenSelected);
            show(dlg);
            returnSelectedKeywords.addAll(dlg.getSelectedKeywords());
            return dlg.getSelectedTaxa();
        }
    }

    @Override
    public Boolean promptForYesNoOption(String message) {
        int selectedOption = JOptionPane.showConfirmDialog(getMainFrame(), message, null, JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String promptForString(String message, String initialValue, String directiveName) {
        return (String) JOptionPane.showInputDialog(getMainFrame(), message, directiveName, JOptionPane.PLAIN_MESSAGE, null, null, initialValue);
    }

    @Override
    public List<String> promptForTextValue(TextCharacter ch, List<String> currentValues) {
        List<String> inputValues = null;

        if (_context.getImageDisplayMode() == ImageDisplayMode.AUTO && !ch.getImages().isEmpty()) {
            try {
                CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), null, _context.getImageSettings(), true, true, _context.displayScaled());
                dlg.setInitialTextValues(currentValues);
                dlg.displayImagesForCharacter(ch);
                show(dlg);
                if (dlg.okButtonPressed()) {
                    inputValues = dlg.getInputTextValues();
                } else if (dlg.cancelButtonPressed()) {
                    return null;
                }
            } catch (IllegalArgumentException ex) {
                // Display error message if unable to display
                displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayImage.error", ex.getMessage()));
                return null;
            }
        }

        if (inputValues == null) {
            TextInputDialog dlg = new TextInputDialog(getMainFrame(), ch, currentValues, _context.getImageSettings(), _context.displayNumbering(),
                    _context.getImageDisplayMode() != ImageDisplayMode.OFF, _context.displayScaled(), _advancedMode);
            UIUtils.showDialog(dlg);
            if (dlg.okPressed()) {
                inputValues = dlg.getInputData();
            }
        }

        return inputValues;
    }

    @Override
    public Set<Integer> promptForIntegerValue(IntegerCharacter ch, Set<Integer> currentValues) {
        Set<Integer> returnValues = new HashSet<Integer>();
        Set<Integer> rawInputValues = null;

        if (_context.getImageDisplayMode() == ImageDisplayMode.AUTO && !ch.getImages().isEmpty()) {
            try {
                CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), null, _context.getImageSettings(), true, true, _context.displayScaled());
                dlg.setInitialIntegerValues(currentValues);
                dlg.displayImagesForCharacter(ch);
                show(dlg);
                if (dlg.okButtonPressed()) {
                    rawInputValues = dlg.getInputIntegerValues();
                } else if (dlg.cancelButtonPressed()) {
                    return null;
                }
            } catch (IllegalArgumentException ex) {
                // Display error message if unable to display
                displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayImage.error", ex.getMessage()));
                return null;
            }
        }

        if (rawInputValues == null) {
            IntegerInputDialog dlg = new IntegerInputDialog(getMainFrame(), ch, currentValues, _context.getImageSettings(), _context.displayNumbering(),
                    _context.getImageDisplayMode() != ImageDisplayMode.OFF, _context.displayScaled(), _advancedMode);
            UIUtils.showDialog(dlg);
            if (dlg.okPressed()) {
                rawInputValues = dlg.getInputData();
            }
        }

        // The only acceptable values for an integer character are minimum - 1,
        // any values in the range minimum-maximum, or maximum + 1. Modify the
        // raw input values
        // in accordance with this.
        if (rawInputValues != null) {
            for (int value : rawInputValues) {
                if (value <= ch.getMinimumValue() - 1) {
                    returnValues.add(ch.getMinimumValue() - 1);
                } else if (value >= ch.getMaximumValue() + 1) {
                    returnValues.add(ch.getMaximumValue() + 1);
                } else {
                    returnValues.add(value);
                }
            }

            return returnValues;
        } else {
            return null;
        }
    }

    @Override
    public FloatRange promptForRealValue(RealCharacter ch, FloatRange currentValues) {
        FloatRange selectedValue = null;

        if (_context.getImageDisplayMode() == ImageDisplayMode.AUTO && !ch.getImages().isEmpty()) {
            try {
                CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), null, _context.getImageSettings(), true, true, _context.displayScaled());
                dlg.setInitialRealValues(currentValues);
                dlg.displayImagesForCharacter(ch);
                show(dlg);
                if (dlg.okButtonPressed()) {
                    selectedValue = dlg.getInputRealValues();
                } else if (dlg.cancelButtonPressed()) {
                    return null;
                }
            } catch (IllegalArgumentException ex) {
                // Display error message if unable to display
                displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayImage.error", ex.getMessage()));
                return null;
            }
        }

        if (selectedValue == null) {
            RealInputDialog dlg = new RealInputDialog(getMainFrame(), ch, currentValues, _context.getImageSettings(), _context.displayNumbering(),
                    _context.getImageDisplayMode() != ImageDisplayMode.OFF, _context.displayScaled(), _advancedMode);
            UIUtils.showDialog(dlg);
            if (dlg.okPressed()) {
                selectedValue = dlg.getInputData();
            }
        }

        return selectedValue;
    }

    @Override
    public Set<Integer> promptForMultiStateValue(MultiStateCharacter ch, Set<Integer> currentSelectedStates, Character dependentCharacter) {
        Set<Integer> selectedStates = null;

        if (_context.getImageDisplayMode() == ImageDisplayMode.AUTO && !ch.getImages().isEmpty()) {
            try {
                CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), dependentCharacter, _context.getImageSettings(), true, true,
                        _context.displayScaled());
                dlg.setInitialSelectedStates(currentSelectedStates);
                dlg.displayImagesForCharacter(ch);
                show(dlg);
                if (dlg.okButtonPressed()) {
                    selectedStates = dlg.getSelectedStates();
                } else if (dlg.cancelButtonPressed()) {
                    return null;
                }
            } catch (IllegalArgumentException ex) {
                // Display error message if unable to display
                displayErrorMessage(UIUtils.getResourceString("CouldNotDisplayImage.error", ex.getMessage()));
                return null;
            }
        }

        if (selectedStates == null) {
            MultiStateInputDialog dlg = new MultiStateInputDialog(getMainFrame(), ch, currentSelectedStates, dependentCharacter, _context.getImageSettings(), _context.displayNumbering(),
                    _context.getImageDisplayMode() != ImageDisplayMode.OFF, _context.displayScaled(), _advancedMode);
            UIUtils.showDialog(dlg);
            if (dlg.okPressed()) {
                return dlg.getInputData();
            } else {
                return null;
            }
        }

        return selectedStates;
    }

    @Override
    public File promptForFile(List<String> fileExtensions, String description, boolean createFileIfNonExistant) throws IOException {
        return UIUtils.promptForFile(fileExtensions, description, createFileIfNonExistant, _lastOpenedDatasetDirectory, getMainFrame());
    }

    @Override
    public Boolean promptForOnOffValue(String directiveName, boolean initialValue) {
        OnOffPromptDialog dlg = new OnOffPromptDialog(getMainFrame(), directiveName.toUpperCase(), initialValue);
        show(dlg);
        if (dlg.isOkButtonPressed()) {
            return dlg.getSelectedValue();
        } else {
            return null;
        }
    }

    @Override
    public List<Object> promptForMatchSettings() {
        List<Object> retList = new ArrayList<Object>();

        SetMatchPromptDialog dlg = new SetMatchPromptDialog(getMainFrame(), true, _context.getMatchInapplicables(), _context.getMatchUnknowns(), _context.getMatchType());
        show(dlg);
        if (dlg.wasOkButtonPressed()) {
            boolean matchUnknowns = dlg.getMatchUnknowns();
            boolean matchInapplicables = dlg.getMatchInapplicables();
            MatchType matchType = dlg.getMatchType();
            retList.add(matchUnknowns);
            retList.add(matchInapplicables);
            retList.add(matchType);
        } else {
            return null;
        }

        return retList;
    }

    @Override
    public List<Object> promptForButtonDefinition() {
        List<Object> returnValues = new ArrayList<Object>();

        DefineButtonDialog dlg = new DefineButtonDialog(getMainFrame(), true);
        show(dlg);

        if (dlg.wasOkButtonPressed()) {
            returnValues.add(dlg.isInsertSpace());
            returnValues.add(dlg.isRemoveAllButtons());
            returnValues.add(dlg.getImageFilePath());
            returnValues.add(dlg.getCommands());
            returnValues.add(dlg.getBriefHelp());
            returnValues.add(dlg.getDetailedHelp());
            returnValues.add(dlg.enableIfUsedCharactersOnly());
            returnValues.add(dlg.enableInNormalModeOnly());
            returnValues.add(dlg.enableInAdvancedModeOnly());
            return returnValues;
        } else {
            // cancelled
            return null;
        }
    }

    @Override
    public Pair<ImageDisplayMode, DisplayImagesReportType> promptForImageDisplaySettings() {
        DisplayImagesDialog dlg = new DisplayImagesDialog(getMainFrame(), _context.getImageDisplayMode());
        show(dlg);

        if (dlg.wasOkButtonPressed()) {
            return new Pair<ImageDisplayMode, DisplayImagesReportType>(dlg.getSelectedImageDisplayMode(), dlg.getSelectedReportType());
        } else {
            return null;
        }
    }

    @Override
    public String promptForDataset() {
        OpenDataSetDialog dlg = new OpenDataSetDialog(getMainFrame(), UIUtils.readDatasetIndex(), _lastOpenedDatasetDirectory);
        show(dlg);
        return dlg.getSelectedDatasetPath();
    }

    // ======== Methods for "find in characters" and "find in taxa" functions
    // ====================

    // Returns number of taxa matched
    public int findTaxa(String searchText, boolean searchSynonyms, boolean searchEliminatedTaxa) {

        IntkeyDataset dataset = _context.getDataset();

        List<Item> availableTaxa = _context.getAvailableTaxa();
        List<Item> eliminatedTaxa = _context.getEliminatedTaxa();
        _foundAvailableTaxa = new ArrayList<Item>();
        _foundEliminatedTaxa = new ArrayList<Item>();

        Map<Item, List<TextAttribute>> taxaSynonymyAttributes = dataset.getSynonymyAttributesForTaxa();

        for (Item taxon : availableTaxa) {
            if (SearchUtils.taxonMatches(searchText, taxon, SearchUtils.getSynonymyStringsForTaxon(taxon, taxaSynonymyAttributes))) {
                _foundAvailableTaxa.add(taxon);
            }
        }

        for (Item taxon : eliminatedTaxa) {
            if (SearchUtils.taxonMatches(searchText, taxon, SearchUtils.getSynonymyStringsForTaxon(taxon, taxaSynonymyAttributes))) {
                _foundEliminatedTaxa.add(taxon);
            }
        }

        // found available taxa must be sorted by difference count if the
        // tolerance has been
        // set to greater than zero - this mirrors the ordering in which the
        // available taxa are
        // displayed in this situation
        if (_context.getTolerance() > 0) {
            Collections.sort(_foundAvailableTaxa, new DifferenceCountComparator(_context.getSpecimen().getTaxonDifferences()));
        }

        // eliminated taxa must always be sorted by difference count. This
        // mirrors the order in which the
        // eliminated taxa are displayed.
        Collections.sort(_foundEliminatedTaxa, new DifferenceCountComparator(_context.getSpecimen().getTaxonDifferences()));

        _availableTaxaCellRenderer.setTaxaToColor(new HashSet<Item>(_foundAvailableTaxa));
        _eliminatedTaxaCellRenderer.setTaxaToColor(new HashSet<Item>(_foundEliminatedTaxa));

        _listRemainingTaxa.repaint();
        _listEliminatedTaxa.repaint();

        return _foundAvailableTaxa.size() + _foundEliminatedTaxa.size();
    }

    public void selectCurrentMatchedTaxon(int matchedTaxonIndex) {

        if (matchedTaxonIndex < _foundAvailableTaxa.size()) {
            Item taxon = _foundAvailableTaxa.get(matchedTaxonIndex);
            _listRemainingTaxa.setSelectedValue(taxon, true);
            _listEliminatedTaxa.clearSelection();
        } else if (!_foundEliminatedTaxa.isEmpty()) {
            int offsetIndex = matchedTaxonIndex - _foundAvailableTaxa.size();
            if (offsetIndex < _foundEliminatedTaxa.size()) {
                Item taxon = _foundEliminatedTaxa.get(offsetIndex);
                _listEliminatedTaxa.setSelectedValue(taxon, true);
                _listRemainingTaxa.clearSelection();
            }
        }
    }

    public void selectAllMatchedTaxa() {

        int[] availableTaxaSelectedIndices = new int[_foundAvailableTaxa.size()];
        for (int i = 0; i < _foundAvailableTaxa.size(); i++) {
            Item taxon = _foundAvailableTaxa.get(i);
            availableTaxaSelectedIndices[i] = _availableTaxaListModel.indexOf(taxon);
        }

        int[] eliminatedTaxaSelectedIndices = new int[_foundEliminatedTaxa.size()];
        for (int i = 0; i < _foundEliminatedTaxa.size(); i++) {
            Item taxon = _foundEliminatedTaxa.get(i);
            eliminatedTaxaSelectedIndices[i] = _eliminatedTaxaListModel.indexOf(taxon);
        }

        _listRemainingTaxa.setSelectedIndices(availableTaxaSelectedIndices);
        _listEliminatedTaxa.setSelectedIndices(eliminatedTaxaSelectedIndices);
    }

    // Returns number of characters matched
    public int findCharacters(String searchText, boolean searchStates, boolean searchUsedCharacters) {
        List<Character> availableCharacters;

        IntkeyCharacterOrder charOrder = _context.getCharacterOrder();
        switch (charOrder) {
        case NATURAL:
            availableCharacters = _context.getAvailableCharacters();
            break;
        case BEST:
            availableCharacters = new ArrayList<Character>(_context.getBestOrSeparateCharacters().keySet());
            break;
        case SEPARATE:
            throw new NotImplementedException();
        default:
            throw new RuntimeException("Unrecognised character order");
        }

        List<Character> usedCharacters = _context.getUsedCharacters();

        _foundAvailableCharacters = new ArrayList<Character>();
        _foundUsedCharacters = new ArrayList<Character>();

        for (Character ch : availableCharacters) {
            if (SearchUtils.characterMatches(ch, searchText, searchStates)) {
                _foundAvailableCharacters.add(ch);
            }
        }

        if (searchUsedCharacters) {
            for (Character ch : usedCharacters) {
                if (SearchUtils.characterMatches(ch, searchText, searchStates)) {
                    _foundUsedCharacters.add(ch);
                }
            }
        }

        _availableCharactersListCellRenderer.setCharactersToColor(new HashSet<Character>(_foundAvailableCharacters));
        _usedCharactersListCellRenderer.setCharactersToColor(new HashSet<Character>(_foundUsedCharacters));

        _listAvailableCharacters.repaint();
        _listUsedCharacters.repaint();

        return _foundAvailableCharacters.size() + _foundUsedCharacters.size();
    }

    public void selectCurrentMatchedCharacter(int matchedCharacterIndex) {

        if (matchedCharacterIndex < _foundAvailableCharacters.size()) {
            Character ch = _foundAvailableCharacters.get(matchedCharacterIndex);
            _listAvailableCharacters.setSelectedValue(ch, true);
            _listUsedCharacters.clearSelection();
        } else if (!_foundUsedCharacters.isEmpty()) {
            int offsetIndex = matchedCharacterIndex - _foundAvailableCharacters.size();
            if (offsetIndex < _foundUsedCharacters.size()) {
                Character ch = _foundUsedCharacters.get(offsetIndex);
                Attribute attr = _context.getSpecimen().getAttributeForCharacter(ch);
                _listUsedCharacters.setSelectedValue(attr, true);
                _listAvailableCharacters.clearSelection();
            }
        }
    }

    private class DifferenceCountComparator implements Comparator<Item> {

        private Map<Item, Set<Character>> _taxaDifferingCharacters;

        public DifferenceCountComparator(Map<Item, Set<Character>> taxaDifferingCharacters) {
            _taxaDifferingCharacters = taxaDifferingCharacters;
        }

        @Override
        public int compare(Item t1, Item t2) {
            int diffT1 = _taxaDifferingCharacters.get(t1).size();
            int diffT2 = _taxaDifferingCharacters.get(t2).size();

            if (diffT1 == diffT2) {
                return t1.compareTo(t2);
            } else {
                return Integer.valueOf(diffT1).compareTo(Integer.valueOf(diffT2));
            }
        }
    }

    private void openPreviouslyOpenedFile(String fileName) {
        executeDirective(new NewDatasetDirective(), "\"" + fileName + "\"");
    }

    /**
     * This method saves information about the currently opened dataset: 1. If
     * the dataset was downloaded from a remote location, the user will be given
     * the option to save it to disk 2. The dataset is added to the list of most
     * recently used datasets 3. If the dataset is not currently saved in the
     * dataset index, the user will be given the option to do this.
     */
    private void saveCurrentlyOpenedDataset() {
        String datasetTitle = _context.getDataset().getHeading().trim();

        // if the dataset was downloaded, ask the user if they wish to save it
        StartupFileData startupFileData = _context.getStartupFileData();

        boolean remoteDatasetSavedLocally = false;

        // Always use the path to the startup file supplied to the NEWDATASET
        // directive
        // Ignore the "inkFile" URL listed in the startup file.
        String datasetPath;
        URL startupFileURL = _context.getDatasetStartupURL();
        if (startupFileURL.getProtocol().equalsIgnoreCase("file")) {
            try {
                datasetPath = new File(startupFileURL.toURI()).getAbsolutePath();
            } catch (URISyntaxException ex) {
                datasetPath = startupFileURL.toString();
            }
        } else {
            datasetPath = startupFileURL.toString();
        }

        if (startupFileData != null && startupFileData.isRemoteDataset()) {
            int chosenOption = JOptionPane.showConfirmDialog(getMainFrame(), UIUtils.getResourceString("SaveDownloadedDatasetPrompt.caption", datasetTitle), UIUtils.getResourceString("Save.caption"),
                    JOptionPane.YES_NO_OPTION);
            if (chosenOption == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnVal = fileChooser.showOpenDialog(getMainFrame());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File saveDir = fileChooser.getSelectedFile();

                    try {
                        File newInkFile = StartupUtils.saveRemoteDataset(_context, saveDir);
                        datasetPath = newInkFile.getAbsolutePath();
                        remoteDatasetSavedLocally = true;

                        // Remove the current startup file from the MRU as a new
                        // file will go in its
                        // place.
                        UIUtils.removeFileFromMRU(_context.getDatasetStartupFile().getAbsolutePath());
                    } catch (IOException ex) {
                        Logger.error("Error saving downloaded dataset", ex);
                        displayErrorMessage(UIUtils.getResourceString("ErrorSavingDownloadedDataset.error", ex.getMessage()));
                        // not much we can do here, just abort saving/adding to
                        // recents list.
                        return;
                    }
                }
            }
        }

        // Add to list of most recently used datasets.
        if (_context.getDataset() != null) {
            UIUtils.addFileToMRU(datasetPath, datasetTitle, UIUtils.getPreviouslyUsedFiles());
        }

        // If the dataset is not present in the dataset index, give the user the
        // option to add it
        String promptMessage = null;
        if (startupFileData != null && startupFileData.isRemoteDataset()) {
            if (remoteDatasetSavedLocally) {
                promptMessage = UIUtils.getResourceString("AddSavedCopyOfDatasetToIndexPrompt.caption", datasetTitle);
            } else {
                promptMessage = UIUtils.getResourceString("AddStartupFileForRemoteDatasetToIndexPrompt.caption", datasetTitle);
            }
        } else {
            promptMessage = UIUtils.getResourceString("AddDatasetToIndexPrompt.caption", datasetTitle);
        }

        // check if the datasetPath is already present in the index
        if (!UIUtils.getDatasetIndexAsMap().containsKey(datasetPath)) {
            int chosenOption = JOptionPane.showConfirmDialog(getMainFrame(), promptMessage, UIUtils.getResourceString("AddToDataset.caption"), JOptionPane.YES_NO_OPTION);
            if (chosenOption == JOptionPane.YES_OPTION) {
                addToDatasetIndex(datasetTitle, datasetPath);
            }
        }
    }

    private void addToDatasetIndex(String datasetTitle, String datasetPath) {
        EditDatasetIndexDialog dlg = new EditDatasetIndexDialog(getMainFrame(), UIUtils.readDatasetIndex(), datasetTitle, datasetPath);
        show(dlg);
        List<Pair<String, String>> modifiedDatasetIndex = dlg.getModifiedDatasetIndex();
        if (modifiedDatasetIndex != null) {
            UIUtils.writeDatasetIndex(modifiedDatasetIndex);
        }
    }

    public Rectangle getClientBounds() {
        Rectangle r = _rootSplitPane.getBounds();
        // Rectangle outer = getMainFrame().getBounds();
        // r.x = r.x + outer.x;
        // r.y = r.y + _pnlAvailableCharactersHeader.getHeight();
        Point p1 = new Point(0, 0);
        SwingUtilities.convertPointToScreen(p1, _rootSplitPane);
        r.x = p1.x;
        r.y = p1.y + _pnlAvailableCharactersHeader.getHeight();
        r.height = r.height - _pnlAvailableCharactersHeader.getHeight();

        return r;
    }

    private void setLookAndFeel() {
        // To avoid setting the look and feel twice, we are updating the
        // resource bundle before the Swing
        // Application Framework sets the look and feel.
        try {
            getContext().setApplicationClass(Intkey.class);

            ResourceMap resources = getContext().getResourceMap();
            Method method = ResourceMap.class.getDeclaredMethod("getBundlesMap");
            method.setAccessible(true);
            Map<String, Object> resourceMap = (Map<String, Object>) method.invoke(resources);
            resourceMap.put("Application.lookAndFeel", UIUtils.getPreferredLookAndFeel());

        } catch (Throwable t) {
            // Doesn't matter if we fail, going with the defaults is fine.
        }
    }

    @Action
    public void systemLookAndFeel() {
        au.org.ala.delta.ui.util.UIUtils.systemLookAndFeel(getMainFrame());
        UIUtils.setPreferredLookAndFeel(UIUtils.SYSTEM_LOOK_AND_FEEL);
    }

    @Action
    public void metalLookAndFeel() {
        au.org.ala.delta.ui.util.UIUtils.metalLookAndFeel(getMainFrame());
        UIUtils.setPreferredLookAndFeel(UIUtils.METAL_LOOK_AND_FEEL);
    }

    @Action
    public void nimbusLookAndFeel() {
        au.org.ala.delta.ui.util.UIUtils.nimbusLookAndFeel(getMainFrame());
        UIUtils.setPreferredLookAndFeel(UIUtils.NIMBUS_LOOK_AND_FEEL);
    }

    @Action
    public void mnuItSetMainWindowSize() {
        JFrame mainFrame = getMainFrame();
        int currentWidth = mainFrame.getWidth();
        int currentHeight = mainFrame.getHeight();
        SetMainWindowSizeDialog dlg = new SetMainWindowSizeDialog(mainFrame, currentWidth, currentHeight);
        show(dlg);
        try {
            Pair<Integer, Integer> newWidthHeight = dlg.getWidthAndHeight();
            int newWidth = newWidthHeight.getFirst();
            int newHeight = newWidthHeight.getSecond();
            mainFrame.setSize(newWidth, newHeight);
        } catch (NumberFormatException ex) {
            displayErrorMessage(UIUtils.getResourceString("InvalidWidthOrHeight.error"));
        }
    }

    @Action
    public void chooseFont() {
        Font f = UIManager.getFont("Label.font");
        Font newFont = JFontChooser.showDialog(getMainFrame(), UIUtils.getResourceString("SelectFontPrompt.caption"), f);
        if (newFont != null) {
            FontUIResource fontResource = new FontUIResource(newFont);
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof javax.swing.plaf.FontUIResource) {
                    UIManager.put(key, fontResource);
                }
            }
            SwingUtilities.updateComponentTreeUI(getMainFrame());
        }
    }
}
