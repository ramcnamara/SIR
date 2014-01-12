package model;

/**
 * Trivial object creation methods required by JAXB. 
 * 
 * @author Robyn
 *
 */
public class ObjectFactory {
	public Checkbox createCheckbox() {
		return new Checkbox();
	}
	
	public Criterion createCriterion() {
		return new Criterion();
	}
	
	public MarkingScheme createMarkingScheme() {
		return new MarkingScheme();
	}
	
	public QTask createQTask() {
		return new QTask();
	}
	
	public String[] createScale() {
		return new String[0];
	}
	
	public Task createTask() {
		return new Task();
	}

}
