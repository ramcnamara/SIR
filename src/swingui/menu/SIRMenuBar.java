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
		JMenu mnFile = new JMenu("File");
		this.add(mnFile);
		
		loadTeachingPeriods();
		
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				parent.newScheme();
			}
		});
		mnFile.add(mntmNew);
	
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
	
			public void actionPerformed(ActionEvent e) {
				
				// display "file open" dialog
				Preferences prefs = Preferences.userRoot().node(getClass().getName());
				JFileChooser fc = new JFileChooser(prefs.get("LAST_USED_FOLDER", new File(".").getAbsolutePath()));
				fc.addChoosableFileFilter(new XmlFileFilter());
				int fcval = fc.showOpenDialog(parent);
				
				// file chosen?
				if (fcval == JFileChooser.APPROVE_OPTION) {
					// This will need to change if we ever move to a multidocument interface.
					File infile = fc.getSelectedFile();
					prefs.put("LAST_USED_FOLDER", infile.getParent());
					try {
						JAXBContext  context = JAXBContext.newInstance(MarkingScheme.class);
						Unmarshaller unmarshaller = context.createUnmarshaller();
						parent.setScheme((MarkingScheme) unmarshaller.unmarshal(infile));
					} catch (JAXBException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
		
		mnFile.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
	
			public void actionPerformed(ActionEvent e) {
				Preferences prefs = Preferences.userRoot().node(getClass().getName());
				JFileChooser fc = new JFileChooser(prefs.get("LAST_USED_FOLDER", new File(".").getAbsolutePath()));
				int fcval = fc.showSaveDialog(parent);
				
				if (fcval == JFileChooser.APPROVE_OPTION) {
					File outfile = fc.getSelectedFile();
					prefs.put("LAST_USED_FOLDER", outfile.getParent());
	
						JAXBContext context;
						try {
							context = JAXBContext.newInstance(MarkingScheme.class);
	
						Marshaller marshaller = context.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
						OutputStream outstream = new FileOutputStream(outfile);
						marshaller.marshal(parent.getScheme(), outstream);
						} catch (JAXBException e1) {
							// TODO Display some kind of sensible error message
							e1.printStackTrace();
						} catch (FileNotFoundException e1) {
							// TODO put up a "file not found" dialog (although this shouldn't happen)
							e1.printStackTrace();
						}
	
				}
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			// TODO: confirmation dialog if unsaved changes
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
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
