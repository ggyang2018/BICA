package imgfile;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class JPEGSaveFilter extends FileFilter{
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return false;
		}   
		String s = f.getName().toLowerCase();   
		return s.endsWith(".jpeg");
	}
  
	@Override
	public String getDescription() {
		return "*.jpeg,*.JPEG\"";
	}

}
