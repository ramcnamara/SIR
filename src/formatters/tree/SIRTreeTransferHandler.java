package formatters.tree;

import java.awt.Component;
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
import model.SubtaskTypeException;
import model.Task;


final class SIRTreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;


	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	@Override
	public Transferable createTransferable(JComponent c) {
		if (!(c instanceof SIRTree))
			return null;
		
		SIRTree tree = (SIRTree) c;
		SIRNode selectedNode = (SIRNode) tree.getSelectionPath().getLastPathComponent();
		return selectedNode;
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport supp) {
		// currently, we only support drag and drop
		// TODO: change this when we implement cut and paste
		if (!supp.isDrop()) {
			System.out.println("Not drop");
			return false;
		}
		
		JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();
		// loc is guaranteed non-null
		TreePath path = loc.getPath();
		if (path == null) {
			System.out.println("Null path");
			return false;
		}
		
		Object node = path.getLastPathComponent();
		if (!(node instanceof SIRNode)) {
			System.out.println("Invalid node");
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
			System.out.println("Can't drop here.");
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
			System.out.println("IO Exception");
			return false;
		} catch (UnsupportedFlavorException ex) {
			System.out.println("Unsupported flavor");
			return false;
		}
		
		Mark childTask = node.getMark().clone();
		
		SIRNode parentNode = (SIRNode)path.getLastPathComponent();
		if (parentNode == null)
			System.out.println("Null parent!  Insert at root?");
		int index = loc.getChildIndex();
		if (index == -1)
			index = parentNode.getChildCount();
		
		Mark parentTask = parentNode.getMark();
		try {
			parentTask.insertAt(index, childTask);
		} catch (SubtaskTypeException ex) {
			System.out.println("Invalid subtask type");
			return false;
		}
		
		Component c = supp.getComponent();
		if (c instanceof SIRTree)
			((SIRTree)c).getMarkingScheme().refresh();
		else
			System.out.println("Not a tree. (?!)");
		return true;
	}
	
	@Override
	public void exportDone(JComponent source, Transferable data, int action) {
		
		// Nothing to do if it was a copy
		if (action != MOVE) 
			return;
		
		// It was a move, so delete node from its current position
		if (!(data instanceof SIRNode)) {
			System.out.println("Not a node");
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