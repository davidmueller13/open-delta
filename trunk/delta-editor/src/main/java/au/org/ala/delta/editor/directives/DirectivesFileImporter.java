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
package au.org.ala.delta.editor.directives;

import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.slotfile.Directive;
import au.org.ala.delta.editor.slotfile.directive.ConforDirType;
import au.org.ala.delta.editor.slotfile.directive.DistDirType;
import au.org.ala.delta.editor.slotfile.directive.IntkeyDirType;
import au.org.ala.delta.editor.slotfile.directive.KeyDirType;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile.DirectiveType;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class DirectivesFileImporter {
	
	private EditorViewModel _model;
	private ImportContext _context;

	public DirectivesFileImporter(EditorViewModel model, ImportContext context) {
		_model = model;
		_context = context;
	}
	
	public boolean importDirectivesFile(DirectiveFileInfo fileInfo, File toImport, DirectiveImportHandler handler) throws IOException {
		boolean result = false;
		InputStreamReader reader = null;
		try {
			FileInputStream fileIn = new FileInputStream(toImport);
		
			reader = new InputStreamReader(fileIn, _context.getFileEncoding());
		
		    result =  importDirectivesFile(fileInfo, reader, handler);
		}
		finally {
			IOUtils.closeQuietly(reader);
		}
		return result;
		
	}
	
	
	/**
	 * Parses the supplied stream into the directives file identified by
	 * the supplied DirectiveFileInfo.  If a directive exists with the same
	 * name, the contents of that file will be replaced, otherwise a new
	 * directives file will be added.
	 * If the import fails the directives file will not be updated.
	 * @param fileInfo identifies the directives file to import into.
	 * @param directivesReader a reader containing the raw directives data.
	 * @param handler handler for directives as they are imported.
	 * @return true if the import succeeded, false otherwise.
	 */
	public boolean importDirectivesFile(DirectiveFileInfo fileInfo, Reader directivesReader, DirectiveImportHandler handler) {
		
		String name = fileInfo.getName();
		
		DirectiveFile existing =  _model.getDirectiveFile(name);
		DirectiveFile directiveFile = _model.addDirectiveFile(_model.getDirectiveFileCount()+1, name, fileInfo.getType());
        if (fileInfo.isItemsFile()) {
            directiveFile.setItemsFile(true);
        }
        if (fileInfo.isCharsFile()) {
            directiveFile.setCharsFile(true);
        }
        if (fileInfo.isSpecsFile()) {
            directiveFile.setSpecsFile(true);
        }
 		directiveFile.setLastModifiedTime(System.currentTimeMillis());
		DirectiveFileImporter importer = new DirectiveFileImporter(handler, directivesOfType(directiveFile.getType()));
		
		_context.setDirectiveFile(directiveFile);
		
		// Looks like we skip the specs file if we have non zero items or chars.....
		try {
			importer.parse(directivesReader, _context);
			
			if (importer.success()) {
				if (existing != null) {
					copyToExistingFile(existing, directiveFile);
					_model.deleteDirectiveFile(directiveFile);
				}
			}
			else {
				_model.deleteDirectiveFile(directiveFile);
			}
		}
		catch (Exception e) {
			_model.deleteDirectiveFile(directiveFile);
			e.printStackTrace();
		}
		
		return importer.success();
	}

	private void copyToExistingFile(DirectiveFile existing, DirectiveFile directiveFile) {
		existing.setDirectives(directiveFile.getDirectives());
		existing.setLastModifiedTime(directiveFile.getLastModifiedTime());
		existing.setFlags(directiveFile.getFlags());
	}
	
	protected Directive[] directivesOfType(DirectiveType type) {
		Directive[] directives = null;
		switch(type) {
		case CONFOR:
			directives = ConforDirType.ConforDirArray;
			break;
		case INTKEY:
			directives = IntkeyDirType.IntkeyDirArray;
			break;
		case KEY:
			directives = KeyDirType.KeyDirArray;
			break;
		case DIST:
			directives = DistDirType.DistDirArray;
			break;
		}
		return directives;
	}
}
