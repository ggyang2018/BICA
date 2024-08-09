package parser;
/**
 * Main Frame: for take user action. Class structure, public interface, protected
 * implementation, and private support functions
 * @Author Dr. Guang Yang 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
//import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Serial;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import tech.sunnykit.guikit.gui.MainGuiFrame;
import tech.sunnykit.guikit.util.MixConverter;
import tech.sunnykit.guikit.widget.XYLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import imgfile.*;
import persistent.*;

//set logger
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParserFrame extends MainGuiFrame implements ActionListener
{
	@Serial
	private static final long serialVersionUID = MainGuiFrame.serialVersionUID;
	private static final Logger logger = LogManager.getLogger(ParserFrame.class);
	//GUI components
	JMenuBar menuBar;
	JPanel statusBar;
	JLabel status;
	ParserPane ctxPane;
	//processor
	XlsxExtractor extractor;
	PdfGenerater pdfMaker;
    //Data
	ArrayList<String> tableHeaders;
    List<List<String>> tableData;
    //List<List<String>> workingData;
	Color navyBlue;
	String statusStr;
	BookingStore bookStore;

	boolean makeInvoiceFromBookAct = false;
	public ParserFrame() {
		super("BICA (Booking Invoice Confirmation App)", true);
		tableHeaders = new ArrayList<String>();
		//set table headers for view
		tableHeaders.add(AppConstants.START_TFLD); //0
		tableHeaders.add(AppConstants.END_TFLD); //1
		tableHeaders.add(AppConstants.DURATION_TFLD); //2
		tableHeaders.add(AppConstants.COUNT_TFLD); //3
		tableHeaders.add(AppConstants.TITLE_FLD); //4
		tableHeaders.add(AppConstants.PRICE_TFLD); //5
		tableHeaders.add(AppConstants.STATUS_TFLD);  //paind,unpaid, no need idx =6
		tableHeaders.add(AppConstants.FIRST_NAME_TFLD); //7
		tableHeaders.add(AppConstants.LAST_NAME_TFLD);//8
		tableHeaders.add(AppConstants.ORG_TFLD); //9
		tableHeaders.add(AppConstants.TEL_TFLD); //10
		tableHeaders.add(AppConstants.EMAIL_TFLD); //11
		tableHeaders.add(AppConstants.SPACE_TFLD); //12
		tableHeaders.add(AppConstants.CATEGORY_TFLD); //13
		tableHeaders.add("Row Unique ID"); //extra field for data structure.
	}

	@Override
	public void init() {
		bookStore = new BookingStore();
		makeMenuBar();
		getContentPane().setLayout(new XYLayout());
		//resolution 1250x750
		int frmWid = DefaultWidth - 10;
		int frmHgt = DefaultHeight -110;	
		int x=5;
		int y = 5;
		ctxPane = new ParserPane(this, frmWid, frmHgt);
		ctxPane.setBounds(x, y, frmWid, frmHgt);
		getContentPane().add(ctxPane); y+=(frmHgt+5);
		setStatusBar();		
		statusBar.setBounds(x, y, frmWid, 28);
		getContentPane().add(statusBar);
		
		extractor = new XlsxExtractor();
		pdfMaker = new PdfGenerater();
	}
	
	
	// ---- Support Methods -----
	void makeMenuBar()
	{
		Font f = new Font("sans-serif", Font.BOLD, 12);
		menuBar = new JMenuBar();
		EtchedBorder etchedBorder = new EtchedBorder();
		menuBar.setBorder(etchedBorder);
		//JMenu emptyMenu = new JMenu("         "); did not work

		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(f);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		//add iitems
		JMenuItem connItem = new JMenuItem(AppConstants.LOAD_XLSX);
		connItem.setMnemonic(KeyEvent.VK_L);
		connItem.setToolTipText("Load Excel File: xlsx");
		connItem.addActionListener(this);
		fileMenu.add(connItem);	
		JMenuItem closeItem = new JMenuItem(AppConstants.SAVE_XLSX_AS);
		closeItem.setMnemonic(KeyEvent.VK_S);
		closeItem.setToolTipText("Save Excel File As");
		closeItem.addActionListener(this);
		closeItem.setEnabled(false);
		fileMenu.add(closeItem);
		
		fileMenu.addSeparator();
		JMenuItem openData = new JMenuItem(AppConstants.OPEN_STREAM);
		openData.setMnemonic(KeyEvent.VK_O);
		openData.setToolTipText("Open the .lkc file");
		openData.addActionListener(this);
		openData.setEnabled(false);
		fileMenu.add(openData);		
		JMenuItem saveData = new JMenuItem(AppConstants.SAVE_STREAM);
		saveData.setMnemonic(KeyEvent.VK_K);
		saveData.setToolTipText("Save data into lkc file");
		saveData.addActionListener(this);
		saveData.setEnabled(false);
		fileMenu.add(saveData);
		
		fileMenu.addSeparator();
		//ImageIcon icon = new ImageIcon("exit.png");
		//JMenuItem exitItem = new JMenuItem("Exit", icon);
		JMenuItem exitItem = new JMenuItem(AppConstants.SYS_EXIT);
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.setToolTipText("Exit application");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		menuBar.add(new JMenu("         "));

		JMenu toolMenu = new JMenu("Sorting");
		menuBar.add(toolMenu);
		toolMenu.setFont(f);
		JMenu titleMenu = new JMenu("Sort Titles");
		toolMenu.add(titleMenu);
		JMenuItem listItem = new JMenuItem(AppConstants.SORT_TITLE_A);
		listItem.setMnemonic(KeyEvent.VK_T);
		listItem.setToolTipText("Sorting by titles ascending");
		listItem.addActionListener(this);
		titleMenu.add(listItem);
		
		JMenuItem listItem1 = new JMenuItem(AppConstants.SORT_TITLE_D);
		listItem1.setToolTipText("Sorting by titles descending");
		listItem1.addActionListener(this);
		titleMenu.add(listItem1);
		
		JMenu startMenu = new JMenu("Sort Start Times");
		toolMenu.add(startMenu);
		JMenuItem scriptItem = new JMenuItem(AppConstants.SORT_START_TIME_A);
		scriptItem.setMnemonic(KeyEvent.VK_R);
		scriptItem.setToolTipText("Sort by schedule start time ascending");
		scriptItem.addActionListener(this);
		startMenu.add(scriptItem);
		
		JMenuItem scriptItem1 = new JMenuItem(AppConstants.SORT_START_TIME_D);
		scriptItem1.setToolTipText("Sort by schedule start time descending");
		scriptItem1.addActionListener(this);
		startMenu.add(scriptItem1);
		
		JMenu spaceMenu = new JMenu("Sort Spaces");
		toolMenu.add(spaceMenu);
		JMenuItem fieldItem = new JMenuItem(AppConstants.SORT_SPACE_A);
		fieldItem.setMnemonic(KeyEvent.VK_P);
		fieldItem.setToolTipText("Sorting by space  ascending");
		fieldItem.addActionListener(this);
		spaceMenu.add(fieldItem);
		
		JMenuItem fieldItem1 = new JMenuItem(AppConstants.SORT_SPACE_D);
		fieldItem1.setToolTipText("Sorting by space  descending");
		fieldItem1.addActionListener(this);
		spaceMenu.add(fieldItem1);
		
		toolMenu.addSeparator();
		JMenuItem htmlItem = new JMenuItem(AppConstants.SORT_COMBINE);
		htmlItem.setMnemonic(KeyEvent.VK_B);
		htmlItem.setToolTipText("Combination sorting ");
		htmlItem.addActionListener(this);
		toolMenu.add(htmlItem);
		htmlItem.setEnabled(false);
		menuBar.add(new JMenu("         "));

		JMenu process = new JMenu("Create_PDF");
		process.setFont(f);
		menuBar.add(process);
		JMenuItem contract = new JMenuItem(AppConstants.GEN_CONTRACT);
		contract.setMnemonic(KeyEvent.VK_C);
		contract.setToolTipText("Generate constroct for selected records");
		contract.addActionListener(this);
		process.add(contract);
		
		JMenuItem inv = new JMenuItem(AppConstants.GET_INVOICE);
		inv.setMnemonic(KeyEvent.VK_I);
		inv.setToolTipText("Generate invoice for selected records");
		inv.addActionListener(this);
		process.add(inv);
		menuBar.add(new JMenu("         "));
		//recording menu
		JMenu recordMenu = new JMenu("Recordings");
		recordMenu.setFont(f);
		menuBar.add(recordMenu);
		JMenuItem bookSortItem = new JMenuItem(AppConstants.REFRESH_BOOK);
		bookSortItem.setMnemonic(KeyEvent.VK_S);
		bookSortItem.setToolTipText("Refresh the booking table");
		bookSortItem.addActionListener(this);
		recordMenu.add(bookSortItem);
		JMenuItem bookDetailItem = new JMenuItem(AppConstants.BOOK_DETAILS);
		bookDetailItem.setMnemonic(KeyEvent.VK_D);
		bookDetailItem.setToolTipText("Show book detail list");
		bookDetailItem.addActionListener(this);
		recordMenu.add(bookDetailItem);
		JMenuItem viewBookItem = new JMenuItem(AppConstants.BOOK_VIEW);
		viewBookItem.addActionListener(this);
		recordMenu.add(viewBookItem);
		recordMenu.addSeparator();
		JMenuItem invoiceSortItem = new JMenuItem(AppConstants.REFRESH_INVOICE);
		invoiceSortItem.setMnemonic(KeyEvent.VK_I);
		invoiceSortItem.setToolTipText("Refresh the the invoice table");
		invoiceSortItem.addActionListener(this);
		recordMenu.add(invoiceSortItem);
		JMenuItem invoiceDetailItem = new JMenuItem(AppConstants.INVOICE_DETAILS);
		invoiceDetailItem.setMnemonic(KeyEvent.VK_V);
		invoiceDetailItem.setToolTipText("Show invoice detail list");
		invoiceDetailItem.addActionListener(this);
		recordMenu.add(invoiceDetailItem);
		JMenuItem invoiceViewItem = new JMenuItem(AppConstants.INVOICE_VIEW);
		invoiceViewItem.addActionListener(this);
		recordMenu.add(invoiceViewItem);
		recordMenu.addSeparator();

		JMenuItem invoiceBookItem = new JMenuItem(AppConstants.BOOK_INVOICE);
		invoiceBookItem.setMnemonic(KeyEvent.VK_C);
		invoiceBookItem.setToolTipText("Create invoice from book");
		invoiceBookItem.addActionListener(this);
		recordMenu.add(invoiceBookItem);
		JMenuItem sortingItem = new JMenuItem(AppConstants.SORT_RECORDS);
		sortingItem.setMnemonic(KeyEvent.VK_S);
		sortingItem.addActionListener(this);
		recordMenu.add(sortingItem);

		menuBar.add(new JMenu("         "));
		JMenu analyst = new JMenu("Reporting");
		analyst.setFont(f);
		menuBar.add(analyst);
		JMenuItem comp_income = new JMenuItem(AppConstants.COMP_PAY);
		comp_income.setMnemonic(KeyEvent.VK_C);
		comp_income.setToolTipText("Display percentage of timing and income");
		comp_income.addActionListener(this);
		analyst.add(comp_income);

		JMenuItem save_img = new JMenuItem(AppConstants.SAVE_IMG);
		save_img.setMnemonic(KeyEvent.VK_S);
		save_img.setToolTipText("Save the panel as Image");
		save_img.addActionListener(this);
		analyst.add(save_img);

		// shift to the right
	    menuBar.add(Box.createGlue());
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(f);
		menuBar.add(helpMenu);
		JMenuItem aboutItem = new JMenuItem(AppConstants.HELP_ABOUT);
		aboutItem.setMnemonic(KeyEvent.VK_A);
		aboutItem.setToolTipText("About the Kirk Parser for Excel File");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);

		JMenuItem infoItem = new JMenuItem(AppConstants.HELP_INFO);
		infoItem.setMnemonic(KeyEvent.VK_I);
		infoItem.setToolTipText("Information about the application");
		infoItem.addActionListener(this);
		helpMenu.add(infoItem);
		
		setJMenuBar(menuBar);
	}
	void setStatusBar()
	{
		java.util.Date now = new java.util.Date();
        //String ss = DateFormat.getDateTimeInstance().format(now);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm");
		String ss = sdf.format(now);		
        statusBar = new JPanel();
	    JLabel today = new JLabel(ss, JLabel.RIGHT);
	    status = new JLabel("Data Status: ", JLabel.RIGHT);
	    today.setOpaque(true);//to set the color for jlabel
	    navyBlue = new Color(0, 0, 52);
        today.setBackground(navyBlue.brighter());
	    today.setForeground(Color.WHITE);
	    status.setOpaque(true);
        status.setBackground(navyBlue.brighter());
        status.setForeground(Color.WHITE);      
        statusBar.setLayout(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setBackground(Color.LIGHT_GRAY);
        statusBar.add(status, BorderLayout.WEST);
        statusBar.add(today, BorderLayout.EAST);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		switch(cmd) {		
			case AppConstants.LOAD_XLSX:
				extractData();
				break;
			case AppConstants.SAVE_XLSX_AS:
				System.out.println(cmd);
				break;
			case AppConstants.OPEN_STREAM:
				System.out.println(cmd);
				break;
			case AppConstants.SAVE_STREAM:
				System.out.println(cmd);
				break;
			case AppConstants.SYS_EXIT:
				System.exit(0);
			case AppConstants.SORT_TITLE_A: 
				sortTitles(true);
				break;
			case AppConstants.SORT_TITLE_D:
				sortTitles(false);
				break;
			case AppConstants.SORT_START_TIME_A:
				sortStartTime(true);
				break;
			case AppConstants.SORT_START_TIME_D:
				sortStartTime(false);
				break;
			case AppConstants.SORT_SPACE_A:
				sortSpace(true);
				break;
			case AppConstants.SORT_SPACE_D:
				sortSpace(false);
				break;
			case AppConstants.VIEW_DETAILS:
				System.out.println(cmd);
				break;
			case AppConstants.SORT_COMBINE:
				System.out.println(cmd);
				break;
			case AppConstants.GEN_CONTRACT:
				makeContract();
				break;
			case AppConstants.GET_INVOICE:
				makeInvoice();
				break;
			case AppConstants.HELP_ABOUT:
				showAbout();
				break;
			case AppConstants.COMP_PAY:
				comparePay();
				break;
			case AppConstants.SAVE_IMG:
				savePanelImage();
				break;
			case AppConstants.REFRESH_BOOK:
				this.refreshBookings();
				break;
			case AppConstants.REFRESH_INVOICE:
				this.refreshInvoices();
				break;
			case AppConstants.BOOK_DETAILS:
				this.getBookDetailList();
				break;
			case AppConstants.INVOICE_DETAILS:
				this.getInvoiceDetailList();
				break;
			case AppConstants.BOOK_VIEW:
				viewBookDetails();
				break;
			case AppConstants.INVOICE_VIEW:
				viewInvoiceDetails();
				break;
			case AppConstants.BOOK_INVOICE:
				this.makeInvoiceFromBook();
				break;
			case AppConstants.SORT_RECORDS:
				openSortDlg();
				break;
			case AppConstants.HELP_INFO:
				System.out.println(cmd);
				break;
		}
	}
	//------- Action Implementation -------
	void extractData() {
		ArrayList<MapRow> lst = extractXlsxFile();
		String logTest = "\n ******* load xlsx file OK *********";
		logger.info(logTest);
		System.out.println(logTest);
		if(lst !=null && lst.size()>0)
		{
			tableData = new ArrayList<List<String>>();
			for(int i=0; i<lst.size(); i++)
			{
				MapRow rw = lst.get(i);
				rw.extractData();
				tableData.add(rw.getExtractField());
			}
			ctxPane.renderData(tableData, tableHeaders);
			//get time period
		    String start_date = tableData.get(0).get(0);
		    String end_date = tableData.get(tableData.size()-1).get(0);
		    String start_str = MixConverter.convertISODateStr2UK(start_date, false);
		    String end_str = MixConverter.convertISODateStr2UK(end_date, false);
		    statusStr = String.format("Period: %s--%s; Data size: %d; ", start_str, end_str, tableData.size());		
			status.setText(statusStr);
			ctxPane.reportPane.setPeriod(statusStr);
		}
	}
	
	void sortTitles(boolean isAscending) {		
		Collections.sort(tableData, new Comparator<List<String>>() {		    
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscending)
					return o1.get(4).compareTo(o2.get(4));
				else
					return o2.get(4).compareTo(o1.get(4));
			}
		  });
		ctxPane.renderData(tableData, tableHeaders);
		String str1 = ", Sorted by title in ascending";
		if(!isAscending) str1 = ", Sorted by title in descending";
		status.setText(statusStr+str1);
	
	}
	
	void sortStartTime(boolean isAscending) {
		Collections.sort(tableData, new Comparator<List<String>>() {		    
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscending)
					return o1.get(0).compareTo(o2.get(0));
				else
					return o2.get(0).compareTo(o1.get(0));
			}
		  });
		ctxPane.renderData(tableData, tableHeaders);
		String str1 = ", Sorted by start time in ascending";
		if(!isAscending) str1 = ", Sorted by start time in descending";
		status.setText(statusStr+str1);
	}
	
	void sortSpace(boolean isAscending) {
		Collections.sort(tableData, new Comparator<List<String>>() {		    
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(isAscending)
					return o1.get(12).compareTo(o2.get(12));
				else
					return o2.get(12).compareTo(o1.get(12));
			}
		  });
		ctxPane.renderData(tableData, tableHeaders);
		String str1 = ", Sorted by space in ascending";
		if(!isAscending) str1 = ", Sorted by space in descending";
		status.setText(statusStr+str1);
	}
			
	
	void makeContract() {
		if(!ctxPane.hasLoadData()) return;
		
		List<MapRow> selects = getSelectDataFromTable(true);
		if(selects == null) return;		
		Map<String, List<MapRow>> mp_rows = pdfMaker.groupSelectTitles(selects);
		URL imgUrl = getClass().getResource("/plus_blue.png");
		SelectListDlg dlg = new SelectListDlg(this, "Contract Generator", imgUrl.getPath());
		dlg.setFavourBounds(580, 480);
		dlg.setDataPane(mp_rows);
		dlg.displayDlg();
	}
	
	void makeInvoice() {
		if(!ctxPane.hasLoadData()) return;
		
		List<MapRow> selects = getSelectDataFromTable(true);
		if(selects == null) return;		
		Map<String, List<MapRow>> mp_rows = pdfMaker.groupSelectTitles(selects);
		URL imgUrl = getClass().getResource("/plus_blue.png");
		InvoiceDlg dlg = new InvoiceDlg(this, "Generating Invoice", imgUrl.getPath());
		dlg.setFavourBounds(400, 520);
		dlg.setDataPane(mp_rows);
		dlg.displayDlg();		
		
	}
	
	void showAbout() {
		AboutDlg dlg = new AboutDlg("res/question.png");
		dlg.setFavourBounds(600, 330);
		dlg.setVisible(true);
	
	}
	
	// ----- support methods ------
	private ArrayList<MapRow> extractXlsxFile() {
		
		//FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			String pt = selectedFile.getAbsolutePath();						
			extractor.extractFields(pt);
			ArrayList<MapRow> listBooker = extractor.getDataRows();
			return listBooker;
		}else
			return null;
		
	}

	private List<MapRow> getSelectDataFromTable(boolean isContract)
	{
		int[] rws = ctxPane.dataPane.mainTable.getSelectedRows();
		if(rws == null|| rws.length<1)
		{
			String tit = "Warning to Produce PDF File";
			if(!isContract) tit = "Warning to Produce Invoice";
			
			NoticeBox box = new NoticeBox("Please make selection for processing", NoticeBox.ERROR, 300, 200);
			box.showMsg(tit);
			return null;
		}else
		{
		   List<MapRow> selects = new ArrayList<MapRow>();
		   for(int i=0; i<rws.length; i++) {
			   int idx = rws[i];
			   List<String> rw = tableData.get(idx);
			   String rid = rw.get(14); 
			   MapRow mr = extractor.getMapRow(rid);
			   selects.add(mr);
		   }
		   return selects;
		}		
	}
	ArrayList<MapRow> all_list = null;
	//---------- Chart actions ---------------
	private void comparePay() {
		if(all_list == null || all_list.size()<10)	
			all_list = extractor.getDataRows();
		
		if(all_list == null || all_list.size()<10) return; //retrive nothing	
		ctxPane.makePieChart(all_list);			
	}
	//https://stackoverflow.com/questions/2531037/how-to-save-file-using-jfilechooser
	private void savePanelImage() {
		if(!ctxPane.reportPane.hasLoad()) return;
		URL imgUrl = getClass().getResource("/plus_blue.png");
		ImageSaveDlg dlg = new ImageSaveDlg(this, "Sava Duration/Income Image", imgUrl.getPath());
		dlg.setFavourBounds(535, 180);
		dlg.setPane(ctxPane.reportPane);
		dlg.displayDlg();
	}
	
	void makeBarChart() {
		if(all_list == null || all_list.size()<10)	
			all_list = extractor.getDataRows();
		
		if(all_list == null || all_list.size()<10) {
			logger.debug("No Data be counted. ");
			return; //retrieve nothing	
		}
	}
	//------------- recording support mehtods ---------------
	private void refreshBookings() { ctxPane.renderBookData();}
	private void refreshInvoices() {ctxPane.renderInvoiceData();}
	private void getBookDetailList() {ctxPane.renderBookDetails();}
	private void getInvoiceDetailList(){ctxPane.renderInvoiceDetails();}
	private void makeInvoiceFromBook()
	{
		int row_idx = ctxPane.bookingPane.getMainTable().getSelectedRow();
		if(row_idx <0) return; //no selection
		String aid = (String)ctxPane.bookingPane.getMainTable().getModel().getValueAt(row_idx, 10);
		BookData bdx = ctxPane.bookRecords.get(aid);

		String invDone = "This invoice had been generated, find file in Invoice Tab";
		if(bdx.getBookStatus().equals(AppConstants.INVOICE_DONE_STATUS)) {
			JOptionPane.showMessageDialog(null, invDone,
					"Invoice Done", JOptionPane.ERROR_MESSAGE);
			return;
		}
		BookData bd = new BookData(bdx);
		//check if retrieve the details;
		List<String> time_lst = bd.getTimeList();
		if(time_lst == null || time_lst.isEmpty())
			bookStore.retrieveBookDetail(bd);
		String b_file = bdx.getFileNames();
		bd.setBookFileName(b_file); //avoid immutable
		BookInvoiceDlg dlg = new BookInvoiceDlg(this, bd);
		dlg.setFavourBounds(360, 360);
		dlg.displayDlg();
	}
	private void viewBookDetails() {ctxPane.viewBookContents();}
	private void viewInvoiceDetails(){ctxPane.viewInvoiceContents();}

	private void openSortDlg(){
		SortRecordDlg dlg = new SortRecordDlg(this);
		dlg.setFavourBounds(265, 270);
		dlg.displayDlg();
	}
}
