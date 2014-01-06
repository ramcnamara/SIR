package gui.cards;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
import model.Checkbox;

public class CheckboxPanel extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private JPanel contents;
	private JTextField tfTaskName;
	private JTextField tfDescription;
	private JTextField tfMark;

	/**
	 * Create the panel.
	 */
	public CheckboxPanel(Checkbox checkbox) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][357.00px,grow][172.00px]",
				"[14px][14px][14px][][]"));

		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx trailing");

		tfTaskName = new JTextField();
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);

		JLabel lblMaxMark = new JLabel("Mark");
		contents.add(lblMaxMark, "flowx,cell 2 0");

		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 1,alignx trailing");

		tfDescription = new JTextField();
		contents.add(tfDescription, "cell 1 1 2 1,growx");
		tfDescription.setColumns(10);

		JCheckBox chckbxAllowMarkerComment = new JCheckBox(
				"Allow marker comment");
		contents.add(chckbxAllowMarkerComment, "cell 1 3");
		setViewportView(contents);

		tfMark = new JTextField();
		contents.add(tfMark, "cell 2 0");
		tfMark.setColumns(10);

		JCheckBox chckbxGroupTask = new JCheckBox("Group task");
		contents.add(chckbxGroupTask, "flowx,cell 1 2");
	}

	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}

}
