package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A QTask is a qualitative Task.  It can have qualitatively-marked subtasks but not 
 * numerically-marked subtasks.  It may have a Scale for qualitative marking, but this is
 * optional.  It may also have subcriteria.
 * 
 * 
 * @author Robyn
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualitativeType")
public class QTask extends ComplexTask implements CriterionReferenced {
	@XmlElementWrapper(name="Subtasks", required=false)
	@XmlElement(name="QTask", type=QTask.class)
	private ArrayList<QTask> subtasks;
	
	@XmlElement(name="Scale", required=false, nillable=false)
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
			subtasks.add((QTask)task);
		else throw new SubtaskTypeException();
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
			for (Criterion c: criteria)
				c.makeOutput(om);
		}
		if (subtasks != null)
			for (QTask qt: subtasks)
				qt.makeOutput(om);
		om.endQTask(this);
		
	}
	
	/**
	 * Default constructor, required by JAXB
	 */
	public QTask() {
		this.scale = null;
		this.subtasks = null;
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
	 * @param scale the new scale
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
		for (QTask qt: subtasks)
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
	 * Remove and return the idx'th task in the subtask list.
	 * Returns null if given an invalid index.
	 * 
	 * @param idx the index of the subtask to remove
	 */
	@Override
	public Mark removeSubtask(int idx) {
		return subtasks.remove(idx);
	}

}
