package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


import javax.swing.JButton;
import javax.swing.JPanel;

import model.ComplexTask;
import model.Mark;
import model.MarkingScheme;
import model.Task;
import net.miginfocom.swing.MigLayout;
import formatters.JTreeMaker;
import formatters.JTreeMaker.Node;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SIRCardPanel extends JPanel implements Observer, TreeSelectionListener, ActionListener {
	private MarkingScheme scheme;
	private ComplexTask parent = null;
	private Mark task = null;
	
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
	public void valueChanged(TreeSelectionEvent e) {
		JTreeMaker.Node node = (Node) e.getNewLeadSelectionPath().getLastPathComponent();
		parent = node.getParentTask();
		task = node.getMark();
		((CardLayout) cardArea.getLayout()).show(cardArea, node.getId());
		
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
		if (cmd.equals("Add task")) {
			Task newTask = new Task();
			newTask.setName("New task");
			scheme.add(newTask);
			((CardLayout)cardArea.getLayout()).last(cardArea);
			validate();
		}
		
		else if (cmd.equals("Remove task")) {
			if (parent == null) {
				scheme.delete(task);
			}
			else
				parent.removeSubtask(task);
			scheme.refresh();
			validate();
		}
		
	}
}
