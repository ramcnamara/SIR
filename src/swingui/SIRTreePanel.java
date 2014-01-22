package swingui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.TreeSelectionListener;

import model.MarkingScheme;
import formatters.tree.JTreeMaker;

/**
 * This class is a ScrollPane that wraps a JTree displaying the marking
 * scheme structure.  It is also responsible for handling the tree's response
 * to events that change the data model.
 * @author Robyn
 *
 */
public class SIRTreePanel extends JScrollPane implements Observer {

	private static final long serialVersionUID = 1L;
	JTree tree;
	
	SIRTreePanel() {
		tree = new JTree();
		setLayout(new ScrollPaneLayout());
	}
	

	/**
	 * Handle changes to the domain model.
	 * 
	 * SIRTreePanel deals with this by creating a new JTree and copying the old tree's
	 * event listeners over to it.
	 * 
	 * @param o the Observable that triggered the update (which should be a MarkingScheme)
	 * @param parameter required by the override but currently ignored
	 */
	@Override
	public void update(Observable o, Object parameter) {
		// check we're being invoked on something sane
		if (!(o instanceof MarkingScheme))
			return;
		
		// copy out the old listeners
		TreeSelectionListener[] tsl = tree.getTreeSelectionListeners();
		
		// instantiate shiny new contents
		JTreeMaker treemaker = new JTreeMaker();
		treemaker.doScheme((MarkingScheme)o);
		tree = treemaker.getJTree();
		this.getViewport().removeAll();
		this.getViewport().add(tree);
		
		// copy the listeners to the new tree
		for (TreeSelectionListener t: tsl)
			tree.addTreeSelectionListener(t);
		
		// expand the tree
		int j=tree.getRowCount();
		int i=0;
		while( i < j) {
			tree.expandRow(i);
			i++;
			j = tree.getRowCount();
		}

		// we have changed a component and need to redraw
		repaint();
	}


	/**
	 * Wrapper for JTree.addTreeSelectionListener().
	 * @param listener
	 */
	public void addTreeSelectionListener(TreeSelectionListener listener) {
		tree.addTreeSelectionListener(listener);		
	}

}
