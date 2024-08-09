package parser;
/**
 * Prototype of Exchange panel the parent class GExchange needs to be layout
 * @author guang yang (Sunny)
 */
import java.util.Vector;

import javax.swing.JDialog;

import tech.sunnykit.guikit.widget.GExchange;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;

public class ExchangePane extends GExchange 
{

	static private final long  serialVersionUID = GUIWidgetAdaptor.serialVersionUID;
	public ExchangePane(Vector<String> left, Vector<String> right) 
	{
		super(left, right);
		
	}

	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		
	}
	
	public void displayAsDialog() {
		JDialog dlg = new JDialog();
		dlg.getContentPane().add(this);
		dlg.setVisible(true);
	}

}
