package parser;
/**
 * Work with frame to display data
 * Calculation Methods: countTime and countPrice
 * Count time: duration x spaces count, if anderson hall should be weight 1.2
 * Count price: sum of paid/unpaid price. 
 * @author guangyang
 */

import persistent.BookData;
import tech.sunnykit.guikit.gui.MainGuiFrame;
import tech.sunnykit.guikit.widget.GTabbedPane;
import tech.sunnykit.guikit.widget.PanelAdapter;
import tech.sunnykit.guikit.widget.XYLayout;

//import guikit.widget.GStyledTextPane;
import tech.sunnykit.guikit.widget.GTableModel;
import tech.sunnykit.guikit.widget.GTablePane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import java.awt.Color;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class ParserPane extends JPanel implements PanelAdapter
{
	private static final long serialVersionUID = MainGuiFrame.serialVersionUID;
	
	ParserFrame frm;
	GTabbedPane tabPane;
	GTablePane dataPane;
	ImagePanel welcomePane;
	ReportPane reportPane;

	GTabbedPane recordPane;
	GTablePane bookingPane;
	GTablePane invoicePane;

	int wid,  hgt;
	//as reference
	ArrayList<String> tableHeaders;
	List<List<String>> tableData;

	//Book recordings --- as transit object to hold retrieve bookings
	Map<String, BookData> bookRecords, invoiceRecords;
	List<List<String>> bookTableData, invoiceTableData;
	List<String> bookTableHeader, invoiceTableHeader;

	Font tabFont = new Font("Arial", Font.CENTER_BASELINE, 14);
	Font tabFont2 = new Font("Arial", Font.CENTER_BASELINE, 12);

	public ParserPane(ParserFrame frm, int wid, int hgt)
	{
		this.frm = frm;
		//setBackground(Color.MAGENTA);
		this.wid = wid; this.hgt=hgt;
		UIManager.put("TabbedPane.selected", Color.red);
		init();
	}

	void init()
	{
		setLayout(new XYLayout());
		tabPane = new GTabbedPane();
		tabPane.setFont(tabFont);
		welcomePane = new ImagePanel();
		dataPane = new GTablePane(1);
		reportPane = new ReportPane();

		recordPane = new GTabbedPane();
		recordPane.setFont(tabFont2);
		bookingPane = new GTablePane(2);
		invoicePane = new GTablePane(3);
		recordPane.add("Bookings", bookingPane);
		recordPane.setBackgroundAt(0, Color.GREEN);
		recordPane.add("Invoices", invoicePane);
		recordPane.setBackgroundAt(1, Color.MAGENTA);
		renderBookData();
		renderInvoiceData();
		recordPane.addChangeListener(e -> {
            if (e.getSource() instanceof GTabbedPane) {
                GTabbedPane pane = (GTabbedPane) e.getSource();
                int paneIdx = pane.getSelectedIndex();
                if(paneIdx == 0 && invoicePane.getMainTable()!=null)
                    invoicePane.getMainTable().clearSelection();
                else if(paneIdx==1 && bookingPane.getMainTable()!=null)
                    bookingPane.getMainTable().clearSelection();
            }
        });

		int x=5, y=5;
		tabPane.setBounds(x, y, wid-2*x, hgt-2*y);
		dataPane.setBounds(x, y, wid-2*x, hgt-2*y);
		tabPane.add("Welcome", welcomePane);
		tabPane.add("Working", dataPane);
		tabPane.add("Recordings", recordPane);
		tabPane.add("Reporting", reportPane);
		add(tabPane);
	}

	public void renderData(List<List<String>> tdata, List<String> theaders)
	{
	    GTableModel tm = new GTableModel(tdata, theaders);
	    List<Integer> csz = new ArrayList<Integer>();
		csz.add(150);//start date
		csz.add(150);//end date
		csz.add(80);//duration
		csz.add(80); //space count 
		csz.add(200); //title
		csz.add(50);//price
		csz.add(80); //payment status
		csz.add(100);//first namme
		csz.add(100);//last name
		csz.add(200);//organisation
		csz.add(150); //telephone
		csz.add(150); //email
		csz.add(150); //space
		csz.add(100);  //category
		csz.add(100); //for row id
		tm.setColumnSizes(csz);
		dataPane.setMainTable(tm, false);
		dataPane.updateUI();
		tabPane.setSelectedIndex(1);				
	}

	@Override
	public void doAction(int componentId, Object row) {
		System.out.println("doction: "+componentId);
		
	}

	@Override
	public void doAction(int commentId, String key, String value) {
		System.out.println("doction: "+commentId+", Key:"+key);
		
	}
	
	public boolean hasLoadData() {
		boolean fg = false;
		if(dataPane.getDataModel()!=null)
			fg = dataPane.getDataModel().getRowCount()>0;
		return fg;
	}
	
	//remove church meeting becuase data not significant, it is include others
	public void makePieChart(ArrayList<MapRow> all_list) {
		//category counter and group
		List<MapRow> privateBook = new ArrayList<MapRow>(); 
		List<MapRow> commercialBook = new ArrayList<MapRow>();  
		List<MapRow> uniformBook = new ArrayList<MapRow>();
		List<MapRow> churchBook = new ArrayList<MapRow>();
		List<MapRow> youngBook =  new ArrayList<MapRow>();
		List<MapRow> othersBook = new ArrayList<MapRow>();
				
		for (MapRow rw : all_list) {
			String cat = rw.getCategory();
			if(cat.equals(AppConstants.PRIVATE_GROUP))
				privateBook.add(rw);
			else if(cat.equals( AppConstants.COMMERCIAL_GROUP))
				commercialBook.add(rw);
			else if(cat.equals( AppConstants.UNIFORM_GROUP))
				uniformBook.add(rw);
			else if(cat.equals(AppConstants.CHURCH_GROUP)) {
				String tit = rw.getDataValue(AppConstants.TITLE_FLD);
				String org = rw.getDataValue(AppConstants.ORG_FLD);
				if(!org.contains("Cafe Manager") && !tit.equalsIgnoreCase("Kirkgate Cafe"))
					churchBook.add(rw);
			}else if(cat.equals(AppConstants.YOUNG_LIFE_GROUP))
				youngBook.add(rw);
			else 
				othersBook.add(rw);	
		}
		//time count in minutes
		int pvt_time = countTime(privateBook);
		int com_time = countTime(commercialBook);
		int uni_time = countTime(uniformBook);
		int chu_time = countTime(churchBook);
		int you_time = countTime(youngBook);
		int oth_time = countTime(othersBook);
		int sum_time = pvt_time+com_time+uni_time+chu_time+you_time+oth_time;

		Map<String, Integer> time_mp = new LinkedHashMap<String, Integer>();	
		time_mp.put(makePercentage(AppConstants.PRIVATE_GROUP, pvt_time, sum_time), pvt_time);
		time_mp.put(makePercentage(AppConstants.COMMERCIAL_GROUP, com_time, sum_time), com_time);
		time_mp.put(makePercentage(AppConstants.UNIFORM_GROUP, uni_time, sum_time), uni_time);
		time_mp.put(makePercentage(AppConstants.CHURCH_GROUP, chu_time, sum_time), chu_time);
		//time_mp.put(makePercentage(AppConstants.CHURCH_ME_GROUP, chum_time, sum_time), chum_time);
		time_mp.put(makePercentage(AppConstants.YOUNG_LIFE_GROUP, you_time, sum_time), you_time);
		time_mp.put(makePercentage(AppConstants.OTHER_GROUP, oth_time, sum_time), oth_time);
		//price count in pounds
		float pvt_pay = countPrice(privateBook);
		float com_pay = countPrice(commercialBook);
		float uni_pay = countPrice(uniformBook);
		float chu_pay = countPrice(churchBook);
		//float chum_pay = countPrice(churchMeBook);
		float you_pay = countPrice(youngBook);
		float oth_pay = countPrice(othersBook);
		float sum_pay = pvt_pay+com_pay+uni_pay+chu_pay+you_pay+oth_pay;

		Map<String, Float> pay_mp = new LinkedHashMap<String, Float>();
		pay_mp.put(makePercentage(AppConstants.PRIVATE_GROUP, pvt_pay, sum_pay), pvt_pay);
		pay_mp.put(makePercentage(AppConstants.COMMERCIAL_GROUP,com_pay, sum_pay), com_pay);
		pay_mp.put(makePercentage(AppConstants.UNIFORM_GROUP, uni_pay, sum_pay), uni_pay);
		pay_mp.put(makePercentage(AppConstants.CHURCH_GROUP, chu_pay, sum_pay), chu_pay);
		pay_mp.put(makePercentage(AppConstants.YOUNG_LIFE_GROUP, you_pay, sum_pay), you_pay);
		pay_mp.put(makePercentage(AppConstants.OTHER_GROUP, oth_pay, sum_pay), oth_pay);
		reportPane.setTotalTime(sum_time);
		reportPane.setTotalPay(sum_pay);
		reportPane.composePieChart(time_mp, pay_mp);
		tabPane.setSelectedIndex(3);
	}
	
	private int countTime(List<MapRow> dat) {
		int rtn = 0;		
		for (MapRow rw : dat) {
			String dur = rw.getDataValue(AppConstants.DURATION_FLD);
			String space_cnt = rw.getDataValue(AppConstants.COUNT_FLD);
			String space = rw.getDataValue(AppConstants.SPACE_FLD).toLowerCase();
			try {
				float m1 = Float.parseFloat(dur);
				if(space.contains("anderson hall")) m1 = (m1 * 1.5f); //add weight to 
				int mx = Math.round(m1);
				if(space.contains("anderson hall"));
				float c1 = Float.parseFloat(space_cnt);
				int mc = Math.round(c1);
				if(mc >9) mc = 9; //as data bluncder
				rtn += (mx * mc);
			}catch(Exception ex) { ex.printStackTrace(); }
		}				
		return rtn;		
	}
	
	private float countPrice(List<MapRow> dat) {
		float rtn = 0.0f;
		for (MapRow rw : dat) {
			String pay = rw.getDataValue(AppConstants.PRICE_FLD);
			try {
				float m1 = Float.parseFloat(pay);
				rtn += m1;
			}catch(Exception ex) { ex.printStackTrace(); }
		}
		return rtn;
	}
	
	private String makePercentage(String name, int div, int dom) {
		if(div <= 30) return name+"=0.0%"; //less 30 miniutes not count. 				
		
		double rest =100.0 * div/dom;
		String str = String.format("%2.02f", rest);
		return name+"="+str+"%";
	}
	
	private String makePercentage(String name, float div, float dom) {
		if(div <=1) return name+"=0.0%"; //less one pound not count 
		
		double rest =100.0 * div/dom;
		String str = String.format("%2.02f", rest);
		return name+"="+str+"%";
	}
	
	//debug information
	void checkRowInfo(int rowIndex) {		
		System.out.println("*****************************");
		System.out.println(tableHeaders);
		System.out.println("-------------------------------------------------");
		List<String> rw = tableData.get(rowIndex);
		System.out.println(rw);
		System.out.println("*****************************");
		System.out.println(tableHeaders.size()+", data size:"+tableData.get(rowIndex).size()+", original size:");				
	}
	
	void checkIntegerValues(String title, int pvt, int com, int uni, int chu, int chm, int you, int oth ) {
		String values = String.format("row: prv=%d, com=%d, uni=%d, chu=%d, chm=%d, you=%d, oth=%d", 
										pvt, com, uni, chu, chm, you, oth);
	    System.out.println(title+": "+values);
	}
	
	void checkFloatValues(String title, float pvt, float com, float uni, float chu, float chm, float you, float oth ) {
		String values = String.format("row: prv=%.02f, com=%.02f, uni=%.02f, chu=%.02f, chm=%.02f, you=%.02f, oth=%.02f", 
										pvt, com, uni, chu, chm, you, oth);
	    System.out.println(title+": "+values);
	}

	//-------- persistent support ---------------------
	void renderBookData()
	{
		bookRecords = frm.bookStore.retrieveBookings();
		if(bookRecords == null || bookRecords.isEmpty()) return;
		bookTableData = new ArrayList<>();
		bookTableHeader = null;
		for(Map.Entry<String, BookData> entry : bookRecords.entrySet())
		{
			BookData bkData = entry.getValue();
			bookTableData.add(bkData.getTableData());
			if(bookTableHeader == null)
				bookTableHeader = bkData.getTableHeader();
		}
		GTableModel tm = new GTableModel(bookTableData, bookTableHeader);
		List<Integer> csz = new ArrayList<>();
		csz.add(160);//file name
		csz.add(100);// invoice number
		csz.add(160); //client name
		csz.add(200); //event title
		csz.add(200);//client email
		csz.add(160); //client telephone
		csz.add(200);//organisation
		csz.add(100);//book statue
		csz.add(200);//book note
		csz.add(100); //create time
		csz.add(150); //actionId
		tm.setColumnSizes(csz);
		bookingPane.setMainTable(tm, false);
		bookingPane.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookingPane.getMainTable().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt)
			{
				super.mousePressed(evt);
				int clickCnt = evt.getClickCount();
				if (clickCnt == 2)
				{
					int row = bookingPane.getMainTable().rowAtPoint(evt.getPoint());
					int col = bookingPane.getMainTable().columnAtPoint(evt.getPoint());
					if (col == 0) {
						String aid = (String)bookingPane.getMainTable().getModel().getValueAt(row, 10);
						BookData bd = bookRecords.get(aid);
						openPdfApp(bd);
					}
				}
			}
		});
		bookingPane.updateUI();
	}
	void renderBookDetails(){
		int row_idx = bookingPane.getMainTable().getSelectedRow();
		if(row_idx <0) return; //no selection
		String aid = (String)bookingPane.getMainTable().getModel().getValueAt(row_idx, 10);
		BookData bd = bookRecords.get(aid);
		if(bd==null) return;
		//if could be retrieved before.
		List<String> time_lst = bd.getTimeList();
		if(time_lst == null || time_lst.isEmpty())
			frm.bookStore.retrieveBookDetail(bd);
		BookDetailDlg dlg = new BookDetailDlg(frm, bd);
		dlg.setFavourBounds(650, 500);
		dlg.displayDlg();
	}

	void renderInvoiceData()
	{
		invoiceRecords = frm.bookStore.retrieveInvoices();
		if(invoiceRecords == null || invoiceRecords.isEmpty()) return;
		invoiceTableData = new ArrayList<>();
		invoiceTableHeader = null;
		for(Map.Entry<String, BookData> entry : invoiceRecords.entrySet())
		{
			BookData bkData = entry.getValue();
			invoiceTableData.add(bkData.getInvoiceTableData());
			if(invoiceTableHeader == null)
				invoiceTableHeader = bkData.getInvoiceHeader();
		}
		GTableModel tm = new GTableModel(invoiceTableData, invoiceTableHeader);
		List<Integer> csz = new ArrayList<>();
		csz.add(160);//file name
		csz.add(160); //book file name
		csz.add(100);// invoice number
		csz.add(100); //purchase order
		csz.add(100); //pay status
		csz.add(160); //client name
		csz.add(200); //event title
		csz.add(200);//client email
		csz.add(160); //client telephone
		csz.add(200);//organisation
		csz.add(200);//book note
		csz.add(100); //create time
		csz.add(150); //actionId
		tm.setColumnSizes(csz);
		invoicePane.setMainTable(tm, false);
		invoicePane.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		invoicePane.getMainTable().addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt)
			{
				super.mousePressed(evt);
				int clickCnt = evt.getClickCount();
				if (clickCnt == 2)
				{
					int row = invoicePane.getMainTable().rowAtPoint(evt.getPoint());
					int col = invoicePane.getMainTable().columnAtPoint(evt.getPoint());
					if (col == 0) {
						String 	aid = (String)invoicePane.getMainTable().getModel().getValueAt(row, 12);
						BookData bd = invoiceRecords.get(aid);
						openPdfApp(bd);
					}
				}
			}
		});
		invoicePane.updateUI();
	}
	void renderInvoiceDetails(){
		int row_idx = invoicePane.getMainTable().getSelectedRow();
		if(row_idx <0) return; //no selection
		String 	aid = (String)invoicePane.getMainTable().getModel().getValueAt(row_idx, 12);
		BookData bd = invoiceRecords.get(aid);
		if(bd==null) return;
		//if could be retrieved before.
		List<String> time_lst = bd.getTimeList();
		if(time_lst == null || time_lst.isEmpty())
			frm.bookStore.retrieveBookDetail(bd);
		BookDetailDlg dlg = new BookDetailDlg(frm, bd);
		dlg.setFavourBounds(650, 500);
		dlg.displayDlg();
	}

	void viewBookContents(){
		int row_idx = bookingPane.getMainTable().getSelectedRow();
		if(row_idx <0) return; //no selection
		String aid = (String)bookingPane.getMainTable().getModel().getValueAt(row_idx, 10);
		BookData bd = bookRecords.get(aid);
		if(bd !=null) {
			List<String> time_lst = bd.getTimeList();
			if(time_lst == null || time_lst.isEmpty())
				frm.bookStore.retrieveBookDetail(bd);
			JOptionPane.showMessageDialog(null, bd.toString(),
					"Booking Contents", JOptionPane.PLAIN_MESSAGE);
		}else
			JOptionPane.showMessageDialog(null, "No data selected",
					"Booking Contents", JOptionPane.ERROR_MESSAGE);
	}
	void viewInvoiceContents() {
		int row_idx = invoicePane.getMainTable().getSelectedRow();
		if(row_idx <0) return; //no selection
		String 	aid = (String)invoicePane.getMainTable().getModel().getValueAt(row_idx, 12);
		BookData bd = invoiceRecords.get(aid);
		if(bd !=null) {
			List<String> time_lst = bd.getTimeList();
			if(time_lst == null || time_lst.isEmpty())
				frm.bookStore.retrieveBookDetail(bd);
			JOptionPane.showMessageDialog(null, bd.toString(),
					"Invoice Contents", JOptionPane.PLAIN_MESSAGE);
		}else
			JOptionPane.showMessageDialog(null, "No record selected",
					"Invoice Contents", JOptionPane.ERROR_MESSAGE);
	}

	private void openPdfApp(BookData bd)
	{
		if(bd==null) return;
		String fnm = bd.getFileNames();
		String filePath = frm.pdfMaker.findStoredFilePath(fnm);
		//System.out.println("File path="+filePath);
		if(filePath !=null)
		{
			if (Desktop.isDesktopSupported()) {
				try{
					File pf = new File(filePath);
					Desktop.getDesktop().open(pf);
				}catch(Exception ex) { ex.printStackTrace();}
			}
		}else {
			String errMsg = filePath + " doesn't exist in file store";
			JOptionPane.showMessageDialog(null, errMsg,
					"PDF Generator Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	//sorting book and invoices records
	void sortBookTitle(boolean isAscend){
		if(bookTableData == null || bookTableData.isEmpty()) return;
		Collections.sort(bookTableData, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscend) return o1.get(3).compareTo(o2.get(3));
				else return o2.get(3).compareTo(o1.get(3));
			}
		});
		showBookTableData(bookTableData, bookTableHeader);
	}
	void sortBookInvNumber(boolean isAscend){
		if(bookTableData == null || bookTableData.isEmpty()) return;
		Collections.sort(bookTableData, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscend) return o1.get(1).compareTo(o2.get(1));
				else return o2.get(1).compareTo(o1.get(1));
			}
		});
		showBookTableData(bookTableData, bookTableHeader);
	}
	void sortInvoiceTitle(boolean isAscend) {
		if(invoiceTableData == null || invoiceTableData.isEmpty()) return;
		Collections.sort(invoiceTableData, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscend) return o1.get(6).compareTo(o2.get(6));
				else return o2.get(6).compareTo(o1.get(6));
			}
		});
		showInvoiceTableData(invoiceTableData, invoiceTableHeader);
	}
	void sortInvoiceInvNumber(boolean isAscend) {
		if(invoiceTableData == null || invoiceTableData.isEmpty()) return;
		Collections.sort(invoiceTableData, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscend) return o1.get(2).compareTo(o2.get(2));
				else return o2.get(2).compareTo(o1.get(2));
			}
		});
		showInvoiceTableData(invoiceTableData, invoiceTableHeader);
	}

	private void showBookTableData(List<List<String>> data, List<String> header){
		GTableModel tm = new GTableModel(data, header);
		List<Integer> csz = new ArrayList<>();
		csz.add(160);//file name
		csz.add(100);// invoice number
		csz.add(160); //client name
		csz.add(200); //event title
		csz.add(200);//client email
		csz.add(160); //client telephone
		csz.add(200);//organisation
		csz.add(100);//book statue
		csz.add(200);//book note
		csz.add(100); //create time
		csz.add(150); //actionId
		tm.setColumnSizes(csz);
		bookingPane.setMainTable(tm, false);
		bookingPane.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookingPane.getMainTable().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt)
			{
				super.mousePressed(evt);
				int clickCnt = evt.getClickCount();
				if (clickCnt == 2)
				{
					int row = bookingPane.getMainTable().rowAtPoint(evt.getPoint());
					int col = bookingPane.getMainTable().columnAtPoint(evt.getPoint());
					if (col == 0) {
						String aid = (String)bookingPane.getMainTable().getModel().getValueAt(row, 10);
						BookData bd = bookRecords.get(aid);
						openPdfApp(bd);
					}
				}
			}
		});
		bookingPane.updateUI();
	}

	private void showInvoiceTableData(List<List<String>> data, List<String> header){
		GTableModel tm = new GTableModel(data, header);
		List<Integer> csz = new ArrayList<>();
		csz.add(160);//file name
		csz.add(160); //book file name
		csz.add(100);// invoice number
		csz.add(100); //purchase order
		csz.add(100); //pay status
		csz.add(160); //client name
		csz.add(200); //event title
		csz.add(200);//client email
		csz.add(160); //client telephone
		csz.add(200);//organisation
		csz.add(200);//book note
		csz.add(100); //create time
		csz.add(150); //actionId
		tm.setColumnSizes(csz);
		invoicePane.setMainTable(tm, false);
		invoicePane.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		invoicePane.getMainTable().addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt)
			{
				super.mousePressed(evt);
				int clickCnt = evt.getClickCount();
				if (clickCnt == 2)
				{
					int row = invoicePane.getMainTable().rowAtPoint(evt.getPoint());
					int col = invoicePane.getMainTable().columnAtPoint(evt.getPoint());
					if (col == 0) {
						String 	aid = (String)invoicePane.getMainTable().getModel().getValueAt(row, 12);
						BookData bd = invoiceRecords.get(aid);
						openPdfApp(bd);
					}
				}
			}
		});
		invoicePane.updateUI();
	}
}
