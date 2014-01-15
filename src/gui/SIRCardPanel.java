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
import model.SubtaskTypeException;
import model.Task;
import net.miginfocom.swing.MigLayout;
import formatters.JTreeMaker;
import formatters.JTreeMaker.Node;
import gui.cards.Card;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JSeparator;
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
		taskControlPanel = new JPanel();
		JButton btnAddTask = new JButton("Add task");
		btnAddTask.setActionCommand("Add task");
		btnAddTask.addActionListener(this);
		taskControlPanel.setLayout(new MigLayout("", "[95px]", "[][][][][23px][23px][]"));
		
		JButton btnSaveTask = new JButton("Save task");
		btnSaveTask.addActionListener(this);
		taskControlPanel.add(btnSaveTask, "cell 0 0, growx, aligny center");
		
		JButton btnResetTask = new JButton("Reset task");
		btnResetTask.addActionListener(this);
		taskControlPanel.add(btnResetTask, "cell 0 1, growx, aligny center");
		
		JButton btnRemoveTask = new JButton("Remove task");
		btnRemoveTask.setActionCommand("Remove task");
		btnRemoveTask.addActionListener(this);
		taskControlPanel.add(btnRemoveTask, "cell 0 2,alignx left,aligny center");	
		
		JSeparator separator = new JSeparator();
		taskControlPanel.add(separator, "cell 0 3, growx");
		taskControlPanel.add(btnAddTask, "cell 0 4,growx,aligny center");
		
		btnAddSubtask = new JButton("Add subtask");
		btnAddSubtask.setActionCommand("Add subtask");
		btnAddSubtask.addActionListener(this);
		btnAddSubtask.setEnabled(task instanceof ComplexTask);
		taskControlPanel.add(btnAddSubtask, "cell 0 5,growx,aligny center");
	}

	private JPanel cardArea;
	private static final long serialVersionUID = 1L;
	private JPanel taskControlPanel;
	private JButton btnAddSubtask;

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
		this.add(taskControlPanel, "dock east");
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
		replaceAddSubtaskButton();
	}

	private void replaceAddSubtaskButton() {
		taskControlPanel.remove(btnAddSubtask);
		btnAddSubtask = getCurrentCard().getAddSubtaskButton();
		btnAddSubtask.addActionListener(this);
		taskControlPanel.add(btnAddSubtask, "cell 0 5,growx,aligny center");
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
	
	// Seek to the requested card.  If it is not present, seeks to first card.
	public void seekToCard(Mark mark){
		// remember where the first card is	
		CardLayout cl = (CardLayout) cardArea.getLayout();
		cl.first(cardArea);
		Card firstCard = getCurrentCard();
		do {
			cl.next(cardArea);
			if (getCurrentCard().getTask() == mark) {
				task = mark;
				replaceAddSubtaskButton();
				repaint();
				return;
			}
				
		}
		while (getCurrentCard() != firstCard);
		task = firstCard.getTask();
		replaceAddSubtaskButton();
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
			task = newTask;
			replaceAddSubtaskButton();
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
			replaceAddSubtaskButton();
			validate();
		}	
		
		// Save current task
		else if (cmd.equals("Save task")) {
			getCurrentCard().save();
		}
		
		// Reset current task
		else if (cmd.equals("Reset task")) {
			getCurrentCard().reset();
		}
		
		else if (cmd.equals("Add subtask")) {
			// check that we can do this
			Mark ourTask = getCurrentCard().getTask();
			if (!(ourTask instanceof ComplexTask))
				return;
			
			// Bring up dialog to select task type
			JRadioButton rbtask = new JRadioButton("Numerically-marked task");
			JRadioButton rbqtask = new JRadioButton("Qualitatively-marked task");
			JRadioButton rbcheckbox = new JRadioButton(
					"Checkbox (done or not done, no intermediate grades)");

			ButtonGroup group = new ButtonGroup();
			group.add(rbtask);
			group.add(rbqtask);
			group.add(rbcheckbox);

			Object[] components = {
					new JLabel("What type of task do you wish to add?"),
					rbtask, rbqtask, rbcheckbox };

			int result = JOptionPane.showConfirmDialog(null, components,
					"Add task", JOptionPane.OK_CANCEL_OPTION);

			// User closed or cancelled out of the dialog
			if (result == JOptionPane.CANCEL_OPTION
					|| result == JOptionPane.CLOSED_OPTION)
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

			try {
				// we have already checked that this cast is kosher
				((ComplexTask) ourTask).addSubtask(newTask);
			} catch (SubtaskTypeException e) {
				System.out.println("Trying to add a Task as a subtask of a QTask");
			}
			scheme.refresh();
			seekToCard(newTask);
		}
		
		else if (cmd.equals("Add subtask")) {
			if (!(task instanceof ComplexTask))
				return;
			QTask newqtask = new QTask();
			newqtask.setName("New task");
			try {
				((ComplexTask) task).addSubtask(newqtask);
			} catch (SubtaskTypeException e) {
				System.out.println("Somehow adding a QTask is causing a SubtaskTypeException.");
			}
			
			scheme.refresh();
			seekToCard(newqtask);
		}

	}
}
