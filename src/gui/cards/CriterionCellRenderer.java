package gui.cards;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import model.Criterion;
import net.miginfocom.swing.MigLayout;

public class CriterionCellRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		// check that we are being asked to render something we know about
		if (!(value instanceof Criterion))
			return null;
		
		Criterion criterion = (Criterion) value;
		JPanel panel = new JPanel(new MigLayout("fillx", "[fill][]"));
		
		// add name display -- editable if we have focus
		JComponent name;
		if (cellHasFocus)
			name = new JTextArea(criterion.getName());
		else
			name = new JLabel(criterion.getName());
		panel.add(name, "cell 0 0");
		
		// add scale
		panel.add(new ScaleBox(criterion), "cell 1 0, align right");
		
		return panel;
	}

}
