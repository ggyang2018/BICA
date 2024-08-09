package parser;
/**
 * For Dialog for display information
 * @author Sunny Yang
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import tech.sunnykit.guikit.widget.GStyledTextPane;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;
import tech.sunnykit.guikit.widget.XYLayout;

public class AboutDlg extends JDialog implements ActionListener
{
	private static final long serialVersionUID = GUIWidgetAdaptor.serialVersionUID;

	GStyledTextPane txtPane;
	JButton closeBtn;
	int wid, hgt;
	
	String str1 ="Objectives of the software package is to automate the office work by generating both hall hiring contract form and invioces,";  
	
	String str2 = "It is a simple version of bespoke application only essential functions being implemented as a quick "
			+ "developed package. There may be further developments to many "
			+ "details and improvements, such as toolbar, context-drop-down menu, logging, embedded database, and so on.";
	
	String str3 = "It is dialog based desktop application rather than page based web application. Multiple frames can work together to make it "
			+ "intuitive, whereas page based application only show one page a time and using embedded link to other pages";
		
	String str4 ="Due to the fact that  we don't have privilege to access the site data directly, neither via "
			+ "VPN (virtual private network) nor webservice API. We can only download data as excel file as data source, "
			+ "therefore it has an offline standalone software.";   
	
	public AboutDlg(String iconPath) {
		setModal(true);
		setTitle("Features of LK Booking");
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
		
		txtPane = new GStyledTextPane();
		getContentPane().setLayout(new XYLayout());
		init();
	}
	
	public void setFavourBounds(int width, int height)
	{
		wid = width; hgt = height;
		Dimension dim = Toolkit.getDefaultToolkit( ).getScreenSize();
		float fx = (float)dim.getWidth( )/2;
		float fy = (float)dim.getHeight( )/2;
		int x = Math.round(fx - (float)(width/2));
		int y = Math.round(fy - (float)(height/2))-15;
		setBounds(x, y, width, height);
	}
	
	void init() {
		//600 x 330
		int x=10, y=10, w=580, h=250;
		JScrollPane jsp = new JScrollPane(txtPane);
		jsp.setBounds(x, y, w, h);
		getContentPane().add(jsp);
		
		x=480; y+=(10+h); h=25; w=100;
		closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		closeBtn.setBounds(x, y, w, h);
		getContentPane().add(closeBtn);
		
		//add information
		txtPane.append(str1+"\n\n", 1);
		txtPane.append(str2+"\n\n", 1);
		txtPane.append(str3+"\n\n", 1);
		txtPane.append(str4+"\n\n", 1);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
		setVisible(false);
		
	}
	
	

}
