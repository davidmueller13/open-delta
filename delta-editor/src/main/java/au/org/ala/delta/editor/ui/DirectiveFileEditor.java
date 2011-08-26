package au.org.ala.delta.editor.ui;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JInternalFrame;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.DeltaView;
import au.org.ala.delta.editor.directives.ExportController;
import au.org.ala.delta.editor.directives.ImportController;
import au.org.ala.delta.editor.directives.ImportExportStatus;
import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.slotfile.directive.DirectiveInOutState;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.ui.validator.ValidationListener;
import au.org.ala.delta.editor.ui.validator.ValidationResult;
import au.org.ala.delta.ui.codeeditor.CodeEditor;

/**
 * Provides a user interface that allows directive files to be edited.
 */
public class DirectiveFileEditor extends JInternalFrame implements ValidationListener, DeltaView {

	private static final long serialVersionUID = 9193388605723396077L;

	/** Contains the directive file are editing */
	private EditorViewModel _model;

	private ResourceMap _resources;

	private ActionMap _actions;

	private CodeEditor directivesEditor;

	public DirectiveFileEditor(EditorViewModel model) {
		super();
		setName("ItemEditorDialog");
		_model = model;
		_resources = Application.getInstance().getContext().getResourceMap(DirectiveFileEditor.class);
		_actions = Application.getInstance().getContext().getActionMap(this);

		createUI();
		updateGUI();
	}

	private void createUI() {
		directivesEditor = new CodeEditor(getMimeType());
		directivesEditor.getTextArea().setEOLMarkersPainted(false);
		getContentPane().add(directivesEditor, BorderLayout.CENTER);
		JButton apply = new JButton("Apply");
		apply.setAction(_actions.get("applyChanges"));
		getContentPane().add(apply, BorderLayout.SOUTH);
		
	}

	private String getMimeType() {
		DirectiveFile file = _model.getSelectedDirectiveFile();
		String mimeType;
		switch (file.getType()) {
		case CONFOR:
			mimeType = "text/confor";
			break;
		default:
			mimeType = "text/plain";
			break;
		}
		return mimeType;
	}

	private void updateGUI() {
		DirectiveFile file = _model.getSelectedDirectiveFile();
		ExportController ec = new ExportController((DeltaEditor) Application.getInstance());
		DirectiveInOutState state = new DirectiveInOutState(_model);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(out);
		state.setPrintStream(p);
		ec.writeDirectivesFile(file, state);

		if (file.isCharsFile() || file.isItemsFile() || file.isSpecsFile()) {
			directivesEditor.getTextArea().setEditable(false);
		}
		directivesEditor.setText(new String(out.toByteArray()));
	}

	@Override
	public String getViewTitle() {
		return _model.getSelectedDirectiveFile().getShortFileName();
	}

	@Override
	public void open() {

	}

	@Action
	public void applyChanges() {
		ImportController controller = new ImportController((DeltaEditor) Application.getInstance(), _model);
	
		String text = directivesEditor.getTextArea().getText();
		ImportExportStatus status = new ImportExportStatus();
		controller.importDirectivesFile(
				_model.getSelectedDirectiveFile(), new StringReader(text), status);
		
		updateGUI();
	}
	
	@Override
	public boolean editsValid() {

		return false;
	}

	@Override
	public ReorderableList getCharacterListView() {
		return null;
	}

	@Override
	public ReorderableList getItemListView() {
		return null;
	}

	@Override
	public void validationSuceeded(ValidationResult results) {

	}

	@Override
	public void validationFailed(ValidationResult results) {

	}

}
