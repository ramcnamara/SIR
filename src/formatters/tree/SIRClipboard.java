package formatters.tree;

import java.awt.datatransfer.Clipboard;

/**
 * A singleton Clipboard.
 * 
 * @author ram
 *
 */
public class SIRClipboard {
	private Clipboard clipboard = null;
	
	private SIRClipboard() {
	}
	
	public Clipboard getClipboard() {
		if (clipboard == null)
			clipboard = new Clipboard("SIR");
		
		return clipboard;
	}

}
