package swingui.cards;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;

import model.Scale;
import net.miginfocom.swing.MigLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;

/**
 * Dialog displayed when a user has selected the "New scale..." option in a
 * scale selection dropdown.
 * 
 * 
 * @author Robyn
 *
 */
public class NewScaleDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable levelTable;
	private Scale theScale;

	/**
	 * Create the dialog.
	 */
	
	public NewScaleDialog(Window parent) {
		super(parent, "Add scale", JDialog.ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 280, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][grow][][]"));
		JLabel lblPrompt = new JLabel("Enter new scale:");
		contentPanel.add(lblPrompt, "cell 0 0,alignx left,aligny top");
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		
		Object[][] emptyStrings = new Object[][]{{""},{""}};
		String[] columnheader = {"Level"};
		DefaultTableModel m = new DefaultTableModel(emptyStrings, columnheader);
		levelTable = new JTable(m);
		levelTable.setFillsViewportHeight(false);
		levelTable.setShowGrid(true);
		levelTable.setGridColor(UIManager.getColor("Table.foreground"));
		levelTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		contentPanel.add(levelTable, "cell 0 1,grow");

		JButton btnNewButton = new JButton("Add level");
		btnNewButton.setActionCommand("Add level");
		btnNewButton.addActionListener(this);
		contentPanel.add(btnNewButton, "cell 0 3,alignx right");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);

	}

	@Override
	/**
	 * Handle "Add level", "OK", and "Cancel" buttons
	 */
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
		if (cmd.equals("Add level")) {
			((DefaultTableModel) levelTable.getModel()).addRow(new Object[]{""});
		}
		if (cmd.equals("OK")) {
			ArrayList<String> levels = new ArrayList<String>();
			DefaultTableModel m = (DefaultTableModel) levelTable.getModel();
			for (int i = 0; i < m.getRowCount(); i++) {
				Object val = m.getValueAt(i, 0);
				if (val != null) {
					levels.add(val.toString());
				}
			}
			
			theScale = Scale.makeScheme(levels.toArray(new String[levels.size()]));
			setVisible(false);
			dispose();
		}
		
		if (cmd.equals("Cancel")) {
			setVisible(false);
			dispose();
		}

	}
	
	
	/**
	 * Allow callers to access the new scale.
	 * 
	 * @return the Scale created in this dialog
	 */
	public Scale getScale() {
		return theScale;
	}

}
