package gui.cards;

import java.util.ArrayList;

import model.Criterion;

import javax.swing.table.AbstractTableModel;

public class CriterionTableModel extends AbstractTableModel {
	private ArrayList<Criterion> data = new ArrayList<Criterion>();

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
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
		data.add(criterion);
	}

}
