package au.org.ala.delta.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import au.org.ala.delta.editor.EditorPreferences;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile.DirectiveType;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSet;
import au.org.ala.delta.model.AbstractObservableDataSet;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.ObservableDeltaDataSet;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.model.observer.DeltaDataSetChangeEvent;


/**
 * Provides the UI model with a backing DeltaDataSet.  Each EditorDataModel is associated with a 
 * single view component.  This class maintains a separate list of DeltaDataSetObservers 
 * to allow clean removal of listeners from the backing DeltaDataSet when a view of the model is 
 * closed. 
 */
public class EditorDataModel extends DataSetWrapper implements EditorViewModel, PreferenceChangeListener {

	/** The number of the currently selected character */
	private Character _selectedCharacter;
	
	/** The number of the currently selected item */
	private Item _selectedItem;
	
	/** The number of the selected state.  Only valid when the selected
	 * character is a multistate character (otherwise it's -1).
	 */
	private int _selectedState;
	
	/** the currently selected image */ 
	private Image _selectedImage;
	
	/** Helper class for notifying interested parties of property changes */
	private PropertyChangeSupport _propertyChangeSupport;
	
	private List<PreferenceChangeListener> _preferenceChangeListeners;
	
	/** Keeps track of whether this data set has been modified */
	private boolean _modified;
	
	public EditorDataModel(AbstractObservableDataSet dataSet) {
		super(dataSet);
		_propertyChangeSupport = new PropertyChangeSupport(this);
		_preferenceChangeListeners = new ArrayList<PreferenceChangeListener>();
		_selectedState = -1;
		_selectedCharacter = null;
		_selectedItem = null;
		EditorPreferences.addPreferencesChangeListener(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		_propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		_propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	
	@Override
	public void setSelectedItem(Item selectedItem) {
		_selectedItem = selectedItem;
	}
	
	
	@Override
	public void setSelectedCharacter(Character selectedCharacter) {
		if (_selectedCharacter == null || selectedCharacter == null || 
				!_selectedCharacter.equals(selectedCharacter)) {
			_selectedState = -1;
		}
		
		_selectedCharacter = selectedCharacter;
	}
	
	@Override
	public void setSelectedState(int state) {
		if (!(_selectedCharacter instanceof MultiStateCharacter)) {
			_selectedState = -1;
		}
		_selectedState = state;
	}
	
	@Override
	public int getSelectedState() {
		return _selectedState;
	}
	
	@Override
	public Item getSelectedItem() {
		return _selectedItem;
	}
	
	
	@Override
	public Character getSelectedCharacter() {
		return _selectedCharacter;
	}
	
	@Override
	public Image getSelectedImage() {
		return _selectedImage;
	}
	
	@Override
	public void setSelectedImage(Image image) {
		_selectedImage = image;
	}
	
	public ObservableDeltaDataSet getDeltaDataSet() {
		return _wrappedDataSet;
	}
	
	@Override
	public void deleteItem(Item item) {
		_wrappedDataSet.deleteItem(item);
		if (_selectedItem != null && _selectedItem.equals(item)) {
			_selectedItem = null;
		}
	}
	
	@Override
	public String getName() {
		String name = _wrappedDataSet.getName();
		if (name == null) {
			name = "";
		}
		return name;
	}
	
	@Override
	public String getShortName() {
		String name = _wrappedDataSet.getName();
		if (name == null) {
			name = "";
		}
		name = new File(name).getName();
		return name;
	}
	
	@Override
	public String getImagePath() {
		if (getName().equals("")) {
			return "";
		}
		
		String imagePath = getImageSettings().getImagePath();
		return imagePath;
	}
	
	
	@Override
	public ImageSettings getImageSettings() {
		ImageSettings settings = _wrappedDataSet.getImageSettings();
		if (settings == null) {
			settings = new ImageSettings();
		}
		settings.setDataSetPath(getDataSetPath());
		return settings;
	}

	@Override
	public String getDataSetPath() {
		File name = new File(getName());
		String dataSetFolder = "";
		if (name.isAbsolute()) {
			dataSetFolder = name.getParent()+File.separator;
		}
		return dataSetFolder;
	}
	
	@Override
	public void setName(String name) {
		
		_wrappedDataSet.setName(name);
		_propertyChangeSupport.firePropertyChange("name", null, name);
	}

	@Override
	public void close() {
		EditorPreferences.removePreferenceChangeListener(this);
		_wrappedDataSet.removeDeltaDataSetObserver(this);
		_wrappedDataSet.close();
	}
	
	public DirectiveFile addDirectiveFile(int fileNumber, String fileName, DirectiveType type) {
		return ((SlotFileDataSet)_wrappedDataSet).addDirectiveFile(fileNumber, fileName, type);
	}
	
	public int getDirectiveFileCount() {
		return ((SlotFileDataSet)_wrappedDataSet).getDirectiveFileCount();
	}
	
	public DirectiveFile getDirectiveFile(int fileNumber) {
		return ((SlotFileDataSet)_wrappedDataSet).getDirectiveFile(fileNumber);
	}
	
	public boolean isModified() {
		return _wrappedDataSet.isModified();
	}
	
	public void setModified(boolean modified) {
		
		if (modified != _modified) {
			_propertyChangeSupport.firePropertyChange("modified", _modified, modified);
		}
		_modified = modified;
	}
	
	public void addPreferenceChangeListener(PreferenceChangeListener listener) {
		_preferenceChangeListeners.add(listener);
	}
	
	public void removePreferenceChangeListener(PreferenceChangeListener listener) {
		_preferenceChangeListeners.remove(listener);
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent evt) {
		for (PreferenceChangeListener listener : _preferenceChangeListeners) {
			listener.preferenceChange(evt);
		}
	}

	@Override
	public void itemAdded(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.itemAdded(event);
	}

	@Override
	public void itemDeleted(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.itemDeleted(event);
	}

	@Override
	public void itemMoved(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.itemMoved(event);
	}

	@Override
	public void itemEdited(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.itemEdited(event);
	}

	@Override
	public void characterAdded(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.characterAdded(event);
	}

	@Override
	public void characterDeleted(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.characterDeleted(event);
	}

	@Override
	public void characterMoved(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.characterMoved(event);
	}

	@Override
	public void characterEdited(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.characterEdited(event);
	}

	@Override
	public void imageEdited(DeltaDataSetChangeEvent event) {
		setModified(true);
		super.imageEdited(event);
	}
	
	

}
