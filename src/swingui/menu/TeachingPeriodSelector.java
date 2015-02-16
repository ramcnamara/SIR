package swingui.menu;

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import model.mappings.TeachingPeriod;


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
		tpBox.setSelectedItem(TeachingPeriod.getCurrentTeachingPeriod());
		JLabel tpLabel = new JLabel("Teaching period:");
		tpLabel.setLabelFor(tpBox);
		add(tpLabel);
		add(tpBox);
		
		// Set up year spinner
		Calendar calendar = Calendar.getInstance();
		int year;
		String currentYear = TeachingPeriod.getCurrentTeachingYear();
		if (currentYear == null)
			year = calendar.get(Calendar.YEAR);
		else
			try {
				year = Integer.parseInt(currentYear);
			} catch (NumberFormatException e) {
				year = calendar.get(Calendar.YEAR);
			}
		
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
	 * @return String containing teaching period + ", " + year.
	 */
	public String getTeachingPeriod() {
		return tpBox.getSelectedItem().toString(); 
	}

	public String getTeachingYear() {
		// TODO Auto-generated method stub
		return yearSpinner.getValue().toString();
	}
}
