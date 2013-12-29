package formatters;

import java.util.List;
import java.util.Stack;

import model.Checkbox;
import model.ComplexTask;
import model.Criterion;
import model.Mark;
import model.MarkingScheme;
import model.OutputMaker;
import model.QTask;
import model.Scale;
import model.Task;

public class ConsoleMaker implements OutputMaker {
	private Stack<Integer> sectionNumber;
	
	
	public ConsoleMaker() {
		sectionNumber = new Stack<Integer>();
		sectionNumber.push(0);
	}
	
	
	/**
	 * If the constructor is given a MarkingScheme as an argument, it will
	 * display it to console.
	 * 
	 * @param scheme
	 */
	public ConsoleMaker(MarkingScheme scheme) {
		sectionNumber = new Stack<Integer>();
		sectionNumber.push(0);
		this.doScheme(scheme);
	}
	
	
	private void nextSection() {
		sectionNumber.push(sectionNumber.pop() + 1);
	}
	
	private void newSection() {
		sectionNumber.push(0);
	}

	@Override
	public void doCheckbox(Checkbox checkbox) {
		doMark(checkbox);		
	}


	private void doMark(Mark m) {
		nextSection();
		String label = m.getLabel();
		if (label == null) label = "Task";
		System.out.println(label + " " + sectString() + ": " + m.getName());
		if (m.isGroup()) System.out.println("[GROUP]");
		String desc = m.getDescription();
		String mi = m.getMarkerInstruction();
		if (desc != null)
			System.out.println(desc + "\n");
		if (mi != null)
			System.out.println(mi + "\n");
		

	}
	
	private void doComplexTask(ComplexTask ct) {
		doMark(ct);
		
		List<Criterion> cl = ct.getCriteria();
		if (cl != null && cl.size() > 0)
			for (Criterion c: cl) {
				c.makeOutput(this);
			}
		
		List<Mark> ml = ct.getSubtasks();
		if (ml != null && ml.size() > 0) {
			newSection();
			for (Mark m: ml)
				m.makeOutput(this);
			sectionNumber.pop();
			
		}
	}
	
	private void endComplexTask(ComplexTask ct) {
		if (ct.hasComment())
			System.out.println("Comment: \n\n\n");
		System.out.println();
	}

	@Override
	public void endCheckbox(Checkbox checkbox) {
		System.out.println("[ ] (" + checkbox.getMaxMark() + " marks)\n");

	}

	@Override
	public void doCriterion(Criterion criterion) {
		System.out.println(criterion.getDescription());
		String[] levels = criterion.getScale().asArray();
		for (String level: levels)
			System.out.println("\t[ ] " + level);
	}

	@Override
	public void endCriterion(Criterion criterion) {
		System.out.println();

	}

	@Override
	public void doQTask(QTask qtask) {
		doComplexTask(qtask);
		Scale s = qtask.getScale();
		if (s != null) {
			String[] levels = s.asArray();
			System.out.println("Mark: ");
			for (String level: levels)
				System.out.println("\t[ ] " + level);
		}
	}

	@Override
	public void endQTask(QTask qtask) {
		endComplexTask(qtask);
	}

	@Override
	public void doTask(Task task) {
		doComplexTask(task);
		System.out.println("\nMark: ___/" + task.getMaxMark());
		if (task.isBonus())
			System.out.println("[Bonus allowed] ");
	}

	@Override
	public void endTask(Task task) {
		endComplexTask(task);
	}

	@Override
	public void doScheme(MarkingScheme markingScheme) {
		System.out.println(markingScheme.getUnitCode());
		System.out.println(markingScheme.getActivityName());
		System.out.println(markingScheme.getSubtitle());
		System.out.println(markingScheme.getPreamble() + "\n");
		
		for (Mark m: markingScheme.getSubtasks())
			m.makeOutput(this);

	}

	@Override
	public void endScheme(MarkingScheme markingScheme) {
		// TODO Auto-generated method stub

	}
	
	private String sectString() {
		String s = "";
		for (Integer i:sectionNumber) {
			if (s != "") s += ".";
			s += i;
		}
		return s;
	}

}
