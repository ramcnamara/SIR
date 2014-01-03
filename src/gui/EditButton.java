package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class EditButton extends JButton {

	private static final long serialVersionUID = 8521367046169718607L;

	EditButton() {
		super(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		setOpaque(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
	}
}
