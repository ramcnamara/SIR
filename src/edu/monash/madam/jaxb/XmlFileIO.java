package edu.monash.madam.jaxb;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import edu.monash.madam.model.MarkingScheme;
import edu.monash.madam.model.SubtaskTypeException;

public class XmlFileIO {
	private JAXBContext context;
	private MarkingScheme jaxbscheme;
	
	public XmlFileIO(String filename) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(MarkingScheme.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		File infile = new File(filename);
		jaxbscheme = (MarkingScheme) unmarshaller.unmarshal(infile);
	}


	public String getXML() throws JAXBException {
		
	    java.io.StringWriter sw = new StringWriter();

	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	    marshaller.marshal(jaxbscheme, sw);

	    return sw.toString();		
	}
	
	
	public MarkingScheme getMarkingScheme() throws SubtaskTypeException {
		if (jaxbscheme == null)
			return null;
		
//		MarkingScheme scheme = new MarkingScheme();
//		scheme.setPreamble(jaxbscheme.getPreamble());
//		scheme.setActivityName(jaxbscheme.getActivityName());
//		scheme.setSubtitle(jaxbscheme.getSubtitle());
//		scheme.setUnitCode(jaxbscheme.getUnitCode());
//		
//		for (Mark m: jaxbscheme.getTasks().getTaskOrQTaskOrCheckbox()) {
//			scheme.add(m.toMark());
//		}
//		
//		
		return jaxbscheme;
	}
}
