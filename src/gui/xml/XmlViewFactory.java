package gui.xml;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * ViewFactory for creating XML syntax-highlighted text views.
 * Taken from https://boplicity.nl/confluence/display/Java/Xml+syntax+highlighting+in+Swing+JTextPane.
 * @author Robyn
 *
 */
public class XmlViewFactory implements ViewFactory {

    /**
     * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
     */
    public View create(Element element) {
 
        return new XmlView(element);
    }

}
