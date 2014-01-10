package gui.cards;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

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

	// Populate ScaleBox with all the Scales in the current model.
	public ScaleBox() {
		for (Scale s: Scale.getScales()) {
			ScaleItem item = new ScaleItem(s);
			addItem(item);
		}
		addItem("New scale...");
		
		setActionCommand("scalebox");
		addActionListener(this);
	}

	public Scale getSelectedScale() {
		Object item = getSelectedItem();
		if (item != null && item instanceof ScaleItem)
			return ((ScaleItem) getSelectedItem()).getScale();
		return null;
	}
	
	public Scale newScale() {
		Scale s = null;
		
		// Display dialog
		return s;
	}
	
	private Window findRoot() {
		Container c = getParent();
		while (c.getParent() != null)
			c = c.getParent();
		
		if (c instanceof Window)
			return (Window) c;
		return null;
		
	}
	
	public void actionPerformed(ActionEvent ev) {
		if (ev.getActionCommand().equals("scalebox")) {
			// Adding a scale?
			Object selectedItem = getSelectedItem();
			if (selectedItem instanceof String && selectedItem.equals("New scale...")) {
				NewScaleDialog dlg = new NewScaleDialog(findRoot());
				dlg.setVisible(true);
				Scale newScale = dlg.getScale();
				if (newScale != null) {
					ScaleItem item = new ScaleItem(newScale);
					removeItemAt(getItemCount() - 1);
					addItem(item);
					addItem("New scale...");
					setSelectedItem(item);
				}
			}
		}
	}
}
