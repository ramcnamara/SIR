package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumericType")
public class Task extends ComplexTask {
	@XmlElementWrapper(name="Subtasks", required=false, nillable=false)
	@XmlElements({ @XmlElement(name="Task", type=Task.class),
		@XmlElement(name="QTask", type=QTask.class),
		@XmlElement(name="Checkbox", type=Checkbox.class)
	})
	private ArrayList<Mark> subtasks;
	
	@XmlAttribute
	private float maxMark;
	
	@XmlAttribute
	private float minMark;
	
	@XmlAttribute
	private boolean bonus;
	
	@Override
	public List<Mark> getSubtasks() {
		if (subtasks == null) return null;
		return Collections.unmodifiableList(subtasks);
	}
	
	public Task() {
	}

	@Override
	public void addSubtask(Mark task) throws SubtaskTypeException {
		if (subtasks == null)
			subtasks = new ArrayList<Mark>();
		subtasks.add(task);
		
	}

	@Override
	public void makeOutput(OutputMaker om) {
		om.doTask(this);
		if (criteria != null)
			for (Criterion c: criteria)
				c.makeOutput(om);
		if (subtasks != null)
			for (Mark m: subtasks)
				m.makeOutput(om);
		om.endTask(this);
		
	}

	public float getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(float maxMark) {
		this.maxMark = maxMark;
	}

	public boolean getBonus() {
		return bonus;
	}

	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}

	@Override
	public float getMark() {
		float myMark = this.maxMark;
		if (subtasks != null)
			for (Mark m: subtasks)
				myMark += m.getMark();
		return myMark;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Mark removeSubtask(Mark task) {
		int idx = subtasks.indexOf(task);
		if (idx == -1)
			return null;
		return removeSubtask(idx);
	}

	@Override
	public Mark removeSubtask(int idx) {
		return subtasks.remove(idx);
	}
}
