/*
 * Connect/disconnect from given MySQL database
 */
package Database;
import java.sql.*;
/**
 * @author c.parrott
 */

public class Connect {
    private static final String SERVER = "3.227.166.251";
    private static final String DBNAME = "U05gAT";
    private static final String UNAME = "U05gAT";
    private static final String PASS = "53688496816";
    private static final String DBURL = "jdbc:mysql://"+SERVER+"/"+DBNAME;
    
    public static Connection CONN;
    
    //Connect to database
    public static Connection connect() throws SQLException{
        Connection conn = DriverManager.getConnection(DBURL,UNAME,PASS);      
        System.out.println("Connected to database.");
        return conn;
    }
    
    //Disconnect from database
    public static void disconnect(Connection conn) throws SQLException{
        if(conn.isValid(0)){
            conn.close();
            System.out.println("Disconnected from database.");
        }
        else{
            System.out.println("Error disconnecting from database.");
        }
    }
    
}
