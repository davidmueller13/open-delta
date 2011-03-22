package au.org.ala.delta.intkey;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.ActionMap;
import javax.swing.JButton;
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

import org.jdesktop.application.Action;

import au.org.ala.delta.intkey.ui.SelectDataSetDialog;
import au.org.ala.delta.ui.AboutBox;
import au.org.ala.delta.ui.DeltaSingleFrameApplication;
import au.org.ala.delta.ui.util.IconHelper;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Intkey extends DeltaSingleFrameApplication {
    
    private JPanel _rootPanel;
    private JSplitPane _rootSplitPane;
    private JSplitPane _innerSplitPaneRight;
    private JSplitPane _innerSplitPaneLeft;
    
    public static void main(String[] args) {
        launch(Intkey.class, args);
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    protected void startup() {
        ActionMap actionMap = getContext().getActionMap(this);

        JFrame mainFrame = getMainFrame();
        mainFrame.setTitle("Intkey");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setIconImages(IconHelper.getRedIconList());
        
        _rootPanel = new JPanel();
        _rootPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        _rootPanel.setBackground(SystemColor.control);
        _rootPanel.setLayout(new BorderLayout(0, 0));
        
        JPanel globalOptionBar = new JPanel();
        _rootPanel.add(globalOptionBar, BorderLayout.NORTH);
        globalOptionBar.setLayout(new BorderLayout(0, 0));
        
        JButton btnHelpIntro = new JButton(IconHelper.createImageIconFromAbsolutePath("/au/org/ala/delta/intkey/resources/icons/helpa.png"));
        btnHelpIntro.setMargin(new Insets(2, 5, 2, 5));
        globalOptionBar.add(btnHelpIntro, BorderLayout.WEST);
        
        JButton btnContextHelp = new JButton(IconHelper.createImageIconFromAbsolutePath("/au/org/ala/delta/intkey/resources/icons/find.png"));
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
        
        JList listBestCharacters = new JList();
        sclPaneBestCharacters.setViewportView(listBestCharacters);
        
        JPanel pnlBestCharactersHeader = new JPanel();
        pnlBestCharacters.add(pnlBestCharactersHeader, BorderLayout.NORTH);
        pnlBestCharactersHeader.setLayout(new BorderLayout(0, 0));
        
        JLabel lblNumBestCharacters = new JLabel("Best Characters");
        lblNumBestCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlBestCharactersHeader.add(lblNumBestCharacters, BorderLayout.WEST);
        
        JPanel pnlUsedCharacters = new JPanel();
        _innerSplitPaneLeft.setRightComponent(pnlUsedCharacters);
        pnlUsedCharacters.setLayout(new BorderLayout(0, 0));
        
        JScrollPane sclPnUsedCharacters = new JScrollPane();
        pnlUsedCharacters.add(sclPnUsedCharacters, BorderLayout.CENTER);
        
        JList listUsedCharacters = new JList();
        sclPnUsedCharacters.setViewportView(listUsedCharacters);
        
        JPanel pnlUsedCharactersHeader = new JPanel();
        pnlUsedCharacters.add(pnlUsedCharactersHeader, BorderLayout.NORTH);
        pnlUsedCharactersHeader.setLayout(new BorderLayout(0, 0));
        
        JLabel lblNumUsedCharacters = new JLabel("Used Characters");
        lblNumUsedCharacters.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlUsedCharactersHeader.add(lblNumUsedCharacters, BorderLayout.WEST);
        
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
        
        JList listRemainingTaxa = new JList();
        sclPnRemainingTaxa.setViewportView(listRemainingTaxa);
        
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
        
        JList listEliminatedTaxa = new JList();
        sclPnEliminatedTaxa.setViewportView(listEliminatedTaxa);
        
        JPanel pnlEliminatedTaxaHeader = new JPanel();
        pnlEliminatedTaxa.add(pnlEliminatedTaxaHeader, BorderLayout.NORTH);
        pnlEliminatedTaxaHeader.setLayout(new BorderLayout(0, 0));
        
        JLabel lblNewLabel = new JLabel("Eliminated Taxa");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlEliminatedTaxaHeader.add(lblNewLabel, BorderLayout.WEST);

        getMainView().setMenuBar(buildMenus());
        
        show(_rootPanel);
    }
    
    @Override
    protected void ready() {
        super.ready();
        _rootSplitPane.setDividerLocation(2.0/3.0);
        _innerSplitPaneLeft.setDividerLocation(2.0/3.0);
        _innerSplitPaneRight.setDividerLocation(2.0/3.0);
    }

    @Override
    protected void shutdown() {
        super.shutdown();
    }
    
    private JMenuBar buildMenus() {
        
        ActionMap actionMap = getContext().getActionMap();

        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu mnuFile = new JMenu();
        mnuFile.setName("mnuFile");

        JMenuItem mnuItNewDataSet = new JMenuItem();
        mnuItNewDataSet.setAction(actionMap.get("newDataSet"));
        mnuFile.add(mnuItNewDataSet);
        
        mnuFile.addSeparator();

        JMenuItem mnuItAdvancedMode = new JMenuItem();
        mnuItAdvancedMode.setAction(actionMap.get("switchAdvancedMode"));
        mnuItAdvancedMode.setEnabled(false);
        mnuFile.add(mnuItAdvancedMode);
        
        mnuFile.addSeparator();        
        
        JMenuItem mnuItFileExit = new JMenuItem();
        mnuItFileExit.setAction(actionMap.get("exitApplication"));
        mnuFile.add(mnuItFileExit);
        menuBar.add(mnuFile);
        
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
        //mnuItHelpContents.addActionListener(_helpController.helpAction());
        
        JMenuItem mnuItAbout = new JMenuItem();
        mnuItAbout.setAction(actionMap.get("openAbout"));
        
        mnuHelp.add(mnuItAbout);
        
        menuBar.add(mnuHelp);
        
        return menuBar;
    }
    
    //File menu actions
    @Action
    public void exitApplication() {
        exit();
    }
    
    @Action
    public void newDataSet() {
        SelectDataSetDialog dlg = new SelectDataSetDialog(getMainFrame());
        show(dlg);
        if (dlg.isFileSelected()) {
            JOptionPane.showMessageDialog(getMainFrame(), "Selected Filename: " + dlg.getSelectedFilePath());
        }
    }
    
    @Action
    public void switchAdvancedMode() {
        
    }
    
    //Window menu actions
    @Action
    public void cascadeWindows() {
    }
    
    @Action
    public void tileWindows() {
        
    }
    
    @Action
    public void closeAllWindows() {
        
    }    
    
    
    //Help menu actions
    
    @Action
    public void openAbout() {
        AboutBox aboutBox = new AboutBox(getMainFrame());
        show(aboutBox);
    }

    public JSplitPane getInnerSplitPaneLeft() {
        return _innerSplitPaneLeft;
    }
}
