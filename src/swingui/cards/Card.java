package swingui.cards;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import model.scheme.Mark;
import model.scheme.MarkingScheme;

public abstract class Card extends JPanel {


	private static final long serialVersionUID = 1L;
	private Mark parent;
	protected ChangeListener listener;
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
