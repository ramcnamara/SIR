package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A Checkbox is a kind of task with no granularity, i.e. the marks are either awarded or not awarded.  No
 * partial marks, subcriteria, or comments are allowed.  It is intended for applications such as late penalties.
 * 
 * @author Robyn
 *
 */
@XmlType(name="Checkbox")
@XmlRootElement(name="Checkbox")
public class Checkbox extends Mark {
	
	private static final long serialVersionUID = -2323175496709406435L;

	@XmlAttribute
	private float maxMark;
	
	@XmlAttribute
	private boolean bonus;
	
	/**
	 * Copy constructor.
	 * 
	 * @param old the Checkbox to copy
	 */
	public Checkbox(Checkbox old) {
		super(old);
		
		bonus = old.getBonus();
		maxMark = old.getMaxMark();
	}
	
	/**
	 * Default constructor, required by JAXB
	 */
	public Checkbox() {
	}

	@Override
	/**
	 * Callback for output creation classes.
	 * 
	 * @param om the OutputMaker that will create the output 
	 */
	public void makeOutput(OutputMaker om) {
		om.doCheckbox(this);
		om.endCheckbox(this);		
	}

	/**
	 * Accessor for max mark.
	 * 
	 * @return the current maximum mark
	 */
	public float getMaxMark() {
		return maxMark;
	}

	/**
	 * Mutator for max mark.
	 * 
	 * @param maxMark the new maximum mark
	 */
	public void setMaxMark(float maxMark) {
		this.maxMark = maxMark;
	}


	/**
	 * Mutator for bonus.
	 * If bonus is true, then this task's mark does not contribute to the total mark
	 * available.  Because the total mark available is used for computing the percentage,
	 * bonus tasks may allow students to score more than 100% on an activity.
	 * 
	 * @param bonus true if this is a bonus task, otherwise false
	 */
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	
	/**
	 * Accessor for bonus.
	 * If bonus is true, then this task's mark does not contribute to the total mark
	 * available.  Because the total mark available is used for computing the percentage,
	 * bonus tasks may allow students to score more than 100% on an activity.
	 * @return
	 */
	public boolean getBonus() {
		return bonus;
	}

	@Override
	/**
	 * The String used to identify a task is its name.  If you need more sophisticated
	 * output, write an OutputMaker.
	 * 
	 * @return the name of this task
	 */
	public String toString() {
		return "(" + getMaxMark() + "marks) " + getName();
	}

	@Override
	public void insertAt(int index, Mark childTask) throws SubtaskTypeException {
		throw new SubtaskTypeException();
	}

	/**
	 * Generates a clone of the current object by calling the copy constructor.
	 * Allows copy-constructor cloning via polymorphism.
	 */
	@Override
	public Mark getCopy() {
		return new Checkbox(this);
	}

	@Override
	public boolean isPenalty() {
		// TODO Auto-generated method stub
		return penalty;
	}
}
