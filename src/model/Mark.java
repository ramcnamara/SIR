package model;

/**
 * Base class for markable objects in SIR.  Similar to the Mark base type in the MADAM
 * XML schema.
 * 
 * @author Robyn
 *
 */
public abstract class Mark {

	protected String name;
	protected String description;
	protected String markerInstruction;
	protected String label;
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
