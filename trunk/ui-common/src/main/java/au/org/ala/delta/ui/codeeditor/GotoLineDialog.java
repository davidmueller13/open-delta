package au.org.ala.delta.ui.codeeditor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class GotoLineDialog extends BaseDialog {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	/** The text area. */
	private CodeTextArea textArea;

	/**
	 * Sets the current line number.
	 * 
	 * @param lineNumber
	 *            The line number to set.
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumberField.setText(Integer.toString(lineNumber + 1));
	}

	/**
	 * Constructs a GotoLineDialog instance with specific attributes.
	 * 
	 * @param textArea
	 *            The text area.
	 */
	public GotoLineDialog(CodeTextArea textArea) {
		super(textArea.getFrame(), false);
		this.textArea = textArea;
		setTitle("Goto line");
		initComponents();

		// defining key bindings
		InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");

		ActionMap actionMap = this.getRootPane().getActionMap();
		actionMap.put("escape", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});
		actionMap.put("enter", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		// centering dialog over text area
		centerDialog();
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		mainPanel = new JPanel();
		lineNumberLabel = new JLabel();
		lineNumberField = new JTextField();
		buttonPanel = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		addWindowListener(new WindowAdapter() {

			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		mainPanel.setLayout(new java.awt.GridBagLayout());

		lineNumberLabel.setText("Line number");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
		mainPanel.add(lineNumberLabel, gridBagConstraints);

		lineNumberField.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(java.awt.event.KeyEvent evt) {
				lineNumberFieldKeyReleased(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
		mainPanel.add(lineNumberField, gridBagConstraints);

		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(okButton);

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(cancelButton);

		getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

		pack();
	}

	private void lineNumberFieldKeyReleased(java.awt.event.KeyEvent evt) {
		checkLineNumberInput();
	}

	/**
	 * Checks if the input for the line number is valid.
	 * 
	 * @return true if the input is currently valid, else false.
	 */
	private boolean checkLineNumberInput() {
		String line = lineNumberField.getText();
		if (line.length() == 0) {
			lineNumberField.setForeground(Color.black);
			return true;
		}
		try {
			int lineNumber = Integer.parseInt(line);
			if ((lineNumber < 1) || (lineNumber > textArea.getLineCount())) {
				lineNumberField.setForeground(Color.red);
				return false;
			}
			lineNumberField.setForeground(Color.black);
		} catch (NumberFormatException ex) {
			lineNumberField.setForeground(Color.red);
			return false;
		}
		return true;
	}

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (!checkLineNumberInput()) {
			return;
		}
		String line = lineNumberField.getText();
		try {
			int lineNumber = Integer.parseInt(line);
			textArea.gotoLine(lineNumber - 1);
			this.setVisible(false);
		} catch (NumberFormatException ex) {
			lineNumberField.setForeground(Color.red);
		}
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.setVisible(false);
	}

	private JPanel buttonPanel;
	private JButton cancelButton;
	private JTextField lineNumberField;
	private JLabel lineNumberLabel;
	private JPanel mainPanel;
	private JButton okButton;

}
