package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.ComplexTask;
import model.MarkingScheme;
import net.miginfocom.swing.MigLayout;
import formatters.JTreeMaker;
import formatters.JTreeMaker.Node;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SIRCardPanel extends JPanel implements Observer, TreeSelectionListener, ActionListener {
	public SIRCardPanel() {
		setLayout(new MigLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton btnAddTask = new JButton("Add task");
		btnAddTask.setActionCommand("Add task");
		btnAddTask.addActionListener(this);
		buttonPanel.add(btnAddTask);
		
		JButton btnRemoveTask = new JButton("Remove task");
		btnRemoveTask.setActionCommand("Remove task");
		btnRemoveTask.addActionListener(this);
		buttonPanel.add(btnRemoveTask);	
	}

	private JPanel cardArea;
	private ComplexTask parent = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel buttonPanel;

	@Override
	public void update(Observable scheme, Object o) {
		if (!(scheme instanceof MarkingScheme))
			return;
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme((MarkingScheme)scheme);
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
		((CardLayout) cardArea.getLayout()).show(cardArea, node.getId());
		parent = node.getParentTask();
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
