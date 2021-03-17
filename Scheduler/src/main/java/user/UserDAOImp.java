package user;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * This is the DAO controller for Users. It sends SQL queries to the
 * Database Statement controller and holds User information returned.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class UserDAOImp {
    private HashMap<Integer, User> userMap;
    private int currentUserId;
    
    /**
     * The constructor for the user DAO. Initializes the HashMap and gets
     * Users from the database.
     */
    public UserDAOImp(){
        this.userMap = new HashMap<Integer, User>();
        this.getUsers();
    }
    
    /**
     * Adds a user to the HashMap.
     * 
     * @param userToAdd The User to add to the HashMap.
     */
    private void addUserToMap(User userToAdd){
        this.userMap.put(userToAdd.getUserId(), userToAdd);
    }
    
    /**
     * Gets users from the database with a SELECT query and adds them to the
     * HashMap.
     */
    public void getUsers(){
        //Clear the Hashmap if it's not empty
        if (this.userMap.isEmpty()){
            System.out.println("User map already clear.");
        }else{
            this.userMap.clear();
            System.out.println("Successfully cleared user map.");
        }
        //Retrieve the contacts from the database server
        try{
            String query = "SELECT User_ID, User_Name, Password\n"
                                + "FROM users;";
            System.out.println(query);
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in UserDAOImp.getUsers() during SQL execution.");
        }
        //Iterate over the result set and insert customers into the Hashmap
        try{
            ResultSet rs = DBStatement.getResultSet();
            rs.beforeFirst();
            while(rs.next()){
                this.addUserToMap(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
                System.out.println("User added to map.");
            }
            System.out.println("Successfully iterated through result set for users.");
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in UserDAOImp.getUsers() during record set iteration.");
        }
        
    }
    
    /**
     * When the user logs in, the current user id is set and this function
     * returns it.
     * 
     * @return The id of the current user.
     */
    public int getCurrentUserId(){
        return this.currentUserId;
    }
    
    /**
     * This method returns the set of all keys in the user map.
     * 
     * @return The Set of User ids.
     */
    public Set getUserKeys(){
        return this.userMap.keySet();
    }
    
    /**
     * Returns a user with the given user id from the HashMap.
     * 
     * @param key   The user id.
     * @return      The User to return.
     */
    public User getUser(int key){
        return this.userMap.get(key);
    }
    
    /**
     * When the user logs in, this method sets the user's id as the current user
     * 
     * @param userId The user's id.
     */
    public void setCurrentUserId(int userId){
        this.currentUserId = userId;
    }
}
