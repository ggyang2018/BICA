package parser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import tech.sunnykit.guikit.widget.XYLayout;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NoticeBox extends JDialog
{
	private static final long serialVersionUID = 1L;
	static public int WARNING = 1;
	static public int ERROR   = 2;
	static public int INFORMATION = 3;
	static public int SUCCESS = 4;

	String  Msg;
	String  infoImg = "/info.png";
	String  errImg = "/Stop24.gif";
	String  sucImg = "/yes.png";
	int     Type;	
	//position 
	
	JButton  OKBtn;
	
	public NoticeBox(String msg, int type, int wid, int ht)
	{
		getContentPane().setLayout(new XYLayout());
		Msg = msg;
		
		Type = type;

		setFavourBounds(wid,  ht);
		String t;
		//set panel here.
		if(Type == 1)
		   t = "Warning Message";
		else if(Type==2)
		   t= "Error Message";
		else 
		   t= "Information";

		JLabel lb = new JLabel(t, JLabel.CENTER);
		lb.setFont(new Font("Dialog", Font.BOLD+Font.ITALIC, 16));
		lb.setBounds(50, 5, wid-80, 20);
		getContentPane().add(lb);

		JButton btn = getImgBtn( );
		btn.setBounds(20, 3, 35, 35);
		getContentPane().add(btn);

	   
		JTextArea ar = new JTextArea();
		JScrollPane scl = new JScrollPane(ar);
		scl.setBounds(10, 35, wid-30, ht-105);
		ar.setBackground(getContentPane().getBackground());
		Border bd //= BorderFactory.createEmptyBorder();
			= BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		 //= BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
                                        //Color highlight,
                                     //Color shadow)
		ar.setBorder(bd);
		ar.setLineWrap(true);
        ar.setWrapStyleWord(true);
		ar.setFont(new Font("Dialog", Font.BOLD, 12));
        ar.setText(msg);
		//ar.setBounds(10, 35, wid-30, ht-100);
		getContentPane().add(scl);


      	OKBtn = new JButton("OK");
		int x = Math.round(wid - (float)(wid/2))-45;
		int y = ht - 65;
		OKBtn.setBounds(x, y, 100, 23);
		getContentPane().add(OKBtn);
		OKBtn.addActionListener(new ActionListener()
		{ 
			 public void actionPerformed(ActionEvent ev)
			 {  
				 setVisible(false);
			     dispose();
			 }
		 });
		
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

	//beware in jar using getResource, but in loose class using file. 
	public JButton getImgBtn( )
	{
		JButton btn = new JButton();
		
		Image iconImg = null;
		
		String Img = infoImg;
		if(Type==ERROR) Img = errImg;
		else if(Type==SUCCESS) Img = this.sucImg;
		
		URL imageURL = this.getClass().getResource(Img);			
	    if(imageURL !=null)
	    	iconImg = Toolkit.getDefaultToolkit ().getImage (imageURL);
	    else {//not in jar but in file
	    	try
	    	{
	    		iconImg = ImageIO.read(new File(Img));
	    	}catch(IOException iex) {
	    		iex.printStackTrace();
	    		return btn;
	    	}
	    }
      Image img=iconImg.getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING); 	 
	  //setIconImage(Img);		
	  ImageIcon 	BtnIcon = new ImageIcon (img);
	  btn.setIcon (BtnIcon);
	  //Border bd= BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	  Border bd = BorderFactory.createEmptyBorder();
	  btn.setBorder (bd);
	  
	  return btn;
	}

    public void showMsg(String tit)
	{
		this.setTitle(tit);
	    this.setModal(true);
        this.setVisible(true);
	}

	public void showMsg(String tit, boolean mod)
	{
		this.setTitle(tit);
	    this.setModal(mod);
        this.setVisible(true);
	}

  //test driver
 /* static public void main(String args[])
  {
	  String s =" position private int Wid, Hei; JButton OKBtn"
		        +" position private int Wid, Hei; JButton OKBtn"
				+" position private int Wid, Hei; JButton OKBtn"
				+" position private int Wid, Hei; JButton OKBtn"
			    +" position private int Wid, Hei; JButton OKBtn"
				+" position private int Wid, Hei; JButton OKBtn"
				+" position private int Wid, Hei; JButton OKBtn"; 
	  MessageBox bx = new MessageBox(s, 1, 250, 200);
	  bx.showMsg("Message Box");
  }*/
}
	
