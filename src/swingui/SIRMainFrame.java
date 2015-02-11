package swingui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JSplitPane;

import swingui.menu.SIRMenuBar;
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
	private List<String> teachingPeriods;
	
	/**
	 * Create the frame.
	 */
	public SIRMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1152, 820);
		
		teachingPeriods = loadTeachingPeriods();
		
		SIRMenuBar menuBar = new SIRMenuBar(this);
		setJMenuBar(menuBar);
		
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
	// this method manages communication with the panels that depend on the
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


	/**
	 * Set current marking scheme to a newly-instantiated one.
	 * 
	 * Used by the File -> New menu item.
	 */
	public void newScheme() {
		// create new model.scheme object
		theScheme = new MarkingScheme();
		
		// Set up observers
		changeScheme(theScheme);
		
		controlPanel.removeAll();
		controlPanel.add(schemePanel, "dock north");
		controlPanel.add(cardPanel, "dock center, aligny top, growy");
		validate();
	}


	/**
	 * Set current marking scheme
	 * 
	 * Used by the File -> Open menu item.
	 * 
	 * @param scheme the new scheme.
	 */
	public void setScheme(MarkingScheme scheme) {
		theScheme = scheme;			
		
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


	/**
	 * Accessor method
	 * 
	 * Used by File -> Save menu item.
	 * 
	 * @return the current MarkingScheme
	 */
	public MarkingScheme getScheme() {
		return theScheme;
	}


	public List<String> getTeachingPeriods() {
		return teachingPeriods;
	}


	/** Loads list of teaching periods from config file.
	 * 
	 * Currently, the config file is hard-coded to ~/SIR/config.properties.  Eventually, it should look in a
	 * set of default properties and then overwrite with user-defined properties.
	 * 
	 * @return a newly-constructed List<String> where each entry describes a teaching period
	 * 
	 */
	private List<String> loadTeachingPeriods() {
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


	public void setTeachingPeriod(List<String> teachingPeriod) {
		this.teachingPeriods = teachingPeriod;
		
	}
}
