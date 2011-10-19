package au.org.ala.delta.editor.directives;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JFileChooser;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.directives.ui.ExportViewModel;
import au.org.ala.delta.editor.directives.ui.ImportExportDialog;
import au.org.ala.delta.editor.directives.ui.ImportExportViewModel;
import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.slotfile.Directive;
import au.org.ala.delta.editor.slotfile.DirectiveInstance;
import au.org.ala.delta.editor.slotfile.directive.ConforDirType;
import au.org.ala.delta.editor.slotfile.directive.DirectiveInOutState;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile.DirectiveType;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.util.FileUtils;

/**
 * The ExportController manages the process of exporting a set of directives
 * files from the data set to the file system.
 */
public class ExportController {
	private DeltaEditor _editor;
	private EditorViewModel _model;
	private ImportExportViewModel _exportModel;
	private ImportExportDialog _exportDialog;
	private ResourceMap _resources;
	private ActionMap _actions;
	
	public ExportController(DeltaEditor context) {
		_editor = context;
		_model = context.getCurrentDataSet();
		_resources = _editor.getContext().getResourceMap();
		_actions = _editor.getContext().getActionMap(this);
	}

	public void begin(PropertyChangeListener listener) {
		_exportModel = new ExportViewModel();
		_exportModel.populate(_model);

		_exportDialog = new ImportExportDialog(_editor.getMainFrame(), _exportModel, "ExportDialog");
		_exportDialog.setDirectorySelectionAction(_actions.get("changeExportDirectory"));
		
		_editor.show(_exportDialog);

		if (_exportDialog.proceed()) {
			List<DirectiveFileInfo> files = _exportModel.getSelectedFiles();
			File selectedDirectory = _exportModel.getCurrentDirectory();
			_model.setExportPath(selectedDirectory.getAbsolutePath());
			doExport(selectedDirectory, files, listener);
		}
	}
	
	public void begin() {
		begin(null);
	}

	
	/**
	 * Exports the supplied directives files into the specified directory.
	 * @param selectedDirectory the directory to export the files to.
	 * @param files the files to export.
	 */
	public void doExport(File selectedDirectory, List<DirectiveFileInfo> files, PropertyChangeListener listener) {

		// Do the export on a background thread.
		DoExportTask exportTask = new DoExportTask(selectedDirectory, files, false);
		if (listener != null) {
			exportTask.addPropertyChangeListener(listener);
		}
		exportTask.execute();
	}
	
	@Action
	public void changeExportDirectory() {
		JFileChooser directorySelector = new JFileChooser(_exportModel.getCurrentDirectory());
		String directoryChooserTitle = _resources.getString("ExportDialog.directoryChooserTitle");
		directorySelector.setDialogTitle(directoryChooserTitle);
		directorySelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		directorySelector.setAcceptAllFileFilterUsed(false);
		
		int result = directorySelector.showOpenDialog(_exportDialog);
		if (result == JFileChooser.APPROVE_OPTION) {
			_exportModel.setCurrentDirectory(directorySelector.getSelectedFile());
			_exportDialog.updateUI();
		}
	}

	public void doSilentExport(File selectedDirectory,
			List<DirectiveFileInfo> files) {
		new DoExportTask(selectedDirectory, files, true).execute();
	}
	
	public void writeDirectivesFile(DirectiveFile file, DirectiveInOutState state) {
		try {
			List<DirectiveInstance> directives = file.getDirectives();

			for (int i = 0; i < directives.size(); i++) {
				writeDirective(directives.get(i), state);
				if (i != directives.size() - 1) {
					state.getPrinter().writeBlankLines(1, 0);
				}
			}
			state.getPrinter().printBufferLine();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (state.getPrinter() != null) {
				state.getPrinter().close();
			}
		}
	}
	
	private File createExportFile(DirectiveFile file, String directoryPath) {
		String fileName = file.getShortFileName();
		FileUtils.backupAndDelete(fileName, directoryPath);
		
		FilenameUtils.concat(directoryPath, fileName);
		File directivesFile = new File(directoryPath + fileName);
		
		return directivesFile;
	}

	protected void writeDirective(DirectiveInstance directive,
			DirectiveInOutState state) {
		
		state.setCurrentDirective(directive);
		Directive directiveInfo = directive.getDirective();

		directiveInfo.getOutFunc().process(state);
	}

	public class DoExportTask extends ImportExportTask {
		
		public DoExportTask(File directory, List<DirectiveFileInfo> files, boolean silent) {
			super(_editor, _model, directory, files, "export", silent);
		}

		@Override
		protected Void doInBackground() throws Exception {
			
			publish(_status);

			buildSpecialDirFiles(_files);
			
			DirectiveInOutState state = new StatusUpdatingState(_model, _status);
			for (DirectiveFileInfo file : _files) {
				DirectiveFile dirFile = file.getDirectiveFile();
				if (dirFile != null) {
					File output = createExportFile(dirFile, _directoryName);
					state.setPrintStream(new PrintStream(output, "utf-8"));
					_status.setCurrentFile(file);
					writeDirectivesFile(dirFile, state);
				}

				publish(_status);
			}
			_status.finish();
			publish(_status);

			return null;
		}
	}
	
	/**
	 * Wraps the DirectiveInOutState to allow status updates during the 
	 * export operation.
	 */
	private class StatusUpdatingState extends DirectiveInOutState {
		
		private ImportExportStatus _status;
		public StatusUpdatingState(EditorViewModel model, ImportExportStatus status) {
			super(model);
			_status = status;
		}

		@Override
		public void setCurrentDirective(DirectiveInstance directive) {
			
			_status.setCurrentDirective(directive);
			super.setCurrentDirective(directive);
		}
	}
	
	private void buildSpecialDirFiles(List<DirectiveFileInfo> files) {
	 
		DirectiveFile specsFile = null;
		DirectiveFile charsFile = null;
		DirectiveFile itemsFile = null;
		
		// Should probably handle this is a more generalized way
		// For now, we'll use specialized handling for these data directives
		// which reference "core" data rather than actions. We do this to
		// make certain that these directives will be exported somewhere.
		// At the moment, in addition to the stuff residing in specs, chars,
		// and items, we need CHARACTER NOTES, CHARACTER IMAGES, TAXON IMAGES,
		// and OVERLAY FONTS

		boolean hasCNotes = false;
		boolean hasCImages = false;
		boolean hasTImages = false;
		boolean hasOFonts = false;

		boolean hasCNotesDir = false;
		boolean hasCImagesDir = false;
		boolean hasTImagesDir = false;
		boolean hasOFontsDir = false;

		boolean CNotesInChars = false;

		// Find out what data we have that might require adding new directives
		for (int i=1; i<=_model.getNumberOfCharacters() && !(hasCNotes && hasCImages); i++) {
		 
		    Character character = _model.getCharacter(i);
		    if (!hasCNotes) {
		        hasCNotes = StringUtils.isNotEmpty(character.getNotes());
		    }
		    if (!hasCImages) {
		        hasCImages = character.getImageCount() > 0;
		    }
		}

		for (int i=1; i<=_model.getMaximumNumberOfItems() && !hasTImages; i++) {
		    Item item = _model.getItem(i);
		    if (item.getImageCount() > 0)
		        hasTImages = true;
		}

		ImageSettings imageInfo = _model.getImageSettings();	  
		if (imageInfo != null && imageInfo.getFontCount() > 0) {
		    hasOFonts = true;
		}

		for (int i=1; i<=_model.getDirectiveFileCount(); i++) {
		    DirectiveFile dirFile = _model.getDirectiveFile(i);
		    if (dirFile.getType() == DirectiveType.CONFOR)  {
		        if (dirFile.isSpecsFile()) {
		    	    specsFile = dirFile;
		    	}
		    	if (dirFile.isCharsFile()) {
		    	    charsFile = dirFile;
		    	}
		    	if (dirFile.isItemsFile()) {
		    	    itemsFile = dirFile;
		    	}
		          
		    	List<DirectiveInstance> curDirectives = dirFile.getDirectives();
		          
		        for (DirectiveInstance directive : curDirectives) {
		            int dirType = directive.getDirective().getNumber();
		            if (dirType == ConforDirType.CHARACTER_NOTES) {
		                if (dirFile.isCharsFile())
		                    CNotesInChars = true;
		                else
		                    hasCNotesDir = true;
		                  
		            } else if (dirType == ConforDirType.CHARACTER_IMAGES)
		                 hasCImagesDir = true;
		            else if (dirType == ConforDirType.TAXON_IMAGES)
		                hasTImagesDir = true;
		            else if (dirType == ConforDirType.OVERLAY_FONTS)
		                hasOFontsDir = true;
		        }
		    }
		    // Break out of the loop if we have everything we need
		    if (specsFile != null && itemsFile != null && charsFile != null && // Have basic files?
		       (hasCNotesDir || !hasCNotes) &&    // Have Char Notes, or don't need to?
		       (hasCImagesDir || !hasCImages) &&  // Have Char Images, or don't need to?
		       (hasTImagesDir || !hasTImages) &&  // Have Tax Images, or don't need
		       (hasOFontsDir || !hasOFonts))      // Have Overlay Fonts, or don't need
		        break;
		    }

		if (specsFile == null) { // Need to create an internal "specs" file
		    specsFile = createDirectiveFile(files, "specs", "~ Dataset specifications.");
		    specsFile.setSpecsFile(true);
		}

		// Make sure specs file has all relevant directives...
		boolean hadDataBufferSize = false; //Confor::DATA_BUFFER_SIZE    // Level 1
		boolean hadNumbChars = false; // Confor::NUMBER_OF_CHARACTERS
		boolean hadMaxStates = false; // Confor::MAXIMUM_NUMBER_OF_STATES
		boolean hadMaxItems = false;  // Confor::MAXIMUM_NUMBER_OF_ITEMS
		boolean hadCharTypes = false; // Confor::CHARACTER_TYPES         // Level 2
		boolean hadNStates = false;   // Confor::NUMBERS_OF_STATES       // Level 3
		boolean hadImplicitValues = false; // Confor::IMPLICIT_VALUES    // Level 4
		boolean hadDependentChars = false; // Confor::DEPENDENT_CHARACTERS
		boolean hadMandatoryChars = false; // Confor::MANDATORY_CHARACTERS
		  
		List<DirectiveInstance> curDirectives = specsFile.getDirectives();
		int origDirCount = curDirectives.size();
		int dirIter = 0;
		for (int curLevel = 1; curLevel < 5; ++curLevel) {
		    while (dirIter < curDirectives.size())  {
		        int dirType = curDirectives.get(dirIter).getDirective().getNumber();
		        if (ConforDirType.ConforDirArray[dirType].getLevel() > curLevel)
		            break;
		        switch (dirType) {
		        case ConforDirType.DATA_BUFFER_SIZE :
		            hadDataBufferSize = true;
		            break;
		        case ConforDirType.NUMBER_OF_CHARACTERS:
		            hadNumbChars = true;
		            break;
		        case ConforDirType.MAXIMUM_NUMBER_OF_STATES:
		            hadMaxStates = true;
		            break;
		        case ConforDirType.MAXIMUM_NUMBER_OF_ITEMS:
		            hadMaxItems = true;
		            break;
		        case ConforDirType.CHARACTER_TYPES:
		            hadCharTypes = true;
		            break;
		        case ConforDirType.NUMBERS_OF_STATES:
		            hadNStates = true;
		            break;
		        case ConforDirType.IMPLICIT_VALUES:
		            hadImplicitValues = true;
		            break;
		        case ConforDirType.DEPENDENT_CHARACTERS:
		        case ConforDirType.APPLICABLE_CHARACTERS:
		            hadDependentChars = true;
		            break;
		        case ConforDirType.MANDATORY_CHARACTERS:
		            hadMandatoryChars = true;
		            break;
		        default:
		            break;
		        }
		        ++dirIter;
		    }
		    if (curLevel == 1) {
		        if (!hadDataBufferSize) {
		            specsFile.addIntegerDirective(dirIter++, ConforDirType.get(ConforDirType.DATA_BUFFER_SIZE), 4000);
		        }
		        if (!hadNumbChars) {
		      	    specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.NUMBER_OF_CHARACTERS));
		        }
		        if (!hadMaxStates)
		            specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.MAXIMUM_NUMBER_OF_STATES));
		        if (!hadMaxItems)
		        	  specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.MAXIMUM_NUMBER_OF_ITEMS));
		        }
		    else if (curLevel == 2) {
		        if (!hadCharTypes)
		        	  specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.CHARACTER_TYPES));
		        }
		    else if (curLevel == 3) {
		        if (!hadNStates)
		        	  specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.NUMBERS_OF_STATES));
		        }
		    else if (curLevel == 4) {
		        if (!hadImplicitValues)
		            specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.IMPLICIT_VALUES));
		        if (!hadDependentChars)
		            specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.DEPENDENT_CHARACTERS));
		        if (!hadMandatoryChars)
		            specsFile.addNoArgDirective(dirIter++, ConforDirType.get(ConforDirType.MANDATORY_CHARACTERS));
		    }
		}
		  
		if (hasCNotes && !hasCNotesDir) { 
		    createCharacterNotesDirectivesFile(files);
		    hasCNotesDir = true;
		}

		if (charsFile == null) { // Need to create an internal "chars" file
		    charsFile = createDirectiveFile(files, "chars", "~ Character list.");
		    charsFile.setCharsFile(true);
		}

		curDirectives = charsFile.getDirectives();
		  
		int charListDirectiveIndex = -1;
		for (int i=0; i<curDirectives.size(); i++) {
		    if (curDirectives.get(i).getDirective().getNumber() == ConforDirType.CHARACTER_LIST) {
		        charListDirectiveIndex = i;
		        break;
		    }
		}

		boolean hadCharList = origDirCount > 0 && charListDirectiveIndex != -1;

		  /*
		  // If a *CHARACTER NOTES directive is required, insert just before the *CHARACTER LIST directive (if present)
		  //

		  if (hasCNotes && !hasCNotesDir && !CNotesInChars)
		    {
		      if (hadCharList)
		        curDirectives.insert(charListIter, TDir(Confor::CHARACTER_NOTES));
		      else
		        curDirectives.push_back(TDir(Confor::CHARACTER_NOTES));
		    }
		  */
		  // Wait a minute. The above wasn't such a great idea if one then later imports
		  // a CNOTES directives file. What then? How do we REMOVE this *CHARACTER NOTES
		  // directive from here when we no longer need it? So what we do here is
		  // remove the *CHARACTER NOTES directive from the CHARS file if is not
		  // needed - that is, if there are NO character notes or it there is a
		  // *CHARACTER NOTES directive found in any other directives file.
		  // (I should never have tried inserting character notes here in the first place!!!)

		if (CNotesInChars && (hasCNotesDir || !hasCNotes)) {
		    for (dirIter=0; dirIter<curDirectives.size(); dirIter++) {
		        if (curDirectives.get(dirIter).getDirective().getNumber() == ConforDirType.CHARACTER_NOTES) {
		            charsFile.deleteDirective(dirIter);
		            break;
		        }
		    }
		}
		if (!hadCharList)
		    charsFile.addNoArgDirective(curDirectives.size(), ConforDirType.get(ConforDirType.CHARACTER_LIST));

		if (itemsFile == null) {
		    itemsFile = createDirectiveFile(files, "items", "~ Item descriptions");
		    itemsFile.setItemsFile(true);
		}

		boolean hadItemDesc = false; // Confor::ITEM_DESCRIPTIONS
		curDirectives = itemsFile.getDirectives();
		origDirCount = curDirectives.size();
		for (dirIter = 0; dirIter<curDirectives.size(); ++dirIter) {
		    if (curDirectives.get(dirIter).getDirective().getNumber() == ConforDirType.ITEM_DESCRIPTIONS) {
		        hadItemDesc = true;
		        break;
		    }
		}

		if (!hadItemDesc)
		     itemsFile.addNoArgDirective(curDirectives.size(), ConforDirType.get(ConforDirType.ITEM_DESCRIPTIONS));

		if (hasCImages && !hasCImagesDir) {
		    createDirectiveFile(files, "cimages", "~ Character images.", ConforDirType.CHARACTER_IMAGES);
		}
		if (hasTImages && !hasTImagesDir) {
		    createDirectiveFile(files, "timages", "~ Taxon images.", ConforDirType.TAXON_IMAGES);
		}
		if (hasOFonts && !hasOFontsDir) {
		    createDirectiveFile(files, "ofonts", "~ Set fonts for image overlays.", ConforDirType.OVERLAY_FONTS);
		}
	}

	private void updateDirectiveFileInfo(List<DirectiveFileInfo> files, String name, DirectiveFile directivesFile) {
		for (DirectiveFileInfo file : files) {
			if (file.getName().equals(name)) {
				file.setDirectiveFile(directivesFile);
				break;
			}
		}
	}

	private void createCharacterNotesDirectivesFile(List<DirectiveFileInfo> fileInfo) {
		createDirectiveFile(fileInfo, "cnotes", "~ Character notes.", ConforDirType.CHARACTER_NOTES);
	}
	
	private DirectiveFile createDirectiveFile(List<DirectiveFileInfo> fileInfo, String fileName, String showDirectiveText) {
		int numFiles = _model.getDirectiveFileCount();
		DirectiveFile dirFile = _model.addDirectiveFile(numFiles+1, fileName, DirectiveType.CONFOR);
		dirFile.addTextDirective(0, ConforDirType.get(ConforDirType.SHOW), showDirectiveText);
		updateDirectiveFileInfo(fileInfo, fileName, dirFile);
		
		return dirFile;
	}
	
	private DirectiveFile createDirectiveFile(List<DirectiveFileInfo> fileInfo, String fileName, String showDirectiveText, int directive) {
		DirectiveFile dirFile = createDirectiveFile(fileInfo, fileName, showDirectiveText);
		dirFile.addNoArgDirective(1, ConforDirType.get(directive));
		return dirFile;
	}
}
