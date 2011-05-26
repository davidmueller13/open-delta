package au.org.ala.delta.editor.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.editor.DeltaView;
import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.ui.util.MessageDialogHelper;
import au.org.ala.delta.editor.ui.validator.CharacterValidator;
import au.org.ala.delta.editor.ui.validator.ValidationResult;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.observer.AbstractDataSetObserver;
import au.org.ala.delta.model.observer.DeltaDataSetChangeEvent;
import au.org.ala.delta.ui.rtf.RtfEditor;

/**
 * Provides a user interface that allows a character to be edited.
 */
public class CharacterEditor extends JInternalFrame implements DeltaView {
	
	private static final long serialVersionUID = 9193388605723396077L;

	/** Contains the items we can edit */
	private EditorViewModel _dataSet;
	
	/** The currently selected character */
	private Character _selectedCharacter;
	
	/** Flag to allow updates to the model to be disabled during new character selection */
	private boolean _editsDisabled;
	
	private JSpinner spinner;
	private RtfEditor rtfEditor;
	private JCheckBox mandatoryCheckBox;
	private JButton btnDone;
	private JLabel lblEditCharacterName;
	private JToggleButton btnSelect;
	private SelectionList characterSelectionList;
	private JScrollPane editorScroller;
	private JCheckBox exclusiveCheckBox; 
	private JLabel characterNumberLabel;
	private StateEditor stateEditor;
	
	@Resource
	private String titleSuffix;
	@Resource
	private String editCharacterLabelText;
	@Resource
	private String selectCharacterLabelText;
	private JComboBox comboBox;
	private JTabbedPane tabbedPane;
	
	private ApplicationContext _context;
	private ResourceMap _resources;
	
	private MessageDialogHelper _dialogHelper;
	
	private CharacterValidator _validator;
	private CharacterNotesEditor characterNotesEditor;
	private ImageDetailsPanel imageDetails;
	
	public CharacterEditor(EditorViewModel model) {	
		setName("CharacterEditorDialog");
		_dialogHelper = new MessageDialogHelper();
		_context = Application.getInstance().getContext();
		_resources = _context.getResourceMap(CharacterEditor.class);
		_resources.injectFields(this);
		ActionMap map = Application.getInstance().getContext().getActionMap(this);
		createUI();
		addEventHandlers(map);
		bind(model);
	}

	/**
	 * Adds the event handlers to the UI components.
	 */
	private void addEventHandlers(ActionMap map) {
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (_editsDisabled) {
					return;
				}
				setSelectedCharacter(_dataSet.getCharacter((Integer)spinner.getValue()));
			}
		});
		
		rtfEditor.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				characterEditPerformed();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				characterEditPerformed();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				characterEditPerformed();
			}
		});
		characterSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		characterSelectionList.setModel(new CharacterListModel());
		characterSelectionList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (_editsDisabled) {
					return;
				}
				setSelectedCharacter(_dataSet.getCharacter(characterSelectionList.getSelectedIndex()+1));
				
			}
		});
		
		btnDone.setAction(map.get("characterEditDone"));
		mandatoryCheckBox.setAction(map.get("mandatoryChanged"));
		exclusiveCheckBox.setAction(map.get("exclusiveChanged"));
		
		btnSelect.setAction(map.get("selectCharacterByName"));
		characterSelectionList.setSelectionAction(map.get("characterSelected"));
		
		comboBox.setAction(map.get("characterTypeChanged"));
	}
	
	@Action
	public void characterEditDone() {
		setVisible(false);
	}
	
	@Action
	public void mandatoryChanged() {
		
		boolean mandatory = !_selectedCharacter.isMandatory();
		ValidationResult result = _validator.validateMandatory(mandatory);
		
		if (!result.isValid()) {
			_dialogHelper.displayValidationResult(result);
		}
		if (!result.isError()) {
			_selectedCharacter.setMandatory(mandatory);
		}
	}
	
	@Action
	public void exclusiveChanged() {
		if (_selectedCharacter.getCharacterType().isMultistate()) {
			MultiStateCharacter multiStateChar = (MultiStateCharacter)_selectedCharacter;
			boolean currentlyExclusive = multiStateChar.isExclusive();
			
			ValidationResult result = _validator.validateExclusive(!currentlyExclusive);
			if (result.isValid()) {
				multiStateChar.setExclusive(!currentlyExclusive);
			}
			else {
				_dialogHelper.displayValidationResult(result);
				// This is done to reset the checkbox as a result of the event this is
				// fired by the model.
				multiStateChar.setExclusive(false);
			}
		}
		else {
			throw new UnsupportedOperationException("Only MultiStateCharacters can be exclusive");
		}
	}
	
	@Action
	public void selectCharacterByName() {
		if (btnSelect.isSelected()) {
			mandatoryCheckBox.setEnabled(false);
			spinner.setEnabled(false);
			exclusiveCheckBox.setEnabled(false);
			comboBox.setEnabled(false);
			lblEditCharacterName.setText(selectCharacterLabelText);
			editorScroller.setViewportView(characterSelectionList);
			characterSelectionList.requestFocusInWindow();
			
		}
		else {
			mandatoryCheckBox.setEnabled(true);
			spinner.setEnabled(true);
			exclusiveCheckBox.setEnabled(true);
			comboBox.setEnabled(true);
			lblEditCharacterName.setText(editCharacterLabelText);
			editorScroller.setViewportView(rtfEditor);
		}
	}
	
	public void setSelectedCharacter(Character character) {
		_selectedCharacter = character;
		characterNotesEditor.bind(_selectedCharacter);
		imageDetails.bind(_selectedCharacter);
		
		_validator = new CharacterValidator(_dataSet, _selectedCharacter);
		
		updateScreen();
	}
	
	@Action
	public void characterSelected() {
		btnSelect.setSelected(false);
		selectCharacterByName();
	}
	
	@Action
	public void characterTypeChanged() {
		CharacterType type = (CharacterType)comboBox.getSelectedItem();
		CharacterType existingType = _selectedCharacter.getCharacterType();
		if (type.equals(existingType)) {
			return;
		}
		
		if (_dataSet.canChangeCharacterType(_selectedCharacter, type)) {
			_dataSet.changeCharacterType(_selectedCharacter, type);
		}
		else {
			comboBox.getModel().setSelectedItem(existingType);
		}
	}
	
	/**
	 * Creates the user interface components of this dialog.
	 */
	private void createUI() {
		
		characterNumberLabel = new JLabel("Character Number:");
		characterNumberLabel.setName("characterNumberLabel");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 1, 1));
		
		btnSelect = new JToggleButton("Select");
		btnSelect.setName("selectTaxonNumberButton");
		
		lblEditCharacterName = new JLabel("");
		lblEditCharacterName.setName("lblEditCharacterName");
		
		rtfEditor = new RtfEditor();
		editorScroller = new JScrollPane(rtfEditor);
		
		mandatoryCheckBox = new JCheckBox();
		mandatoryCheckBox.setName("mandatoryCheckbox");
		
		JPanel panel = new JPanel();
		
		btnDone = new JButton("Done");
		btnDone.setName("doneEditingTaxonButton");
		
		JButton btnHelp = new JButton("Help");
		btnHelp.setName("helpWithTaxonEditorButton");
		
		characterSelectionList = new ItemList();
		
	    exclusiveCheckBox = new JCheckBox("Exclusive");
		
		comboBox = new JComboBox();
		comboBox.setModel(new CharacterTypeComboModel());
		comboBox.setRenderer(new CharacterTypeRenderer());
		
		JLabel lblCharacterType = new JLabel("Character Type:");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(characterNumberLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(spinner, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
									.addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(lblCharacterType)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(6)
									.addComponent(mandatoryCheckBox)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(exclusiveCheckBox)
								.addComponent(btnSelect))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEditCharacterName)
								.addComponent(editorScroller, GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(0, 567, Short.MAX_VALUE)
									.addComponent(btnDone)
									.addGap(5)
									.addComponent(btnHelp))
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
							.addGap(1)))
					.addGap(19))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(characterNumberLabel)
						.addComponent(lblEditCharacterName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSelect))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblCharacterType)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
									.addComponent(exclusiveCheckBox))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(mandatoryCheckBox))))
						.addComponent(editorScroller, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
					.addGap(9)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDone)
						.addComponent(btnHelp))
					.addGap(17))
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		stateEditor = new StateEditor();
		tabbedPane.addTab(_resources.getString("states.tab.title"), stateEditor);
		
		imageDetails = new ImageDetailsPanel();
		imageDetails.setEnabled(false);
		tabbedPane.addTab(_resources.getString("images.tab.title"), imageDetails);
		
		characterNotesEditor = new CharacterNotesEditor();
		_context.getResourceMap(CharacterNotesEditor.class).injectComponents(characterNotesEditor);
		tabbedPane.addTab(_resources.getString("notes.tab.title"), characterNotesEditor);
		
		
		panel.add(tabbedPane);
		getContentPane().setLayout(groupLayout);
		setPreferredSize(new Dimension(827, 500));
		setMinimumSize(new Dimension(748, 444));
	}
	
	/**
	 * Provides the backing model for this Dialog.
	 * @param dataSet the data set the dialog operates from.
	 * @param itemNumber the currently selected item
	 */
	public void bind(EditorViewModel dataSet) {
		_dataSet = dataSet;
		setSelectedCharacter(dataSet.getSelectedCharacter());
		_validator = new CharacterValidator(_dataSet, _selectedCharacter);
		_dataSet.addDeltaDataSetObserver(new AbstractDataSetObserver() {

			@Override
			public void characterEdited(DeltaDataSetChangeEvent event) {
				if (event.getCharacter().equals(_selectedCharacter)) {
					// This is to handle CharacterType changes.
					_selectedCharacter = _dataSet.getCharacter(_selectedCharacter.getCharacterId());
					
					updateScreen();
				}
			}
		});
		
		updateScreen();
	}
	
	private void characterEditPerformed() {
		if (_editsDisabled) {
			return;
		}
		_selectedCharacter.setDescription(rtfEditor.getRtfTextBody());
	}
	
	/**
	 * Synchronizes the state of the UI with the currently selected Item.
	 */
	private void updateScreen() {
		
		_editsDisabled = true;
		setTitle(_dataSet.getName() + " "+titleSuffix);
		if (_selectedCharacter == null) {
			_selectedCharacter = _dataSet.getCharacter(1);
		}
		
		SpinnerNumberModel model = (SpinnerNumberModel)spinner.getModel();
		model.setMaximum(_dataSet.getNumberOfCharacters());
		model.setValue(_selectedCharacter.getCharacterId());
		
		// This check prevents update errors on the editor pane Document.
		if (!_selectedCharacter.getDescription().equals(rtfEditor.getRtfTextBody())) {
			rtfEditor.setText(_selectedCharacter.getDescription());
		}
		mandatoryCheckBox.setSelected(_selectedCharacter.isMandatory());
		
		if (!_selectedCharacter.getCharacterType().equals(comboBox.getSelectedItem())) {
			comboBox.setSelectedItem(_selectedCharacter.getCharacterType());
		}
		if (_selectedCharacter instanceof MultiStateCharacter) {
			MultiStateCharacter multistateChar = (MultiStateCharacter)_selectedCharacter;
			stateEditor.bind(_dataSet, multistateChar);
			tabbedPane.setEnabledAt(0, true);
			exclusiveCheckBox.setEnabled(true);
			exclusiveCheckBox.setSelected(multistateChar.isExclusive());
		}
		else {
			exclusiveCheckBox.setEnabled(false);
			exclusiveCheckBox.setSelected(false);
			tabbedPane.setEnabledAt(0, false);
			if (tabbedPane.getSelectedIndex() == 0) {
				tabbedPane.setSelectedIndex(1);
			}
		}
		_editsDisabled = false;
	}
	
	@Override
	public void open() {}

	@Override
	public boolean editsValid() {
		return true;
	}

	@Override
	public String getViewTitle() {
		return titleSuffix;
	}
	
	class CharacterListModel extends AbstractListModel {

		private CharacterFormatter _formatter = new CharacterFormatter();
		private static final long serialVersionUID = 6573565854830718124L;

		@Override
		public int getSize() {
			return _dataSet.getNumberOfCharacters();
		}

		@Override
		public Object getElementAt(int index) {
			return _formatter.formatCharacterDescription(_dataSet.getCharacter(index+1));
		}
		
	}
	
	class CharacterTypeComboModel extends AbstractListModel implements ComboBoxModel {

		private static final long serialVersionUID = -9004809838787455121L;
		private Object _selected;
		
		@Override
		public int getSize() {
			return CharacterType.values().length;
		}

		@Override
		public Object getElementAt(int index) {
			return CharacterType.values()[index];
		}

		@Override
		public void setSelectedItem(Object anItem) {
			_selected = anItem;
			fireContentsChanged(this, -1, -1);
		}

		@Override
		public Object getSelectedItem() {
			return _selected;
		}
		
	}
	
	class CharacterTypeRenderer extends BasicComboBoxRenderer {

		private static final long serialVersionUID = 7953163275755684592L;

		private static final String PREFIX = "characterType.";
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			String key = PREFIX+value;
			setText(_resources.getString(key));
			
			return this;
		}
	}
}
