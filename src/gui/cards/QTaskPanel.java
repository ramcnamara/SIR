package gui.cards;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Criterion;
import model.QTask;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;

public class QTaskPanel extends JScrollPane implements CriterionContainer {

	private static final long serialVersionUID = 1L;
	private JPanel contents;
	private CriterionPanel cp;
	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextArea taMarkerInstructions;

	/**
	 * Create the panel.
	 */
	public QTaskPanel(QTask qtask) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow,fill][]", "[][][pref!,grow,top][][][][pref!,fill]"));
		
		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx right");
		
		tfTaskName = new JTextField(qtask.getName());
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);

		
		JLabel lblComputed = new JLabel("(from subtasks)");
		contents.add(lblComputed, "cell 1 1,alignx left,aligny baseline, growx");
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 2,alignx right");
		
		taDescription = new JTextArea(qtask.getDescription());
		taDescription.setLineWrap(true);
		contents.add(taDescription, "cell 1 2,wmin 10,grow");
		taDescription.setColumns(10);
		
		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 3,alignx trailing");
		
		taMarkerInstructions = new JTextArea(qtask.getMarkerInstruction());
		contents.add(taMarkerInstructions, "cell 1 3,wmin 10,grow");
		taMarkerInstructions.setColumns(10);
		
		JCheckBox chckbxAllowMarkerComment = new JCheckBox("Allow marker comment", qtask.hasComment());
		contents.add(chckbxAllowMarkerComment, "cell 1 5");
		cp = new CriterionPanel();
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 6 2 2,aligny top,grow");
		setViewportView(contents);
		
		JCheckBox chckbxGroupTask = new JCheckBox("Group task", qtask.isGroup());
		contents.add(chckbxGroupTask, "flowx,cell 1 4");
		

		ScaleBox scalebox = new ScaleBox(qtask);
		scalebox.addItem("None");
		contents.add(scalebox, "cell 2 0");
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}

	
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			getVerticalScrollBar().setValue(0);
			repaint();
		}
	}

}
