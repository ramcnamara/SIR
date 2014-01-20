package formatters.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SIRTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel();

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        ImageIcon img = ((SIRNode) value).getIcon();
        
        label.setOpaque(true);

        if (img == null)
        	return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        
        // Handle selection -- code taken with slight modification from DefaultTreeCellRenderer source
        if (selected)
        {
          label.setBackground(getBackgroundSelectionColor());
          label.setForeground(getTextSelectionColor());
        }
      else
        {
          label.setBackground(getBackgroundNonSelectionColor());
          label.setForeground(getTextNonSelectionColor());
        }
        
        label.setIcon(img);
        label.setText(((SIRNode) value).toString());

        return label;
    }

}
