package parser;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
/**
 * Running and testing as application entry point
 * @author guang yang (sunny)
 */
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.*;

//set logger
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Runner {
	
	private static final Logger logger = LogManager.getLogger(Runner.class);
	
	static void displayFrame()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() 
			{
				ParserFrame frm = new ParserFrame();
				frm.setVisible(true);
				frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}
	
	//new running style using lambda
	static void runFrame() {
		EventQueue.invokeLater(() -> {
			ParserFrame frm = new ParserFrame();
			frm.setVisible(true);
			frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			logger.debug("running component as ParserFrame");
        });
	}
	
	static void testing() {
		int dom = 578;
		int div = 321;  //beware be round up
		double rest =100.0 * div/dom;
		System.out.println(rest);
		
		String str = String.format("%2.02f", rest);
		System.out.println(str+"%");
	}
	
	static void testImage() {
		try {
			BufferedImage input = ImageIO.read(new File("input.png"));
			  System.out.println("input image type=" + input.getType());
			  int width = input.getWidth();
			  int height = input.getHeight();
			  BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			  int px[] = new int[width * height];
			  input.getRGB(0, 0, width, height, px, 0, width);
			  output.setRGB(0, 0, width, height, px, 0, width);
			  ImageIO.write(output, "jpg", new File("output.jpg"));
		}catch(Exception ex) { ex.printStackTrace(); }
	}
		
	static public void main(String args[])
	{
		Date start = new Date();
		String startStr = "----- start: "+start.toString()+"---------";
		System.out.println(startStr);
		logger.debug(startStr);
		try
		{
			runFrame();
			//testing();
		}catch(Exception ex) 
		{ 
			ex.printStackTrace();
			logger.error("{}", ex);
		}
	}
}

