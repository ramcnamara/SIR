package gui.cards;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

public class NewScaleDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the dialog.
	 */
	public NewScaleDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][]"));
		JLabel lblPrompt = new JLabel("Enter new scale:");
		contentPanel.add(lblPrompt, "cell 0 0,alignx left,aligny top");
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			textField = new JTextField();
			contentPanel.add(textField, "cell 0 1,growx");
			textField.setColumns(10);
		}
		{
			textField_1 = new JTextField();
			contentPanel.add(textField_1, "cell 0 2,growx");
			textField_1.setColumns(10);
		}
		{
			JButton btnNewButton = new JButton("Add level");
			contentPanel.add(btnNewButton, "cell 0 3,alignx right");
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
