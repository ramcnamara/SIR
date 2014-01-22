package formatters.tree;

import java.awt.datatransfer.DataFlavor;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import model.Checkbox;
import model.Criterion;
import model.MarkingScheme;
import model.QTask;
import model.Task;

/**
 * An extension of JTree that implements drag and drop.
 * 
 * @author ram
 *
 */
public class SIRTree extends JTree {

	private static final long serialVersionUID = 1L;
	private MarkingScheme theScheme;
	
	/*
	 * Data flavors -- one per kind of task.
	 */
	
	static final DataFlavor TASK_FLAVOR = new DataFlavor(Task.class, "TaskFlavor");
	static final DataFlavor QTASK_FLAVOR = new DataFlavor(QTask.class, "QTaskFlavor");
	static final DataFlavor CHECKBOX_FLAVOR = new DataFlavor(Checkbox.class, "CheckboxFlavor");
	static final DataFlavor CRITERION_FLAVOR = new DataFlavor(Criterion.class, "CriterionFlavor");

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
		setRootVisible(false);
		setShowsRootHandles(true);
	}

	public MarkingScheme getMarkingScheme() {
		return theScheme;
		
	}
}
