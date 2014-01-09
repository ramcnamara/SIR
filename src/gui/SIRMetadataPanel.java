package gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;
import model.MarkingScheme;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



/**
 * Contains metadata for a marking scheme (unit code, activity name, preamble, and subtitle)
 * and allows these to be edited.  An instance of MarkingScheme must be passed to the constructor.
 * 
 * Tasks and subtasks are not displayed here.
 * @author Robyn
 *
 */
public class SIRMetadataPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	MarkingScheme theScheme;
	private JLabel lblUnitCode;
	private JLabel lblActivityName;
	private JLabel lblSubtitle;
	private JTextPane preambleTextPane;

	/**
	 * Create the panel.
	 */
	public SIRMetadataPanel(MarkingScheme scheme) {
		
		String notLoadedString = new String("<no marking scheme loaded>");
		String unitCode = notLoadedString;
		String activityName = notLoadedString;
		String subtitle = notLoadedString;
		String preamble = notLoadedString;
		

		
		if (scheme != null) {
			unitCode = scheme.getUnitCode();
			activityName = scheme.getActivityName();
			subtitle = (scheme.getSubtitle() == null? "":scheme.getSubtitle());
			preamble = (scheme.getPreamble() == null? "":scheme.getPreamble());
		}
		
		// Displays for marking scheme metadata.  These are all editable.
		JLabel lblUCtext = new JLabel("Unit code");
		lblUnitCode = new JLabel(unitCode);
		lblUnitCode.setFont(new Font("Tahoma", Font.BOLD, 14));
		EditButton btnEditUnitCode = new EditButton();
		btnEditUnitCode.setActionCommand("Unit code");
		
		
		JLabel lblANtext = new JLabel("Activity name");
		lblActivityName = new JLabel(activityName);
		lblActivityName.setFont(new Font("Tahoma", Font.BOLD, 14));
		EditButton btnEditActivityName = new EditButton();
		btnEditActivityName.setActionCommand("Activity name");


		JLabel lblST = new JLabel("Subtitle");
		lblSubtitle = new JLabel(subtitle);
		lblSubtitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		EditButton btnEditSubtitle = new EditButton();
		btnEditSubtitle.setActionCommand("Subtitle");
		
		JLabel lblPreamble = new JLabel("Preamble");
		preambleTextPane = new JTextPane();
		preambleTextPane.setEditable(false);
		preambleTextPane.setText(preamble);
		EditButton btnEditPreamble = new EditButton();

		btnEditPreamble.setActionCommand("Preamble");
		
		if (scheme != null) {
			btnEditUnitCode.addActionListener(this);
			btnEditActivityName.addActionListener(this);
			btnEditSubtitle.addActionListener(this);
			btnEditPreamble.addActionListener(this);
		}
		
		else
			for (Component c:this.getComponents()) {
				c.setEnabled(false);
			}
		
		setLayout(new MigLayout("", "[align right][fill][pref!]", "[][][][fill]"));
		add(lblUCtext, "cell 0 0");
		add(lblANtext, "cell 0 1");
		add(lblST, "cell 0 2");
		add(lblPreamble, "cell 0 3");
		
		add(lblUnitCode, "cell 1 0");
		add(lblActivityName, "cell 1 1");
		add(lblSubtitle, "cell 1 2");
		add(preambleTextPane, "cell 1 3");
		
		add(btnEditUnitCode, "cell 2 0");
		add(btnEditActivityName, "cell 2 1");
		add(btnEditSubtitle, "cell 2 2");
		add(btnEditPreamble, "cell 2 3");
		
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Respond to edit button clicks.  Pop up a dialog with appropriate controls.
		String action = e.getActionCommand();
		String currentValue = "";
		
		if (action.equals("Activity name"))
			currentValue = lblActivityName.getText();
		else if (action.equals("Unit code"))
			currentValue = lblUnitCode.getText();
		else if (action.equals("Subtitle"))
			currentValue = lblSubtitle.getText();
		else if (action.equals("Preamble"))
			currentValue = preambleTextPane.getText();
		else return;
		
		SIREditDialog dlg = new SIREditDialog(action, currentValue);
		dlg.setVisible(true);
		String newValue = dlg.getValue();
		
		if (action.equals("Activity name")) {
			lblActivityName.setText(newValue);
			theScheme.setActivityName(newValue);
		}
		else if (action.equals("Unit code")) {
			lblUnitCode.setText(newValue);
			theScheme.setUnitCode(newValue);
		}
		else if (action.equals("Subtitle")) {
			lblSubtitle.setText(newValue);
			theScheme.setSubtitle(newValue);
		}
		else if (action.equals("Preamble")) {
			preambleTextPane.setText(newValue);
			theScheme.setSubtitle(newValue);
		}
	}
}
