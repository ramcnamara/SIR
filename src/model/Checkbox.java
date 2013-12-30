package model;

/**
 * A Checkbox is a kind of task with no granularity, i.e. the marks are either awarded or not awarded.  No
 * partial marks, subcriteria, or comments are allowed.  It is intended for applications such as late penalties.
 * 
 * @author Robyn
 *
 */
public class Checkbox extends Mark {
	
	private float maxMark;

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

}
