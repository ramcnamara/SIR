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
	 * Returns a bundle containing an XML representation of the Mark to be
	 * transferred and a reference to the node itself. Also locally stores a
	 * reference to the moving node, for tree structure hygiene and tidying up
	 * purposes.
	 * 
	 * @param c
	 *            the JComponent that contains the moving object (in practice, a
	 *            SIRTree)
	 */
	@Override
	public Transferable createTransferable(JComponent c) {
		mover = null;
		relinquishingParent = null;
		incoming = null;

		TreePath path = tree.getSelectionPath();

		// Don't allow drag of root node
		if (path.getPathCount() == 1)
			return null;
		mover = (SIRNode) path.getLastPathComponent();
		relinquishingParent = ((SIRNode)mover.getParent()).getParentTask();
		return toXml(mover.getMark(), mover);
	}

	/**
	 * Creates the SIRXmlBundle.
	 * 
	 * @param mark
	 *            the Mark that is being moved or copied
	 * @param n
	 *            the SIRNode in which the Mark is stored
	 * @return
	 */
	private Transferable toXml(Mark mark, SIRNode n) {
		relinquishingParent = n.getParentTask();
			
		return new SIRXmlBundle(mark, n);
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport supp) {

		// check flavour: are we looking at a SIRXmlBundle?
		if (!supp.isDataFlavorSupported(SIRXmlBundle.SIRXml))
			return false;

		// is it legal to drop this data here?
		// start by unmarshalling to object
		if (incoming == null)
			if (!parseXml(supp))
				return false;

		TreePath path;

		if (supp.isDrop()) {
			JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();
			// loc is guaranteed non-null
			path = loc.getPath();
			
			// can't drop a node onto one of its children
			// (can paste a copy though, so this has to be guarded by isDrop())
			for (Object o : path.getPath()) {
				if (o.equals(mover)) {
					return false;
				}
			}
		} else {
			path = ((SIRTree) supp.getComponent()).getSelectionPath();
		}
		if (path == null) {
			return false;
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
		if (m instanceof QTask) {
			return (incoming instanceof QTask || incoming instanceof Criterion);
		}

		// The only thing you can't drop on the root node is a Criterion.
		return !(incoming instanceof Criterion);
	}

	/**
	 * Parses the XML data being transferred and sets up sundry fields.
	 * 
	 * @param supp
	 *            the TransferSupport object containing the XML to be parsed
	 * @return true if the data was successfully parsed to a Mark object
	 */
	private boolean parseXml(TransferHandler.TransferSupport supp) {
		Object parsedXml = null;
		Object xferData = null;
		Transferable trans = supp.getTransferable();
		try {
			xferData = trans.getTransferData(SIRXmlBundle.SIRXml);
		} catch (UnsupportedFlavorException e) {
			System.out.println("Data tastes funny");
			return false;
		} catch (IOException e) {
			System.out.println("An IOException that couldn't happen, happened");
			e.printStackTrace();
			return false;
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
				relinquishingParent = ((SIRNode) trans
						.getTransferData(SIRXmlBundle.SIRNode)).getParentTask();
			} catch (UnsupportedFlavorException e) {
				System.out.println("Can't get node flavor");
			} catch (IOException e) {
				// IO exception? Shouldn't happen.
				e.printStackTrace();
				return false;
			}
		} else
			// didn't parse to anything we know about
			return false;
		return true;
	}

	/**
	 * Import dragged data into a SIRTree. Drag and drop into other Components
	 * is done in other TransferHandlers.
	 */
	public boolean importData(TransferHandler.TransferSupport supp) {
		if (!canImport(supp)) {
			return false;
		}

		SIRNode dest;
		int index = -1;
		

		// set up the marking scheme
		MarkingScheme scheme = tree.getMarkingScheme();
		
		if (supp.isDrop())	{
			JTree.DropLocation loc = (JTree.DropLocation) supp.getDropLocation();
		// incoming will have been set up by canImport but checking is cheap
		if (incoming == null)
			if (!parseXml(supp))
				return false;


		// find destination node
		dest = (SIRNode) loc.getPath().getLastPathComponent();
		index = loc.getChildIndex();
		}
		
		else {
			TreePath path = ((SIRTree) supp.getComponent()).getSelectionPath();
			dest = (SIRNode) path.getLastPathComponent();
		}

		if (dest == null || dest.getMark() == null) {
			if (scheme != null) {
				if (index == -1)
					scheme.add(incoming);
				else
					scheme.insertAt(index, incoming);
				return true;
			} else {
				return false;
			}
		}

		Mark newParentTask = dest.getMark();

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