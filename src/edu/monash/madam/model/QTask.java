package edu.monash.madam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A QTask is a qualitative Task.  It can have qualitatively-marked subtasks but not 
 * numerically-marked subtasks.  It may have a Scale for qualitative marking, but this is
 * optional.  It may also have subcriteria.
 * 
 * 
 * @author Robyn
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualitativeType")
public class QTask extends ComplexTask {
	@XmlElementWrapper(name="Subtasks")
	@XmlElement(name="QTask", type=QTask.class)
	private ArrayList<QTask> subtasks;
	
	@XmlElement(name="Scale")
	@XmlJavaTypeAdapter(ScaleAdapter.class)
	private Scale scale;


	@Override
	public void addSubtask(Mark task) throws SubtaskTypeException {
		// Check that prospective subtask is qualitative
		if (task instanceof QTask)
			subtasks.add((QTask)task);
		else throw new SubtaskTypeException();
	}

	@Override
	public void makeOutput(OutputMaker om) {
		om.doQTask(this);
		if (criteria != null) {
			for (Criterion c: criteria)
				c.makeOutput(om);
		}
		if (subtasks != null)
			for (QTask qt: subtasks)
				qt.makeOutput(om);
		om.endQTask(this);
		
	}
	
	public QTask() {
		this.scale = null;
		this.subtasks = new ArrayList<QTask>();
	}

	public Scale getScale() {
		return scale;
	}

	public void setScale(Scale scale) {
		this.scale = scale;
	}

	@Override
	public float getMark() {
		return 0;
	}

	@Override
	public List<Mark> getSubtasks() {
		ArrayList<Mark> newList = new ArrayList<Mark>();
		for (QTask qt: subtasks)
			newList.add(qt);
		return Collections.unmodifiableList(newList);
	}

}
