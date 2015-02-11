package swingui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.scheme.MarkingScheme;
import swingui.SIRMainFrame;
import swingui.SIRMetadataPanel;
import swingui.XmlFileFilter;

public class SIRMenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> teachingPeriods;

	/**
	 * @param menuBar
	 */
	public SIRMenuBar(final SIRMainFrame parent) {
		SIRFileMenu mnFile = new SIRFileMenu(parent);
		this.add(mnFile);
		
		loadTeachingPeriods();
		
		
		
	}

	/** Loads list of teaching periods from config file.
	 * 
	 * Currently, the config file is hard-coded to ~/SIR/config.properties.  Eventually, it should look in a
	 * set of default properties and then overwrite with user-defined properties.
	 * 
	 * @return a newly-constructed List<String> where each entry describes a teaching period
	 * 
	 */
	private ArrayList<String> loadTeachingPeriods() {
		// FIXME: this is VERY quick'n'dirty -- polish up and defensify!
		InputStream propFile = null;
		try {
			propFile = new FileInputStream(System.getProperty("user.home") + File.separator + "SIR" + File.separator + "config.properties");
		} catch (FileNotFoundException ex) {
			System.out.println("Properties file not found");
		}
		
		Properties props = new Properties();
		try {
			props.load(propFile);
		} catch (IOException e2) {
			System.out.println("Failed to read properties");
		}
		
		teachingPeriods = new ArrayList<String>();
		for (String s : props.getProperty("teachingperiods").split("\\|")) {
			teachingPeriods.add(s);
			System.out.println(s);
		}
		
		return teachingPeriods;
	}

}
