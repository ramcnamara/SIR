package model;

import java.util.ArrayList;

/**
 * A set of levels for marking criterion-referenced assessment.  Each qualitatively-marked
 * item is associated with a Scale that determines which levels are available to the
 * marker.
 * 
 * RatingSchemes should be instantiated with the static method Scale.makeScheme(String[]),
 * which generates and interns the Scale if it is new and returns a reference to a semantically-identical
 * Scale if one exists.  This ensures that RatingSchemes can be compared with ==.
 * 
 * Accordingly, Scale has no public constructors.
 * @author ram
 *
 */
public class Scale {
	
	/**
	 * Set of all unique rating schemes.
	 */
	private static ArrayList<Scale> schemes = new ArrayList<Scale>();
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	/**
	 * Given an array of Strings representing a rating scheme, return a reference to an equivalent rating scheme
	 * in the current static scheme set.  If the given scheme isn't already there, it is added.
	 * 
	 * This allows rating schemes to be compared for equivalence with ==.
	 * 
	 * @param aScheme
	 * @return a reference to the scheme
	 */
	public static Scale makeScheme(String[] aScheme) {
		
		for (Scale t: schemes) {
			if (t.asArray().equals(aScheme))
				return t;
		}
		Scale newScheme = new Scale(aScheme);
		schemes.add(newScheme);
		return newScheme;
	}
	
	/**
	 * The ordered set of unique strings that describe the achievement levels possible in this Scale.
	 */
	private ArrayList<String> levels;
	
	protected Scale(String[] scheme) {
		this.levels = new ArrayList<String>();
		for (String s: scheme) {
			this.levels.add(s);
		}
	}
	
	/**
	 * Returns the levels in the current Scale as an array of Strings.
	 * 
	 * @return String array representation of this Scale.
	 */
	public String[] asArray() {
		String[] array = new String[this.levels.size()];
		return this.levels.toArray(array);
	}
	
	/**
	 * Remove a given rating scheme from the current static scheme set if it's there, otherwise do nothing.
	 * 
	 * @param aScheme	the scheme to be removed
	 */
	public void removeScheme(ArrayList<String> aScheme) {
		schemes.remove(aScheme);
	}
	
	@Override
	public String toString() {
		String out = "";
		for (String s: levels) {
			out += s + "\t";
		}
		return out;
	}
}
	
