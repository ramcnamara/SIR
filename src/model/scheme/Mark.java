package model.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;

import model.outcomes.LearningOutcomes;

/**
 * Base class for markable objects in SIR.  Similar to the Mark base type in the MADAM
 * XML schema.
 * 
 * @author Robyn
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)

// List of subtypes of Mark for the benefit of JAXB
@XmlType(name="Mark")
@XmlSeeAlso({Task.class,
		QTask.class,
		Checkbox.class,
		ComplexTask.class,
		Criterion.class
})
public abstract class Mark implements Serializable {

	protected Mark() {
		outcomes = new ArrayList<LearningOutcomeRef>();
	}
	
	private static final long serialVersionUID = 2644920807147460137L;

	@XmlElement(name="Name", required=true, nillable=false)
	protected String name;
	
	@XmlElement(name="Description", required=false, nillable=false)
	protected String description;
	
	@XmlElement(name="MarkerInstruction", required=false, nillable=false)
	protected String markerInstruction;
	
	@XmlAttribute
	protected String label;
	
	@XmlAttribute
	protected boolean group;
	
	@XmlElementWrapper(name="LearningOutcomes")
	@XmlElement(name="Outcome", required=true, nillable=true)
	protected List<LearningOutcomeRef> outcomes;

	public Mark(Mark old) {
		description = old.getDescription();
		group = old.isGroup();
		label = old.getLabel();
		markerInstruction = old.getMarkerInstruction();
		name = old.getName();
		outcomes = new ArrayList<LearningOutcomeRef>();
		for (LearningOutcomeRef lo : old.getOutcomes()) {
			outcomes.add(lo);
		}
	}


	private List<LearningOutcomeRef> getOutcomes() {
		return outcomes;
	}

	// Subclasses of Mark need to be able to tell an OutputMaker how to deal with their structure.
	public abstract void makeOutput(OutputMaker om);
	
	public abstract float getMaxMark();
	
	
	/**
	 * Accessor for name.
	 * 
	 * The name of a mark is a short string, suitable for formatting as a heading.
	 * Longer explanations of the meaning of this Mark should go in the description 
	 * field if they are to be visible to students, or in the marker instruction field
	 * if they are to be visible only to markers.
	 * 
	 * @return the name of this Mark
	 */
	public String getName() {
		if (name != null && name.length() == 0)
			return null;
		return name;
	}

	
	/**
	 * Mutator for name.
	 * 
	 * The name of a mark is a short string, suitable for formatting as a heading.
	 * Longer explanations of the meaning of this Mark should go in the description 
	 * field if they are to be visible to students, or in the marker instruction field
	 * if they are to be visible only to markers.
	 * 
	 * @param name the new name for this Mark
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Accessor for description.
	 * 
	 * The name of a mark is a short string, suitable for formatting as a heading.
	 * Longer explanations of the meaning of this Mark should go in the description 
	 * field if they are to be visible to students, or in the marker instruction field
	 * if they are to be visible only to markers.
	 * 
	 * @return the description of this mark
	 */
	public String getDescription() {
		if (description != null && description.length() == 0)
			return null;
		return description;
	}

	
	/**
	 * Mutator for description.
	 * 
	 * The name of a mark is a short string, suitable for formatting as a heading.
	 * Longer explanations of the meaning of this Mark should go in the description 
	 * field if they are to be visible to students, or in the marker instruction field
	 * if they are to be visible only to markers.
	 *
	 * @parameter description the new description of this Mark
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Accessor for marker instruction.
	 * 
	 * A marker instruction is visible to markers when they are marking, but is not 
	 * visible to students.
	 * 
	 * @return this Mark's marker instruction
	 */
	public String getMarkerInstruction() {
		if (markerInstruction != null && markerInstruction.length() == 0)
			return null;
		return markerInstruction;
	}

	/** 
	 * Mutator for marker instruction.
	 * 
	 * A marker instruction is visible to markers when they are marking, but is not 
	 * visible to students.
	 * 
	 * @param markerInstruction
	 */
	public void setMarkerInstruction(String markerInstruction) {
		this.markerInstruction = markerInstruction;
	}

	
	/**
	 * Accessor for label.
	 * 
	 * The label of a task is the string that will be displayed next to its number,
	 * e.g. Exercise, Task, Question, etc.
	 * 
	 * @return the label of this Mark
	 */
	public String getLabel() {
		if (label != null && label.length() == 0)
			return null;
		return label;
	}

	/**
	 * Mutator for label.
	 * 
	 * The label of a task is the string that will be displayed next to its number,
	 * e.g. Exercise, Task, Question, etc.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Accessor for group.
	 * 
	 * Marks for group tasks are saved against every member of the group.
	 * 
	 * @return true if and only if this Mark is a group task
	 */
	public boolean isGroup() {
		return group;
	}

	/**
	 * Mutator for group.
	 * 
	 * Marks for group tasks are saved against every member of the group.
	 * 
	 * @param group true if and only if this Mark is a group task
	 */
	public void setGroup(boolean group) {
		this.group = group;
	}
	
	public abstract String toString();

	public abstract void insertAt(int index, Mark childTask) throws SubtaskTypeException;

	public abstract Mark getCopy();
	
	public float getEffectiveMaxMark() {
		if (isPenalty() || isBonus())
			return 0;
		
		return this.getMaxMark();
	}

	public abstract boolean isPenalty();

	public abstract boolean isBonus();

}
