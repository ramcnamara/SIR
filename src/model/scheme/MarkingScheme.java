package model.scheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * Marking scheme.  All marking schemes have precisely one instance of this class, which
 * forms the root of the tree.
 * 
 * 
 * @author ram
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Scheme")
public class MarkingScheme extends Observable {
	
	@XmlElement(name="UnitCode", required=true)
	private String unitCode;
	
	@XmlElement(name="ActivityName", required=true)
	private String activityName;
	
	@XmlElement(name="Subtitle", required=false)
	private String subtitle;
	
	@XmlElement(name="Preamble", required=false)
	private String preamble;
	
	/*
	 * This markup tells JAXB that the "Tasks" element is a wrapper containing instances
	 * of "Task", "QTask", and "Checkbox".
	 */
	@XmlElementWrapper(name="Tasks")
	@XmlElements({@XmlElement(name = "Task", type=Task.class),
		@XmlElement(name="QTask", type=QTask.class),
		@XmlElement(name="Checkbox", type=Checkbox.class)
	})
	
	private ArrayList<Mark> tasks;

	/*
	 * This markup tells JAXB to ignore this field -- it's for SIR's use only, to store the sets of
	 * learning outcomes that this marking scheme can reference.  It does not get added to the XML output.
	 */
	@XmlTransient
//	private List<LearningOutcomes> outcomeSets;

//	private String offering;
	
	/** Unit code accessor. 
	 * @return the unit code
	 */
	public String getUnitCode() {
		return unitCode;
	}

	/** Unit code mutator.
	 * @param unitCode the unit code (if any)
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
		change();
	}


	/**
	 * Activity name accessor.
	 * @return the name of the activity
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * Activity name mutator.
	 * @param name the name of the activity
	 */
	public void setActivityName(String name) {
		this.activityName = name;
		change();
	}

	/**
	 * Accessor for preamble.
	 * 
	 * The preamble is a String which will may optionally be displayed between the title
	 * and the sections.  This may provide instructions to markers, feedback to students,
	 * or general explanations about the assignment.
	 * 
	 * @return the preamble
	 */
	public String getPreamble() {
		return preamble;
	}

	/**
	 * The preamble is a String which will may optionally be displayed between the title
	 * and the sections.  This may provide instructions to markers, feedback to students,
	 * or general explanations about the assignment.
	 * 
	 * @param preamble the preamble
	 */
	public void setPreamble(String preamble) {
		this.preamble = preamble;
		change();
	}


	/**
	 * Default constructor.  
	 * Initializes the lists of tasks and outcome sets to empty lists.
	 */
	public MarkingScheme() {
		tasks = new ArrayList<Mark>();
//		outcomeSets = new ArrayList<LearningOutcomes>();
		change();
	}
	
	/**
	 * Add a top-level Mark (i.e. a Task, QTask, or Checkbox).
	 * 
	 * @param newSection
	 */
	public void add(Mark newSection) {
		tasks.add(newSection);
		change();
	}
	
	/**
	 * Notify observers that the marking scheme has changed.
	 */
	public void refresh() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove and return the nth top-level Mark.
	 * 
	 * @param n the index of the Mark to remove
	 * @return the removed Mark
	 */
	public Mark delete(int n) {
		return tasks.remove(n);
	}
	
	
	/**
	 * Remove and return the supplied Mark, if it exists in the 
	 * MarkingScheme as a top-level Mark.  Does not traverse subtask lists.
	 * 
	 * @param m
	 * @return the removed Mark, or null if the Mark was not found.
	 */
	public Mark delete(Mark m) {
		int idx = tasks.indexOf(m);
		if (idx == -1)
			return null;
		
		return delete(tasks.indexOf(m));
	}
	

	/**
	 * Callback for output creation.
	 * 
	 * @param om the OutputMaker
	 */
	public void makeOutput(OutputMaker om) {
		om.doScheme(this);
		for (Mark m: tasks) {
			m.makeOutput(om);
		}
		om.endScheme(this);
	}
	

	/**
	 * Subtitle accessor.
	 * 
	 * @return the subtitle, which may be null
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * Subtitle mutator.
	 * @param subtitle the new subtitle
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		change();
	}

	/**
	 * Subtask list accessor.
	 * Returns an unmodifiable copy of the task list.
	 * @return
	 */
	public List<Mark> getSubtasks() {
		return Collections.unmodifiableList(tasks);
	}

	/** 
	 * Notify observers that the marking scheme has changed.
	 * 
	 */
	private void change() {
		setChanged();
		notifyObservers();
	}

	/**
	 * Remove a top-level Task, QTask, or Checkbox.
	 * 
	 * @param theMark
	 */
	public void removeTask(Mark theMark) {
		tasks.remove(theMark);		
	}

	/**
	 * Insert a Task, QTask, or Checkbox into the list of top-level tasks.
	 * 
	 * If the tasks list doesn't exist, it is created.
	 * 
	 * @param index
	 * @param incoming
	 */
	public void insertAt(int index, Mark incoming) {
		if (tasks == null)
			tasks = new ArrayList<Mark>();
		if (index < tasks.size())
			tasks.add(index, incoming);
		else
			tasks.add(incoming);
		
	}

	/**
	 * Computes and returns the total number of marks available, i.e. what the activity is out of.
	 * 
	 * Bonus and penalty tasks do not contribute towards the number of available marks.
	 * 
	 * 
	 * @return float
	 */
	public float getAvailableMarks() {
		float marks = 0.0f;
		
		for (Mark m:this.tasks)
			marks += m.getEffectiveMaxMark();
		
		return marks;
	}
	
//	/**
//	 * Add a set of learning outcomes to the set available for this marking scheme's tasks.
//	 * 
//	 * 
//	 */
//	public void addLearningOutcomes(LearningOutcomes outcomes) {
//		outcomeSets.add(outcomes);
//	}
//
//	/**
//	 * Accessor for offering field.
//	 * 
//	 * @return String the offering: unit code + " " + teaching period + ", " + year
//	 */
//	public String getOffering() {
//		// TODO Auto-generated method stub
//		return offering;
//	}
//	
//	/**
//	 * Mutator for offering field.
//	 * 
//	 * @param offering the offering: unit code + " " + teaching period + ", " + year
//	 */
//	public void setOffering(String offering) {
//		this.offering = offering;
//	}
}

