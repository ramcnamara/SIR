package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;




import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JSplitPane;

import model.MarkingScheme;

import java.awt.Component;

import javax.swing.Box;

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
	private SIRMarkListPanel cardPanel;


	/**
	 * Create the frame.
	 */
	public SIRMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 602);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
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

					// Get things displaying on the XML Pane and Tree
					theScheme.addObserver(xmlPanel);
					theScheme.addObserver(treePanel);
					theScheme.addObserver(cardPanel);
					xmlPanel.update(theScheme, null);
					treePanel.update(theScheme, cardPanel);
					cardPanel.update(theScheme, null);
					
					// Instantiate scheme editor panel
					schemePanel = new SIRMetadataPanel(theScheme);
					controlPanel.removeAll();
					controlPanel.add(schemePanel);
					controlPanel.add(new JSeparator());
					controlPanel.add(cardPanel);
					validate();
				}
			}
		});
		mnFile.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		// TODO: factor this out so that it can also be used for File -> New.
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane xmlSplitPane = new JSplitPane();
		xmlSplitPane.setOneTouchExpandable(true);
		xmlSplitPane.setContinuousLayout(true);
		xmlSplitPane.setDividerLocation(0.75);
		xmlSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(xmlSplitPane, BorderLayout.CENTER);
		
		xmlPanel = new SIRXmlPanel();
		xmlSplitPane.setRightComponent(xmlPanel);
		
		JSplitPane treeSplitPane = new JSplitPane();
		xmlSplitPane.setLeftComponent(treeSplitPane);
		
		treePanel = new SIRTreePanel();
		treeSplitPane.setLeftComponent(treePanel);
		treePanel.setMinimumSize(new Dimension(100, 100));
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		treeSplitPane.setRightComponent(controlPanel);
		cardPanel = new SIRMarkListPanel();
		
		// If we don't put any content into the treePanel on creation it'll end up one line high,
		// so add a VerticalStrut.  Note that this controlPanel will be replaced on load anyway.
		Component verticalStrut = Box.createVerticalStrut(300);
		controlPanel.add(verticalStrut);
	}

}
