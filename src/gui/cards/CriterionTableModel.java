package gui.cards;

import java.util.ArrayList;

import model.Criterion;
import model.Scale;

import javax.swing.table.AbstractTableModel;

/**
 * Data model used by the table in a CriterionPanel.
 * 
 * @author Robyn
 *
 */
public class CriterionTableModel extends AbstractTableModel {


	private static final long serialVersionUID = 1L;
	private String[] tableHeaders = {"Name", "Scale"};
	private ArrayList<Criterion> data = new ArrayList<Criterion>();

	@Override
	/**
	 * All criterion tables have precisely two columns: one for the description, one for the scale.
	 * 
	 * @return 2
	 */
	public int getColumnCount() {
		return 2;
	}
	
	/**
	 * Column names are hard coded.
	 * 
	 * @return "Name" for column 0, "Scale" for column 1
	 */
	public String getColumnName(int idx) {
		return tableHeaders[idx];
	}

	@Override
	/**
	 * Returns the number of rows in the table.
	 * 
	 * @return the number of rows in the table
	 */
	public int getRowCount() {
		return data.size();
	}
	
	@Override
	/**
	 * All table cells should be editable.
	 * 
	 * @return true
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	/**
	 * Gets and returns the contents of the selected table cell.
	 * 
	 * @return a String representing the criterion name for column 0, or a Scale for column 1.
	 */
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
	
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= data.size())
			// invalid row -- TODO should throw an exception
			return;
		
		if (columnIndex == 0) {
			if (value instanceof String)
				data.get(rowIndex).setName(value.toString());
		}
		else if (columnIndex == 1) {
			if (value instanceof Scale)
				data.get(rowIndex).setScale((Scale)value);
		}
		
		fireTableRowsUpdated(rowIndex, rowIndex);		
	}
	
	
	/**
	 * Add a new Criterion to the table.
	 * 
	 * @param criterion the Criterion to add
	 */
	public void addCriterion(Criterion criterion) {
		int row = data.size();
		data.add(criterion);
		fireTableRowsInserted(row, row);
	}
	
	
	/**
	 * Remove the row'th row of the table.
	 * 
	 * @param row the index of the row to remove
	 */
	public void removeRow(int row) {
		if (row >= 0 && row < data.size()) {
			data.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

}
