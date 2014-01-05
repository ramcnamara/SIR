package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import model.Criterion;
import model.Task;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

public class TaskPanel extends JPanel implements CriterionContainer {

	private static final long serialVersionUID = 1L;
	private Task target;
	private CriterionPanel cp;

	/**
	 * Create the panel.
	 */
	public TaskPanel(Task task) {
		setLayout(new MigLayout("", "[450px]", "[14px][14px][14px][258px]"));
		add(new JLabel(task.getName()), "cell 0 0,alignx left,aligny center");
		add(new JLabel("Max mark: " + task.getMaxMark()), "cell 0 1,alignx left,aligny center");
		add(new JLabel(task.getDescription()), "cell 0 2,alignx left,aligny center");
		cp = new CriterionPanel();
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(cp, "cell 0 3,growx,aligny top");
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}

}
