package swingui.menu;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;


public class TeachingPeriodSelector extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox tpBox;
	private JSpinner yearSpinner;

	public TeachingPeriodSelector(List<String> teachingPeriodNames) {
		super(new GridLayout(2, 2));
		
		
		// Set up teaching period spinner
		tpBox = new JComboBox(teachingPeriodNames.toArray());
		JLabel tpLabel = new JLabel("Teaching period:");
		tpLabel.setLabelFor(tpBox);
		add(tpLabel);
		add(tpBox);
		
		// Set up year spinner
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		SpinnerModel yearModel = new SpinnerNumberModel(year, year-100, year+100, 1);
		yearSpinner = new JSpinner(yearModel);
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setLabelFor(yearSpinner);
		add(yearLabel);
		add(yearSpinner);
		
	
	}
	
	/**
	 * Retrieve current values of teaching period and year.
	 * 
	 * @return String[] tp such that tp[0] represents the teaching period and tp[1] represents the year.
	 */
	public List<String> getTeachingPeriod() {
		ArrayList<String> tplist = new ArrayList<String>();
		tplist.add((String) tpBox.getSelectedItem());
		tplist.add(yearSpinner.getValue().toString());
		return tplist;
	}
}