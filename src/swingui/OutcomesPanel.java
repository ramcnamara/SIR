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


public class OutcomesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OutcomesTable outcomes;

	public OutcomesPanel(LearningOutcomes lo) {
		setLayout(new BorderLayout());
		
		JScrollPane contents;

		String desc = "";
		List<String> outcomeList = new ArrayList<String>();
		if (lo != null) {
			desc = lo.getDescription();
			Outcomes oc = lo.getOutcomes();
			if (oc != null)
				outcomeList = oc.getOutcome();
		}
		JLabel lblDescription = new JLabel(desc);
		add(lblDescription, BorderLayout.NORTH);


		OutcomesTableModel m = new OutcomesTableModel(outcomeList);
		outcomes = new OutcomesTable(m);
		outcomes.setFillsViewportHeight(true);
		
		
		TableColumn weights = outcomes.getColumnModel().getColumn(OutcomesTableModel.WEIGHT_COL);
		int boxWidth = new WeightBox().getPreferredSize().width;
		weights.setMaxWidth(boxWidth);
		weights.setMinWidth(boxWidth);
		weights.setCellEditor(new WeightEditor());

		TableColumn descriptions = outcomes.getColumnModel().getColumn(OutcomesTableModel.DESC_COL);
		descriptions.setCellRenderer(new OutcomeTextRenderer());

		contents = new JScrollPane(outcomes);
		add(contents, BorderLayout.CENTER);
	}

	public void setWeight(int index, String weight) {
		outcomes.setValueAt(weight, index, OutcomesTableModel.WEIGHT_COL);
	}

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

	private class OutcomeTextRenderer extends JTextArea implements TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean isPainting = false;

		public OutcomeTextRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
		}

		@Override
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

	private class OutcomesTable extends JTable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
}
