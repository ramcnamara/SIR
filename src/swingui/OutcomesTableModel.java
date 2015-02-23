package swingui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public class OutcomesTableModel extends AbstractTableModel {

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
	
	private static String[] columns = {"Outcome", "Weight"};
	private ArrayList<OutcomeEntry> data;

	
	public OutcomesTableModel(List<String> outcomes) {
		data = new ArrayList<OutcomeEntry>();
		
		for (String outcome : outcomes) {
			data.add(new OutcomeEntry(outcome, "None"));
		}
	}
	
	@Override
	public String getColumnName(int index) {
		return columns[index];
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 1)
			return data.get(row).getWeight();
		return data.get(row).getDescription();
	}
	
	@Override
	public void setValueAt(Object o, int row, int col) {
		// descriptions are immutable
		if (col == 0)
			return;
		
		if (o instanceof String)
			data.get(row).setWeight((String) o);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 1;
	}
}
