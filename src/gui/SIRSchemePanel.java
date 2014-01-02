package gui;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
public class SIRSchemePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	MarkingScheme theScheme;
	private JLabel lblUnitCode;
	private JLabel lblActivityName;
	private JLabel lblSubtitle;
	private JTextPane preambleTextPane;

	/**
	 * Create the panel.
	 */
	public SIRSchemePanel(MarkingScheme scheme) {
		
		// Displays for marking scheme metadata.  These are all editable.
		JLabel lblUCtext = new JLabel("Unit code");
		lblUnitCode = new JLabel(scheme.getUnitCode());
		lblUnitCode.setFont(new Font("Tahoma", Font.BOLD, 14));
		JButton btnEditUnitCode = new JButton("");
		btnEditUnitCode.setActionCommand("Unit code");
		btnEditUnitCode.addActionListener(this);
		btnEditUnitCode.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		
		JLabel lblANtext = new JLabel("Activity name");
		lblActivityName = new JLabel(scheme.getActivityName());
		lblActivityName.setFont(new Font("Tahoma", Font.BOLD, 14));
		JButton btnEditActivityName = new JButton("");
		btnEditActivityName.setActionCommand("Activity name");
		btnEditActivityName.addActionListener(this);
		btnEditActivityName.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		// Subtitle and Preamble are optional.
		String st = scheme.getSubtitle();
		if (st == null) st = "";
		JLabel lblST = new JLabel("Subtitle");
		lblSubtitle = new JLabel(st);
		lblSubtitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		JButton btnEditSubtitle = new JButton("");

		btnEditSubtitle.setActionCommand("Subtitle");
		btnEditSubtitle.addActionListener(this);
		btnEditSubtitle.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		String pr = scheme.getPreamble();
		if (pr == null) pr = "";
		JLabel lblPreamble = new JLabel("Preamble");
		preambleTextPane = new JTextPane();
		preambleTextPane.setText(pr);
		JButton btnEditPreamble = new JButton("");

		btnEditPreamble.setActionCommand("Preamble");
		btnEditPreamble.addActionListener(this);
		btnEditPreamble.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblANtext)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblActivityName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEditActivityName, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUCtext)
								.addComponent(lblST)
								.addComponent(lblPreamble))
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(preambleTextPane, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnEditPreamble, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblUnitCode, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnEditUnitCode, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblSubtitle, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
									.addComponent(btnEditSubtitle, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
									.addGap(245))))))
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
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblST)
							.addComponent(lblSubtitle))
						.addComponent(btnEditSubtitle, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnEditPreamble, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPreamble)
								.addComponent(preambleTextPane, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
							.addGap(4))))
		);
		setLayout(groupLayout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Respond to edit button clicks.  Pop up a dialog with appropriate controls.
		String action = e.getActionCommand();
		String currentValue = "";
		
		if (e.equals("Activity name"))
			currentValue = lblActivityName.getText();
		else if (e.equals("Unit code"))
			currentValue = lblUnitCode.getText();
		else if (e.equals("Subtitle"))
			currentValue = lblSubtitle.getText();
		else if (e.equals("Preamble"))
			currentValue = preambleTextPane.getText();
		else return;
		
		SIREditDialog dlg = new SIREditDialog(action, currentValue);
		String newValue = dlg.showDialog();
		
		if (e.equals("Activity name"))
			lblActivityName.setText(newValue);
		else if (e.equals("Unit code"))
			lblUnitCode.setText(newValue);
		else if (e.equals("Subtitle"))
			lblSubtitle.setText(newValue);
		else if (e.equals("Preamble"))
			preambleTextPane.setText(newValue);	
	}
}
