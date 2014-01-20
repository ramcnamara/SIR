package formatters.tree;

import javax.swing.JTree;

import formatters.tree.JTreeMaker.Node;

/**
 * An extension of JTree that implements drag and drop.
 * 
 * @author ram
 *
 */
public class SIRTree extends JTree {


	/**
	 * Wrapper for JTree constructor.
	 * 
	 * @param root the TreeNode at the root of the SIRTree
	 */
	public SIRTree(Node root) {
		super(root);
	}

	private static final long serialVersionUID = 1L;

}
