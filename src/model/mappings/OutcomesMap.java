package model.mappings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.outcomes.LearningOutcomes;

public class OutcomesMap {

	private static Map<String, Set<String>> guidMap = new HashMap<String, Set<String>>();
	private static Map<String, LearningOutcomes>  outcomeMap = new HashMap<String, LearningOutcomes>();
	
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
	 * @return the set of Strings denoting associated GUIDs, or an empty set if the offering is not found.
	 */
	public static Set<String> getOutcomesGuid(String offering) {
		offering = offering.toUpperCase();
		if (guidMap.containsKey(offering))
			return guidMap.get(offering);
		
		return new HashSet<String>();
	}
	
	public static void loadOutcomes(String guid) {
		// find outcomes file in outcomes.sirx
		ZipFile zip = null;
		ZipEntry outcomeFile = null;
		try {
			String path = System.getProperty("user.home") + File.separator + "SIR" + File.separator + "outcomes.sirx";
			zip = new ZipFile(path);
			outcomeFile = zip.getEntry(guid + ".xml");
			if (outcomeFile == null) {
				System.out.println("Learning outcomes file " + guid + ".xml wasn't found in outcomes.sirx");
				return;
			}
		} catch (IOException e1) {
			// TODO should be a dialog
			System.out.println("Couldn't read outcomes.sirx");
		}
			
				System.out.println("Outcomes file found!");
				LearningOutcomes learningOutcomes = null;
				try {
					System.out.println("Reading outcomes file " + guid + ".xml");
					JAXBContext  context = JAXBContext.newInstance(LearningOutcomes.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					learningOutcomes = (LearningOutcomes) unmarshaller.unmarshal(zip.getInputStream(outcomeFile));
					outcomeMap.put(guid, learningOutcomes);
				} catch (JAXBException e1) {
					// invalid XML in guid.xml
					e1.printStackTrace();
				} catch (IOException e) {
					// failed to open outcomeFile
					e.printStackTrace();
				}
				
			

	}


	/**
	 * Remove current GUID -> outcomes mapping, leaving the set of offering -> GUID mappings intact.
	 * 
	 * Used when instantiating a new marking scheme or when changing the current unit code.  The outcomes will be reloaded when
	 * the unit code is reset.
	 */
	public static void reset() {
		outcomeMap = new HashMap<String, LearningOutcomes>();		
	}
	
}
