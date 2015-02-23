package swingui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public class OutcomesTableModel extends AbstractTableModel {
	public static final int WEIGHT_COL = 1;
	public static final int DESC_COL = 0;
	
	public class OutcomeEntry {

		
		private String description;
		private String weight;
		
		public OutcomeEntry(String desc, String w) {
			description = desc;
			weight = w;
		}
		
		// Setter
		public void setWeight(String w) {
			weight = w;
		}
		
		// Getters
		
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @return the weight
		 */
		public String getWeight() {
			return weight;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<OutcomeEntry> data;

	
	public OutcomesTableModel(List<String> outcomes) {
		data = new ArrayList<OutcomeEntry>();
		
		for (String outcome : outcomes) {
			data.add(new OutcomeEntry(outcome, "None"));
		}
	}
	
	@Override
	public String getColumnName(int index) {
		if (index == OutcomesTableModel.WEIGHT_COL)
			return "Weight";
		if (index == OutcomesTableModel.DESC_COL)
			return "Description";
		
		return "<no label>";
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == WEIGHT_COL)
			return data.get(row).getWeight();
		return data.get(row).getDescription();
	}
	
	@Override
	public void setValueAt(Object o, int row, int col) {
		// descriptions are immutable
		if (col == DESC_COL)
			return;
		
		if (o instanceof String)
			data.get(row).setWeight((String) o);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return col == WEIGHT_COL;
	}
}
