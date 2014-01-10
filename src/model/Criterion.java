package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A Criterion captures a single qualitative judgement.  It has a description and a rating scale.
 * @author Robyn
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Criterion")
public class Criterion extends Mark implements CriterionReferenced {
	
	@XmlElement(name="Scale")
	@XmlJavaTypeAdapter(ScaleAdapter.class)
	private Scale scale;
	
	public Criterion(String description, Scale scale) {
		this.description = description;
		this.scale = scale;
	}
	public Criterion() {
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
	public void setMarkerInstruction(String markerInstruction) {
		this.markerInstruction = markerInstruction;
		
	}
	
	public String getMarkerInstruction() {
		return markerInstruction;
	}
	@Override
	public float getMark() {
		return 0;
	}
	@Override
	public String toString() {
		return getName();
	}
}
