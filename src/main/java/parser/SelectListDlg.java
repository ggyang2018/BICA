package parser;
/**
 * A dialgo for selection of group to generate contract in PDF. 
 * A requirement, file name need to be import by user
 * @author guangyang
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import persistent.*;

/**
 * Dialog for select item from list and show detals, then select for generated contract form in pdf format
 * @author guangyang
 */
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import tech.sunnykit.guikit.gui.JTextFieldRegularPopupMenu;
import tech.sunnykit.guikit.util.IdentityKey;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;
import tech.sunnykit.guikit.widget.XYLayout;

public class SelectListDlg extends JDialog implements ActionListener
{
	private static final long serialVersionUID = GUIWidgetAdaptor.serialVersionUID;
	
	int wid, hgt;
	ParserFrame frame;
	//component
	ListPane listPane;
	JTextArea detailTxt;
	JTextField nameFd, nbrFd;
	JButton pdfBtn, closeBtn;
	JCheckBox videoCheck, audeoCheck, tableCheck, kitchenCheck;
	JFormattedTextField speakerNbrFd;
	JTextArea noteTxt;

	Map<String, List<MapRow>> titleGroup;
	String workingTitle = null;
	int selectIndex = -1;
	
	
	public SelectListDlg(ParserFrame dlg, String title, String iconPath)
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
	
	//-- implement panel
	void setDataPane(Map<String, List<MapRow>> group)
	{
		titleGroup = group;
		Vector<String> data_v = new Vector<String>(group.keySet());
		// 560 x 400
		int x=10, y=10;
		int w = 360, h=25;
		JLabel titLb = new JLabel("Title List for Selection", SwingConstants.CENTER);		
		titLb.setBounds(x, y, w, h);
		getContentPane().add(titLb);
		y+=h; h=200;			
		listPane = new ListPane(this, data_v);
		
		JScrollPane js = listPane.createScrollPane();
		js.setBounds(x, y, w, h);
		getContentPane().add(js);
		
		x=370; y=30; w=160; h=20;
		JLabel reqLb = new JLabel("Requirements", SwingConstants.CENTER);
		reqLb.setBounds(x, y, w, h);
		getContentPane().add(reqLb);
		y+=h;
		videoCheck = new JCheckBox("Video Project/Screen", false);
		videoCheck.setBounds(x, y, w, h);
		getContentPane().add(videoCheck);
		y+=h;
		kitchenCheck = new JCheckBox("Kitchen Access", false);
		kitchenCheck.setBounds(x, y, w, h);
		getContentPane().add(kitchenCheck);
		y+=h;
		tableCheck = new JCheckBox("Tables and Chairs", false);
		tableCheck.setBounds(x, y, w, h);
		getContentPane().add(tableCheck);
		y+=h;
		audeoCheck = new JCheckBox("Public Address System", false);
		audeoCheck.setBounds(x, y, w, h);
		getContentPane().add(audeoCheck);
		y+=h;
		NumberFormat numb_fmt = NumberFormat.getIntegerInstance();
		numb_fmt.setGroupingUsed(false);
		NumberFormatter nbr_fmt = new NumberFormatter(numb_fmt){
				@Override
				public Object stringToValue(String text) throws ParseException {
					if (text.length() == 0)
						return null;
					return super.stringToValue(text);
				}
		};
		nbr_fmt.setValueClass(Integer.class);
		nbr_fmt.setMinimum(0);
		nbr_fmt.setMaximum(20);
		nbr_fmt.setAllowsInvalid(false);
		// If you want the value to be committed on each keystroke instead of focus lost
		//formatter.setCommitsOnValidEdit(true);
		speakerNbrFd = new JFormattedTextField(nbr_fmt);
		speakerNbrFd.setValue(1);
		speakerNbrFd.setBounds(x, y, 30, h+5);
		getContentPane().add(speakerNbrFd);
		JLabel speakLb = new JLabel("Microphones", JLabel.LEFT);
		speakLb.setBounds(x+35,y, w-35, h);
		getContentPane().add(speakLb);

		x=400; w=100; h=25; y=205; 	
		pdfBtn = new JButton("Create PDF");
		pdfBtn.addActionListener(this);
		pdfBtn.setBounds(x, y, w, h);
		getContentPane().add(pdfBtn);
		pdfBtn.setEnabled(false);
		y+=h+5;
		closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		closeBtn.setBounds(x, y, w, h);
		getContentPane().add(closeBtn);
				
		x=10; y=235; h = 25; w=80; 
		JLabel flb = new JLabel("File Name:");
		flb.setBounds(x, y, w, h);
		getContentPane().add(flb);
		x=x+w; w=280;
		String ikey = IdentityKey.getDateKey("con", 1);
		nameFd = new JTextField(ikey+".pdf");
		nameFd.setBounds(x, y, w, h);
		getContentPane().add(nameFd);
		JTextFieldRegularPopupMenu.addTo(nameFd);
		x=10; w=80; y+=h;
		JLabel inlb = new JLabel("Invoice No:");
		inlb.setBounds(x, y, w, h);
		getContentPane().add(inlb);
		x+=w; w=150;
		nbrFd = new JTextField();
		nbrFd.setBounds(x, y, w, h);
		JTextFieldRegularPopupMenu.addTo(nbrFd);
		getContentPane().add(nbrFd);
		
		x=10; y+=h; h=70; w=560;
		detailTxt = new JTextArea();
		detailTxt.setEnabled(false);//not editable
		detailTxt.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(detailTxt);
		jsp.setBounds(x, y, w, h);
		getContentPane().add(jsp);
		y+=h; h=20;
		JLabel noteLb = new JLabel("Additional Administration Note ", JLabel.CENTER);
		noteLb.setBounds(x, y, w, h);
		getContentPane().add(noteLb);
		y+=h; h = 60;
		noteTxt = new JTextArea();
		noteTxt.setBounds(x, y, w, h);
		getContentPane().add(noteTxt);
												
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Close")) closeDlg();				
		else if(cmd.equals("Create PDF")) processPdf();
				
	}
	
	void processPdf() {
		if(workingTitle == null) return;
		try 
		{
			String file_name = nameFd.getText();
			if(file_name==null || file_name.trim().length()<2)
			{
				String tit = "Warning to Produce Contract";	
				NoticeBox box = new NoticeBox("Please add the pdf file name for processing", NoticeBox.WARNING, 300, 200);
				box.showMsg(tit);
				return;
			}
			
		    String file_path = frame.pdfMaker.checkFile(file_name);
		    if(file_path == null)
		    {
		    	String tit = "Warning to Produce Contract";	
				NoticeBox box = new NoticeBox("The file exists already, please change another name", NoticeBox.ERROR, 300, 200);
				box.showMsg(tit);
				return;
		    }
			String invNbr = nbrFd.getText();
			if(invNbr==null || invNbr.isEmpty()) {
				String msg1 = "Invoice Number cannot empty, please input";
				JOptionPane.showMessageDialog(null,
						msg1, "Empty Invoice Number", JOptionPane.ERROR_MESSAGE);
				return;
			}
		    List<MapRow> lstx1 = titleGroup.get(workingTitle);
			if(lstx1 == null || lstx1.size()<1) return;
		    
			MediaData md = createMediaData(lstx1);
			md.setBookType(AppConstants.CONTRACT_TYPE);
			md.setInvoiceNo(invNbr);
			if(!md.checkEmpty())
			{
				String msg = null;
				frame.pdfMaker.makeContract(md, file_path);
				NoticeBox box1 = new NoticeBox("Successful create PDF: " + file_path,
												NoticeBox.SUCCESS, 300, 200);
				BookData bkData = new BookData();
				bkData.transfer(md);
				bkData.setBookType(AppConstants.CONTRACT_TYPE);
				bkData.setBookStatus(AppConstants.CONTRACT_DONE_STATUS);
				bkData.setFileNames(file_name);
				bkData.setBookNote(noteTxt.getText());
				if(frame.bookStore.storeBooking(bkData))
					msg = "Success Create Contract PDF file and store in DB ";
				else msg = "Success Create Contract PDF file";
				box1.showMsg(msg);
			}else {
				NoticeBox box2 = new NoticeBox("Something wrong to create PDF: "+file_path+". Please contact Admin", NoticeBox.ERROR, 300, 200);
				box2.showMsg("Fail to create contract pdf file");
			}
			//listPane.updateUI();			
		}catch(Exception ex) { ex.printStackTrace(); }
	}
	
	MediaData createMediaData(List<MapRow> list) {		
		MediaData md = new MediaData();
		md.extractData(list);
		if (videoCheck.isSelected())
			md.addRequire("V. P.");
		if(audeoCheck.isSelected()) {
			String nbr_str = "1";
			if (speakerNbrFd.getValue() != null)
				nbr_str = speakerNbrFd.getValue().toString();
			md.addRequire("P. A. with " +nbr_str + " Mic");
		}if(tableCheck.isSelected())
			md.addRequire("Tables/Chairs");
		if(kitchenCheck.isSelected())
			md.addRequire("Kitchen Access");
		return md;
	}
}
