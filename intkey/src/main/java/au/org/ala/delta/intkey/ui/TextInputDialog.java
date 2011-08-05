package au.org.ala.delta.intkey.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Application;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.image.ImageSettings;

public class TextInputDialog extends CharacterValueInputDialog {
    private JPanel _pnlTxtFld;
    private JTextField _txtInput;
    private List<String> _inputData;

    @Resource
    String title;

    public TextInputDialog(Frame owner, TextCharacter ch, ImageSettings imageSettings) {
        super(owner, ch, imageSettings);

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(TextInputDialog.class);
        resourceMap.injectFields(this);

        setTitle(title);

        _pnlTxtFld = new JPanel();
        _pnlMain.add(_pnlTxtFld, BorderLayout.CENTER);
        _pnlTxtFld.setLayout(new BorderLayout(0, 0));

        _txtInput = new JTextField();
        _pnlTxtFld.add(_txtInput, BorderLayout.NORTH);
        _txtInput.setColumns(10);
        _txtInput.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TextInputDialog.this.handleBtnOKClicked();
            }
        });

        _inputData = new ArrayList<String>();
    }

    @Override
    void handleBtnOKClicked() {
        String data = _txtInput.getText();

        for (String str : data.split("/")) {
            if (str.length() > 0) {
                _inputData.add(str);
            }
        }

        setVisible(false);
    }

    public List<String> getInputData() {
        return _inputData;
    }

    @Override
    void handleBtnCancelClicked() {
        this.setVisible(false);
    }

    @Override
    void handleBtnImagesClicked() {
        ImageCharacterInputDialog dlg = new ImageCharacterInputDialog(this, _ch, _imageSettings);
        dlg.setVisible(true);
        
        _inputData = dlg.getInputTextValues();
        if (!_inputData.isEmpty()) {
            this.setVisible(false);
        }
    }

}
