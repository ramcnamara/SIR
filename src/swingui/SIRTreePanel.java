package swingui;

import java.util.Observable;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.TreeSelectionListener;

import model.scheme.MarkingScheme;
import formatters.tree.JTreeMaker;

/**
 * This class is a ScrollPane that wraps a JTree displaying the marking
 * scheme structure.  It is also responsible for handling the tree's response
 * to events that change the data model.scheme.
 * @author Robyn
 *
 */
public class SIRTreePanel extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private JTree tree;
	private NavDisableEventListener ndel;
	
	public SIRTreePanel(NavDisableEventListener ndel) {
		this.tree = new JTree();
		this.ndel = ndel;
		setLayout(new ScrollPaneLayout());
	}
	

	/**
	 * Handle changes to the domain model.scheme.
	 * 
	 * SIRTreePanel deals with this by creating a new JTree and copying the old tree's
	 * event listeners over to it.
	 * 
	 * @param o the Observable that triggered the update (which should be a MarkingScheme)
	 * @param parameter object, but currently ignored
	 */
	public void update(Observable o, Object parameter) {
		// check we're being invoked on something sane
		if (!(o instanceof MarkingScheme))
			return;
		
		// copy out the old listeners
		TreeSelectionListener[] tsl = null;
		if (tree != null) {
			tsl = tree.getListeners(TreeSelectionListener.class);
		}
		
		JTreeMaker treemaker = new JTreeMaker(ndel);
		treemaker.doScheme((MarkingScheme)o);
		tree = treemaker.getJTree();
		if (tree == null)
			return;
		
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
