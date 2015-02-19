package swingui.cards;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.scheme.Criterion;
import model.scheme.Mark;
import model.scheme.MarkingScheme;
import model.scheme.QTask;
import model.scheme.Task;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;

import swingui.NavDisableEventListener;
import swingui.OutcomesDialog;

/**
 * Panel that allows reading and editing of numerically-marked tasks.
 * 
 * @author Robyn
 * 
 */
public class TaskPanel extends Card implements CriterionContainer, ActionListener {

	private static final long serialVersionUID = 1L;
	private Task target;
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
	private JCheckBox chckbxPenaltyTask;
	private JLabel lblLabel;
	private JTextField tfLabel;
	private Component horizontalGlue;
	private JLabel lblName;
	private JButton btnSelectLearningOutcomes;

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
	public TaskPanel(MarkingScheme scheme, NavDisableEventListener listener, Mark parent, Task task) {
		super(scheme, listener, parent);
		target = task;

		setLayout(new MigLayout("", "[grow]", "[grow]"));

		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow,fill]", "[][][][pref!,grow,top][][][][][][][grow,fill]"));
		
		// String and numeric data
		String label = "";
		String taskName = "";
		float maxMark = 0.0f;
		String description = "";
		String markerInstruction = "";
		
		if (task != null) {
			label += (task.getLabel() == null? "": task.getLabel());
			taskName += (task.getName() == null? "": task.getName());
			maxMark += task.getMaxMark();
			description += (task.getDescription() == null? "": task.getDescription());
			markerInstruction += (task.getMarkerInstruction() == null? "": task.getMarkerInstruction());
		}
		
		// default to "task" if no label set
		if (label.length() == 0)
			label = "Task";
		
		lblLabel = new JLabel("Label");
		contents.add(lblLabel, "cell 0 0,alignx trailing");
		
		tfLabel = new JTextField(label);
		contents.add(tfLabel, "flowx,cell 1 0,alignx left");
		tfLabel.setColumns(10);


		lblName = new JLabel(label + " name");
		contents.add(lblName, "cell 0 1,alignx right");

		tfTaskName = new JTextField(taskName);
		contents.add(tfTaskName, "cell 1 1,growx");
		tfTaskName.setColumns(10);

		JLabel lblMaxMark = new JLabel("Max. mark");
		contents.add(lblMaxMark, "cell 0 2,alignx right");

		// Add float to empty string to coerce it to string 
		tfMaxMark = new JTextField("" + maxMark);
		contents.add(tfMaxMark, "flowx,cell 1 2,alignx left,aligny baseline");
		tfMaxMark.setColumns(10);

		JLabel lblComputed = new JLabel("(from subtasks)");
		contents.add(lblComputed, "cell 1 2,growx,alignx left,aligny baseline");
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 3,alignx right");

		taDescription = new JTextArea(description);
		taDescription.setLineWrap(true);
		contents.add(taDescription, "cell 1 3,wmin 10,grow");
		taDescription.setColumns(10);

		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 4,alignx trailing");

		taMarkerInstructions = new JTextArea(markerInstruction);
		contents.add(taMarkerInstructions, "cell 1 4,wmin 10,grow");
		taMarkerInstructions.setColumns(10);
		
		btnSelectLearningOutcomes = new JButton("Select learning outcomes");
		contents.add(btnSelectLearningOutcomes, "cell 1 5");
		btnSelectLearningOutcomes.setActionCommand("Select learning outcomes");
		btnSelectLearningOutcomes.addActionListener(this);

		
		cp = new CriterionPanel(listener);
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 10 2 1,aligny top,grow");
		scrollpane = new JScrollPane(contents);
		add(scrollpane, "cell 0 0,grow");

		JXCollapsiblePane checkboxes = new JXCollapsiblePane();
		GridLayout boxstack = new GridLayout(0,1);	// 1 column, many rows
		checkboxes.setLayout(boxstack);
		contents.add(checkboxes, "cell 1 7");
		JButton toggle = new JButton(checkboxes.getActionMap().get("toggle"));
		contents.add(toggle, "cell 1 6");
		
		chckbxAllowMarkerComment = new JCheckBox("Allow marker comment",
				task.hasComment());
		checkboxes.add(chckbxAllowMarkerComment);
		
		chckbxGroupTask = new JCheckBox("Group task", task.isGroup());
		checkboxes.add(chckbxGroupTask);

		chckbxBonusTask = new JCheckBox("Bonus task", task.isBonus());
		checkboxes.add(chckbxBonusTask);
		
		chckbxPenaltyTask = new JCheckBox("Penalty task", task.isPenalty());
		checkboxes.add(chckbxPenaltyTask);
		
		horizontalGlue = Box.createHorizontalGlue();
		contents.add(horizontalGlue, "cell 1 0");

		boolean enableMarksEntry = true;
		
		if (task.getSubtasks() != null && task.getSubtasks().size() > 0)
			for (Mark m: task.getSubtasks()) {
				if (!(m instanceof QTask)) {
					enableMarksEntry = false;
					break;
				}
			}
		
		if (enableMarksEntry)
			lblComputed.setVisible(false);
		else {

			tfMaxMark.setEnabled(enableMarksEntry);
		}

		scrollpane.getVerticalScrollBar().setValue(0);
		
		// Add a listener to each component to allow navigation to be disabled when edits have been made
		chckbxAllowMarkerComment.addChangeListener(listener);
		chckbxBonusTask.addChangeListener(listener);
		chckbxGroupTask.addChangeListener(listener);
		chckbxPenaltyTask.addChangeListener(listener);
		taDescription.getDocument().addDocumentListener(listener);
		taMarkerInstructions.getDocument().addDocumentListener(listener);
		tfLabel.getDocument().addDocumentListener(listener);
		tfMaxMark.getDocument().addDocumentListener(listener);
		tfTaskName.getDocument().addDocumentListener(listener);
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
	 * Re-reads the displayed data from the model.scheme, which has the effect of
	 * cancelling any changes that were to have been made.
	 */
	public void reset() {
		tfLabel.setText(target.getLabel() == null || target.getLabel().length() == 0? "Task":target.getLabel());
		lblName.setText(tfLabel.getText() + " name");
		tfTaskName.setText(target.getName());
		tfMaxMark.setText("" + target.getMaxMark());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());

		chckbxAllowMarkerComment.setSelected(target.hasComment());
		chckbxGroupTask.setSelected(target.isGroup());
		chckbxBonusTask.setSelected(target.isBonus());
		validate();
	}

	/**
	 * Stores displayed values back into the model.scheme.
	 */
	public void save() {
		String labelField = tfLabel.getText();
		String label = (labelField == null || labelField.length() == 0? "Task":labelField);
		target.setLabel(label);
		lblName.setText(label + " name");
		target.setName(tfTaskName.getText());
		try {
			target.setMaxMark(Float.parseFloat(tfMaxMark.getText()));
		} catch (NumberFormatException e) {
			target.setMaxMark(0);
		}
		
		target.clearCriteria();
		for (Criterion c: cp.getCriteria()) {
			target.addCriterion(c);
		}
		target.setDescription(taDescription.getText());
		target.setMarkerInstruction(taMarkerInstructions.getText());

		target.setHasComment(chckbxAllowMarkerComment.isSelected());
		target.setGroup(chckbxGroupTask.isSelected());
		target.setBonus(chckbxBonusTask.isSelected());
		target.setPenalty(chckbxPenaltyTask.isSelected());
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


	@Override
	public Mark getTask() {
		return target;
	}

	public JButton getAddSubtaskButton() {
		JButton newButton = new JButton("Add subtask");
		newButton.setActionCommand("Add subtask");
		return newButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Select learning outcomes") {
			OutcomesDialog od = new OutcomesDialog(scheme.getOffering());
			od.pack();
			od.setVisible(true);
		}
		
	}
}
