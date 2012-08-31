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

import au.org.ala.delta.directives.OutputParameters.OutputParameter;
import au.org.ala.delta.translation.PrintFile;

public class Command extends ParameterTranslator {
	private String _value;
	private int _trailingLines;
	
	private static final String COMMAND_TERMINATOR = ";";
	
	public Command(PrintFile outputFile, String value) {
		this(outputFile, value, 0);
	}
	
	public Command(PrintFile outputFile, String value, int trailingBlankLines) {
		super(outputFile);
		_value = value;
		_trailingLines = trailingBlankLines;
	}
	@Override
	public void translateParameter(OutputParameter parameter) {
		_outputFile.outputLine(_value+COMMAND_TERMINATOR);
		_outputFile.writeBlankLines(_trailingLines, 0);
	}
}
