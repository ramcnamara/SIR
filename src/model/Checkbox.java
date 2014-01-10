package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A Checkbox is a kind of task with no granularity, i.e. the marks are either awarded or not awarded.  No
 * partial marks, subcriteria, or comments are allowed.  It is intended for applications such as late penalties.
 * 
 * @author Robyn
 *
 */
@XmlType(name="Checkbox")
public class Checkbox extends Mark {
	
	@XmlAttribute
	private float maxMark;
	
	@XmlAttribute
	private boolean bonus;

	@Override
	public void makeOutput(OutputMaker om) {
		om.doCheckbox(this);
		om.endCheckbox(this);		
	}

	public float getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(float maxMark) {
		this.maxMark = maxMark;
	}

	@Override
	public float getMark() {
		return maxMark;
	}

	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	
	public boolean getBonus() {
		return bonus;
	}

	@Override
	public String toString() {
		return getName();
	}

}
