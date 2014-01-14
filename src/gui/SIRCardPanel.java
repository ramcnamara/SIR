package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.Checkbox;
import model.ComplexTask;
import model.Mark;
import model.MarkingScheme;
import model.QTask;
import model.Task;
import net.miginfocom.swing.MigLayout;
import formatters.JTreeMaker;
import formatters.JTreeMaker.Node;
import gui.cards.Card;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
/**
 * The SIRCardPanel is responsible for displaying the Tasks, QTasks, and Checkboxes
 * that comprise SIR's data model.  It also allows the user to perform task-level
 * modifications: adding and removing tasks and subtasks.
 * 
 * @author Robyn
 *
 */
public class SIRCardPanel extends JPanel implements Observer, TreeSelectionListener, ActionListener {
	private MarkingScheme scheme;
	private ComplexTask parent = null;
	private Mark task = null;
	
	/**
	 * Instantiates a new button panel.  Does not add the tree panel;
	 * this is done by the update() method.
	 */
	public SIRCardPanel() {
		setLayout(new MigLayout());
		buttonPanel = new JPanel();
		JButton btnAddTask = new JButton("Add task");
		btnAddTask.setActionCommand("Add task");
		btnAddTask.addActionListener(this);
		buttonPanel.setLayout(new MigLayout("", "[95px]", "[23px][23px]"));
		buttonPanel.add(btnAddTask, "cell 0 0,growx,aligny center");
		
		JButton btnRemoveTask = new JButton("Remove task");
		btnRemoveTask.setActionCommand("Remove task");
		btnRemoveTask.addActionListener(this);
		buttonPanel.add(btnRemoveTask, "cell 0 1,alignx left,aligny center");	
	}

	private JPanel cardArea;
	private static final long serialVersionUID = 1L;
	private JPanel buttonPanel;

	@Override
	/**
	 * Respond to changes in the data model.
	 * 
	 * @param scheme the MarkingScheme in the data model
	 * @param o parameter object, currently ignored
	 */
	public void update(Observable scheme, Object o) {
		if (!(scheme instanceof MarkingScheme))
			return;
		this.scheme = (MarkingScheme)scheme;
		parent = null;
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme(this.scheme);
		cardArea = treemaker.getCardStack();
		this.removeAll();
		this.add(cardArea, "dock center");
		this.add(buttonPanel, "dock east");
		cardArea.setAlignmentY(Component.LEFT_ALIGNMENT);
		this.repaint();
	}

	@Override
	/**
	 * Respond to TreeSelectionEvents by showing the task selected in the tree.
	 */
	public void valueChanged(TreeSelectionEvent e) {
		JTreeMaker.Node node = (Node) e.getNewLeadSelectionPath().getLastPathComponent();
		parent = node.getParentTask();
		task = node.getMark();
		((CardLayout) cardArea.getLayout()).show(cardArea, node.getId());
		
	}
	
	private Card getCurrentCard() {
		Component panels[] = cardArea.getComponents();
		
		// Card area is empty, so don't bother
		if (panels == null || panels.length == 0)
			return null;
		
		for (Component current : panels) {
			if (current.isVisible()) {
				if (current instanceof Card)
					return (Card) current;
				return null;
			}
		}
		
		// nothing is visible (no Cards, anyway)
		return null;
	}
	
	public void seekToCard(Mark mark){
		// remember where the first card is	
		CardLayout cl = (CardLayout) cardArea.getLayout();
		cl.first(cardArea);
		Card firstCard = getCurrentCard();
		do {
			cl.next(cardArea);
			if (getCurrentCard().getTask() == mark) {
				cardArea.repaint();
				return;
			}
				
		}
		while (getCurrentCard() != firstCard);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
		
		// Add new top-level task.
		if (cmd.equals("Add task")) {
			// Bring up dialog to select task type
			JRadioButton rbtask = new JRadioButton("Numerically-marked task");
			JRadioButton rbqtask = new JRadioButton("Qualitatively-marked task");
			JRadioButton rbcheckbox = new JRadioButton("Checkbox (done or not done, no intermediate grades)");
			
			ButtonGroup group = new ButtonGroup();
			group.add(rbtask);
			group.add(rbqtask);
			group.add(rbcheckbox);
			
			Object[] components = {
					new JLabel("What type of task do you wish to add?"),
					rbtask,
					rbqtask,
					rbcheckbox
			};
			
			int result = JOptionPane.showConfirmDialog(null, components, "Add task", JOptionPane.OK_CANCEL_OPTION);
			
			// User closed or cancelled out of the dialog
			if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION)
				return;
			
			Mark newTask;
			
			if (rbtask.isSelected())
				newTask = new Task();
			else if (rbqtask.isSelected())
				newTask = new QTask();
			else if (rbcheckbox.isSelected())
				newTask = new Checkbox();
			else
				return;
			
			newTask.setName("New task");
			scheme.add(newTask);
			parent = null;
			((CardLayout)cardArea.getLayout()).last(cardArea);
			validate();
		}
		
		// Remove currently-visible task (no matter what type it is)
		else if (cmd.equals("Remove task")) {
			if (parent == null) {
				scheme.delete(task);
			}
			else
				parent.removeSubtask(task);
			scheme.refresh();
			seekToCard(parent);
			validate();
		}		
	}
}
