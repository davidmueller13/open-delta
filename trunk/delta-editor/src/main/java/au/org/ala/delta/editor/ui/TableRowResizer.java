package au.org.ala.delta.editor.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;

public class TableRowResizer extends MouseInputAdapter {

	public static Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);

	private int mouseYOffset, resizingRow;
	private Cursor otherCursor = resizeCursor;
	private JTable _fixedColumnsTable;
	private JTable _mainTable;

	public TableRowResizer(JTable fixedTable, JTable mainTable) {
		this._fixedColumnsTable = fixedTable;
		_fixedColumnsTable.addMouseListener(this);
		_fixedColumnsTable.addMouseMotionListener(this);
		_mainTable = mainTable;
	}

	private int getResizingRow(Point p) {
		return getResizingRow(p, _fixedColumnsTable.rowAtPoint(p));
	}

	private int getResizingRow(Point p, int row) {
		int resizingRow = -1;
		if (row != -1) {
			
			int col = _fixedColumnsTable.columnAtPoint(p);
			if (col != -1) {
					
				Rectangle r = _fixedColumnsTable.getCellRect(row, col, true);
				r.grow(0, -3);
				if (!r.contains(p)) {
					
					int midPoint = r.y + r.height / 2;
					resizingRow = (p.y < midPoint) ? row - 1 : row;
				}
			}
		}
		// Disable drag and drop during a resize operation.
		_fixedColumnsTable.setDragEnabled(resizingRow == -1);
		return resizingRow;
	}

	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();

		resizingRow = getResizingRow(p);
		mouseYOffset = p.y - _fixedColumnsTable.getRowHeight(resizingRow);
	}

	private void swapCursor() {
		Cursor tmp = _fixedColumnsTable.getCursor();
		_fixedColumnsTable.setCursor(otherCursor);
		otherCursor = tmp;
	}

	public void mouseMoved(MouseEvent e) {
		if ((getResizingRow(e.getPoint()) >= 0) != (_fixedColumnsTable.getCursor() == resizeCursor)) {
			swapCursor();
		}
	}

	public void mouseDragged(MouseEvent e) {
		int mouseY = e.getY();

		if (resizingRow >= 0) {
			
			int newHeight = mouseY - mouseYOffset;
			if (newHeight > 0) {
				_fixedColumnsTable.setRowHeight(resizingRow, newHeight);
				_mainTable.setRowHeight(resizingRow, newHeight);
			}

		}
	}
}