package swingui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


/**
 * Data model for the table that stores learning outcomes so that users can
 * select which are appropriate for each task. 
 * 
 * @author ram
 *
 */
public class OutcomesTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3528889301143274346L;
	public static final int WEIGHT_COL = 1;
	public static final int DESC_COL = 0;

	/**
	 * Each OutcomeEntry instance represents a single row of the table.
	 * 
	 * @author ram
	 *
	 */
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


	
	private ArrayList<OutcomeEntry> data;

	/**
	 * Constructor.  By default, no outcomes are selected.
	 * 
	 * @param outcomes a list of Strings containing the description of each learning outcome.
	 */
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
