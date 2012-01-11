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
package au.org.ala.delta.key;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.key.directives.io.KeyOutputFileManager;
import au.org.ala.delta.util.Pair;

public class KeyContext extends DeltaContext {

    double _aBase;
    double _rBase;
    double _reuse;
    double _varyWt;

    private int _numberOfConfirmatoryCharacters;

    private File _charactersFile;
    private File _itemsFile;

    private File _keyOutputFile;
    private File _keyTypesettingFile;
    private File _listingFile;

    private File _dataDirectory;

    private boolean _addCharacterNumbers;
    private boolean _displayBracketedKey;
    private boolean _displayTabularKey;

    private String _typeSettingFileHeaderText;

    private Map<Pair<Integer, Integer>, Integer> _presetCharacters;

    public KeyContext(File dataDirectory, PrintStream out, PrintStream err) {
        super(out, err);
        this._dataDirectory = dataDirectory;

        try {
            _outputFileSelector.setOutputDirectory(_dataDirectory.getAbsolutePath());
        } catch (Exception ex) {
            throw new RuntimeException("Error setting output directory");
        }

        _aBase = 2;
        _rBase = 1.4;
        _reuse = 1.01;
        _varyWt = 0.8;

        _charactersFile = new File(_dataDirectory, "kchars");
        _itemsFile = new File(_dataDirectory, "kitems");

        _addCharacterNumbers = false;
        _displayBracketedKey = true;
        _displayTabularKey = true;

        _presetCharacters = new HashMap<Pair<Integer, Integer>, Integer>();
    }

    public KeyContext(File dataDirectory) {
        this(dataDirectory, System.out, System.err);
    }

    public File getDataDirectory() {
        return _dataDirectory;
    }

    public double getABase() {
        return _aBase;
    }

    public void setABase(double aBase) {
        this._aBase = aBase;
    }

    public double getRBase() {
        return _rBase;
    }

    public void setRBase(double rBase) {
        this._rBase = rBase;
    }

    public double getReuse() {
        return _reuse;
    }

    public void setReuse(double reuse) {
        this._reuse = reuse;
    }

    public double getVaryWt() {
        return _varyWt;
    }

    public void setVaryWt(double varyWt) {
        this._varyWt = varyWt;
    }

    public File getCharactersFile() {
        return _charactersFile;
    }

    public void setCharactersFile(File charactersFile) {
        this._charactersFile = charactersFile;
    }

    public File getItemsFile() {
        return _itemsFile;
    }

    public void setItemsFile(File itemsFile) {
        this._itemsFile = itemsFile;
    }

    public boolean getAddCharacterNumbers() {
        return _addCharacterNumbers;
    }

    public void setAddCharacterNumbers(boolean addCharacterNumbers) {
        this._addCharacterNumbers = addCharacterNumbers;
    }

    public boolean getDisplayBracketedKey() {
        return _displayBracketedKey;
    }

    public void setDisplayBracketedKey(boolean displayBracketedKey) {
        this._displayBracketedKey = displayBracketedKey;
    }

    public boolean getDisplayTabularKey() {
        return _displayTabularKey;
    }

    public void setDisplayTabularKey(boolean displayTabularKey) {
        this._displayTabularKey = displayTabularKey;
    }

    public File getKeyOutputFile() {
        return _keyOutputFile;
    }

    public void setKeyOutputFile(File keyOutputFile) {
        this._keyOutputFile = keyOutputFile;
    }

    public File getKeyTypesettingFile() {
        return _keyTypesettingFile;
    }

    public void setKeyTypesettingFile(File keyTypesettingFile) {
        this._keyTypesettingFile = keyTypesettingFile;
    }

    public File getListingFile() {
        return _listingFile;
    }

    public void setListingFile(File listingFile) {
        this._listingFile = listingFile;
    }

    @Override
    protected void createOutputFileManager() {
        _outputFileSelector = new KeyOutputFileManager(getDataSet());
    }

    public KeyOutputFileManager getOutputFileManager() {
        return (KeyOutputFileManager) _outputFileSelector;
    }

    public String getTypeSettingFileHeaderText() {
        return _typeSettingFileHeaderText;
    }

    public void setTypeSettingFileHeaderText(String typeSettingFileHeaderText) {
        this._typeSettingFileHeaderText = typeSettingFileHeaderText;
    }

    public void setPresetCharacter(int characterNumber, int columnNumber, int groupNumber) {
        Pair<Integer, Integer> columnGroupPair = new Pair<Integer, Integer>(columnNumber, groupNumber);
        _presetCharacters.put(columnGroupPair, characterNumber);
    }

    /**
     * Returns the preset character number for the given column number and group
     * number, or -1 if no character has been preset for the column and group.
     * 
     * @param columnNumber
     * @param groupNumber
     * @return the preset character number for the given column number and group
     *         number, or -1 if no character has been preset for the column and
     *         group.
     */
    public int getPresetCharacter(int columnNumber, int groupNumber) {
        Pair<Integer, Integer> columnGroupPair = new Pair<Integer, Integer>(columnNumber, groupNumber);
        if (_presetCharacters.containsKey(columnGroupPair)) {
            return _presetCharacters.get(columnGroupPair);
        } else {
            return -1;
        }
    }

    public int getNumberOfConfirmatoryCharacters() {
        return _numberOfConfirmatoryCharacters;
    }

    public void setNumberOfConfirmatoryCharacters(int numberOfConfirmatoryCharacters) {
        if (numberOfConfirmatoryCharacters < 1 || numberOfConfirmatoryCharacters > 4) {
            throw new IllegalArgumentException("Number of confirmatory characters must be between 1 and 4");
        }
        this._numberOfConfirmatoryCharacters = numberOfConfirmatoryCharacters;
    }
}
