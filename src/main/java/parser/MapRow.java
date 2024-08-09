package parser;
/**
 * General wrap class to store a row of data with header as key
 * One row  as one maprow instance to solve data bugs
 * @author guangyang
 *
 */
import java.util.*;

public class MapRow implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;
	
	Map<String, String> rowDataMap;
	ArrayList<String> extractField;
	
	String note;
	String address;
	final String rowID;
	
	public MapRow() {
		rowDataMap = new LinkedHashMap<String, String>();
		rowID = UUID.randomUUID().toString();
		
	}
	public String getRowID() { return rowID; }
	public ArrayList<String> getExtractField(){ return extractField; }
	public void putData(String key, String value) {
		if(key == null|| key.trim().length()<=0) return;
		
		if (value ==null || value.trim().length()<=0)
			value = new String("");
		
		rowDataMap.put(key, value);
	}
	public String getDataValue(String key) { return rowDataMap.get(key); }
	
	public String getAddress() {
			return address;
		
	}
	
	//debug purpose
	public void printMapRow() {
		for (Map.Entry<String, String> entry : rowDataMap.entrySet())
		{
		    System.out.print(entry.getKey() + ":" + entry.getValue()+", ");
		}
		System.out.println("");
	}
	
	public void extractData() {
		extractField = new ArrayList<String>();
		String st = rowDataMap.get(AppConstants.START_FLD);
		if(st !=null) st = st.trim();
		extractField.add(st);
		extractField.add(rowDataMap.get(AppConstants.END_FLD));
		extractField.add(rowDataMap.get(AppConstants.DURATION_FLD));
		extractField.add(rowDataMap.get(AppConstants.COUNT_FLD));
		String tit = rowDataMap.get(AppConstants.TITLE_FLD);
		if(tit !=null) tit = tit.trim();
		extractField.add(tit); //4
		extractField.add(rowDataMap.get(AppConstants.PRICE_FLD));
		extractField.add(rowDataMap.get(AppConstants.STATUS_FLD));
		extractField.add(rowDataMap.get(AppConstants.FIRST_NAME_FLD));
		extractField.add(rowDataMap.get(AppConstants.LAST_NAME_FLD));
		extractField.add(rowDataMap.get(AppConstants.ORG_FLD));
		extractField.add(rowDataMap.get(AppConstants.TEL_FLD));
		extractField.add(rowDataMap.get(AppConstants.EMAIL_FLD));
		String sp = rowDataMap.get(AppConstants.SPACE_FLD);
		if(sp !=null) sp = sp.trim();
		extractField.add(sp);  //12			
		extractField.add(this.getCategory()); 
		extractField.add(rowID);
		address = rowDataMap.get(AppConstants.NOTE_ADDRESS_FLD);
	}
	
	public String getCategory() {		
		String privatex = rowDataMap.get(AppConstants.PRIVATE_FLD); 
		String commercial = rowDataMap.get(AppConstants.COMMERCIAL_FLD);  
		String uniform = rowDataMap.get(AppConstants.UNIFORM_FLD);
		String church = rowDataMap.get(AppConstants.CHURCH_FLD);
		String churchMe = rowDataMap.get(AppConstants.CHURCH_ME_FLD);
		String young =  rowDataMap.get(AppConstants.YOUNG_FLD);
		String category = new String("Others");
				
		if(privatex!=null && privatex.equalsIgnoreCase("yes"))
			category = AppConstants.PRIVATE_GROUP;
		else if(commercial != null && commercial.equalsIgnoreCase("yes"))
			category= AppConstants.COMMERCIAL_GROUP;
		else if(uniform !=null && uniform.equalsIgnoreCase("yes"))
			category = AppConstants.UNIFORM_GROUP;
		else if(church !=null && church.equalsIgnoreCase("yes"))
			category = AppConstants.CHURCH_GROUP;
		else if(churchMe !=null && churchMe.equalsIgnoreCase("yes"))
			category = AppConstants.CHURCH_ME_GROUP;
		else if(young !=null && young.equalsIgnoreCase("yes"))
			category = AppConstants.YOUNG_LIFE_GROUP;
		return category;
	}
	
	public void setCustomerNote( ) {
		String noteCustom = rowDataMap.get(AppConstants.NOTE_CUSTOM_FLD); 
		String noteEdnaCustomer = rowDataMap.get(AppConstants.NOTE_EDNA_FLD); 
		
		String str = null;		
		if(noteCustom !=null && noteCustom.trim().length()>0)
			str = noteCustom;
		 
		if(noteEdnaCustomer !=null && noteEdnaCustomer.trim().length()>0)
		{
			if(str !=null) str = str+"; "+noteEdnaCustomer;
			else str = noteEdnaCustomer;
		}
		if(str != null)
			note = str;		
	}
	public String displayString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("Space: "+extractField.get(12));
		sbf.append(", Start time: "+extractField.get(0) + ", End Time:"+extractField.get(1));
		sbf.append("\n");
		return sbf.toString();
	}
	
	//debug: print datamap
	public void printDebugData() {
		rowDataMap.forEach((key, value) -> System.out.println(key + ":" + value));
	}

}
