package edu.monash.madam.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import edu.monash.madam.formatters.JTreeMaker;
import edu.monash.madam.model.MarkingScheme;


public class SIRTreePanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTree tree;
	
	SIRTreePanel() {

		setLayout(new BorderLayout());
		add(new JLabel("No scheme loaded"));
		setPreferredSize(new Dimension(100, 200));
	}
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof MarkingScheme))
			return;
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme((MarkingScheme)o);
		tree = treemaker.getJTree();
		removeAll();
		add(tree);
		tree.repaint();
		repaint();
	}

}
