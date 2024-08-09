package imgfile;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class BMPSaveFilter extends FileFilter{
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return false;
		}   
		String s = f.getName().toLowerCase();   
		return s.endsWith(".bmp");
	}
  
	@Override
	public String getDescription() {
		return "*.bmp,*.BMP";
	}

}
