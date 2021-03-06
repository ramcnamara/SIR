package swingui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import model.scheme.MarkingScheme;
import net.miginfocom.swing.MigLayout;
import swingui.menu.SIRMenuBar;


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
		
//		TeachingPeriod.loadTeachingPeriods();
//		TeachingPeriod.getCurrentTeachingPeriod();
		
		// load associations for learning outcome sets
//		try {
//			String path = System.getProperty("user.home") + File.separator + "SIR" + File.separator + "outcomes.sirx";
//			ZipFile zip = new ZipFile(path);
//			ZipEntry mapfile = zip.getEntry("mappings.xml");
//			zip.close();
//			if (mapfile == null)
//				System.out.println("Outcomes mapping file wasn't found in outcomes.sirx");
//			else {
//				System.out.println("Outcomes mapping file found!");
//				XMLMappings mappings = null;
//				try {
//					JAXBContext  context = JAXBContext.newInstance(XMLMappings.class);
//					Unmarshaller unmarshaller = context.createUnmarshaller();
//					mappings = (XMLMappings) unmarshaller.unmarshal(zip.getInputStream(mapfile));
//					
//					for (XMLMappingType map:mappings.getUnitdata()) {
//						String key = map.getUnitcode() + " " + map.getTeachingperiod();
//
//						// Store key -> GUID mappings
//						for (String guid:map.getOutcomecollections().getGuid()) {
//							OutcomesMap.addGuidMapping(key, guid);
//							System.out.println("Mapping " + key + " to GUID " + guid);
//						}
//					}
//				} catch (JAXBException e1) {
//					e1.printStackTrace();
//				}
//				
//			}
//		} catch (IOException e1) {
//			// TODO should be a dialog
//			System.out.println("Couldn't read outcomes.sirx");
//		}
		
		SIRMenuBar menuBar = new SIRMenuBar(this);
		setJMenuBar(menuBar);
		
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
	/**
	 * Part of the Observer pattern.  This method delegates the
	 * update to the subpanels of SIRMainFrame that contain the tree and
	 * card views.
	 */
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
//		theScheme.setOffering("<Unknown unit code>, " + TeachingPeriod.getCurrentTeachingPeriod() + ", " + TeachingPeriod.getCurrentTeachingYear());
		
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
//		theScheme.setOffering(theScheme.getUnitCode() + " " + TeachingPeriod.getCurrentTeachingPeriod() + ", " + TeachingPeriod.getCurrentTeachingYear());
		
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

}
