package swingui.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import model.scheme.Criterion;

import java.awt.Dimension;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.UIManager;

/**
 * A JPanel that displays assessment criteria for a particular Task or QTask, and
 * allows these to be edited.
 * 
 * @author Robyn
 *
 */
public class CriterionPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -8613718010076085076L;
	private JScrollPane contents;
	private JTable criteria;
	
	
	/**
	 * Creates the panel, including a JTable containing the data and
	 * a panel of buttons for insertion and deletion.
	 */
	public CriterionPanel(TableModelListener listener) {
		setLayout(new MigLayout("fill", "[fill]", "[grow][]"));
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
		
		// set up table and its containers
		CriterionTableModel m = new CriterionTableModel();

		criteria = new JTable(m);
		criteria.setSurrendersFocusOnKeystroke(true);
		criteria.setGridColor(UIManager.getColor("Table.dropLineColor"));
		criteria.setPreferredScrollableViewportSize(new Dimension(450, 40));
		criteria.setRowSelectionAllowed(true);
		criteria.setColumnSelectionAllowed(false);
		criteria.setFillsViewportHeight(true);	// for dnd on empty table
		criteria.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		criteria.setDragEnabled(true);
		criteria.setDropMode(DropMode.INSERT_ROWS);
		
// TODO		criteria.setTransferHandler();
		
		TableColumn scales = criteria.getColumnModel().getColumn(1);
		scales.setCellEditor(new ScaleBoxCellEditor());
		contents = new JScrollPane(criteria);
		contents.setBackground(UIManager.getColor("Panel.background"));
		contents.getViewport().setBackground(UIManager.getColor("Panel.background"));
		
		// get everything to display
		add(contents, "cell 0 0,grow");
		add(buttonPanel, "dock south");
		
		// add handler to allow disabling of navigation if the table data is changed
		m.addTableModelListener(listener);
	}
	
	public void addCriterion(Criterion criterion) {
		((CriterionTableModel) criteria.getModel()).addCriterion(criterion);
		contents.validate();
	}

	@Override
	/**
	 * Handle add/delete button clicks
	 */
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
	
	/**
	 * Returns the list of Criterion as currently displayed.
	 * 
	 * @return ArrayList<Criterion>
	 */
	public ArrayList<Criterion> getCriteria() {
		CriterionTableModel model = (CriterionTableModel) criteria.getModel();
		return model.getCriteria();
	}
}
	