package formatters.tree;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Checkbox;
import model.ComplexTask;
import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.QTask;
import model.SubtaskTypeException;
import model.Task;

final class SIRTreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;

	private SIRTree tree;
	private SIRNode mover = null;
	private ComplexTask relinquishingParent = null;

	// keeps track of referenced object to avoid repeated unmarshalling
	private Mark incoming = null;

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
	 * Returns a reference to the SIRNode that is being transferred. Also stores
	 * references to the moving node, for tree structure hygiene and tidying up
	 * purposes.
	 */
	@Override
	public Transferable createTransferable(JComponent c) {
		TreePath path = tree.getSelectionPath();
		mover = (SIRNode) path.getLastPathComponent();
		return toXml(mover.getMark(), mover);
	}

	private Transferable toXml(Mark mark, SIRNode n) {
		return new SIRXmlBundle(mark, n);
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport supp) {
		// currently, we only support drag and drop
		// TODO: change this when we implement cut and paste
		if (!supp.isDrop()) {
			return false;
		}

		// check flavour: are we looking at a SIRXmlBundle?
		if (!supp.isDataFlavorSupported(SIRXmlBundle.SIRXml))
			return false;

		// is it legal to drop this data here?
		// start by unmarshalling to object
		if (incoming == null)
			if (!parseXml(supp))
				return false;

		JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();
		// loc is guaranteed non-null
		TreePath path = loc.getPath();
		if (path == null) {
			return false;
		}

		// can't drop a node onto one of its children
		for (Object o : path.getPath()) {
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
			return true;
		}
		// You can't drop anything onto a Checkbox or Criterion
		if (m instanceof Checkbox || m instanceof Criterion) {
			return false;
		}

		// You can't drop Tasks or Checkboxes on QTasks
		return (incoming instanceof QTask || incoming instanceof Criterion);
	}

	private boolean parseXml(TransferHandler.TransferSupport supp) {
		Object parsedXml = null;
		Object xferData = null;
		Transferable trans = supp.getTransferable();
		try {
			xferData = trans.getTransferData(SIRXmlBundle.SIRXml);
		} catch (UnsupportedFlavorException e) {
			System.out.println("Data tastes funny");
		} catch (IOException e) {
			System.out.println("An IOException that couldn't happen, happened");
			e.printStackTrace();
		}
		try {
			JAXBContext context = JAXBContext.newInstance(MarkingScheme.class);
			StringReader reader = new StringReader(xferData.toString());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			parsedXml = unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			System.out.println("Can't parse the XML");
			return false;
		}
		if (parsedXml instanceof Mark) {
			incoming = (Mark) parsedXml;
			try {
				relinquishingParent = ((SIRNode) trans.getTransferData(SIRXmlBundle.SIRNode)).getParentTask();
			} catch (UnsupportedFlavorException e) {
				System.out.println("Can't get node flavor");
			} catch (IOException e) {
				// IO exception? Shouldn't happen.
				e.printStackTrace();
			}
		}
		else
			return false;
		return true;
	}

	public boolean importData(TransferHandler.TransferSupport supp) {
		if (!canImport(supp)) {
			return false;
		}

		JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();

		// incoming will have been set up by canImport but checking is cheap
		if (incoming == null)
			if (!parseXml(supp))
				return false;
		
		// set up the marking scheme
		MarkingScheme scheme = tree.getMarkingScheme();

		// find destination node
		SIRNode dest = (SIRNode) loc.getPath().getLastPathComponent();
		if (dest == null) {
			if (scheme != null) {
				scheme.add(incoming);
				return true;
			}
		}

		Mark newParentTask = dest.getMark();
		int index = loc.getChildIndex();
		if (index == -1) {
			// dropped on the path, insert at end
			index = dest.getChildCount();
		}

		// Are we dealing with a criterion?
		if (incoming instanceof Criterion
				&& newParentTask instanceof ComplexTask) {
			((ComplexTask) newParentTask).insertCriterion(index,
					(Criterion) incoming);
			if (scheme != null)
				scheme.refresh();
			return true;
		}

		// we have a Checkbox, Task or QTask
		try {
			newParentTask.insertAt(index, incoming);
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
		if (!(data instanceof SIRXmlBundle)) {
			return;
		}
		
		SIRNode nodeToDelete = null;
		Mark markToDelete = null;
		try {
			nodeToDelete = (SIRNode) data.getTransferData(SIRXmlBundle.SIRNode);
			markToDelete = nodeToDelete.getMark();
		} catch (UnsupportedFlavorException e) {
			System.out.println("Unsupported flavor");
		} catch (IOException e) {
			// shouldn't happen
			e.printStackTrace();
		}

		if (relinquishingParent == null) {
			((SIRTree) source).getMarkingScheme().removeTask(markToDelete);
		}

		else if (incoming instanceof Criterion)
			relinquishingParent.removeCriterion(markToDelete);
		else
			relinquishingParent.removeSubtask(markToDelete);

		// trigger update
		((SIRTree) source).getMarkingScheme().refresh();
	}
}