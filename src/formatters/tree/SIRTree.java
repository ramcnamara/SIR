package formatters.tree;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;

/**
 * An extension of JTree that implements drag and drop.
 * 
 * @author ram
 *
 */
public class SIRTree extends JTree implements DragGestureListener, DragSourceListener, DropTargetListener {


	/**
	 * Wrapper for JTree constructor.
	 * 
	 * @param root the TreeNode at the root of the SIRTree
	 */
	public SIRTree(SIRNode root) {
		super(root);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void dragGestureRecognized(DragGestureEvent ev) {
		System.out.print("Dragging! ");
		
		Object dragObj = getLastSelectedPathComponent();
		SIRNode dragNode;
		
		if (dragObj instanceof SIRNode) {
			dragNode = (SIRNode) dragObj;
		}
		else
			return;
		
		System.out.println(dragNode.toString());
		
	}
	
	/*
	 * Methods from DragSourceListener
	 * 
	 */

	// This method intentionally left blank.
	@Override
	public void dragDropEnd(DragSourceDropEvent ev) {
		
	}

	@Override
	public void dragEnter(DragSourceDragEvent ev) {
		// TODO Change the cursor
		
	}

	@Override
	public void dragExit(DragSourceEvent ev) {
		
	}

	@Override
	public void dragOver(DragSourceDragEvent ev) {
		// TODO Change the cursor to reflect what's being dragged over
		
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent ev) {
		
	}
	
	/*
	 * Methods from DropTargetListener
	 * 
	 */

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
