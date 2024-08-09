package persistent;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import parser.AppConstants;
import parser.MediaData;
/**
 * Booking data for store book in terms of contract and invoice
 * All data as string type for easy display
 * @author guang yang (sunny)
 */
public class BookData
{
    String actionId;
    String invoiceNbr;
    String clientName;
    String eventTitle;
    String clientEmail;
    String clientTelephone;
    String firstBookDate;
    String totalBookHours;
    String hallSpace;
    String organisation;
    String payStatus;

    //no need to calculation only for display, using string
    String createTime;
    String updateTime;
    //for invoice only
    String purchaseOrder;
    String bookType;
    String bookStatus;
    String clientAddress;
    String bookFileName="No Booking";
    //note for both contract and invoice
    String  bookNote;
    List<String> timeList;
    List<String> spaceList;
    List<String> priceList;
    List<String> durationList;
    String fileNames;
    boolean newData;
    String totalFee;
    List<String> headers;//book header
    List<String> invoiceHeaders;

    public BookData()
    {
        newData = false;
        timeList = new ArrayList<>();
        spaceList = new ArrayList<>();
        priceList = new ArrayList<>();
        durationList = new ArrayList<>();
        headers = new ArrayList<>();
        invoiceHeaders = new ArrayList<>();
    }
    //property copy
    public BookData(BookData bd)
    {
        actionId = bd.actionId;
        invoiceNbr = bd.invoiceNbr;
        clientName = bd.clientName;
        eventTitle = bd.eventTitle;
        clientEmail = bd.clientEmail;
        clientTelephone = bd.clientTelephone;
        firstBookDate = bd.firstBookDate;
        totalBookHours = bd.totalBookHours;
        hallSpace = bd.hallSpace;
        organisation = bd.organisation;
        payStatus = bd.payStatus;

        createTime = bd.createTime;
        updateTime = bd.updateTime;
        purchaseOrder = bd.purchaseOrder;
        bookType = bd.bookType;
        bookStatus = bd.bookStatus;
        bookFileName = bd.bookFileName;
        bookNote = bd.bookNote;
        fileNames = bd.fileNames;
        newData = bd.newData;
        totalFee = bd.totalFee;
        //reference pass only
        timeList = bd.timeList;
        spaceList = bd.spaceList;
        priceList = bd.priceList;
        durationList = bd.durationList;
        headers = bd.headers;
        invoiceHeaders = bd.invoiceHeaders;
    }

    public void transfer(MediaData md) {
         actionId = md.getActionId();
         invoiceNbr = md.getInvoiceNo();
         clientName = md.getUserName();
         eventTitle = md.getTitle();
         clientEmail = md.getUserEmail();
         clientTelephone = md.getUserTel();
         totalBookHours = md.getTotalHours();
         firstBookDate = md.getFirstDate();
         organisation = md.getUserOrg();
         createTime = md.getTodayDate();
        //for invoice only
        purchaseOrder = md.getPurchaseOrderNo();
        clientAddress =md.getUserAddress(); //not in use put into note
        //for details
        timeList = md.getTimeList();
        spaceList = md.getSpaceList();
        priceList = md.getPriceList();
        durationList = md.getDuratList();
    }

    //only from booking and for pdf generation
    public MediaData makeInvoiceMediaData(){
        MediaData md = new MediaData();
        md.setActionId(actionId);
        md.setInvoiceNo(invoiceNbr);
        md.setUserName(clientName);
        md.setTitle(eventTitle);
        md.setUserEmail(clientEmail);
        md.setUserTel(clientTelephone);
        md.setFirstDate(timeList.getFirst());
        md.setUserOrg(organisation);
        //md.setPayStatus();
        //md.setPurchaseOrderNo();
        //md.setUserAddress(); put on note 
        md.setBookType(AppConstants.INVOICE_TYPE);
        md.setTimeList(timeList);
        md.setSpaceList(spaceList);
        md.setPriceList(priceList);
        md.setDuratList(durationList);
        //calculate total fee.
        float totalFee = 0;
        for(int i=0; i<priceList.size(); i++)
        {
            String pf = priceList.get(i);
            try {
                float tp = Float.parseFloat(pf);
                totalFee+=tp;
            }catch(Exception ex) { ex.printStackTrace(); }
        }
        md.setHireFee("£"+String.format("%.02f", totalFee));
        //calculate total hours
        int minutes = 0;
        for(int i=0; i<durationList.size(); i++) {
            String m = durationList.get(i);
            //input contains 1.0
            try {
                float m1 = Float.parseFloat(m);
                int mx = Math.round(m1);
                minutes+=mx;
            }catch(Exception ex) { ex.printStackTrace(); }
        }
        int hours = Math.floorDiv(minutes, 60);
        int min = minutes % 60;
        if(hours>0) md.setTotalHours(hours + " and "+min+" minutes");
        else md.setTotalHours(min+" minutes");
        // today date
        LocalDate today = LocalDate.now();//For reference
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy");
        md.setTodayDate(today.format(formatter1));
        return md;
    }

    public String getActionId() { return actionId;}
    public void setActionId(String id) { actionId = id;}

    public String getInvoiceNbr() { return invoiceNbr;}
    public void setInvoiceNbr(String str) { invoiceNbr = str; }

    public String getClientName() { return clientName; }
    public void setClientName(String str) { clientName = str; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String str) { eventTitle = str; }

    public String getClientEmail() { return clientEmail;}
    public void setClientEmail(String str) { clientEmail = str; }

    public String getClientTelephone() { return clientTelephone; }
    public void setClientTelephone(String str) { clientTelephone = str; }

    public String getTotalBookHours() { return totalBookHours; }
    public void setTotalBookHours(String str) { totalBookHours = str;}

    public String getHallSpace() { return hallSpace; }
    public void setHallSpace(String str) { hallSpace = str; }

    public String getOrganisation() { return organisation; }
    public void setOrganisation(String str) { organisation = str;}

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String str) { updateTime = str; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String time) { createTime = time;}

    //for invoice only
    public String getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(String str) { purchaseOrder = str;}

    public String getBookType() { return bookType;}
    public void setBookType(String str) { bookType = str;}

    public String getBookStatus() { return bookStatus;}
    public void setBookStatus(String str) { bookStatus = str;}

    public String getClientAddress() { return clientAddress; }
    public void setClientAddress(String str) { clientAddress = str;}
    //note for both contract and invoice
    public String getBookNote() { return bookNote; }
    public void setBookNote(String str) { bookNote = str; }

    public List<String> getTimeList() { return timeList; }
    public void setTimeList(List<String> lst) { timeList = lst;}

    public List<String> getSpaceList() { return spaceList; }
    public void setSpaceList(List<String> lst) { spaceList = lst;}

    public List<String> getPriceList() { return priceList; }
    public void setPriceList(List<String> lst) { priceList = lst; }

    public List<String> getDurationList() { return durationList; }
    public void setDurationList(List<String> lst) { durationList = lst; }

    public void setFileNames(String names) { fileNames = names;}
    public String getFileNames() { return fileNames; }

    public void setNewData (boolean is) { newData = is;}
    public boolean isNewData() { return newData;}

    public void setBookFileName(String fnm) { bookFileName = fnm;}
    public String getBookFileName() { return bookFileName; }

    public void setPayStatus(String str) { payStatus = str;}
    public String getPayStatus() { return payStatus; }

    //transit property
    public void setFirstBookDate(String dt) { firstBookDate = dt;}
    public String getFirstBookDate() { return firstBookDate; }

    public void setTotalFee(String fee) { totalFee = fee; }
    public String getTotalFee() { return totalFee; }

    //For display booking on creen
    public List<String> getTableHeader(){
        List<String> lst = headers.subList(1, headers.size()-1);
        lst.add(headers.get(0)); //add action id at last
        return lst;
    }


    public List<String> getTableData()
    { //not include actionId and update_time. 10 items
        List<String> lst = new ArrayList<>();
        //lst.add(actionId);
        lst.add(fileNames);
        lst.add(invoiceNbr);
        lst.add(clientName);
        lst.add(eventTitle);
        lst.add(clientEmail);
        lst.add(clientTelephone);
        lst.add(organisation);
        lst.add(bookStatus);
        lst.add(bookNote);
        lst.add(createTime.substring(0, 10));
        lst.add(actionId); //for sorting
        return lst;
    }
    //for display invoice table header and
    public List<String> getInvoiceHeader() {
        List<String> lst =  invoiceHeaders.subList(1, invoiceHeaders.size()-1);
        lst.add(invoiceHeaders.get(0));
        return lst;
    }

    public List<String> getInvoiceTableData()
    { //not include actionId and update_time. 12 items
        List<String> lst = new ArrayList<>();
        //lst.add(actionId);
        lst.add(fileNames);
        lst.add(bookFileName);
        lst.add(invoiceNbr);
        lst.add(purchaseOrder);
        lst.add(payStatus);
        lst.add(clientName);
        lst.add(eventTitle);
        lst.add(clientEmail);
        lst.add(clientTelephone);
        lst.add(organisation);
        lst.add(bookNote);
        lst.add(createTime.substring(0, 10));
        lst.add(actionId); //add for sorting
        return lst;
    }

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

    @Override
    public String toString()
    {
        StringBuffer sbf = new StringBuffer();
        sbf.append("\n Type: ").append(bookType);
        sbf.append("\nAction ID: ").append(actionId);
        sbf.append("\nInvoice Number: ").append(invoiceNbr);
        sbf.append("\nClient Name: ").append(clientName);
        sbf.append("\nEvent Title: ").append(eventTitle);
        sbf.append("\nClient Email: ").append(clientEmail);
        sbf.append("\nClient Tel: ").append(clientTelephone);
        sbf.append("\nFirst Date: ").append(firstBookDate);
        sbf.append("\nDuration: ").append(totalBookHours);
        sbf.append("\nHall Space: ").append(hallSpace);
        sbf.append("\nOrganisation: ").append(organisation);
        //no need to calculation only for display, using string
        sbf.append("\nCreate Date: ").append(createTime);
        sbf.append("\nUpdate Date: ").append(updateTime);
        //for invoice only
        sbf.append("\nBook Status: ").append(bookStatus);
        if(bookType.equals(AppConstants.INVOICE_TYPE))
        {
            sbf.append("\nInvoice from book: ").append(bookFileName);
            sbf.append("\nPurchase Order: ").append(purchaseOrder);
            sbf.append("\nPay Status: ").append(totalFee).append(" ").append(payStatus);
        }
        sbf.append("\nPDF File Name: ").append(fileNames);
        sbf.append("\nNote: ").append(bookNote);

        return sbf.toString();
    }

}
