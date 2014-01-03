package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Criterion;

public class CriterionPanel extends JPanel {
	private Criterion target;

	/**
	 * Create the panel.
	 */
	public CriterionPanel(Criterion criterion) {
		target = criterion;
		
		String levels = "";
		for (String level:criterion.getScale().asArray()) {
			levels += " ";
		}
		
		add(new JLabel(criterion.getDescription()));
		add(new JLabel(levels));

	}

}
