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

public class SIRXmlBundle implements Transferable {

	private String xml;
	private SIRNode node;
	public static final DataFlavor SIRXml = new DataFlavor(SIRXmlBundle.class, "SIR Xml");
	public static final DataFlavor SIRNode = new DataFlavor(SIRNode.class, "SIR Tree Node");
	private static final DataFlavor[] flavors = {SIRXml, SIRNode, DataFlavor.stringFlavor};
	
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

		xml = new String(out.toByteArray());
	}
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor == DataFlavor.stringFlavor || flavor == SIRXml)
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
