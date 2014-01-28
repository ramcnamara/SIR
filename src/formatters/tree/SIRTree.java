package formatters.tree;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import model.MarkingScheme;
import model.QTask;

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
	 * @param root
	 *            the TreeNode at the root of the SIRTree
	 */
	public SIRTree(SIRNode root, MarkingScheme scheme) {
		super(root);
		theScheme = scheme;
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new SIRTreeCellRenderer());

		setDragEnabled(true);
		setDropMode(DropMode.ON_OR_INSERT);
		setTransferHandler(new SIRTreeTransferHandler(this));
		setShowsRootHandles(true);

		MouseListener ml = new MouseListener() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					Point p = e.getPoint();
					TreePath path = getPathForLocation(p.x, p.y);
				}
			}// mousePressed

			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					Component mc = e.getComponent();
					TreePath path = getSelectionPath();
					if (path != null) {
						SIRTreeContextMenu popup = new SIRTreeContextMenu();
						popup.show(mc, e.getX(), e.getY());
						
						Clipboard theClipboard = SIRClipboard.getClipboard();
						
						// can't paste to Checkbox or Criterion
						if (theClipboard.isDataFlavorAvailable(new DataFlavor(model.Checkbox.class, "Mark")) ||
								theClipboard.isDataFlavorAvailable(new DataFlavor(model.Criterion.class, "Mark"))) {
							popup.enablePaste(false);
						}
						else {
							SIRNode curr = (SIRNode) path.getLastPathComponent();
							// QTasks can contain only QTask or Criterion
							if (curr.getMark() instanceof QTask)
								popup.enablePaste(theClipboard.isDataFlavorAvailable(new DataFlavor(model.QTask.class, "Mark")) ||
										theClipboard.isDataFlavorAvailable(new DataFlavor(model.Criterion.class, "Mark")));
							
							else // it's a Task, paste anything here if it exists
							{
								Transferable contents = theClipboard.getContents(null);
								popup.enablePaste(contents != null);
							}
						}
						//if (popup.getParent().getX() == 0)
						//	popup.show(mc, e.getX(),
						//			e.getY() - popup.getHeight());
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		};
		
		addMouseListener(ml);

	}

	public MarkingScheme getMarkingScheme() {
		return theScheme;

	}
}
