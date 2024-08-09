package parser;
/**
 * A excell data extract based on Apache PDF Box Lib
 * @author Guang Yang (Sunny)
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxExtractor 
{
	ArrayList<String> headers;	
	ArrayList<MapRow> dataRows;
	Map<String, MapRow> dataMap;
	
	int lastHeaderNum;
	
	
	
	public XlsxExtractor()
	{	
		headers = new ArrayList<String>();
		dataRows = new ArrayList<MapRow>();
		dataMap = new HashMap<String, MapRow>();
	}
	
	public ArrayList<MapRow> getDataRows(){
		return dataRows;
	}
	public MapRow getMapRow(String key) {return dataMap.get(key);}
	
	public void extractFields(String filePath)
	{	
	  	try
		{ 
			File excel = new File(filePath); 
			FileInputStream fis = new FileInputStream(excel); 
			XSSFWorkbook book = new XSSFWorkbook(fis); 
			XSSFSheet sheet = book.getSheetAt(0);
			
			//checkNumberRowCells(sheet); //for debug purpose		
			Iterator<Row> itr = sheet.iterator(); // Iterating over Excel file in Java 
			Row headerRow= itr.next();
			setHeaders(headerRow);
			
			if(dataRows.size()>0) dataRows.clear();		
			
			while (itr.hasNext()) 
			{ 
				Row row = itr.next(); 
				MapRow mp = createRowDataMap(row);
				dataRows.add(mp);
				dataMap.put(mp.getRowID(), mp);
			}
			book.close();
			fis.close();
		}catch (FileNotFoundException fe) { fe.printStackTrace(); } 
	    catch (IOException ie) { 
	    	System.out.println("Error on row");
	    	ie.printStackTrace(); }
	}
	
	void setHeaders(Row header_row)
	{
		if(!headers.isEmpty()) headers.clear();
		
		lastHeaderNum = header_row.getLastCellNum();
		Iterator<Cell> cellIterator = header_row.cellIterator();
		while (cellIterator.hasNext()) 
		{
			Cell cell = cellIterator.next();
			String hv = cell.getStringCellValue();
			if (hv !=null) hv = hv.trim();
			else hv = new String("");
			headers.add(hv);
		}
		//debug: 
	   //headers.stream().forEach(System.out::println);
	}
	
	void checkNumberRowCells(XSSFSheet sheet)
	{
		System.out.println("Row: "+sheet.getFirstRowNum()+" - "+sheet.getLastRowNum());
		Iterator<Row> itr = sheet.iterator(); // Iterating over Excel file in Java		
		int row_idx = 0;
		while (itr.hasNext() && row_idx <371) 
		{
			Row row = itr.next();
			System.out.println("Row "+row_idx+": "+row.getFirstCellNum()+" - "
								+row.getLastCellNum());
			row_idx++;
		}
	}

	
	public String getCellStrValue(Cell cell)
	{
		String str = "";
		if(cell.getCellType() == CellType.STRING)
			str =  cell.getRichStringCellValue().toString();
		else if(cell.getCellType() == CellType.NUMERIC || cell.getCellType()==CellType.FORMULA)
		{
			str = String.valueOf(cell.getNumericCellValue());
			if(DateUtil.isCellDateFormatted(cell)) {
				LocalDateTime dt = cell.getLocalDateTimeCellValue();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		    	str = dt.format(myFormatObj);
			}			
		}	
		return str;			
	}
	
	MapRow createRowDataMap(Row row)
	{
		int count = row.getLastCellNum();
		MapRow rowMap = new MapRow();
		for(int i =0; i<count; i++)
		{
			String dv = new String("");
			Cell cell = row.getCell(i);
			if(cell != null)
				dv = getCellStrValue(cell);
			String hd = headers.get(i);
			rowMap.putData(hd, dv);
		}	
		return rowMap;		
	}
	
	//debug purpose
	public void printHeaderData() {
		System.out.println(headers);
		for(int i=0; i<dataRows.size(); i++) {
			MapRow mr = dataRows.get(i);
			mr.printMapRow();
		}
			
	}
				
}
