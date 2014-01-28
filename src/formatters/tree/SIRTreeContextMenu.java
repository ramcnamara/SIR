package formatters.tree;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class SIRTreeContextMenu extends JPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem copy = new JMenuItem("Copy");
	JMenuItem cut = new JMenuItem("Cut");
	JMenuItem paste = new JMenuItem("Paste");
	
	public SIRTreeContextMenu(final SIRTree tree) {
		
		Action copyAction = SIRTreeTransferHandler.getCopyAction();
		Action cutAction = SIRTreeTransferHandler.getCutAction();
		Action pasteAction = SIRTreeTransferHandler.getPasteAction();
		TransferActionListener al = new TransferActionListener();
		
		cut.setAction(cutAction);
		copy.setAction(copyAction);
		paste.setAction(pasteAction);
		
		cut.setActionCommand((String)SIRTreeTransferHandler.getCutAction().getValue(Action.NAME));
		copy.setActionCommand((String)SIRTreeTransferHandler.getCopyAction().getValue(Action.NAME));
		paste.setActionCommand((String)SIRTreeTransferHandler.getPasteAction().getValue(Action.NAME));
		
		add(cut);
		add(copy);
		add(paste);
		
		cut.addActionListener(al);
		copy.addActionListener(al);
		paste.addActionListener(al);
	}
	
	public void enablePaste(boolean enable) {
		paste.setEnabled(enable);
	}
}
