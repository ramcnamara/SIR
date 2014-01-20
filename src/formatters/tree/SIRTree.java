package formatters.tree;

import javax.swing.JTree;

/**
 * An extension of JTree that implements drag and drop.
 * 
 * @author ram
 *
 */
public class SIRTree extends JTree {

	private static final long serialVersionUID = 1L;

	/**
	 * Wrapper for JTree constructor.
	 * 
	 * @param root the TreeNode at the root of the SIRTree
	 */
	public SIRTree(SIRNode root) {
		super(root);
	}
}
