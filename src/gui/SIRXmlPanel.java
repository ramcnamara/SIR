package gui;
import java.io.StringWriter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JScrollPane;

import model.MarkingScheme;

public class SIRXmlPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3215744673653118442L;
	
	// TODO: fix problem with scrolling and sizing in XmlTextPane
	//private XmlTextPane xmlDisplay;
	private JTextPane xmlDisplay;
	private JScrollPane scrollPanel;


	/**
	 * Create the panel.
	 */
	public SIRXmlPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "XML View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setPreferredSize(new Dimension(0, 0));
		
		// scrolling is broken in XmlTextPane
		// xmlDisplay = new XmlTextPane();
		xmlDisplay = new JTextPane();
		xmlDisplay.setEditable(false);
		scrollPanel = new JScrollPane(xmlDisplay);
		panel.add(scrollPanel);
	}
	
	@Override
	public void update(Observable scheme, Object arg1) {
		java.io.StringWriter sw = new StringWriter();

		JAXBContext context;
		try {
			context = JAXBContext.newInstance(MarkingScheme.class);

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(scheme, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		xmlDisplay.setText(sw.toString());
		
		// set display position to top of XML
		xmlDisplay.setSelectionStart(0);
		xmlDisplay.setSelectionEnd(0);
		scrollPanel.repaint();
	}
}
