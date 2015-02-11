package model.mappings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OutcomesMap {

	private static Map<String, Set<String>> guidMap = new HashMap<String, Set<String>>();
	
	/**
	 * Record the association between an offering (unit + teaching period) and a set of outcomes denoted by GUID.
	 * 
	 * @param offering String containing the unit code and teaching period (including year)
	 * @param guid String giving the GUID of the set of learning outcomes to be associated
	 */
	public static void addMapping(String offering, String guid) {
		Set<String> newGuids;
		
		offering = offering.toUpperCase();
		if (guidMap.containsKey(offering))
			newGuids = guidMap.get(offering);
		else
			newGuids = new HashSet<String>();
		
		newGuids.add(guid);
		guidMap.put(offering, newGuids);
	}
	
	
	/**
	 * Look up and return a set of all outcome GUIDs associated with a particular course offering (unit code + teaching period).
	 * 
	 * @param offering
	 * @return the set of Strings denoting associated GUIDs, or null if the offering is not found.
	 */
	public Set<String> getOutcomesGuid(String offering) {
		if (guidMap.containsKey(offering))
			return guidMap.get(offering);
		
		return null;
	}
	
}
