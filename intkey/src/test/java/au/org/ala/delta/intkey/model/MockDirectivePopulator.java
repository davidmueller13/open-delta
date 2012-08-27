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
package au.org.ala.delta.intkey.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.mutable.MutableBoolean;

import au.org.ala.delta.intkey.directives.DirectivePopulator;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.util.Pair;

public class MockDirectivePopulator implements DirectivePopulator {

    @Override
    public List<Character> promptForCharactersByKeyword(String directiveName, boolean permitSelectionFromIncludedCharactersOnly, boolean noneKeywordAvailable, List<String> returnSelectedKeywords) {
        return null;
    }

    @Override
    public List<Character> promptForCharactersByList(String directiveName, boolean permitSelectionFromIncludedCharactersOnly, List<String> returnSelectedKeywords) {
        return null;
    }

    @Override
    public List<Item> promptForTaxaByKeyword(String directiveName, boolean selectFromIncludedTaxaOnly, boolean noneKeywordAvailable, boolean includeSpecimenAsOption, MutableBoolean specimenSelected, List<String> returnSelectedKeywords) {
        return null;
    }

    @Override
    public List<Item> promptForTaxaByList(String directiveName, boolean selectFromIncludedTaxaOnly, boolean autoSelectSingleValue, boolean singleSelect, boolean includeSpecimenAsOption, MutableBoolean specimenSelected, List<String> returnSelectedKeywords) {
        return null;
    }

    @Override
    public Boolean promptForYesNoOption(String message) {
        return false;
    }

    @Override
    public String promptForString(String message, String initialValue, String directiveName) {
        return null;
    }

    @Override
    public List<String> promptForTextValue(TextCharacter ch, List<String> currentValue) {
        return null;
    }

    @Override
    public Set<Integer> promptForIntegerValue(IntegerCharacter ch, Set<Integer> currentValue) {
        return null;
    }

    @Override
    public FloatRange promptForRealValue(RealCharacter ch, FloatRange currentValue) {
        return null;
    }

    @Override
    public Set<Integer> promptForMultiStateValue(MultiStateCharacter ch, Set<Integer> currentValue, Character dependentCharacter) {
        return null;
    }

    @Override
    public File promptForFile(List<String> fileExtensions, String description, boolean createFileIfNonExistant) throws IOException {
        return null;
    }

    @Override
    public Boolean promptForOnOffValue(String directiveName, boolean initialValue) {
        return null;
    }

    @Override
    public List<Object> promptForMatchSettings() {
        return null;
    }

    @Override
    public List<Object> promptForButtonDefinition() {
        return null;
    }

    @Override
    public Pair<ImageDisplayMode, DisplayImagesReportType> promptForImageDisplaySettings() {
        return null;
    }

    @Override
    public String promptForDataset() {
        // TODO Auto-generated method stub
        return null;
    }

}
