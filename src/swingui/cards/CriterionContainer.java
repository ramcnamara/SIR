package swingui.cards;

import model.Criterion;

/**
 * Interface implemented by the cardpanel classes that display
 * objects that can take criteria, namely Tasks and QTasks.
 * 
 * @author Robyn
 *
 */
public interface CriterionContainer {
	public void addCriterion(Criterion c);
}
