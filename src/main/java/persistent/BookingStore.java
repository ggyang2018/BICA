package persistent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import parser.AppConstants;
import parser.MapRow;

import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;

/**
 * Persistent layer to connect the embedded database Hsqldb
 * @author guangyang
 */
public class BookingStore
{   private static final Logger logger = LogManager.getLogger(BookingStore.class);

    HsqldbHandle sqldb;

    public BookingStore()
    {
        sqldb = new HsqldbHandle();
        if(sqldb.hasDatabaseOn())
        {
            try
            {
                Connection con = sqldb.getConnector();
                sqldb.createBookTable(con);
            }catch(Exception ex)
            {
                logger.debug("{}", ex);
                ex.printStackTrace();
            }
        }
    }

    public boolean storeBooking(BookData data){
        boolean fg = false;
        try{
            Connection conn = sqldb.getConnector();
            PreparedStatement ppStt = getPreparedInsertBook(data);
            ppStt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            ppStt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            int row = ppStt.executeUpdate();
            ppStt.close();
            insertPreparedInsertDetail(data);
            fg = row>0;
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.error(String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
            logger.error("{}", e);
        }
        return fg;
    }

    public boolean storeInvoice(BookData data){
        boolean fg = false;
        try{
            if(checkTableRecord("hall_invoices", data.getActionId()))
                return fg;  //exist one
            Connection conn = sqldb.getConnector();
            PreparedStatement ppStt = getPreparedInsertInvoice(data);
            ppStt.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            ppStt.setTimestamp(14, Timestamp.valueOf(LocalDateTime.now()));
            int row = ppStt.executeUpdate();
            fg = row>0;
            ppStt.close();
            if(data.isNewData())
                insertPreparedInsertDetail(data);
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.error(String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
            logger.error("{}", e);
        }
        return fg;
    }

    public Map<String, BookData> retrieveBookings(){
        Map<String, BookData> mp = new LinkedHashMap<>();
        String sql = "select * from hall_bookings";
        try(
                Connection conn = sqldb.getConnector();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        )
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<String> hList = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++ ) {
                hList.add(rsmd.getColumnName(i));
            }
            while(rs.next())
            {
                BookData bk = new BookData();
                bk.setNewData(false); //retrieving data
                bk.headers = hList;
                bk.setActionId(rs.getString("action_id"));
                bk.setFileNames(rs.getString("file_name"));
                bk.setInvoiceNbr(rs.getString("invoice_nbr"));
                bk.setClientName(rs.getString("client_name"));
                bk.setEventTitle(rs.getString("event_title"));
                bk.setClientEmail(rs.getString("client_email"));
                bk.setClientTelephone(rs.getString("client_telephone"));
                bk.setOrganisation(rs.getString("organisation"));
                bk.setBookStatus(rs.getString("book_status"));
                bk.setBookNote(rs.getString("book_note"));
                bk.setCreateTime(rs.getString("create_time"));
                bk.setUpdateTime(rs.getString("update_time"));
                //add book type
                bk.setBookType(AppConstants.CONTRACT_TYPE);
                mp.put(bk.getActionId(), bk);
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.error(String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
            logger.error("{}", e);
        }
        return mp;
    }

    public Map<String, BookData> retrieveInvoices(){
        Map<String, BookData> mp = new LinkedHashMap<>();
        String sql = "select * from hall_invoices";
        try(
                Connection conn = sqldb.getConnector();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        )
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<String> hList = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++ ) {
                hList.add(rsmd.getColumnName(i));
            }
            while(rs.next())
            {
                BookData bk = new BookData();
                bk.setNewData(false); //retrieving data
                bk.invoiceHeaders = hList;
                bk.setActionId(rs.getString("action_id"));
                bk.setFileNames(rs.getString("file_name"));
                bk.setBookFileName(rs.getString("book_file_name"));
                bk.setInvoiceNbr(rs.getString("invoice_nbr"));
                bk.setPurchaseOrder(rs.getString("purchase_order"));
                bk.setPayStatus(rs.getString("pay_status"));
                bk.setClientName(rs.getString("client_name"));
                bk.setEventTitle(rs.getString("event_title"));
                bk.setClientEmail(rs.getString("client_email"));
                bk.setClientTelephone(rs.getString("client_telephone"));
                bk.setOrganisation(rs.getString("organisation"));
                bk.setBookNote(rs.getString("book_note"));
                bk.setCreateTime(rs.getString("create_time"));
                bk.setUpdateTime(rs.getString("update_time"));
                bk.setBookType(AppConstants.INVOICE_TYPE);
                mp.put(bk.getActionId(), bk);
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.error(String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
            logger.error("{}", e);
        }
        return mp;
    }

    //for both booking and invoice, retrieve on the fly
    public void retrieveBookDetail(BookData bd) {
        String sql = "select * from hall_book_details where action_id='" + bd.getActionId()+"'";
        try (
                Connection conn = sqldb.getConnector();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                bd.timeList.add(rs.getString("book_time"));
                bd.spaceList.add(rs.getString("book_space"));
                bd.priceList.add(rs.getString("book_price"));
                bd.durationList.add(rs.getString("book_duration"));
            }
            bd.setTotalBookHours(calcTotalHours(bd.durationList));
            bd.setHallSpace(getAllSpaces(bd.spaceList));
            bd.setTotalFee(calcuTotalPrice(bd.priceList));
            if(bd.timeList !=null && !bd.timeList.isEmpty())
                bd.setFirstBookDate(bd.timeList.get(0));
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.error(String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
            logger.error("{}", e);
        }
    }

    public boolean checkTableRecord(String tableName, String whereClause) throws SQLException
    {
        Connection conn = sqldb.getConnector();
        String sql = String.format("Select 1 from %s where action_id = '%s'", tableName, whereClause);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    public int updateBookStatus(String status, String aid) throws SQLException
    {
        String updateSql =String.format(" UPDATE hall_bookings SET book_status='%s' " +
                " WHERE action_id ='%s'", status, aid);
        Connection conn = sqldb.getConnector();
        Statement stt = conn.createStatement();
        return stt.executeUpdate(updateSql);
    }


    //----------- support methods -------------------
    private PreparedStatement getPreparedInsertBook(BookData data) throws SQLException
    {
        Connection conn = sqldb.getConnector();
        PreparedStatement ppStt = conn.prepareStatement(SqlCommands.insertBookSql());
        ppStt.setString(1, data.getActionId());
        ppStt.setString(2, data.getFileNames());
        ppStt.setString(3, data.getInvoiceNbr());
        ppStt.setString(4, data.getClientName());
        ppStt.setString(5, data.getEventTitle());
        ppStt.setString(6, data.getClientEmail());
        ppStt.setString(7, data.getClientTelephone());
        ppStt.setString(8, data.getOrganisation());
        ppStt.setString(9, data.getBookStatus());
        ppStt.setString(10, data.getBookNote());
        return ppStt;
    }

    private PreparedStatement getPreparedInsertInvoice(BookData data) throws SQLException
    {
        Connection conn = sqldb.getConnector();
        PreparedStatement ppStt = conn.prepareStatement(SqlCommands.insertInvoiceSql());
        ppStt.setString(1, data.getActionId());
        ppStt.setString(2, data.getFileNames());
        ppStt.setString(3, data.getBookFileName());
        ppStt.setString(4, data.getInvoiceNbr());
        ppStt.setString(5, data.getPurchaseOrder());
        ppStt.setString(6, data.getPayStatus());
        ppStt.setString(7, data.getClientName());
        ppStt.setString(8, data.getEventTitle());
        ppStt.setString(9, data.getClientEmail());
        ppStt.setString(10, data.getClientTelephone());
        ppStt.setString(11, data.getOrganisation());
        ppStt.setString(12, data.getBookNote());
        return ppStt;
    }

    private void insertPreparedInsertDetail(BookData data) throws SQLException
    {
        if(checkTableRecord("hall_book_details", data.getActionId()))
            return; //existing one
        List<String> timeList = data.getTimeList();
        List<String> spaceList = data.getSpaceList();
        List<String> priceList = data.getPriceList();
        List<String> durationList = data.getDurationList();
        Connection conn = sqldb.getConnector();
        conn.setAutoCommit(false);
        PreparedStatement ppStt = conn.prepareStatement(SqlCommands.insertBookDetailsSql());
        for(int i=0; i<timeList.size(); i++)
        {
            ppStt.setString(1, data.getActionId());
            ppStt.setString(2, timeList.get(i));
            ppStt.setString(3, spaceList.get(i));
            ppStt.setString(4, priceList.get(i));
            ppStt.setString(5, durationList.get(i));
            ppStt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ppStt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ppStt.addBatch();
        }
        int[] rows = ppStt.executeBatch();
        conn.commit();
        conn.setAutoCommit(true);
    }


    //some calculation
    private String calcTotalHours(List<String> durList)
    {
        String totalHours="0";
        int minutes = 0;
        for(int i=0; i<durList.size(); i++) {
            String m = durList.get(i);
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
        return totalHours;
    }

    private String calcuTotalPrice(List<String> priceList)
    {
        float totalFee = 0;
        for(int i=0; i<priceList.size(); i++)
        {
            String pf = priceList.get(i);
            try {
                float tp = Float.parseFloat(pf);
                totalFee+=tp;
            }catch(Exception ex) { ex.printStackTrace(); }
        }
        return "£"+String.format("%.02f", totalFee);
    }

    private String getAllSpaces(List<String> spaceLst)
    {
        Set<String> lst = new LinkedHashSet<>();
        for(int i=0; i<spaceLst.size(); i++)
        {
            String sp = spaceLst.get(i);
            lst.add(sp);
        }
        return lst.toString();
    }
}
