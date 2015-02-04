package formatters.tree;

import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import model.scheme.MarkingScheme;
import model.scheme.QTask;

/**
 * An extension of JTree that implements drag and drop.
 * 
 * @author ram
 * 
 */
public class SIRTree extends JTree {

	private static final long serialVersionUID = 1L;
	private MarkingScheme theScheme;
	private SIRTreeContextMenu popup;

	/**
	 * Wrapper for JTree constructor.
	 * 
	 * @param root
	 *            the TreeNode at the root of the SIRTree
	 */
	public SIRTree(SIRNode root, MarkingScheme scheme) {
		super(root);
		theScheme = scheme;
		
		popup = new SIRTreeContextMenu(this);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new SIRTreeCellRenderer());

		setDragEnabled(true);
		setDropMode(DropMode.ON_OR_INSERT);
		setTransferHandler(new SIRTreeTransferHandler(this));
		setShowsRootHandles(true);
		
		// Set up key bindings for ccp
		ActionMap map = getActionMap();
		map.put(CutCopyPasteHelper.getCutAction().getValue(Action.NAME),
				CutCopyPasteHelper.getCutAction());
        map.put(CutCopyPasteHelper.getCopyAction().getValue(Action.NAME),
        		CutCopyPasteHelper.getCopyAction());
        map.put(CutCopyPasteHelper.getPasteAction().getValue(Action.NAME),
        		CutCopyPasteHelper.getPasteAction());
		
        InputMap imap = this.getInputMap();
        imap.put(KeyStroke.getKeyStroke("ctrl X"),
        		CutCopyPasteHelper.getCutAction().getValue(Action.NAME));
        imap.put(KeyStroke.getKeyStroke("ctrl C"),
        		CutCopyPasteHelper.getCopyAction().getValue(Action.NAME));
        imap.put(KeyStroke.getKeyStroke("ctrl V"),
        		CutCopyPasteHelper.getPasteAction().getValue(Action.NAME));

		// Display popup menu on right click
		MouseListener ml = new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					Component mc = e.getComponent();
					TreePath path = getSelectionPath();
					if (path != null) {
						popup.show(mc, e.getX(), e.getY());
						
						Clipboard theClipboard = CutCopyPasteHelper.getClipboard();
						
						// can't paste to Checkbox or Criterion
						if (theClipboard.isDataFlavorAvailable(new DataFlavor(model.scheme.Checkbox.class, "Mark")) ||
								theClipboard.isDataFlavorAvailable(new DataFlavor(model.scheme.Criterion.class, "Mark"))) {
							popup.enablePaste(false);
						}
						else {
							SIRNode curr = (SIRNode) path.getLastPathComponent();
							// QTasks can contain only QTask or Criterion
							if (curr.getMark() instanceof QTask)
								popup.enablePaste(theClipboard.isDataFlavorAvailable(new DataFlavor(model.scheme.QTask.class, "Mark")) ||
										theClipboard.isDataFlavorAvailable(new DataFlavor(model.scheme.Criterion.class, "Mark")));
							
							else // it's a Task, paste anything here if it exists
							{
								Transferable contents = theClipboard.getContents(null);
								popup.enablePaste(contents != null);
							}
						}
					}
				}
			}
		};
		
		addMouseListener(ml);

	}

	public MarkingScheme getMarkingScheme() {
		return theScheme;

	}
}
