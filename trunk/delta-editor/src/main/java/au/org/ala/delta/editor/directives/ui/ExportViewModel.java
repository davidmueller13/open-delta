package au.org.ala.delta.editor.directives.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.editor.directives.DirectiveFileInfo;
import au.org.ala.delta.editor.model.EditorViewModel;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;

/**
 * Manages the data displayed in the ImportExportDialog during an export 
 * operation.
 */
public class ExportViewModel extends ImportExportViewModel {

	/**
	 * Builds the model for the export dialog from the directive files
	 * currently a part of the model.
	 * @return a new ImportExportViewModel initialised from the data set.
	 */
	@Override
	public void populate(EditorViewModel model) {

		int directiveFileCount = model.getDirectiveFileCount();

		List<DirectiveFileInfo> files = new ArrayList<DirectiveFileInfo>(
				directiveFileCount);

		for (int i = 1; i <= directiveFileCount; i++) {
			DirectiveFile dirFile = model.getDirectiveFile(i);

			DirectiveFileInfo info = new DirectiveFileInfo(
					dirFile.getShortFileName(), dirFile.getType());
			info.setDirectiveFile(dirFile);

			if (dirFile.isSpecsFile()) {
				setSpecsFile(info);
			} else if (dirFile.isItemsFile()) {
				setItemsFile(info);
			} else if (dirFile.isCharsFile()) {
				setCharactersFile(info);
			} else {
				files.add(info);
			}
		}
		setIncludedDirectivesFiles(files);
		setCurrentDirectory(new File(model.getDataSetPath()));
	}
}
