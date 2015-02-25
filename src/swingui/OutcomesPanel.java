package swingui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import model.outcomes.LearningOutcomes;
import model.outcomes.LearningOutcomes.Outcomes;
import model.scheme.LearningOutcomeRef;

/**
 * Panel for displaying learning outcomes, so that they are selectable.
 * 
 * The OutcomesDialog contains one of these JPanels per set of learning outcomes, each in
 * its own tab.
 * 
 * @author ram
 *
 */
public class OutcomesPanel extends JPanel {


	private static final long serialVersionUID = 3981253910395288526L;
	private OutcomesTable outcomes;
	private String guid;

	/**
	 * Constructor.  
	 * 
	 * @param lo the LearningOutcomes to display in this panel
	 */
	public OutcomesPanel(LearningOutcomes lo) {
		setLayout(new BorderLayout());
		guid = lo.getGuid();
		
		JScrollPane contents;

		String desc = "";
		List<String> outcomeList = new ArrayList<String>();
		
		// Set name and description if learning outcomes have been provided.
		if (lo != null) {
			desc = lo.getDescription();
			Outcomes oc = lo.getOutcomes();
			if (oc != null)
				outcomeList = oc.getOutcome();
		}
		JLabel lblDescription = new JLabel(desc);
		add(lblDescription, BorderLayout.NORTH);

		// Construct table model and instantiate table
		OutcomesTableModel m = new OutcomesTableModel(outcomeList);
		outcomes = new OutcomesTable(m);
		outcomes.setFillsViewportHeight(true);
		

		// Set table column widths: weights column should be set to the preferred size of
		// a weight selection combobox; the description column will expand to fill the remainder of the space
		TableColumn weights = outcomes.getColumnModel().getColumn(OutcomesTableModel.WEIGHT_COL);
		int boxWidth = new WeightBox().getPreferredSize().width;
		weights.setMaxWidth(boxWidth);
		weights.setMinWidth(boxWidth);
		
		// Set combobox editor for weights column, wordwrapping TextArea renderer for description column
		weights.setCellEditor(new WeightEditor());
		TableColumn descriptions = outcomes.getColumnModel().getColumn(OutcomesTableModel.DESC_COL);
		descriptions.setCellRenderer(new OutcomeTextRenderer());

		contents = new JScrollPane(outcomes);
		add(contents, BorderLayout.CENTER);
	}
	
	/**
	 * Create and return a list of LearningOutcomeRefs denoting selected outcomes for a
	 * task and their weights.
	 * 
	 * @return List<LearningOutcomeRef>
	 */
	public List<LearningOutcomeRef> getSelectedOutcomes() {
		List<LearningOutcomeRef> outcomeList = new ArrayList<LearningOutcomeRef>();
		for (int row = 0; row < outcomes.getRowCount(); row++) {
			String weight = outcomes.getValueAt(row, OutcomesTableModel.WEIGHT_COL).toString();
			if (weight != "None") {
				LearningOutcomeRef oc = new LearningOutcomeRef();
				oc.setGuid(guid);
				oc.setIndex(row);
				oc.setWeight(weight);
				
				outcomeList.add(oc);
			}
		}
		
		return outcomeList;
	}

	/**
	 * Set the weight of a given outcome.
	 * 
	 * @param index the row number of the outcome to be reweighted
	 * @param weight the new weight
	 */
	public void setWeight(int index, String weight) {
		outcomes.setValueAt(weight, index, OutcomesTableModel.WEIGHT_COL);
	}

	/**
	 * Weight selection requires a nonstandard editor.
	 * 
	 * Weights may be "None", "Low", "Medium", or "High".
	 * 
	 * @author ram
	 *
	 */
	private class WeightEditor extends AbstractCellEditor implements TableCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private WeightBox box;

		public WeightEditor() {
			box = new WeightBox();
		}

		@Override
		public Object getCellEditorValue() {
			return box.getSelectedItem();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			return box;
		}
	}

	/**
	 * Dropdown for selecting weights.
	 * 
	 * @author ram
	 *
	 */
	private static class WeightBox extends JComboBox {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static String[] weights = {"None", "Low", "Medium", "High"};

		public WeightBox() {
			super(weights);
			setSelectedItem(weights[0]);
		}

		
		public String toString() {
			return (String) getSelectedItem();
		}
	}

	/**
	 * Text renderer that extends JTextArea so that it can use word wrapping.
	 * 
	 * @author ram
	 *
	 */
	private class OutcomeTextRenderer extends JTextArea implements TableCellRenderer {

		private static final long serialVersionUID = 4218433225861339320L;
		private boolean isPainting = false;

		/**
		 * Default constructor: turns word wrapping on.
		 * 
		 */
		public OutcomeTextRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
		}

		@Override
		/**
		 * The renderer does three things: sets the text for the component, if any;
		 * sets the background colour to enable table striping; and sets the row height to
		 * the height of this component, since this field will be the limiting factor as far
		 * as height is concerned.
		 * 
		 * Resetting the row height can trigger rerendering, so other methods in this class
		 * are guarded not to go off if the component is currently being painted.
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object text, boolean isSelected, boolean hasFocus, int row, int col) {
			if (text instanceof String)
				setText((String)text);
			else
				setText("");
			
			if (row % 2 == 1)
				setBackground(Color.LIGHT_GRAY);
			else
				setBackground(Color.WHITE);
			
			setSize(table.getColumnModel().getColumn(OutcomesTableModel.DESC_COL).getWidth(), Short.MAX_VALUE);
			int rowHeight = getPreferredSize().height;
			if (table.getRowHeight(row) != rowHeight)
				table.setRowHeight(row, rowHeight);

			return this;
		}


		/*
		 * These methods are taken from https://community.oracle.com/thread/1362611 --
		 * they prevent rendering time blowing out due to resetting row height in the renderer.
		 */
		@Override
		protected void paintComponent(Graphics g) {
			if(isPainting) return;
			isPainting = true;
			super.paintComponent(g);
			isPainting = false;
		}

		@Override
		public void invalidate() {
			if(isPainting)
				return;
			super.invalidate();
		}

		@Override
		public void validate() {
			if(isPainting)
				return;
			super.invalidate();
		}

		@Override
		public void revalidate() {
			if(isPainting)
				return;
			super.revalidate();
		}

		@Override
		public void repaint(long tm, int x, int y, int width, int height) {
			if(isPainting)
				return;
			super.repaint(tm, x, y, width, height);
		}

	}

	/**
	 * JTable with the renderer overridden to do horizontal striping in light grey and white.
	 * @author ram
	 *
	 */
	private class OutcomesTable extends JTable {

		private static final long serialVersionUID = -6194949790411023752L;

		public OutcomesTable(OutcomesTableModel m) {
			super(m);
		}

		@Override
		public Component prepareRenderer(TableCellRenderer renderer,int row, int col) { 
			  Component comp = super.prepareRenderer(renderer, row, col); 
	
			  if (row % 2 == 0) { 
				  comp.setBackground(Color.LIGHT_GRAY); 
				  comp.setForeground(Color.BLACK);
			  } 
			  else { 
				  comp.setBackground(Color.WHITE); 
				  comp.setForeground(Color.BLACK);
			  } 
			  return comp; 
		}
	}

	public void setValue(int index, String weight) {
		outcomes.setValueAt(weight, index, OutcomesTableModel.WEIGHT_COL);	
	}
}
