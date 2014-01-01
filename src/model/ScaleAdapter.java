package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ScaleAdapter extends XmlAdapter<AdaptedScale,Scale> {

	@Override
	public AdaptedScale marshal(Scale scale) throws Exception {
		AdaptedScale newScale = new AdaptedScale();
		newScale.levels = new ArrayList<String>();
		for (String lev: scale.asArray()) {
			newScale.levels.add(lev);
		}
		return newScale;
	}

	@Override
	public Scale unmarshal(AdaptedScale strings) throws Exception {
		return Scale.makeScheme(strings.levels.toArray(new String[strings.levels.size()]));
	}
	
	ScaleAdapter() {
	}
}