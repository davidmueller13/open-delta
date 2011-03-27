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
package au.org.ala.delta.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.ActionMap;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.ui.AboutBox;

public class MatrixViewer extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private EditorDataModel _dataSet;
	private JTable _table;
	private JTable _fixedColumns;
	private MatrixTableModel _model;
	private AttributeEditor _stateEditor;
	private ItemColumnModel _fixedModel;

	@Resource
	String windowTitle;

	public MatrixViewer(EditorDataModel dataSet) {
		super();

		ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(MatrixViewer.class);
		resourceMap.injectFields(this);

		this.setTitle(String.format(windowTitle, dataSet.getName()));

		_dataSet = dataSet;
		new InternalFrameDataModelListener(this, dataSet, windowTitle);
		_model = new MatrixTableModel(dataSet);

		this.setSize(new Dimension(600, 500));

		_table = new JTable(_model);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		_table.getTableHeader().setSize(new Dimension(_table.getColumnModel().getTotalColumnWidth(), 100));
		_table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		_table.setCellSelectionEnabled(true);
		_table.setDefaultRenderer(Object.class, new AttributeCellRenderer());

		_fixedModel = new ItemColumnModel(dataSet);
		_fixedColumns = new JTable(_fixedModel);
		_fixedColumns.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		_fixedColumns.getTableHeader().setPreferredSize(new Dimension(_table.getColumnModel().getTotalColumnWidth(), 100));
		_fixedColumns.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_fixedColumns.setBackground(SystemColor.control);
		_fixedColumns.setDefaultRenderer(Object.class, new FixedColumnRenderer());
		
		_fixedColumns.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = _fixedColumns.getSelectedRow();
				_table.getSelectionModel().setSelectionInterval(row, row);
			}
		});
				
		new TableRowResizer(_fixedColumns, _table);

		ListSelectionListener listener = new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = _table.getSelectedRow();
				_fixedColumns.getSelectionModel().setSelectionInterval(row, row);
				int charId = _table.getSelectedColumn() + 1;
				int itemId = _table.getSelectedRow() + 1;

				if (charId > 0 && itemId > 0) {
					au.org.ala.delta.model.Character selectedCharacter = _dataSet.getCharacter(charId);
					Item selectedItem = _dataSet.getItem(itemId);
					_stateEditor.bind(selectedCharacter, selectedItem);
				}

			}
		};

		_table.getSelectionModel().addListSelectionListener(listener);
		_table.getColumnModel().getSelectionModel().addListSelectionListener(listener);

		_table.getTableHeader().setDefaultRenderer(new TableHeaderRenderer());

		final JScrollPane scrollpane = new JScrollPane(_table);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		final JScrollPane fixedScrollPane = new JScrollPane(_fixedColumns);
		fixedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		fixedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		fixedScrollPane.setPreferredSize(new Dimension(120, 200));

		scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				fixedScrollPane.getViewport().setViewPosition(scrollpane.getViewport().getViewPosition());
				fixedScrollPane.invalidate();
				fixedScrollPane.updateUI();
			}
		});

		JSplitPane content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		// JPanel content = new JPanel(new BorderLayout());
		content.add(scrollpane, JSplitPane.RIGHT);
		content.add(fixedScrollPane, JSplitPane.LEFT);
		content.setDividerSize(4);
		content.setDividerLocation(180);

		_stateEditor = new AttributeEditor(_dataSet);

		JSplitPane divider = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		divider.setDividerLocation(getHeight() - 200);
		divider.setResizeWeight(1);

		divider.setTopComponent(content);
		divider.setBottomComponent(_stateEditor);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(divider, BorderLayout.CENTER);

		_table.getActionMap().getParent().remove("paste");

		ActionMap actionMap = Application.getInstance().getContext().getActionMap(this);
		javax.swing.Action copyAll = actionMap.get("copyAll");
		if (copyAll != null) {
			_table.getActionMap().put("copyAll", copyAll);
		}

		javax.swing.Action copySelection = actionMap.get("copy");
		if (copySelection != null) {
			_table.getActionMap().put("copy", copySelection);
		}
		
		selectCell(0, 0);
	}
	
	private void selectCell(int rowIndex, int colIndex) {
		_table.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
		_table.getColumnModel().getSelectionModel().setSelectionInterval(colIndex, colIndex);		
	}

	@Action(block = BlockingScope.APPLICATION)
	public Task<Void, Void> copy() {
		return new CopySelectedTask(Application.getInstance());
	}

	@Action(block = BlockingScope.APPLICATION)
	public Task<Void, Void> copyAll() {
		return new CopyAllTask(Application.getInstance());
	}

	abstract class ClipboardCopyTask extends Task<Void, Void> {

		protected String CELL_SEPERATOR = "\t";
		protected String EOL = "\n";
		private long _totalCells = 0;
		private long _cellsCopied = 0;
		private int _lastPercent = 0;

		public ClipboardCopyTask(Application application) {
			super(application);
		}

		protected void setTotalCells(long total) {
			_totalCells = total;
		}

		protected void incrementCellsCopied() {
			_cellsCopied++;
			int newPercent = (int) (((double) _cellsCopied) / ((double) _totalCells) * 100);
			if (newPercent != _lastPercent) {
				_lastPercent = newPercent;
				setProgress(_lastPercent);
			}
		}

	}

	class CopySelectedTask extends ClipboardCopyTask {

		public CopySelectedTask(Application application) {
			super(application);
		}

		@Override
		protected Void doInBackground() throws Exception {

			message("copying");

			StringBuilder b = new StringBuilder();

			int[] cols = _table.getSelectedColumns();
			int[] rows = _table.getSelectedRows();

			setTotalCells(cols.length * rows.length);

			// Now for each row...
			for (int i = 0; i < rows.length; ++i) {
				int row = rows[i];

				for (int j = 0; j < cols.length; ++j) {
					int col = cols[j];
					String value = (String) _model.getValueAt(row, col);
					if (col > 0) {
						b.append(CELL_SEPERATOR);
					}
					b.append(value);
					incrementCellsCopied();
				}
				b.append(EOL);
			}

			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(b.toString()), null);

			return null;
		}

	}

	class CopyAllTask extends CopySelectedTask {

		public CopyAllTask(Application application) {
			super(application);
		}

		@Override
		protected Void doInBackground() throws Exception {

			message("copying");

			StringBuilder b = new StringBuilder();

			setTotalCells(_model.getColumnCount() * _model.getRowCount());

			// First do row headers, which are item descriptions
			b.append("(Items)");
			for (int i = 0; i < _model.getColumnCount(); i++) {
				b.append(CELL_SEPERATOR).append(_model.getColumnName(i));
			}
			b.append(EOL);

			// Now for each row...

			for (int row = 0; row < _model.getRowCount(); ++row) {
				b.append(_fixedModel.getValueAt(row, 0));
				// and for each data item (column)
				for (int col = 0; col < _model.getColumnCount(); ++col) {
					String value = (String) _model.getValueAt(row, col);
					b.append(CELL_SEPERATOR).append(value);
					incrementCellsCopied();
				}
				b.append(EOL);
			}

			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(b.toString()), null);

			return null;
		}

	}

	
}

class BottomLineBorder extends LineBorder {

	private static final long serialVersionUID = 1L;

	public BottomLineBorder(Color color) {
		super(color);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		Color oldColor = g.getColor();
		g.setColor(lineColor);
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		g.setColor(oldColor);
	}
}

class ItemColumnModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private DeltaDataSet _dataSet;
	private ItemFormatter _formatter;

	@Resource
	String columnName;

	public ItemColumnModel(DeltaDataSet dataSet) {
		ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(AboutBox.class);
		resourceMap.injectFields(this);
		_dataSet = dataSet;
		boolean includeNumber = false;
		boolean stripRtf = true;
		_formatter = new ItemFormatter(includeNumber, stripRtf);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int column) {
		return columnName;
	}

	@Override
	public int getRowCount() {
		return _dataSet.getMaximumNumberOfItems();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Item item = _dataSet.getItem(row + 1);
		return _formatter.formatItemDescription(item);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Item.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
