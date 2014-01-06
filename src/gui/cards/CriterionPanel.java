package gui.cards;



import javax.swing.JPanel;

import model.Criterion;

import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

public class CriterionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int row = 0;

	/**
	 * Create the panel.
	 * 
	 */
	public CriterionPanel() {
		setLayout(new MigLayout("", "unrel[pref!,grow]rel[]", "[]"));
	}
	
	public void addCriterion(Criterion criterion) {
		add (new JTextArea(criterion.getName()), "cell 1 " + row);
		add (new ScaleBox(criterion), "cell 3 " + row);
		row += 1;
	}
}
