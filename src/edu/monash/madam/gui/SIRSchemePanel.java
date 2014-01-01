package edu.monash.madam.gui;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import edu.monash.madam.model.MarkingScheme;


/**
 * Contains metadata for a marking scheme (unit code, activity name, preamble, and subtitle)
 * and allows these to be edited.  An instance of MarkingScheme must be passed to the constructor.
 * 
 * Tasks and subtasks are not displayed here.
 * @author Robyn
 *
 */
public class SIRSchemePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	MarkingScheme theScheme;

	/**
	 * Create the panel.
	 */
	public SIRSchemePanel(MarkingScheme scheme) {
		
		// Displays for marking scheme metadata.  These are all editable.
		JLabel lblUCtext = new JLabel("Unit code");
		JLabel lblUnitCode = new JLabel(scheme.getUnitCode());
		lblUnitCode.setFont(new Font("Tahoma", Font.BOLD, 14));
		JButton btnEditUnitCode = new JButton("");
		btnEditUnitCode.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		
		JLabel lblANtext = new JLabel("Activity name");
		JLabel lblActivityName = new JLabel(scheme.getActivityName());
		lblActivityName.setFont(new Font("Tahoma", Font.BOLD, 14));
		JButton btnEditActivityName = new JButton("");
		btnEditActivityName.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		// Subtitle and Preamble are optional.
		String st = scheme.getSubtitle();
		if (st == null) st = "";
		JLabel lblST = new JLabel("Subtitle");
		JLabel lblSubtitle = new JLabel(st);
		lblSubtitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		JButton btnEditSubtitle = new JButton("");
		btnEditSubtitle.setIcon(new ImageIcon(SIRSchemePanel.class.getResource("/resources/editIcon.png")));
		
		String pr = scheme.getPreamble();
		if (pr == null) pr = "";
		JLabel lblPreamble = new JLabel("Preamble");
		JTextPane preambleTextPane = new JTextPane();
		preambleTextPane.setText(pr);
		JButton btnEditPreamble = new JButton("");
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
}
