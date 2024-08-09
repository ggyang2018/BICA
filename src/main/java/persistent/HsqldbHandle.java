package persistent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.*;

/*
HsqldbHandle is hand Hsqldb connection and environment
Class.forName("org.hsqldb.jdbc.JDBCDriver"); can be omitted for new JDBC
following connection will shutdown database after the connection is closed
DriverManager.getConnection("jdbc:hsqldb:file:PATH_TO_MY_DB;shutdown=true","sa","");
if not connection can be open or close many times.
Its good practice to always explicitly close result sets, statements, and connections.
JDBC drivers turn on auto-commit mode for new database connections by default.
manually: connection.setAutoCommit(true);
@author Guang Yang
 */
public class HsqldbHandle
{
    private static final Logger logger = LogManager.getLogger(HsqldbHandle.class);

    Path dbPath;
    String dbFolder;
    String dbName;

    Connection connector;

    boolean databaseOn;
    public HsqldbHandle()
    {
        dbFolder = "lkcdata";
        dbName = "lkcbooks";
        try {
            dbPath = Paths.get(dbFolder);
            if (Files.notExists(dbPath))
                Files.createDirectories(dbPath);
            Path abs = dbPath.toAbsolutePath();
            logger.debug(abs.toString());
            databaseOn = true;
        }catch(IOException ioe) {
            databaseOn = false;
            ioe.printStackTrace();
            logger.debug("{}", ioe);
        }
    }

    public boolean hasDatabaseOn() { return databaseOn;}

   public Connection getConnector() throws SQLException
   {
       if(connector == null || connector.isClosed()) {
           String dbUrl = String.format("jdbc:hsqldb:file:%s/%s", dbFolder, dbName);
           connector = DriverManager.getConnection(dbUrl, "SA", "");
           connector.setAutoCommit(true);
       }
       return connector;
   }

   public void createBookTable(Connection cnn) throws SQLException
   {   //auto committed, re-use statement object
       Statement stt = cnn.createStatement();
       stt.executeUpdate(SqlCommands.makeBookTableSql());
       stt.executeUpdate(SqlCommands.makeInvoiceTableSql());
       stt.executeUpdate(SqlCommands.makeBookDetailTableSql());
       stt.close();
   }

}
