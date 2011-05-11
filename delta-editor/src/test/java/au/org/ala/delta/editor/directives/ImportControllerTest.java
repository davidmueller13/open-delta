package au.org.ala.delta.editor.directives;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import junit.framework.TestCase;

import org.junit.Before;

import sun.awt.AppContext;
import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.directives.ui.ImportExportDialog.DirectiveFile;
import au.org.ala.delta.editor.directives.ui.ImportExportDialog.DirectiveType;
import au.org.ala.delta.editor.model.EditorDataModel;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSetFactory;
import au.org.ala.delta.model.AbstractObservableDataSet;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.UnorderedMultiStateCharacter;

/**
 * Tests the ImportController class.  The SuppressWarnings annotation is to prevent warnings
 * about accessing the AppContext which is required to do the thread synchronization 
 * necessary to make the tests run in a repeatable manner.
 */
@SuppressWarnings("restriction")
public class ImportControllerTest extends TestCase {

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
	private ImportController importer;
	
	/** The data set we are importing into */
	private AbstractObservableDataSet _dataSet;
	
	@Before
	public void setUp() {
		// Sure hope this won't throw a headless exception at some point...
		DeltaEditorTestHelper helper = new DeltaEditorTestHelper();
		_dataSet = (AbstractObservableDataSet)new SlotFileDataSetFactory().createDataSet("test");
		EditorDataModel model = new EditorDataModel(_dataSet);
		helper.setModel(model);
		
		importer = new ImportController(helper);
	}
	
	public void testSilentImport() throws Exception {
		
		File datasetDirectory = new File(getClass().getResource("/dataset").toURI());
		DirectiveFile specs = new DirectiveFile("specs", DirectiveType.CONFOR);
		DirectiveFile chars = new DirectiveFile("chars", DirectiveType.CONFOR);
		DirectiveFile items = new DirectiveFile("items", DirectiveType.CONFOR);
		
		List<DirectiveFile> files = Arrays.asList(new DirectiveFile[] {specs, chars, items});
		
		importer.doSilentImport(datasetDirectory, files);
		
		// Because the import happens on a background (daemon) thread, we have to wait until 
		// the import is finished before doing our assertions.
		waitForTaskCompletion();
		
		assertEquals(89, _dataSet.getNumberOfCharacters());
		// do a few random assertions
		Character character = _dataSet.getCharacter(10);
		assertEquals(10, character.getCharacterId());
		assertEquals("<adaxial> ligule <presence>", character.getDescription());
		assertEquals(CharacterType.UnorderedMultiState, character.getCharacterType());
		UnorderedMultiStateCharacter multiStateChar = (UnorderedMultiStateCharacter)character;
		assertEquals(2, multiStateChar.getNumberOfStates());
		assertEquals("<consistently> present <<implicit>>", multiStateChar.getState(1));
		assertEquals("absent <at least from upper leaves>", multiStateChar.getState(2));
		
		character = _dataSet.getCharacter(48);
		assertEquals("awns <of female-fertile lemmas, if present, number>", character.getDescription());
		assertEquals(CharacterType.IntegerNumeric, character.getCharacterType());
		assertEquals(48, character.getCharacterId());
		
		character = _dataSet.getCharacter(85);
		assertEquals(85, character.getCharacterId());
		assertEquals("<number of species>", character.getDescription());
		assertEquals(CharacterType.IntegerNumeric, character.getCharacterType());
		IntegerCharacter integerCharacter = (IntegerCharacter)character;
		assertEquals("species", integerCharacter.getUnits());
		
		
		assertEquals(14, _dataSet.getMaximumNumberOfItems());
		
		Item item = _dataSet.getItem(5);
		assertEquals(5, item.getItemNumber());
		
		// At the moment getDescription() strips RTF... probably should leave that to the formatter.
		//assertEquals("\\i{}Cynodon\\i0{} <Rich.>", item.getDescription());
		
		assertEquals("\\i{}Cynodon\\i0{} <Rich.>", item.getDescription());
		assertEquals("4-60(-100)", item.getAttribute(_dataSet.getCharacter(2)).getValue());
		assertEquals("3", item.getAttribute(_dataSet.getCharacter(60)).getValue());
		
	}
	
	/**
	 * This is a way we can wait for the import task to complete without adding extra methods
	 * to the ImportController just for the unit test.
	 */
	private void waitForTaskCompletion() throws Exception {
		final AppContext appContext = AppContext.getAppContext();
	     ExecutorService executorService =
	            (ExecutorService) appContext.get(SwingWorker.class);
	     executorService.shutdown();
	     executorService.awaitTermination(10, TimeUnit.SECONDS);
	}
	
}
