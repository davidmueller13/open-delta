package au.org.ala.delta.editor.directives;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaTestCase;
import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.model.EditorDataModel;
import au.org.ala.delta.editor.slotfile.DeltaVOP;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile.DirectiveType;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSet;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSetFactory;

/**
 * Tests the ExportController class.  The SuppressWarnings annotation is to prevent warnings
 * about accessing the AppContext which is required to do the thread synchronization 
 * necessary to make the tests run in a repeatable manner.
 */
public class ExportControllerTest extends DeltaTestCase {

	/**
	 * Allows us to manually set the data set to be returned from the
	 * getCurrentDataSet method.
	 */
	private class DeltaEditorTestHelper extends DeltaEditor {
		private EditorDataModel _model;
		public void setModel(EditorDataModel model) {
			_model = model;
		}
		
		@Override
		public EditorDataModel getCurrentDataSet() {
			return _model;
		}
	}
	
	
	/** The instance of the class we are testing */
	private ExportController exporter;
	
	/** The data set we are exporting */
	private SlotFileDataSet _dataSet;
	
	@Before
	public void setUp() throws Exception {
		// Sure hope this won't throw a headless exception at some point...
		DeltaEditorTestHelper helper = new DeltaEditorTestHelper();
	
		File f = copyURLToFile("/SAMPLE.DLT");
			
		DeltaVOP vop = new DeltaVOP(f.getAbsolutePath(), false);
		
		SlotFileDataSetFactory factory = new SlotFileDataSetFactory(vop);
		
		_dataSet = (SlotFileDataSet)factory.createDataSet("test");
		EditorDataModel model = new EditorDataModel(_dataSet);
		helper.setModel(model);
		
		exporter = new ExportController(helper);
	}
	
	@Test
	public void testSilentExport() throws Exception {
		
		for (int i=1; i<=_dataSet.getDirectiveFileCount(); i++) {
			DirectiveFile directiveFile = _dataSet.getDirectiveFile(i);
			DirectiveFileInfo test = new DirectiveFileInfo(directiveFile.getFileName(), DirectiveType.CONFOR, directiveFile);
			
			List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {test});
			File tempDir = new File("/tmp");
			System.out.println(i+" : "+directiveFile.getShortFileName());
			exporter.new DoExportTask(tempDir, files).doInBackground();
		}
	}
	
	/**
	 * This tests the toint directives file which has:
	 * SHOW (DIRARG_TEXT)
	 * LISTING FILE (DIRARG FILE)
	 * HEADING (DIRARG_TEXT)
	 * REGISTRATION SUBHEADING (DIRARG_TEXT)
	 * TRANSLATE INTO (DIRARG_TRANSLATION)
	 * CHARACTERS FOR SYNONYMY (DIRARG_CHARLIST)
	 * OMIT PERIOD FOR CHARACTERS (DIRARG_CHARLIST)
	 * OMIT OR FOR CHARACTERS (DIRARG_CHARLIST)
	 * OMIT INNER COMMENTS (DIRARG_NONE)
	 * EXCLUDE CHARACTERS (DIRARG_CHARLIST)
	 * CHARACTER RELIABILIITES (DIRARG_CHARREALLIST)
	 * NEW PARAGRAPHS AT CHARACTERS (DIRARG_CHARLIST)
	 * ITEM SUBHEADINGS (DIRARG_CHARTEXTLIST)
	 * INPUT FILE (DIRARG_FILE)
	 * INTKEY OUTPUT FILE (DIRARG_FILE)
	 * 
	 * It also includes a commented out directive.
	 * 
	 */
	@Test
	public void testExportToInt() throws Exception {
		// toint is directive file 13 in the sample dataset.
		exportAndCheck(13, "toint");
	}
	
	/**
	 * This tests the toint directives file which has:
	 * COMMENT (DIRARG_COMMENT)
	 * PRINT WIDTH (DIRARG_INTEGER)
	 * TYPESETTING MARKS (DIRARG_TEXTLIST)
	 */
	@Test
	public void testExportMarkRtf() throws Exception {
		exportAndCheck(6, "markrtf");
	}
	
	private void exportAndCheck(int directiveFileNum, String directiveFileName) throws Exception {
		File directory = FileUtils.getTempDirectory();
		
		export(directory, directiveFileNum);
		
		String[] directives = read(directiveFileName);
		
		String actual = FileUtils.readFileToString(new File(directory, directiveFileName));
		actual = actual.replace("\r\n", "\n");
		String[] actualDirectives = actual.split("\\*");
		
		int i=0;
		for (String directive : directives) {
			assertEquals(directive, actualDirectives[i++]);
		}
	}
	
	/**
	 * Exports the directive file identified by directiveFileNum into the
	 * supplied directory.
	 * @param directory the directory to export the directives to.
	 * @param directiveFileNum the directives file to export.
	 * @throws Exception if there is an error.
	 */
	private void export(File directory, int directiveFileNum) throws Exception {
		DirectiveFile directiveFile = _dataSet.getDirectiveFile(directiveFileNum);
		DirectiveFileInfo test = new DirectiveFileInfo(directiveFile.getFileName(), DirectiveType.CONFOR, directiveFile);
		
		List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {test});
		
		exporter.new DoExportTask(directory, files).doInBackground();
	}
	
	private String[] read(String fileName) throws Exception {
		
		URL expected = getClass().getResource("expected_results/"+fileName);
	
		File f = new File(expected.toURI());
		String buffer = FileUtils.readFileToString(f);
		buffer = buffer.replace("\r\n", "\n");
		return buffer.toString().split("\\*");
	}
	
}
