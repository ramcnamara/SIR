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

import javax.swing.JSplitPane;

import model.MarkingScheme;
import net.miginfocom.swing.MigLayout;


public class SIRMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MarkingScheme theScheme;
	private SIRXmlPanel xmlPanel;
	private SIRTreePanel treePanel;
	private JPanel controlPanel;
	private SIRMetadataPanel schemePanel;
	private SIRCardPanel cardPanel;


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
				theScheme = new MarkingScheme();
				schemePanel.setScheme(theScheme);
				
				// Set up observers
				theScheme.addObserver(xmlPanel);
				theScheme.addObserver(treePanel);
				theScheme.addObserver(cardPanel);
				theScheme.addObserver(schemePanel);
				theScheme.refresh();
				
				// Instantiate scheme editor panel
				//schemePanel = new SIRMetadataPanel(theScheme);
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
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new XmlFileFilter());
				int fcval = fc.showOpenDialog(contentPane);
				
				// file chosen?
				if (fcval == JFileChooser.APPROVE_OPTION) {
					// This will need to change if we ever move to a multidocument interface.
					File infile = fc.getSelectedFile();
					try {
						JAXBContext  context = JAXBContext.newInstance(MarkingScheme.class);
						Unmarshaller unmarshaller = context.createUnmarshaller();
						theScheme = (MarkingScheme) unmarshaller.unmarshal(infile);
					} catch (JAXBException e1) {
						// TODO This should pop up a dialog.
						e1.printStackTrace();
					}

					// Set up observers
					theScheme.addObserver(xmlPanel);
					theScheme.addObserver(treePanel);
					theScheme.addObserver(cardPanel);
					theScheme.refresh();
					
					// Instantiate scheme editor panel
					schemePanel = new SIRMetadataPanel(theScheme);
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
				JFileChooser fc = new JFileChooser();
				int fcval = fc.showSaveDialog(contentPane);
				
				if (fcval == JFileChooser.APPROVE_OPTION) {
					File outfile = fc.getSelectedFile();

						JAXBContext context;
						try {
							context = JAXBContext.newInstance(MarkingScheme.class);

						Marshaller marshaller = context.createMarshaller();
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
		
		JSplitPane treeSplitPane = new JSplitPane();
		xmlSplitPane.setLeftComponent(treeSplitPane);
		xmlSplitPane.setResizeWeight(0.8);
		
		treePanel = new SIRTreePanel();
		treeSplitPane.setLeftComponent(treePanel);
		controlPanel = new JPanel();
		controlPanel.setLayout(new MigLayout("fill", "", ""));
		schemePanel = new SIRMetadataPanel(null);
		controlPanel.add(schemePanel, "dock north, growy");
		cardPanel = new SIRCardPanel();
		treePanel.addTreeSelectionListener(cardPanel);
		controlPanel.add(cardPanel, "push ,grow");
		treeSplitPane.setRightComponent(controlPanel);
		treeSplitPane.setResizeWeight(0.2);
		
		// Get things displaying on the XML Pane and Tree
		treePanel.addTreeSelectionListener(cardPanel);
	}
}