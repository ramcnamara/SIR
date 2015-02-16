package swingui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import model.mappings.TeachingPeriod;
import swingui.SIRMainFrame;

public class SIROptionsMenu extends JMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SIROptionsMenu(final SIRMainFrame parent) {
		super("Options");
		
		JMenuItem mntmNew = new JMenuItem("Set teaching period");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				TeachingPeriodSelector tps = new TeachingPeriodSelector(TeachingPeriod.getTeachingPeriods());
				int option =JOptionPane.showConfirmDialog(parent, tps, "Select teaching period and year", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				
				// User cancelled?
				if (option == JOptionPane.CANCEL_OPTION)
					return;
				
				// User selected a new teaching period and year, so store that to current preferences
				String teachingPeriod = tps.getTeachingPeriod();
				String teachingYear = tps.getTeachingYear();
				TeachingPeriod.setCurrentTeachingPeriod(teachingPeriod, teachingYear);
			}
		});
		this.add(mntmNew);
	}

}
