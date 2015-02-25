package swingui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Allow the file chooser to be restricted to XML files
 * 
 * @author Robyn
 *
 */
public class XmlFileFilter extends FileFilter {

	@Override
	/**
	 * Returns true for selectable files, which in this case means files ending in "xml" 
	 * and subdirectories.
	 */
	public boolean accept(File f) {
		// Always allow the option to browse subdirectories.
		if (f.isDirectory())
			return true;

		String ext = null;
		String filename = f.getName();
		int i = filename.lastIndexOf('.');

		if (i > 0 && i < filename.length() - 1) {
			ext = filename.substring(i + 1).toLowerCase();
		}

		if (ext == null)
			return false;

		return (ext.equals("xml"));
	}

	@Override
	/**
	 * Returns the constant string "XML files".
	 */
	public String getDescription() {
		return "XML files";
	}

}
