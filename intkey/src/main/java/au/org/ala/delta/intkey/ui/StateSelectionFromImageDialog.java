package au.org.ala.delta.intkey.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;

import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.model.image.OverlayType;
import au.org.ala.delta.ui.image.ImageViewer;
import au.org.ala.delta.ui.image.OverlaySelectionObserver;
import au.org.ala.delta.ui.image.SelectableOverlay;

public class StateSelectionFromImageDialog extends JDialog {

    private ImageSettings _imageSettings;
    private Set<Integer> _selectedStates;
    private Character _character;

    public StateSelectionFromImageDialog(Frame owner, MultiStateCharacter character, ImageSettings imageSettings) {
        super(owner, true);
    }

    public StateSelectionFromImageDialog(Dialog owner, MultiStateCharacter character, ImageSettings imageSettings) {
        super(owner, true);
    }

    private void init(MultiStateCharacter character, ImageSettings imageSettings) {
        _imageSettings = imageSettings;
        _character = character;
        _selectedStates = new HashSet<Integer>();

        Image img = _character.getImages().get(0);
        ImageViewer viewer = new ImageViewer(img, _imageSettings);
        viewer.addOverlaySelectionObserver(new OverlaySelectionObserver() {

            @Override
            public void overlaySelected(SelectableOverlay overlay) {
                handleOverlaySelection(overlay);
            }
        });

        this.add(viewer);
    }

    private void handleOverlaySelection(SelectableOverlay overlay) {
        overlay.setSelected(!overlay.isSelected());
        ImageOverlay imageOverlay = overlay.getImageOverlay();
        if (imageOverlay.isType(OverlayType.OLOK) || imageOverlay.isType(OverlayType.OLCANCEL)) {
            this.setVisible(false);
        } else if (imageOverlay.isType(OverlayType.OLNOTES)) {
            // showCharacterNotes();
        } else if (imageOverlay.isType(OverlayType.OLIMAGENOTES)) {
            // showImageNotes();
        } else if (imageOverlay.isType(OverlayType.OLSTATE)) {
            //imageOverlay.ge
        }
    }

}
