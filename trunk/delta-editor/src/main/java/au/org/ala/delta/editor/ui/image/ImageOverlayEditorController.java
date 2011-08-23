package au.org.ala.delta.editor.ui.image;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.ui.util.MenuBuilder;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Illustratable;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericCharacter;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.model.image.OverlayLocation;
import au.org.ala.delta.model.image.OverlayType;

/**
 * Handles actions performed on an ImageEditorPanel. (add/delete/update overlays).
 */
public class ImageOverlayEditorController {

	private ButtonAlignment _alignment;
	private ImageSettings _imageSettings;
	private ImageEditorSelectionModel _selection;
	private ResourceMap _resources;
	private ActionMap _actions;
	
	enum ButtonAlignment {
		ALIGN_VERTICAL, ALIGN_HORIZONTAL, ALIGN_NONE
	};

	public ImageOverlayEditorController(ImageEditorSelectionModel selection, EditorViewModel model) {
		_imageSettings = model.getImageSettings();
		_selection = selection;
		_resources = Application.getInstance().getContext().getResourceMap();
		_actions = Application.getInstance().getContext().getActionMap(this);
		
	}
	
	private void disableActions() {
		enableStateOverlays();
		Illustratable subject = _selection.getSelectedImage().getSubject();
		
		if (isCharacterIllustrated()) {
			if (!((Character)subject).hasNotes()) {
				_actions.get("addNotesOverlay").setEnabled(false);
			}
		}
		boolean enableHotspot = _selection.getSelectedOverlay() != null && 
			_selection.getSelectedOverlay().isType(OverlayType.OLSTATE);
		_actions.get("addHotspot").setEnabled(enableHotspot);
		
		disableIfPresent(OverlayType.OLOK, "addOkOverlay");
		disableIfPresent(OverlayType.OLCANCEL, "addCancelOverlay");
		disableIfPresent(OverlayType.OLIMAGENOTES, "addImageNotesOverlay");
		disableIfPresent(OverlayType.OLNOTES, "addNotesOverlay");
		disableIfPresent(OverlayType.OLFEATURE, "addFeatureDescriptionOverlay");
		disableIfPresent(OverlayType.OLITEM, "addItemDescriptionOverlay");
		
	}
	
	private void disableIfPresent(int overlayType, String action) {
		Image selectedImage =_selection.getSelectedImage();
		boolean present = selectedImage.getOverlay(overlayType) != null;
		_actions.get(action).setEnabled(!present);
	}
	
	private boolean isMultistateCharacterIllustrated() {
		return getSubject() instanceof MultiStateCharacter;
	}
	private boolean isCharacterIllustrated() {
		return getSubject() instanceof Character;
	}
	private Illustratable getSubject() {
		return _selection.getSelectedImage().getSubject();
	}
	private void enableStateOverlays() {
		if (!isMultistateCharacterIllustrated()) {
			_actions.get("addStateOverlay").setEnabled(false);
		}
		else {
			MultiStateCharacter character = (MultiStateCharacter)getSubject();
			
			Set<Integer> states = getStatesWithOverlays();
			_actions.get("addStateOverlay").setEnabled(states.size() != character.getNumberOfStates());
		}
		
	}
	
	public JPopupMenu buildPopupMenu() {
		disableActions();
		boolean itemImage = (_selection.getSelectedImage().getSubject() instanceof Item);
		List<String> popupMenuActions = new ArrayList<String>();
		if (_selection.getSelectedOverlay() != null) {
			popupMenuActions.add("editSelectedOverlay");
			popupMenuActions.add("deleteSelectedOverlay");
			popupMenuActions.add("-");
		}
		popupMenuActions.add("deleteAllOverlays");
		popupMenuActions.add("-");
		popupMenuActions.add("displayImageSettings");
		popupMenuActions.add("-");
		popupMenuActions.add("cancelPopup");
		
		JPopupMenu popup = new JPopupMenu();
		MenuBuilder.buildMenu(popup, popupMenuActions, _actions);

		if (_selection.getSelectedOverlay() != null) {
			List<String> stackOverlayMenuActions = new ArrayList<String>();
			stackOverlayMenuActions.add("stackSelectedOverlayHigher");
			stackOverlayMenuActions.add("stackSelectedOverlayLower");
			stackOverlayMenuActions.add("stackSelectedOverlayOnTop");
			stackOverlayMenuActions.add("stackSelectedOverlayOnBottom");
			JMenu stackOverlayMenu = new JMenu(_resources.getString("overlayPopup.stackOverlayMenu"));
			MenuBuilder.buildMenu(stackOverlayMenu, stackOverlayMenuActions, _actions);
			popup.add(stackOverlayMenu, 2);
		}
		List<String> insertOverlayMenuActions = new ArrayList<String>();
		insertOverlayMenuActions.add("addTextOverlay");
		if (itemImage) {
			insertOverlayMenuActions.add("addItemDescriptionOverlay");
		}
		insertOverlayMenuActions.add("-");
		if (!itemImage) {
			insertOverlayMenuActions.add("addAllUsualOverlays");
			insertOverlayMenuActions.add("addFeatureDescriptionOverlay");
			insertOverlayMenuActions.add("addStateOverlay");
			insertOverlayMenuActions.add("addHotspot");
			insertOverlayMenuActions.add("-");
		}
		insertOverlayMenuActions.add("addOkOverlay");
		insertOverlayMenuActions.add("addCancelOverlay");
		if (!itemImage) {
			insertOverlayMenuActions.add("addNotesOverlay");
		}
		else {
			insertOverlayMenuActions.add("addImageNotesOverlay");
		}
		
		JMenu insertOverlayMenu = new JMenu(_resources.getString("overlayPopup.insertOverlayMenu"));
		MenuBuilder.buildMenu(insertOverlayMenu, insertOverlayMenuActions, _actions);
		int indexModifier = _selection.getSelectedOverlay() == null ? 4 : 0;
		popup.add(insertOverlayMenu, 5-indexModifier);
		
		List<String> alignButtonsMenuActions = new ArrayList<String>();
		alignButtonsMenuActions.add("useDefaultButtonAlignment");
		alignButtonsMenuActions.add("alignButtonsVertically");
		alignButtonsMenuActions.add("alignButtonsHorizontally");
		alignButtonsMenuActions.add("dontAlignButtons");
		JMenu alignButtonsMenu = new JMenu(_resources.getString("overlayPopup.alignButtonsMenu"));
		alignButtonsMenu.setEnabled(false);
		MenuBuilder.buildMenu(alignButtonsMenu, alignButtonsMenuActions, _actions);
		popup.add(alignButtonsMenu, 7-indexModifier);
		
		return popup;
	}

	
	@Action
	public void editSelectedOverlay() {
		if (!_selection.isHotSpotSelected()) {
			editOverlay(_selection.getSelectedOverlay());
		}
		else {
			editHotspot(_selection.getSelectedOverlayLocation());
		}
	}
	
	protected void editOverlay(ImageOverlay overlay) {
		DeltaEditor editor = (DeltaEditor)Application.getInstance();
		OverlayEditDialog overlayEditor = new OverlayEditDialog(editor.getMainFrame(), 
				_selection.getSelectedImage(), overlay);
		editor.show(overlayEditor);
	}
	
	protected void editHotspot(OverlayLocation location) {
		DeltaEditor editor = (DeltaEditor)Application.getInstance();
		ImageOverlay overlay = _selection.getSelectedOverlay();
		HotspotEditDialog overlayEditor = new HotspotEditDialog(editor.getMainFrame(), 
				_selection.getSelectedImage(), 
				overlay, overlay.location.indexOf(location));
		editor.show(overlayEditor);
	}

	@Action
	public void deleteSelectedOverlay() {
		if (_selection.isHotSpotSelected()) {
			_selection.getSelectedOverlay().deleteLocation(_selection.getSelectedOverlayLocation());
			_selection.getSelectedImage().updateOverlay(_selection.getSelectedOverlay());
		}
		else {
			_selection.getSelectedImage().deleteOverlay(_selection.getSelectedOverlay());
		}
	}

	@Action
	public void deleteAllOverlays() {
		// TODO are you sure?
		_selection.getSelectedImage().deleteAllOverlays();
	}

	@Action
	public void displayImageSettings() {
		DeltaEditor editor = (DeltaEditor)Application.getInstance();
		ImageSettingsDialog dialog = new ImageSettingsDialog(
				editor.getMainFrame(), editor.getCurrentDataSet(), _imageSettings);
		editor.show(dialog);
	}

	@Action
	public void cancelPopup() {
	}

	@Action
	public void stackSelectedOverlayHigher() {
		Image image = _selection.getSelectedImage();
		image.moveUp(_selection.getSelectedOverlay());
	
	}

	@Action
	public void stackSelectedOverlayLower() {
		Image image = _selection.getSelectedImage();
		image.moveDown(_selection.getSelectedOverlay());
	}

	@Action
	public void stackSelectedOverlayOnTop() {
		Image image = _selection.getSelectedImage();
		image.moveToTop(_selection.getSelectedOverlay());
	}

	@Action
	public void stackSelectedOverlayOnBottom() {
		Image image = _selection.getSelectedImage();
		image.moveToBottom(_selection.getSelectedOverlay());
	}

	@Action
	public void useDefaultButtonAlignment() {
		_alignment = ButtonAlignment.ALIGN_NONE;
	}

	@Action
	public void alignButtonsVertically() {
		_alignment = ButtonAlignment.ALIGN_VERTICAL;
	}

	@Action
	public void alignButtonsHorizontally() {
		_alignment = ButtonAlignment.ALIGN_HORIZONTAL;
	}

	@Action
	public void dontAlignButtons() {
		_alignment = ButtonAlignment.ALIGN_NONE;
	}

	@Action
	public void addTextOverlay() {
		addOverlay(OverlayType.OLTEXT);
	}

	@Action
	public void addAllUsualOverlays() {
		Image image = _selection.getSelectedImage();
		if (image.getSubject() instanceof Character) {
			Character character = (Character) image.getSubject();
			Point origin = new Point(150, 300);
			if (image.getOverlay(OverlayType.OLFEATURE) == null) {
				ImageOverlay newOverlay = newOverlay(OverlayType.OLFEATURE);

				newOverlay.setX(origin.x);
				newOverlay.setY(origin.y);
				origin.x += 25;
				origin.y += 50;
				image.updateOverlay(newOverlay);

			}
			if (character.getCharacterType().isMultistate()) {
				addStateOverlays((MultiStateCharacter) character, origin);
			} else if (character.getCharacterType().isNumeric()) {
				if (image.getOverlay(OverlayType.OLENTER) == null) {
					ImageOverlay newOverlay = newOverlay(OverlayType.OLENTER);
					newOverlay.setX(400);
					newOverlay.setY(600);
					image.updateOverlay(newOverlay);
				}
				if (image.getOverlay(OverlayType.OLUNITS) == null
						&& StringUtils.isNotEmpty(((NumericCharacter<?>) character).getUnits())) {
					ImageOverlay newOverlay = newOverlay(OverlayType.OLUNITS);

					newOverlay.setX(Short.MIN_VALUE);
					newOverlay.setY(Short.MIN_VALUE);
					image.updateOverlay(newOverlay);

				}
			}
			if (image.getOverlay(OverlayType.OLOK) == null) {
				newOverlay(OverlayType.OLOK);
			}
			if (image.getOverlay(OverlayType.OLCANCEL) == null) {
				newOverlay(OverlayType.OLCANCEL);
			}
			if (StringUtils.isNotEmpty(character.getNotes()) && image.getOverlay(OverlayType.OLNOTES) == null) {
				newOverlay(OverlayType.OLNOTES);
			}
		}
	}

	private void addStateOverlays(MultiStateCharacter character, Point origin) {
		int stateNum;
		Set<Integer> states = getStatesWithOverlays();

		for (stateNum = 1; stateNum <= character.getNumberOfStates(); stateNum++) {
			if (states.contains(stateNum)) {
				continue;
			}
			ImageOverlay newOverlay = newStateOverlay(stateNum);

			newOverlay.setX(origin.x);
			newOverlay.setY(origin.y);

			OverlayLocation hotspot = addHotspot(newOverlay);
			hotspot.setX(origin.x);
			hotspot.setY(origin.y);
			
			_selection.getSelectedImage().updateOverlay(newOverlay);
			origin.x += 25;
			origin.y += 50;
			if (origin.x > 350)
				origin.x -= 170;
			if (origin.y > 650)
				origin.y -= 330;
		}
	}

	private Set<Integer> getStatesWithOverlays() {
		List<ImageOverlay> overlays = _selection.getSelectedImage().getOverlaysOfType(OverlayType.OLSTATE);
		Set<Integer> states = new HashSet<Integer>();
		for (ImageOverlay overlay : overlays) {
			states.add(overlay.stateId);
		}
		return states;
	}

	@Action
	public void addFeatureDescriptionOverlay() {
		addOverlay(OverlayType.OLFEATURE);
	}
	
	@Action
	public void addItemDescriptionOverlay() {
		addOverlay(OverlayType.OLITEM);
	}

	@Action
	public void addStateOverlay() {
		Set<Integer> states = getStatesWithOverlays();		
		MultiStateCharacter character = (MultiStateCharacter)_selection.getSelectedImage().getSubject();
		for (int i=1; i<=character.getNumberOfStates(); i++) {
			if (!states.contains(i)) {
				ImageOverlay overlay = newStateOverlay(i);
				configureOverlay(overlay);
				_selection.getSelectedImage().updateOverlay(_selection.getSelectedOverlay());
				break;
			}
		}
	}

	@Action
	public void addHotspot() {
		ImageOverlay overlay = _selection.getSelectedOverlay();
		if (overlay.isType(OverlayType.OLSTATE)) {
			addHotspot(overlay);
			_selection.getSelectedImage().updateOverlay(overlay);
		}
	}
	
	private OverlayLocation addHotspot(ImageOverlay overlay) {
		OverlayLocation hotSpot = new OverlayLocation();
		_imageSettings.configureHotSpotDefaults(hotSpot);
		overlay.addLocation(hotSpot);
		return hotSpot;
	}

	@Action
	public void addOkOverlay() {
		addOverlay(OverlayType.OLOK);
	}

	@Action
	public void addCancelOverlay() {
		addOverlay(OverlayType.OLCANCEL);
	}

	@Action
	public void addNotesOverlay() {
		addOverlay(OverlayType.OLNOTES);
	}
	
	@Action
	public void addImageNotesOverlay() {
		addOverlay(OverlayType.OLIMAGENOTES);
	}

	private void addOverlay(int overlayType) {
		ImageOverlay overlay = newOverlay(overlayType);
		_selection.getSelectedImage().updateOverlay(overlay);
		editOverlay(overlay);
		
	}

	private ImageOverlay newStateOverlay(int stateNum) {
		ImageOverlay overlay = new ImageOverlay(OverlayType.OLSTATE);
		overlay.stateId = stateNum;
		configureOverlay(overlay);
		
		_selection.getSelectedImage().addOverlay(overlay);
		return overlay;
	}

	private ImageOverlay newOverlay(int overlayType) {
		ImageOverlay anOverlay = new ImageOverlay(overlayType);
		configureOverlay(anOverlay);

		_selection.getSelectedImage().addOverlay(anOverlay);
		return anOverlay;
	}

	private void configureOverlay(ImageOverlay anOverlay) {
		Point menuPoint = _selection.getSelectedPoint();
		if (menuPoint == null) {
			menuPoint = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		}
		OverlayLocation newLocation;
		if (anOverlay.location.size() == 0) {
			newLocation = new OverlayLocation();
			anOverlay.location.add(newLocation);
		}
		else {
			newLocation = anOverlay.getLocation(0);
		}
		
		anOverlay.setIntegralHeight(true);
		anOverlay.setHeight(-1);
		
		
		if (menuPoint.x != Integer.MIN_VALUE) {
			
			newLocation.setX(menuPoint.x);
			newLocation.setY(menuPoint.y);
		} else {
			newLocation.X = 350;
			newLocation.Y = 450;
		}
		if (anOverlay.isButton()) {
			int bhClient = newLocation.H;
			int bwClient = newLocation.W;
			newLocation.W = newLocation.H = Short.MIN_VALUE;
			ButtonAlignment align = _alignment;
			if (align == ButtonAlignment.ALIGN_NONE)
				align = ButtonAlignment.ALIGN_VERTICAL;
			if (menuPoint.x == Integer.MIN_VALUE) {
				newLocation.setX(align == ButtonAlignment.ALIGN_VERTICAL ? 800 : 500);
				newLocation.setY(align == ButtonAlignment.ALIGN_VERTICAL ? 500 : 800);
			}
			newLocation.setX(Math.max(0, Math.min(1000 - bwClient, (int) newLocation.X)));
			newLocation.setY(Math.max(0, Math.min(1000 - bhClient, (int) newLocation.Y)));
			int okWhere = Integer.MIN_VALUE, cancelWhere = Integer.MIN_VALUE, notesWhere = Integer.MIN_VALUE;
			ImageOverlay okOverlay = _selection.getSelectedImage().getOverlay(OverlayType.OLOK);
			if (okOverlay != null) {
				if (align == ButtonAlignment.ALIGN_VERTICAL) {
					newLocation.setX(okOverlay.getX());
					okWhere = okOverlay.getY();
				} else if (align == ButtonAlignment.ALIGN_HORIZONTAL) {
					newLocation.setY(okOverlay.getY());
					okWhere = okOverlay.getX();
				}
			}
			ImageOverlay cancelOverlay = _selection.getSelectedImage().getOverlay(OverlayType.OLCANCEL);
			if (cancelOverlay != null) {
				if (align == ButtonAlignment.ALIGN_VERTICAL) {
					newLocation.setX(cancelOverlay.getX());
					cancelWhere = cancelOverlay.getY();
				} else if (align == ButtonAlignment.ALIGN_HORIZONTAL) {
					newLocation.setY(cancelOverlay.getY());
					cancelWhere = cancelOverlay.getX();
				}
			}
			ImageOverlay notesOverlay;
			if (isCharacterIllustrated())
				notesOverlay = _selection.getSelectedImage().getOverlay(OverlayType.OLNOTES);
			else
				notesOverlay = _selection.getSelectedImage().getOverlay(OverlayType.OLIMAGENOTES);
			if (notesOverlay != null) {
				if (align == ButtonAlignment.ALIGN_VERTICAL) {
					newLocation.setX(notesOverlay.getX());
					notesWhere = notesOverlay.getY();
				} else if (align == ButtonAlignment.ALIGN_HORIZONTAL) {
					newLocation.setY(notesOverlay.getY());
					notesWhere = notesOverlay.getX();
				}
			}
			int newWhere = Integer.MIN_VALUE;
			int size = 0;
			if (align == ButtonAlignment.ALIGN_VERTICAL)
				size = bhClient;
			else if (align == ButtonAlignment.ALIGN_HORIZONTAL)
				size = bwClient;
			int space = size + (size + 1) / 2;
			if (anOverlay.type == OverlayType.OLOK) {
				if (cancelWhere != Integer.MIN_VALUE && notesWhere != Integer.MIN_VALUE)
					newWhere = cancelWhere - Math.abs(notesWhere - cancelWhere);
				else if (cancelWhere != Integer.MIN_VALUE || notesWhere != Integer.MIN_VALUE)
					newWhere = Math.max(cancelWhere, notesWhere) - space;
			} else if (anOverlay.type == OverlayType.OLCANCEL) {
				if (okWhere != Integer.MIN_VALUE && notesWhere != Integer.MIN_VALUE)
					newWhere = (okWhere + notesWhere) / 2;
				else if (okWhere != Integer.MIN_VALUE)
					newWhere = okWhere + space;
				else if (notesWhere != Integer.MIN_VALUE)
					newWhere = notesWhere - space;
			}
			if (anOverlay.type == OverlayType.OLNOTES || anOverlay.type == OverlayType.OLIMAGENOTES) {
				if (okWhere != Integer.MIN_VALUE && cancelWhere != Integer.MIN_VALUE)
					newWhere = cancelWhere + Math.abs(cancelWhere - okWhere);
				else if (okWhere != Integer.MIN_VALUE || cancelWhere != Integer.MIN_VALUE)
					newWhere = Math.max(okWhere, cancelWhere) + space;
			}
			if (newWhere + size > 1000)
				newWhere = 1000 - size;
			if (newWhere != Integer.MIN_VALUE) {
				if (newWhere < 0)
					newWhere = 0;
				if (align == ButtonAlignment.ALIGN_VERTICAL)
					newLocation.setY(newWhere);
				else if (align == ButtonAlignment.ALIGN_HORIZONTAL)
					newLocation.setX(newWhere);
			}
		} else if (anOverlay.type == OverlayType.OLHOTSPOT)
			newLocation.setW(Math.min(200, 1000 - newLocation.X));
		else
			newLocation.setW(Math.min(300, 1000 - newLocation.X));
		
	}
}
