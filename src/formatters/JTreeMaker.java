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

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Checkbox;
import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.OutputMaker;
import model.QTask;
import model.Task;

public class JTreeMaker implements OutputMaker {

	public class Node implements MutableTreeNode {
		public final String id;
		private MutableTreeNode parent;
		private ArrayList<MutableTreeNode> children;
		private Object userObject;

		Node(String id, Node parent, Object userObject) {
			this.id = id;
			this.parent = parent;
			this.userObject = userObject;
			children = new ArrayList<MutableTreeNode>();
		}

		public String toString() {
			return userObject.toString();
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
	}

	private Node root = null;
	private JPanel panel = new JPanel(new CardLayout());
	private CriterionContainer lastcard;
	private Stack<Node> path = new Stack<Node>();
	private Integer tasknum = 0;

	@Override
	public void doCheckbox(Checkbox checkbox) {
		Node parent = path.pop();
		String idstr = (++tasknum).toString();
		parent.add(new Node(idstr, parent, checkbox.getName()));
		path.push(parent);
		panel.add(new CheckboxPanel(checkbox), idstr);
	}

	@Override
	public void endCheckbox(Checkbox checkbox) {
	}

	@Override
	public void doCriterion(Criterion criterion) {
		Node parent = path.pop();
		String uuid = parent.getId();
		parent.add(new Node(uuid, parent, criterion.getName()));
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
		Node child = new Node(idstr, parent, qtask.getName());
		parent.add(child);
		path.push(parent);
		path.push(child);
		QTaskPanel card = new QTaskPanel(qtask);
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
		Node child = new Node(idstr, parent, task.getName());
		parent.add(child);
		path.push(parent);
		path.push(child);

		TaskPanel card = new TaskPanel(task);
		lastcard = card;
		panel.add(card, idstr);
	}

	@Override
	public void endTask(Task task) {
		path.pop();
	}

	@Override
	public void doScheme(MarkingScheme markingScheme) {
		root = new Node("0", null, markingScheme.getActivityName());
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

		return tree;
	}

	public JPanel getCardStack() {
		return panel;
	}

}
