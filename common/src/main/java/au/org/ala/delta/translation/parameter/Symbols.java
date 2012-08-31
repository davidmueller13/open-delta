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
package au.org.ala.delta.translation.parameter;

import java.util.Iterator;

import au.org.ala.delta.directives.OutputParameters.OutputParameter;
import au.org.ala.delta.model.IdentificationKeyCharacter;
import au.org.ala.delta.translation.FilteredDataSet;
import au.org.ala.delta.translation.PrintFile;

public class Symbols extends ParameterTranslator {

	private FilteredDataSet _dataSet;
	private boolean _quoteSymbols;
	private boolean _numberFromZero;
	private String _command;
	
	public Symbols(
			PrintFile outputFile, 
			FilteredDataSet dataSet,
			String command,
			boolean quoteSymbols, 
			boolean numberFromZero) {
		super(outputFile);
		_dataSet = dataSet;
		_quoteSymbols = quoteSymbols;
		_numberFromZero = numberFromZero;
		_command = command;
	}

	@Override
	public void translateParameter(OutputParameter parameter) {
		
		_outputFile.outputLine(symbols());
	}
	
	protected String symbols() {
		StringBuilder symbols = new StringBuilder();
		symbols.append(_command);
		Iterator<IdentificationKeyCharacter> characters = _dataSet.identificationKeyCharacterIterator();
		int max = 0;
		while (characters.hasNext()) {
			max = Math.max(max, characters.next().getNumberOfStates());
		}
		int first = _numberFromZero ? 0 : 1;
		
		if (_quoteSymbols) {
			symbols.append("\"");
		}
		writeSymbols(symbols, first, max+first-1);
		
		if (_quoteSymbols) {
			symbols.append("\"");
		}
		symbols.append(";");
		
		return symbols.toString();
	}
	
	protected void writeSymbols(StringBuilder symbols, int first, int last) {
		
		for (int i=first; i<last+first; i++) {
			symbols.append(StateEncoder.STATE_CODES[i]);
		}
		
	}

}
