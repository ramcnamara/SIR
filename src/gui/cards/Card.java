package gui.cards;

import javax.swing.JButton;

import model.Mark;

public interface Card {
	public Mark getTask();
	public Mark getParentTask();
	public void save();
	public void reset();
	public JButton getAddSubtaskButton();
}
