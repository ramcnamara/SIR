package edu.monash.madam.formatters;

import java.util.Stack;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.monash.madam.model.Checkbox;
import edu.monash.madam.model.Criterion;
import edu.monash.madam.model.Mark;
import edu.monash.madam.model.MarkingScheme;
import edu.monash.madam.model.OutputMaker;
import edu.monash.madam.model.QTask;
import edu.monash.madam.model.Task;

public class JTreeMaker implements OutputMaker {
	private DefaultMutableTreeNode root = null;
	
	private Stack<DefaultMutableTreeNode> path = new Stack<DefaultMutableTreeNode>();

	@Override
	public void doCheckbox(Checkbox checkbox) {
		DefaultMutableTreeNode parent = path.pop();
		parent.add(new DefaultMutableTreeNode(checkbox.getName()));
		path.push(parent);
	}

	@Override
	public void endCheckbox(Checkbox checkbox) {	
	}

	@Override
	public void doCriterion(Criterion criterion) {
		DefaultMutableTreeNode parent = path.pop();
		parent.add(new DefaultMutableTreeNode(criterion.getName()));
		path.push(parent);
	}

	@Override
	public void endCriterion(Criterion criterion) {	
	}

	@Override
	public void doQTask(QTask qtask) {
		DefaultMutableTreeNode parent = path.pop();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(qtask.getName());
		parent.add(child);
		path.push(parent);
		path.push(child);
		
		if (qtask.getSubtasks() == null) return;
		for (Mark m: qtask.getSubtasks()) {
			m.makeOutput(this);
		}
		
		if (qtask.getCriteria() == null) return;
		for (Mark m: qtask.getCriteria()) {
			m.makeOutput(this);
		}
	}

	@Override
	public void endQTask(QTask qtask) {
		path.pop();
	}

	@Override
	public void doTask(Task task) {
		DefaultMutableTreeNode parent = path.pop();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(task.getName());
		parent.add(child);
		path.push(parent);
		path.push(child);
		
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
		root = new DefaultMutableTreeNode(markingScheme.getActivityName());
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
		return new JTree(root);
	}

}
