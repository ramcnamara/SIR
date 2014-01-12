package gui.cards;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
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


	/**
	 * Inner class that wraps Scale, just in case ScaleBox needs to do anything dodgy with it.
	 * @author Robyn
	 *
	 */
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
	
	
	/**
	 * Constructor that creates the ScaleBox with the given criterion's Scale selected.
	 * @param criterion the criterion whose scale should be selected
	 */
	ScaleBox(CriterionReferenced criterion) {
		this(criterion.getScale());
	}
	
	
	/**
	 * Constructor that creates the ScaleBox with the given scale selected.
	 * @param scale the scale to be selected
	 */
	public ScaleBox(Scale scale) {
		this();
		
		if (scale == null)
			return;
		for (int i=0; i<getItemCount(); i++) {
			if (scale.toString().equals(getItemAt(i)))
				setSelectedIndex(i);
		}
	}

	/**
	 * Constructor that creates a ScaleBox containing all Scales currently registered
	 * in the system, plus a "New scale" option. 
	 */
	public ScaleBox() {
		for (Scale s: Scale.getScales()) {
			ScaleItem item = new ScaleItem(s);
			addItem(item);
		}
		addItem("New scale...");
		
		setActionCommand("scalebox");
		addActionListener(this);
	}

	
	/**
	 * Returns the currently-selected scale.  If no scale is selected, returns null.
	 * @return
	 */
	public Scale getSelectedScale() {
		Object item = getSelectedItem();
		if (item != null && item instanceof ScaleItem)
			return ((ScaleItem) getSelectedItem()).getScale();
		return null;
	}
	
	
	/**
	 * Find the root of the current container.  Needed in order to pass a reference to the containing JFrame to NewScaleDialog.
	 * @return
	 */
	private Window findRoot() {
		Container c = getParent();
		while (c.getParent() != null)
			c = c.getParent();
		
		if (c instanceof Window)
			return (Window) c;
		return null;
		
	}
	
	
	/**
	 * Handle "new scale" command.
	 */
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
