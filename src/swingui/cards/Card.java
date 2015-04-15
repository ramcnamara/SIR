package swingui.cards;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import model.scheme.Mark;
import model.scheme.MarkingScheme;

/**
 * Base class for Cards.
 * 
 * The main task-editing display is handled by a CardStack inside swingui.SIRCardPanel.
 * 
 * @author ram
 *
 */
public abstract class Card extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2627577147787672593L;
	protected ChangeListener listener;
	protected Mark parent;
	protected static MarkingScheme scheme;
	
	public Card(MarkingScheme scheme, ChangeListener listener, Mark parent) {
		this.parent = parent;
		this.listener = listener;
		Card.scheme = scheme;
	}
	
	
	public abstract Mark getTask();
	public Mark getParentTask() {
		return parent;
	}
	
	
	public abstract void save();
	
	
	public abstract void reset();
	
	
	public abstract JButton getAddSubtaskButton();
}
