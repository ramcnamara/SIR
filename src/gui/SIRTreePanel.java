package gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneLayout;

import model.MarkingScheme;

import formatters.JTreeMaker;


public class SIRTreePanel extends JScrollPane implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTree tree;
	
	SIRTreePanel() {

		setLayout(new ScrollPaneLayout());
		add(new JLabel("No scheme loaded"));
		setPreferredSize(new Dimension(300, 500));
	}
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof MarkingScheme))
			return;
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme((MarkingScheme)o);
		tree = treemaker.getJTree();
		this.getViewport().removeAll();
		this.getViewport().add(tree);
		tree.repaint();
		repaint();
	}

}
