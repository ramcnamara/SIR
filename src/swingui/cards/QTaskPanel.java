package swingui.cards;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.scheme.Criterion;
import model.scheme.Mark;
import model.scheme.MarkingScheme;
import model.scheme.QTask;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;

import java.awt.Component;

import javax.swing.border.MatteBorder;

import java.awt.Color;

import javax.swing.Box;

import swingui.NavDisableEventListener;


/**
 * Panel allowing the perusal and editing of qualitative tasks  (QTasks).
 * 
 * @author Robyn
 *
 */
public class QTaskPanel extends Card implements CriterionContainer {

	private static final long serialVersionUID = 5332242915675785276L;
	private JPanel contents;
	private CriterionPanel cp;
	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextArea taMarkerInstructions;
	private JCheckBox chckbxGroupTask;
	private JCheckBox chckbxAllowMarkerComment;
	private JScrollPane scrollpane;
	private JLabel lblLabel;
	private JTextField tfLabel;
	private Component horizontalGlue;
	private ScaleBox scalebox;
	private JLabel lblName;
	private QTask target;

	/**
	 * Create the panel.
	 * @param qtask the qualitative task to display and save to if required
	 * @param mark qtask's parent, used for deletion
	 * @param scheme the marking scheme, used for change notification
	 */
	public QTaskPanel(MarkingScheme scheme, NavDisableEventListener listener, Mark parent, QTask qtask) {
		super(scheme, listener, parent);
		
		setBorder(null);
		target=qtask;
		setLayout(new MigLayout("fill", "[]","[]"));
		
		String label = "";
		String taskName = "";
		String description = "";
		String markerInstruction = "";
		
		if (qtask != null) {
			label += (qtask.getLabel() == null? "Task" : qtask.getLabel());
			taskName += (qtask.getName() == null? "": qtask.getName());
			description += (qtask.getDescription() == null? "": qtask.getDescription());
			markerInstruction += (qtask.getMarkerInstruction() == null? "": qtask.getMarkerInstruction());
		}
		
		if (label.length() == 0)
			label = "Task";


		scrollpane = new JScrollPane();
		scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollpane, "cell 0 0,alignx left,aligny top,grow");
		contents = new JPanel();
		contents.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		contents.setLayout(new MigLayout("", "[][grow,fill]", "[][][][][][][][][grow,fill]"));
		
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
		

		scalebox = new ScaleBox(qtask);
		scalebox.addItem("None");
		contents.add(scalebox, "cell 1 2");

		JLabel lblDescription = new JLabel("Description");
		contents.add(lblDescription, "cell 0 4,alignx right");
		
		taDescription = new JTextArea(description);
		taDescription.setLineWrap(true);
		contents.add(taDescription, "cell 1 4,wmin 10,grow");
		taDescription.setColumns(10);
		
		JLabel lblInstructionsToMarkers = new JLabel("Instructions to markers");
		contents.add(lblInstructionsToMarkers, "cell 0 5,alignx trailing");
		
		taMarkerInstructions = new JTextArea(markerInstruction);
		contents.add(taMarkerInstructions, "cell 1 5,wmin 10,grow");
		taMarkerInstructions.setColumns(10);
		
		chckbxAllowMarkerComment = new JCheckBox("Allow marker comment", qtask.hasComment());
		contents.add(chckbxAllowMarkerComment, "cell 1 7");
		cp = new CriterionPanel(listener);
		cp.setAlignmentY(LEFT_ALIGNMENT);
		cp.setBorder(new TitledBorder(null, "Criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contents.add(cp, "cell 0 8 2 2,aligny top,grow");
		scrollpane.setViewportView(contents);
		
		chckbxGroupTask = new JCheckBox("Group task", qtask.isGroup());
		contents.add(chckbxGroupTask, "flowx,cell 1 6");
		
		horizontalGlue = Box.createHorizontalGlue();
		contents.add(horizontalGlue, "cell 1 0");
		
		if (qtask.getScale() == null) {
			scalebox.setSelectedIndex(scalebox.getItemCount() - 1);
		}
		
		// Add a listener to each component to allow navigation to be disabled when edits have been made
		chckbxAllowMarkerComment.addChangeListener(listener);
		chckbxGroupTask.addChangeListener(listener);
		taDescription.getDocument().addDocumentListener(listener);
		taMarkerInstructions.getDocument().addDocumentListener(listener);
		tfLabel.getDocument().addDocumentListener(listener);
		tfTaskName.getDocument().addDocumentListener(listener);
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
	 * Re-reads the displayed data from the model.scheme, which has the
	 * effect of cancelling any changes that were to have been made.
	 */
	public void reset() {
		tfLabel.setText(target.getLabel() == null || target.getLabel().length() == 0? "Task":target.getLabel());
		lblName.setText(tfLabel.getText() + " name");
		tfTaskName.setText(target.getName());
		taDescription.setText(target.getDescription());
		taMarkerInstructions.setText(target.getMarkerInstruction());
		
		chckbxAllowMarkerComment.setSelected(target.hasComment());
		chckbxGroupTask.setSelected(target.isGroup());
		
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
		target.setDescription(taDescription.getText());
		target.setMarkerInstruction(taMarkerInstructions.getText());
		target.setHasComment(chckbxAllowMarkerComment.isSelected());
		target.setGroup(chckbxGroupTask.isSelected());
		target.setScale(scalebox.getSelectedScale());
		
		target.clearCriteria();
		for (Criterion c: cp.getCriteria()) {
			target.addCriterion(c);
		}
		
		// tell the marking scheme it's changed
		scheme.refresh();
	}
	
	/**
	 * The Add Subtask button has different behaviour according to the type of task
	 * currently displayed, so each Card implements it differently.
	 * 
	 */
	public JButton getAddSubtaskButton() {
		JButton newButton = new JButton("Add subtask");
		newButton.setActionCommand("Add QTask");
		return newButton;
	}

	/**
	 * Accessor for target -- returns the QTask associated with this Card.
	 */
	@Override
	public Mark getTask() {
		return target;
	}
	
	/**
	 * Handle the "Select learning outcomes" event.
	 * 
	 * Instantiates and displays an OptionDialog.
	 */
//	public void actionPerformed(ActionEvent e) {
//		if (e.getActionCommand() == "Select learning outcomes") {
//			OutcomesDialog od = new OutcomesDialog(scheme.getOffering(), target);
//			int result = JOptionPane.showConfirmDialog(null, od, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//			if (result == JOptionPane.CANCEL_OPTION)
//				return;
//			
//			if (result == JOptionPane.OK_OPTION) {
//				target.setOutcomes(od.getSelectedOutcomes());
//			}
//		}
//	}

}
