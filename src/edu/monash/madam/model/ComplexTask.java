package edu.monash.madam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A ComplexTask may have criteria and subtasks.
 * 
 * @author Robyn
 *
 */
public abstract class ComplexTask extends Mark {
	
	protected ArrayList<Criterion> criteria;
	protected boolean hasComment;
	
	public abstract List<Mark> getSubtasks(); 
	public abstract void addSubtask(Mark task) throws SubtaskTypeException;
	
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
	
	public void addCriterion(Criterion c) {
		if (criteria == null)
			criteria = new ArrayList<Criterion>();
		criteria.add(c);
	}
	
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
	
	
	
	// Accessors and mutators
	
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
