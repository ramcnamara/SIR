package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import model.QTask;
import javax.swing.BoxLayout;

public class QTaskPanel extends JPanel {
	private QTask target;

	/**
	 * Create the panel.
	 */
	public QTaskPanel(QTask task) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel(task.getName()));
		add(new JLabel(task.getDescription()));
		add(new JSeparator());
	}

}
