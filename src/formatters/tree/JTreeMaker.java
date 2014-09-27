package formatters.tree;

import java.awt.CardLayout;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import swingui.NavDisableEventListener;
import swingui.cards.CheckboxPanel;
import swingui.cards.CriterionContainer;
import swingui.cards.QTaskPanel;
import swingui.cards.TaskPanel;
import model.Checkbox;
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
	
	private SIRNode root = null;
	private SIRTree tree;
	private JPanel panel = new JPanel(new CardLayout());
	private CriterionContainer lastcard;
	private Stack<SIRNode> path = new Stack<SIRNode>();
	private Integer tasknum = 0;
	private MarkingScheme scheme;
	private NavDisableEventListener listener;

	public JTreeMaker(NavDisableEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void doCheckbox(Checkbox checkbox) {
		SIRNode parent = path.pop();
		String idstr = (++tasknum).toString();
		parent.add(new SIRNode(idstr, parent, checkbox, checkboxIcon));
		path.push(parent);
		panel.add(new CheckboxPanel(scheme, listener, parent.getMark(), checkbox), idstr);
	}

	@Override
	public void endCheckbox(Mark checkbox) {
	}

	@Override
	public void doCriterion(Criterion criterion) {
		SIRNode parent = path.pop();
		String uuid = parent.getId();
		parent.add(new SIRNode(uuid, parent, criterion, criterionIcon));
		path.push(parent);
		lastcard.addCriterion(criterion);
	}

	@Override
	public void endCriterion(Criterion criterion) {
	}

	@Override
	public void doQTask(QTask qtask) {
		SIRNode parent = path.pop();
		String idstr = (++tasknum).toString();
		SIRNode child = new SIRNode(idstr, parent, qtask, qtaskIcon);
		parent.add(child);
		path.push(parent);
		path.push(child);
		QTaskPanel card = new QTaskPanel(scheme, listener, parent.getMark(), qtask);
		panel.add(card, idstr);
		lastcard = card;
	}

	@Override
	public void endQTask(QTask qtask) {
		path.pop();
	}

	@Override
	public void doTask(Task task) {
		SIRNode parent = path.pop();
		String idstr = (++tasknum).toString();
		SIRNode child = new SIRNode(idstr, parent, task, taskIcon);
		parent.add(child);
		path.push(parent);
		path.push(child);

		TaskPanel card = new TaskPanel(scheme, listener, parent.getMark(), task);
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
		root = new SIRNode("0", null, markingScheme.getActivityName(), schemeIcon);
		path.push(root);

		for (Mark m : markingScheme.getSubtasks()) {
			m.makeOutput(this);
		}
		endScheme(markingScheme);
	}

	@Override
	public void endScheme(MarkingScheme markingScheme) {
		if (root != null && markingScheme != null)
			tree = new SIRTree(root, markingScheme);
	}

	public SIRTree getJTree() {
		return tree;
	}

	public JPanel getCardStack() {
		return panel;
	}
}
