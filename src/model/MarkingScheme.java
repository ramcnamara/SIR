package model;

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
	
	
	@XmlElementWrapper(name="Tasks")
	@XmlElements({@XmlElement(name = "Task", type=Task.class),
		@XmlElement(name="QTask", type=QTask.class),
		@XmlElement(name="Checkbox", type=Checkbox.class)
	})

	private ArrayList<Mark> tasks;
	/**
	 * @return the unit code
	 */
	public String getUnitCode() {
		return unitCode;
	}

	/**
	 * @param unitCode the unit code (if any)
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
		change();
	}


	/**
	 * @return the name of the activity
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param name the name of the activity
	 */
	public void setTitle(String name) {
		this.activityName = name;
		change();
	}

	/**
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


	
	public MarkingScheme() {
		tasks = new ArrayList<Mark>();
		change();
	}
	
	public void add(Mark newSection) {
		tasks.add(newSection);
		change();
	}
	
	public void refresh() {
		setChanged();
		notifyObservers();
	}
	
	public Mark delete(int n) {
		return tasks.remove(n);
	}
	
	public void makeOutput(OutputMaker om) {
		om.doScheme(this);
		for (Mark m: tasks) {
			m.makeOutput(om);
		}
		om.endScheme(this);
	}
	

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		change();
	}

	public List<Mark> getSubtasks() {
		return Collections.unmodifiableList(tasks);
	}

	public void setActivityName(String name) {
		this.activityName = name;
		change();
		
	}

	private void change() {
		setChanged();
		notifyObservers();
	}
}

