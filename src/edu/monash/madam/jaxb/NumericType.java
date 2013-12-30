//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.12.30 at 08:09:15 PM EST 
//


package edu.monash.madam.jaxb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import edu.monash.madam.model.Criterion;
import edu.monash.madam.model.SubtaskTypeException;
import edu.monash.madam.model.Task;


/**
 * <p>Java class for NumericType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NumericType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.monash.edu/MADAM}Mark">
 *       &lt;sequence>
 *         &lt;element name="Subtasks" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded">
 *                   &lt;element name="Task" type="{http://www.monash.edu/MADAM}NumericType"/>
 *                   &lt;element name="QTask" type="{http://www.monash.edu/MADAM}QualitativeType"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Criteria" type="{http://www.monash.edu/MADAM}CriteriaType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="maxMark" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="minMark" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="hasComment" type="{http://www.monash.edu/MADAM}Boolean" />
 *       &lt;attribute name="bonus" type="{http://www.monash.edu/MADAM}Boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumericType", propOrder = {
    "subtasks",
    "criteria"
})
public class NumericType
    extends Mark
{

    @XmlElement(name = "Subtasks")
    protected NumericType.Subtasks subtasks;
    @XmlElement(name = "Criteria")
    protected CriteriaType criteria;
    @XmlAttribute(name = "maxMark")
    protected BigDecimal maxMark;
    @XmlAttribute(name = "minMark")
    protected BigDecimal minMark;
    @XmlAttribute(name = "hasComment")
    protected Boolean hasComment;
    @XmlAttribute(name = "bonus")
    protected Boolean bonus;

    /**
     * Gets the value of the subtasks property.
     * 
     * @return
     *     possible object is
     *     {@link NumericType.Subtasks }
     *     
     */
    public NumericType.Subtasks getSubtasks() {
        return subtasks;
    }

    /**
     * Sets the value of the subtasks property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumericType.Subtasks }
     *     
     */
    public void setSubtasks(NumericType.Subtasks value) {
        this.subtasks = value;
    }

    /**
     * Gets the value of the criteria property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriaType }
     *     
     */
    public CriteriaType getCriteria() {
        return criteria;
    }

    /**
     * Sets the value of the criteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriaType }
     *     
     */
    public void setCriteria(CriteriaType value) {
        this.criteria = value;
    }

    /**
     * Gets the value of the maxMark property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxMark() {
        return maxMark;
    }

    /**
     * Sets the value of the maxMark property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxMark(BigDecimal value) {
        this.maxMark = value;
    }

    /**
     * Gets the value of the minMark property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinMark() {
        return minMark;
    }

    /**
     * Sets the value of the minMark property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinMark(BigDecimal value) {
        this.minMark = value;
    }

    /**
     * Gets the value of the hasComment property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getHasComment() {
        return hasComment;
    }

    /**
     * Sets the value of the hasComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasComment(Boolean value) {
        this.hasComment = value;
    }

    /**
     * Gets the value of the bonus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getBonus() {
        return bonus;
    }

    /**
     * Sets the value of the bonus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBonus(Boolean value) {
        this.bonus = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded">
     *         &lt;element name="Task" type="{http://www.monash.edu/MADAM}NumericType"/>
     *         &lt;element name="QTask" type="{http://www.monash.edu/MADAM}QualitativeType"/>
     *         &lt;element name="Checkbox" type="{http://www.monash.edu/MADAM}CheckboxType"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "taskOrQTask"
    })
    public static class Subtasks implements Iterable<Mark> {

        @XmlElements({
            @XmlElement(name = "Task", type = NumericType.class),
            @XmlElement(name = "QTask", type = QualitativeType.class),
            @XmlElement(name="Checkbox", type=CheckboxType.class)
        })
        protected List<Mark> taskOrQTask;

        /**
         * Gets the value of the taskOrQTask property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the taskOrQTask property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTaskOrQTask().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NumericType }
         * {@link QualitativeType }
         * 
         * 
         */
        public List<Mark> getTaskOrQTask() {
            if (taskOrQTask == null) {
                taskOrQTask = new ArrayList<Mark>();
            }
            return this.taskOrQTask;
        }

        public class MarkIterator implements Iterator<Mark> {
        	Iterator<Mark> it = taskOrQTask.iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Mark next() {
				return it.next();
			}

			@Override
			public void remove() {
				it.remove();
			}
        	
        }

		@Override
		public Iterator<Mark> iterator() {
			return new MarkIterator();
		}


    }
    
    /**
     * Returns the MADAM Task equivalent of this NumericType.
     * @throws SubtaskTypeException 
     * 
     */
    public edu.monash.madam.model.Mark toMark() throws SubtaskTypeException {
    	Task t = new Task();    		
    	t.setBonus(bonus == null? false : bonus.asBoolean());
    	t.setDescription(description);
    	t.setGroup(group == null? false : group.asBoolean());
    	t.setHasComment(hasComment == null? false : hasComment.asBoolean());
    	t.setLabel(label);
    	t.setMarkerInstruction(markerInstruction);
    	t.setMaxMark(maxMark == null? 0 : maxMark.floatValue());
    	t.setName(name);
    	
    	// sort out subtasks
    	if (subtasks != null)
    		for (Mark m: subtasks.getTaskOrQTask()) {
    			t.addSubtask(m.toMark());
    		}
    	
    	// sort out criteria
    	if (criteria != null)
    		for (CriterionType ct: getCriteria().criterion) {
    			Criterion c = new Criterion(ct.getName(), ct.getScale().toScale());
    			c.setMarkerInstruction(ct.getMarkerInstruction());   		
    			t.addCriterion(c);
    		}
    	
    	return t;
    }

}
