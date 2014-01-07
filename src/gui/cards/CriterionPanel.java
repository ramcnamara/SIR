package gui.cards;



import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Criterion;


import net.miginfocom.swing.MigLayout;

public class CriterionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Container for UI fields needed to enter information for a single Criterion.
	 * @author ram
	 *
	 */
	private class CriterionData {
		private JTextArea name;
		private ScaleBox scale;
		
		CriterionData(JTextArea name, ScaleBox scale) {
			this.name = name;
			this.scale = scale;
		}
		
		public Criterion toCriterion() {
			return new Criterion(name.getText(),  scale.getSelectedScale());	
		}
	}
	
	private ArrayList<CriterionData> data;

	private int row = 0;

	/**
	 * Create the panel.
	 * 
	 */
	public CriterionPanel() {
		setLayout(new MigLayout("", "unrel[pref!,grow]rel[]", "[]"));
		data = new ArrayList<CriterionData>();
	}
	
	
	/**
	 * Add editing fields representing a criterion to this panel.
	 * This also 
	 * @param criterion the criterion to add
	 */
	public void addCriterion(Criterion criterion) {
		JTextArea taName = new JTextArea(criterion.getName());
		add (taName, "cell 1 " + row);
		ScaleBox taScale = new ScaleBox(criterion);
		add (taScale, "cell 3 " + row);
		row += 1;
		data.add(new CriterionData(taName, taScale));
	}
	
	
	/**
	 * Creates an ArrayList of Criterion that reflect the current contents of the panel.
	 * This is called by TaskPane and QTaskPane and used for writing back to the model class.
	 * @return an ArrayList of freshly-created Criterion objects
	 */
	public ArrayList<Criterion> getCriteria() {
		if (data == null || data.size() == 0)
			return null;
		
		ArrayList<Criterion> res = new ArrayList<Criterion>();
		for (CriterionData c: data) {
			res.add(c.toCriterion());
		}
		return res;
	}
	
	public void reset() {
		removeAll();
		data = new ArrayList<CriterionData>();
	}
}
