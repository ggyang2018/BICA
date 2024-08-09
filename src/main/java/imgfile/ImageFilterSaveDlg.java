package imgfile;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * ImageFilterSaveDlg based on JFileChooser filter to save the image .
 * @author guangyang
 *
 */
public class ImageFilterSaveDlg {
	
	
	public void saveImageWithFilter() {
		//FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		//jfc.setFileFilter(filter);
		jfc.addChoosableFileFilter(new JPGSaveFilter());
		jfc.addChoosableFileFilter(new JPEGSaveFilter());
		jfc.addChoosableFileFilter(new PNGSaveFilter());
		jfc.addChoosableFileFilter(new GIFSaveFilter());
		jfc.addChoosableFileFilter(new BMPSaveFilter());
		jfc.addChoosableFileFilter(new WBMPSaveFilter()); 
				
		int returnValue = jfc.showSaveDialog(null);
						
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			String ext = "";		  
			String extension = jfc.getFileFilter().getDescription();
			
			if (extension.equals("*.jpg,*.JPG"))
				ext = ".jpg";
			else if (extension.equals("*.png,*.PNG"))
				ext = ".png";
			else if (extension.equals("*.gif,*.GIF"))
				ext = ".gif";
			else if (extension.equals("*.wbmp,*.WBMP"))
				ext = ".wbmp";
			else if (extension.equals("*.jpeg,*.JPEG"))
				ext = ".jpeg";
			else if (extension.equals("*.bmp,*.BMP"))
				ext = ".bmp";
								
			File selectedFile = jfc.getSelectedFile();
			String pt = selectedFile.getAbsolutePath()+ext;
			System.out.println(pt);					

		}
	}

}
