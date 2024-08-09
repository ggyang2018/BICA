package parser;
/**
 * Application constant place holder
 * @author guang yang (Sunny)
 *
 */


public class AppConstants 
{
	//general properties
	static public String DATE_FORMAT = "yyyy-MM-dd : hhmm";
	static public String PAID_STATUS = "Paid"; //"  -*(paid with thanks), ???";

	//Extract Field Names
	final static public String START_FLD ="Scheduled start";
	final static public String END_FLD ="End";
	final static public String DURATION_FLD ="Duration (minutes)";
	final static public String COUNT_FLD ="Spaces count";
	final static public String TITLE_FLD ="Title";
	final static public String PRICE_FLD ="Price";
	final static public String STATUS_FLD ="Payment status";
	final static public String FIRST_NAME_FLD ="Holder first name";
	final static public String LAST_NAME_FLD ="Holder last name";
	final static public String ORG_FLD ="Holder organization";
	final static public String TEL_FLD ="Holder telephone";
	final static public String EMAIL_FLD ="Holder email";
	final static public String CREATE_FLD ="Created";
	final static public String SPACE_FLD ="Spaces";
	
	final static public String PRIVATE_FLD = "Private (Custom field 4)"; 
	final static public String COMMERCIAL_FLD ="Commercial (Custom field 5)";  
	final static public String UNIFORM_FLD = "Uniform (Custom field 6)";
	final static public String CHURCH_FLD = "Church (Custom field 7)";
	final static public String CHURCH_ME_FLD = "Church Me… (Custom field 8)"; 
	final static public String YOUNG_FLD = "Young Life (Custom field 9)"; 
	
	final static public String NOTE_CUSTOM_FLD = "Notes (Custom field 1)"; 
	final static public String NOTE_EDNA_FLD = "Edna - Th… (Custom field 2)"; 
	final static public String NOTE_ADDRESS_FLD = "Address (Custom field 3)";
	
	//Table header
	final static public String START_TFLD = "Schedule Start"; 
	final static public String END_TFLD = "Schedule End"; 
	final static public String DURATION_TFLD = "Duration"; 
	final static public String COUNT_TFLD = "Space Count"; 
	final static public String TITLE_TFLD = "Title"; 
	final static public String PRICE_TFLD = "Price";
	final static public String STATUS_TFLD = "Payment Satus";  //paind,unpaid, no need idx =6
	final static public String FIRST_NAME_TFLD = "First Name";
	final static public String LAST_NAME_TFLD = "Last Name";
	final static public String ORG_TFLD = "Organisation";
	final static public String TEL_TFLD = "Telephone";
	final static public String EMAIL_TFLD = "Email";
	final static public String SPACE_TFLD = "Space";
	final static public String CATEGORY_TFLD = "Category";
	
	//Menu File Action Commands
	final static public String LOAD_XLSX = "Load xlsx";
	final static public String SAVE_XLSX_AS = "Save xlsx";
	final static public String OPEN_STREAM = "Open lck"; //FOR .LKC FILE
	final static public String SAVE_STREAM = "Save lkc";
	final static public String SYS_EXIT = "Exit";
	
	//Menu View ing Sorting Action Commands
	final static public String SORT_TITLE_A = "Title sort (asc)";
	final static public String SORT_TITLE_D = "Title sort (desc)";
	final static public String SORT_START_TIME_A = "Start sort (asc)";
	final static public String SORT_START_TIME_D = "Start sort (desc)";
	final static public String SORT_SPACE_A = "Space sort (asc)";
	final static public String SORT_SPACE_D = "Space sort (desc)";
	final static public String SORT_COMBINE = "Combine sort"; //doing later
	final static public String VIEW_DETAILS = "View details";
	
	//Menu Tools Action Commands
	final static public String GEN_CONTRACT = "Generate contract";
	final static public String GET_INVOICE = "Generate invoice";
	
	//Menu Analyst Action Commands
	final static public String COMP_PAY = "Duration & Income"; //pie chart for timing and income
	final static public String SAVE_IMG = "Save as Image";
	
	//Menu Help Action
	final static public String HELP_ABOUT = "About";
	final static public String HELP_INFO = "Information";

	//Group Names: 
	final static public String PRIVATE_GROUP = "Private";
	final static public String COMMERCIAL_GROUP = "Commerical";
	final static public String UNIFORM_GROUP = "Uniform";
	final static public String CHURCH_GROUP = "Church";
	final static public String CHURCH_ME_GROUP = "Church Meeting";
	final static public String OTHER_GROUP = "Others";
	final static public String YOUNG_LIFE_GROUP = "Young Life";

	//For Persistent
	final static public String CONTRACT_TYPE="Book_Contract";
	final static public String INVOICE_TYPE="Book_Invoice";
	public final static String CONTRACT_DONE_STATUS = "Ready for invoice";
	public final static String INVOICE_DONE_STATUS = "Invoice has done";//for contract
	//menu item for persistent tools
	public final static String REFRESH_BOOK = "Refresh Bookings";
	public final static String REFRESH_INVOICE = "Refresh Invoices";
	public final static String BOOK_DETAILS = "View Book List";
	public final static String INVOICE_DETAILS = "View Invoice List";
	public final static String BOOK_INVOICE = "Invoice from Book";
	public final static String BOOK_VIEW = "View Book Contents";
	public final static String INVOICE_VIEW = "View Invoice Contents";

	public  final static String SORT_RECORDS = "Sort Records";
}
	


