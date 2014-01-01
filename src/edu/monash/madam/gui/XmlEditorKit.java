package edu.monash.madam.gui;


import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
 * EditorKit for syntax-highlighting XML.
 * Taken from https://boplicity.nl/confluence/display/Java/Xml+syntax+highlighting+in+Swing+JTextPane.
 * @author Robyn
 *
 */
public class XmlEditorKit extends StyledEditorKit {
	private static final long serialVersionUID = 2969169649596107757L;
	private ViewFactory xmlViewFactory;

	public XmlEditorKit() {
		xmlViewFactory = new XmlViewFactory();
	}

	@Override
	public ViewFactory getViewFactory() {
		return xmlViewFactory;
	}

	@Override
	public String getContentType() {
		return "text/xml";
	}

}
