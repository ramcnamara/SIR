package formatters.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * A renderer that sets the icon associated with a tree node.
 * 
 * @author ram
 *
 */
public class SIRTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * Returns a tree cell renderer but with the icon set to the one defined in value.getIcon().
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		ImageIcon img = ((SIRNode) value).getIcon();
		setOpenIcon(img);
		setClosedIcon(img);
		setLeafIcon(img);
		return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	}
}
