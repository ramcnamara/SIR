package gui.cards;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	private JTextField tfDescription;
	private JTextField tfMaxMark;

	/**
	 * Create the panel.
	 */
	public QTaskPanel(QTask qtask) {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setAlignmentY(LEFT_ALIGNMENT);
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][357.00px,grow][172.00px]", "[14px][14px][14px][][][55.00px,grow,top]"));
		
		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx trailing");
		
		tfTaskName = new JTextField();
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);
		
		JLabel lblMaxMark = new JLabel("Scale");
		contents.add(lblMaxMark, "flowx,cell 2 0");
		
		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 1,alignx trailing");
		
		tfDescription = new JTextField();
		contents.add(tfDescription, "cell 1 1 2 1,growx");
		tfDescription.setColumns(10);
		
		JCheckBox chckbxAllowMarkerComment = new JCheckBox("Allow marker comment");
		contents.add(chckbxAllowMarkerComment, "cell 1 4");
		cp = new CriterionPanel();
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 5 3 1,aligny top,grow");
		setViewportView(contents);
		
		ScaleBox scalebox = new ScaleBox(qtask);
		scalebox.addItem("None");
		contents.add(tfMaxMark, "cell 2 0");
		tfMaxMark.setColumns(10);
		
		JLabel lblComputed = new JLabel("(computed from subtasks)");
		lblComputed.setVisible(false);
		contents.add(lblComputed, "cell 2 0,alignx left");
		
		JCheckBox chckbxGroupTask = new JCheckBox("Group task");
		contents.add(chckbxGroupTask, "flowx,cell 1 2");
		
		JCheckBox chckbxBonusTask = new JCheckBox("Bonus task");
		contents.add(chckbxBonusTask, "cell 1 3");
	}
	
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}

}
