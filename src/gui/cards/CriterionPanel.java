package gui.cards;



import javax.swing.JPanel;

import model.Criterion;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;

public class CriterionPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CriterionPanel() {
	}
	private JTextField criterionName;

	/**
	 * Create the panel.
	 * 
	 */
	public CriterionPanel(Criterion criterion) {

		criterionName = new JTextField(criterion.getName());
		criterionName.setColumns(10);

		JComboBox scaleBox = new ScaleBox();

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(criterionName,
								GroupLayout.PREFERRED_SIZE, 493,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(scaleBox, 0, 153, Short.MAX_VALUE)
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(3)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																criterionName,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																scaleBox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(14, Short.MAX_VALUE)));
		setLayout(groupLayout);

		// temporary, for debugging the cardlayout/jtree stuff
		// String levels = "";
		// for (String level:criterion.getScale().asArray()) {
		// levels += level + " ";
		// }
		// add(new JLabel(criterion.getName()));
		// add(new JLabel(levels));
	}
}
