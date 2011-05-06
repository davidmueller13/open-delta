package au.org.ala.delta.editor.ui.validator;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import au.org.ala.delta.ui.util.IconHelper;

/**
 * Provides basic indications on a JTextComponent that the component's text is invalid.
 * These include a popup with an error message and modifying the background / foreground colours
 * of the text.
 * The actual validation is delegated to an instance of Validator which much be provided to this
 * class on creation.
 */
public class TextComponentValidator extends InputVerifier {

	/**
	 * Responsible for hiding the popup when a key is pressed.
	 */
	class ErrorDisplayHider extends KeyAdapter implements FocusListener, ComponentListener {

		private JComponent _component;
		public ErrorDisplayHider(JComponent component) {
			_component = component;
			_component.addFocusListener(this);
		}
		@Override
		public void keyPressed(KeyEvent e) {
			cleanup();
		}
		
		private void cleanup() {
			try {
				_errorMessageDisplay.setVisible(false);
			}
			finally {
				_component.removeKeyListener(this);
				_component.removeFocusListener(this);
			}
		}
		
		@Override
		public void focusGained(FocusEvent e) {}
		
		@Override
		public void focusLost(FocusEvent e) {
			cleanup();
		}
		@Override
		public void componentResized(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentHidden(ComponentEvent e) {
			cleanup();
		}
	}
	
	
	/**
	 * An undecorated JDialog that displays an error icon and a supplied message.
	 */
	public class ErrorDisplay extends JDialog {

		private static final long serialVersionUID = -706192271224257836L;
		private JLabel _messageLabel;

		public ErrorDisplay() {
			setUndecorated(true);
			_messageLabel = new JLabel(IconHelper.createImageIcon("error.png"));
			_messageLabel.setBackground(UIManager.getColor("ToolTip.background"));
			_messageLabel.setForeground(UIManager.getColor("ToolTip.foreground"));
			_messageLabel.setFont(UIManager.getFont("ToolTip.font"));
			_messageLabel.setBorder(BorderFactory.createLineBorder(UIManager.getColor("ToolTip.foreground")));
			_messageLabel.setOpaque(true);
			
			getContentPane().add(_messageLabel);
			setFocusableWindowState(false);
		}

		public void setMessage(String message) {
			_messageLabel.setText(message);
			setSize(0, 0);
			pack();
		}

		/**
		 * Convenience method to display this dialog immediately above the specified component.
		 * @param component the component that determines where this dialog should be shown.
		 */
		public void showAbove(JComponent component) {

			if (component.isShowing()) {
				Point p = component.getLocationOnScreen();
				Dimension size = _messageLabel.getPreferredSize();
				setLocation(p.x, p.y-size.height);
				
				setVisible(true);
			}
			else {
				setVisible(false);
			}
		}

	}

	/** Used to display a popup that indicates the text is invalid */
	private ErrorDisplay _errorMessageDisplay;
	/** The class that contains the validation logic */
	private Validator _validator;
	/** An object interested in hearing about validation events */
	private ValidationListener _listener;
	
	
	public TextComponentValidator(Validator validator) {
		this(validator, null);
	}
	
	/**
	 * Creates a new TextComponentValidator that will notify the supplied ValidationListener when
	 * a validation is performed.
	 * @param validator implements the validation logic.
	 * @param listener an object interested in hearing about the results of a validation.
	 */
	public TextComponentValidator(Validator validator, ValidationListener listener) {
		_validator = validator;
		_listener = listener;
		_errorMessageDisplay = new ErrorDisplay();
		
	}
	
	/**
	 * Called when focus is lost on the supplied component - this method invokes the validation routine.
	 */
	@Override
	public boolean verify(JComponent component) {
		if (!(component instanceof JTextComponent)) {
			return true;
		}
		
		return validate(component);
	}

	/**
	 * Validates the component and acts on the result.
	 * @param component the component to validate.

	 * @return true if validation succeeded.
	 */
	private boolean validate(JComponent component) {
		boolean valid = false;
		Object value = getValueToValidate((JTextComponent)component);
			
		ValidationResult result = _validator.validate(value);
		updateTextStyles((JTextComponent)component, result);
		if (!result.isValid()) {
			_errorMessageDisplay.setMessage(result.getMessage());
			_errorMessageDisplay.showAbove(component);
			ErrorDisplayHider hider = new ErrorDisplayHider(component);
			component.addKeyListener(hider);
			
			if (_listener != null) {
				_listener.validationFailed(result);
			}
		}
		else {
			if (_listener != null) {
				_listener.validationSuceeded(result);
			}
			valid = true;
		}
		return valid;
	}
	
	/**
	 * Retrieves the value to validate from the supplied component.
	 * @param component
	 * @return
	 */
	public Object getValueToValidate(JComponent component) {
		String text = null;
		if (component instanceof JTextComponent) {
			text = ((JTextComponent)component).getText();
		}
		return text;
	}
	
	private void updateTextStyles(JTextComponent component, ValidationResult validationResult) {
		if (!validationResult.isValid()) {
			component.select(validationResult.getInvalidCharacterPosition(), component.getDocument().getLength());
		}
	}

}
