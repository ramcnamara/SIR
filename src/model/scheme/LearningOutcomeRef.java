package model.scheme;

import javax.xml.bind.annotation.XmlElement;

public class LearningOutcomeRef {
	

	private String guid;
	private int index;
	private String weight;
	
	@XmlElement(required=true, nillable=false)
	/** Accessor for guid.
	 * 
	 * This identifies the outcome set to which this outcome belongs.  Outcome sets are 
	 * stored in outcomes.sirx as zipped XML files, each named [guid].xml.
	 * 
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}
	
	/** Mutator for guid.
	 * 
	 * This field identifies the outcome set to which this outcome belongs.  Outcome sets are 
	 * stored in outcomes.sirx as zipped XML files, each named [guid].xml.
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	@XmlElement(required=true, nillable=false)
	/**Accessor for index.
	 * 
	 * This identifies the position of this outcome in the outcome set nominated by the guid field.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**Mutator for index.
	 * 
	 * This identifies the position of this outcome in the outcome set nominated by the guid field.
	 * Attempts to set index to a negative number will silently fail.
	 * 
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		if (index >= 0)
			this.index = index;
	}
	
	@XmlElement(required=true, nillable=false)
	/** Accessor for weight.
	 *  
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}
	
	/** Mutator for weight.
	 * 
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}
}
