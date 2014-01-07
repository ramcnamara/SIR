package gui.cards;

import javax.swing.JComboBox;

import model.CriterionReferenced;
import model.Scale;

/** 
 * A ComboBox that is populated with all Scales used in the current marking scheme.
 * Also allows user-defined Scales.
 * @author ram
 *
 */
public class ScaleBox extends JComboBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public class ScaleItem {
		private Scale scale;
		
		ScaleItem(Scale s) {
			this.scale = s;
		}
		
		public Scale getScale() {
			return scale;
		}
		
		public String toString() {
			if (scale == null || scale.asArray().length == 0)
				return "";

			return scale.toString();
		}
	}
	
	
	// Populate ScaleBox with all the Scales in the current model.
	ScaleBox(CriterionReferenced criterion) {
		for (Scale s: Scale.getScales()) {
			ScaleItem item = new ScaleItem(s);
			addItem(item);
		}
		addItem("New scale...");
		
		// Set selected item to whatever the criterion is currently using
		String s = criterion.getScale().toString();
		setSelectedItem(null);
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemAt(i).toString().equals(s))
				setSelectedIndex(i);
		}
	}
	
	public Scale getSelectedScale() {
		return ((ScaleItem) getSelectedItem()).getScale();
	}
}
