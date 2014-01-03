package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Criterion;

public class CriterionPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public CriterionPanel(Criterion criterion) {
		
		String levels = "";
		for (String level:criterion.getScale().asArray()) {
			levels += level + " ";
		}
		
		add(new JLabel(criterion.getName()));
		add(new JLabel(levels));

	}

}
