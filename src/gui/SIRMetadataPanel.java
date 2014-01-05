package gui;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

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
		
		// Displays for marking scheme metadata.  These are all editable.
		JLabel lblUCtext = new JLabel("Unit code");
		lblUnitCode = new JLabel(scheme.getUnitCode());
		lblUnitCode.setFont(new Font("Tahoma", Font.BOLD, 14));
		EditButton btnEditUnitCode = new EditButton();
		btnEditUnitCode.setActionCommand("Unit code");
		btnEditUnitCode.addActionListener(this);
		
		
		JLabel lblANtext = new JLabel("Activity name");
		lblActivityName = new JLabel(scheme.getActivityName());
		lblActivityName.setFont(new Font("Tahoma", Font.BOLD, 14));
		EditButton btnEditActivityName = new EditButton();
		btnEditActivityName.setActionCommand("Activity name");
		btnEditActivityName.addActionListener(this);
		
		// Subtitle and Preamble are optional.
		String st = scheme.getSubtitle();
		if (st == null) st = "";
		JLabel lblST = new JLabel("Subtitle");
		lblSubtitle = new JLabel(st);
		lblSubtitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		EditButton btnEditSubtitle = new EditButton();

		btnEditSubtitle.setActionCommand("Subtitle");
		btnEditSubtitle.addActionListener(this);
		
		String pr = scheme.getPreamble();
		if (pr == null) pr = "";
		JLabel lblPreamble = new JLabel("Preamble");
		preambleTextPane = new JTextPane();
		preambleTextPane.setEditable(false);
		preambleTextPane.setText(pr);
		EditButton btnEditPreamble = new EditButton();

		btnEditPreamble.setActionCommand("Preamble");
		btnEditPreamble.addActionListener(this);
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPreamble, Alignment.TRAILING)
								.addComponent(lblST, Alignment.TRAILING)
								.addComponent(lblANtext, Alignment.TRAILING)
								.addComponent(lblUCtext, Alignment.TRAILING))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSubtitle, GroupLayout.PREFERRED_SIZE, 348, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblActivityName, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
								.addComponent(lblUnitCode, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnEditUnitCode, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnEditActivityName, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnEditSubtitle, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnEditPreamble, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(33)
							.addComponent(preambleTextPane, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEditUnitCode, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblUCtext)
							.addComponent(lblUnitCode)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblANtext)
						.addComponent(lblActivityName)
						.addComponent(btnEditActivityName, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(5)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblST)
								.addComponent(lblSubtitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(btnEditSubtitle, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEditPreamble, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPreamble)
							.addGap(10)
							.addComponent(preambleTextPane, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)))
					.addGap(11))
		);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		setLayout(groupLayout);
		this.repaint();
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
