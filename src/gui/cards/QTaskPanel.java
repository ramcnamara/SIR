package gui.cards;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Criterion;
import model.QTask;
import javax.swing.BoxLayout;

public class QTaskPanel extends JPanel implements CriterionContainer {

	private static final long serialVersionUID = 1L;
	private QTask target;
	private CriterionPanel cp;

	/**
	 * Create the panel.
	 */
	public QTaskPanel(QTask task) {
		add(new JLabel(task.getName()));
		add(new JLabel(task.getDescription()));
		cp = new CriterionPanel();
		add(cp);
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}
	

}
