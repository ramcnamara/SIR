package gui.cards;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;
import model.Criterion;

public class CriterionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane contents;
	private JTable criteria;
	
	public CriterionPanel() {
		setLayout(new MigLayout("fill", "[fill]", "[pref!]"));
		// set up list and its containers
		CriterionTableModel m = new CriterionTableModel();
		criteria = new JTable(m);
		contents = new JScrollPane(criteria);
		
		// get everything to display
		add(contents, "dock center");
	}
	
	public void addCriterion(Criterion criterion) {
		((CriterionTableModel) criteria.getModel()).addCriterion(criterion);
	}
}
	