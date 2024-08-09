package parser;
import java.awt.Color;
/**
 * Image panel for display image
 * beware:  ImageIO is not for animated GIF 
 * @author guang yang (Sunny)
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import tech.sunnykit.guikit.gui.MainGuiFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = MainGuiFrame.serialVersionUID;
	private static final Logger logger = LogManager.getLogger(ImagePanel.class);
	
	BufferedImage image;
	Image img;
	Color navyBlue;
	
	int offsetX, offsetY;

    public ImagePanel() {
   	
    	//setBackground(Color.BLUE.brighter().brighter());
    	navyBlue = new Color(0, 0, 52);
    	setBackground(navyBlue.brighter().brighter());
    	offsetX=20;
    	offsetY=0;
    	
       try {                
    	   loadImage("libertonkirk.jpg");
    	   //img = Toolkit.getDefaultToolkit().createImage("res/libertonkirk.jpg"); 	             
       } catch (Exception ex) 
       { 
    	   ex.printStackTrace();
    	   logger.debug("{}", ex);
       }
    }
  
    void loadImage(String filename) throws IOException 
    {
    	try
    	{ 
    		if(!filename.startsWith("/")) filename ="/"+filename;
			image = ImageIO.read(getClass().getResource(filename)); 		
    	}catch(Exception ex)
    	{  //as file
    		this.image = ImageIO.read(new File(filename));   		 
    	}
    	this.repaint(); //must call for buffered image 
   }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
             g.drawImage(image, offsetX, offsetY, getWidth()-offsetX, getHeight(), 
            		 	 0, 0, image.getWidth(), image.getHeight(), navyBlue, this);
       
        }
    }

}