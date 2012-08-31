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
package au.org.ala.delta.editor;

import au.org.ala.delta.editor.model.DeltaViewModel;
import au.org.ala.delta.editor.model.EditorDataModel;
import au.org.ala.delta.editor.ui.AquaInternalFrameMaximiseListener;
import au.org.ala.delta.editor.ui.InternalFrameDataModelListener;
import au.org.ala.delta.model.DeltaDataSetRepository;
import au.org.ala.delta.ui.MessageDialogHelper;
import au.org.ala.delta.ui.help.HelpController;
import org.jdesktop.application.ResourceMap;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DeltaViewControllers is responsible for managing a single instance of the EditorDataModel
 * and any associated views.
 */
public class DeltaViewController extends InternalFrameAdapter implements VetoableChangeListener{

	private static final String DELTA_FILE_EXTENSION = "dlt";
	
	private DeltaEditor _deltaEditor;
	
	/** The model this controller controls */
	private  EditorDataModel _dataSet;
	
	/** Used for saving the model */
	private DeltaDataSetRepository _repository;

	/** Keeps track of the active views of the model */
	private List<JInternalFrame> _activeViews;
	
	private String _newDataSetName;
	
	private String _closeWithoutSavingMessage;
	
	private String _unableToCloseMessage;
	
	private DeltaViewFactory _viewFactory;
	
	private Map<DeltaView, DeltaViewModel> _models;
	
	/** 
	 * Set while the closeAll method is being invoked, this flag modifies the behavior of 
	 * the close operations.
	 */
	private boolean _closingAll;

    private ResourceMap _resourceMap;

    private HelpController _helpController;

	/**
	 * Creates a new DeltaViewController.
	 * 
	 * @param dataSet The data set associated with the viewer
	 * @param deltaEditor Reference to the instance of DeltaEditor that created the viewer
     * @param repository used to help with save/save as actions.
     * @param helpController views register themselves with the help controller.
	 */
	public DeltaViewController(EditorDataModel dataSet, DeltaEditor deltaEditor, DeltaDataSetRepository repository, HelpController helpController) {
		_dataSet = dataSet;
		_deltaEditor = deltaEditor;
		_repository = repository;
        _helpController = helpController;
		_closingAll = false;
		_newDataSetName = "";
		_viewFactory = new DeltaViewFactory();
		_activeViews = new ArrayList<JInternalFrame>();
		_models = new HashMap<DeltaView, DeltaViewModel>();
		_observers = new ArrayList<DeltaViewStatusObserver>();
        _resourceMap = deltaEditor.getContext().getResourceMap(DeltaEditor.class);
	}

	public void setNewDataSetName(String newDataSetName) {
		_newDataSetName = newDataSetName;
	}
	
	public void setCloseWithoutSavingMessage(String windowClosingMessage) {
		_closeWithoutSavingMessage = windowClosingMessage;
	}
	
	public void setUnableToCloseMessage(String unableToCloseMessage) {
		_unableToCloseMessage = unableToCloseMessage;
	}

	/**
	 * If the last view of a modified model is about to be closed this method will
	 * intervene and ask the user if they want to save.
	 * If the user selects cancel, the view will not be closed.
	 */
	@Override
	public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {

		
		if (JInternalFrame.IS_CLOSED_PROPERTY.equals(e.getPropertyName()) && (e.getNewValue().equals(Boolean.TRUE))) {
			if (((DeltaView)e.getSource()).canClose()) {
				if (_activeViews.size() == 1) {
					boolean canClose = confirmClose();
					if (!canClose) {
						throw new PropertyVetoException("Close cancelled", e);
					}
				}
			}
			else {
				throw new PropertyVetoException("Close cancelled by view", e);
			}
		}
		else if (JInternalFrame.IS_SELECTED_PROPERTY.equals(e.getPropertyName())) {
            if (!(e.getSource() instanceof JInternalFrame)) {
                return;
            }
            JInternalFrame eventSource = (JInternalFrame)e.getSource();
            if (eventSource.getDesktopPane() == null) {
                // This is happening on close, let the close property change event handle it.
                return;
            }
            JInternalFrame selected = eventSource.getDesktopPane().getSelectedFrame();

            if (Boolean.TRUE.equals(e.getNewValue())) {
                if (selected != null && selected instanceof DeltaView) {
                    DeltaView view = (DeltaView)selected;
                    if (!view.editsValid()) {
                        selected.moveToFront();
                        throw new PropertyVetoException("Selected pane is invalid", e);
                    }
                }
            }
			if (Boolean.FALSE.equals(e.getNewValue())) {
                if (e.getSource() instanceof DeltaView) {
                    DeltaView view = (DeltaView)e.getSource();
                    if (!view.editsValid()) {
                        eventSource.moveToFront();
                        throw new PropertyVetoException("Selected pane is invalid", e);
                    }
                }
            }
		}
	}
	
	/**
	 * Notifies this controller there is a new view interested in the model.
	 * @param view the new view of the model.
	 */
	public void viewerOpened(DeltaView view, DeltaViewModel model) {
        view.registerHelp(_helpController);
		JInternalFrame frameView = (JInternalFrame)view;
		_activeViews.add(frameView);
		frameView.addVetoableChangeListener(this);
		frameView.addInternalFrameListener(this);

        _models.put(view, model);
		new InternalFrameDataModelListener(frameView, _dataSet, view.getViewTitle());
        workAroundAquaLookAndFeel(frameView);
	}

    private void workAroundAquaLookAndFeel(JInternalFrame frame) {

        if (DeltaEditor.isMac()) {
            frame.addPropertyChangeListener(new AquaInternalFrameMaximiseListener());
        }
    }

	/**
	 * Removes the view from the ones being tracked.
	 */
	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		JInternalFrame frame = e.getInternalFrame();
		if (!_closingAll) {
			_activeViews.remove(frame);
		}
		DeltaView view = (DeltaView)frame;
		DeltaViewModel model = _models.remove(view);
		_dataSet.removeDeltaDataSetObserver(model);
		_dataSet.removePreferenceChangeListener(model);
		fireViewClosed(view);
		
		if (_activeViews.size() == 0) {
			_dataSet.close();
		}
	}
	
	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		fireViewSelected((DeltaView)e.getInternalFrame());
	}

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        if (selectedViewModel() == null) {
            fireViewSelected(null);
        }
    }

	/**
	 * Asks the user whether they wish to save before closing.  If this method returns false
	 * the close will be aborted.
	 * @return true if the close can proceed.
	 */
	private boolean confirmClose() {
		if (_closingAll) {
			return true;
		}
		boolean canClose = true;
		if (_dataSet.isModified()) {
			String title = _dataSet.getName();
			if (title != null) {
				title = new File(title).getName();
			}
			else {
				title = newDataSetName();
			}
			int result = MessageDialogHelper.showConfirmDialog(_deltaEditor.getMainFrame(), title, _closeWithoutSavingMessage, 20);
			canClose = (result != JOptionPane.CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			}
		}
		return canClose;
	}
	
	/**
	 * First commits any outstanding edits, then saves the model.  The save will not happen if there are outstanding
     * validation errors.
	 */
	public void save() {
        if (!commitEditsAndValidate()) {
            return;
        }

        String name = _dataSet.getName();
        if (!new File(name).exists()) {
			saveAs();
		}
		else {
			_repository.save(_dataSet.getDeltaDataSet(), null);
			_dataSet.setModified(false);
		}
	}

    private boolean commitEditsAndValidate() {
        DeltaView activeView = (DeltaView)getCurrentView();

        if (activeView != null) {
            return activeView.editsValid();
        }
        return true;
    }

	/**
	 * Closes all views of the model, asking the user to save first if the model has been
	 * modified.
	 * @return true if the close proceeded.
	 */
	public boolean closeAll() {
		
		for (JInternalFrame view : _activeViews) {
			if (view instanceof DeltaView) {
				DeltaView dv = (DeltaView) view;
				if (!dv.canClose()) {
					JOptionPane.showMessageDialog(view, _unableToCloseMessage);
					return false;
				}								
			}
		}
		
		if (confirmClose()) {
			try {
				_closingAll = true;
				for (JInternalFrame view : _activeViews) {
					view.setClosed(true);
				}
				_activeViews.clear();
				_dataSet.close();
				return true;
			}
			catch (PropertyVetoException e) {}
			finally {
				_closingAll = false;
			}
		}
		return false;
	}
	
	
	/**
	 * Saves the model using a different name.
	 */
	public void saveAs() {

        if (!commitEditsAndValidate()) {
            return;
        }

		File newFile = _deltaEditor.selectFile(false);
		
		if (newFile != null) {

            if (!newFile.getName().endsWith("."+DELTA_FILE_EXTENSION)) {
                newFile = new File(newFile.getAbsolutePath()+"."+DELTA_FILE_EXTENSION);
            }

            if (newFile.exists()){
				JOptionPane.showMessageDialog(_deltaEditor.getMainFrame(), _resourceMap.getString("DeltaEditor.fileExists"));
				// Try again.
                saveAs();
			}
            else {

                _repository.saveAsName(_dataSet.getDeltaDataSet(), newFile.getAbsolutePath(), false, null);
                _dataSet.setName(_dataSet.getName());
                _dataSet.setModified(false);
                EditorPreferences.addFileToMRU(newFile.getAbsolutePath());
            }
			
		}
	}
	
	/**
	 * Returns true if this controller is managing the supplied view.
	 * @param view the view to check.
	 * @return true if this controller manages the view.
	 */
	public boolean controls(JInternalFrame view) {
		return _activeViews.contains(view);
	}
	
	
	private String newDataSetName() {
		return _newDataSetName;
	}
	
	public EditorDataModel getModel() {
		return _dataSet;
	}
	
	/**
	 * 
	 * @return the number of active views of the model being controlled by this controller.
	 */
	public int getViewCount() {
		if (_closingAll) {
			return 0;
		}
		return _activeViews.size();
	}
	

	public DeltaView createTreeView() {
		DeltaViewModel model = createViewModel();
		DeltaView view = _viewFactory.createTreeView(model);
		
		new TreeCharacterController(view.getCharacterListView(), model);
		new StateController(view.getStateListView(), model);
		new ItemController(view.getItemListView(), model);
		
		viewerOpened(view, model);
		
		return view;
	}
	
	public DeltaView createGridView() {
		DeltaViewModel model = createViewModel();
		DeltaView view = _viewFactory.createGridView(model);
		new CharacterController(view.getCharacterListView(), model);
		new ItemController(view.getItemListView(), model);
		viewerOpened(view, model);
		
		return view;
	}
	
	public DeltaView createItemEditView() {
		
		DeltaViewModel model = createViewModel();
		DeltaView view = _viewFactory.createItemEditView(model, getCurrentView());
		viewerOpened(view, model);
		
		return view;
	}
	
	private JInternalFrame getCurrentView() {
		for (JInternalFrame frame : _activeViews) {
			if (frame.isSelected()) {
				return frame;
			}
		}
		return null;
	}
	
	public DeltaView createCharacterEditView() {		
		DeltaViewModel model = createViewModel();		
		DeltaView view = _viewFactory.createCharacterEditView(model, getCurrentView());
		viewerOpened(view, model);
		
		return view;
	}
	
	public DeltaView createImageEditorView() {
		DeltaViewModel model = createViewModel();
		DeltaView view = _viewFactory.createImageEditorView(model);
		viewerOpened(view, model);
		
		return view;
	}
	
	public DeltaView createDirectivesEditorView() {
		DeltaViewModel model = createViewModel();
		DeltaView view = _viewFactory.createDirectivesEditorView(model);
		viewerOpened(view, model);
		
		return view;
	}
	
	public DeltaView createActionSetsView() {
		DeltaViewModel model = createViewModel();
		DeltaView view = _viewFactory.createActionSetsView(model);
		viewerOpened(view, model);
		
		return view;
	}

    public DeltaView createImageSettingsView() {
        DeltaViewModel model = createViewModel();
        DeltaView view = _viewFactory.createImageSettingsView(model);
        viewerOpened(view, model);

        return view;
    }

	private DeltaViewModel createViewModel() {
		
		DeltaViewModel model = new DeltaViewModel(_dataSet);
		DeltaViewModel selectedModel = selectedViewModel();
		if (selectedModel != null) {
			model.setSelectedCharacter(selectedModel.getSelectedCharacter());
			model.setSelectedItem(selectedModel.getSelectedItem());
			model.setSelectedState(selectedModel.getSelectedState());
			model.setSelectedImage(selectedModel.getSelectedImage());
			model.setSelectedDirectiveFile(selectedModel.getSelectedDirectiveFile());
		}
		return model;
	}
	
	private DeltaViewModel selectedViewModel() {
		for (JInternalFrame view : _activeViews) {
			if (view.isSelected()) {
				DeltaView deltaView = (DeltaView)view;
				return _models.get(deltaView);
			}
		}
		return null;
	}
	
	private List<DeltaViewStatusObserver> _observers;
	public void addDeltaViewStatusObserver(DeltaViewStatusObserver observer) {
		_observers.add(observer);
	}
	
	
	protected void fireViewClosed(DeltaView view) {
		for (int i=_observers.size()-1; i>=0; i--) {
			_observers.get(i).viewClosed(this, view);
		}
	}
	
	protected void fireViewSelected(DeltaView view) {
		for (int i=_observers.size()-1; i>=0; i--) {
			_observers.get(i).viewSelected(this, view);
		}
	}
	
}
 
