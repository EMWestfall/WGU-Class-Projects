package division;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * This is the DAO controller for Divisions. It sends queries through the
 * Database Statement controller and stores the results.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class DivisionDAO {
    HashMap<Integer, Division> divMap;
    
    /**
     * This is the contructor for DivisionDAO. It gets divisions from the
     * database and stores them in the divisions HashMap.
     */
    public DivisionDAO(){
        this.divMap = new HashMap<>();
        this.getDivisionsFromDatabase();
    }
    
    /**
     * This method places divisions in the HashMap.
     * 
     * @param divToAdd The Division to add to the HashMap.
     */
    private void addDivisionToMap(Division divToAdd){
        divMap.put(divToAdd.getDivId(), divToAdd);
    }
    
    /**
     * Sends a SELECT query to get divisions from the database and then calls
     * the appropriate methods to store them in the HashMap.
     */
    public void getDivisionsFromDatabase(){
        //Clear map if not clear
        if(!this.divMap.isEmpty()) this.divMap.clear();
        
        String query = "SELECT Division_ID, Division, COUNTRY_ID FROM first_level_divisions;";
        System.out.println(query);
        
        try{
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in query at DivisionDAO.getDivisions().");
        }
        ResultSet rs;
        try{
            rs = DBStatement.getResultSet();
            rs.beforeFirst();
            while(rs.next()){
                this.addDivisionToMap(new Division(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("Result set retrieval exception at DivisionDAO.getDivisions().");
        }
    }
    
    //Getters
    
    /**
     * Gets the key set of division ids from the HashMap, usually for iteration
     * purposes.
     * 
     * @return The Set of division ids.
     */
    public Set getDivMapKeys(){
        return this.divMap.keySet();
    }
    
    /**
     * Get the Division with the given id.
     * 
     * @param divId The division's id number.
     * @return      The Division to return.
     */
    public Division getDivision(int divId){
        return this.divMap.get(divId);
    }
}
