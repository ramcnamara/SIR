package gui.cards;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.QTask;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;

import java.awt.Component;


/**
 * Panel allowing the perusal and editing of qualitative tasks  (QTasks).
 * 
 * @author Robyn
 *
 */
public class QTaskPanel extends JPanel implements CriterionContainer, Card {

	private static final long serialVersionUID = 1L;
	private QTask target;
	private MarkingScheme scheme;
	private Mark parent;
	private JPanel contents;
	private CriterionPanel cp;
	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextArea taMarkerInstructions;
	private JCheckBox chckbxGroupTask;
	private JCheckBox chckbxAllowMarkerComment;
	private JScrollPane scrollpane;

	/**
	 * Create the panel.
	 * @param qtask the qualitative task to display and save to if required
	 * @param mark qtask's parent, used for deletion
	 * @param scheme the marking scheme, used for change notification
	 */
	public QTaskPanel(QTask qtask, Mark mark, MarkingScheme scheme) {
		target=qtask;
		parent = mark;
		this.scheme = scheme;
		setLayout(new MigLayout("", "[grow]", "[][grow]"));
		

		scrollpane = new JScrollPane();
		scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollpane, "cell 0 0,alignx left,aligny top");
		contents = new JPanel();
		contents.setLayout(new MigLayout("", "[][grow,fill]", "[][][][pref!,grow,top][][][][pref!,grow,fill]"));
		
		JLabel lblName = new JLabel("Task name");
		contents.add(lblName, "cell 0 0,alignx right");
		
		tfTaskName = new JTextField(qtask.getName());
		contents.add(tfTaskName, "cell 1 0,growx");
		tfTaskName.setColumns(10);
		

		ScaleBox scalebox = new ScaleBox(qtask);
		scalebox.addItem("None");
		contents.add(scalebox, "cell 1 1");

		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 3,alignx right");
		
		taDescription = new JTextArea(qtask.getDescription());
		taDescription.setLineWrap(true);
		contents.add(taDescription, "cell 1 3,wmin 10,grow");
		taDescription.setColumns(10);
		
		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 4,alignx trailing");
		
		taMarkerInstructions = new JTextArea(qtask.getMarkerInstruction());
		contents.add(taMarkerInstructions, "cell 1 4,wmin 10,grow");
		taMarkerInstructions.setColumns(10);
		
		chckbxAllowMarkerComment = new JCheckBox("Allow marker comment", qtask.hasComment());
		contents.add(chckbxAllowMarkerComment, "cell 1 6");
		cp = new CriterionPanel();
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 7 2 2,aligny top,grow");
		scrollpane.setViewportView(contents);
		
		chckbxGroupTask = new JCheckBox("Group task", qtask.isGroup());
		contents.add(chckbxGroupTask, "flowx,cell 1 5");
		if (qtask.getScale() == null) {
			scalebox.setSelectedIndex(scalebox.getItemCount() - 1);
		}
	}
	
	/**
	 * Add a new criterion to this qtask.
	 * 
	 * @param c the criterion to add
	 */
	public void addCriterion(Criterion c) {
		cp.addCriterion(c);
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
	
	/**
	 * Re-reads the displayed data from the model, which has the
	 * effect of cancelling any changes that were to have been made.
	 */
	public void reset() {
		tfTaskName.setText(target.getName());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());
		
		chckbxAllowMarkerComment.setSelected(target.hasComment());
		chckbxGroupTask.setSelected(target.isGroup());
	}
	
	/**
	 * Stores displayed values back into the model.
	 */
	public void save() {
		target.setName(tfTaskName.getText());
		target.setDescription(taDescription.getText());
		target.setMarkerInstruction(taMarkerInstructions.getText());
		
		target.setHasComment(chckbxAllowMarkerComment.isSelected());
		target.setGroup(chckbxGroupTask.isSelected());
		
		// tell the marking scheme it's changed
		scheme.refresh();
	}
	
	/**
	 * Retrieve the model object representing the parent of the 
	 * QTask providing the data.  This is used for deletion.
	 * @return an instance of Mark representing the parent
	 */
	public Mark getParentTask() {
		return parent;
	}
	
	public JButton getAddSubtaskButton() {
		JButton newButton = new JButton("Add subtask");
		newButton.setActionCommand("Add QTask");
		return newButton;
	}

	@Override
	public Mark getTask() {
		return target;
	}

}
