package edu.monash.madam.gui;

import javax.swing.JTextPane;

/**
 * Syntax-highlighting textpane for displaying XML. 
 * Taken from https://boplicity.nl/confluence/display/Java/Xml+syntax+highlighting+in+Swing+JTextPane.
 * 
 * @author Robyn
 *
 */
public class XmlTextPane extends JTextPane {
	
	private static final long serialVersionUID = 1L;

	public XmlTextPane() {
        
        // Set editor kit
        this.setEditorKitForContentType("text/xml", new XmlEditorKit());
        this.setContentType("text/xml");
    }
}
