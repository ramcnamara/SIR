package swingui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A JButton that acts as a clickable image showing an "edit me" icon.
 * 
 * @author Robyn
 *
 */
public class EditButton extends JButton {

	private static final long serialVersionUID = 8521367046169718607L;

	public EditButton() {
		super(new ImageIcon(SIRMetadataPanel.class.getResource("/resources/editIcon.png")));
		setOpaque(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
	}
}
