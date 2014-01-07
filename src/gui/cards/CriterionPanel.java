package gui.cards;



import java.util.ArrayList;

import javax.swing.JPanel;

import model.Criterion;

import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class CriterionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
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
	
	public void addCriterion(Criterion criterion) {
		JTextArea taName = new JTextArea(criterion.getName());
		add (taName, "cell 1 " + row);
		ScaleBox taScale = new ScaleBox(criterion);
		add (taScale, "cell 3 " + row);
		row += 1;
		data.add(new CriterionData(taName, taScale));
	}
	
	public ArrayList<Criterion> getCriteria() {
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
