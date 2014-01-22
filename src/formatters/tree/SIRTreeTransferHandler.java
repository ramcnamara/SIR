package formatters.tree;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import model.Checkbox;
import model.ComplexTask;
import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.SubtaskTypeException;
import model.Task;


final class SIRTreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;

	private SIRTree tree;
	private SIRNode mover = null;
	private SIRNode parent = null;
	
	public SIRTreeTransferHandler(SIRTree tree) {
		super();
		this.tree = tree;
	}

	/**
	 * SIRTrees support copy and move operations.
	 */
	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	/**
	 * Returns a reference to the SIRNode that is being transferred.  Also
	 * stores references to the moving node and its parents, for tree structure
	 * hygiene and tidying up purposes.
	 */
	@Override
	public Transferable createTransferable(JComponent c) {
		TreePath path = tree.getSelectionPath();
		mover = (SIRNode) path.getLastPathComponent();

		if (mover == null)
			parent = null;
		else parent = (SIRNode) path.getParentPath().getLastPathComponent();
		return mover;
	}

	
	@Override
	public boolean canImport(TransferHandler.TransferSupport supp) {
		// currently, we only support drag and drop
		// TODO: change this when we implement cut and paste
		if (!supp.isDrop()) {
			return false;
		}
		
		JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();
		// loc is guaranteed non-null
		TreePath path = loc.getPath();	
		if (path == null) {
			return false;
		}
		
		// can't drop a node onto one of its children
		for (Object o: path.getPath()) {
			if (o.equals(mover)) {
				return false;
			}
		}
		
		Object node = path.getLastPathComponent();
		if (!(node instanceof SIRNode)) {
			return false;
		}

		
		Mark m = ((SIRNode) node).getMark();
		
		// You can drop anything onto a Task
		if (m instanceof Task) {
			boolean yn = (supp.isDataFlavorSupported(SIRTree.TASK_FLAVOR) || 
					supp.isDataFlavorSupported(SIRTree.CHECKBOX_FLAVOR) ||
					supp.isDataFlavorSupported(SIRTree.QTASK_FLAVOR) ||
					supp.isDataFlavorSupported(SIRTree.CRITERION_FLAVOR));
			return yn;
		}
		// You can't drop anything onto a Checkbox or Criterion
		if (m instanceof Checkbox || m instanceof Criterion) {
			return false;
		}
		
		// You can't drop Tasks or Checkboxes on QTasks
		boolean yn = !(supp.isDataFlavorSupported(SIRTree.TASK_FLAVOR) || 
				supp.isDataFlavorSupported(SIRTree.CHECKBOX_FLAVOR));
		return yn;
	}
	
	public boolean importData(TransferHandler.TransferSupport supp) {
		if (!canImport(supp)) {
			return false;
		}
		
		JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();
		TreePath path = loc.getPath();
		
		Transferable data = supp.getTransferable();
		
		SIRNode node = null;
		try {
			node = (SIRNode) data.getTransferData(supp.getDataFlavors()[0]);
		}
		catch (IOException ex) {
			return false;
		} catch (UnsupportedFlavorException ex) {
			return false;
		}
		
		Mark childTask = node.getMark().clone();
		MarkingScheme scheme = null;
		if (tree != null)
			scheme = tree.getMarkingScheme();
			
		if (parent == null) {
			if (scheme != null) {
				scheme.add(childTask);
				return true;
			}
		}
		
		Mark parentTask = parent.getMark();
		int index = loc.getChildIndex();
		if (index == -1) {
			// dropped on the path, insert at end
			index = parent.getChildCount();
		}
		
		// Are we dealing with a criterion?
		if (childTask instanceof Criterion && parentTask instanceof ComplexTask) {
			((ComplexTask)parentTask).insertCriterion(index, (Criterion)childTask);
			if (scheme != null)
				scheme.refresh();
			return true;
		}
		

		// we have a Checkbox, Task or QTask
		try {
			parentTask.insertAt(index, childTask);
		} catch (SubtaskTypeException ex) {
			return false;
		}
		
		scheme.refresh();
		return true;
	}
	
	@Override
	public void exportDone(JComponent source, Transferable data, int action) {
		
		// Nothing to do if it was a copy or null action
		if (action == COPY) 
			return;
		
		if (action == NONE)
			return;
		
		// It was a move, so delete node from its current position
		if (!(data instanceof SIRNode)) {
			return;
		}
		
		SIRNode node = (SIRNode)data;
		Mark theMark = node.getMark();
		ComplexTask parent = node.getParentTask();
		if (parent == null)
		{
			((SIRTree) source).getMarkingScheme().removeTask(theMark);
		}
		
		else
			if (theMark instanceof Criterion)
				parent.removeCriterion(theMark);
			else parent.removeSubtask(theMark);
		
		// trigger update
		((SIRTree) source).getMarkingScheme().refresh();
	}
}