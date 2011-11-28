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
package au.org.ala.delta.intkey;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.io.BinaryKeyFile;

/**
 * Encapsulates the way the various bits of data in the character file
 * are stored and accessed.
 * It does not support random access - it primarily supports the use case
 * of CONFOR creating the intkey characters file.
 */
public class WriteOnceIntkeyCharsFile extends BinaryKeyFile {

	private CharactersFileHeader _header;
	
	public WriteOnceIntkeyCharsFile(int numCharacters, String fileName, BinFileMode mode) {
		super(fileName, mode);
		createHeader(numCharacters);
	}
	
	public void createHeader(int numCharacters) {
		nextAvailableRecord();
		_header = new CharactersFileHeader();
		_header.setNC(numCharacters);
		// This is done to allocate the first record to the header.
		writeToRecord(1, _header.toInts());
	}
	
	public void writeHeader() {
		overwriteRecord(1, _header.toInts());
	}
	
	public void writeCharacterNotes(List<String> notes, List<Integer> groups) {
		checkEmpty(_header.getRpChlp());
		checkLength(notes);
		checkEmpty(_header.getRpChlpGrp());
		checkLength(groups);
		
		int notesIndexRecord = nextAvailableRecord();
		_header.setRpChlp(notesIndexRecord);
		
		int neededGapForGroups = (int)Math.floor(groups.size()/RECORD_LENGTH_INTEGERS) + 1;
		int groupRecord = writeIndexedValuesWithGap(notesIndexRecord, neededGapForGroups, notes.toArray(new String[notes.size()]));
		
	
		_header.setRpChlpGrp(groupRecord);
		writeToRecord(groupRecord, groups);
	}
	
	/**
	 * Writes the list of character features to this intkey chars file.
	 * @param features the features to write.
	 */
	public void writeCharacterFeatures(List<List<String>> features) {
		checkEmpty(_header.getRpCdes());
		checkLength(features);
		
		int recordNum = nextAvailableRecord();
		int[] indicies = new int[features.size()];
		int[] numStates = new int[features.size()];
		
		int maxLength = 0;
		for (int i=0; i<features.size(); i++) {
			indicies[i] = recordNum;
			// The first value is always the feature description (hence the -1)
			numStates[i] = features.get(i).size()-1;
			List<String> feature = features.get(i);
			String[] featureArrary = new String[feature.size()];
			
			int size = 0;
			for (int j=0; j<feature.size(); j++) {
				String featureText = feature.get(j);
				featureArrary[j] = featureText;
				size += featureText.length();
			}
			maxLength = Math.max(maxLength, size);
			recordNum += writeAsContinousString(recordNum, features.get(i).toArray(new String[0]));
		}
		
		_header.setRpCdes(recordNum);
		recordNum += writeToRecord(recordNum, indicies);
		
		_header.setRpStat(recordNum);
		writeToRecord(recordNum, numStates);
		
		_header.setMaxDes(maxLength);
	}
	
	public void writeCharacterNotesFormat(String format) {
		checkEmpty(_header.getRpChlpFmt1());
		
		int recordNum = nextAvailableRecord();
		_header.setRpChlpFmt1(recordNum);
		writeCharacterNotesFormat(_header.getRpChlpFmt1(), format);
	}
	
	public void writeCharacterNotesHelpFormat(String format) {
		checkEmpty(_header.getRpChlpFmt2());
		
		int recordNum = nextAvailableRecord();
		_header.setRpChlpFmt2(recordNum);
		writeCharacterNotesFormat(_header.getRpChlpFmt2(), format);
	}
	
	private void writeCharacterNotesFormat(int recordNum, String format) {
		writeStringWithLength(recordNum, format);
	}
	
	public void writeCharacterImages(List<String> charImages) {
		checkEmpty(_header.getRpCImagesC());
		checkLength(charImages);
		
		int indexRecord = nextAvailableRecord();
		_header.setRpCImagesC(indexRecord);
		
		writeIndexedValues(indexRecord, charImages.toArray(new String[charImages.size()]));
	}
	
	public void writeStartupImages(String startupImages) {
		checkEmpty(_header.getRpStartupImages());
		
		int recordNum = nextAvailableRecord();
		_header.setRpStartupImages(recordNum);
		
		writeIndirectStringWithLength(recordNum, startupImages);
	}
	
	public void writeCharacterKeyImages(String characterKeyImages) {
		checkEmpty(_header.getRpCKeyImages());
		
		int recordNum = nextAvailableRecord();
		_header.setRpCKeyImages(recordNum);
		
		writeIndirectStringWithLength(recordNum, characterKeyImages);
	}
	
	public void writeTaxonKeyImages(String taxonKeyImages) {
		checkEmpty(_header.getRpTKeyImages());
		
		int recordNum = nextAvailableRecord();
		_header.setRpTKeyImages(recordNum);
		
		writeIndirectStringWithLength(recordNum, taxonKeyImages);
	}
	
	public void writeHeading(String heading) {
		checkEmpty(_header.getRpHeading());
		
		int recordNum = nextAvailableRecord();
		_header.setRpHeading(recordNum);
		
		writeStringWithLength(recordNum, heading);
	}
	
	public void writeSubHeading(String subHeading) {
		checkEmpty(_header.getRpRegSubHeading());
		
		int recordNum = nextAvailableRecord();
		_header.setRpRegSubHeading(recordNum);
		
		writeStringWithLength(recordNum, subHeading);
	}
	
	public void writeValidationString(String validationString) {
		checkEmpty(_header.getRpValidationString());
		int recordNum = nextAvailableRecord();
		_header.setRpValidationString(recordNum);
		writeStringWithLength(recordNum, validationString);
	}
	
	public void writeCharacterMask(int originalNumChars, BitSet characters) {
		checkEmpty(_header.getRpCharacterMask());
	
		int recordNum = nextAvailableRecord();
		_header.setRpCharacterMask(recordNum);
		
		List<Integer> values = new ArrayList<Integer>();
		values.add(originalNumChars);
		values.addAll(bitSetToInts(characters, originalNumChars));
		writeToRecord(recordNum, values);
	}
	
	public void writeOrWord(String orWord) {
		checkEmpty(_header.getRpOrWord());
		
		int recordNum = nextAvailableRecord();
		_header.setRpOrWord(recordNum);
		
		writeStringWithLength(recordNum, orWord);
	}
	
	public void writeFonts(List<String> fonts) {
		checkEmpty(_header.getRpFont());
		
		int recordNum = nextAvailableRecord();
		_header.setRpFont(recordNum);
		
		writeToRecord(recordNum, fonts.size());
		writeAsContinousString(recordNum+1, fonts.toArray(new String[fonts.size()]));
	}
	
	public void writeItemSubheadings(List<String> itemSubHeadings) {
		checkEmpty(_header.getRpItemSubHead());
		checkLength(itemSubHeadings);
		
		int recordNum = nextAvailableRecord();
		_header.setRpItemSubHead(recordNum);
		writeIndexedValues(recordNum, itemSubHeadings.toArray(new String[itemSubHeadings.size()]));
	}
	
	private void checkEmpty(int recordNum) {
		if (recordNum > 0) {
			throw new RuntimeException("The record has already been allocated.");
		}
	}
	
	private void checkLength(List<?> values) {
		if (values.size() != _header.getNC()) {
			throw new RuntimeException("There must be one value for each character");
		}
	}
}
