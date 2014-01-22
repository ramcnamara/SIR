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
	
	private static final long serialVersionUID = -3497740853288213985L;
	@XmlElement(name="Scale")
	@XmlJavaTypeAdapter(ScaleAdapter.class)
	private Scale scale;
	
	
	/**
	 * Constructor.
	 * 
	 * @param description the description of this criterion
	 * @param scale the rating scale to use
	 */
	public Criterion(String description, Scale scale) {
		this.description = description;
		this.scale = scale;
	}
	
	/**
	 * Default constructor, needed for JAXB to work
	 */
	public Criterion() {
	}


	/**
	 * Copy constructor
	 * @param c the Criterion to be copied
	 */
	public Criterion(Criterion c) {
		super(c);
		scale = c.getScale();
	}

	/**
	 * Accessor for scale.
	 * 
	 * @return this criterion's rating scale
	 */
	public Scale getScale() {
		return scale;
	}

	/**
	 * Mutator for scale.
	 * 
	 * @param scale the new rating scale
	 */
	public void setScale(Scale scale) {
		this.scale = scale;
	}
	
	/**
	 * Accessor for description.
	 * 
	 * Note that descriptions for criteria are not yet supported in SIR or MADAM.
	 * 
	 * @return the description of this criterion
	 */
	public String getDescription() {
		return description;
	}
	
	/** 
	 * Mutator for description.
	 * 
	 * Note that descriptions for criteria are not yet supported in SIR or MADAM.
	 * 
	 * @param the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * Callback for output creation objects.
	 * 
	 * @param the OutputMaker that is creating the output.
	 */
	public void makeOutput(OutputMaker om) {
		om.doCriterion(this);
		om.endCriterion(this);
	}
	
	/**
	 * Mutator for marker instruction.
	 * 
	 * Note that marker instructions for criteria are not yet supported in SIR or MADAM.
	 * 
	 * @param the new marker instruction
	 */
	public void setMarkerInstruction(String markerInstruction) {
		this.markerInstruction = markerInstruction;
		
	}
	
	
	/**
	 * Accessor for marker instruction.
	 * 
	 * Note that marker instructions for criteria are not yet supported in SIR or MADAM.
	 * 
	 * @return this criterion's marker instruction.
	 */
	public String getMarkerInstruction() {
		return markerInstruction;
	}
	
	
	/**
	 * Dummy function -- criteria are not numerically marked.
	 * 
	 * @return 0
	 */
	@Override
	public float getMaxMark() {
		return 0;
	}
	
	
	/**
	 * The String equivalent of a criterion is its name. 
	 * 
	 *  If you want more sophisticated output, use an OutputMaker.
	 */
	@Override
	public String toString() {
		return getName();
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
		return new Criterion(this);
	}
}
