package formatters.tree;

import java.awt.datatransfer.Clipboard;

/**
 * A singleton Clipboard.
 * 
 * @author ram
 *
 */
public class SIRClipboard {
	private static Clipboard clipboard = null;
	
	private SIRClipboard() {
	}
	
	public static Clipboard getClipboard() {
		if (clipboard == null)
			clipboard = new Clipboard("SIR");
		
		return clipboard;
	}

}
