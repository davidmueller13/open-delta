package au.org.ala.delta.intkey.model;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

import au.org.ala.delta.intkey.IntkeyUI;
import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;

public class MockIntkeyUI implements IntkeyUI {

    @Override
    public void handleNewDataset(IntkeyDataset dataset) {
        // do nothing
    }

    @Override
    public void handleDatasetClosed() {
        // do nothing
    }

    @Override
    public void handleUpdateAll() {
        // do nothing
    }

    @Override
    public void handleIdentificationRestarted() {
        // do nothing
    }

    @Override
    public void displayRTFReport(String rtfSource, String title) {
        // do nothing
    }

    @Override
    public void displayErrorMessage(String message) {
        // do nothing
    }

    @Override
    public void displayInformationMessage(String message) {
        // do nothing
    }

    @Override
    public void displayBusyMessage(String message) {
    }

    @Override
    public void removeBusyMessage() {
    }

    @Override
    public void displayTaxonInformation(List<Item> taxa, String imagesAutoDisplayText, String otherItemsAutoDisplayText, boolean closePromptAfterAutoDisplay) {
    }

    @Override
    public void addToolbarButton(boolean advancedModeOnly, boolean normalModeOnly, boolean inactiveUnlessUsedCharacters, String imageFileName, List<String> commands, String shortHelp, String fullHelp) {
    }

    @Override
    public void addToolbarSpace() {
    }

    @Override
    public void clearToolbar() {
    }

    @Override
    public void IllustrateCharacters(List<Character> characters) {
    }

    @Override
    public void IllustrateTaxa(List<Item> taxa) {
    }

    @Override
    public void displayContents(LinkedHashMap<String, String> contentsMap) {
    }

    @Override
    public void displayFile(URL fileURL, String description) {
    }

    @Override
    public void setLogVisible(boolean visible) {
    }

    @Override
    public void updateLog() {
    }

    @Override
    public boolean isLogVisible() {
        return false;
    }

    @Override
    public void quitApplication() {
    }

    @Override
    public List<Item> getSelectedTaxa() {
        return null;
    }

}
