package swingui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.mappings.OutcomesMap;
import model.scheme.MarkingScheme;
import swingui.SIRMainFrame;
import swingui.XmlFileFilter;

public class SIRFileMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SIRFileMenu(final SIRMainFrame parent) {
		super("File");
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				OutcomesMap.reset();
				parent.newScheme();
			}
		});
		this.add(mntmNew);
	
		
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
		
		this.add(mntmLoad);
		
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
		this.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			// TODO: confirmation dialog if unsaved changes
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(mntmExit);
	}
}
