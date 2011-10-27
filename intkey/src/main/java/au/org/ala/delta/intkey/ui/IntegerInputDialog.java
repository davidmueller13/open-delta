package au.org.ala.delta.intkey.ui;

import java.awt.Frame;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.intkey.directives.ParsingUtils;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.image.ImageSettings;

public class IntegerInputDialog extends NumberInputDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1248724422772112737L;

    private Set<Integer> _inputData;

    @Resource
    String title;

    @Resource
    String validationErrorMessage;

    @Resource
    String validationErrorTitle;

    public IntegerInputDialog(Frame owner, IntegerCharacter ch, ImageSettings imageSettings, boolean displayNumbering, boolean enableImagesButton) {
        super(owner, ch, imageSettings, displayNumbering, enableImagesButton);

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(IntegerInputDialog.class);
        resourceMap.injectFields(this);

        setTitle(title);

        _inputData = null;
    }

    @Override
    void handleBtnOKClicked() {
        String inputTxt = _txtInput.getText();
        if (inputTxt.length() > 0) {
            try {
                _inputData = ParsingUtils.parseMultistateOrIntegerCharacterValue(inputTxt);
                this.setVisible(false);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, validationErrorMessage, validationErrorTitle, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            this.setVisible(false);
        }
    }

    @Override
    void handleBtnImagesClicked() {
        CharacterImageDialog dlg = new CharacterImageDialog(this, Arrays.asList(new au.org.ala.delta.model.Character[] { _ch }), _imageSettings, true, true);
        dlg.setVisible(true);

        try {
            Set<Integer> inputValuesFromImageDialog = dlg.getInputIntegerValues();
            if (inputValuesFromImageDialog != null && !inputValuesFromImageDialog.isEmpty()) {
                _inputData = inputValuesFromImageDialog;
                this.setVisible(false);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, validationErrorMessage, validationErrorTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    public Set<Integer> getInputData() {
        return _inputData;
    }

    @Override
    void handleBtnCancelClicked() {
        _inputData = null;
        this.setVisible(false);
    }

}
