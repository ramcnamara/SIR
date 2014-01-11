package gui.cards;

import java.util.ArrayList;

import model.Criterion;

import javax.swing.table.AbstractTableModel;

public class CriterionTableModel extends AbstractTableModel {


	private static final long serialVersionUID = 1L;
	private String[] tableHeaders = {"Name", "Scale"};
	private ArrayList<Criterion> data = new ArrayList<Criterion>();

	@Override
	public int getColumnCount() {
		return 2;
	}
	
	public String getColumnName(int idx) {
		return tableHeaders[idx];
	}

	@Override
	public int getRowCount() {
		return data.size();
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= data.size())
			// invalid row
			return null;
		
		if (columnIndex == 0)
			return data.get(rowIndex).getName();
		if (columnIndex == 1)
			return data.get(rowIndex).getScale();
		
		// invalid column
		return null;
	}
	
	public void addCriterion(Criterion criterion) {
		int row = data.size();
		data.add(criterion);
		fireTableRowsInserted(row, row);
	}
	
	public void removeRow(int row) {
		if (row >= 0 && row < data.size()) {
			data.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

}
