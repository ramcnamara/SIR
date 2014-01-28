package formatters.tree;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
 
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
 
/**
 * Helper class to deal with ccp.
 * This class was developed by Michael Simons based on an original implementation
 * by Scott Violet.
 * 
 * http://info.michael-simons.eu/2010/07/09/j2se-cutcopypaste-helper/
 * @author ram
 *
 */
public final class CutCopyPasteHelper {
	private static final FocusedAction CUT_INSTANCE;
	private static final FocusedAction COPY_INSTANCE;
	private static final FocusedAction PASTE_INSTANCE;
 
	private static final Clipboard CLIPBOARD;
 
	private static final List<FocusedAction> FOCUSED_ACTIONS = new ArrayList<FocusedAction>();
 
	static {
		Clipboard clipboard;
		try {
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (SecurityException e) {
			// Don't have access to the clipboard, create a new one
			clipboard = new Clipboard("SIR Clipboard");
		}
		CLIPBOARD = clipboard;
 
		FOCUSED_ACTIONS.add(CUT_INSTANCE = new CutAction());
		FOCUSED_ACTIONS.add(COPY_INSTANCE = new CopyAction());
		FOCUSED_ACTIONS.add(PASTE_INSTANCE = new PasteAction());		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new PropertyChangeHandler());
	}
 
	static Clipboard getClipboard() {
		return CLIPBOARD;
	}
 
	/**
	 * Returns an action to perform a cut operation.
	 *
	 * @return the cut action
	 */
	public static Action getCutAction() {
		return CUT_INSTANCE;
	}
 
	/**
	 * Returns an action to perform a copy operation.
	 *
	 * @return the copy action
	 */
	public static Action getCopyAction() {
		return COPY_INSTANCE;
	}
 
	/**
	 * Returns an action to perform a paste operation.
	 *
	 * @return the paste action
	 */
	public static Action getPasteAction() {
		return PASTE_INSTANCE;
	}
 
	private static void updateActions(Component focusedComponent) {
		for(FocusedAction action : FOCUSED_ACTIONS)						
			action.update(focusedComponent);					
	}
 
	private static final class PropertyChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getPropertyName() == "permanentFocusOwner") {
				updateActions((Component)e.getNewValue());
			}
		}
	}
 
	private CutCopyPasteHelper() {
	}
 
 
	private static abstract class FocusedAction extends AbstractAction {        
		private static final long serialVersionUID = 9134168335741527777L;		
		protected JComponent focusedComponent;
 
		public FocusedAction(String name) {
			super(name);
		}		
 
		protected void update(Component permanentFocusOwner) {
			this.focusedComponent = (permanentFocusOwner instanceof JComponent) ? (JComponent)permanentFocusOwner : null; 			
			this.checkTarget();
		}
 
		public JComponent getFocusedComponent() {
			return focusedComponent;
		}		
 
		public boolean actionSupported(int actionMap, int action) {	
			return (actionMap & action) != 0;
		}
 
		abstract protected void checkTarget();	
	}
 
	protected static void export(final int exportAction, final JComponent component) {
		final Clipboard clipboard = getClipboard();		
		component.getTransferHandler().exportToClipboard(component, clipboard, exportAction);
	}
 
	private static final class CopyAction extends FocusedAction {		
		private static final long serialVersionUID = 1765740238724009255L;
 
		public CopyAction() {
			super("Copy");	
			super.setEnabled(false);
			super.putValue(ACTION_COMMAND_KEY, "a6f9f030-c408-4531-857f-dd3aad7b43f6");
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		}
 
		@Override
		public void actionPerformed(ActionEvent e) {			
			export(TransferHandler.COPY, focusedComponent);
		}
 
		@Override
		public void checkTarget() {
			super.setEnabled(focusedComponent != null && focusedComponent.getTransferHandler()!= null && actionSupported(focusedComponent.getTransferHandler().getSourceActions(focusedComponent), TransferHandler.COPY));
		}		
	}
 
	private static final class CutAction extends FocusedAction {		
		private static final long serialVersionUID = 1765740238724009255L;
 
		public CutAction() {
			super("Cut");	
			super.setEnabled(false);
			super.putValue(ACTION_COMMAND_KEY, "aa33f95a-c741-407a-a0ca-0c3aedd33c4d");
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		}
 
		@Override
		public void actionPerformed(ActionEvent e) {			
			export(TransferHandler.MOVE, focusedComponent);
		}
 
		@Override
		public void checkTarget() {
			super.setEnabled(focusedComponent != null && focusedComponent.getTransferHandler()!= null && actionSupported(focusedComponent.getTransferHandler().getSourceActions(focusedComponent), TransferHandler.MOVE));
		}		
	}
 
 
	private static final class PasteAction extends FocusedAction {		
		private static final long serialVersionUID = -2117467682323608417L;
 
		public PasteAction() {
			super("Paste");	
			super.setEnabled(false);
			super.putValue(ACTION_COMMAND_KEY, "2676413d-8b9e-4d64-b59a-800db6747d80");
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		}
 
		public void checkTarget() {			
			super.setEnabled(focusedComponent != null && focusedComponent.getTransferHandler() != null && focusedComponent.getTransferHandler().canImport(focusedComponent, getClipboard().getAvailableDataFlavors()));			
		}
 
		public void actionPerformed(ActionEvent e) {
			Clipboard clipboard = getClipboard();
			JComponent target = getFocusedComponent();
			target.getTransferHandler().importData(target, clipboard.getContents(null));
		}		
	}
}
