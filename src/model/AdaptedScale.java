package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class AdaptedScale {
	@XmlElement(name="Level")
	public List<String> levels;
}