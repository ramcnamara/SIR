package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * This is an adaptor that allows JAXB to deal with the Scale class.
 * @author Robyn
 *
 */
public class AdaptedScale {
	@XmlElement(name="Level")
	public List<String> levels;
}