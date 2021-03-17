package data;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class defines the database to connect to. Queries are handled in a
 * separate class: data.DBStatement.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class DBConnection {
    //Variables
    //UCertify Database Information
    private static final String SERVERNAME = "wgudb.ucertify.com";
    private static final int PORT = 3306;
    private static final String DBNAME = "WJ06PIt";
    private static final String USERNAME = "U06PIt";
    private static final String DBPASSWORD = "53688829617";
    //JDBC Information
    private static final String PROTOCOL = "jdbc";
    private static final String DBVENDOR = "mysql";
    private static String jdbcURL = DBConnection.PROTOCOL + ":" + DBConnection.DBVENDOR + "://" + DBConnection.SERVERNAME + ":" + DBConnection.PORT + "/" + DBConnection.DBNAME;
    //Driver and Connection Interface References
    private static final String MYSQLJDBCDRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection;
    
    
    //Connection Methods
    /**
     * Establishes the connection to the database using the static class vars.
     * 
     * @return The Connection to return.
     */
    public static Connection makeConnection(){
        try{
            Class.forName(DBConnection.MYSQLJDBCDRIVER);
            DBConnection.connection = (Connection)DriverManager.getConnection(DBConnection.jdbcURL, DBConnection.USERNAME, DBConnection.DBPASSWORD);
        }catch(ClassNotFoundException | SQLException e){
            System.out.println(e);
            System.out.println("Connection was unsuccessful, see stack trace above.");
        }
        System.out.println("Connection was successful.");
        
        return connection;
    }
    
    /**
     * Closes the database connection.
     */
    public static void closeConnection(){
        try{
            DBConnection.connection.close();
            System.out.println("Successful closing connection.");
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("Unsuccessful closing connection, see stack trace.");
        }
    }
    
    /**
     * Getter for the JDBC URL defined in the class variables.
     * 
     * @return The String containing the JDBC URL.
     */
    public static String getJdbcUrl(){
        return DBConnection.jdbcURL;
    }
    
    /**
     * Getter for the Connection object pointed at the database.
     * 
     * @return The Connection object pointed at the database.
     */
    public static Connection getConn(){
        return DBConnection.connection;
    }
    
}
