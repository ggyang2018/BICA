package persistent;

/**
 * This class only contain prepared statement query, simple query will in class code
 * All data are string exception create/update time
 * 1. Booking and invoice table separated link with action_id.
 * 2. Detail table will be shared by both booking and invoice so not foreign key constraints
 * 3. Invoice can be created from existing booking (using booking id) or from xls file (create
 * it own id)
 * 4. BookData will be used for both bookings and invoice
 * @author guangyang
 */

public class SqlCommands
{
    //------------ create sql table ----------------------
    public static String makeBookTableSql(){
        String sqlC = "CREATE TABLE IF NOT EXISTS hall_bookings ("
                +  " action_id varchar(50),"
                +  " file_name varchar(50),"
                +  " invoice_nbr varchar(50),"
                +  " client_name varchar(50),"
                +  " event_title varchar(100),"
                +  " client_email varchar(100),"
                +  " client_telephone varchar(50),"
                +  " organisation varchar(50),"
                +  " book_status varchar(50),"
                +  " book_note varchar(200), "
                +  " create_time timestamp,"
                +  " update_time timestamp, "
                +  " PRIMARY KEY (action_id))";
        return sqlC;
    }

    public static String makeInvoiceTableSql(){
        String sqlC = "CREATE TABLE IF NOT EXISTS hall_invoices ("
                +  " action_id varchar(50),"
                +  " file_name varchar(50),"
                +  " book_file_name varchar(50),"
                +  " invoice_nbr varchar(50),"
                +  " purchase_order varchar(50), "
                +  " pay_status varchar(25),"
                +  " client_name varchar(50),"
                +  " event_title varchar(100),"
                +  " client_email varchar(100),"
                +  " client_telephone varchar(50),"
                +  " organisation varchar(50),"
                +  " book_note varchar(200), "
                +  " create_time timestamp,"
                +  " update_time timestamp, "
                +  " PRIMARY KEY (action_id))";
        return sqlC;
    }
    //action_id may reference from both bookings and invices
    public static String makeBookDetailTableSql(){
        String sqlC = "CREATE TABLE IF NOT EXISTS hall_book_details("
                +  " action_id varchar(50), "
                +  " book_time varchar(100),"
                +  " book_space varchar(100),"
                +  " book_price varchar(50),"
                +  " book_duration varchar(40),"
                +  " create_time timestamp,"
                +  " update_time timestamp) ";
                //+ " FOREIGN KEY (action_id) REFERENCES hall_bookings (action_id))";
        return sqlC;
    }

    //------------ insert data Sql -----------------
    public static String insertBookSql(){
        String fields = " action_id,"
                +  " file_name,"
                +  " invoice_nbr,"
                +  " client_name,"
                +  " event_title,"
                +  " client_email,"
                +  " client_telephone,"
                +  " organisation,"
                +  " book_status,"
                +  " book_note, "
                +  " create_time,"
                +  " update_time";
        return  "INSERT INTO hall_bookings (" +fields +") "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String insertInvoiceSql(){
        String fields = " action_id,"
                +  " file_name,"
                +  " book_file_name,"
                +  " invoice_nbr,"
                +  " purchase_order,"
                +  " pay_status,"
                +  " client_name,"
                +  " event_title,"
                +  " client_email,"
                +  " client_telephone,"
                +  " organisation,"
                +  " book_note, "
                +  " create_time,"
                +  " update_time";
        return  "INSERT INTO hall_invoices (" +fields +") "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String insertBookDetailsSql(){
        String fields =  "action_id, "
                +  " book_time,"
                +  " book_space,"
                +  " book_price,"
                +  " book_duration,"
                +  " create_time,"
                +  " update_time";
        return "INSERT INTO hall_book_details (" +fields +") "
                + "VALUES (?,?,?,?,?,?,?)";
    }

    /******************** Reference Only ************************
    public static String createBookTable(){
        String sqlC = "CREATE TABLE IF NOT EXISTS hall_book ("
                +  " action_id varchar(40),"
                +  " file_name varchar(80),"   //may contain both book/invoice name
                +  " invoice_nbr varchar(40),"
                +  " client_name varchar(40),"
                +  " event_title varchar(40),"
                +  " client_email varchar(40),"
                +  " client_telephone varchar(40),"
                +  " organisation varchar(40),"
                +  " book_status varchar(40),"
                +  " book_type varchar(40),"
                +  " purchase_order varchar(30), "
                +  " book_note varchar(200), "
                +  " create_time timestamp,"
                +  " update_time timestamp, "
                +  " PRIMARY KEY (action_id))";
        return sqlC;
    }

    public static String insertBook(){
        String fields = " action_id,"
                +  " file_name,"
                +  " invoice_nbr,"
                +  " client_name,"
                +  " event_title,"
                +  " client_email,"
                +  " client_telephone,"
                +  " organisation,"
                +  " book_status,"
                +  " book_type,"
                +  " purchase_order, "
                +  " book_note, "
                +  " create_time,"
                +  " update_time";
        return  "INSERT INTO hall_book (" +fields +") "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
    public static String getTableBookData(){ return "select * from hall_book"; }
    */
}
