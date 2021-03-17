package data;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * This class executes queries and returns result sets on the database
 * defined in data.DBConnection.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class DBStatement {
    //Variables
    //Statement Reference
    private static Statement statement;
    
    //Methods
    /**
     * This method sets the statement object and points it at the database
     * defined in data.DBConnection.
     * 
     * @throws SQLException SQLException to be handled.
     */
    public static void setStatement() throws SQLException{
        DBStatement.statement = DBConnection.getConn().createStatement();
    }
    
    /**
     * Getter for the statement.
     * 
     * @return The Statement to get.
     */
    public static Statement getStatement(){
        return DBStatement.statement;
    }
    
    /**
     * Executes the query defined in the query String on the database that 
     * setStatement() points to.
     * 
     * @param query         The String holding the SQL query to execute.
     * @throws SQLException The SQLException to handle.
     */
    public static void executeQuery(String query) throws SQLException{
        DBStatement.setStatement();
        if (DBStatement.statement.execute(query) == false){
            if (DBStatement.statement.getUpdateCount() > 0){
                System.out.println("Rows effected: " + DBStatement.statement.getUpdateCount());
            }else{
                System.out.println("No rows effected.");
            }
        }
    }
    
    /**
     * Returns the ResultSet returned from the last executed SQL query.
     * 
     * @return              The ResultSet to return.
     * @throws SQLException The SQLException to handle.
     */
    public static ResultSet getResultSet() throws SQLException {
        return DBStatement.statement.getResultSet();
    }
}
