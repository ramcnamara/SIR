package gui.cards;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Criterion;
import model.Task;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

public class TaskPanel extends JScrollPane implements CriterionContainer {

	private static final long serialVersionUID = 1L;
	private Task target;
	private JPanel contents;
	private CriterionPanel cp;

	/**
	 * Create the panel.
	 */
	public TaskPanel(Task task) {
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[450px]", "[14px][14px][14px][258px]"));
		contents.add(new JLabel(task.getName()), "cell 0 0,alignx left,aligny center");
		contents.add(new JLabel("Max mark: " + task.getMaxMark()), "cell 0 1,alignx left,aligny center");
		contents.add(new JLabel(task.getDescription()), "cell 0 2,alignx left,aligny center");
		cp = new CriterionPanel();
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 3,growx,aligny top");
		setViewportView(contents);
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}

}
