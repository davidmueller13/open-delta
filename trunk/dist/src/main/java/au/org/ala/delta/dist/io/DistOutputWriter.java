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
package au.org.ala.delta.dist.io;

import java.text.DecimalFormat;
import java.util.Iterator;

import au.org.ala.delta.dist.DistContext;
import au.org.ala.delta.dist.DistanceMatrix;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.translation.FilteredDataSet;
import au.org.ala.delta.translation.FilteredItem;
import au.org.ala.delta.translation.PrintFile;

/**
 * Writes the distance matrix and item names to the files identified by their
 * respective directives.
 */
public class DistOutputWriter {

	protected static final int OUTPUT_COLUMNS = 10;
	protected DecimalFormat _decimalFormat;
	protected DistContext _context;
	protected FilteredDataSet _dataSet;
	
	public DistOutputWriter(DistContext context, FilteredDataSet dataSet) {
		_context = context;
		_dataSet = dataSet;
		_decimalFormat = new DecimalFormat(".00000");
	
	}
	
	
	public void writeOutput(DistanceMatrix matrix) throws Exception {
		DistOutputFileManager outputFileManager = _context.getOutputFileManager();
		
		PrintFile outputFile = getOutputFile();
		
		PrintFile namesFile = outputFileManager.getNamesFile();
		writeNames(namesFile);
		
		writeMatrix(matrix, outputFile);
		
	}

	protected PrintFile getOutputFile() {
		DistOutputFileManager outputFileManager = _context.getOutputFileManager();
		
		PrintFile outputFile = outputFileManager.getOutputFile();
		outputFile.setTrimInput(false);
		outputFile.setIndentOnLineWrap(false);
		outputFile.setIndent(0);
		outputFile.setLineWrapIndent(0);
		outputFile.setPrintWidth(80);
		return outputFile;
	}
	
	protected void writeMatrix(DistanceMatrix matrix, PrintFile outputFile) {
		Iterator<FilteredItem> items = _dataSet.filteredItems();
		while (items.hasNext()) {
			FilteredItem item1 = items.next();
			writeDistances(matrix, item1, outputFile);
			outputFile.printBufferLine();
		}
		
	}
	
	protected void writeDistances(DistanceMatrix matrix, FilteredItem item1, PrintFile outputFile) {
		Iterator<FilteredItem> itemsToCompareAgainst = _dataSet.filteredItems();
		outputFile.indent();
		while (itemsToCompareAgainst.hasNext()) {
			FilteredItem item2 = itemsToCompareAgainst.next();
			
			double value = matrix.get(item1.getItemNumber(), item2.getItemNumber());
			if (item2.getItemNumber() > item1.getItemNumber()) {
				outputFile.writeJustifiedText("    "+_decimalFormat.format(value), -1);
			}
		}
	}

	private void writeNames(PrintFile namesFile) {
		
		Iterator<FilteredItem> items = _dataSet.filteredItems();
		while (items.hasNext()) {
			Item item = items.next().getItem();
			writeName(item, namesFile);
		}
	}
	
	protected void writeName(Item item, PrintFile namesFile) {
		namesFile.outputLine(item.getDescription());
	}
}
