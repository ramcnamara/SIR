package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * A ComplexTask may have criteria and subtasks.
 * Concrete subclasses of ComplexTask are Task and QTask.  Checkbox and Criterion are 
 * not ComplexTasks.
 * 
 * @author Robyn
 *
 */

//This markup tells JAXB that ComplexTask refers to Task or QTask.
@XmlType(name="ComplexTask")
@XmlSeeAlso({
	Task.class,
	QTask.class})

public abstract class ComplexTask extends Mark {
	
	/* ComplexTasks may have criteria, which in the XML representation will be wrapped in
	 * a Criteria element. 
	 */
	@XmlElementWrapper(name="Criteria")
	@XmlElement(name="Criterion")
	protected ArrayList<Criterion> criteria;
	
	@XmlAttribute
	protected boolean hasComment;
	
	public abstract List<Mark> getSubtasks(); 
	public abstract void addSubtask(Mark task) throws SubtaskTypeException;
	public abstract Mark removeSubtask(Mark task);
	public abstract Mark removeSubtask(int idx);
	
	// Criterion handling
	/**
	 * Generates and returns an unmodifiable view of the list of criteria.
	 * 
	 * @return a read-only List<Criterion> of this ComplexTask's criteria
	 */
	public List<Criterion> getCriteria() {
		if (criteria == null) return null;
		return Collections.unmodifiableList(criteria);
	}
	
	/**
	 * Add a new Criterion to this task.
	 * 
	 * @param c the Criterion to add
	 */
	public void addCriterion(Criterion c) {
		if (criteria == null)
			criteria = new ArrayList<Criterion>();
		criteria.add(c);
	}
	
	/**
	 * Add a new Criterion to this task.
	 * 
	 * @param description the name of the new criterion
	 * @param scale the scale the new criterion will use
	 */
	public void addCriterion(String description, Scale scale) {
		criteria.add(new Criterion(description, scale));
	}
	
	
	/**
	 * Move the criterion initially at startIndex by the number of positions given by offset.
	 * 
	 * @param startIndex initial position of the item to move
	 * @param offset number of positions to move
	 */
	//TODO: test this!
	public void moveCriterion(int startIndex, int offset) {
		int endPosition = 0;
		if (offset < 0) {
			startIndex = startIndex + offset;
		}
		else endPosition = startIndex + offset + 1;
		Collections.rotate(criteria.subList(startIndex, endPosition), (offset < 0? 1:-1));
	}

	
	/**
	 * Returns true if this ComplexTask should allow marker comments.
	 * 
	 * @return true if and only if this ComplexTask allows marker comments
	 */
	public boolean hasComment() {
		return hasComment;
	}
	
	/**
	 * Sets the commenting status of this ComplexTask
	 * @param hasComment true if and only if this task should allow marker comments
	 */
	public void setHasComment(boolean hasComment) {
		this.hasComment = hasComment;
	}

}
