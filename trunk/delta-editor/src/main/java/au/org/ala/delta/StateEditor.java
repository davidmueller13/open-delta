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
package au.org.ala.delta;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import au.org.ala.delta.gui.rtf.RtfEditor;
import au.org.ala.delta.gui.validator.AttributeValidator;
import au.org.ala.delta.gui.validator.RtfEditorValidator;
import au.org.ala.delta.gui.validator.ValidationListener;
import au.org.ala.delta.gui.validator.ValidationResult;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.StateValue;

public class StateEditor extends JPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;

	private DeltaContext _context;
	private RtfEditor _textPane;
	private JList _list;
	private boolean _valid = true;

	private Character _character;
	private Item _item;

	/** Tracks whether the attribute has been modified since it was displayed */
	private boolean _modified;

	public StateEditor(DeltaContext context) {
		_context = context;
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 150));
		JSplitPane split = new JSplitPane();

		_textPane = new RtfEditor();
		_list = new JList();

		split.setLeftComponent(_textPane);
		split.setRightComponent(_list);

		split.setDividerLocation(300);
		split.setResizeWeight(0.5);

		JToolBar toolbar = _textPane.buildAndInstallToolbar();
		add(toolbar, BorderLayout.NORTH);

		_textPane.getDocument().addDocumentListener(new EditListener());

		_textPane.addFocusListener(new EditCommitter() );
		
		add(split, BorderLayout.CENTER);
	}

	public void bind(Character ch, Item item) {
		if (!_valid) {
			return;
		}
		_character = ch;
		_item = item;
		if (ch != null && item != null) {
			final StateValue sv = _context.getMatrix().getValue(_character.getCharacterId(), _item.getItemId());
			if (sv != null) {
				String str = sv.getValue();
				if (!str.startsWith("{\\rtf1")) {
					str = String.format("{\\rtf1\\ansi\\ansicpg1252 %s }", sv.getValue());
				}

				_textPane.setText(str);
			} else {
				_textPane.setText("");
			}

			if (ch instanceof MultiStateCharacter) {
				MultiStateCharacter mc = (MultiStateCharacter) ch;
				_list.setModel(new StateListModel(mc.getStates()));
				_list.setCellRenderer(new StateRenderer());
			} else {
				_list.setModel(new DefaultListModel());
				_list.setCellRenderer(new DefaultListCellRenderer());
			}
			AttributeValidator validator = new AttributeValidator(_item, _character);
			RtfEditorValidator rtfValidator = new RtfEditorValidator(validator, this);
			_textPane.setInputVerifier(rtfValidator);
		}
		else {
			_textPane.setInputVerifier(null);
			
		}
		_modified = false;
	}
	
	
	@Override
	public void validationSuceeded(ValidationResult results) {
		_valid = true;
		
	}


	@Override
	public void validationFailed(ValidationResult results) {
		_valid = false;
	}



	class EditListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			_modified = true;
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			_modified = true;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			_modified = true;
		}
	}
	
	/**
	 * Edits are committed on focus lost events - hence we validate at this
	 * point. TODO a failed validate prevents focus transferal but it doesn't
	 * prevent a selection on the table or list from updating the text in the
	 * document!
	 * 
	 * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
	 */
	class EditCommitter extends FocusAdapter {

		@Override
		public void focusLost(FocusEvent e) {
			if (!_modified || !_valid) {
				return;
			}
			String attributeText = _textPane.getRtfTextBody();
			
			_item.getAttribute(_character).setValue(attributeText);
			_modified = false;
		}
		
	}
	

	class StateRenderer extends DefaultListCellRenderer {

		private JCheckBox stateRenderer = new JCheckBox();

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.DefaultListCellRenderer#getListCellRendererComponent(
		 * javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			stateRenderer.setBackground(getBackground());
			stateRenderer.setForeground(getForeground());
			stateRenderer.setText(value.toString());
			stateRenderer.setSelected(false);
			if (_item != null) {
				Attribute attribute = _item.getAttribute(_character);
				if (attribute != null) {
					stateRenderer.setSelected(attribute.isPresent(index));
				}
			}

			return stateRenderer;
		}

	}
}

class StateListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;

	private String[] _states;

	public StateListModel(String[] states) {
		_states = states;
	}

	@Override
	public int getSize() {
		return _states.length;
	}

	@Override
	public Object getElementAt(int index) {
		return String.format("%d. %s", index + 1, _states[index]);
	}

}
