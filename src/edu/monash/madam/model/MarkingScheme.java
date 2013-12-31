package edu.monash.madam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import edu.monash.madam.jaxb.XmlFileIO;

/**
 * Marking scheme.  All marking schemes have precisely one instance of this class, which
 * forms the root of the tree.
 * 
 * 
 * @author ram
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Scheme")
public class MarkingScheme extends Observable {
	
	@XmlElement(name="UnitCode", required=true)
	private String unitCode;
	
	@XmlElement(name="ActivityName", required=true)
	private String activityName;
	
	@XmlElement(name="Subtitle", required=false)
	private String subtitle;
	
	@XmlElement(name="Preamble", required=false)
	private String preamble;
	
	
	@XmlElementWrapper(name="Tasks")
	@XmlElements({@XmlElement(name = "Task", type=Task.class),
		@XmlElement(name="QTask", type=QTask.class),
		@XmlElement(name="Checkbox", type=Checkbox.class)
	})

	private ArrayList<Mark> tasks;
	/**
	 * @return the unit code
	 */
	public String getUnitCode() {
		return unitCode;
	}

	/**
	 * @param unitCode the unit code (if any)
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
		setChanged();
	}


	/**
	 * @return the name of the activity
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param name the name of the activity
	 */
	public void setTitle(String name) {
		this.activityName = name;
		setChanged();
	}

	/**
	 * @return the preamble
	 */
	public String getPreamble() {
		return preamble;
	}

	/**
	 * The preamble is a String which will may optionally be displayed between the title
	 * and the sections.  This may provide instructions to markers, feedback to students,
	 * or general explanations about the assignment.
	 * 
	 * @param preamble the preamble
	 */
	public void setPreamble(String preamble) {
		this.preamble = preamble;
		setChanged();
	}

	
	public MarkingScheme() {
		tasks = new ArrayList<Mark>();
		setChanged();
	}
	
	public void add(Mark newSection) {
		tasks.add(newSection);
		setChanged();
	}
	
	public Mark delete(int n) {
		return tasks.remove(n);
	}
	
	public void makeOutput(OutputMaker om) {
		om.doScheme(this);
		for (Mark m: tasks) {
			m.makeOutput(om);
		}
		om.endScheme(this);
	}
	
	
	// test
	public static void main(String[] args) throws JAXBException, SubtaskTypeException {
//		MarkingScheme it = new MarkingScheme();
//		
//		it.setUnitCode("BSK2345 Intermediate Basket Weaving");
//		it.setTitle("Assignment 2: Applied basketry");
//		it.setPreamble("This is the preamble");
//		it.setSubtitle("This is the subtitle");
//		
//		Task s1 = new Task();
//		s1.setDescription("Appearance");
//		it.add(s1);
//		
//		String[] levels = {"poor", "good", "okay"};
//		Scale threeLevels = Scale.makeScheme(levels);
//		
//		String[] levels2 = {"N/A", "poor", "good", "okay"};
//		Scale fourLevels = Scale.makeScheme(levels2);
//		
//		Task n1 = new Task();
//		n1.setMaxMark(3);
//		n1.setName("Well-chosen colours");
//		QTask q1 = new QTask();
//		q1.setName("Colours not too bright");
//		q1.setScale(threeLevels);
//		try {
//			n1.addSubtask(q1);
//			QTask q2 = new QTask();
//			q2.setName("Pattern (if any) is clearly visible");
//			q2.setScale(fourLevels);
//			n1.addSubtask(q2);
//			s1.addSubtask(n1);
//			
//			Task n2 = new Task();
//			n2.setMaxMark(2);
//			n2.setName("Attractive materials");
//			s1.addSubtask(n2);
//		} catch (SubtaskTypeException e) {
//			e.printStackTrace();
//		}
		
		// New test: how well does our XML marshaller/unmarshaller do?
		XmlFileIO infile = new XmlFileIO("C:\\Users\\Robyn\\Desktop\\test.xml");
		//MarkingScheme it = infile.getMarkingScheme();

		String s = infile.getXML();
		System.out.println(s);
		
//		edu.monash.madam.formatters.ConsoleMaker cm = new edu.monash.madam.formatters.ConsoleMaker(it);
//		cm.doScheme(it);
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		setChanged();
	}

	public List<Mark> getSubtasks() {
		return Collections.unmodifiableList(tasks);
	}

	public void setActivityName(String name) {
		this.activityName = name;
		setChanged();
		
	}
}

