package swingui.cards;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import formatters.tree.SIRTree;

public class SIRCriteriaTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 5322000923718135277L;
	private static DataFlavor ROW = new DataFlavor(CriterionPanel.class, "Criterion table row");
	
	@Override
	public boolean canImport(TransferHandler.TransferSupport supp) {
		// are we dropping an instance of Criterion?
		for (DataFlavor df: supp.getDataFlavors()) {
			if (df == SIRTree.CRITERION_FLAVOR)
				return true;
			if (df == ROW)
				return true;
		}
		return false;
	}
	
	@Override
	public Transferable createTransferable(JComponent theTable) {
		if (!(theTable instanceof JTable))
			return new TransferableTableRow((JTable)theTable, ((JTable)theTable).getSelectedRow());
		
		return null;
	}
	
	public boolean importData(TransferHandler.TransferSupport supp) {
		DataFlavor df = supp.getDataFlavors()[0];
		Transferable trans = supp.getTransferable();
		if (df == ROW) {
			// source was a table
			TransferableTableRow trow = (TransferableTableRow) trans;
			
		}
		return false;
	}
	
	
	private class TransferableTableRow implements Transferable {
		
		private int data;
		private JTable table;

		public TransferableTableRow(JTable theTable, int selectedRow) {
			table = theTable;
			data = selectedRow;
		}

		@Override
		public Object getTransferData(DataFlavor df)
				throws UnsupportedFlavorException, IOException {
			
			return null;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] arr = {SIRCriteriaTransferHandler.ROW};
			return arr;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor df) {
			return (df == SIRCriteriaTransferHandler.ROW);
		}
		
		public int getRow() {
			return data;
		}
		
		public JTable getTable() {
			return table;
		}
		
	}

}
