package imgfile;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class WBMPSaveFilter extends FileFilter{
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return false;
		}   
		String s = f.getName().toLowerCase();   
		return s.endsWith(".wbmp");
	}
  
	@Override
	public String getDescription() {
		return "*.wbmp,*.WBMP";
	}

}
