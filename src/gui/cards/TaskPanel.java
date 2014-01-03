package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import model.Task;
import javax.swing.BoxLayout;

public class TaskPanel extends JPanel {
	private Task target;

	/**
	 * Create the panel.
	 */
	public TaskPanel(Task task) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel(task.getName()));
		add(new JLabel("Max mark: " + task.getMaxMark()));
		add(new JLabel(task.getDescription()));
		add(new JSeparator());
	}

}
