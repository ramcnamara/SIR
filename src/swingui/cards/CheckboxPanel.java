package swingui.cards;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import swingui.NavDisableEventListener;
import net.miginfocom.swing.MigLayout;
import model.scheme.Checkbox;
import model.scheme.Mark;
import model.scheme.MarkingScheme;

/**
 * Display for Checkbox type tasks.
 * 
 * @author Robyn
 *
 */
public class CheckboxPanel extends Card implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Checkbox target;

	private JPanel contents;
	private JTextArea tfTaskName;
	private JTextArea taDescription;
	private JTextField tfMark;
	private JTextArea taMarkerInstructions;
	private JCheckBox chckbxGroupTask;
	private JCheckBox chckbxBonusTask;
	private JCheckBox chckbxPenaltyTask;
	private JScrollPane scrollpane;
	private JLabel lblLabel;
	private JTextField tfLabel;
	private JLabel lblName;

	/**
	 * Create the panel.
	 * @param mark 
	 */
	public CheckboxPanel(MarkingScheme scheme, NavDisableEventListener listener, Mark parent, Checkbox checkbox) {
		super(scheme, listener, parent);
		
		target = checkbox;
		
		setLayout(new MigLayout("fill", "[grow]", "[][grow]"));
		scrollpane = new JScrollPane();
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		

		// Set up checkbox information
		String label = "";
		String taskName = "";
		Float maxMark = 0.0f;
		String description = "";
		String markerInst = "";
		boolean group = false;
		boolean bonus = false;
		boolean penalty = false;
		
		if (checkbox != null) {
			label = label + (checkbox.getLabel() == null? "": checkbox.getLabel());
			taskName = taskName + (checkbox.getName() == null? "" : checkbox.getName());
			maxMark += checkbox.getMaxMark();
			description = description + (checkbox.getDescription() == null? "": checkbox.getDescription());
			markerInst = markerInst + (checkbox.getMarkerInstruction() == null? "" : checkbox.getMarkerInstruction());
			group = checkbox.isGroup();
			bonus = checkbox.isBonus();
			penalty = checkbox.isPenalty();
		}
		
		if (label.length() == 0)
			label = "Task";
		
		lblLabel = new JLabel("Label");
		contents.add(lblLabel, "cell 0 0,alignx trailing,aligny baseline");
		
		tfLabel = new JTextField(label);
		contents.add(tfLabel, "cell 1 0,alignx left");
		tfLabel.setColumns(10);
		
		lblName = new JLabel(label + " name");
		contents.add(lblName, "cell 0 1,alignx trailing");

		tfTaskName = new JTextArea(taskName);
		contents.add(tfTaskName, "cell 1 1,growx");
		tfTaskName.setColumns(10);

		JLabel lblMaxMark = new JLabel("Mark");
		contents.add(lblMaxMark, "cell 0 2,alignx trailing");

		tfMark = new JTextField(maxMark.toString());
		contents.add(tfMark, "cell 1 2");
		tfMark.setColumns(5);
		
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 3,alignx trailing");

		taDescription = new JTextArea(description);
		contents.add(taDescription, "cell 1 3,grow");
		taDescription.setColumns(10);
		
		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 4,alignx trailing");

		taMarkerInstructions = new JTextArea(markerInst);
		contents.add(taMarkerInstructions, "cell 1 4,grow");
		taMarkerInstructions.setColumns(10);


		chckbxGroupTask = new JCheckBox("Group task", group);
		contents.add(chckbxGroupTask, "flowx,cell 1 6");
		
		chckbxBonusTask = new JCheckBox("Bonus task", bonus);
		contents.add(chckbxBonusTask, "flowx,cell 1 7");
		
		chckbxPenaltyTask = new JCheckBox("Penalty task", penalty);
		contents.add(chckbxPenaltyTask, "flowx,cell 1 8");
		
		scrollpane.setViewportView(contents);
		add(scrollpane, "cell 0 1, growx, growy");
		
		// Add a listener to each component to allow navigation to be disabled when edits have been made
		chckbxBonusTask.addChangeListener(listener);
		chckbxGroupTask.addChangeListener(listener);
		chckbxPenaltyTask.addChangeListener(listener);
		taDescription.getDocument().addDocumentListener(listener);
		taMarkerInstructions.getDocument().addDocumentListener(listener);
		tfLabel.getDocument().addDocumentListener(listener);
		tfMark.getDocument().addDocumentListener(listener);
		tfTaskName.getDocument().addDocumentListener(listener);
	}
	
	/**
	 * Re-reads the displayed data from the model.scheme, which has the
	 * effect of cancelling any changes that were to have been made.
	 */
	public void reset() {
		tfLabel.setText(target.getLabel() == null || target.getLabel().length() == 0? "Task":target.getLabel());
		lblName.setText(tfLabel.getText() + " name");
		tfTaskName.setText(target.getName());
		tfMark.setText("" + target.getMaxMark());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());
		
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
			target.setMaxMark(Float.parseFloat(tfMark.getText()));
		} catch (NumberFormatException e) {
			target.setMaxMark(0);
		}
		target.setDescription(taDescription.getText());
		target.setMarkerInstruction(taMarkerInstructions.getText());
		
		target.setGroup(chckbxGroupTask.isSelected());
		target.setBonus(chckbxBonusTask.isSelected());
		target.setPenalty(chckbxPenaltyTask.isSelected());
	}
	
	/**
	 * setVisible is overloaded to reset the vertical scroll position to the top.
	 * 
	 * @param visible true to turn visibility on, false to turn it off 
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			scrollpane.getVerticalScrollBar().setValue(0);
			repaint();
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();

		if (cmd.equals("Reset"))
			reset();

		else if (cmd.equals("Save"))
			save();
		
	}

	@Override
	public Mark getTask() {
		return target;
	}

	public JButton getAddSubtaskButton() {
		JButton newButton = new JButton("Add subtask");
		newButton.setEnabled(false);
		return newButton;
	}
}
