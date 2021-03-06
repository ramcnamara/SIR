package model.scheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumericType")
@XmlRootElement(name = "Task")
/**
 * A Task is a numerically-marked component of a marking scheme.  It may either be marked "by hand"
 * or have a mark that is the sum of that of its subtasks.
 * 
 * If a Task has numerically-marked subtasks (i.e. Tasks or Checkboxes), then its "max mark" field is ignored.
 * It is preserved, though, in case a later edit removes the subtasks.
 * 
 * @author Robyn
 *
 */
public class Task extends ComplexTask {

	private static final long serialVersionUID = -3343401615567901898L;

	@XmlElementWrapper(name = "Subtasks", required = false, nillable = false)
	@XmlElements({ @XmlElement(name = "Task", type = Task.class),
			@XmlElement(name = "QTask", type = QTask.class),
			@XmlElement(name = "Checkbox", type = Checkbox.class) })
	private ArrayList<Mark> subtasks = null;

	@XmlAttribute
	/**
	 * Maximum mark that a marker may assign to this Task, if the Task is marked manually.
	 * If the task mark is computed by adding up marks for subtask, then this field
	 * is ignored in MADAM.
	 */
	private float maxMark;

	@XmlAttribute
	/**
	 * Minimum mark that a marker may assign to this Task, if the Task is marked manually.
	 * This field is currently ignored in MADAM but may be used in future versions. 
	 */
	private float minMark;

	@XmlAttribute
	/**
	 * True if and only if this Task is a bonus Task.  A bonus Task contributes to a student's earned
	 * mark for the activity, if they achieve marks for it, but it does not count towards the computed
	 * maximum mark for the activity.  Hence, a student can get a mark of greater than 100% for an
	 * activity that has bonus Tasks. 
	 */
	private boolean bonus;
	
	@XmlAttribute
	/**
	 * True if and only if this Task is a penalty Task.  Penalty marks are subtracted from a student's
	 * total mark rather than added, and penalty Tasks do not not count towards the computed maximum mark
	 * for the activity.
	 */
	private boolean penalty;

	/**
	 * Copy constructor, required by JAXB
	 * 
	 * @param old
	 *            the Task to be copied
	 */
	public Task(Task old) {
		super(old);

		bonus = old.isBonus();
		maxMark = old.getMaxMark();

		if (old.subtasks != null)
			for (Mark m : old.subtasks)
				try {
					addSubtask(m.getCopy());
				} catch (SubtaskTypeException e) {
					// Can't happen, you can add anything to a Task
					e.printStackTrace();
				}
	}

	@Override
	/**
	 * Creates and returns an unmodifiable copy of this Task's subtasks.
	 * 
	 * @return an unmodifiable copy of this Task's subtasks
	 * 
	 */
	public List<Mark> getSubtasks() {
		if (subtasks == null)
			return null;
		return Collections.unmodifiableList(subtasks);
	}

	/**
	 * Default constructor, required by JAXB.
	 */
	public Task() {
	}

	@Override
	/**
	 * Add a new subtask to this Task.
	 * 
	 * @param task the subtask to add
	 */
	public void addSubtask(Mark task) throws SubtaskTypeException {
		if (subtasks == null)
			subtasks = new ArrayList<Mark>();
		subtasks.add(task);

	}

	@Override
	/**
	 * Handle output formatter callback by iterating through criteria and subtasks.
	 */
	public void makeOutput(OutputMaker om) {
		om.doTask(this);
		if (criteria != null)
			for (Criterion c : criteria)
				c.makeOutput(om);
		if (subtasks != null)
			for (Mark m : subtasks)
				m.makeOutput(om);
		om.endTask(this);

	}

	/**
	 * Mutator for maximum mark.
	 * 
	 * @param maxMark
	 *            the new maximum mark
	 */
	public void setMaxMark(float maxMark) {
		this.maxMark = maxMark;
	}

	/**
	 * Accessor for bonus status.
	 * 
	 * Bonus tasks attract marks but do not contribute to the computation of
	 * maximum total mark. This allows students to get more than 100% on a task
	 * or activity.
	 * 
	 * @return true if and only if this is a bonus task.
	 */
	public boolean isBonus() {
		return bonus;
	}

	/**
	 * Mutator for bonus status.
	 * 
	 * Bonus tasks attract marks but do not contribute to the computation of
	 * maximum total mark. This allows students to get more than 100% on a task
	 * or activity.
	 * 
	 * param true if this is a bonus task.
	 */
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}

	@Override
	/**
	 * Compute the mark for this task.  This is done by iterating
	 * through subtasks, if any, and adding up any marks that they
	 * are worth.  If they are worth marks, the maximum mark for this Task
	 * is the total accruable from its subtasks.
	 * 
	 *@return the total mark for this task, including its subtasks
	 */
	public float getMaxMark() {
		float myMark = 0.0f;

		if (subtasks != null)
			for (Mark m : subtasks) {
				// Bonus subtasks don't count toward the total unless this is a bonus task
				// Likewise for penalty subtasks
				// I wonder if this would be more readable if I applied DeMorgan's Law?
				if (!((!isBonus() && m.isBonus()) || (!isPenalty() && m.isPenalty())))
					myMark += m.getMaxMark();
			}
		return (myMark > 0 ? myMark : this.maxMark);
	}

	@Override
	/**
	 * Returns a string representation of the current Task, suitable for display.
	 */
	public String toString() {
		return "(" + (penalty? "Penalty: " : "") + getMaxMark() + (bonus? " bonus": "") + " marks) " + getName();
	}

	@Override
	/**
	 * Remove and return the given subtask.
	 * 
	 * If the subtask is not found, returns null.  Does not
	 * recurse through the subtasks.
	 * 
	 * @param task the subtask to remove
	 */
	public Mark removeSubtask(Mark task) {
		int idx = subtasks.indexOf(task);
		if (idx == -1)
			return null;
		return removeSubtask(idx);
	}

	/**
	 * Remove and return the idx'th task in the subtask list. Returns null if
	 * given an invalid index.
	 * 
	 * @param idx
	 *            the index of the subtask to remove
	 */
	@Override
	public Mark removeSubtask(int idx) {
		if (idx < 0 || subtasks == null || idx >= subtasks.size())
			return null;
		return subtasks.remove(idx);
	}

	/**
	 * Inserts a subtask into the list.  This is a thin wrapper for ArrayList.add().
	 */
	@Override
	public void insertSubtask(int index, Mark subtask) {
		if (subtasks == null)
			subtasks = new ArrayList<Mark>();
		subtasks.add(index, subtask);
	}


	/**
	 * Inserts a subtask or criterion into the relevant ArrayList.
	 */
	@Override
	public void insertAt(int index, Mark childTask) throws SubtaskTypeException {
		if (childTask instanceof Criterion)
			insertCriterion(index, (Criterion) childTask);
		else {
			if (subtasks == null)
				subtasks = new ArrayList<Mark>();

			if (index < subtasks.size())
				insertSubtask(index, childTask);
			else
				addSubtask(childTask);
		}
	}

	@Override
	/**
	 * Generates a clone of the current object by calling the copy constructor.
	 * Allows copy-constructor cloning via polymorphism.
	 */
	public Mark getCopy() {
		return new Task(this);
	}

	@Override
	/**
	 * Remove all subtasks.
	 */
	public void clearSubtasks() {
		subtasks = new ArrayList<Mark>();

	}

	/**
	 * Accessor for penalty field.
	 */
	@Override
	public boolean isPenalty() {
		return penalty;
	}

	/**
	 * Mutator for penalty field.
	 * @param penalty
	 */
	public void setPenalty(boolean penalty) {
		this.penalty = penalty;
	}
}
