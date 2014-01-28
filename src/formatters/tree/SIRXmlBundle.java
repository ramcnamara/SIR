package formatters.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Mark;
import model.MarkingScheme;

/**
 * Transferable class to support drag/drop and copy/cut/paste operations.
 * 
 * This class packages an XML representation of its data (which can be used for String-based
 * operations if needed) with a reference to a node from a SIRTree (if that is the source of the
 * data).
 * 
 * @author ram
 *
 */
public class SIRXmlBundle implements Transferable {

	private String xml;
	private SIRNode node;
	public static final DataFlavor SIRXml = new DataFlavor(SIRXmlBundle.class, "SIR Xml");
	public static final DataFlavor SIRNode = new DataFlavor(SIRNode.class, "SIR Tree Node");
	private DataFlavor[] flavors = {SIRXml, SIRNode, DataFlavor.stringFlavor, null};
	
	
	/**
	 * Two-parameter constructor.  This constructor bundles together an XML version of
	 * the Mark being transferred (and its subtasks and criteria, if any) and a SIRNode
	 * (if the datasource is a SIRTree).  It also registers the actual class of the Mark
	 * as an available DataFlavor, which allows components to react intelligently to
	 * the type of data stored in the clipboard.
	 * 
	 * This constructor will terminate if the first parameter does not contain a valid Mark.
	 * 
	 * @param m the Mark to be transferred
	 * @param n the source node (which may be null)
	 */
	public SIRXmlBundle(Mark m, SIRNode n) {
		JAXBContext context;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		node = n;
		
		try {
			context = JAXBContext.newInstance(MarkingScheme.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(m, out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		// set a DataFlavor to represent the actual runtime type of the Mark
		flavors[3] = new DataFlavor(m.getClass(), "Mark");

		xml = new String(out.toByteArray());
	}
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor == DataFlavor.stringFlavor || flavor == SIRXml || flavor == flavors[3])
			return xml;
		if (flavor == SIRNode)
			return node;
		throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (DataFlavor df: flavors)
			if (flavor == df)
				return true;
		return false;
	}
}
