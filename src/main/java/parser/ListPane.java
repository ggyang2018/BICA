package parser;
/**
import java.util.List;
/**
 * A list panel for select items for create contract of booking form 
 * @author guangyang
 */
import java.util.Vector;

import tech.sunnykit.guikit.widget.GList;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;
/**
 * Implementation of GList for doing select st
 * @author guangyang
 *
 */
import java.util.*;

public class ListPane extends GList
{
	private static final long serialVersionUID = GUIWidgetAdaptor.serialVersionUID;

	SelectListDlg parentDlg;
	
	public ListPane(SelectListDlg dlg, Vector<String> data) {
		super(data);
		parentDlg = dlg;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect() {
		parentDlg.workingTitle = this.getSelectedValue();
		List<MapRow> lst = parentDlg.titleGroup.get(parentDlg.workingTitle);
		if(lst == null) return;
		StringBuffer sbf = new StringBuffer();
		for(int i=0; i<lst.size(); i++)
		{		
			MapRow mr = lst.get(i);
			sbf.append(mr.displayString());
			sbf.append("\n");
		}
		parentDlg.detailTxt.setText(sbf.toString());
		parentDlg.selectIndex = this.getSelectedIndex();
		parentDlg.pdfBtn.setEnabled(true);
	}

	@Override
	public void doAction(String cmd) {
		// TODO Auto-generated method stub
		
	}

}
