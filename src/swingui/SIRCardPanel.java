package swingui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.scheme.Checkbox;
import model.scheme.ComplexTask;
import model.scheme.Mark;
import model.scheme.MarkingScheme;
import model.scheme.QTask;
import model.scheme.SubtaskTypeException;
import model.scheme.Task;
import net.miginfocom.swing.MigLayout;
import formatters.tree.JTreeMaker;
import formatters.tree.SIRNode;

import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JSeparator;

import swingui.cards.Card;

/**
 * The SIRCardPanel is responsible for displaying the Tasks, QTasks, and Checkboxes
 * that comprise SIR's data model.scheme.  It also allows the user to perform task-level
 * modifications: adding and removing tasks and subtasks.
 * 
 * @author Robyn
 *
 */
public class SIRCardPanel extends JPanel implements TreeSelectionListener, ActionListener, NavDisableEventListener {
	private MarkingScheme scheme;
	private ComplexTask parent = null;
	private Mark task = null;
	private boolean allowNavigation = true;
	
	/**
	 * Instantiates a new button panel.  Does not add the tree panel;
	 * this is done by the update() method.
	 */
	public SIRCardPanel() {
		setLayout(new MigLayout());
		taskControlPanel = new JPanel();
		btnAddTask = new JButton("Add task");
		btnAddTask.setActionCommand("Add task");
		btnAddTask.addActionListener(this);
		taskControlPanel.setLayout(new MigLayout("", "[95px]", "[][][][][23px][23px][]"));
		
		btnSaveTask = new JButton("Save task");
		btnSaveTask.addActionListener(this);
		taskControlPanel.add(btnSaveTask, "cell 0 0, growx, aligny center");
		
		btnResetTask = new JButton("Revert task");
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
	private JButton btnSaveTask;
	private JButton btnResetTask;
	private JButton btnAddTask;

	/**
	 * Respond to changes in the data model.scheme.
	 * 
	 * @param scheme the MarkingScheme in the data model.scheme
	 * @param o parameter object, currently ignored
	 */
	public void update(Observable scheme, Object o) {
		if (!(scheme instanceof MarkingScheme))
			return;
		
		// dispose of current components
		
		this.scheme = (MarkingScheme)scheme;
		parent = null;
		JTreeMaker treemaker = new JTreeMaker(this);
		treemaker.doScheme(this.scheme);
		cardArea = treemaker.getCardStack();
		this.removeAll();
		this.add(cardArea, "dock center");
		this.add(taskControlPanel, "dock east");
		cardArea.setAlignmentY(Component.LEFT_ALIGNMENT);
		if (task != null)
			seekToCard(task);
		enableNavigation();
		this.repaint();
	}

	@Override
	/**
	 * Respond to TreeSelectionEvents by showing the task selected in the tree.
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if (!allowNavigation)
			return;
		SIRNode node = (SIRNode) e.getNewLeadSelectionPath().getLastPathComponent();
		parent = node.getParentTask();
		task = node.getMark();
		((CardLayout) cardArea.getLayout()).show(cardArea, node.getId());
		replaceAddSubtaskButton();
	}

	private void replaceAddSubtaskButton() {
		if (getCurrentCard() == null) return;
		
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
		if (firstCard == null) return;
		do {
			cl.next(cardArea);
			Card card = getCurrentCard();
			if (card != null 
					&& card.getTask() != null 
					&& card.getTask() == mark) {
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
			if (getCurrentCard() == null) return;
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
			// We will need to manually reset the card selection 
			// because saving will trigger the card display to be rebuilt
			if (getCurrentCard() == null) return;
			Mark ourTask = getCurrentCard().getTask();
			getCurrentCard().save();
			seekToCard(ourTask);
			scheme.refresh();
			enableNavigation();
		}
		
		// Reset current task
		else if (cmd.equals("Revert task")) {
			Card currentCard = getCurrentCard();
			if (currentCard == null) return;
			currentCard.reset();
			enableNavigation();
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

			rbcheckbox.setEnabled(false);
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
		
		else if (cmd.equals("Add QTask")) {
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
	

	private void disableNavigation() {
		allowNavigation = false;
		btnAddSubtask.setEnabled(false);
		btnAddTask.setEnabled(false);
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		disableNavigation();
		
	}

	
	private void enableNavigation() {
		allowNavigation = true;
		btnAddSubtask.setEnabled(true);
		btnAddTask.setEnabled(true);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		disableNavigation();		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		disableNavigation();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		disableNavigation();		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		disableNavigation();
		
	}

}
