package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A QTask is a qualitative Task. It can have qualitatively-marked subtasks but
 * not numerically-marked subtasks. It may have a Scale for qualitative marking,
 * but this is optional. It may also have subcriteria.
 * 
 * 
 * @author Robyn
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualitativeType")
@XmlRootElement(name="QTask")
public class QTask extends ComplexTask implements CriterionReferenced {

	private static final long serialVersionUID = 8196928245062238063L;

	@XmlElementWrapper(name = "Subtasks", required = false)
	@XmlElement(name = "QTask", type = QTask.class)
	private ArrayList<QTask> subtasks = null;

	@XmlElement(name = "Scale", required = false, nillable = false)
	@XmlJavaTypeAdapter(ScaleAdapter.class)
	private Scale scale;

	@Override
	/**
	 * Add a new subtask.
	 * 
	 * @param task the subtask to add
	 * @throws SubtaskTypeException if the given task isn't also a QTask
	 */
	public void addSubtask(Mark task) throws SubtaskTypeException {
		if (subtasks == null)
			subtasks = new ArrayList<QTask>();
		// Check that prospective subtask is qualitative
		if (task instanceof QTask)
			subtasks.add((QTask) task);
		else
			throw new SubtaskTypeException();
	}

	@Override
	/**
	 * Handle output callbacks.  Do this by outputting instance-related
	 * data, then iterating through criteria and subtasks.
	 * 
	 * @param om the OutputMaker producing the formatted output
	 */
	public void makeOutput(OutputMaker om) {
		om.doQTask(this);
		if (criteria != null) {
			for (Criterion c : criteria)
				c.makeOutput(om);
		}
		if (subtasks != null)
			for (QTask qt : subtasks)
				qt.makeOutput(om);
		om.endQTask(this);

	}

	/**
	 * Default constructor, required by JAXB
	 */
	public QTask() {
		this.scale = null;
		this.subtasks = null;
		
		// default values
		this.hasComment = true;
		this.label = "Task";
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param old
	 */
	public QTask(QTask old) {
		super(old);
		
		if (old.subtasks != null)
			for (QTask q:old.subtasks)
				try {
					addSubtask(new QTask(q));
				} catch (SubtaskTypeException e) {
					// Can only happen if somebody has either been using reflection
					// to get around type restrictions, or sorcery.
					e.printStackTrace();
				}
		
		scale = old.getScale();
	}

	/**
	 * Accessor for scale.
	 * 
	 * @return this QTask's scale (if any)
	 */
	public Scale getScale() {
		return scale;
	}

	/**
	 * Mutator for scale.
	 * 
	 * @param scale
	 *            the new scale
	 */
	public void setScale(Scale scale) {
		this.scale = scale;
	}

	@Override
	/**
	 * Dummy function -- all QTasks have a maximum mark of zero.
	 * 
	 * @return 0.0f
	 */
	public float getMaxMark() {
		return 0.0f;
	}

	@Override
	/**
	 * Create and return an unmodifiable list of references to subtasks.
	 * 
	 * @return a List<Mark> of subtasks of this QTask
	 */
	public List<Mark> getSubtasks() {
		ArrayList<Mark> newList = new ArrayList<Mark>();
		for (QTask qt : subtasks)
			newList.add(qt);
		return Collections.unmodifiableList(newList);
	}

	@Override
	/**
	 * The String representation of any task is its name.  If
	 * a more detailed representation is required, use an OutputMaker.
	 */
	public String toString() {
		return getName();
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
		return subtasks.remove(idx);
	}

	@Override
	public void insertAt(int index, Mark childTask) throws SubtaskTypeException {
		if (childTask instanceof Criterion)
			insertCriterion(index, (Criterion) childTask);

		else if (childTask instanceof QTask)
			if (subtasks != null && index < subtasks.size())
				insertSubtask(index, childTask);		
			else addSubtask(childTask);
		else
			throw new SubtaskTypeException();

	}

	@Override
	public void insertSubtask(int index, Mark subtask)
			throws SubtaskTypeException {
		if (subtask instanceof QTask) {
			if (subtasks == null)
				subtasks = new ArrayList<QTask>();
			subtasks.add(index, (QTask) subtask);
		}
		else throw new SubtaskTypeException();
	}

	/**
	 * Generates a clone of the current object by calling the copy constructor.
	 * Allows copy-constructor cloning via polymorphism.
	 */
	@Override
	public Mark getCopy() {
		return new QTask(this);
	}

	@Override
	/**
	 * Remove all subtasks.
	 */
	public void clearSubtasks() {
		subtasks = new ArrayList<QTask>();
		
	}

	@Override
	public boolean isPenalty() {
		return false;
	}

	@Override
	public boolean isBonus() {
		return false;
	}
}
