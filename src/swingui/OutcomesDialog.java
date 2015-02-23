package swingui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import model.mappings.OutcomesMap;
import model.outcomes.LearningOutcomes;
import model.scheme.LearningOutcomeRef;
import model.scheme.Mark;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

public class OutcomesDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, OutcomesPanel> guidmap = new HashMap<String, OutcomesPanel>();
	
	public OutcomesDialog(String offering, Mark mark) {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		for (String guid : OutcomesMap.getGuidsForOffering(offering)) {
			LearningOutcomes lo = OutcomesMap.getOutcomesForGuid(guid);
			OutcomesPanel op = new OutcomesPanel(lo);
			guidmap.put(guid, op);
			tabbedPane.addTab(lo.getName(), op);
		}
		
		for (LearningOutcomeRef lref : mark.getOutcomes()) {
			String guid = lref.getGuid();
			OutcomesPanel op = guidmap.get(guid);
			if (op != null) {
				op.setValue(lref.getIndex(), lref.getWeight());
			}
		}
	}
	
	public List<LearningOutcomeRef> getSelectedOutcomes() {
		List<LearningOutcomeRef> outcomes = new ArrayList<LearningOutcomeRef>();
		for (OutcomesPanel op : guidmap.values()) {
			outcomes.addAll(op.getSelectedOutcomes());
		}
		
		return outcomes;
	}


}
