package swingui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import model.mappings.OutcomesMap;
import model.outcomes.LearningOutcomes;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

public class OutcomesDialog extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, OutcomesPanel> guidmap = new HashMap<String, OutcomesPanel>();
	
	public OutcomesDialog(String offering) {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		for (String guid : OutcomesMap.getGuidsForOffering(offering)) {
			LearningOutcomes lo = OutcomesMap.getOutcomesForGuid(guid);
			OutcomesPanel op = new OutcomesPanel(lo);
			guidmap.put(guid, op);
			tabbedPane.addTab(lo.getName(), op);
		}
		
		pack();
	}


}
