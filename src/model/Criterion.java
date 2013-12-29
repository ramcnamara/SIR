package model;

/**
 * A Criterion captures a single qualitative judgement.  It has a description and a rating scale.
 * @author Robyn
 *
 */
public class Criterion {
	private String description;
	private Scale scale;
	
	public Criterion(String description, Scale scale) {
		this.description = description;
		this.scale = scale;
	}
	public Scale getScale() {
		return scale;
	}
	public void setScale(Scale scale) {
		this.scale = scale;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void makeOutput(OutputMaker om) {
		om.doCriterion(this);
		om.endCriterion(this);
	}
}
