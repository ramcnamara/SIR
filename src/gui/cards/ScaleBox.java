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
		
		public String toString() {
			if (scale == null || scale.asArray().length == 0)
				return "";
			
			String rep = "";
			for (String s: scale.asArray()) {
				if (rep.length() > 0)
					rep +=" / ";
				rep += s;
			}
			return rep;
		}
	}
	
	
	// Populate ScaleBox with all the Scales in the current model.
	ScaleBox(CriterionReferenced criterion) {
		for (Scale s: Scale.getScales()) {
			ScaleItem item = new ScaleItem(s);
			addItem(item);
		}
		addItem("New scale...");
		setSelectedItem(new ScaleItem(criterion.getScale()));
	}
}
