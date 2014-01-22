package swingui.cards;

import java.awt.datatransfer.DataFlavor;

import javax.swing.TransferHandler;

import formatters.tree.SIRTree;

public class SIRCriteriaTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 5322000923718135277L;
	private static DataFlavor ROW = new DataFlavor(CriterionPanel.class, "Criterion table row");
	
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
	
	
	

}
