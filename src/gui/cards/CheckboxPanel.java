package gui.cards;


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

import net.miginfocom.swing.MigLayout;
import model.Checkbox;
import model.Mark;

/**
 * Display for Checkbox type tasks.
 * 
 * @author Robyn
 *
 */
public class CheckboxPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Checkbox target;
	private Mark parent;
	private JPanel contents;
	private JTextArea tfTaskName;
	private JTextArea taDescription;
	private JTextField tfMark;
	private JTextArea taMarkerInstructions;
	private JCheckBox chckbxGroupTask;
	private JCheckBox chckbxBonusTask;
	private JScrollPane scrollpane;

	/**
	 * Create the panel.
	 * @param mark 
	 */
	public CheckboxPanel(Checkbox checkbox, Mark mark) {
		target = checkbox;
		parent = mark;
		
		setLayout(new MigLayout("fill", "[grow]", "[][grow]"));
		scrollpane = new JScrollPane();
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));
		
		JButton btnSave = new JButton("Save");
		btnSave.setActionCommand("Save");
		btnSave.addActionListener(this);
		add(btnSave, "flowx,cell 0 0,alignx right");

		JButton btnReset = new JButton("Reset");
		btnReset.setActionCommand("Reset");
		btnReset.addActionListener(this);
		add(btnReset, "cell 0 0,alignx right");
		

		// Set up checkbox information
		String taskName = "";
		Float maxMark = 0.0f;
		String description = "";
		String markerInst = "";
		
		if (checkbox != null) {
			taskName = taskName + checkbox.getName();
			maxMark += checkbox.getMaxMark();
			description = description + checkbox.getDescription();
			markerInst = markerInst + checkbox.getMarkerInstruction();
		}
		
		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx trailing");

		tfTaskName = new JTextArea(taskName);
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);

		JLabel lblMaxMark = new JLabel("Mark");
		contents.add(lblMaxMark, "cell 0 1,alignx trailing");

		tfMark = new JTextField(maxMark.toString());
		contents.add(tfMark, "cell 1 1");
		tfMark.setColumns(5);
		
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 2,alignx trailing");

		taDescription = new JTextArea(description);
		contents.add(taDescription, "cell 1 2,growx");
		taDescription.setColumns(10);
		
		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 3,alignx trailing");

		taMarkerInstructions = new JTextArea(markerInst);
		contents.add(taMarkerInstructions, "cell 1 3,wmin 10,grow");
		taMarkerInstructions.setColumns(10);

		scrollpane.setViewportView(contents);

		chckbxGroupTask = new JCheckBox("Group task");
		contents.add(chckbxGroupTask, "flowx,cell 1 5");
		
		chckbxBonusTask = new JCheckBox("Bonus task");
		contents.add(chckbxBonusTask, "flowx, cell 1 6");
	}
	
	/**
	 * Re-reads the displayed data from the model, which has the
	 * effect of cancelling any changes that were to have been made.
	 */
	public void reset() {
		tfTaskName.setText(target.getName());
		tfMark.setText("" + target.getMaxMark());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());
		
		chckbxGroupTask.setSelected(target.isGroup());
		chckbxBonusTask.setSelected(target.getBonus());
	}
	
	
	/**
	 * Stores displayed values back into the model.
	 */
	public void save() {
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
	}
	
	/**
	 * Retrieve the model object representing the parent of the 
	 * Checkbox providing the data.  This is used for deletion.
	 * @return an instance of Mark representing the parent
	 */
	public Mark getParentTask() {
		return parent;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();

		if (cmd.equals("Reset"))
			reset();

		else if (cmd.equals("Save"))
			save();
		
	}
}
