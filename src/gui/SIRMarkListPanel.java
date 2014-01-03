package gui;

import java.awt.CardLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.MarkingScheme;
import formatters.JTreeMaker;
import formatters.JTreeMaker.Node;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SIRMarkListPanel extends JPanel implements Observer, TreeSelectionListener {

	private JPanel panel;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void update(Observable scheme, Object o) {
		if (!(scheme instanceof MarkingScheme))
			return;
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme((MarkingScheme)scheme);
		panel = treemaker.getCardStack();
		this.removeAll();
		this.add(panel);
		this.repaint();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTreeMaker.Node node = (Node) e.getNewLeadSelectionPath().getLastPathComponent();
		((CardLayout) panel.getLayout()).show(panel, node.getId());
	}

}
