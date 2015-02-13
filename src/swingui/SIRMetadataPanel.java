package swingui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;
import model.mappings.OutcomesMap;
import model.mappings.TeachingPeriod;
import model.scheme.MarkingScheme;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;


/**
 * Contains metadata for a marking scheme (unit code, activity name, preamble, and subtitle)
 * and allows these to be edited.  The MarkingScheme to be edited must be passed to the constructor.
 * If a null reference is passed to the constructor, the SIRMetadataPanel displays
 * placeholder text.
 * 
 * Tasks and subtasks are not displayed here.  They are displayed in the SIRCardPanel.
 * @author Robyn
 *
 */
public class SIRMetadataPanel extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;
	private MarkingScheme theScheme;
	private JLabel lblUnitCode;
	private JLabel lblActivityName;
	private JLabel lblSubtitle;
	private JTextPane preambleTextPane;
	private EditButton btnEditUnitCode;
	private EditButton btnEditSubtitle;
	private EditButton btnEditPreamble;
	private EditButton btnEditActivityName;
	private JLabel lblTotalMarks;
	private JLabel mark;

	/**
	 * Create the panel.
	 */
	public SIRMetadataPanel(MarkingScheme scheme) {
		theScheme = scheme;

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
		btnEditUnitCode = new EditButton();
		btnEditUnitCode.setActionCommand("Unit code");


		JLabel lblANtext = new JLabel("Activity name");
		lblActivityName = new JLabel(activityName);
		lblActivityName.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEditActivityName = new EditButton();
		btnEditActivityName.setActionCommand("Activity name");


		JLabel lblST = new JLabel("Subtitle");
		lblSubtitle = new JLabel(subtitle);
		lblSubtitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		btnEditSubtitle = new EditButton();
		btnEditSubtitle.setActionCommand("Subtitle");

		JLabel lblPreamble = new JLabel("Preamble");
		preambleTextPane = new JTextPane();
		preambleTextPane.setEditable(false);
		preambleTextPane.setText(preamble);
		btnEditPreamble = new EditButton();

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

		setLayout(new MigLayout("", "[right][grow,fill][pref!]", "[][][][grow][]"));
		add(lblUCtext, "cell 0 0");
		add(lblANtext, "cell 0 1");
		add(lblST, "cell 0 2");
		add(lblPreamble, "cell 0 3");

		add(lblUnitCode, "cell 1 0, growx");
		add(lblActivityName, "cell 1 1, growx");
		add(lblSubtitle, "cell 1 2, growx");
		add(preambleTextPane, "cell 1 3, growx");

		add(btnEditUnitCode, "cell 2 0");
		add(btnEditActivityName, "cell 2 1");
		add(btnEditSubtitle, "cell 2 2");
		add(btnEditPreamble, "cell 2 3");

		lblTotalMarks = new JLabel("Total marks");
		add(lblTotalMarks, "cell 0 4,alignx trailing");

		mark = new JLabel("<no marking scheme loaded>");
		add(mark, "cell 1 4");
		rereadTotalMark();
		repaint();
	}

	public void setScheme(MarkingScheme scheme) {
		theScheme = scheme;
		rereadTotalMark();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Respond to edit button clicks.  Pop up a dialog with appropriate controls.

		String action = e.getActionCommand();
		String currentValue = "";

		if (action.equals("Activity name"))
			currentValue = lblActivityName.getText();
		else if (action.equals("Unit code")) {
			currentValue = lblUnitCode.getText();

		}
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
			OutcomesMap.reset();
			String targetOffering = newValue + " " + TeachingPeriod.getCurrentTeachingPeriod();
			System.out.println("Seeking outcomes for " + targetOffering);
			for (String guid: OutcomesMap.getOutcomesGuid(targetOffering)) {
				OutcomesMap.loadOutcomes(guid);
			}
		}
		else if (action.equals("Subtitle")) {
			lblSubtitle.setText(newValue);
			theScheme.setSubtitle(newValue);
		}
		else if (action.equals("Preamble")) {
			preambleTextPane.setText(newValue);
			theScheme.setPreamble(newValue);
		}
	}

	@Override
	public void update(Observable schemeobject, Object o) {
		if (schemeobject == null || !(schemeobject instanceof MarkingScheme))
			return;

		MarkingScheme scheme = (MarkingScheme) schemeobject;

		String unitCode = (scheme.getUnitCode() == null? "" : scheme.getUnitCode());
		String activityName = (scheme.getActivityName() == null? "" : scheme.getActivityName());
		String subtitle = (scheme.getSubtitle() == null? "":scheme.getSubtitle());
		String preamble = (scheme.getPreamble() == null? "":scheme.getPreamble());

		lblUnitCode.setText(unitCode);
		lblActivityName.setText(activityName);
		lblSubtitle.setText(subtitle);
		preambleTextPane.setText(preamble);
		rereadTotalMark();

		if (scheme != null && btnEditUnitCode.getActionListeners().length == 0) {
			btnEditUnitCode.addActionListener(this);
			btnEditActivityName.addActionListener(this);
			btnEditSubtitle.addActionListener(this);
			btnEditPreamble.addActionListener(this);
		}
	}

	public void rereadTotalMark() {
		if (theScheme != null)
			mark.setText("" + theScheme.getAvailableMarks());
	}

}
