package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel(task.getName()));
		add(new JLabel(task.getDescription()));
		add(new JSeparator());
		cp = new CriterionPanel();
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}
	

}
