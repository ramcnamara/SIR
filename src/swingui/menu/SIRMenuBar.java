package swingui.menu;

import javax.swing.JMenuBar;
import swingui.SIRMainFrame;

public class SIRMenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param menuBar
	 */
	public SIRMenuBar(final SIRMainFrame parent) {
		SIRFileMenu mnFile = new SIRFileMenu(parent);
		this.add(mnFile);
		
	}

}
