package edu.monash.madam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task extends ComplexTask {
	private ArrayList<Mark> subtasks;
	private float maxMark;
	private boolean bonus;
	
	@Override
	public List<Mark> getSubtasks() {
		return Collections.unmodifiableList(subtasks);
	}
	
	public Task() {
		subtasks = new ArrayList<Mark>();
	}

	@Override
	public void addSubtask(Mark task) throws SubtaskTypeException {
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

	public boolean isBonus() {
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

}
