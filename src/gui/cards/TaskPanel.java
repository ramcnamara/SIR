package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import model.Criterion;
import model.Task;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;

public class TaskPanel extends JPanel implements CriterionContainer {

	private static final long serialVersionUID = 1L;
	private Task target;
	private CriterionPanel cp;

	/**
	 * Create the panel.
	 */
	public TaskPanel(Task task) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel(task.getName()));
		add(new JLabel("Max mark: " + task.getMaxMark()));
		add(new JLabel(task.getDescription()));
		cp = new CriterionPanel();
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(cp);
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}

}
