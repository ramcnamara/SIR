package gui.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import model.Criterion;
import java.awt.Dimension;
import java.awt.Component;

public class CriterionPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JScrollPane contents;
	private JTable criteria;
	
	public CriterionPanel() {
		setLayout(new MigLayout("fill", "[fill]", "[pref!,grow][]"));
		// create button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton addButton = new JButton("Add criterion");
		addButton.setActionCommand("Add criterion");
		addButton.addActionListener(this);
		JButton deleteButton = new JButton("Delete criterion");
		deleteButton.setActionCommand("Delete criterion");
		deleteButton.addActionListener(this);
		
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		
		// set up list and its containers
		CriterionTableModel m = new CriterionTableModel();
		criteria = new JTable(m);
		criteria.setPreferredScrollableViewportSize(new Dimension(450, 40));
		criteria.setRowSelectionAllowed(true);
		criteria.setColumnSelectionAllowed(false);
		TableColumn scales = criteria.getColumnModel().getColumn(1);
		scales.setCellEditor(new ScaleboxCellEditor());
		contents = new JScrollPane(criteria);
		
		// get everything to display
		add(contents, "cell 0 0,push ,grow");
		add(buttonPanel, "dock south");
	}
	
	public void addCriterion(Criterion criterion) {
		((CriterionTableModel) criteria.getModel()).addCriterion(criterion);
		contents.validate();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
		
		if (cmd.equals("Add criterion")) {
			Criterion c = new Criterion();
			((CriterionTableModel) criteria.getModel()).addCriterion(c);
		}
		else if (cmd.equals("Delete criterion")) {
			int row = criteria.getSelectedRow();
			if (row != -1)
			((CriterionTableModel) criteria.getModel()).removeRow(row);
		}
		
	}
}
	