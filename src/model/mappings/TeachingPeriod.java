package model.mappings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TeachingPeriod {

	private static String currentTeachingPeriod;
	private static List<String> teachingPeriods;
	
	public static void setCurrentTeachingPeriod(String tp) {
		currentTeachingPeriod = tp;
	}
	
	public static String getCurrentTeachingPeriod() {
		return currentTeachingPeriod;
	}

	
	public static void setTeachingPeriods(List<String> tps) {
		teachingPeriods = tps;
	}

	public static List<String> getTeachingPeriods() {
		return teachingPeriods;
	}

	/** Loads list of teaching periods from config file.
	 * 
	 * Currently, the config file is hard-coded to ~/SIR/config.properties.  Eventually, it should look in a
	 * set of default properties and then overwrite with user-defined properties. 
	 */
	public static void loadTeachingPeriods() {
		// FIXME: this is VERY quick'n'dirty -- polish up and defensify!
		InputStream propFile = null;
		try {
			propFile = new FileInputStream(System.getProperty("user.home") + File.separator + "SIR" + File.separator + "config.properties");
		} catch (FileNotFoundException ex) {
			System.out.println("Properties file not found");
		}
		
		Properties props = new Properties();
		try {
			props.load(propFile);
		} catch (IOException e2) {
			System.out.println("Failed to read properties");
		}
		
		teachingPeriods = new ArrayList<String>();
		for (String s : props.getProperty("teachingperiods").split("\\|")) {
			teachingPeriods.add(s);
			System.out.println(s);
		}

	}
}
