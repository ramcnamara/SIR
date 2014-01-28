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
	 * Returns a version of the default tree cell renderer but with the
	 * icon set to the one defined in value.getIcon().
	 *  
	 * @param tree the JTree to be rendered
	 * @param value the value of the node
	 * @param selected true if and only if the node to be rendered is selected
	 * @param expanded true if and only if the node to be rendered is expanded
	 * @param leaf true if the node to be rendered has no children
	 * @param row the index of the row the node is in
	 * @param hasFocus true if and only if the node to be rendered has keyboard focus 
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
