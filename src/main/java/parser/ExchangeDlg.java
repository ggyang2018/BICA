package parser;
/**
 * Not in use, just as reference for future improvements
 * @author guang yang (Sunny)
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.util.Vector;

/**
 * Exchange Dialog 
 * Usage: 
 * 		ExchangeDlg dlg = new ExchangeDlg(this, "Selection Box", "About24.gif");
 		dlg.setDataPane(v1, v2);
 		dlg.setFavourBounds(510, 360);
 		dlg.displayDlg();
 		
 * @author guangyang
 *
 */
import javax.swing.*;

import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;
import tech.sunnykit.guikit.widget.XYLayout;

public class ExchangeDlg extends JDialog implements ActionListener
{
	@Serial
	static private final long  serialVersionUID = GUIWidgetAdaptor.serialVersionUID;
	
	int wid, hgt;

	JFrame frame;
	ExchangePane ctxPane;
	public ExchangeDlg (JFrame dlg, String title, String iconPath)
	{
		super(dlg);
		frame = dlg;
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
	}
		
	public void setDataPane(Vector<String> v1, Vector<String> v2)
	{
		ctxPane = new ExchangePane(v1, v2);
		ctxPane.setBounds(5, 5, 500, 350);
		ctxPane.setSubTitle();
		ctxPane.setA2Pane();
		getContentPane().add(ctxPane);
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
	
	public void displayDlg() {
		this.setVisible(true);
	}
	
	void closeDlg() {
		this.dispose();
		this.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	   //String cmd = e.getActionCommand();
	   		
	}


}
