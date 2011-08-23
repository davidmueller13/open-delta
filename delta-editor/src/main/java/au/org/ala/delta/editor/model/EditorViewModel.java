package au.org.ala.delta.editor.model;

import java.util.prefs.PreferenceChangeListener;

import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.ObservableDeltaDataSet;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageSettings;

public interface EditorViewModel extends ObservableDeltaDataSet {

	public void setSelectedItem(Item selectedItem);

	public void setSelectedCharacter(Character selectedCharacter);

	public Item getSelectedItem();

	public Character getSelectedCharacter();

	public void deleteItem(Item item);

	public String getName();

	public String getShortName();

	public void setName(String name);

	public void close();
	
	public void addPreferenceChangeListener(PreferenceChangeListener listener); 
	
	public void removePreferenceChangeListener(PreferenceChangeListener listener);

	public abstract String getImagePath();
	
	public ImageSettings getImageSettings();

	public void setSelectedState(int stateNo);
	
	public int getSelectedState();

	void setSelectedImage(Image image);

	Image getSelectedImage();

	String getDataSetPath();
	
	public int getDirectiveFileCount();
	
	public DirectiveFile getDirectiveFile(int directiveFileNumber);
}