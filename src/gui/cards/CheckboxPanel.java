package gui.cards;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
import model.Checkbox;

public class CheckboxPanel extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private JPanel contents;
	private JTextArea tfTaskName;
	private JTextArea taDescription;
	private JTextField tfMark;
	private JTextArea taMarkerInstructions;

	/**
	 * Create the panel.
	 */
	public CheckboxPanel(Checkbox checkbox) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));

		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx trailing");

		tfTaskName = new JTextArea();
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);

		JLabel lblMaxMark = new JLabel("Mark");
		contents.add(lblMaxMark, "cell 0 1,alignx trailing");

		tfMark = new JTextField();
		contents.add(tfMark, "cell 1 1");
		tfMark.setColumns(5);
		
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 2,alignx trailing");

		taDescription = new JTextArea();
		contents.add(taDescription, "cell 1 2,growx");
		taDescription.setColumns(10);
		
		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 3,alignx trailing");

		taMarkerInstructions = new JTextArea(checkbox.getMarkerInstruction());
		contents.add(taMarkerInstructions, "cell 1 3,wmin 10,grow");
		taMarkerInstructions.setColumns(10);

		JCheckBox chckbxAllowMarkerComment = new JCheckBox("Allow marker comment");
		contents.add(chckbxAllowMarkerComment, "cell 1 4");
		setViewportView(contents);


		JCheckBox chckbxGroupTask = new JCheckBox("Group task");
		contents.add(chckbxGroupTask, "flowx,cell 1 5");
		
		JCheckBox chkbxBonusTask = new JCheckBox("Bonus task");
		contents.add(chkbxBonusTask, "flowx, cell 1 6");
	}

	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}

}
