package gui.cards;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ScaleBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
	private ScaleBox box;

	private static final long serialVersionUID = 1L;
	
	public ScaleBoxCellEditor() {
		box = new ScaleBox();
	}

	@Override
	public Object getCellEditorValue() {
		return box.getSelectedScale();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		box.setSelectedItem(value);
		return box;
	}

}
