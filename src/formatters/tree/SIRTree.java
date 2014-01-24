package formatters.tree;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;


import model.MarkingScheme;

/**
 * An extension of JTree that implements drag and drop.
 * 
 * @author ram
 *
 */
public class SIRTree extends JTree {

	private static final long serialVersionUID = 1L;
	private MarkingScheme theScheme;
	



	/**
	 * Wrapper for JTree constructor.
	 * 
	 * @param root the TreeNode at the root of the SIRTree
	 */
	public SIRTree(SIRNode root, MarkingScheme scheme) {
		super(root);
		theScheme = scheme;
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new SIRTreeCellRenderer());
		
		setDragEnabled(true);
		setDropMode(DropMode.ON_OR_INSERT);
		setTransferHandler(new SIRTreeTransferHandler(this));
		setShowsRootHandles(true);
	}

	public MarkingScheme getMarkingScheme() {
		return theScheme;
		
	}
}
