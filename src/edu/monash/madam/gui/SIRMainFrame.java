package edu.monash.madam.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.monash.madam.model.MarkingScheme;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JSplitPane;

public class SIRMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MarkingScheme theScheme;
	private SIRXmlPanel xmlPanel;
	private SIRTreePanel treePanel;
	private SIRControlPanel controlPanel;
	private SIRSchemePanel schemePanel;


	/**
	 * Create the frame.
	 */
	public SIRMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 812, 596);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// display "file open" dialog
				JFileChooser fc = new JFileChooser();
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
					xmlPanel.update(theScheme, null);
					treePanel.update(theScheme, null);
					
					// Instantiate scheme editor panel
					schemePanel = new SIRSchemePanel(theScheme);
					controlPanel.removeAll();
					controlPanel.add(schemePanel);
					repaint();
				}
			}
		});
		mnFile.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane.setLeftComponent(splitPane_1);
		
		xmlPanel = new SIRXmlPanel();
		splitPane.setRightComponent(xmlPanel);
		treePanel = new SIRTreePanel();
		splitPane_1.setLeftComponent(treePanel);
		controlPanel = new SIRControlPanel();
		splitPane_1.setRightComponent(controlPanel);
		contentPane.repaint();
	}

}
