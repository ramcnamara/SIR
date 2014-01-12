package gui.cards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import model.Criterion;
import model.Mark;
import model.Task;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class TaskPanel extends JScrollPane implements CriterionContainer {

	private static final long serialVersionUID = 1L;
	private Task target;
	private Mark parent;
	private JPanel contents;
	private CriterionPanel cp;
	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextField tfMaxMark;
	private JTextArea taMarkerInstructions;
	private JCheckBox chckbxGroupTask;
	private JCheckBox chckbxAllowMarkerComment;
	private JCheckBox chckbxBonusTask;

	/**
	 * Create the panel.
	 * @param mark 
	 */
	public TaskPanel(Task task, Mark mark) {
		target = task;
		parent = mark;
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow,fill]", "[14px][][pref!,grow,top][][14px][][][fill]"));

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

		chckbxAllowMarkerComment = new JCheckBox(
				"Allow marker comment", task.hasComment());
		contents.add(chckbxAllowMarkerComment, "cell 1 6");
		cp = new CriterionPanel();
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 7 2 1,aligny top,grow");
		setViewportView(contents);

		chckbxGroupTask = new JCheckBox("Group task", task.isGroup());
		contents.add(chckbxGroupTask, "flowx,cell 1 4");

		chckbxBonusTask = new JCheckBox("Bonus task", task.getBonus());
		contents.add(chckbxBonusTask, "cell 1 5");
		if (task.getSubtasks() == null || task.getSubtasks().size() == 0)
			lblComputed.setVisible(false);
		else
			tfMaxMark.setEnabled(false);

		getVerticalScrollBar().setValue(0);
	}

	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}
	
	public void reset() {
		tfTaskName.setText(target.getName());
		tfMaxMark.setText("" + target.getMaxMark());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());
		
		chckbxAllowMarkerComment.setSelected(target.hasComment());
		chckbxGroupTask.setSelected(target.isGroup());
		chckbxBonusTask.setSelected(target.getBonus());
	}
	
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
	}


	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			getVerticalScrollBar().setValue(0);
			repaint();
		}
	}
	
	public Mark getParentTask() {
		return parent;
	}
}
