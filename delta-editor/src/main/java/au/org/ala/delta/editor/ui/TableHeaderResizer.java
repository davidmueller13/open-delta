package au.org.ala.delta.editor.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

/**
 * The TableHeaderResizer listens for mouse events on the grid view table header component
 * and responds to a press and drag operation within 3 pixels of the bottom of the component
 * with a resize operation.
 */
public class TableHeaderResizer extends MouseInputAdapter {

	public static Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);

	private int mouseYOffset;
	boolean resizing = false;
	private Cursor otherCursor = resizeCursor;
	private DropIndicationTableHeader _tableHeader;
	private JScrollPane _scrollPane;
	private JScrollPane _tableRowHeaderScrollPane;

	/**
	 * Creates a new TableHeaderResizer capable of resizing the table header in the context
	 * of the grid view.
	 * @param tableHeader the table header to make resizable.
	 * @param scrollPane the scrollpane the main table is in.  This is required so the table 
	 * header viewport preferred size can be adjusted.
	 * @param tableRowScrollPane the scrollpane the table row header is in.  This is required
	 * so the header of the table row header can be resized consistently with the main table header.
	 */
	public TableHeaderResizer(DropIndicationTableHeader tableHeader, JScrollPane scrollPane, JScrollPane tableRowScrollPane) {
		this._tableHeader = tableHeader;
		_tableHeader.addMouseListener(this);
		_tableHeader.addMouseMotionListener(this);
		_scrollPane = scrollPane;
		_tableRowHeaderScrollPane = tableRowScrollPane;
	}


	public void mousePressed(MouseEvent e) {
		if (inResizeZone(e)) {
			resizing = true;
			_tableHeader.setDragEnabled(false);
			Point p = e.getPoint();
			mouseYOffset = p.y - _tableHeader.getPreferredSize().height;
		}
		
	}

	private void swapCursor() {
		Cursor tmp = _tableHeader.getCursor();
		_tableHeader.setCursor(otherCursor);
		otherCursor = tmp;
	}

	public void mouseMoved(MouseEvent e) {
		
		if (inResizeZone(e)) {
			if (!(_tableHeader.getCursor() == resizeCursor)) {
				swapCursor();
				_tableHeader.setDragEnabled(false);
			}
		}
		else {
			if (_tableHeader.getCursor() == resizeCursor) {
				swapCursor();
				_tableHeader.setDragEnabled(true);
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		resizing = false;
		_tableHeader.setDragEnabled(true);
		
	}
	
	private boolean inResizeZone(MouseEvent e) {
		if (e.getSource() == _tableHeader) {
			return e.getY() >= _scrollPane.getColumnHeader().getHeight()-3;
		}
		else {
			return false;
		}
	}

	public void mouseDragged(MouseEvent e) {
		int mouseY = e.getY();

		if (resizing) {
			
			int newHeight = mouseY - mouseYOffset;
			if (newHeight > 0) {
				
				_tableHeader.setPreferredSize(new Dimension(1000, newHeight));
				_scrollPane.getColumnHeader().setPreferredSize(new Dimension(1000, newHeight));
				_tableRowHeaderScrollPane.getColumnHeader().setPreferredSize(new Dimension(10000, newHeight));
				_scrollPane.revalidate();
				_tableRowHeaderScrollPane.revalidate();
			}

		}
	}
}