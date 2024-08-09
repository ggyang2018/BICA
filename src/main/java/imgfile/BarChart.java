package imgfile;

import java.util.*;

import parser.AppConstants;
import parser.MapRow;
/**
 * Like an agent class composite
 * @author guangyang
 */
public class BarChart 
{
	List<MapRow> privateBook; 
	List<MapRow> commercialBook;  
	List<MapRow> uniformBook;
	List<MapRow> churchBook;
	List<MapRow> youngBook;
	List<MapRow> othersBook;
	
	List<MapRow> allList;
	
	
	public BarChart() {
		privateBook = new ArrayList<MapRow>(); 
		commercialBook = new ArrayList<MapRow>();  
		uniformBook = new ArrayList<MapRow>();
		churchBook = new ArrayList<MapRow>();
		youngBook =  new ArrayList<MapRow>();
		othersBook = new ArrayList<MapRow>();
		
		
	}
	
	public void groupData(ArrayList<MapRow> all_list) {
		allList = all_list;
		for (MapRow rw : all_list) {
			String cat = rw.getCategory();
			if(cat.equals(AppConstants.PRIVATE_GROUP))
				privateBook.add(rw);
			else if(cat.equals( AppConstants.COMMERCIAL_GROUP))
				commercialBook.add(rw);
			else if(cat.equals( AppConstants.UNIFORM_GROUP))
				uniformBook.add(rw);
			else if(cat.equals(AppConstants.CHURCH_GROUP)) {
				//String tit = rw.getDataValue(AppConstants.TITLE_FLD);
				//String org = rw.getDataValue(AppConstants.ORG_FLD);
				//if(!org.contains("Cafe Manager") && !tit.equalsIgnoreCase("Kirkgate Cafe"))
					churchBook.add(rw);
			}else if(cat.equals(AppConstants.YOUNG_LIFE_GROUP))
				youngBook.add(rw);
			else 
				othersBook.add(rw);	
		}
	}
	
	public Map<String, Number> countGroupTime() {
		Map<String, Number> rmp = new LinkedHashMap<>();
		int pvt_time = countTime(privateBook);
		int com_time = countTime(commercialBook);
		int uni_time = countTime(uniformBook);
		int chu_time = countTime(churchBook);
		int you_time = countTime(youngBook);
		int oth_time = countTime(othersBook);
		int sum_time = pvt_time+com_time+uni_time+chu_time+you_time+oth_time;
		
		rmp.put(AppConstants.PRIVATE_GROUP, pvt_time);
		rmp.put(AppConstants.COMMERCIAL_GROUP, com_time);
		rmp.put(AppConstants.UNIFORM_GROUP, uni_time);
		rmp.put(AppConstants.CHURCH_GROUP, chu_time);
		rmp.put(AppConstants.YOUNG_LIFE_GROUP, you_time);
		rmp.put(AppConstants.OTHER_GROUP, oth_time);
		rmp.put("sum", sum_time);
		return rmp;		
	}
	
	public Map<String, Number> countGroupMoney(){
		Map<String, Number> pay_mp = new LinkedHashMap<>();
		
		float pvt_pay = countPrice(privateBook);
		float com_pay = countPrice(commercialBook);
		float uni_pay = countPrice(uniformBook);
		float chu_pay = countPrice(churchBook);
		//float chum_pay = countPrice(churchMeBook);
		float you_pay = countPrice(youngBook);
		float oth_pay = countPrice(othersBook);
		float sum_pay = pvt_pay+com_pay+uni_pay+chu_pay+you_pay+oth_pay;
		
		pay_mp.put(AppConstants.PRIVATE_GROUP, pvt_pay);
		pay_mp.put(AppConstants.COMMERCIAL_GROUP, com_pay);
		pay_mp.put(AppConstants.UNIFORM_GROUP, uni_pay);
		pay_mp.put(AppConstants.CHURCH_GROUP, chu_pay);
		pay_mp.put(AppConstants.YOUNG_LIFE_GROUP, you_pay);
		pay_mp.put(AppConstants.OTHER_GROUP, oth_pay);
		pay_mp.put("sum", sum_pay);
		
		return pay_mp;		
	}
	
	public void makeBarChart(List<MapRow> all_list) {
		
	}
	
	public String makePercentage(String name, Number div, Number dom) {
		double a = dom.doubleValue();
		double b = div.doubleValue();		
		double rest =100.0 * b/a;
		String str = String.format("%2.02f", rest);
		return name+"="+str+"%";
	}
	
	
	//support methods 
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

}
