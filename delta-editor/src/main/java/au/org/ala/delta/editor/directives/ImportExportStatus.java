package au.org.ala.delta.editor.directives;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.directives.AbstractDeltaContext;
import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.directives.Heading;
import au.org.ala.delta.directives.Show;
import au.org.ala.delta.rtf.RTFBuilder;
import au.org.ala.delta.rtf.RTFBuilder.Alignment;


/**
 * The status of the current import or export operation. Used as a model for the ImportExportStatusDialog.
 */
public class ImportExportStatus  {

	private String heading;
	private String importDirectory;
	private String currentFile;
	private String currentDirective;
	
	private int totalLines;
	private int totalErrors;
	
	private int lineInCurentFile;
	private int errorsInCurrentFile;
	
	private String textFromLastShowDirective;

	private RTFBuilder _logBuilder;
	
	
	public ImportExportStatus() {
		_logBuilder = new RTFBuilder();
		_logBuilder.startDocument();
		_logBuilder.setAlignment(Alignment.CENTER);
		_logBuilder.appendText("DELTA - IMPORT LOG");
		_logBuilder.appendText("Dataset : ");
	}
	
	/**
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * @param heading the heading to set
	 */
	public void setHeading(String heading) {
		this.heading = heading;
	}

	/**
	 * @return the importDirectory
	 */
	public String getImportDirectory() {
		return importDirectory;
	}

	/**
	 * @param importDirectory the importDirectory to set
	 */
	public void setImportDirectory(String importDirectory) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		
		this.importDirectory = importDirectory;
		_logBuilder.appendText("Import directory: "+importDirectory);
		_logBuilder.appendText("Import begun :"+dateFormat.format(new Date()));
		
	}

	/**
	 * @return the currentFile
	 */
	public String getCurrentFile() {
		return currentFile;
	}

	/**
	 * @param currentFile the currentFile to set
	 */
	public void setCurrentFile(String currentFile) {
		
		if (StringUtils.isNotEmpty(currentFile)) {
			_logBuilder.increaseIndent();
			_logBuilder.appendText("Import succeeded");
			_logBuilder.decreaseIndent();
		}
		
		this.currentFile = currentFile;
		errorsInCurrentFile = 0;
		
		_logBuilder.setAlignment(Alignment.LEFT);
		_logBuilder.appendText("");
		_logBuilder.appendText("Directives file: \\b " + currentFile + " \\b0");
		_logBuilder.increaseIndent();
		_logBuilder.appendText("File type ");
		
		_logBuilder.decreaseIndent();
	}

	/**
	 * @return the currentDirective
	 */
	public String getCurrentDirective() {
		return currentDirective;
	}

	/**
	 * @param currentDirective the currentDirective to set
	 */
	public void setCurrentDirective(AbstractDirective<? extends AbstractDeltaContext> directive, String data) {
		currentDirective = directive.getName();
		if (Arrays.equals(Show.CONTROL_WORDS, directive.getControlWords())) {
			_logBuilder.increaseIndent();
			_logBuilder.appendText("*"+directive.getName()+" "+data);
			textFromLastShowDirective = data;
		}
		else if (Arrays.equals(Heading.CONTROL_WORDS, directive.getControlWords())) {
			heading = data;
		}
	}

	/**
	 * @return the totalLines
	 */
	public int getTotalLines() {
		return totalLines;
	}

	/**
	 * @param totalLines the totalLines to set
	 */
	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}

	public void error(String message) {
		incrementErrors();
		_logBuilder.appendText(message);
		_logBuilder.increaseIndent();
		_logBuilder.appendText("Import \\b failed \\b0 .");
		_logBuilder.decreaseIndent();
	}
	
	private void incrementErrors() {
		totalErrors++;
		errorsInCurrentFile++;
	}
	
	/**
	 * @return the totalErrors
	 */
	public int getTotalErrors() {
		return totalErrors;
	}


	/**
	 * @return the lineInCurentFile
	 */
	public int getLineInCurentFile() {
		return lineInCurentFile;
	}

	/**
	 * @param lineInCurentFile the lineInCurentFile to set
	 */
	public void setLineInCurentFile(int lineInCurentFile) {
		this.lineInCurentFile = lineInCurentFile;
	}

	/**
	 * @return the errorsInCurrentFile
	 */
	public int getErrorsInCurrentFile() {
		return errorsInCurrentFile;
	}

	/**
	 * @param errorsInCurrentFile the errorsInCurrentFile to set
	 */
	public void setErrorsInCurrentFile(int errorsInCurrentFile) {
		this.errorsInCurrentFile = errorsInCurrentFile;
	}

	/**
	 * @return the textFromLastShowDirective
	 */
	public String getTextFromLastShowDirective() {
		return textFromLastShowDirective;
	}

	/**
	 * @param textFromLastShowDirective the textFromLastShowDirective to set
	 */
	public void setTextFromLastShowDirective(String textFromLastShowDirective) {
		this.textFromLastShowDirective = textFromLastShowDirective;
	}
	
	public String getImportLog() {
		return _logBuilder.toString() + "}\n";
	}
	
	
}
