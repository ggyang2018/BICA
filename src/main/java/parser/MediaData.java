package parser;
/**
 * Media class between pdf render and data. It help to get data. 
 * This transite object. 
 * @author guangyang 
 */
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MediaData 
{
	String title = "hire title";
	String userName ="firs surname";
	String userOrg;
	String userTel = "user tel";
	String userEmail = "user eamil";
	String userAddress ="user address";
	String hireFee = "£30";
	String payStatus;
	String category;
	String address;
	
	List<String> timeList;
	List<String> spaceList;
	List<String> priceList;
	List<String> duratList;
	String recurring;
	
	String totalHours;
	boolean isEmpty; 
	
	String firstDate;
	String todayDate;
	
	//user Import paramenters
	String invoiceNo;
	String purchaseOrderNo;
	List<String> requires;
	//UUID are actually universally unique identifiers
	String actionId;
	//new data for persistent
	String bookType;
	String bookStatus;
	
	public MediaData() {
		isEmpty = true;
		actionId= UUID.randomUUID().toString().replace("-", "");
	}
	
	public void extractData(List<MapRow> maps) {
		if ( maps == null || maps.size()<1) return;
		//using first line to get common features
		ArrayList<String> cols = maps.get(0).getExtractField();
		title = cols.get(4);
		userName = cols.get(7)+" "+cols.get(8);
		userOrg = cols.get(9);
		userTel = cols.get(10);
		userEmail = cols.get(11);
		category = maps.get(0).getCategory();
		address = maps.get(0).getAddress();
		
		timeList= new ArrayList<String>();
		spaceList = new ArrayList<String>();
		priceList = new ArrayList<String>();
		duratList = new ArrayList<String>();
		//sorting
		if(maps.size()>1) //need sorted according to start time
		{
			Collections.sort(maps, new Comparator<MapRow>() {		    
				@Override
				public int compare(MapRow o1, MapRow o2) {
					return o1.getExtractField().get(0).compareTo(o2.getExtractField().get(0));
					
				}
			  });
		}
		String statStr = null;
		for(int i=0; i<maps.size(); i++) //list maximum keep 3 row, other put into recuring. 
		{
			MapRow row = maps.get(i);
			String tm1 = row.getExtractField().get(0);
			if(i==0) statStr = tm1;
			tm1 = convertISODateStr(tm1);			
			String tm2[] = row.getExtractField().get(1).split(" ");
			String tm = tm1+" - "+tm2[1];
			timeList.add(tm);
			spaceList.add(row.getExtractField().get(12));
			String fs = row.getExtractField().get(5);
			try {
				float fee = Float.parseFloat(fs);
				fs = String.format("%.02f", fee);
			}catch(Exception ex) { ex.printStackTrace(); }		
			priceList.add(fs);	
			duratList.add(row.getExtractField().get(2));
		}
		//price calculation
		float totalFee = 0;
		for(int i=0; i<priceList.size(); i++)
		{
			String pf = priceList.get(i);
			try {
				float tp = Float.parseFloat(pf);
				totalFee+=tp;
			}catch(Exception ex) { ex.printStackTrace(); }
		}
		hireFee = "£"+String.format("%.02f", totalFee);
		
		//time calcuation, due convertion averything became float
		int minutes = 0;
		for(int i=0; i<duratList.size(); i++) {
			String m = duratList.get(i);
			//input contains 1.0 
			try {
				float m1 = Float.parseFloat(m);
				int mx = Math.round(m1);
				minutes+=mx;
			}catch(Exception ex) { ex.printStackTrace(); }
		}
		int hours = Math.floorDiv(minutes, 60);
		int min = minutes % 60;
		if(hours >0)
			totalHours = String.valueOf(hours)+" hours and "+String.valueOf(min)+" minutes";
		else
			totalHours = String.valueOf(min)+" minutes";
				
		StringBuffer sbf = new StringBuffer();
		int tsz = timeList.size();
		if(tsz>=3) {
			sbf.append(String.format("There are %d bookings, see additional bookings", tsz));
			recurring = sbf.toString();
		}
		//date transfer
		firstDate = this.convertISODateStr2UK(statStr);
		LocalDate today = LocalDate.now();//For reference
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy");
		todayDate = today.format(formatter1);
		requires = new ArrayList<String>();
		isEmpty = false;
	}		
	public String getTitle() { return title; }
	public String getUserName() { return userName; }
	public String getUserOrg() { return userOrg; }
	public String getUserTel() { return userTel; }
	public String getUserEmail() { return userEmail; }
	public String getUserAddress() { return userAddress; }
	public String getHireFee() { return hireFee; } //total amount
	public String getPayStatus() { return payStatus; }
	public String getCategory() { return category; }
	public String getAddress() { return address; }
		
	public List<String> getTimeList() { return timeList; }
	public List<String> getSpaceList() { return spaceList; }
	public List<String> getPriceList() { return priceList; }
	public List<String> getDuratList() { return duratList;}

	public String getRecurring() { return recurring; }
	
	public String getTotalHours() { return totalHours; }
	public boolean checkEmpty() { return isEmpty; } 
	
	public String getFirstDate() { return firstDate; }
	public String getTodayDate() { return todayDate; }
	
	public void addRequire(String require) {
		requires.add(require);
	}
	public List<String> getRequires( ) {
		return requires;
	}
	
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo =invoiceNo;
	}
	public String getInvoiceNo () {
		if(invoiceNo != null)
			return invoiceNo;
		else
			return new String();
	}
	
	public void setPurchaseOrderNo(String s) {
		purchaseOrderNo = s;
	}
	public String getPurchaseOrderNo() {
		if(purchaseOrderNo != null)
			return purchaseOrderNo;
		else
			return new String();
	}

	public void setActionId(String id) { actionId = id;}
	public String getActionId() { return actionId;}

	public String getBookType() { return bookType; }
	public void setBookType(String tp) { bookType = tp;}

	public String getBookStatus() { return bookStatus; }
	public void setBookStatus(String status) { bookStatus= status;}

	//only convert date not include time
	public String convertISODateStr2UK(String iso_date) {
		try {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.UK);
			LocalDate local_date = LocalDate.parse(iso_date, formatter);
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy ", Locale.UK);			
			return local_date.format(formatter1);
		}catch(DateTimeParseException dex) { 
			dex.printStackTrace();
			return iso_date;
		}
	}
	//convert date and time
	public String convertISODateStr(String iso_date) {
		try 
		{		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d = sdf.parse(iso_date);
			sdf.applyPattern("EEE, dd MMM yyyy HH:mm");
			return sdf.format(d);
			
		}catch(ParseException dex) { 
			dex.printStackTrace();
			return iso_date;
		}
	}
	//SETTERS for new Actions
	public void setTitle(String tit) { title = tit; }
	public void setUserName(String str) { userName = str; }
	public void setUserOrg(String str) { userOrg = str; }
	public void setUserTel(String str) { userTel = str; }
	public void setUserEmail(String str) { userEmail = str; }
	public void setUserAddress(String str) { userAddress = str; }
	public void setHireFee(String str) { hireFee = str; } //total amount
	public void setPayStatus(String str) { payStatus = str; }
	public void setCategory(String str) {  category = str; }
	public void setAddress(String str) {  address = str; }

	public void setTimeList(List<String> lst) { timeList = lst; }
	public void setSpaceList(List<String> lst) { spaceList = lst; }
	public void setPriceList(List<String> lst) { priceList = lst; }
	public void setDuratList(List<String> lst) { duratList = lst;}

	public void setRecurring(String str) { recurring = str; }
	public void setTotalHours(String str) { totalHours = str; }
	public void setFirstDate(String str) { firstDate = str; }
	public void setTodayDate(String str) { todayDate = str; }
}
