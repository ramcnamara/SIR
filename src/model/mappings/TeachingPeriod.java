package model.mappings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *<p> This class holds a list of valid teaching periods for this institution and also stores the currently-selected
 * teaching period (term or semester) and year.
 * 
 * <p>It is used to enable the automation of selection of appropriate learning outcomes sets.  These are specified in the file mappings.xml, 
 * which is zipped into outcomes.sirx.  Note that the key to these mappings is a concatenation of teaching period + ", " + year.
 * 
 * @author ram
 *
 */
public class TeachingPeriod {

	private static String currentTeachingPeriod;
	private static String currentTeachingYear;
	private static List<String> teachingPeriods;
	
	public static void setCurrentTeachingPeriod(String tp, String yr) {
		currentTeachingPeriod = tp;
		currentTeachingYear = yr;
	
		// The currently-selected teaching period is persistent between sessions, so
		// it is saved out to the user-level configuration file.
		Properties props = new Properties();
		
		File f = readTeachingPeriodFromFile(props);

		props.setProperty("currentteachingperiod", tp);
		props.setProperty("currentteachingyear", yr);
		try {
			OutputStream outfile = new FileOutputStream(f);
			props.store(outfile, "");
			outfile.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not open config.properties for reading");
		} catch (IOException e) {
			System.out.println("Failed to write to file config.properties");
		}
		
		
	}

	/**
	 * <p>Reads currently-selected teaching period from ~/SIR/config.properties into its parameter and returns a reference
	 * to the opened file.
	 * 
	 * @param props reference to Properties object
	 * @return File reference to config.properties file
	 */
	private static File readTeachingPeriodFromFile(Properties props) {
		String path = System.getProperty("user.home") + File.separator + "SIR" + File.separator + "config.properties";
		File f = new File(path);
		try {
			InputStream instream = new FileInputStream(f);
			props.load(instream);
			instream.close();
		} catch (FileNotFoundException ex) {
			System.out.println("No config.properties file found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to read file config.properties");
		}
		return f;
	}
	
	public static String getCurrentTeachingPeriod() {
		if (currentTeachingPeriod == null) {
			Properties props = new Properties();
			readTeachingPeriodFromFile(props);
			currentTeachingPeriod = props.getProperty("currentteachingperiod");
		}
		return currentTeachingPeriod;
	}
	
	public static String getCurrentTeachingYear() {
		if (currentTeachingYear == null) {
			Properties props = new Properties();
			readTeachingPeriodFromFile(props);
			currentTeachingYear = props.getProperty("currentteachingyear");
		}
		return currentTeachingYear;
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
