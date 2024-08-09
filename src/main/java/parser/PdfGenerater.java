package parser;
/**
 * PDF Generation based on Apapche PdfBox Library.
 * https://www.tutorialspoint.com/pdfbox/pdfbox_quick_guide.htm  as tutorial
 * bdfbox packages and dependences include: common-logging-1.2.jar, fontbox-2.0.27.jar, pdfbox-2.0.27.jar
  add filename
Path pathToFolder = Path.of("/Users/someuser/");
Path pathToFile = pathToFolder.resolve("your-file-name");

Path path = Paths.get("c:\\data\\myfile.txt");
 * nonStrokingColor using 1 as mmax
 * strokingColor using 255 both using different namespace
 * 
 * @author guangyang
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.util.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//set logger
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class PdfGenerater {	
	private static final Logger logger = LogManager.getLogger(PdfGenerater.class);
	
	  // Create a new font object selecting one of the PDF base fonts
    PDFont fontPlain = PDType1Font.HELVETICA;
    PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    PDFont fontMono = PDType1Font.COURIER;
    PDFont fontItalicBold = PDType1Font.HELVETICA_BOLD_OBLIQUE;
    
    //final String info1 = "A period 15 mins will be allowed before and after hires for setting up/cleaning up, extra times will be included in booking time";
    final String header1="LIBERTON KIRK-CHURCH OF SCOTLAND (Scottish Charity No. SC011602) 28-30 Kirkgate EH16 6RY"; 
    final String lkTel = "07478111407";
    final String lkEmail = "facilities@libertonkirk.net";
	final String info2_0="-*Payment is acceptance of our terms & conditions of hire.";
    final String info2=" -*Payment must be made prior to the hire period commencing unless agreed otherwise";
    final String bankName="Bank of Scotland, Newington Branch, 51 South Clerk Branch, Edinburgh EH8 9PP";
    final String bankTrans="A/C Name: SEECOS Kirk Session; Sort Code: 80.02.73; Account No: 00492471";
    final String bankInfo="Please refer to invoice for payment details";
		
	String subFolder;
	Path pdfPath;
	
	final int halfPageRowCount = 13;
	
	public PdfGenerater() {
		subFolder = "pdf_files";
		
		try {
			 pdfPath = Paths.get(subFolder);
			 if (Files.notExists(pdfPath))
				 Files.createDirectories(pdfPath);
			 System.out.println(pdfPath.toString());
			 Path abs = pdfPath.toAbsolutePath();
			 logger.debug(abs.toString());
		}catch(IOException ioe) {
			ioe.printStackTrace();
			logger.debug("{}", ioe);
		}
	}

	public void makeContract(MediaData mediaData, String filePath) throws IOException
	{
		PDDocument document = new PDDocument();
		PDPage page1 = new PDPage(PDRectangle.A4);
		PDRectangle rect = page1.getMediaBox();	//w=595.27563, h=841.8898			
		document.addPage(page1);
		
		//create content of page
		PDPageContentStream cos = new PDPageContentStream(document, page1);
		try 
		{
			//works as on loose file
			//PDImageXObject ximage = PDImageXObject.createFromFile("kirk.jpg", document);
			InputStream logoFileAsStream = getClass().getResourceAsStream("/kirk.jpg");
			PDImageXObject ximage = PDImageXObject.createFromByteArray(document,
								IOUtils.toByteArray(logoFileAsStream), "kirk.jpg");
			float scale = 0.3f; // alter this value to set the image size
            cos.drawImage(ximage, 20, rect.getHeight()- 50, ximage.getWidth()*scale,
							ximage.getHeight()*scale);
			logoFileAsStream.close();
        } catch (Exception ioex)
		{
            System.out.println("No image for you");
            ioex.printStackTrace();
        }
		addText(cos, fontBold, 16, 200, rect.getHeight() - 40, "Contract for Hire of Halls");
		addText(cos, fontBold, 11, 15, rect.getHeight() - 70, header1);
		//contract section
		float x=15, w1=100, w2=200, h=20;  
		float y=rect.getHeight()-35*3; 
		addRectText(cos, fontBold, 11, x, y, w1, h, "Hire agreement"); 
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, mediaData.getTitle());		
		x+=w2; w1=60;
		addRectText(cos, fontBold, 11, x, y, w1, h, "Date");
		cos.addRect(x, y, w1, h);
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, mediaData.getFirstDate());
		
		//contact section
		x=30; y=rect.getHeight()-32*4; 
		addText(cos, fontBold, 11, x, y, "Representative at Liberton Kirk");
		x=330;
		addText(cos, fontBold, 11, x, y, "User/Group Contact Details");	
		x=15; y=rect.getHeight()-155;
		w1=80; w2=200;
		addRectText(cos, fontBold, 11, x, y, w1, h, "L.K. Rep.");		
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, "Edna Matthews");		
		x+=w2;
		addRectText(cos, fontBold, 11, x, y, w1, h, "Name:");
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, mediaData.getUserName());
		
		x=15; y-=h;
		addRectText(cos, fontBold, 11, x, y, w1, h, "Contact Tel:");		
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, this.lkTel);
		x+=w2;
		addRectText(cos, fontBold, 11, x, y, w1, h, "Contact Tel:");		
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, mediaData.getUserTel());		
		x=15; y-=h; 
		addRectText(cos, fontBold, 11, x, y, w1, h, "Email:");		
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, lkEmail);
		x+=w2;
		addRectText(cos, fontBold, 11, x, y, w1, h, "Email:");
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, mediaData.getUserEmail());
		x=15; y-=h;  w1=110; w2=450;
		addRectText(cos, fontBold, 11, x, y, w1, h, "Customer Address:");		
		x+=w1;
		addRectText(cos, fontPlain, 11, x, y, w2, h, mediaData.getAddress());
				
		//payment section
		x=15; y=rect.getHeight()-240; h=15;
		addText(cos, fontBold, 11, x, y, "Total Hire Fee: ");
		x+=80;
		addText(cos, fontPlain, 11, x, y, mediaData.getHireFee());
		x+=60;
		addText(cos, fontItalic, 10, x, y, info2_0);
		y-=h;
		addText(cos, fontItalic, 10, x, y, info2);
		y-=h;
		//x=15;
		addText(cos, fontBold, 11, x, y, bankInfo);		
		//y-=h; x=15;
		//addText(cos, fontBold, 11, x, y, "Important quote Reference/Invoice No: ");
		//x+=210; w1=200;
		//addRectText(cos, fontBold, 11, x, y-5, w1, h, mediaData.getInvoiceNo());
	
	    //book detail section
		w1=100;
		float reqx = 0, reqy = 0;
		x=150; y=rect.getHeight()-300; h=20;
		addText(cos, fontBold, 11, x, y, "           Details of Hire ");				
		
		x=15; y-=30; w1=180; w2=80;
		float w11=170, w13=80, w14=120;
		addRectText(cos, fontBold, 11, x, y, w11, h, "         Date + Time");
		x+=w11;
		addRectText(cos, fontBold, 11, x, y, w1, h, "         Location");
		x+=w1;
		addRectText(cos, fontBold, 11, x, y, w13, h, "  Price (£)");
		x+=w13; reqx=x;
		addRectText(cos, fontBold, 11, x, y, w14, h, "    Requirements");
		x=15; y-=h; 
		List<String> timeList = mediaData.getTimeList();
		List<String> spaceList = mediaData.getSpaceList();
		List<String> priceList = mediaData.getPriceList();

		for(int i=0; i<3; i++) {
			if(i<timeList.size())
			{
				addRectText(cos, fontPlain, 11, x, y, w11, h, timeList.get(i));
				x+=w11;
				addRectText(cos, fontPlain, 11, x, y, w1, h, spaceList.get(i));
				x+=w1;
				addRectText(cos, fontPlain, 11, x, y, w13, h, priceList.get(i));
			}else {
				addRectText(cos, fontPlain, 11, x, y, w11, h, " ");
				x+=w11;
				addRectText(cos, fontPlain, 11, x, y, w1, h, " ");
				x+=w1;
				addRectText(cos, fontPlain, 11, x, y, w13, h, " ");
			}
			x=15;
			reqy = y;
			y-=h;
		}
		cos.addRect(reqx, reqy, w14, 3*h); //empty require box. 
		List<String> reqs = mediaData.getRequires();
		if(reqs !=null && reqs.size()>0) 
		{   reqy+=(3*h-10);
		    float h1 = h-5;
		    for(int i=0; i<reqs.size(); i++) {
		    	this.addText(cos, fontPlain, 11, reqx+10, reqy, reqs.get(i));
		    	reqy-=h1;
		    }
		}
		
		String recu = mediaData.getRecurring();
	    float rec_wid = w11+w1+w13+w14;
		if(recu !=null && recu.length()>0)
			addRectText(cos, fontPlain, 11, x, y, rec_wid, h, recu);
		else
			addRectText(cos, fontPlain, 11, x, y, rec_wid, h, " ");
		
		
		h=15; y-=h; 
		addText(cos, fontPlain, 11, x, y, "Requirements include:  1. P.A = Public Address System, Mic = microphone");
		y-=h;
		addText(cos, fontPlain, 11, x, y, "                                 2. V.P = Video projector and/or screen,");
		y-=h;
		addText(cos, fontPlain, 11, x, y, "                                 3. Tables/Chairs = table/chairs available for you to set up");
		
		//waster section as final heighjt (no change from here 
		x=40; y=rect.getHeight()-500;;
		addText(cos, fontBold, 11, x, y, "**PLEASE ALSO TAKE ALL WASTE HOME AS WE DON'T HAVE ENOUGH BIN SPACES**");
		x=20; y-=h;
		addText(cos, fontPlain, 11, x, y, "The large bin at side of Anderson Hall does not belong to us so please do not put your waste in this bin");
		
		//term sections, height won't be changed from here
		x=30; y=rect.getHeight()-540; h=20;
		String tm1 = "* Please read the full version of our Terms and Conditions of which these are only a part, See full terms & conditions attached"; 
	    addText(cos, fontPlain, 9, x, y, tm1);
	    y-=h;
	    String tm2= "* The user must inform Libertion Kirk of their intention to have any \"third pary\" entertainment in the halls and must ensure if so";
	    addText(cos, fontPlain, 9, x, y, tm2);
	    y-=10;
	    String tm2_1 = "     1. They have their own Public Liability Insurance";
	    addText(cos, fontPlain, 9, x, y, tm2_1);
		y-=10;
	  	String tm2_2 ="     2. Some activities will require to have in place their own Public Liability Insurence";  
	  	addText(cos, fontPlain, 9, x, y, tm2_2);		
	    y-=h;  
	    String tm3 = "* Any breakage or damage to either equipment or fabric will be met by the user";		
	    addText(cos, fontPlain, 9, x, y, tm3);	    
	    y-=h;
	    String tm4 = "* A period 15 mins will be allowed before and after hire time for setting up/cleaning up, which is not included in booking time";
	    addText(cos, fontPlain, 9, x, y, tm4);	    
	    y-=h;
	    String tm5="* Recurring hire arrangements must be renewed by 1st September for the following year. Failure to do this means hall may ";  
	    addText(cos, fontPlain, 9, x, y, tm5);
	    y-=10; x=35;
	    addText(cos, fontPlain, 9, x, y, "not be available for you to book");
	    
	    //declaration section
	    x=100; y=rect.getHeight()-690; h=20;
	    String decStr = "I declare that I have read and accept the Terms and Conditions.";
	    addText(cos, fontBold, 11, x, y, decStr);
	       
	    x=20; y-=20; w1=240; h=20;
	    addText(cos, fontPlain, 11, x, y, "LK Representative: Edna Matthews");   
		x+=w1;
		addText(cos, fontPlain, 11, x, y, "Client: "+mediaData.getUserName());			   
		x=20; y-=h; 
		addText(cos, fontPlain, 11, x, y, "Signature: ________________________");	    
		x+=w1;
		addText(cos, fontPlain, 11, x, y, "Signature: ________________________");
		x=20; y-=h; 
		addText(cos, fontPlain, 11, x, y, "Date: ________________________");	    
		x+=w1;
		addText(cos, fontPlain, 11, x, y, "Date: ________________________");
		//testing
		addSeecosLogo(document, cos);
		cos.stroke();
		cos.close();
		//add appendix page. 
		int sz = timeList.size();
		if(sz>3 )
		{	
		    if(sz <=2 * this.halfPageRowCount)
		    	this.addApppendixPage(document, timeList, spaceList, priceList, 0);
		    else
		    	this.addMultiplePages(document, timeList, spaceList, priceList, 0);
		}
		document.save(filePath);	
		document.close();
		logger.debug("Complete contract on "+mediaData.getTitle());
	}
	
	public void makeInvoice(MediaData mediaData, String filePath) throws IOException 
	{
		//data			
		PDDocument document = new PDDocument();
		PDPage page1 = new PDPage(PDRectangle.A4);
		PDRectangle rect = page1.getMediaBox();	//w=595.27563, h=841.8898			
		document.addPage(page1);
				
		//create content of page
		PDPageContentStream cos = new PDPageContentStream(document, page1);
		try 
		{
			InputStream logoFileAsStream = getClass().getResourceAsStream("/kirk.jpg");
			PDImageXObject ximage = PDImageXObject.createFromByteArray(document,
					IOUtils.toByteArray(logoFileAsStream), "kirk.jpg");
			float scale = 0.5f; // alter this value to set the image size
			cos.drawImage(ximage, 30, rect.getHeight()- 120, ximage.getWidth()*scale, ximage.getHeight()*scale);
			logoFileAsStream.close();
		} catch (IOException ioex) {
			System.out.println("No image for you");
			ioex.printStackTrace();
		}
		float x=380, y=rect.getHeight()-70, h=20;  
		addText(cos, fontBold, 16, x, y, "Liberton Kirk Centre");
		y-=h;
		addText(cos, fontBold, 16, x, y, "28 Kirkgate");
		y-=h;
		addText(cos, fontBold, 16, x, y, "Edinburgh, EH16 6RY");
		y-=h; x=350;
		addText(cos, fontBold, 14, x, y, "Scottish Charity No. SC011602");
				
		x=50; y=rect.getHeight()-150; 
		int font_size = 12;
		float x1= 150;
		addText(cos, fontBold, font_size+2, x, y, "Invoice No: ");
		addText(cos, fontBold, font_size+2, x1, y, mediaData.getInvoiceNo());
		y-=15;
		addText(cos, fontItalicBold, font_size, x, y, "Important: Please quote the invoice no. when paying online");
				
		x=50;
		x1=240; y=rect.getHeight()-200; h=25;
		addText(cos, fontBold, font_size, x, y, "Invoice to: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getUserName());
		y-=h;
		addText(cos, fontBold, font_size, x, y, "Title of Event/Class: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getTitle());
		y-=h;
		addText(cos, fontBold, font_size, x, y, "Email: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getUserEmail());
		y-=h;
		addText(cos, fontBold, font_size, x, y, "Mobile Number: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getUserTel());
		y-=h;
		addText(cos, fontBold, font_size, x, y, "Invoice Date: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getTodayDate());
		y-=h;
		addText(cos, fontBold, font_size, x, y, "Purchase Order No: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getPurchaseOrderNo());
		y-=h;
		addText(cos, fontBold, font_size, x, y, "Amount Due: ");
		addText(cos, fontPlain, font_size, x1, y, mediaData.getHireFee());
		y-=(h-10);
		addText(cos, fontItalic, 10, x1, y, info2_0);
		y-=h;
		x=45;
		addText(cos, fontBold, 11, x, y, "Pay by BACS: ");
		x+=110;
		addText(cos, fontPlain, 11, x, y, bankName);
		y-=15;
		x=45;
		addText(cos, fontBold, 11, x, y, "Bank Details: ");
		x+=110;
		addText(cos, fontPlain, 11, x, y, this.bankTrans);	
		//get details of bookings
		List<String> timeList = mediaData.getTimeList();
		List<String> spaceList = mediaData.getSpaceList();
		List<String> priceList = mediaData.getPriceList();
		x=50; y-=40; 
		float w1=200, w11=180, w13=80; 
		addRectText(cos, fontBold, 11, x, y, w11, h, "         Date Time");
		x+=w11;
		addRectText(cos, fontBold, 11, x, y, w1, h, "         Location");
		x+=w1;
		addRectText(cos, fontBold, 11, x, y, w13, h, "    Price(£)");
		x=50; y-=h;
		for(int i=0; i<timeList.size() && i<this.halfPageRowCount; i++) {
			addRectText(cos, fontPlain, 11, x, y, w11, h, timeList.get(i));
			x+=w11;
			addRectText(cos, fontPlain, 11, x, y, w1, h, spaceList.get(i));
			x+=w1;
			addRectText(cos, fontPlain, 11, x, y, w13, h, priceList.get(i));
			x=50;
			y-=h;
		}
		addSeecosLogo(document, cos);
		cos.stroke();
		cos.close();
		int list_sz = timeList.size();
		if (list_sz >= this.halfPageRowCount)
		{
		   int page_cnt = 2 * halfPageRowCount;
		   
		   List<String> subTimeList = timeList.subList(this.halfPageRowCount, list_sz);
		   List<String> subSpaceList = spaceList.subList(this.halfPageRowCount, list_sz);
		   List<String> subPriceList = priceList.subList(this.halfPageRowCount, list_sz);
		   
		   int sub_sz = subTimeList.size();
		   if(sub_sz <=page_cnt)
			   addApppendixPage(document, subTimeList, subSpaceList, subPriceList, 0);
		   else
			   addMultiplePages(document, subTimeList, subSpaceList, subPriceList, 0);		   		   		   
		}
		document.save(filePath);	
		document.close();
		logger.debug("Complete Invoice on "+mediaData.getTitle());
	}

	public Map<String, List<MapRow>> groupSelectTitles(List<MapRow> selects) 
	{
		Map<String, List<MapRow>> groups = new LinkedHashMap<String, List<MapRow>>();
		for(int i=0; i<selects.size(); i++)
		{
			MapRow mr = selects.get(i);
			String tit = mr.getDataValue(AppConstants.TITLE_FLD).trim();
			String fnm = mr.getDataValue(AppConstants.FIRST_NAME_FLD).trim();
			String lnm = mr.getDataValue(AppConstants.LAST_NAME_FLD).trim();
			String tk = String.format("%s <%s %s>", tit, fnm, lnm);
			if(groups.containsKey(tk))
			{
				List<MapRow> lst = groups.get(tk);
				lst.add(mr);
			}else {
				List<MapRow> lst1 = new ArrayList<MapRow>();
				lst1.add(mr);
				groups.put(tk, lst1);			
			}		
		}
		return groups;
	}
	
	/*
	 * File will be in same pattern
	 */
	public String checkFile(String fileName)
	{
		Path pathToFile = pdfPath.resolve(fileName);
	    if(Files.exists(pathToFile))
	    	return null;
	    else
	    	return pathToFile.toString();
	}
	public String findStoredFilePath(String fileName)
	{
		Path pathToFile = pdfPath.resolve(fileName);
		if(!Files.exists(pathToFile))
			return null;
		else
			return pathToFile.toString();
	}
	
	void addApppendixPage(PDDocument document, List<String> timeList, List<String> spaceList, List<String> priceList, 
			int opId) throws IOException
	{
		PDPage page1 = new PDPage(PDRectangle.A4);
		PDRectangle rect = page1.getMediaBox();	//w=595.27563, h=841.8898			
		document.addPage(page1);
		
		//create content of page
		PDPageContentStream cos = new PDPageContentStream(document, page1);
		try {
			InputStream logoFileAsStream = getClass().getResourceAsStream("/kirk.jpg");
			PDImageXObject ximage = PDImageXObject.createFromByteArray(document,
					IOUtils.toByteArray(logoFileAsStream), "kirk.jpg");
			float scale = 0.35f; // alter this value to set the image size
            cos.drawImage(ximage, 20, rect.getHeight()- 90, ximage.getWidth()*scale, ximage.getHeight()*scale);
			logoFileAsStream.close();
        } catch (IOException ioex) {
            System.out.println("No image for you");
            ioex.printStackTrace();
        }
		String infoTitle = "Additional Bookings";
		if(opId == 1)
			infoTitle = "Invoice Continue ......";
		
		addText(cos, fontBold, 16, 200, rect.getHeight() - 70, infoTitle);
		
		float x=150, w1=200, h=20, w11=180, w13=80;  
		float y=rect.getHeight()-150;
		//addText(cos, fontBold, 11, x, y, "           Details of Hire ");				
		x=50; //y-=40; 
		addRectText(cos, fontBold, 11, x, y, w11, h, "         Date Time");
		x+=w11;
		addRectText(cos, fontBold, 11, x, y, w1, h, "         Location");
		x+=w1;
		addRectText(cos, fontBold, 11, x, y, w13, h, "   Price(£)");
		x=50; y-=h;
		for(int i=0; i<timeList.size(); i++) {
			addRectText(cos, fontPlain, 11, x, y, w11, h, timeList.get(i));
			x+=w11;
			addRectText(cos, fontPlain, 11, x, y, w1, h, spaceList.get(i));
			x+=w1;
			addRectText(cos, fontPlain, 11, x, y, w13, h, priceList.get(i));
			x=50;
			y-=h;
		}
		addSeecosLogo(document, cos);
		cos.stroke();
		cos.close();
	}
			
	/*
	 * Add text to PDF file, postion x, y (up) 
	 */
	private void addText(PDPageContentStream cos, PDFont pdFont, int fontSize, float x, float y, String txt) throws IOException
	{
		if(txt == null) txt = new String();
		cos.beginText();
		cos.setFont(pdFont, fontSize);
		cos.newLineAtOffset(x, y); 
		cos.showText(txt);
		cos.endText();
	}
	private void addRectText(PDPageContentStream cos, PDFont pdFont, int fontSize, float x, float y, float w, float h, String txt) 
	throws IOException{
		if(txt == null) txt=new String();	
		float x1 = x+2;
		float y1 = y+5;
		cos.addRect(x, y, w, h);
		cos.beginText();
		cos.setFont(pdFont, fontSize);
		cos.newLineAtOffset(x1, y1);
		cos.showText(txt);
		cos.endText();
	}
	private void addMultiplePages(PDDocument document, List<String> timeList, List<String> spaceList,
								  List<String> priceList, int opId) throws IOException
	{
		int sz = timeList.size();
		int page_cnt = 2 * halfPageRowCount;
		int page_nbr = Math.floorDiv(sz, page_cnt);
		if(sz > page_nbr * page_cnt) page_nbr++;
		for(int i=0; i<page_nbr; i++)
		{
			int from = i*page_cnt;
			int to = Math.min((i+1)*page_cnt, sz);
			List<String> subTimeList = timeList.subList(from, to);
			List<String> subSpaceList = spaceList.subList(from, to);
			List<String> subPriceList = priceList.subList(from, to);
			addApppendixPage(document, subTimeList, subSpaceList, subPriceList, opId);
		}
	}
	//add as each page footer
	void addSeecosLogo(PDDocument document, PDPageContentStream cos)
	{
		float x=50;
		float y = 30;
		String logoTxt = "Liberton Kirk is part of SEECOS";
		String website = "www.seecos.org.uk";
		try
		{
			addText(cos, fontBold, 16, x, y, logoTxt);
			addText(cos, fontPlain, 12, x+50, y-17, website);
			x+=350;
			InputStream seeco = getClass().getResourceAsStream("/seecos_black.png");
			PDImageXObject ximage = PDImageXObject.createFromByteArray(document,
					IOUtils.toByteArray(seeco), "seecos_black.png");
			float scale = 0.1f; // alter this value to set the image size
			cos.drawImage(ximage, x, y-25, ximage.getWidth()*scale, ximage.getHeight()*scale);
		} catch (IOException ioex) {
			System.out.println("No image for you");
			ioex.printStackTrace();
		}
	}

}
