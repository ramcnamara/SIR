package formatters;

import gui.cards.CriterionPanel;
import gui.cards.QTaskPanel;
import gui.cards.TaskPanel;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Stack;
import java.util.UUID;

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
		public final String uuid;
		private MutableTreeNode parent;
		private ArrayList<MutableTreeNode> children;
		private Object userObject;
		
		Node(String uuid, Node parent, Object userObject) {
			this.uuid = uuid;
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
			if (parent == null) return;
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

		public String getUuid() {
			return uuid;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return userObject.toString();
		}
		
	}
	private Node root = null;
	private JPanel panel = new JPanel(new CardLayout());
	private JPanel lastcard = new JPanel();
	private Stack<Node> path = new Stack<Node>();

	@Override
	public void doCheckbox(Checkbox checkbox) {
		Node parent = path.pop();
		String uuid = UUID.randomUUID().toString();
		parent.add(new Node(uuid, parent, checkbox.getName()));
		path.push(parent);
//		panel.add(new CheckboxPanel(checkbox, uuid));
	}

	@Override
	public void endCheckbox(Checkbox checkbox) {	
	}

	@Override
	public void doCriterion(Criterion criterion) {
		Node parent = path.pop();
		String uuid = parent.getUuid();
		parent.add(new Node(uuid, parent, criterion.getName()));
		path.push(parent);
		lastcard.add(new CriterionPanel(criterion));
	}

	@Override
	public void endCriterion(Criterion criterion) {	
	}

	@Override
	public void doQTask(QTask qtask) {
		Node parent = path.pop();
		String uuid = UUID.randomUUID().toString();
		Node child = new Node(uuid, parent, qtask.getName());
		parent.add(child);
		path.push(parent);
		path.push(child);
		JPanel card = new QTaskPanel(qtask);
//		panel.add(card, uuid);
		panel.add(card, qtask.getName());
		lastcard = card;
		
		if (qtask.getCriteria() == null) return;
		for (Mark m: qtask.getCriteria()) {
			m.makeOutput(this);
		}
		
		if (qtask.getSubtasks() == null) return;
		for (Mark m: qtask.getSubtasks()) {
			m.makeOutput(this);
		}

	}

	@Override
	public void endQTask(QTask qtask) {
		path.pop();
	}

	@Override
	public void doTask(Task task) {
		Node parent = path.pop();
		String uuid = UUID.randomUUID().toString();
		Node child = new Node(uuid, parent, task.getName());
		parent.add(child);
		path.push(parent);
		path.push(child);
		
		JPanel card = new TaskPanel(task);
		lastcard = card;
//		panel.add(card, uuid);
		panel.add(card, task.getName());
		
		if (task.getCriteria() == null) return;
		for (Mark m: task.getCriteria()) {
			m.makeOutput(this);
		}
		if (task.getSubtasks() == null) return;
		for (Mark m: task.getSubtasks()) {
			m.makeOutput(this);
		}
	}

	@Override
	public void endTask(Task task) {
		path.pop();
	}

	@Override
	public void doScheme(MarkingScheme markingScheme) {
		root = new Node(UUID.randomUUID().toString(), null, markingScheme.getActivityName());
		path.push(root);
		
		for (Mark m: markingScheme.getSubtasks()) {
			m.makeOutput(this);
		}
	}

	@Override
	public void endScheme(MarkingScheme markingScheme) {
	}
	
	public JTree getJTree() {
		if (root == null) return null;
		JTree tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		return tree;
	}
	
	public JPanel getCardStack() {
		return panel;
	}

}
