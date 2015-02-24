package model.scheme;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB requires classes to have a default constructor, which Scale does not have.  This
 * class maps the AdaptedScale class onto a Scale, which allows Scales to be marshalled and
 * unmarshalled without impacting on their runtime characteristics.  It is not used outside
 * the marshalling/unmarshalling process.
 * 
 * @author Robyn
 *
 */
public class ScaleAdapter extends XmlAdapter<AdaptedScale,Scale> {

	@Override
	/**
	 * Convert a Scale into an AdaptedScale.
	 * 
	 * @param scale the Scale to be converted
	 */
	public AdaptedScale marshal(Scale scale) throws Exception {
		if (scale == null)
			return null;
		AdaptedScale newScale = new AdaptedScale();
		newScale.levels = new ArrayList<String>();
		for (String lev: scale.asArray()) {
			newScale.levels.add(lev);
		}
		return newScale;
	}

	@Override
	/**
	 * Convert an AdaptedScale into a Scale.
	 * 
	 * @param the AdaptedScale to be converted.
	 */
	public Scale unmarshal(AdaptedScale strings) throws Exception {
		return Scale.makeScheme(strings.levels.toArray(new String[strings.levels.size()]));
	}
	
	/**
	 * Default constructor.  Required by JAXB.
	 */
	ScaleAdapter() {
	}
}