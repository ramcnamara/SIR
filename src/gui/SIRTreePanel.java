package gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.TreeSelectionListener;

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
		setPreferredSize(new Dimension(200, 800));
	}
	@Override
	public void update(Observable o, Object listener) {
		if (!(o instanceof MarkingScheme))
			return;
		if (listener == null || !(listener instanceof TreeSelectionListener))
			return;
		
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme((MarkingScheme)o);
		tree = treemaker.getJTree();
		tree.addTreeSelectionListener((TreeSelectionListener)listener);
		this.getViewport().removeAll();
		this.getViewport().add(tree);
		tree.repaint();
		repaint();
	}

}
