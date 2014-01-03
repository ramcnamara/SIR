package gui;

import gui.richtext.SIRRichTextComponentFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class SIREditDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextComponent textEntry;

	/**
	 * Create the dialog.
	 */
	public SIREditDialog(String name, JTextComponent textEntry) {
		super((Frame)null, "Editing " + name);
		add(textEntry);
	}
	
	public String showDialog() {
		setVisible(true);
		return textEntry.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Cancel"))
			textEntry.setText("");
	}

}
