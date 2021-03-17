package contact;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * This is the DAO controller for the Contact data. It sends queries to the
 * database through the data.DBStatement class and stores the results in its
 * HashMap.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class ContactDAO {
    private HashMap<Integer, Contact> contMap;
    
    /**
     * This is the constructor for the ContactDAO. It initializes the HashMap
     * and then gets contacts from the database.
     */
    public ContactDAO(){
        this.contMap = new HashMap<>();
        this.getContacts();
    }
    
    /**
     * This method adds a contact to the HashMap.
     * 
     * @param contToAdd The Contact to add to the HashMap.
     */
    private void addContactToMap(Contact contToAdd){
        this.contMap.put(contToAdd.getContId(), contToAdd);
    }
    
    /**
     * Sends a SELECT query to the database, receives contacts in return, and
     * then adds them to the HashMap.
     */
    private void getContacts(){
        //Clear the Hashmap if it's not empty
        if (this.contMap.isEmpty()){
            System.out.println("Contact map already clear.");
        }else{
            this.contMap.clear();
            System.out.println("Successfully cleared contact map.");
        }
        //Retrieve the contacts from the database server
        try{
            String query = "SELECT Contact_ID, Contact_Name\n"
                                + "FROM contacts;";
            System.out.println(query);
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in ContactDAO.getContacts() during SQL execution.");
        }
        //Iterate over the result set and insert customers into the Hashmap
        try{
            ResultSet rs = DBStatement.getResultSet();
            rs.beforeFirst();
            while(rs.next()){
                this.addContactToMap(new Contact(rs.getInt(1), rs.getString(2)));
                System.out.println("Contact added to map.");
            }
            System.out.println("Successfully iterated through result set for contacts.");
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in ContactDAO.getContacts() during record set iteration.");
        }
        
    }
    
    //Getters
    
    /**
     * Getter for the set of contact ids in the HashMap. Usually used for 
     * iterating over the HashMap.
     * 
     * @return The Set of contact ids.
     */
    public Set getContactMapKeys(){
        return this.contMap.keySet();
    }
    
    /**
     * Getter for a contact from the HashMap by the contact id number.
     * 
     * @param contId    The contact's id.
     * @return          The Contact to return from the HashMap.
     */
    public Contact getContact(int contId){
        return this.contMap.get(contId);
    }
}
