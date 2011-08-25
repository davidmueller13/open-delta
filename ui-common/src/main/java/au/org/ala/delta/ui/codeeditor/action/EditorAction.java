package au.org.ala.delta.ui.codeeditor.action;

import javax.swing.AbstractAction;

import au.org.ala.delta.ui.codeeditor.CodeEditor;

public abstract class EditorAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	/** The text editor. */
	protected CodeEditor codeEditor;

	/**
	 * Constructs a EditorAction instance with specific arguments.
	 * 
	 * @param textArea
	 *            The text area.
	 */
	public EditorAction(CodeEditor textArea) {
		this.codeEditor = textArea;
	}

}
