package swingui.cards;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Table cell editor that displays a ScaleBox.
 * 
 * @author Robyn
 *
 */
public class ScaleBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -4779563116518206271L;
	private ScaleBox box;
	
	public ScaleBoxCellEditor() {
		box = new ScaleBox();
	}

	/**
	 * The value of a cell containing a ScaleBox is the currently selected item in the
	 * ScaleBox.
	 */
	@Override
	public Object getCellEditorValue() {
		return box.getSelectedScale();
	}

	/**
	 * The table cell editor is a ScaleBox: a dropdown box that allows the user to
	 * select an existing scale or create a new one.
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		box.setSelectedItem(value);
		return box;
	}

}
