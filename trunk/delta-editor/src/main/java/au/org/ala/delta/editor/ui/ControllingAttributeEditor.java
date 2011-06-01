package au.org.ala.delta.editor.ui;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterDependency;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.format.CharacterDependencyFormatter;
import au.org.ala.delta.model.format.CharacterFormatter;

public class ControllingAttributeEditor extends CharacterDepencencyEditor {
	
	private static final long serialVersionUID = -1550092824029396438L;
	private JComboBox attributeCombo;
	
	private CharacterDependency _controllingAttribute;
	private List<Integer> _remainingCharacters;
	private List<Integer> _controlledCharacters;
	private CharacterDependencyFormatter _formatter;
	private CharacterFormatter _characterFormatter = new CharacterFormatter(true, false, false, true);
	
	
	private JList stateList;
	private JList controlledCharacterList;
	private JList remainingCharacterList;
	private JButton moveRightButton;
	private JButton moveLeftButton;

	public ControllingAttributeEditor() {
		
		createUI();
		addEventHandlers();
	}
	
	private void addEventHandlers() {
		ActionMap actions = Application.getInstance().getContext().getActionMap(this);
		attributeCombo.addActionListener(actions.get("selectedAttributeChanged"));
		moveLeftButton.setAction(actions.get("moveToControlledList"));
		moveRightButton.setAction(actions.get("moveFromControlledList"));
		
		new ButtonEnabler(moveLeftButton, remainingCharacterList);
		new ButtonEnabler(moveRightButton, controlledCharacterList);
	}

	private void createUI() {
		JPanel controllingAttributes = new JPanel();
		JLabel lblControllingAttribute = new JLabel("Controlling attribute");
		
		attributeCombo = new JComboBox();
		attributeCombo.setRenderer(new ControllingAttributeRenderer());
		
		JLabel lblDefinedByStates = new JLabel("Defined by states:");
		
		JScrollPane stateListScroller = new JScrollPane();
		
		JButton btnRedefine = new JButton("Redefine");
		GroupLayout gl_controllingAttributes = new GroupLayout(controllingAttributes);
		gl_controllingAttributes.setHorizontalGroup(
			gl_controllingAttributes.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controllingAttributes.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_controllingAttributes.createParallelGroup(Alignment.LEADING)
						.addComponent(attributeCombo, Alignment.TRAILING, 0, 367, Short.MAX_VALUE)
						.addGroup(gl_controllingAttributes.createSequentialGroup()
							.addComponent(lblDefinedByStates)
							.addPreferredGap(ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
							.addComponent(btnRedefine))
						.addComponent(stateListScroller, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
						.addComponent(lblControllingAttribute))
					.addContainerGap())
		);
		gl_controllingAttributes.setVerticalGroup(
			gl_controllingAttributes.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controllingAttributes.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblControllingAttribute)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(attributeCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_controllingAttributes.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDefinedByStates)
						.addComponent(btnRedefine))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(stateListScroller, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		stateList = new JList();
		stateListScroller.setViewportView(stateList);
		controllingAttributes.setLayout(gl_controllingAttributes);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(controllingAttributes, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(controllingAttributes, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblNewLabel = new JLabel("Makes inapplicable:");
		
		JScrollPane scrollPane = new JScrollPane();
		
		moveRightButton = new JButton("");
		
		moveLeftButton = new JButton("");
		
		JLabel lblNewLabel_1 = new JLabel("Character list:");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(moveLeftButton)
								.addComponent(moveRightButton)))
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(72)
							.addComponent(moveRightButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(moveLeftButton)))
					.addContainerGap())
		);
		
		remainingCharacterList = new JList();
		remainingCharacterList.setCellRenderer(new CharacterListRenderer());
		scrollPane_1.setViewportView(remainingCharacterList);
		
		controlledCharacterList = new JList();
		controlledCharacterList.setCellRenderer(new CharacterListRenderer());
		scrollPane.setViewportView(controlledCharacterList);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
	}
	
	public void bind(EditorViewModel model, Character character) {
		_model = model;
		_formatter = new CharacterDependencyFormatter(_model);
	
		if (character.getCharacterType().isMultistate()) {
			_character = (MultiStateCharacter)character;
			attributeCombo.setModel(new ControllingAttributeModel());
			
			_remainingCharacters = new ArrayList<Integer>(_model.getNumberOfCharacters());
			for (int i=1; i<=_model.getNumberOfCharacters(); i++) {
				if (i != _character.getCharacterId()) {
					_remainingCharacters.add(i);
				}
			}
			
			if (attributeCombo.getItemCount() > 0) {
				attributeCombo.setSelectedItem(attributeCombo.getItemAt(0));
			}
			
		}
		else {
			_character = null;
			attributeCombo.setModel(new DefaultComboBoxModel());
			_controlledCharacters = new ArrayList<Integer>(0);
		}	
	}
	
	@Action
	public void selectedAttributeChanged() { 
		_controllingAttribute = (CharacterDependency)attributeCombo.getSelectedItem();
		_controlledCharacters = new ArrayList<Integer>(_controllingAttribute.getDependentCharacterIds());
		_remainingCharacters.removeAll(_controllingAttribute.getDependentCharacterIds());
		
		Collections.sort(_controlledCharacters);
		updateScreen();
	}
	
	@Action
	public void moveToControlledList() {
		Object[] selectedDependencies = remainingCharacterList.getSelectedValues();
		
		for (Object selected : selectedDependencies) {
			Character dependent = (Character)selected;
			_controllingAttribute.addDependentCharacter(dependent);
		}
		
		selectedAttributeChanged();
	}
	
	@Action
	public void moveFromControlledList() {
		Object[] selectedDependencies = controlledCharacterList.getSelectedValues();
		
		for (Object selected : selectedDependencies) {
			Character dependent = (Character)selected;
			_controllingAttribute.removeDependentCharacter(dependent);
		}
		
		selectedAttributeChanged();
	}
	
	
	
	private void updateScreen() {
		if (_controllingAttribute != null) {
			
			stateList.setModel(new StateListModel());
			stateList.setCellRenderer(new StateListRenderer());
			
			controlledCharacterList.setModel(new CharacterListModel(_controlledCharacters));
			remainingCharacterList.setModel(new CharacterListModel(_remainingCharacters));
		}
	}
	
	class ControllingAttributeModel extends AbstractListModel implements ComboBoxModel {

		private static final long serialVersionUID = -9004809838787455121L;
		private Object _selected;
		private List<CharacterDependency> _controllingAttributes;
		
		public ControllingAttributeModel() {
			_controllingAttributes = _character.getDependentCharacters();
			if (_controllingAttributes.size() > 0) {
				_selected = _controllingAttributes.get(0);
			}
		}
		@Override
		public int getSize() {
			System.out.println(_character.getDependentCharacters().size());
			return _controllingAttributes.size();
		}

		@Override
		public Object getElementAt(int index) {
			return _controllingAttributes.get(index);
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
	
	class ControllingAttributeRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -3556946058778276619L;
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			CharacterDependency dependency = (CharacterDependency)value;
			String description = _formatter.formatCharacterDependency(dependency);
			
			return super.getListCellRendererComponent(list, description, index, isSelected, cellHasFocus);
		}
	}
	
	class StateListModel extends AbstractListModel {

		private static final long serialVersionUID = 1101093504477435463L;
		
		@Override
		public int getSize() {
			MultiStateCharacter character = (MultiStateCharacter)_character;
			return character.getNumberOfStates();
		}

		@Override
		public Object getElementAt(int index) {
			MultiStateCharacter character = (MultiStateCharacter)_character;
			return _characterFormatter.formatState(character, index+1);
		}
		
	}
	
	class StateListRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = -4761565917553288888L;

		private Font _defaultFont = UIManager.getFont("Label.font");
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Set<Integer> states = _controllingAttribute.getStates();
			
			if (states.contains(index+1)) {
				setFont(getFont().deriveFont(Font.BOLD));
			}
			else {
				setFont(_defaultFont);
			}
			
			return this;
		}
	}
	
	class CharacterListModel extends AbstractListModel {

		private static final long serialVersionUID = 6573565854830718124L;
		
		private List<Integer> _characters;
		
		public CharacterListModel(List<Integer> characters) {
			_characters = characters;
		}
		
		@Override
		public int getSize() {
			return _characters.size();
		}

		@Override
		public Object getElementAt(int index) {
			int characterNumber = _characters.get(index);
			return _model.getCharacter(characterNumber);
		}
	}
	
	class CharacterListRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 865677225829236016L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			String description = _characterFormatter.formatCharacterDescription((Character)value);
			return super.getListCellRendererComponent(list, description, index, isSelected, cellHasFocus);
		}
		
	}
}
