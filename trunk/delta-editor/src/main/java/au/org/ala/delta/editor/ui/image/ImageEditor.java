package au.org.ala.delta.editor.ui.image;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GraphicsDevice;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.DeltaView;
import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.ui.ReorderableList;
import au.org.ala.delta.editor.ui.util.MenuBuilder;
import au.org.ala.delta.editor.ui.util.MessageDialogHelper;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Illustratable;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericCharacter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.OverlayType;
import au.org.ala.delta.model.observer.AbstractDataSetObserver;
import au.org.ala.delta.model.observer.DeltaDataSetChangeEvent;
import au.org.ala.delta.ui.RichTextDialog;
import au.org.ala.delta.ui.image.AboutImageDialog;
import au.org.ala.delta.ui.image.AudioPlayer;
import au.org.ala.delta.ui.image.ImagePanel.ScalingMode;
import au.org.ala.delta.ui.image.OverlaySelectionObserver;
import au.org.ala.delta.ui.image.SelectableOverlay;
import au.org.ala.delta.util.DataSetHelper;

/**
 * Displays Character and Taxon images and allows the addition of 
 * ImageOverlays to the Image to assist with IntKey identifications.
 */
public class ImageEditor extends JInternalFrame implements DeltaView {

	private static final long serialVersionUID = 4867008707368683722L;

	private Image _selectedImage;
	private Illustratable _subject;
	private ActionMap _actionMap;
	private CardLayout _layout;
	private EditorViewModel _model;
	private List<Image> _images;
	private JMenu _subjectMenu;
	private ScalingMode _scalingMode;
	private JPanel _contentPanel;
	private boolean _hideHotSpots;
	private boolean _hideTextOverlays;
	private boolean _previewMode;
	private Map<String, ImageEditorPanel> _imageEditors;
	private DataSetHelper _helper;
	private PreviewController _previewController;
	private DeltaEditor editor;
	private MessageDialogHelper _messageHelper;
	
	public ImageEditor(EditorViewModel model) {

		_model = model;
		_model.addDeltaDataSetObserver(new ImageEditListener());
		editor = (DeltaEditor)Application.getInstance();
		_helper = new DataSetHelper(model);
		_actionMap = Application.getInstance().getContext().getActionMap(this);
		_layout = new CardLayout();
		_contentPanel = new JPanel();
		_contentPanel.setLayout(_layout);
		_scalingMode = ScalingMode.FIXED_ASPECT_RATIO;
		_hideHotSpots = false;
		_hideTextOverlays = false;
		_previewMode = false;
		_previewController = new PreviewController();
		_messageHelper = new MessageDialogHelper();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(_contentPanel, BorderLayout.CENTER);
		
		_subjectMenu = new JMenu();
		_subjectMenu.setName("subjectMenu");
		
		Image image =  model.getSelectedImage();
		Illustratable subject = image.getSubject();
		if (subject instanceof Character) {
			displaySubject((Character)subject, image);
		}
		else {
			displaySubject((Item)subject, image);
		}
		
		_actionMap.get("replayVideo").setEnabled(false);
		setName("ImageEditor-"+_subject.toString());
		
		buildMenus();
	}
	
	private void buildMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.add(_subjectMenu);
		menuBar.add(buildControlMenu());
		menuBar.add(buildWindowMenu());
		
		if (_subject.getImages().size() <= 1) {
			_actionMap.get("nextImage").setEnabled(false);
			_actionMap.get("previousImage").setEnabled(false);
		}
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * Creates an ImageEditorPanel for the supplied Image and adds it to the
	 * layout.
	 * @param image the image to add.
	 */
	private void addCardFor(Image image) {
		ImageEditorPanel viewer = new ImageEditorPanel(image, _model);
		String text = image.getSubjectTextOrFileName();
		
		_imageEditors.put(text, viewer);
		
		_contentPanel.add(viewer, text);	
	}
	
	/**
	 * Builds and returns the Subject menu.
	 * 
	 * @return a new JMenu ready to be added to the menu bar.
	 */
	private void buildSubjectMenu() {
		_subjectMenu.removeAll();
		ButtonGroup group = new ButtonGroup();
		for (final Image image : _images) {
			String text = image.getSubjectTextOrFileName();
			JMenuItem subject = new JCheckBoxMenuItem(text);
			group.add(subject);
			subject.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					displayImage(image);
				}
			});
			if (image.equals(_selectedImage)) {
				subject.setSelected(true);
			}
			_subjectMenu.add(subject);
		}
	}
	
	/**
	 * Builds and returns the Control menu.
	 * 
	 * @return a new JMenu ready to be added to the menu bar.
	 */
	private JMenu buildControlMenu() {
		JMenu controlMenu = new JMenu();
		controlMenu.setName("controlMenu");
		
		List<String> controlMenuActions = new ArrayList<String>();
		controlMenuActions.add("nextImage");
		controlMenuActions.add("previousImage");
		controlMenuActions.add("-");
		if (_selectedImage.getSubject() instanceof Item) {
			controlMenuActions.add("nextItemWithImage");
			controlMenuActions.add("previousItemWithImage");
			controlMenuActions.add("showImageNotes");
		}
		else {
			controlMenuActions.add("nextCharacterWithImage");
			controlMenuActions.add("previousCharacterWithImage");
			controlMenuActions.add("-");
			controlMenuActions.add("showCharacterDetails");
			controlMenuActions.add("showCharacterNotes");
		}
		
		MenuBuilder.buildMenu(controlMenu, controlMenuActions.toArray(new String[0]), _actionMap);

		return controlMenu;
	}
	
	
	/**
	 * Builds and returns the View menu.
	 * 
	 * @return a new JMenu ready to be added to the menu bar.
	 */
	private JMenu buildWindowMenu() {
		JMenu windowMenu = new JMenu();
		windowMenu.setName("windowMenu");

		String[] windowMenuActions = { 
				"*toggleScaling", "*toggleHideText", "*toggleHideHotSpots", 
				"replaySound", "replayVideo", "-", 
				"reloadImage", "fitToImage", "fullScreen", "-",
				"*togglePreviewMode", "-",
				"aboutImage", "-",
				"closeImage"};

		JMenuItem[] items = MenuBuilder.buildMenu(windowMenu, windowMenuActions, _actionMap);
		((JCheckBoxMenuItem)items[0]).setSelected(true);
		
		return windowMenu;
	}
	
	@Override
	public String getViewTitle() {
		return _selectedImage.getFileName();
	}

	@Override
	public void open() {}

	@Override
	public boolean editsValid() {
		return true;
	}

	@Override
	public ReorderableList getCharacterListView() {
		return null;
	}

	@Override
	public ReorderableList getItemListView() {
		return null;
	}
	
	private void displaySubject(Character subject, Image image) {
		displaySubject((Illustratable)subject, image);
		
		Character character = _helper.getNextCharacterWithImage((Character)_subject);
		_actionMap.get("nextCharacterWithImage").setEnabled(character != null);
		
		character = _helper.getPreviousCharacterWithImage((Character)_subject);
		_actionMap.get("previousCharacterWithImage").setEnabled(character != null);
		
		_actionMap.get("showCharacterNotes").setEnabled(subject.hasNotes());
	}
	
	private void displaySubject(Item subject, Image image) {
		displaySubject((Illustratable)subject, image);
		
		Item item = _helper.getNextItemWithImage((Item)_subject);
		_actionMap.get("nextItemWithImage").setEnabled(item != null);
		
		item = _helper.getPreviousItemWithImage((Item)_subject);
		_actionMap.get("previousItemWithImage").setEnabled(item != null);
		
		
	}
	
	private void displaySubject(Illustratable subject, Image image) {
		_model.setSelectedImage(image);
		_subject = image.getSubject();
		_images = _subject.getImages();
		_contentPanel.removeAll();
		
		_imageEditors = new HashMap<String, ImageEditorPanel>();
		
		displayImage(image);
		buildSubjectMenu();
	}
	
	/**
	 * Creates an ImageEditorPanel to display the image if necessary then
	 * switches the layout to display the image.
	 * @param image the image to display.
	 */
	public void displayImage(Image image) {
		try {
			_selectedImage = image;
			String text = image.getSubjectTextOrFileName();
			if (!_imageEditors.containsKey(text)) {
				addCardFor(image);
			}
			
			int index = _images.indexOf(_selectedImage);
			_actionMap.get("nextImage").setEnabled(index < (_images.size()-1));
			_actionMap.get("previousImage").setEnabled(index > 0);
			_actionMap.get("showImageNotes").setEnabled(image.hasNotes());
			_layout.show(_contentPanel, text);
			revalidate();
			replaySound();
		}
		catch (Exception e) {
			e.printStackTrace();
			_messageHelper.errorLoadingImage(image.getFileName());
		}
	}
	
	
	/**
	 * Displays the next image of the current subject (Character or Item)
	 */
	@Action
	public void nextImage() {
		int nextIndex = _images.indexOf(_selectedImage) + 1;
		if (nextIndex < _images.size()) {
			displayImage(_images.get(nextIndex));
		}
	}
	
	/**
	 * Displays the previous image of the current subject (Character or Item)
	 */
	@Action
	public void previousImage() {
		int prevIndex = _images.indexOf(_selectedImage) -1;
		if (prevIndex >= 0) {
			displayImage(_images.get(prevIndex));
		}
	}
	
	
	@Action
	public void nextItemWithImage() {
		Item item = _helper.getNextItemWithImage((Item)_subject);
		if (item != null) {
			displaySubject(item, item.getImages().get(0));
		}	
	}
	
	@Action
	public void previousItemWithImage() {
		
		Item item = _helper.getPreviousItemWithImage((Item)_subject);
		if (item != null) {
			displaySubject(item, item.getImages().get(0));
		}	
	}
	
	
	@Action
	public void nextCharacterWithImage() {
		Character character = _helper.getNextCharacterWithImage((Character)_subject);
		if (character != null) {
			displaySubject(character, character.getImages().get(0));	
		}
	}

	@Action
	public void previousCharacterWithImage() {
		Character character = _helper.getPreviousCharacterWithImage((Character)_subject);
		if (character != null) {
			displaySubject(character, character.getImages().get(0));	
		}
	}
	
	@Action
	public void showCharacterDetails() {
		Character character = (Character)_subject;
		CharacterFormatter formatter = new CharacterFormatter();
		StringBuilder text = new StringBuilder();
		text.append(formatter.formatCharacterDescription(character));
		
		if (character instanceof MultiStateCharacter) {
			MultiStateCharacter multiStateChar = (MultiStateCharacter)character;
			for (int i=1; i<=multiStateChar.getNumberOfStates(); i++) {
				text.append("\\par ");
				text.append(formatter.formatState(multiStateChar, i));
			}
		}
		else if (character instanceof NumericCharacter<?>) {
			NumericCharacter<?> numericChar = (NumericCharacter<?>)character;
			text.append("\\par ");
			text.append(numericChar.getUnits());
		}
		RichTextDialog dialog = new RichTextDialog(text.toString());
		editor.show(dialog);
	}
	
	@Action
	public void toggleScaling() {
		if (_scalingMode == ScalingMode.NO_SCALING) {
			setScalingMode(ScalingMode.FIXED_ASPECT_RATIO);
		}
		else {
			setScalingMode(ScalingMode.NO_SCALING);
		}
	}
	
	private void setScalingMode(ScalingMode mode) {
		if (mode == ScalingMode.NO_SCALING) {
			getContentPane().remove(_contentPanel);
			getContentPane().add(new JScrollPane(_contentPanel), BorderLayout.CENTER);
		}
		else if (_scalingMode == ScalingMode.NO_SCALING){
			getContentPane().removeAll();
			getContentPane().add(_contentPanel, BorderLayout.CENTER);
		}
		_scalingMode = mode;
		for (ImageEditorPanel editor : _imageEditors.values()) {
			editor.setScalingMode(mode);
		}
		revalidate();
	}
	
	@Action
	public void toggleHideText() {
		setHideTextOverlays(!_hideTextOverlays);
	}
	
	private void setHideTextOverlays(boolean hideTextOverlays) {
		if (_hideTextOverlays != hideTextOverlays) {
			_hideTextOverlays = hideTextOverlays;
			for (ImageEditorPanel editor : _imageEditors.values()) {
				editor.setDisplayTextOverlays(!hideTextOverlays);
			}
		}
	}
	
	@Action
	public void toggleHideHotSpots() {
		setHideHotSpots(!_hideHotSpots);
	}
	
	public void setHideHotSpots(boolean hideHotSpots) {
		if (_hideHotSpots != hideHotSpots) {
			_hideHotSpots = hideHotSpots;
			for (ImageEditorPanel editor : _imageEditors.values()) {
				editor.setDisplayHotSpots(!hideHotSpots);
			}
		}
	}
	
	@Action
	public void replaySound() {
		List<ImageOverlay> sounds = _selectedImage.getSounds();
		for (ImageOverlay sound : sounds) {
			
			try {
				URL soundUrl = _model.getImageSettings().findFileOnResourcePath(sound.overlayText);
				AudioPlayer.playClip(soundUrl);
			}
			catch (Exception e) {
				_messageHelper.errorPlayingSound(sound.overlayText);
				e.printStackTrace();
			}
		}
	}
	
	@Action
	public void replayVideo() {}
	
	/**
	 * Reloads the image from disk in response to the menu selection.
	 */
	@Action
	public void reloadImage() {
		String key = _selectedImage.getSubjectTextOrFileName();
		ImageEditorPanel editor = _imageEditors.get(key);
		_imageEditors.remove(key);
		remove(editor);
		
		displayImage(_selectedImage);
		revalidate();
	}
	
	private ImageEditorPanel visibleEditor() {
		String key = _selectedImage.getSubjectTextOrFileName();
		ImageEditorPanel editor = _imageEditors.get(key);
		return editor;
	}
	
	/**
	 * Resizes this JInternalFrame so that the image is displayed at 
	 * it's natural size.
	 */
	@Action
	public void fitToImage() {
		pack();
	}
	
	/**
	 * Displays the image in a full screen window.  Clicking the mouse
	 * will dismiss the window and return to normal mode.
	 */
	@Action
	public void fullScreen() {
		Window parent = SwingUtilities.getWindowAncestor(this);
		final Window w = new Window(parent);
		w.setLayout(new BorderLayout());
		
		final String key = _selectedImage.getSubjectTextOrFileName();
		final ImageEditorPanel editor = _imageEditors.get(key);
		w.add(editor, BorderLayout.CENTER);
		final GraphicsDevice gd = parent.getGraphicsConfiguration().getDevice();
		
		w.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
						
				w.dispose();
				gd.setFullScreenWindow(null);
				add(editor, key);
				_layout.show(_contentPanel, key);
				revalidate();
			}
		});
		
		gd.setFullScreenWindow(w);
	}
	
	@Action
	public void togglePreviewMode() {
		setPreviewMode(!_previewMode);
	}
	
	private void setPreviewMode(boolean preview) {
		if (_previewMode != preview) {
			_previewMode = preview;
					
			setHideHotSpots(preview);
			for (ImageEditorPanel editor : _imageEditors.values()) {
				editor.setEditingEnabled(!preview);
				if (!preview) {
					editor.removeOverlaySelectionObserver(_previewController);
				}
				else {
					editor.addOverlaySelectionObserver(_previewController);
				}
			}
		}
	}
	
	@Action
	public void aboutImage() {
		AboutImageDialog about = new AboutImageDialog(
				this, _selectedImage.getSubjectTextOrFileName(), visibleEditor().getImageFileLocation(),
				visibleEditor().getImage(), visibleEditor().getImageFormatName());
		
		editor.show(about);
	}
	
	@Action
	public void closeImage() {
		try {
			setClosed(true);
		}
		catch (PropertyVetoException e) {}
	}
	
	@Action
	public void showImageNotes() {
		RichTextDialog dialog = new RichTextDialog(_selectedImage.getNotes());
		editor.show(dialog);
	}
	
	@Action
	public void showCharacterNotes() {
		Character character = (Character)_subject;
		RichTextDialog dialog = new RichTextDialog(character.getNotes());
		editor.show(dialog);
	}
	
	class PreviewController implements OverlaySelectionObserver {

		@Override
		public void overlaySelected(SelectableOverlay overlay) {
			overlay.setSelected(!overlay.isSelected());
			ImageOverlay imageOverlay = overlay.getImageOverlay();
			if (imageOverlay.isType(OverlayType.OLOK) ||
				imageOverlay.isType(OverlayType.OLCANCEL)) {
				closeImage();
			}
			else if (imageOverlay.isType(OverlayType.OLNOTES)) {
				showCharacterNotes();
			}
			else if (imageOverlay.isType(OverlayType.OLIMAGENOTES)) {
				showImageNotes();
			}
		}
	}
	
	class ImageEditListener extends AbstractDataSetObserver {

		@Override
		public void imageEdited(DeltaDataSetChangeEvent event) {
			if (event.getImage() == _selectedImage) {
				String key = _selectedImage.getSubjectTextOrFileName();
				ImageEditorPanel editor = _imageEditors.get(key);
				editor.addOverlays();
				editor.revalidate();
			}
		}
		
	}
}
