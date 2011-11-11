package au.org.ala.delta.dist.io;

import java.io.PrintStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.io.OutputFileSelector;
import au.org.ala.delta.translation.PrintFile;

/**
 * Manages output files specific to the DIST program.
 */
public class DistOutputFileManager extends OutputFileSelector {

	private static final int DEFAULT_OUTPUT_FILE_WIDTH = 80;
	private static final String DEFAULT_NAMES_FILE_EXTENSION = ".nam";
	private static final String DEFAULT_OUTPUT_FILE_NAME = "dist.dis";
	
	private String _namesFileName;
	
	public DistOutputFileManager() {
		super(null);
		_namesFileName = "";
	}
	
	public void setNamesFileName(String fileName) {
		_namesFileName = fileName;
	}
	
	public PrintFile getNamesFile() throws Exception {
		
		PrintStream out = createPrintStream(getNamesFileName());
		PrintFile nameFiles = new PrintFile(out, DEFAULT_OUTPUT_FILE_WIDTH);
		
		return nameFiles;
	}
	
	protected String getNamesFileName() {
		if (StringUtils.isEmpty(_namesFileName)) {
			String outputFile = outputFile(OutputFileType.OUTPUT_FILE).getFileName();
			_namesFileName = FilenameUtils.getBaseName(outputFile);
			_namesFileName = _namesFileName + DEFAULT_NAMES_FILE_EXTENSION;
		}
		return _namesFileName;
	}

	@Override
	public PrintFile getOutputFile() {
		if (super.getOutputFile() == null) {
			try {
				setOutputFileName(DEFAULT_OUTPUT_FILE_NAME);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return super.getOutputFile();
	}

	
	
}
