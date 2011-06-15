package au.org.ala.delta.editor;

import au.org.ala.delta.editor.ui.ReorderableList;

/**
 * This interface should be implemented by views of a DeltaDataSet.
 * It's purpose is to allow a view to be implemented as a tab or internal frame. (and
 * potentially to allow mocking of a view in unit tests).
 */
public interface DeltaView {

	public String getViewTitle();
	public void open();
	public boolean editsValid();
	
	public ReorderableList getCharacterListView();
	public ReorderableList getItemListView();
}
