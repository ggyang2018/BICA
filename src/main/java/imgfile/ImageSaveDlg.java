package imgfile;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.NumberFormatter;

import tech.sunnykit.guikit.widget.XYLayout;
import parser.NoticeBox;

/**
 * Save Image dialog allow to choose type, size, and location
 * Mainly used for output panel image
 * @author guangyang
 *
 */
public class ImageSaveDlg extends JDialog implements ActionListener{
	
	private static final long serialVersionUID =8L;
	
	int imgWid, imgHgt;
	String imgType;
	JComboBox<String> typeBox;
	JTextField folderFd, nameFd;
	JButton browserBtn, saveBtn, cancelBtn;
	String homeFolder;
	JFormattedTextField imgWidFd, imgHgtFd;
	JLabel extLb; 
	
	String[] imgTypes = { "jpg", "png", "gif", "wbmp", "bmp", "jpeg", "tif"};
	
	JPanel pane;
	
	public ImageSaveDlg(JFrame frm, String title, String iconPath)
	{
		super(frm);
		setModal(true);
		setTitle(title);
		if(iconPath!=null )
			setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{  
				dispose(); 
				setVisible(false);
			}
				 
			public void windowDeiconified(WindowEvent e)
			{
				Dimension dim = getSize();
				Double w = dim.getWidth();
				Double h = dim.getHeight();
				setSize(w.intValue()+1, h.intValue()+1);
				setSize(w.intValue(), h.intValue());
			}
		});
		getContentPane().setLayout(new XYLayout());
		File homedir = FileSystemView.getFileSystemView().getHomeDirectory();
		homeFolder = homedir.getAbsolutePath();
		init();		
	}
			
	public void setFavourBounds(int width, int height)
	{
		Dimension dim = Toolkit.getDefaultToolkit( ).getScreenSize();
		float fx = (float)dim.getWidth( )/2;
		float fy = (float)dim.getHeight( )/2;
		int x = Math.round(fx - (float)(width/2));
		int y = Math.round(fy - (float)(height/2))-15;
		setBounds(x, y, width, height);
	}
	
	public void setPane(JPanel jp) { pane = jp; }
	
	public void displayDlg() {
		this.setVisible(true);
	}
	
	public void closeDlg() {
		this.dispose();
		this.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();		
		switch(cmd) {
			case "chooseType":{
				imgType = typeBox.getSelectedItem().toString();
				extLb.setText("."+imgType);
				extLb.repaint();
				break;
			}case "Browser":
				browserFolder();
				break;
			case "Save":
				saveImage();
				break;
			case "Cancel":
				closeDlg();
				break;								
		}				
	}
	
	//beware jpg and jpeg don't support alpha only 3 bits, to make sure image without alpha
	BufferedImage ensureOpaque(BufferedImage bi) {
	    if (bi.getTransparency() == BufferedImage.OPAQUE)
	        return bi;
	    int w = bi.getWidth();
	    int h = bi.getHeight();
	    int[] pixels = new int[w * h];
	    bi.getRGB(0, 0, w, h, pixels, 0, w);
	    BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    bi2.setRGB(0, 0, w, h, pixels, 0, w);
	    return bi2;
	}
	
	
  //------- support methods ------------------
   private void init() {
	   // 400 x 400
	   int x=10, y=10;
	   int w = 80, w1=250, h=25, pad = 5;
	   JLabel typeLb =new JLabel("Image Type:");
	   typeLb.setBounds(x, y, w, h);
	   getContentPane().add(typeLb);
	   x+=w;
	   typeBox = new JComboBox<String>(imgTypes);
	   typeBox.setBounds(x, y, w, h);
	   typeBox.setActionCommand("chooseType");
	   typeBox.addActionListener(this);
	   imgType = "jpg";
	   getContentPane().add(typeBox);
	   x+=(w+5*pad);
	   JLabel widLb = new JLabel("Image Width:");
	   widLb.setBounds(x, y, w, h);
	   getContentPane().add(widLb);
	   x+=w;
	   NumberFormat format = NumberFormat.getInstance();
	   NumberFormatter formatter = new NumberFormatter(format);
	   formatter.setValueClass(Integer.class);
	   formatter.setMinimum(0);
	   formatter.setMaximum(5000);
	   formatter.setAllowsInvalid(false);
	   // If you want the value to be committed on each keystroke instead of focus lost
	   formatter.setCommitsOnValidEdit(true);
	   imgWidFd = new JFormattedTextField(formatter); //setValue and getValue
	   imgWidFd.setValue(1220);
	   imgWidFd.setBounds(x, y, w, h);
	   getContentPane().add(imgWidFd);
	   x+=(w+pad);
	   JLabel hgtLb = new JLabel("Image Height:");
	   hgtLb.setBounds(x, y, w, h);
	   getContentPane().add(hgtLb);
	   x+=w;
	   imgHgtFd = new JFormattedTextField(formatter);
	   imgHgtFd.setValue(560);
	   imgHgtFd.setBounds(x, y, w, h);
	   getContentPane().add(imgHgtFd);
	   
	   x=10; y+=h; w=130;
	   JLabel folderLb = new JLabel("File Locate Directory: ");		
	   folderLb.setBounds(x, y, w, h);
	   getContentPane().add(folderLb);
	   x+=w;
	   folderFd = new JTextField(homeFolder);
	   folderFd.setBounds(x, y, w1+45, h);
	   getContentPane().add(folderFd);
	   x+=(w1+45); w=80;
	   browserBtn = new JButton("Browser");
	   browserBtn.setBounds(x, y, w, h);
	   browserBtn.addActionListener(this);
	   getContentPane().add(browserBtn);
	   
	   x=10; y+=h; w=200;
	   JLabel nameLb = new JLabel("File Name (extension auto fixed): ");
	   nameLb.setBounds(x, y, w, h);
	   getContentPane().add(nameLb);
	   x+=w;
	   nameFd = new JTextField();
	   nameFd.setBounds(x, y, w1, h);
	   getContentPane().add(nameFd);
	   x+=w1; w=80;
	   extLb = new JLabel(".jpg");
	   extLb.setBounds(x, y, w, h);
	   getContentPane().add(extLb);
	   
	   y+=(h+10);
	   JSeparator jsp = new JSeparator();
	   jsp.setBounds(5, y, 530, 5);
	   getContentPane().add(jsp);
	   
	   x=200; w=80; y+=15;
	   saveBtn = new JButton("Save");
	   saveBtn.addActionListener(this);
	   saveBtn.setBounds(x, y, w, h);
	   getContentPane().add(saveBtn);
	   x+=(w+pad);
	   cancelBtn = new JButton("Cancel");
	   cancelBtn.setBounds(x, y, w, h);
	   cancelBtn.addActionListener(this);
	   getContentPane().add(cancelBtn);
	   	   
   }
   
   private void browserFolder() {
	   JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	   jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
	   int returnValue = jfc.showOpenDialog(null);	   
	   if (returnValue == JFileChooser.APPROVE_OPTION) {
		   File selectedFile = jfc.getSelectedFile();
		   String pt = selectedFile.getAbsolutePath();
		   folderFd.setText(pt);
	   }
   }
   
   private void saveImage() {
	   imgWid = (int) imgWidFd.getValue();
	   imgHgt = (int) imgHgtFd.getValue();
	   String dir = folderFd.getText();
	   String fnm = nameFd.getText();
	   if(fnm == null || fnm.length()<2) {
		   NoticeBox box2 = new NoticeBox("Please enter the file name without extension", NoticeBox.ERROR, 300, 200);
		   box2.showMsg("Empty File Name");
		   return; 
	   }
	   BufferedImage image = new BufferedImage(imgWid, imgHgt, BufferedImage.TYPE_INT_RGB);
	   Graphics2D g2 = image.createGraphics();
	   //paint(g2);
	   pane.printAll(g2);
	   try{
		   File imgf = new File(dir, fnm+"."+imgType);
		   //File imgf = new File(fnm+"."+imgType);
		   //String pth = imgf.getAbsolutePath();
		   //System.out.println(pth);
		   ImageIO.write(image, imgType, imgf);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   closeDlg();
   }
  
}
