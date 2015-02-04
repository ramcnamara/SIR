package formatters.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import model.scheme.ComplexTask;
import model.scheme.Mark;

public class SIRNode implements MutableTreeNode {
	
	public final String id;
	private ImageIcon icon;
	private MutableTreeNode parent;
	private ArrayList<MutableTreeNode> children;
	private Object userObject;

	SIRNode(String id, SIRNode parent, Object userObject, ImageIcon icon) {
		this.id = id;
		this.parent = parent;
		this.userObject = userObject;
		children = new ArrayList<MutableTreeNode>();
		this.icon = icon;
	}
	
	public ComplexTask getParentTask() {
		if (this.parent != null && ((SIRNode)this.parent).userObject instanceof ComplexTask)
			return (ComplexTask)((SIRNode)this.parent).userObject;
		
		return null;
	}
	

	public String toString() {
		if (userObject == null)
			return "";
		return userObject.toString();
	}
	
	public ImageIcon getIcon() {
		return icon;
	}

	@Override
	public Enumeration<MutableTreeNode> children() {
		return Collections.enumeration(children);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int idx) {
		return children.get(idx);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return (children == null || children.size() == 0);
	}

	@Override
	public void insert(MutableTreeNode node, int idx) {
		children.add(idx, node);
	}

	public void add(MutableTreeNode node) {
		children.add(node);
	}

	@Override
	public void remove(int idx) {
		children.remove(idx);

	}

	@Override
	public void remove(MutableTreeNode node) {
		children.remove(node);

	}

	@Override
	public void removeFromParent() {
		if (parent == null)
			return;
		parent.remove(this);
		this.parent = null;
	}

	@Override
	public void setParent(MutableTreeNode parent) {
		this.parent = parent;

	}

	@Override
	public void setUserObject(Object o) {
		userObject = o;
	}

	public String getId() {
		return id;
	}

	public Mark getMark() {
		if (userObject != null && userObject instanceof Mark)
			return (Mark)userObject;
		return null;
	}
}