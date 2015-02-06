package swingui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;

import javax.swing.JSplitPane;

import model.scheme.MarkingScheme;
import net.miginfocom.swing.MigLayout;


public class SIRMainFrame extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MarkingScheme theScheme;
	private SIRXmlPanel xmlPanel;
	private SIRTreePanel treePanel;
	private JPanel controlPanel;
	private SIRCardPanel cardPanel;
	private JSplitPane treeSplitPane;
	private SIRMetadataPanel schemePanel;


	/**
	 * Create the frame.
	 */
	public SIRMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1152, 820);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// create new model.scheme object
				theScheme = new MarkingScheme();
				
				// Set up observers
				changeScheme(theScheme);
				
				controlPanel.removeAll();
				controlPanel.add(schemePanel, "dock north");
				controlPanel.add(cardPanel, "dock center, aligny top, growy");
				validate();
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
				int fcval = fc.showOpenDialog(contentPane);
				
				// file chosen?
				if (fcval == JFileChooser.APPROVE_OPTION) {
					// This will need to change if we ever move to a multidocument interface.
					File infile = fc.getSelectedFile();
					prefs.put("LAST_USED_FOLDER", infile.getParent());
					try {
						JAXBContext  context = JAXBContext.newInstance(MarkingScheme.class);
						Unmarshaller unmarshaller = context.createUnmarshaller();
						theScheme = (MarkingScheme) unmarshaller.unmarshal(infile);
					} catch (JAXBException e1) {
						e1.printStackTrace();
					}

					
					// Instantiate scheme editor panel
					schemePanel = new SIRMetadataPanel(theScheme);
					schemePanel.rereadTotalMark();
					
					// Set up observers
					changeScheme(theScheme);
					
					controlPanel.removeAll();
					controlPanel.add(schemePanel, "dock north");
					controlPanel.add(cardPanel, "dock center, aligny top, growy");
					validate();
				}
			}
		});
		
		mnFile.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Preferences prefs = Preferences.userRoot().node(getClass().getName());
				JFileChooser fc = new JFileChooser(prefs.get("LAST_USED_FOLDER", new File(".").getAbsolutePath()));
				int fcval = fc.showSaveDialog(contentPane);
				
				if (fcval == JFileChooser.APPROVE_OPTION) {
					File outfile = fc.getSelectedFile();
					prefs.put("LAST_USED_FOLDER", outfile.getParent());

						JAXBContext context;
						try {
							context = JAXBContext.newInstance(MarkingScheme.class);

						Marshaller marshaller = context.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
						OutputStream outstream = new FileOutputStream(outfile);
						marshaller.marshal(theScheme, outstream);
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
		
		// TODO: factor this out so that it can also be used for File -> New.
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane xmlSplitPane = new JSplitPane();
		xmlSplitPane.setOneTouchExpandable(true);
		xmlSplitPane.setContinuousLayout(true);
		xmlSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(xmlSplitPane, BorderLayout.CENTER);
		
		xmlPanel = new SIRXmlPanel();
		xmlPanel.setBorder(null);
		xmlSplitPane.setRightComponent(xmlPanel);
		
		treeSplitPane = new JSplitPane();
		xmlSplitPane.setLeftComponent(treeSplitPane);
		xmlSplitPane.setResizeWeight(0.8);
		
		cardPanel = new SIRCardPanel();
		treePanel = new SIRTreePanel(cardPanel);
		treeSplitPane.setLeftComponent(treePanel);
		controlPanel = new JPanel();
		controlPanel.setLayout(new MigLayout("fill", "", ""));
		schemePanel = new SIRMetadataPanel(null);
		controlPanel.add(schemePanel, "dock north, growy");
		treePanel.addTreeSelectionListener(cardPanel);
		controlPanel.add(cardPanel, "push ,grow");
		treeSplitPane.setRightComponent(controlPanel);
		treeSplitPane.setResizeWeight(0.2);
		
		// Get things displaying on the XML Pane and Tree
		treePanel.addTreeSelectionListener(cardPanel);
	}
	
	
	// When the marking scheme is replaced by a new one (rather than updated)
	// this method can manages communication with the panels that depend on the
	// MainFrame.
	private void changeScheme(MarkingScheme newScheme) {
		// Remove previous observers
		newScheme.deleteObservers();
		
		// Set up observers
		newScheme.addObserver(xmlPanel);
		newScheme.addObserver(schemePanel);
		newScheme.addObserver(SIRMainFrame.this);
		
		// reset stored marking schemes
		schemePanel.setScheme(newScheme);
		this.theScheme = newScheme;
		
		// trigger an update
		newScheme.refresh();
	}


	@Override
	public void update(Observable observable, Object parameters) {
		treePanel.update(observable, parameters);
		cardPanel.update(observable, parameters);
	}
}
