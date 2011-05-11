package au.org.ala.delta.intkey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import org.jdesktop.application.Action;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.Logger;
import au.org.ala.delta.intkey.directives.FileCharactersDirective;
import au.org.ala.delta.intkey.directives.FileTaxaDirective;
import au.org.ala.delta.intkey.directives.IntkeyContext;
import au.org.ala.delta.intkey.directives.IntkeyDirective;
import au.org.ala.delta.intkey.directives.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.IntkeyDirectiveParser;
import au.org.ala.delta.intkey.directives.NewDatasetDirective;
import au.org.ala.delta.intkey.directives.UseDirective;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.intkey.model.specimen.CharacterValue;
import au.org.ala.delta.intkey.ui.ReExecuteDialog;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.ui.AboutBox;
import au.org.ala.delta.ui.DeltaSingleFrameApplication;
import au.org.ala.delta.ui.util.IconHelper;

public class Intkey extends DeltaSingleFrameApplication {

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
    
    private AvailableCharacterListModel _availableCharacterListModel;
    private UsedCharacterListModel _usedCharacterListModel;
    private ItemListModel _itemListModel;
    
    private JMenu _mnuReExecute;
    private JLabel _lblNumAvailableCharacters;
    private JLabel _lblNumUsedCharacters;

    @Resource
    String windowTitleWithDatasetTitle;


    public static void main(String[] args) {
        setupMacSystemProperties(Intkey.class);
        launch(Intkey.class, args);
    }

    @Override
    protected void initialize(String[] args) {
        ResourceMap resourceMap = getContext().getResourceMap(Intkey.class);
        resourceMap.injectFields(this);
    }

    @Override
    protected void startup() {
        JFrame mainFrame = getMainFrame();
        mainFrame.setTitle("Intkey");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setIconImages(IconHelper.getRedIconList());

        _context = new IntkeyContext(this);

        _rootPanel = new JPanel();
        _rootPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        _rootPanel.setBackground(SystemColor.control);
        _rootPanel.setLayout(new BorderLayout(0, 0));

        JPanel globalOptionBar = new JPanel();
        _rootPanel.add(globalOptionBar, BorderLayout.NORTH);
        globalOptionBar.setLayout(new BorderLayout(0, 0));

        JButton btnContextHelp = new JButton(IconHelper.createImageIconFromAbsolutePath("/au/org/ala/delta/intkey/resources/icons/helpa.png"));
        btnContextHelp.setMargin(new Insets(2, 5, 2, 5));
        globalOptionBar.add(btnContextHelp, BorderLayout.EAST);

        _rootSplitPane = new JSplitPane();
        _rootSplitPane.setResizeWeight(0.5);
        _rootSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent arg0) {
                System.out.println("root split pane shown");
            }
        });
        _rootSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        _rootSplitPane.setContinuousLayout(true);
        _rootPanel.add(_rootSplitPane);

        _innerSplitPaneLeft = new JSplitPane();
        _innerSplitPaneLeft.setResizeWeight(0.5);

        _innerSplitPaneLeft.setContinuousLayout(true);
        _innerSplitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
        _rootSplitPane.setLeftComponent(_innerSplitPaneLeft);

        JPanel pnlBestCharacters = new JPanel();
        _innerSplitPaneLeft.setLeftComponent(pnlBestCharacters);
        pnlBestCharacters.setLayout(new BorderLayout(0, 0));

        JScrollPane sclPaneBestCharacters = new JScrollPane();
        pnlBestCharacters.add(sclPaneBestCharacters, BorderLayout.CENTER);

        _listAvailableCharacters = new JList();
        _listAvailableCharacters.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int selectedIndex = _listAvailableCharacters.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        try {
                            Character ch = _availableCharacterListModel.getCharacterAt(selectedIndex);
                            IntkeyDirectiveInvocation invoc = new UseDirective().doProcess(_context, Integer.toString(ch.getCharacterId()));
                            _context.executeDirective(invoc);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        sclPaneBestCharacters.setViewportView(_listAvailableCharacters);

        JPanel pnlBestCharactersHeader = new JPanel();
        pnlBestCharacters.add(pnlBestCharactersHeader, BorderLayout.NORTH);
        pnlBestCharactersHeader.setLayout(new BorderLayout(0, 0));

        _lblNumAvailableCharacters = new JLabel("Available Characters");
        _lblNumAvailableCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlBestCharactersHeader.add(_lblNumAvailableCharacters, BorderLayout.WEST);

        JPanel pnlUsedCharacters = new JPanel();
        _innerSplitPaneLeft.setRightComponent(pnlUsedCharacters);
        pnlUsedCharacters.setLayout(new BorderLayout(0, 0));

        JScrollPane sclPnUsedCharacters = new JScrollPane();
        pnlUsedCharacters.add(sclPnUsedCharacters, BorderLayout.CENTER);

        _listUsedCharacters = new JList();
        sclPnUsedCharacters.setViewportView(_listUsedCharacters);

        JPanel pnlUsedCharactersHeader = new JPanel();
        pnlUsedCharacters.add(pnlUsedCharactersHeader, BorderLayout.NORTH);
        pnlUsedCharactersHeader.setLayout(new BorderLayout(0, 0));

        _lblNumUsedCharacters = new JLabel("Used Characters");
        _lblNumUsedCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlUsedCharactersHeader.add(_lblNumUsedCharacters, BorderLayout.WEST);

        _innerSplitPaneRight = new JSplitPane();
        _innerSplitPaneRight.setResizeWeight(0.5);
        _innerSplitPaneRight.setContinuousLayout(true);
        _innerSplitPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
        _rootSplitPane.setRightComponent(_innerSplitPaneRight);

        JPanel pnlRemainingTaxa = new JPanel();
        _innerSplitPaneRight.setLeftComponent(pnlRemainingTaxa);
        pnlRemainingTaxa.setLayout(new BorderLayout(0, 0));

        JScrollPane sclPnRemainingTaxa = new JScrollPane();
        pnlRemainingTaxa.add(sclPnRemainingTaxa, BorderLayout.CENTER);

        _listRemainingTaxa = new JList();
        sclPnRemainingTaxa.setViewportView(_listRemainingTaxa);

        JPanel pnlRemainingTaxaHeader = new JPanel();
        pnlRemainingTaxa.add(pnlRemainingTaxaHeader, BorderLayout.NORTH);
        pnlRemainingTaxaHeader.setLayout(new BorderLayout(0, 0));

        JLabel lblNumRemainingTaxa = new JLabel("Remaining Taxa");
        lblNumRemainingTaxa.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlRemainingTaxaHeader.add(lblNumRemainingTaxa, BorderLayout.WEST);

        JPanel pnlEliminatedTaxa = new JPanel();
        _innerSplitPaneRight.setRightComponent(pnlEliminatedTaxa);
        pnlEliminatedTaxa.setLayout(new BorderLayout(0, 0));

        JScrollPane sclPnEliminatedTaxa = new JScrollPane();
        pnlEliminatedTaxa.add(sclPnEliminatedTaxa, BorderLayout.CENTER);

        _listEliminatedTaxa = new JList();
        sclPnEliminatedTaxa.setViewportView(_listEliminatedTaxa);

        JPanel pnlEliminatedTaxaHeader = new JPanel();
        pnlEliminatedTaxa.add(pnlEliminatedTaxaHeader, BorderLayout.NORTH);
        pnlEliminatedTaxaHeader.setLayout(new BorderLayout(0, 0));

        JLabel lblNewLabel = new JLabel("Eliminated Taxa");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlEliminatedTaxaHeader.add(lblNewLabel, BorderLayout.WEST);

        getMainView().setMenuBar(buildMenus());

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
                    try {
                        IntkeyDirectiveParser.createInstance().parse(new InputStreamReader(new ByteArrayInputStream(cmdStr.getBytes())), _context);
                    } catch (Exception ex) {
                        Logger.log("Exception thrown while processing directive \"%s\"", cmdStr);
                        ex.printStackTrace();
                    }
                }
                _txtFldCmdBar.setText(null);
            }
        });

        _txtFldCmdBar.setFont(new Font("Courier New", Font.BOLD, 13));
        _txtFldCmdBar.setForeground(SystemColor.text);
        _txtFldCmdBar.setBackground(Color.BLACK);
        _rootPanel.add(_txtFldCmdBar, BorderLayout.SOUTH);
        _txtFldCmdBar.setColumns(10);

        show(_rootPanel);
    }

    @Override
    protected void ready() {
        super.ready();
        _rootSplitPane.setDividerLocation(2.0 / 3.0);
        _innerSplitPaneLeft.setDividerLocation(2.0 / 3.0);
        _innerSplitPaneRight.setDividerLocation(2.0 / 3.0);
    }

    @Override
    protected void shutdown() {
        super.shutdown();
    }

    /**
     * @wbp.parser.entryPoint
     */
    private JMenuBar buildMenus() {

        ActionMap actionMap = getContext().getActionMap();

        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu mnuFile = new JMenu();
        mnuFile.setName("mnuFile");

        JMenuItem mnuItNewDataSet = buildMenuItemForDirective(new NewDatasetDirective(), "mnuDirectiveNewDataSet");
        mnuFile.add(mnuItNewDataSet);

        JMenuItem mnuItPreferences = new JMenuItem();
        mnuItPreferences.setAction(actionMap.get("setPreferences"));
        mnuItPreferences.setEnabled(false);
        mnuFile.add(mnuItPreferences);

        JMenuItem mnuItContents = new JMenuItem();
        mnuItContents.setAction(actionMap.get("setContents"));
        mnuItContents.setEnabled(false);
        mnuFile.add(mnuItContents);

        mnuFile.addSeparator();

        JMenu mnuFileCmds = new JMenu();
        mnuFileCmds.setName("mnuFileCmds");

        JMenuItem mnuItFileCharactersCmd = buildMenuItemForDirective(new FileCharactersDirective(), "mnuDirectiveFileCharacters");
        // mnuFileCmds.add(mnuItFileCharactersCmd);

        JMenuItem mnuItFileTaxaCmd = buildMenuItemForDirective(new FileTaxaDirective(), "mnuDirectiveFileTaxa");
        // mnuFileCmds.add(mnuItFileTaxaCmd);

        mnuFile.add(mnuFileCmds);

        mnuFile.addSeparator();

        /*
         * JMenuItem mnuItAdvancedMode = new JMenuItem();
         * mnuItAdvancedMode.setAction(actionMap.get("switchAdvancedMode"));
         * mnuItAdvancedMode.setEnabled(false); mnuFile.add(mnuItAdvancedMode);
         * 
         * mnuFile.addSeparator();
         */

        JMenuItem mnuItFileExit = new JMenuItem();
        mnuItFileExit.setAction(actionMap.get("exitApplication"));
        mnuFile.add(mnuItFileExit);
        menuBar.add(mnuFile);

        _mnuReExecute = new JMenu("ReExecute...");
        _mnuReExecute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ReExecuteDialog dlg = new ReExecuteDialog(_context.getMainFrame(), _context.getExecutedDirectives());
                dlg.setVisible(true);
                _mnuReExecute.setSelected(false);
                IntkeyDirectiveInvocation directive = dlg.getDirectiveToExecute();
                if (directive != null) {
                    _context.executeDirective(directive);
                }
            }
        });
        menuBar.add(_mnuReExecute);

        // Window menu
        JMenu mnuWindow = new JMenu();
        mnuWindow.setName("mnuWindow");

        JMenuItem mnuItCascade = new JMenuItem();
        mnuItCascade.setAction(actionMap.get("cascadeWindows"));
        mnuItCascade.setEnabled(false);
        mnuWindow.add(mnuItCascade);

        JMenuItem mnuItTile = new JMenuItem();
        mnuItTile.setAction(actionMap.get("tileWindows"));
        mnuItTile.setEnabled(false);
        mnuWindow.add(mnuItTile);

        mnuWindow.addSeparator();

        JMenuItem mnuItCloseAll = new JMenuItem();
        mnuItCloseAll.setAction(actionMap.get("closeAllWindows"));
        mnuItCloseAll.setEnabled(false);
        mnuWindow.add(mnuItCloseAll);
        menuBar.add(mnuWindow);

        // Help menu
        JMenu mnuHelp = new JMenu();
        mnuHelp.setName("mnuHelp");
        JMenuItem mnuItHelpIntroduction = new JMenuItem();
        mnuItHelpIntroduction.setEnabled(false);
        mnuItHelpIntroduction.setName("mnuItHelpIntroduction");
        mnuHelp.add(mnuItHelpIntroduction);
        // mnuItHelpContents.addActionListener(_helpController.helpAction());

        if (isMac()) {
            configureMacAboutBox(actionMap.get("openAbout"));
        } else {
            JMenuItem mnuItAbout = new JMenuItem();
            mnuItAbout.setAction(actionMap.get("openAbout"));
            mnuHelp.add(mnuItAbout);
        }

        menuBar.add(mnuHelp);

        _cmdMenus = new HashMap<String, JMenu>();
        _cmdMenus.put("file", mnuFileCmds);

        return menuBar;
    }

    // File menu actions
    @Action
    public void switchAdvancedMode() {
    }

    @Action
    public void exitApplication() {
        exit();
    }

    @Action
    public void setContents() {
    }

    @Action
    public void setPreferences() {
    }

    @Action
    public void setFileCharacters() {
    }

    // Window menu actions
    @Action
    public void cascadeWindows() {
    }

    @Action
    public void tileWindows() {
    }

    @Action
    public void closeAllWindows() {
    }

    // Help menu actions

    @Action
    public void openAbout() {
        AboutBox aboutBox = new AboutBox(getMainFrame());
        show(aboutBox);
    }

    private JMenuItem buildMenuItemForDirective(IntkeyDirective dir, String itemName) {
        JMenuItem mnuIt = new JMenuItem();
        mnuIt.setName(itemName);

        mnuIt.addActionListener(new DirectiveMenuActionListener(dir, _context));

        return mnuIt;
    }

    private class DirectiveMenuActionListener implements ActionListener {

        private IntkeyDirective _dir;
        private IntkeyContext _context;

        public DirectiveMenuActionListener(IntkeyDirective dir, IntkeyContext context) {
            _dir = dir;
            _context = context;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                _dir.process(_context, null);
            } catch (Exception ex) {
                Logger.log("Error while running directive from menu - %s %s", _dir.toString(), ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void handleNewDataSet(IntkeyDataset dataset) {
        getMainFrame().setTitle(String.format(windowTitleWithDatasetTitle, dataset.getHeading()));
        
        _availableCharacterListModel = new AvailableCharacterListModel(dataset.getCharacters());
        _usedCharacterListModel = new UsedCharacterListModel();
        _itemListModel = new ItemListModel(dataset.getTaxa());
        
        _listAvailableCharacters.setModel(_availableCharacterListModel);
        _listUsedCharacters.setModel(_usedCharacterListModel);
        _listRemainingTaxa.setModel(new ItemListModel(dataset.getTaxa()));
    }

    public void handleCharacterUsed(Character ch, CharacterValue value) {
        // remove from top list
        _availableCharacterListModel.removeCharacter(ch);
        
        // add to bottom list
        _usedCharacterListModel.addCharacterValue(ch, value);
    }

    public void handleCharacterChanged(Character ch, CharacterValue value) {

    }

    public void handleCharacterDeleted(Character ch) {

    }

    private class AvailableCharacterListModel extends AbstractListModel {

        private List<Character> _characters;
        private CharacterFormatter _formatter;

        public AvailableCharacterListModel(List<Character> characters) {
            _characters = new ArrayList<Character>(characters);
            _formatter = new CharacterFormatter(false, false, true, true);
        }

        @Override
        public int getSize() {
            return _characters.size();
        }

        @Override
        public Object getElementAt(int index) {
            return _formatter.formatCharacterDescription(_characters.get(index));
        }

        public Character getCharacterAt(int index) {
            return _characters.get(index);
        }

        public void addCharacter(Character ch) {

        }

        public void removeCharacter(Character ch) {
            int charIndex = _characters.indexOf(ch);
            if (charIndex > -1) {
                _characters.remove(charIndex);
                fireIntervalRemoved(this, charIndex, charIndex);
            }
        }
    }

    private class UsedCharacterListModel extends AbstractListModel {

        private List<CharacterValue> _values;
        private HashMap<Character, CharacterValue> _characterValueMap;
        
        public UsedCharacterListModel() {
            _values = new ArrayList<CharacterValue>();
            _characterValueMap = new HashMap<Character, CharacterValue>();
        }

        @Override
        public int getSize() {
            return _values.size();
        }

        @Override
        public Object getElementAt(int index) {
            return _values.get(index).toString();
        }

        public CharacterValue getCharacterValueAt(int index) {
            return _values.get(index);
        }

        public void addCharacterValue(Character ch, CharacterValue value) {
            _values.add(value);
            _characterValueMap.put(ch, value);
            fireIntervalAdded(this, _values.size() - 1, _values.size() - 1);
        }

        public void removeValueForCharacter(Character ch) {

        }

    }

    private class ItemListModel extends AbstractListModel {

        List<Item> _items;
        ItemFormatter _formatter;

        public ItemListModel(List<Item> items) {
            _items = new ArrayList<Item>(items);
            _formatter = new ItemFormatter(false, true, false, true, false);
        }

        @Override
        public int getSize() {
            return _items.size();
        }

        @Override
        public Object getElementAt(int index) {
            return _formatter.formatItemDescription(_items.get(index));
        }
    }

}
