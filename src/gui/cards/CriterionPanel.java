package gui.cards;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import net.miginfocom.swing.MigLayout;
import model.Criterion;

public class CriterionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane contents;
	private JList criteria;
	
	public CriterionPanel() {
		setLayout(new MigLayout("fill", "[fill]", "[pref!]"));
		// set up list and its containers
		ListModel m = new DefaultListModel();
		criteria = new JList(m);
		criteria.setCellRenderer(new CriterionCellRenderer());
		contents = new JScrollPane(criteria);
		
		// get everything to display
		add(contents, "dock center");
	}
	
	public void addCriterion(Criterion criterion) {
		((DefaultListModel) criteria.getModel()).addElement(criterion);
	}
}
	