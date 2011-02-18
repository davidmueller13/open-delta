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
package au.org.ala.delta;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.ImplicitValue;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericCharacter;
import au.org.ala.delta.model.OrderedMultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.StateValue;
import au.org.ala.delta.model.UnorderedMultiStateCharacter;
import au.org.ala.delta.slotfile.Attribute;
import au.org.ala.delta.slotfile.CharType;
import au.org.ala.delta.slotfile.DeltaVOP;
import au.org.ala.delta.slotfile.TextType;
import au.org.ala.delta.slotfile.VOCharBaseDesc;
import au.org.ala.delta.slotfile.VOCharBaseDesc.CharTextInfo;
import au.org.ala.delta.slotfile.VOCharTextDesc;
import au.org.ala.delta.slotfile.VODirFileDesc;
import au.org.ala.delta.slotfile.VODirFileDesc.Dir;
import au.org.ala.delta.slotfile.VOItemAdaptor;
import au.org.ala.delta.slotfile.VOItemDesc;
import au.org.ala.delta.slotfile.VOTextCharacter;
import au.org.ala.delta.util.CodeTimer;
import au.org.ala.delta.util.IProgressObserver;

public class DeltaFileReader {
	
	public static DeltaContext readDeltaFile(String filename) {
		return readDeltaFile(filename, null);
	}

	public static DeltaContext readDeltaFile(String filename, IProgressObserver observer) {
		
		if (observer != null) {
			observer.progress("Loading file " + filename, 0);
		}
		
		CodeTimer t = new CodeTimer("Reading Delta File");
		
		

		DeltaContext context = new DeltaContext();
		
		context.setVariable("HEADING", filename);

		DeltaVOP vop = new DeltaVOP(filename, false);
		
 		context.VOP = vop;

		int nChars = vop.getDeltaMaster().getNChars();
		int nItems = vop.getDeltaMaster().getNItems();
		int nDirectives = vop.getDeltaMaster().getNItems();		
		
		int progmax = nChars + nItems + nDirectives;
		int progress = 0;

		context.setNumberOfCharacters(nChars);
		context.setMaximumNumberOfItems(nItems);
		context.initializeMatrix();

		// Chars
		for (int i = 1; i <= nChars; ++i) {
			int charId = vop.getDeltaMaster().uniIdFromCharNo(i);
			VOCharBaseDesc charDesc = (VOCharBaseDesc) vop.getDescFromId(charId);

			CharTextInfo txtInfo = charDesc.readCharTextInfo(0, (short) 0);
			VOCharTextDesc textDesc = (VOCharTextDesc) vop.getDescFromId(txtInfo.charDesc);
			List<String> states = new ArrayList<String>();
			String[] text = textDesc.ReadAllText(TextType.RTF, states);

			int charType = charDesc.getCharType();
			Character chr = null;
			switch (charType) {
				case CharType.TEXT:
					chr = new VOTextCharacter(charDesc, i);
					break;
				case CharType.INTEGER:
					chr = new IntegerCharacter(i);
					break;
				case CharType.UNORDERED:
					chr = new UnorderedMultiStateCharacter(i);					
					break;
				case CharType.ORDERED:
					chr = new OrderedMultiStateCharacter(i);
					break;
				case CharType.REAL:
					chr = new RealCharacter(i);
					break;
				default:
					throw new RuntimeException("Unrecognized character type: " + charType);
			}
			
			chr.setDescription(text[0]);
			chr.setNotes(text[1]);
			
			if (chr instanceof MultiStateCharacter) {
				populateStates(charDesc, (MultiStateCharacter) chr, states);
			} else if (chr instanceof NumericCharacter) {
				if (charDesc.getNStates() > 0) {					
					@SuppressWarnings("unchecked")
					NumericCharacter<? extends Number> c = (NumericCharacter<? extends Number>) chr;
					int idState = charDesc.uniIdFromStateNo(1);
                    if (idState < states.size()) {					
                    	c.setUnits(states.get(idState));
                    }
				}
			}

			chr.setDescription(textDesc.readFeatureText(TextType.RTF));				
			chr.setMandatory(charDesc.testCharFlag(VOCharBaseDesc.CHAR_MANDATORY));
			chr.setExclusive(charDesc.testCharFlag(VOCharBaseDesc.CHAR_EXCLUSIVE));
			context.addCharacter(chr, chr.getCharacterId());
			progress++;
			if (observer != null && progress % 10 == 0) {
				int percent = (int) (((double) progress / (double) progmax) * 100);
				observer.progress("Loading characters", percent);
			}
		}

		CodeTimer t1 = new CodeTimer("Reading Items");
		// Items...
		for (int i = 1; i <= nItems; ++i) {
			int itemId = vop.getDeltaMaster().uniIdFromItemNo(i);
			VOItemDesc itemDesc = (VOItemDesc) vop.getDescFromId(itemId);			
			Item item = new VOItemAdaptor(itemDesc, i);
			item.setDescription(itemDesc.getAnsiName());
			context.addItem(item, item.getItemId());
			
			List<Attribute> attrs = itemDesc.readAllAttributes();
			for (Attribute attr : attrs) {
				if (attr.getCharId() >= 0) {
					int charIndex = vop.getDeltaMaster().charNoFromUniId(attr.getCharId());
					Character c = context.getCharacter(charIndex);
					
					StateValue sv = new StateValue(c, item, attr.getAsText(0, vop));
					context.getMatrix().setValue(charIndex, i, sv);
				}				
			}
			
			progress++;
			if (observer != null && progress % 10 == 0) {
				int percent = (int) (((double) progress / (double) progmax) * 100);
				observer.progress("Loading Items", percent);
			}
			
		}
		
		t1.stop(false);
		
		CodeTimer t2 = new CodeTimer("Reading Directives");
		
		for (int i = 1; i <= vop.getDeltaMaster().getNDirFiles(); ++i) {
			int uid = vop.getDeltaMaster().uniIdFromDirFileNo(i);
			VODirFileDesc dirDesc = (VODirFileDesc) vop.getDescFromId(uid);
			DirectiveFile dirFile = new DirectiveFile(dirDesc.getFileName());
			dirFile.progType = dirDesc.getProgType();
			 for (int j = 1; j <= dirDesc.getNDirectives(); ++j) {
				 Dir d = dirDesc.readDirective(j);
				if (observer != null && progress % 10 == 0) {
					int percent = (int) (((double) progress / (double) progmax) * 100);
					observer.progress("Loading Directives", percent);
				}				 
			 }
		}
		
		t2.stop(true);
		
		t.stop(false);

		if (context.VOP == null) {
			vop.close();
		}

		return context;

	}
	
	private static void populateStates(VOCharBaseDesc charBase, MultiStateCharacter chr, List<String> states) {
		chr.setNumberOfStates(charBase.getNStatesUsed());

		int uncodedImplicitStateId = charBase.getUncodedImplicit();
		if (uncodedImplicitStateId != VOCharBaseDesc.STATEID_NULL) {
			ImplicitValue iv = new ImplicitValue();
			iv.setUncoded(uncodedImplicitStateId);
			iv.setCoded(charBase.getCodedImplicit());
			chr.setImplicitValueStateId(iv);
		}

		for (int j = 0; j < charBase.getNStates(); ++j) {
			int stateId = charBase.uniIdFromStateNo(j + 1);
			chr.setState(j + 1, states.get(stateId));
		}
		
	}

}

class DirectiveFile {

	public int progType;
	public int type;
	public String name;
	public List<String> directives = new ArrayList<String>();
	
	public DirectiveFile(String name) {
		this.name = name;
	}
}
