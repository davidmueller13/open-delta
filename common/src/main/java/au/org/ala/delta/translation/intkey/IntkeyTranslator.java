package au.org.ala.delta.translation.intkey;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.intkey.WriteOnceIntkeyCharsFile;
import au.org.ala.delta.intkey.WriteOnceIntkeyItemsFile;
import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.translation.DataSetTranslator;
import au.org.ala.delta.translation.FilteredDataSet;
import au.org.ala.delta.util.FileUtils;

/**
 * Translates a DELTA data set into the format used by the INTKEY program.
 */
public class IntkeyTranslator implements DataSetTranslator {

	private DeltaContext _context;
	private FilteredDataSet _dataSet;
	private CharacterFormatter _characterFormatter;
	
	public IntkeyTranslator(DeltaContext context, FilteredDataSet dataSet, CharacterFormatter characterFormatter) {
		_context = context;
		_dataSet = dataSet;
		_characterFormatter = characterFormatter;
	}

	@Override
	public void translateCharacters() {
		
		String fileName = _context.getOutputFileSelector().getIntkeyOutputFilePath();
		FileUtils.backupAndDelete(fileName);
		
		WriteOnceIntkeyCharsFile charsFile = new WriteOnceIntkeyCharsFile(
				_dataSet.getNumberOfFilteredCharacters(), fileName , BinFileMode.FM_APPEND);
		IntkeyCharactersFileWriter charsWriter = new IntkeyCharactersFileWriter(_context, _dataSet, _characterFormatter, charsFile);
		charsWriter.writeAll();
		charsFile.close();
	}
	
	@Override
	public void translateItems() {
		String fileName = _context.getOutputFileSelector().getIntkeyOutputFilePath();
		FileUtils.backupAndDelete(fileName);
		
		WriteOnceIntkeyItemsFile itemsFile = new WriteOnceIntkeyItemsFile(
				_dataSet.getNumberOfFilteredCharacters(), _dataSet.getNumberOfFilteredItems(), fileName, BinFileMode.FM_APPEND);
		IntkeyItemsFileWriter itemsWriter = new IntkeyItemsFileWriter(_context, _dataSet, itemsFile);
		itemsWriter.writeAll();
		itemsFile.close();
	}
	
	@Override
	public void translateOutputParameter(String parameterName) {}
}
