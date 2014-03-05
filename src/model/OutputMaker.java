package model;


/**
 * A callback object for traversing a marking scheme and producing formatted output.
 * 
 * @author ram
 *
 */
public interface OutputMaker {
	public void doCheckbox(Checkbox checkbox);
	public void endCheckbox(Mark checkbox);
	public void doCriterion(Criterion criterion);
	public void endCriterion(Criterion criterion);
	public void doQTask(QTask qtask);
	public void endQTask(QTask qtask);
	public void doTask(Task task);
	public void endTask(Task task);
	public void doScheme(MarkingScheme markingScheme);
	public void endScheme(MarkingScheme markingScheme);
}