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
package au.org.ala.delta.intkey.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.model.image.OverlayType;
import au.org.ala.delta.rtf.RTFBuilder;
import au.org.ala.delta.ui.image.SelectableOverlay;

/**
 * Used to Display images for a single character
 * 
 * @author ChrisF
 * 
 */
public class CharacterImageDialog extends ImageDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 6274578091708982240L;

    private List<Character> _characters;

    private JMenuItem _mnuItNextCharacter;
    private JMenuItem _mnuItPreviousCharacter;

    private int _selectedCharacterIndex;

    private boolean _valuesEditable;

    // Title used when the dialog is used to enter values for an integer
    // character
    @Resource
    String integerTitle;

    // Title used when the dialog is used to enter values for a real character
    @Resource
    String realTitle;

    // Title used when the dialog is used to enter values for an multistate
    // character
    @Resource
    String multistateTitle;

    // Title used when the dialog is used only for display purposes - character
    // values are not
    // editable.
    @Resource
    String notEditableTitle;

    // Message shown when users attempts to set character values when the dialog
    // is being used by the ILLUSTRATE CHARACTERS directive.
    @Resource
    String imageForViewingOnlyMessage;

    public CharacterImageDialog(Frame owner, List<Character> characters, ImageSettings imageSettings, boolean modal, boolean valuesEditable, boolean initScalingMode) {
        super(owner, imageSettings, modal, initScalingMode);
        init(characters, valuesEditable);
    }

    /**
     * @wbp.parser.constructor
     */
    public CharacterImageDialog(Dialog owner, List<Character> characters, ImageSettings imageSettings, boolean modal, boolean valuesEditable, boolean initScalingMode) {
        super(owner, imageSettings, modal, initScalingMode);
        init(characters, valuesEditable);
    }

    private void init(List<Character> characters, boolean valuesEditable) {
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CharacterImageDialog.class);
        resourceMap.injectFields(this);

        _characters = characters;
        _valuesEditable = valuesEditable;
        getContentPane().setLayout(new BorderLayout(0, 0));

        buildMenuItems();
    }

    private void buildMenuItems() {
        ActionMap actionMap = Application.getInstance().getContext().getActionMap(CharacterImageDialog.class, this);

        _mnuItNextCharacter = new JMenuItem();
        _mnuItNextCharacter.setAction(actionMap.get("viewNextCharacter"));
        _mnuControl.add(_mnuItNextCharacter);

        _mnuItPreviousCharacter = new JMenuItem();
        _mnuItPreviousCharacter.setAction(actionMap.get("viewPreviousCharacter"));
        _mnuControl.add(_mnuItPreviousCharacter);
    }

    private void displayImagesForCharacter(int characterIndex, int imageToShow) {
        _selectedCharacterIndex = characterIndex;
        Character selectedCharacter = _characters.get(characterIndex);

        setImages(selectedCharacter.getImages());
        showImage(imageToShow);

        _mnuItNextCharacter.setEnabled(_selectedCharacterIndex < _characters.size() - 1);
        _mnuItPreviousCharacter.setEnabled(_selectedCharacterIndex > 0);
    }

    private void updateTitle() {
        Character selectedCharacter = _characters.get(_selectedCharacterIndex);

        if (_valuesEditable) {
            if (selectedCharacter instanceof IntegerCharacter) {
                setTitle(integerTitle);
            } else if (selectedCharacter instanceof RealCharacter) {
                setTitle(realTitle);
            } else if (selectedCharacter instanceof MultiStateCharacter) {
                setTitle(multistateTitle);
            }
        } else {
            String strCharacterNumber = Integer.toString(selectedCharacter.getCharacterId());
            String formattedImageName = _imageDescriptionFormatter.defaultFormat(_multipleImageViewer.getVisibleImage().getSubjectTextOrFileName());
            setTitle(MessageFormat.format(notEditableTitle, strCharacterNumber, formattedImageName));
        }
    }

    @Override
    protected void handleNewImageSelected() {
        super.handleNewImageSelected();
        updateTitle();
    }

    public void displayImagesForCharacter(Character ch) {
        int characterIndex = _characters.indexOf(ch);
        if (characterIndex > -1) {
            displayImagesForCharacter(characterIndex, 0);
        }
    }

    @Action
    public void viewNextCharacter() {
        if (_selectedCharacterIndex < _characters.size() - 1) {
            displayImagesForCharacter(_selectedCharacterIndex + 1, 0);
        }
    }

    @Action
    public void viewPreviousCharacter() {
        if (_selectedCharacterIndex > 0) {
            displayImagesForCharacter(_selectedCharacterIndex - 1, 0);
        }
    }

    @Override
    public void overlaySelected(SelectableOverlay overlay) {
        ImageOverlay imageOverlay = overlay.getImageOverlay();
        if (imageOverlay.isType(OverlayType.OLNOTES)) {
            RTFBuilder builder = new RTFBuilder();
            builder.startDocument();
            builder.appendText(_characters.get(_selectedCharacterIndex).getNotes());
            builder.endDocument();
            displayRTFWindow(builder.toString(), "Notes");
        } else {
            if (_valuesEditable) {
                super.overlaySelected(overlay);
            } else {
                if (imageOverlay.isType(OverlayType.OLOK) || imageOverlay.isType(OverlayType.OLCANCEL)) {
                    super.overlaySelected(overlay);
                } else {
                    JOptionPane.showMessageDialog(this, imageForViewingOnlyMessage, "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

}
