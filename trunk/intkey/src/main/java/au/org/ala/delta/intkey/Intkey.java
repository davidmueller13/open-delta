package au.org.ala.delta.intkey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.FloatRange;
import org.jdesktop.application.Action;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.Logger;
import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.intkey.directives.ChangeDirective;
import au.org.ala.delta.intkey.directives.DifferencesDirective;
import au.org.ala.delta.intkey.directives.DirectivePopulator;
import au.org.ala.delta.intkey.directives.DisplayCharacterOrderBestDirective;
import au.org.ala.delta.intkey.directives.DisplayCharacterOrderNaturalDirective;
import au.org.ala.delta.intkey.directives.DisplayCharacterOrderSeparateDirective;
import au.org.ala.delta.intkey.directives.FileCharactersDirective;
import au.org.ala.delta.intkey.directives.FileTaxaDirective;
import au.org.ala.delta.intkey.directives.IncludeCharactersDirective;
import au.org.ala.delta.intkey.directives.IncludeTaxaDirective;
import au.org.ala.delta.intkey.directives.IntkeyDirectiveParseException;
import au.org.ala.delta.intkey.directives.NewDatasetDirective;
import au.org.ala.delta.intkey.directives.RestartDirective;
import au.org.ala.delta.intkey.directives.SetToleranceDirective;
import au.org.ala.delta.intkey.directives.UseDirective;
import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyCharacterOrder;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.intkey.model.SearchUtils;
import au.org.ala.delta.intkey.model.StartupFileData;
import au.org.ala.delta.intkey.model.StartupUtils;
import au.org.ala.delta.intkey.model.specimen.CharacterValue;
import au.org.ala.delta.intkey.model.specimen.Specimen;
import au.org.ala.delta.intkey.ui.AttributeCellRenderer;
import au.org.ala.delta.intkey.ui.BestCharacterCellRenderer;
import au.org.ala.delta.intkey.ui.BusyGlassPane;
import au.org.ala.delta.intkey.ui.CharacterCellRenderer;
import au.org.ala.delta.intkey.ui.CharacterImageDialog;
import au.org.ala.delta.intkey.ui.CharacterKeywordSelectionDialog;
import au.org.ala.delta.intkey.ui.CharacterSelectionDialog;
import au.org.ala.delta.intkey.ui.ContentsDialog;
import au.org.ala.delta.intkey.ui.FindInCharactersDialog;
import au.org.ala.delta.intkey.ui.FindInTaxaDialog;
import au.org.ala.delta.intkey.ui.ImageDialog;
import au.org.ala.delta.intkey.ui.ImageUtils;
import au.org.ala.delta.intkey.ui.IntegerInputDialog;
import au.org.ala.delta.intkey.ui.MultiStateInputDialog;
import au.org.ala.delta.intkey.ui.OnOffPromptDialog;
import au.org.ala.delta.intkey.ui.OutputFileSelectionDialog;
import au.org.ala.delta.intkey.ui.ReExecuteDialog;
import au.org.ala.delta.intkey.ui.RealInputDialog;
import au.org.ala.delta.intkey.ui.RtfReportDisplayDialog;
import au.org.ala.delta.intkey.ui.TaxonCellRenderer;
import au.org.ala.delta.intkey.ui.TaxonImageDialog;
import au.org.ala.delta.intkey.ui.TaxonInformationDialog;
import au.org.ala.delta.intkey.ui.TaxonKeywordSelectionDialog;
import au.org.ala.delta.intkey.ui.TaxonSelectionDialog;
import au.org.ala.delta.intkey.ui.TaxonWithDifferenceCountCellRenderer;
import au.org.ala.delta.intkey.ui.TextInputDialog;
import au.org.ala.delta.intkey.ui.UIUtils;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
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
import au.org.ala.delta.ui.rtf.SimpleRtfEditorKit;
import au.org.ala.delta.ui.util.IconHelper;
import au.org.ala.delta.util.Pair;

public class Intkey extends DeltaSingleFrameApplication implements IntkeyUI, DirectivePopulator {

    private static String INTKEY_ICON_PATH = "/au/org/ala/delta/intkey/resources/icons";
    private static String MRU_FILES_PREF_KEY = "MRU";
    private static String MRU_FILES_SEPARATOR = "\n";
    private static String MRU_ITEM_SEPARATOR = ";";
    private static int MAX_SIZE_MRU = 4;

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

    private BusyGlassPane _glassPane = null;

    private boolean _advancedMode = false;

    private List<Character> _foundAvailableCharacters = null;
    private List<Character> _foundUsedCharacters = null;
    private List<Item> _foundAvailableTaxa = null;
    private List<Item> _foundEliminatedTaxa = null;

    private List<JButton> _advancedModeOnlyDynamicButtons;
    private List<JButton> _normalModeOnlyDynamicButtons;
    private List<JButton> _activeOnlyWhenCharactersUsedButtons;

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
    String loadingReportCaption;

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

    private String _datasetInitFileToOpen = null;

    private ItemFormatter _taxonformatter;

    /**
     * Calls Desktop.getDesktop on a background thread as it's slow to
     * initialise
     */
    private SwingWorker<Desktop, Void> _desktopWorker;

    public static void main(String[] args) {
        setupMacSystemProperties(Intkey.class);
        launch(Intkey.class, args);
    }

    @Override
    protected void initialize(String[] args) {
        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectFields(this);

        if (args.length > 0) {
            _datasetInitFileToOpen = args[0];
        }
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    protected void startup() {
        JFrame mainFrame = getMainFrame();
        mainFrame.setTitle("Intkey");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setIconImages(IconHelper.getRedIconList());

        _taxonformatter = new ItemFormatter(false, CommentStrippingMode.STRIP_ALL, AngleBracketHandlingMode.REMOVE, true, false, true);
        _context = new IntkeyContext(this, this);

        _advancedModeOnlyDynamicButtons = new ArrayList<JButton>();
        _normalModeOnlyDynamicButtons = new ArrayList<JButton>();
        _activeOnlyWhenCharactersUsedButtons = new ArrayList<JButton>();

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
        _btnContextHelp.setEnabled(false);
        _btnContextHelp.setPreferredSize(new Dimension(30, 30));
        _btnContextHelp.setMargin(new Insets(2, 5, 2, 5));
        _globalOptionBar.add(_btnContextHelp, BorderLayout.EAST);

        _rootSplitPane = new JSplitPane();
        _rootSplitPane.setDividerSize(3);
        _rootSplitPane.setResizeWeight(0.5);
        _rootSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        _rootSplitPane.setContinuousLayout(true);
        _rootPanel.add(_rootSplitPane);

        _innerSplitPaneLeft = new JSplitPane();
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
        _listAvailableCharacters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        _lblNumAvailableCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblNumAvailableCharacters.setText(String.format(availableCharactersCaption, 0));
        _pnlAvailableCharactersHeader.add(_lblNumAvailableCharacters, BorderLayout.WEST);

        _pnlAvailableCharactersButtons = new JPanel();
        FlowLayout flowLayout = (FlowLayout) _pnlAvailableCharactersButtons.getLayout();
        flowLayout.setVgap(2);
        flowLayout.setHgap(2);
        _pnlAvailableCharactersHeader.add(_pnlAvailableCharactersButtons, BorderLayout.EAST);

        _btnRestart = new JButton();
        _btnRestart.setAction(actionMap.get("btnRestart"));
        _btnRestart.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnRestart);

        _btnBestOrder = new JButton();
        _btnBestOrder.setAction(actionMap.get("btnBestOrder"));
        _btnBestOrder.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnBestOrder);

        _btnSeparate = new JButton();
        _btnSeparate.setAction(actionMap.get("btnSeparate"));
        _btnSeparate.setVisible(_advancedMode);
        _btnSeparate.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnSeparate);

        _btnNaturalOrder = new JButton();
        _btnNaturalOrder.setAction(actionMap.get("btnNaturalOrder"));
        _btnNaturalOrder.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnNaturalOrder);

        _btnDiffSpecimenTaxa = new JButton();
        _btnDiffSpecimenTaxa.setAction(actionMap.get("btnDiffSpecimenTaxa"));
        _btnDiffSpecimenTaxa.setEnabled(false);
        _btnDiffSpecimenTaxa.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnDiffSpecimenTaxa);

        _btnSetTolerance = new JButton();
        _btnSetTolerance.setAction(actionMap.get("btnSetTolerance"));
        _btnSetTolerance.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnSetTolerance);

        _btnSetMatch = new JButton();
        _btnSetMatch.setAction(actionMap.get("btnSetMatch"));
        _btnSetMatch.setEnabled(false);
        _btnSetMatch.setVisible(_advancedMode);
        _btnSetMatch.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnSetMatch);

        _btnSubsetCharacters = new JButton();
        _btnSubsetCharacters.setAction(actionMap.get("btnSubsetCharacters"));
        _btnSubsetCharacters.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnSubsetCharacters);

        _btnFindCharacter = new JButton();
        _btnFindCharacter.setAction(actionMap.get("btnFindCharacter"));
        _btnFindCharacter.setPreferredSize(new Dimension(30, 30));
        _pnlAvailableCharactersButtons.add(_btnFindCharacter);

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
                            CharacterValue chVal = (CharacterValue) _usedCharacterListModel.getElementAt(selectedIndex);

                            if (_context.charactersFixed() && _context.getFixedCharactersList().contains(chVal.getCharacter().getCharacterId())) {
                                return;
                            }

                            executeDirective(new ChangeDirective(), Integer.toString(chVal.getCharacter().getCharacterId()));
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
        _lblNumUsedCharacters.setBorder(new EmptyBorder(7, 0, 7, 0));
        _lblNumUsedCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblNumUsedCharacters.setText(String.format(usedCharactersCaption, 0));
        _pnlUsedCharactersHeader.add(_lblNumUsedCharacters, BorderLayout.WEST);

        _innerSplitPaneRight = new JSplitPane();
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
        _listRemainingTaxa.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                taxonSelectionChanged();
            }
        });

        _sclPnRemainingTaxa.setViewportView(_listRemainingTaxa);

        _pnlRemainingTaxaHeader = new JPanel();
        _pnlRemainingTaxa.add(_pnlRemainingTaxaHeader, BorderLayout.NORTH);
        _pnlRemainingTaxaHeader.setLayout(new BorderLayout(0, 0));

        _lblNumRemainingTaxa = new JLabel();
        _lblNumRemainingTaxa.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblNumRemainingTaxa.setText(String.format(remainingTaxaCaption, 0));
        _pnlRemainingTaxaHeader.add(_lblNumRemainingTaxa, BorderLayout.WEST);

        _pnlRemainingTaxaButtons = new JPanel();
        FlowLayout fl_pnlRemainingTaxaButtons = (FlowLayout) _pnlRemainingTaxaButtons.getLayout();
        fl_pnlRemainingTaxaButtons.setVgap(2);
        fl_pnlRemainingTaxaButtons.setHgap(2);
        _pnlRemainingTaxaHeader.add(_pnlRemainingTaxaButtons, BorderLayout.EAST);

        _btnTaxonInfo = new JButton();
        _btnTaxonInfo.setAction(actionMap.get("btnTaxonInfo"));
        _btnTaxonInfo.setPreferredSize(new Dimension(30, 30));
        _pnlRemainingTaxaButtons.add(_btnTaxonInfo);

        _btnDiffTaxa = new JButton();
        _btnDiffTaxa.setAction(actionMap.get("btnDiffTaxa"));
        _btnDiffTaxa.setPreferredSize(new Dimension(30, 30));
        _btnDiffTaxa.setEnabled(false);
        _pnlRemainingTaxaButtons.add(_btnDiffTaxa);

        _btnSubsetTaxa = new JButton();
        _btnSubsetTaxa.setAction(actionMap.get("btnSubsetTaxa"));
        _btnSubsetTaxa.setPreferredSize(new Dimension(30, 30));
        _pnlRemainingTaxaButtons.add(_btnSubsetTaxa);

        _btnFindTaxon = new JButton();
        _btnFindTaxon.setAction(actionMap.get("btnFindTaxon"));
        _btnFindTaxon.setPreferredSize(new Dimension(30, 30));
        _pnlRemainingTaxaButtons.add(_btnFindTaxon);

        _pnlEliminatedTaxa = new JPanel();
        _innerSplitPaneRight.setRightComponent(_pnlEliminatedTaxa);
        _pnlEliminatedTaxa.setLayout(new BorderLayout(0, 0));

        _sclPnEliminatedTaxa = new JScrollPane();
        _pnlEliminatedTaxa.add(_sclPnEliminatedTaxa, BorderLayout.CENTER);

        _listEliminatedTaxa = new JList();
        _listEliminatedTaxa.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                taxonSelectionChanged();
            }
        });

        _sclPnEliminatedTaxa.setViewportView(_listEliminatedTaxa);

        _pnlEliminatedTaxaHeader = new JPanel();
        _pnlEliminatedTaxa.add(_pnlEliminatedTaxaHeader, BorderLayout.NORTH);
        _pnlEliminatedTaxaHeader.setLayout(new BorderLayout(0, 0));

        _lblEliminatedTaxa = new JLabel();
        _lblEliminatedTaxa.setBorder(new EmptyBorder(7, 0, 7, 0));
        _lblEliminatedTaxa.setFont(new Font("Tahoma", Font.PLAIN, 15));
        _lblEliminatedTaxa.setText(String.format(eliminatedTaxaCaption, 0));
        _pnlEliminatedTaxaHeader.add(_lblEliminatedTaxa, BorderLayout.WEST);

        JMenuBar menuBar = buildMenus(_advancedMode);
        getMainView().setMenuBar(menuBar);

        _txtFldCmdBar = new JTextField();
        _txtFldCmdBar.setCaretColor(Color.WHITE);
        _txtFldCmdBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String cmdStr = _txtFldCmdBar.getText();

                cmdStr = cmdStr.toLowerCase().trim();
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
        _txtFldCmdBar.setVisible(_advancedMode);
        _rootPanel.add(_txtFldCmdBar, BorderLayout.SOUTH);
        _txtFldCmdBar.setColumns(10);

        _logDialog = new RtfReportDisplayDialog(getMainFrame(), new SimpleRtfEditorKit(null), null, logDialogTitle);

        show(_rootPanel);
    }

    @Override
    protected void ready() {
        super.ready();
        _rootSplitPane.setDividerLocation(2.0 / 3.0);
        _innerSplitPaneLeft.setDividerLocation(2.0 / 3.0);
        _innerSplitPaneRight.setDividerLocation(2.0 / 3.0);

        if (_datasetInitFileToOpen != null) {
            executeDirective(new NewDatasetDirective(), _datasetInitFileToOpen);
        }

        loadDesktopInBackground();
    }

    @Override
    protected void shutdown() {
        saveCurrentlyOpenedDataset();
        _context.cleanupForShutdown();
        super.shutdown();
    }

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

    private JMenu buildFileMenu(boolean advancedMode, ActionMap actionMap) {
        JMenu mnuFile = new JMenu();
        mnuFile.setName("mnuFile");

        JMenuItem mnuItNewDataSet = new JMenuItem();
        mnuItNewDataSet.setAction(actionMap.get("mnuItNewDataSet"));
        mnuFile.add(mnuItNewDataSet);

        JMenu mnuFileRecents = buildRecentFilesMenu();
        mnuFile.add(mnuFileRecents);

        if (_advancedMode) {
            JMenuItem mnuItPreferences = new JMenuItem();
            mnuItPreferences.setAction(actionMap.get("mnuItPreferences"));
            mnuItPreferences.setEnabled(false);
            mnuFile.add(mnuItPreferences);

            JMenuItem mnuItContents = new JMenuItem();
            mnuItContents.setAction(actionMap.get("mnuItContents"));
            mnuItContents.setEnabled(false);
            mnuFile.add(mnuItContents);

            mnuFile.addSeparator();

            JMenu mnuFileCmds = new JMenu();
            mnuFileCmds.setName("mnuFileCmds");

            JMenuItem mnuItFileCharactersCmd = new JMenuItem();
            mnuItFileCharactersCmd.setAction(actionMap.get("mnuItFileCharacters"));
            mnuFileCmds.add(mnuItFileCharactersCmd);

            JMenuItem mnuItFileTaxaCmd = new JMenuItem();
            mnuItFileTaxaCmd.setAction(actionMap.get("mnuItFileTaxa"));
            mnuFileCmds.add(mnuItFileTaxaCmd);

            mnuFile.add(mnuFileCmds);

            JMenu mnuOutputCmds = new JMenu();
            mnuOutputCmds.setName("mnuOutputCmds");
            mnuOutputCmds.setEnabled(false);
            mnuFile.add(mnuOutputCmds);

            mnuFile.addSeparator();

            JMenuItem mnuItComment = new JMenuItem();
            mnuItComment.setAction(actionMap.get("mnuItComment"));
            mnuItComment.setEnabled(false);
            mnuFile.add(mnuItComment);

            JMenuItem mnuItShow = new JMenuItem();
            mnuItShow.setAction(actionMap.get("mnuItShow"));
            mnuItShow.setEnabled(false);
            mnuFile.add(mnuItShow);

            mnuFile.addSeparator();

            JMenuItem mnuItNormalMode = new JMenuItem();
            mnuItNormalMode.setAction(actionMap.get("mnuItNormalMode"));
            mnuFile.add(mnuItNormalMode);

            mnuFile.addSeparator();

            JMenuItem mnuItEditIndex = new JMenuItem("Edit Index...");
            mnuItEditIndex.setAction(actionMap.get("mnuItEditIndex"));
            mnuItEditIndex.setEnabled(false);
            mnuFile.add(mnuItEditIndex);

            _cmdMenus.put("file", mnuFileCmds);
        } else {
            mnuFile.addSeparator();

            JMenuItem mnuItAdvancedMode = new JMenuItem();
            mnuItAdvancedMode.setAction(actionMap.get("mnuItAdvancedMode"));
            mnuFile.add(mnuItAdvancedMode);
        }

        mnuFile.addSeparator();

        JMenuItem mnuItFileExit = new JMenuItem();
        mnuItFileExit.setAction(actionMap.get("mnuItExitApplication"));
        mnuFile.add(mnuItFileExit);

        return mnuFile;
    }

    private JMenu buildRecentFilesMenu() {
        JMenu mnuFileRecents = new JMenu();
        mnuFileRecents.setName("mnuFileRecents");

        List<Pair<String, String>> recentFiles = getPreviouslyUsedFiles();

        for (Pair<String, String> recentFile : recentFiles) {
            final String filePath = recentFile.getFirst();
            String title = recentFile.getSecond();

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

    private JMenu buildQueriesMenu(ActionMap actionMap) {
        JMenu mnuQueries = new JMenu();
        mnuQueries.setName("mnuQueries");
        mnuQueries.setEnabled(false);
        return mnuQueries;
    }

    private JMenu buildBrowsingMenu(ActionMap actionMap) {
        JMenu mnuBrowsing = new JMenu();
        mnuBrowsing.setName("mnuBrowsing");
        mnuBrowsing.setEnabled(false);
        return mnuBrowsing;
    }

    private JMenu buildSettingsMenu(ActionMap actionMap) {
        JMenu mnuSettings = new JMenu();
        mnuSettings.setName("mnuSettings");
        mnuSettings.setEnabled(false);
        return mnuSettings;
    }

    private JMenu buildReExecuteMenu(ActionMap actionMap) {
        JMenu mnuReExecute = new JMenu("ReExecute...");
        mnuReExecute.setName("mnuReExecute");

        JMenuItem mnuItReExecute = new JMenuItem();
        mnuItReExecute.setAction(actionMap.get("mnuItReExecute"));
        mnuReExecute.add(mnuItReExecute);

        return mnuReExecute;
    }

    private JMenu buildWindowMenu(ActionMap actionMap) {
        JMenu mnuWindow = new JMenu();
        mnuWindow.setName("mnuWindow");

        JMenuItem mnuItCascade = new JMenuItem();
        mnuItCascade.setAction(actionMap.get("mnuItCascadeWindows"));
        mnuItCascade.setEnabled(false);
        mnuWindow.add(mnuItCascade);

        JMenuItem mnuItTile = new JMenuItem();
        mnuItTile.setAction(actionMap.get("mnuItTileWindows"));
        mnuItTile.setEnabled(false);
        mnuWindow.add(mnuItTile);

        mnuWindow.addSeparator();

        JMenuItem mnuItCloseAll = new JMenuItem();
        mnuItCloseAll.setAction(actionMap.get("mnuItCloseAllWindows"));
        mnuItCloseAll.setEnabled(false);
        mnuWindow.add(mnuItCloseAll);

        return mnuWindow;
    }

    private JMenu buildHelpMenu(boolean advancedMode, ActionMap actionMap) {
        JMenu mnuHelp = new JMenu();
        mnuHelp.setName("mnuHelp");
        JMenuItem mnuItHelpIntroduction = new JMenuItem();
        mnuItHelpIntroduction.setAction(actionMap.get("mnuItHelpIntroduction"));
        mnuItHelpIntroduction.setEnabled(false);
        mnuHelp.add(mnuItHelpIntroduction);

        if (advancedMode) {
            JMenuItem mnuItHelpCommands = new JMenuItem();
            mnuItHelpCommands.setAction(actionMap.get("mnuItHelpCommands"));
            mnuItHelpCommands.setEnabled(false);
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
    @Action
    public void mnuItNewDataSet() {
        executeDirective(new NewDatasetDirective(), null);
    }

    @Action
    public void mnuItPreferences() {

    }

    @Action
    public void mnuItContents() {

    }

    @Action
    public void mnuItFileCharacters() {
        executeDirective(new FileCharactersDirective(), null);
    }

    @Action
    public void mnuItFileTaxa() {
        executeDirective(new FileTaxaDirective(), null);
    }

    @Action
    public void mnuItComment() {
    }

    @Action
    public void mnuItShow() {
    }

    @Action
    public void mnuItNormalMode() {
        toggleAdvancedMode();
    }

    @Action
    public void mnuItAdvancedMode() {
        toggleAdvancedMode();
    }

    private void toggleAdvancedMode() {
        _advancedMode = !_advancedMode;

        if (_advancedMode) {
            JMenuBar menuBar = buildMenus(true);
            getMainFrame().setJMenuBar(menuBar);
            _btnSeparate.setVisible(true);
            _btnSetMatch.setVisible(true);
            _txtFldCmdBar.setVisible(true);
        } else {
            JMenuBar menuBar = buildMenus(false);
            getMainFrame().setJMenuBar(menuBar);
            _btnSeparate.setVisible(false);
            _btnSetMatch.setVisible(false);
            _txtFldCmdBar.setVisible(false);
        }

        // Need to update available characters because character separating
        // powers
        // are only shown in best ordering when in advanced mode.
        updateAvailableCharacters();

        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectComponents(getMainFrame());
        _rootPanel.revalidate();
    }

    @Action
    public void mnuItEditIndex() {
    }

    @Action
    public void mnuItExitApplication() {
        exit();
    }

    // ============================ ReExecute menu actions
    // ===========================

    @Action
    public void mnuItReExecute() {
        ReExecuteDialog dlg = new ReExecuteDialog(getMainFrame(), _context.getExecutedDirectives());
        dlg.setVisible(true);
        IntkeyDirectiveInvocation directive = dlg.getDirectiveToExecute();
        if (directive != null) {
            _context.executeDirective(directive);
        }
    }

    // ============================= Window menu actions
    // ==============================
    @Action
    public void mnuItCascadeWindows() {
    }

    @Action
    public void mnuItTileWindows() {
    }

    @Action
    public void mnuItCloseAllWindows() {
    }

    // ====================== Help menu actions
    // ====================================
    @Action
    public void mnuItHelpIntroduction() {
    }

    @Action
    public void mnuItHelpCommands() {
    }

    @Action
    public void mnuItHelpAbout() {
        AboutBox aboutBox = new AboutBox(getMainFrame(), IconHelper.createRed32ImageIcon());
        show(aboutBox);
    }

    // ============================== Global option buttons
    // ================================

    @Action
    public void btnContextHelp() {
    }

    // ========================= Character toolbar button actions
    // ===================

    @Action
    public void btnRestart() {
        executeDirective(new RestartDirective(), null);
    }

    @Action
    public void btnBestOrder() {
        executeDirective(new DisplayCharacterOrderBestDirective(), null);
    }

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

    @Action
    public void btnNaturalOrder() {
        executeDirective(new DisplayCharacterOrderNaturalDirective(), null);
    }

    @Action
    public void btnDiffSpecimenTaxa() {
        executeDirective(new DifferencesDirective(), "/E (specimen remaining) all");
    }

    @Action
    public void btnSetTolerance() {
        executeDirective(new SetToleranceDirective(), null);
    }

    @Action
    public void btnSetMatch() {
    }

    @Action
    public void btnSubsetCharacters() {
        executeDirective(new IncludeCharactersDirective(), null);
    }

    @Action
    public void btnFindCharacter() {
        new FindInCharactersDialog(this, _context).setVisible(true);
    }

    // ============================= Taxon toolbar button actions
    // ===========================

    @Action
    public void btnTaxonInfo() {
        List<Item> selectedTaxa = new ArrayList<Item>();

        for (int i : _listRemainingTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _availableTaxaListModel.getElementAt(i));
        }

        for (int i : _listEliminatedTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _eliminatedTaxaListModel.getElementAt(i));
        }

        if (!selectedTaxa.isEmpty()) {
            TaxonInformationDialog dlg = new TaxonInformationDialog(getMainFrame(), selectedTaxa, _context);
            show(dlg);
        }
    }

    @Action
    public void btnDiffTaxa() {
        List<Item> selectedTaxa = new ArrayList<Item>();

        for (int i : _listRemainingTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _availableTaxaListModel.getElementAt(i));
        }

        for (int i : _listEliminatedTaxa.getSelectedIndices()) {
            selectedTaxa.add((Item) _eliminatedTaxaListModel.getElementAt(i));
        }

        StringBuilder directiveTextBuilder = new StringBuilder();
        directiveTextBuilder.append("/E /I /U /X (");
        for (Item taxon : selectedTaxa) {
            directiveTextBuilder.append(" ");
            directiveTextBuilder.append(taxon.getItemNumber());
        }
        directiveTextBuilder.append(") all");

        executeDirective(new DifferencesDirective(), directiveTextBuilder.toString());
    }

    @Action
    public void btnSubsetTaxa() {
        executeDirective(new IncludeTaxaDirective(), null);
    }

    @Action
    public void btnFindTaxon() {
        new FindInTaxaDialog(this).setVisible(true);
    }

    // =========================================================================================

    /**
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

    private void executeDirective(AbstractDirective<IntkeyContext> dir, String data) {
        try {
            dir.parseAndProcess(_context, data);
        } catch (IntkeyDirectiveParseException ex) {
            ex.printStackTrace();
            String msg = ex.getMessage();
            JOptionPane.showMessageDialog(UIUtils.getMainFrame(), msg, "Error", JOptionPane.ERROR_MESSAGE);
            Logger.error(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = String.format("Error occurred while processing '%s' command: %s", StringUtils.join(dir.getControlWords(), " ").toUpperCase(), ex.getMessage());
            JOptionPane.showMessageDialog(UIUtils.getMainFrame(), msg, "Error", JOptionPane.ERROR_MESSAGE);
            Logger.error(msg);
            Logger.error(ex);
        }
    }

    private void taxonSelectionChanged() {
        int[] remainingTaxaSelectedIndicies = _listRemainingTaxa.getSelectedIndices();
        int[] eliminatedTaxaSelectedIndicies = _listEliminatedTaxa.getSelectedIndices();

        _btnDiffTaxa.setEnabled((remainingTaxaSelectedIndicies.length + eliminatedTaxaSelectedIndicies.length) >= 2);
    }

    private void initializeIdentification() {
        handleUpdateAll();
    }

    private void updateAvailableCharacters() {

        IntkeyCharacterOrder charOrder = _context.getCharacterOrder();

        Item taxonToSeparate = null;
        String formattedTaxonToSeparateName = null;

        switch (charOrder) {
        case SEPARATE:
            taxonToSeparate = _context.getDataset().getTaxon(_context.getTaxonToSeparate());
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
                    _lblNumAvailableCharacters.setText(String.format(bestCharactersCaption, bestCharactersMap.keySet().size()));
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
            List<Character> availableCharacters = new ArrayList<Character>(_context.getAvailableCharacters());
            _lblNumAvailableCharacters.setText(String.format(availableCharactersCaption, availableCharacters.size()));
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

    private void handleNoAvailableCharacters() {
        String message = null;

        if (_context.getIncludedCharacters().size() < _context.getDataset().getNumberOfCharacters()) { // characters
                                                                                                       // excluded?
            message = charactersExcludedCannotSeparateCaption;
        } else {
            if (_context.getTolerance() > 0) {
                message = mismatchesAllowCannotSeparateCaption;
            } else {
                message = availableCharactersCannotSeparateCaption;
            }
        }

        JLabel lbl = new JLabel(message);
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setBackground(Color.WHITE);
        lbl.setOpaque(true);
        _sclPaneAvailableCharacters.setViewportView(lbl);
        _sclPaneAvailableCharacters.revalidate();
    }

    /**
     * Used to calculate the best characters in a separate thread, then update
     * the UI accordingly when the operation is finished
     * 
     * @author ChrisF
     * 
     */
    /**
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
        }
    }

    private void updateUsedCharacters() {

        Specimen specimen = _context.getSpecimen();
        List<Character> usedCharacters = specimen.getUsedCharacters();

        List<CharacterValue> usedCharacterValues = new ArrayList<CharacterValue>();
        for (Character ch : usedCharacters) {
            usedCharacterValues.add(specimen.getValueForCharacter(ch));
        }

        _usedCharacterListModel = new DefaultListModel();
        for (CharacterValue chVal : usedCharacterValues) {
            _usedCharacterListModel.addElement(chVal);
        }
        _usedCharactersListCellRenderer = new AttributeCellRenderer(_context.displayNumbering(), _context.getDataset().getOrWord());
        _listUsedCharacters.setCellRenderer(_usedCharactersListCellRenderer);
        _listUsedCharacters.setModel(_usedCharacterListModel);

        _lblNumUsedCharacters.setText(String.format(usedCharactersCaption, _usedCharacterListModel.getSize()));
    }

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

        _lblNumRemainingTaxa.setText(String.format(remainingTaxaCaption, _availableTaxaListModel.getSize()));

        _listRemainingTaxa.repaint();
    }

    private void updateUsedTaxa(List<Item> eliminatedTaxa, Map<Item, Set<Character>> taxaDifferingCharacters) {
        // sort eliminated taxa by difference count
        Collections.sort(eliminatedTaxa, new DifferenceCountComparator(taxaDifferingCharacters));

        _eliminatedTaxaListModel = new DefaultListModel();

        for (Item taxon : eliminatedTaxa) {
            _eliminatedTaxaListModel.addElement(taxon);
        }

        _eliminatedTaxaCellRenderer = new TaxonWithDifferenceCountCellRenderer(taxaDifferingCharacters, _context.displayNumbering(), _context.displayComments());

        _listEliminatedTaxa.setCellRenderer(_eliminatedTaxaCellRenderer);
        _listEliminatedTaxa.setModel(_eliminatedTaxaListModel);

        _lblEliminatedTaxa.setText(String.format(eliminatedTaxaCaption, _eliminatedTaxaListModel.getSize()));

        _listEliminatedTaxa.repaint();
    }

    // ================================== IntkeyUI methods
    // ===========================================================

    @Override
    public void handleNewDataset(IntkeyDataset dataset) {
        getMainFrame().setTitle(String.format(windowTitleWithDatasetTitle, dataset.getHeading()));

        // display startup images
        List<Image> startupImages = dataset.getStartupImages();
        if (!startupImages.isEmpty()) {
            ImageUtils.displayStartupScreen(startupImages, _context.getImageSettings(), getMainFrame());
        }

        initializeIdentification();

        _rootPanel.revalidate();
    }

    @Override
    public void handleDatasetClosed() {
        saveCurrentlyOpenedDataset();
        JMenuBar menuBar = buildMenus(_advancedMode); // need to refresh the
                                                      // recent datasets menu
        getMainFrame().setJMenuBar(menuBar);
        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectComponents(getMainFrame());
    }

    @Override
    public void handleUpdateAll() {
        if (_context.getDataset() != null) { // Only update if we have a dataset
                                             // loaded.
            List<Item> availableTaxa = _context.getAvailableTaxa();
            List<Item> eliminatedTaxa = _context.getEliminatedTaxa();

            _btnDiffSpecimenTaxa.setEnabled(availableTaxa.size() > 0 && eliminatedTaxa.size() > 0);

            // Need to display a message in place of the list of available
            // characters
            // if there are no remaining taxa (no matching taxa remain), or only
            // 1
            // remaining taxon (identification complete)
            if (availableTaxa.size() > 1) {
                updateAvailableCharacters();
            } else {
                String message = null;

                if (availableTaxa.size() == 0) {
                    message = noMatchingTaxaRemainCaption;
                } else if (availableTaxa.size() == 1) {
                    message = identificationCompleteCaption;
                }

                JLabel lbl = new JLabel(message);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setBackground(Color.WHITE);
                lbl.setOpaque(true);
                _sclPaneAvailableCharacters.setViewportView(lbl);
                _sclPaneAvailableCharacters.revalidate();

                switch (_context.getCharacterOrder()) {
                case NATURAL:
                    _lblNumAvailableCharacters.setText(String.format(availableCharactersCaption, 0));
                    break;
                case BEST:
                    _lblNumAvailableCharacters.setText(String.format(bestCharactersCaption, 0));
                    break;
                case SEPARATE:
                    Item taxonToSeparate = _context.getDataset().getTaxon(_context.getTaxonToSeparate());
                    String formattedTaxonName = _taxonformatter.formatItemDescription(taxonToSeparate);
                    _lblNumAvailableCharacters.setText(MessageFormat.format(separateCharactersCaption, formattedTaxonName, 0));
                    break;
                default:
                    throw new RuntimeException("Unrecognized character order");
                }
            }

            updateUsedCharacters();
            updateAvailableTaxa(availableTaxa, _context.getSpecimen().getTaxonDifferences());
            updateUsedTaxa(eliminatedTaxa, _context.getSpecimen().getTaxonDifferences());

            updateDynamicButtons();
        }
    }

    @Override
    public void handleIdentificationRestarted() {
        _btnDiffSpecimenTaxa.setEnabled(false);
        handleUpdateAll();
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
                displayBusyMessage(loadingReportCaption);
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
            try {
                _dlg = new RtfReportDisplayDialog(getMainFrame(), new SimpleRtfEditorKit(null), _rtfSource, _title);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            // A runtime exception is thrown by the RtfReportDisplayDialog if
            // the RTF was invalid. This will result in the dialog being null.
            if (_dlg == null) {
                displayErrorMessage(badlyFormedRTFContentMessage);
            } else {
                Intkey.this.show(_dlg);
            }

            removeBusyMessage();
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
        if (_glassPane == null) {
            _glassPane = new BusyGlassPane(message);
            getMainFrame().setGlassPane(_glassPane);
            _glassPane.setVisible(true);
        } else {
            _glassPane.setMessage(message);
        }
    }

    @Override
    public void removeBusyMessage() {
        if (_glassPane != null) {
            _glassPane.setVisible(false);
            _glassPane = null;
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
                displayErrorMessage("Error reading image from file " + iconFile.getAbsolutePath());
            }
        }

        // Is the image file relative to the dataset directory?
        if (icon == null) {
            File relativeIconFile = new File(_context.getDatasetDirectory(), imageFileName);
            if (relativeIconFile.exists() && relativeIconFile.isAbsolute()) {
                try {
                    icon = readImageIconFromFile(relativeIconFile);
                } catch (IOException ex) {
                    displayErrorMessage("Error reading image from file " + iconFile.getAbsolutePath());
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
            displayErrorMessage("Could not find image " + imageFileName);
            return;
        }

        JButton button = new JButton(icon);
        // button.setToolTipText(shortHelp);
        button.setToolTipText(commands.toString());
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
    public void IllustrateCharacters(List<Character> characters) {
        CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), characters, _context.getImageSettings(), true, false);
        show(dlg);
    }

    @Override
    public void IllustrateTaxa(List<Item> taxa) {
        TaxonImageDialog dlg = new TaxonImageDialog(getMainFrame(), _context.getImageSettings(), taxa, false);
        show(dlg);
    }

    @Override
    public void displayContents(LinkedHashMap<String, String> contentsMap) {
        ContentsDialog dlg = new ContentsDialog(getMainFrame(), contentsMap, _context);
        show(dlg);
    }

    @Override
    public void displayFile(File file, String description) {
        try {
            UIUtils.displayFile(file, description, _desktopWorker.get());
        } catch (Exception ex) {
            displayErrorMessage("Error displaying file " + file.getAbsolutePath());
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
        String logContent = _context.getLogText();
        RTFBuilder builder = new RTFBuilder();
        builder.startDocument();
        builder.setFont(1);
        String[] lines = logContent.split("\n");
        for (String line : lines) {
            // directive calls are identified by a leading asterisk. They should
            // be colored blue.
            // All other lines should be colored red.
            if (line.startsWith("*")) {
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

    // ================================== DirectivePopulator methods
    // ===================================================================

    @Override
    public List<Character> promptForCharactersByKeyword(String directiveName, boolean permitSelectionFromIncludedCharactersOnly) {
        List<Image> characterKeywordImages = _context.getDataset().getCharacterKeywordImages();
        if (!_advancedMode && characterKeywordImages != null && !characterKeywordImages.isEmpty()) {
            ImageDialog dlg = new ImageDialog(getMainFrame(), _context.getImageSettings(), true);
            dlg.setImages(characterKeywordImages);
            dlg.setTitle(selectCharacterKeywordsCaption);

            show(dlg);

            Set<String> keywords = dlg.getSelectedKeywords();

            List<Character> selectedCharacters = new ArrayList<Character>();

            for (String keyword : keywords) {
                selectedCharacters.addAll(_context.getCharactersForKeyword(keyword));
            }

            if (permitSelectionFromIncludedCharactersOnly) {
                selectedCharacters.retainAll(_context.getIncludedCharacters());
            }

            return selectedCharacters;
        } else {
            CharacterKeywordSelectionDialog dlg = new CharacterKeywordSelectionDialog(getMainFrame(), _context, directiveName.toUpperCase(), permitSelectionFromIncludedCharactersOnly);
            show(dlg);
            return dlg.getSelectedCharacters();
        }
    }

    @Override
    public List<Character> promptForCharactersByList(String directiveName, boolean selectFromIncludedCharactersOnly) {
        List<Character> charactersToSelect;

        String keyword = null;
        if (selectFromIncludedCharactersOnly) {
            charactersToSelect = _context.getCharactersForKeyword(IntkeyContext.CHARACTER_KEYWORD_AVAILABLE);
            keyword = IntkeyContext.CHARACTER_KEYWORD_AVAILABLE;
        } else {
            charactersToSelect = _context.getCharactersForKeyword(IntkeyContext.CHARACTER_KEYWORD_ALL);
            keyword = IntkeyContext.CHARACTER_KEYWORD_ALL;

        }
        CharacterSelectionDialog dlg = new CharacterSelectionDialog(getMainFrame(), charactersToSelect, directiveName.toUpperCase(), keyword, _context.getImageSettings(), _context.displayNumbering());
        show(dlg);
        return dlg.getSelectedCharacters();
    }

    @Override
    public List<Item> promptForTaxaByKeyword(String directiveName, boolean permitSelectionFromIncludedTaxaOnly) {
        List<Image> taxonKeywordImages = _context.getDataset().getTaxonKeywordImages();
        if (!_advancedMode && taxonKeywordImages != null && !taxonKeywordImages.isEmpty()) {
            ImageDialog dlg = new ImageDialog(getMainFrame(), _context.getImageSettings(), true);
            dlg.setImages(taxonKeywordImages);
            dlg.setTitle(selectTaxonKeywordsCaption);

            show(dlg);

            Set<String> keywords = dlg.getSelectedKeywords();

            List<Item> selectedTaxa = new ArrayList<Item>();

            for (String keyword : keywords) {
                selectedTaxa.addAll(_context.getTaxaForKeyword(keyword));
            }

            if (permitSelectionFromIncludedTaxaOnly) {
                selectedTaxa.retainAll(_context.getIncludedCharacters());
            }

            return selectedTaxa;
        } else {
            TaxonKeywordSelectionDialog dlg = new TaxonKeywordSelectionDialog(getMainFrame(), _context, directiveName.toUpperCase(), permitSelectionFromIncludedTaxaOnly);
            show(dlg);
            return dlg.getSelectedTaxa();
        }
    }

    @Override
    public List<Item> promptForTaxaByList(String directiveName, boolean selectFromIncludedTaxaOnly, boolean autoSelectSingleValue) {
        List<Item> taxaToSelect;

        String keyword = null;
        if (selectFromIncludedTaxaOnly) {
            taxaToSelect = _context.getTaxaForKeyword(IntkeyContext.TAXON_KEYWORD_REMAINING);
            keyword = IntkeyContext.TAXON_KEYWORD_REMAINING;
        } else {
            taxaToSelect = _context.getTaxaForKeyword(IntkeyContext.TAXON_KEYWORD_ALL);
            keyword = IntkeyContext.TAXON_KEYWORD_ALL;
        }

        if (taxaToSelect.size() == 1 && autoSelectSingleValue) {
            return taxaToSelect;
        } else {
            TaxonSelectionDialog dlg = new TaxonSelectionDialog(getMainFrame(), taxaToSelect, directiveName.toUpperCase(), keyword, _context.displayNumbering());
            show(dlg);
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
    public List<String> promptForTextValue(TextCharacter ch) {
        if (!_advancedMode && !ch.getImages().isEmpty()) {
            CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), _context.getImageSettings(), true, true);
            show(dlg);
            if (dlg.okButtonPressed()) {
                return dlg.getInputTextValues();
            } else {
                return Collections.EMPTY_LIST;
            }
        } else {
            TextInputDialog dlg = new TextInputDialog(getMainFrame(), ch, _context.getImageSettings(), _context.displayNumbering());
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }
    }

    @Override
    public Set<Integer> promptForIntegerValue(IntegerCharacter ch) {
        if (!_advancedMode && !ch.getImages().isEmpty()) {
            CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), _context.getImageSettings(), true, true);
            show(dlg);
            if (dlg.okButtonPressed()) {
                return dlg.getInputIntegerValues();
            } else {
                return Collections.EMPTY_SET;
            }
        } else {
            IntegerInputDialog dlg = new IntegerInputDialog(getMainFrame(), ch, _context.getImageSettings(), _context.displayNumbering());
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }
    }

    @Override
    public FloatRange promptForRealValue(RealCharacter ch) {
        if (!_advancedMode && !ch.getImages().isEmpty()) {
            CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), _context.getImageSettings(), true, true);
            show(dlg);
            if (dlg.okButtonPressed()) {
                return dlg.getInputRealValues();
            } else {
                return null;
            }
        } else {
            RealInputDialog dlg = new RealInputDialog(getMainFrame(), ch, _context.getImageSettings(), _context.displayNumbering());
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }
    }

    @Override
    public Set<Integer> promptForMultiStateValue(MultiStateCharacter ch) {
        if (!_advancedMode && !ch.getImages().isEmpty()) {
            CharacterImageDialog dlg = new CharacterImageDialog(getMainFrame(), Arrays.asList(new Character[] { ch }), _context.getImageSettings(), true, true);
            show(dlg);
            if (dlg.okButtonPressed()) {
                return dlg.getSelectedStates();
            } else {
                return Collections.EMPTY_SET;
            }
        } else {
            MultiStateInputDialog dlg = new MultiStateInputDialog(getMainFrame(), ch, _context.getImageSettings(), _context.displayNumbering());
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }
    }

    @Override
    public File promptForFile(List<String> fileExtensions, String description, boolean createFileIfNonExistant) throws IOException {
        String[] extensionsArray = new String[fileExtensions.size()];
        fileExtensions.toArray(extensionsArray);

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensionsArray);
        chooser.setFileFilter(filter);

        int returnVal;

        if (createFileIfNonExistant) {
            returnVal = chooser.showSaveDialog(UIUtils.getMainFrame());
        } else {
            returnVal = chooser.showOpenDialog(UIUtils.getMainFrame());
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            if (createFileIfNonExistant) {
                File file = chooser.getSelectedFile();

                if (!file.exists()) {
                    // if only one file extension was supplied and the filename
                    // does
                    // not end with this extension, add it before
                    // creating the file
                    if (fileExtensions.size() == 1) {
                        String extension = fileExtensions.get(0);
                        String filePath = chooser.getSelectedFile().getAbsolutePath();
                        if (!filePath.endsWith(extension)) {
                            file = new File(filePath + "." + extension);
                        }
                    }

                    file.createNewFile();
                }
                return file;
            } else {
                return chooser.getSelectedFile();
            }
        } else {
            return null;
        }
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
    public File promptForOutputFile() {
        OutputFileSelectionDialog dlg = new OutputFileSelectionDialog(getMainFrame(), _context.getOutputFiles());
        show(dlg);
        if (dlg.isOkButtonPressed()) {
            return dlg.getSelectedFile();
        } else {
            return null;
        }
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
                CharacterValue chVal = _context.getSpecimen().getValueForCharacter(ch);
                _listUsedCharacters.setSelectedValue(chVal, true);
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
        _context.newDataSetFile(new File(fileName));
    }

    private void saveCurrentlyOpenedDataset() {
        // if the dataset was downloaded, ask the user if they wish to save it
        StartupFileData startupFileData = _context.getStartupFileData();

        File fileToOpenDataset = null;
        if (startupFileData != null && startupFileData.isRemoteDataset()) {
            int chosenOption = JOptionPane.showConfirmDialog(getMainFrame(), "Save downloaded dataset?", "Save", JOptionPane.YES_NO_OPTION);
            if (chosenOption == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnVal = fileChooser.showOpenDialog(getMainFrame());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File saveDir = fileChooser.getSelectedFile();

                    try {
                        File newInkFile = StartupUtils.saveRemoteDataset(_context, saveDir);
                        fileToOpenDataset = newInkFile;
                    } catch (IOException ex) {
                        displayErrorMessage("Error saving downloaded dataset");
                        // not much we can do here, just abort saving/adding to
                        // recents list.
                        return;
                    }
                } else {
                    fileToOpenDataset = _context.getDatasetStartupFile();
                }
            } else {
                fileToOpenDataset = _context.getDatasetStartupFile();
            }
        } else {
            fileToOpenDataset = _context.getDatasetStartupFile();
        }

        if (_context.getDataset() != null) {
            String datasetTitle = _context.getDataset().getHeading();

            addFileToMRU(fileToOpenDataset.getAbsolutePath(), datasetTitle);
        }
    }

    public static List<Pair<String, String>> getPreviouslyUsedFiles() {
        List<Pair<String, String>> retList = new ArrayList<Pair<String, String>>();

        Preferences prefs = Preferences.userNodeForPackage(Intkey.class);
        if (prefs != null) {
            String mru = prefs.get(MRU_FILES_PREF_KEY, "");
            if (!StringUtils.isEmpty(mru)) {
                String[] mruFiles = mru.split(MRU_FILES_SEPARATOR);
                for (String mruFile : mruFiles) {
                    String[] mruFileItems = mruFile.split(MRU_ITEM_SEPARATOR);
                    retList.add(new Pair<String, String>(mruFileItems[0], mruFileItems[1]));
                }
            }
        }

        return retList;
    }

    /**
     * Removes the specified file from the most recently used file list
     * 
     * @param filename
     *            The filename to remove
     */
    public static void removeFileFromMRU(String filename) {

        List<Pair<String, String>> existingFiles = getPreviouslyUsedFiles();

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < existingFiles.size(); ++i) {

            Pair<String, String> fileNameTitlePair = existingFiles.get(i);
            String existingFileName = fileNameTitlePair.getFirst();

            if (!existingFileName.equalsIgnoreCase(filename)) {

                if (b.length() > 0) {
                    b.append(MRU_FILES_SEPARATOR);
                }
                b.append(fileNameTitlePair.getFirst() + MRU_ITEM_SEPARATOR + fileNameTitlePair.getSecond());
            }
        }

        Preferences prefs = Preferences.userNodeForPackage(Intkey.class);
        prefs.put(MRU_FILES_PREF_KEY, b.toString());
        try {
            prefs.sync();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds the supplied filename to the top of the most recently used files.
     * 
     * @param filename
     */
    public static void addFileToMRU(String filename, String title) {

        Queue<String> q = new LinkedList<String>();

        String newFilePathAndTitle = filename + MRU_ITEM_SEPARATOR + title;
        q.add(newFilePathAndTitle);

        List<Pair<String, String>> existingFiles = getPreviouslyUsedFiles();
        if (existingFiles != null) {
            for (Pair<String, String> existingFile : existingFiles) {
                String existingFilePathAndTitle = existingFile.getFirst() + MRU_ITEM_SEPARATOR + existingFile.getSecond();
                if (!q.contains(existingFilePathAndTitle)) {
                    q.add(existingFilePathAndTitle);
                }
            }
        }

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < MAX_SIZE_MRU && q.size() > 0; ++i) {
            if (i > 0) {
                b.append(MRU_FILES_SEPARATOR);
            }
            b.append(q.poll());
        }

        Preferences prefs = Preferences.userNodeForPackage(Intkey.class);
        prefs.put(MRU_FILES_PREF_KEY, b.toString());
        try {
            prefs.sync();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

}
