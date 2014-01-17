package formatters;

import gui.cards.CheckboxPanel;
import gui.cards.CriterionContainer;
import gui.cards.QTaskPanel;
import gui.cards.TaskPanel;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Checkbox;
import model.ComplexTask;
import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.OutputMaker;
import model.QTask;
import model.Task;

public class JTreeMaker implements OutputMaker {
	public static final ImageIcon taskIcon = new ImageIcon(JTreeMaker.class.getResource("/resources/diamond.png"));
	public static final ImageIcon qtaskIcon = new ImageIcon(JTreeMaker.class.getResource("/resources/square.png"));
	public static final ImageIcon checkboxIcon = new ImageIcon(JTreeMaker.class.getResource("/resources/star.png"));
	public static final ImageIcon criterionIcon = new ImageIcon(JTreeMaker.class.getResource("/resources/circle.png"));
	public static final ImageIcon schemeIcon = new ImageIcon(JTreeMaker.class.getResource("/resources/diamond.png"));
	
	public class Node implements MutableTreeNode {
		
		public final String id;
		private ImageIcon icon;
		private MutableTreeNode parent;
		private ArrayList<MutableTreeNode> children;
		private Object userObject;

		Node(String id, Node parent, Object userObject, ImageIcon icon) {
			this.id = id;
			this.parent = parent;
			this.userObject = userObject;
			children = new ArrayList<MutableTreeNode>();
			this.icon = icon;
		}
		
		public ComplexTask getParentTask() {
			if (this.parent != null && ((Node)this.parent).userObject instanceof ComplexTask)
				return (ComplexTask)((Node)this.parent).userObject;
			
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

	private Node root = null;
	private JPanel panel = new JPanel(new CardLayout());
	private CriterionContainer lastcard;
	private Stack<Node> path = new Stack<Node>();
	private Integer tasknum = 0;
	private MarkingScheme scheme;

	@Override
	public void doCheckbox(Checkbox checkbox) {
		Node parent = path.pop();
		String idstr = (++tasknum).toString();
		parent.add(new Node(idstr, parent, checkbox, checkboxIcon));
		path.push(parent);
		panel.add(new CheckboxPanel(checkbox, parent.getMark()), idstr);
	}

	@Override
	public void endCheckbox(Checkbox checkbox) {
	}

	@Override
	public void doCriterion(Criterion criterion) {
		Node parent = path.pop();
		String uuid = parent.getId();
		parent.add(new Node(uuid, parent, criterion, criterionIcon));
		path.push(parent);
		lastcard.addCriterion(criterion);
	}

	@Override
	public void endCriterion(Criterion criterion) {
	}

	@Override
	public void doQTask(QTask qtask) {
		Node parent = path.pop();
		String idstr = (++tasknum).toString();
		Node child = new Node(idstr, parent, qtask, qtaskIcon);
		parent.add(child);
		path.push(parent);
		path.push(child);
		QTaskPanel card = new QTaskPanel(qtask, parent.getMark(), scheme);
		panel.add(card, idstr);
		lastcard = card;
	}

	@Override
	public void endQTask(QTask qtask) {
		path.pop();
	}

	@Override
	public void doTask(Task task) {
		Node parent = path.pop();
		String idstr = (++tasknum).toString();
		Node child = new Node(idstr, parent, task, taskIcon);
		parent.add(child);
		path.push(parent);
		path.push(child);

		TaskPanel card = new TaskPanel(task, parent.getMark(), scheme);
		lastcard = card;
		panel.add(card, idstr);
	}

	@Override
	public void endTask(Task task) {
		path.pop();
	}

	@Override
	public void doScheme(MarkingScheme markingScheme) {
		this.scheme = markingScheme;
		root = new Node("0", null, markingScheme.getActivityName(), schemeIcon);
		path.push(root);

		for (Mark m : markingScheme.getSubtasks()) {
			m.makeOutput(this);
		}
	}

	@Override
	public void endScheme(MarkingScheme markingScheme) {
	}

	public JTree getJTree() {
		if (root == null)
			return null;
		JTree tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(new SIRTreeCellRenderer());

		return tree;
	}

	public JPanel getCardStack() {
		return panel;
	}
}
