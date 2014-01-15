package gui.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import model.Checkbox;
import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.QTask;
import model.SubtaskTypeException;
import model.Task;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

/**
 * Panel that allows reading and editing of numerically-marked tasks.
 * 
 * @author Robyn
 * 
 */
public class TaskPanel extends JPanel implements CriterionContainer, Card, ActionListener {

	private static final long serialVersionUID = 1L;
	private Task target;
	private Mark parent;
	private JPanel contents;
	private JScrollPane scrollpane;
	private CriterionPanel cp;
	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextField tfMaxMark;
	private JTextArea taMarkerInstructions;
	private JCheckBox chckbxGroupTask;
	private JCheckBox chckbxAllowMarkerComment;
	private JCheckBox chckbxBonusTask;
	private MarkingScheme scheme;

	/**
	 * Create the panel.
	 * 
	 * @param task
	 *            the Task to display and, if required, save to
	 * @param mark
	 *            parent task, or null if this is a top-level task (needed for
	 *            saving)
	 * @param scheme
	 *            marking scheme (needed for change notification)
	 */
	public TaskPanel(Task task, Mark mark, MarkingScheme scheme) {
		target = task;
		parent = mark;
		this.scheme = scheme;

		setLayout(new MigLayout("", "[grow]", "[grow]"));

		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow,fill]", "[14px][][pref!,grow,top][][14px][][][grow,fill]"));

		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx right");

		tfTaskName = new JTextField(task.getName());
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);

		JLabel lblMaxMark = new JLabel("Max. mark");
		contents.add(lblMaxMark, "cell 0 1,alignx right");

		tfMaxMark = new JTextField("" + task.getMaxMark());
		contents.add(tfMaxMark, "flowx,cell 1 1,alignx left,aligny baseline");
		tfMaxMark.setColumns(10);

		JLabel lblComputed = new JLabel("(from subtasks)");
		contents.add(lblComputed, "cell 1 1,alignx left,aligny baseline, growx");
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 2,alignx right");

		taDescription = new JTextArea(task.getDescription());
		taDescription.setLineWrap(true);
		contents.add(taDescription, "cell 1 2,wmin 10,grow");
		taDescription.setColumns(10);

		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 3,alignx trailing");

		taMarkerInstructions = new JTextArea(task.getMarkerInstruction());
		contents.add(taMarkerInstructions, "cell 1 3,wmin 10,grow");
		taMarkerInstructions.setColumns(10);

		chckbxAllowMarkerComment = new JCheckBox("Allow marker comment",
				task.hasComment());
		contents.add(chckbxAllowMarkerComment, "cell 1 6");
		cp = new CriterionPanel();
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 7 2 1,aligny top,grow");
		scrollpane = new JScrollPane(contents);
		add(scrollpane, "cell 0 0,grow");

		chckbxGroupTask = new JCheckBox("Group task", task.isGroup());
		contents.add(chckbxGroupTask, "flowx,cell 1 4");

		chckbxBonusTask = new JCheckBox("Bonus task", task.getBonus());
		contents.add(chckbxBonusTask, "cell 1 5");
		if (task.getSubtasks() == null || task.getSubtasks().size() == 0)
			lblComputed.setVisible(false);
		else
			tfMaxMark.setEnabled(false);

		scrollpane.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Add a criterion to this task
	 * 
	 * @param c
	 *            the criterion to add
	 */
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}

	/**
	 * Re-reads the displayed data from the model, which has the effect of
	 * cancelling any changes that were to have been made.
	 */
	public void reset() {
		tfTaskName.setText(target.getName());
		tfMaxMark.setText("" + target.getMaxMark());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());

		chckbxAllowMarkerComment.setSelected(target.hasComment());
		chckbxGroupTask.setSelected(target.isGroup());
		chckbxBonusTask.setSelected(target.getBonus());
	}

	/**
	 * Stores displayed values back into the model.
	 */
	public void save() {
		target.setName(tfTaskName.getText());
		try {
			target.setMaxMark(Float.parseFloat(tfMaxMark.getText()));
		} catch (NumberFormatException e) {
			target.setMaxMark(0);
		}
		target.setDescription(taDescription.getText());
		target.setMarkerInstruction(taMarkerInstructions.getText());

		target.setHasComment(chckbxAllowMarkerComment.isSelected());
		target.setGroup(chckbxGroupTask.isSelected());
		target.setBonus(chckbxBonusTask.isSelected());
		scheme.refresh();
	}

	/**
	 * Overload of setVisible so that it resets the vertical scrollbar to the
	 * top on redisplay.
	 * 
	 * @param true to set the panel visible, false to make it invisible
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			scrollpane.getVerticalScrollBar().setValue(0);
			repaint();
		}
	}

	/**
	 * Retrieve the model object representing the parent of the Task providing
	 * the data. This is used for deletion.
	 * 
	 * @return an instance of Mark representing the parent
	 */
	public Mark getParentTask() {
		return parent;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();

		if (cmd.equals("Add subtask")) {
			// Bring up dialog to select task type
			JRadioButton rbtask = new JRadioButton("Numerically-marked task");
			JRadioButton rbqtask = new JRadioButton("Qualitatively-marked task");
			JRadioButton rbcheckbox = new JRadioButton(
					"Checkbox (done or not done, no intermediate grades)");

			ButtonGroup group = new ButtonGroup();
			group.add(rbtask);
			group.add(rbqtask);
			group.add(rbcheckbox);

			Object[] components = {
					new JLabel("What type of task do you wish to add?"),
					rbtask, rbqtask, rbcheckbox };

			int result = JOptionPane.showConfirmDialog(null, components,
					"Add task", JOptionPane.OK_CANCEL_OPTION);

			// User closed or cancelled out of the dialog
			if (result == JOptionPane.CANCEL_OPTION
					|| result == JOptionPane.CLOSED_OPTION)
				return;

			Mark newTask;

			if (rbtask.isSelected())
				newTask = new Task();
			else if (rbqtask.isSelected())
				newTask = new QTask();
			else if (rbcheckbox.isSelected())
				newTask = new Checkbox();
			else
				return;

			newTask.setName("New task");

			try {
				target.addSubtask(newTask);
			} catch (SubtaskTypeException e) {
				System.out
						.println("Somehow adding a QTask is causing a SubtaskTypeException.");
			}

			scheme.refresh();
		}
		
		

	}

	@Override
	public Mark getTask() {
		return target;
	}

	public JButton getAddSubtaskButton() {
		JButton newButton = new JButton("Add subtask");
		newButton.setActionCommand("Add subtask");
		return newButton;
	}
}
