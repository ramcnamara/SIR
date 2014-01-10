package gui.cards;

import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import model.CriterionReferenced;
import model.Scale;

/** 
 * A ComboBox that is populated with all Scales used in the current marking scheme.
 * Also allows user-defined Scales.
 * @author ram
 *
 */
public class ScaleBox extends JComboBox implements ActionListener {
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
		this(criterion.getScale());
	}
	
	public ScaleBox(Scale scale) {
		this();
		
		for (int i=0; i<getItemCount(); i++) {
			if (scale.toString().equals(getItemAt(i)))
				setSelectedIndex(i);
		}
	}

	public ScaleBox() {
		for (Scale s: Scale.getScales()) {
			ScaleItem item = new ScaleItem(s);
			addItem(item);
		}
		addItem("New scale...");
	}

	public Scale getSelectedScale() {
		return ((ScaleItem) getSelectedItem()).getScale();
	}
	
	public Scale newScale() {
		Scale s = null;
		
		// Display dialog
		return s;
	}
}
