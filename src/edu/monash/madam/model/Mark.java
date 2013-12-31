package edu.monash.madam.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Base class for markable objects in SIR.  Similar to the Mark base type in the MADAM
 * XML schema.
 * 
 * @author Robyn
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Mark")
@XmlSeeAlso({Task.class,
		QTask.class,
		Checkbox.class,
		ComplexTask.class,
		Criterion.class
})
public abstract class Mark {

	@XmlElement(name="Name")
	protected String name;
	
	@XmlElement(name="Description")
	protected String description;
	
	@XmlElement(name="MarkerInstruction")
	protected String markerInstruction;
	
	@XmlAttribute
	protected String label;
	
	@XmlAttribute
	protected boolean group;

	public abstract void makeOutput(OutputMaker om);
	
	public abstract float getMark();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMarkerInstruction() {
		return markerInstruction;
	}

	public void setMarkerInstruction(String markerInstruction) {
		this.markerInstruction = markerInstruction;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}
}
