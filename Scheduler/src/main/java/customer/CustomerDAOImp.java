package customer;

import data.DBStatement;
import division.Division;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import scenecontroller.SceneController;

/**
 * This the Customer DAO implementation class. It contains the customer DAO
 * data. It sends queries to the DBStatement class to send to the database and
 * stores customer data in return.
 * 
 * @author bdudu
 * @version 1.0, 01/22/21
 */
public class CustomerDAOImp implements CustomerDAO {
    //---------------Variables------------------
    private HashMap<Integer, Customer> customerMap;
    private int custToEditId;
    
    //--------------Constructor----------------------
    /**
     * The constructor for the CustomerDAO. Initializes the HashMap and then
     * sends a SELECT query to retrieve customers from the database.
     */
    public CustomerDAOImp(){
        this.customerMap = new HashMap<Integer, Customer>();
        this.getCustomers();
        System.out.println("Successfully created CustomerDAOImp.");
    }
    
    //------------Interface Variables----------------------
    /**
     * This method adds a customer to the HashMap.
     * 
     * @param customerId    The customer's id.
     * @param name          The String with the customer's name.
     * @param address       The String with the customer's address.
     * @param postalCode    The String with the customer's postal code.
     * @param phone         The String with the customer's phone number.
     * @param createdBy     The String with the customer's creator.
     * @param lastUpdatedBy The String with the customer's last user to update.
     * @param divisionId    The customer's location as a division id.
     */
    public void addCustomerToMap(int customerId, String name, String address, String postalCode, String phone, String createdBy, String lastUpdatedBy, int divisionId){
        this.customerMap.put(customerId, new Customer(customerId, name, address, postalCode, phone, createdBy, lastUpdatedBy, divisionId));
    }
    
    /**
     * This method is used when the user adds a new customer using the
     * CustomerEditView. It performs an INSERT INTO to add a new customer.
     * 
     * @param name          The String with the customer's name.
     * @param address       The String with the customer's address.
     * @param postalCode    The String with the customer's postal code.
     * @param phone         The String with the customer's phone number.
     * @param divisionName  The customer's location as a division id.
     */
    public void addCustomerToDatabase(String name, String address, String postalCode, String phone, String divisionName){
        
        int divisionId = 0;
        //O(n) search, could be improved later.
        division.DivisionDAO divDao = data.DAOCollection.getDivDao();
        for(var key : divDao.getDivMapKeys()){
            Division division = divDao.getDivision((int) key);
            if(division.getDivName().equals(divisionName)) divisionId = division.getDivId();
        }
        
        if (divisionId == 0){
            SceneController.loadAlert("Division not found!");
            return;
        }
        
        try{
            String userName = data.DAOCollection.getUserDao().getUser(data.DAOCollection.getUserDao().getCurrentUserId()).getUserName();
            String query = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID, Last_Updated_By, Created_By)\n"
                            + "VALUES (\"" + name + "\", \"" + address + "\", \"" + postalCode + "\", \"" + phone + "\", \"" + divisionId + "\", \""
                            + userName + "\", \"" + userName + "\");";
            System.out.println("Query: \n" + query);
            DBStatement.executeQuery(query);
            System.out.println("INSERT INTO in CustomerDAOImp.addCustomerToDatabase() successful.");
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in INSERT INTO in CustomerDAOImp.addCustomerToDatabase().");
        }
    }
    
    /**
     * This query deletes a customer from the database using a DELETE FROM.
     * 
     * @param customerId The id of the customer to delete.
     */
    public void deleteCustomer(int customerId){
        try{
            String query  = "DELETE FROM appointments WHERE Customer_ID = " + customerId;
            System.out.println("Query: \n" + query);
            DBStatement.executeQuery(query);
            query = "DELETE FROM customers WHERE Customer_ID = " + customerId;
            System.out.println("Query: \n" + query);
            DBStatement.executeQuery(query);
            System.out.println("DELETE FROM in CustomerDAOImp.deleteCustomer() successful.");
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in DELETE FROM in CustomerDAOImp.deleteCustomer().");
        }
    }
    
    /**
     * This method sends a SELECT query to the database to retrieve customers
     * and then adds them to the HashMap.
     */
    public void getCustomers(){
        //Clear the Hashmap if it's not empty
        if (this.customerMap.isEmpty()){
            System.out.println("Customer map already clear.");
        }else{
            this.customerMap.clear();
            System.out.println("Successfully cleared customer map.");
        }
        //Retrieve the customers from the database server
        try{
            String query = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID\n"
                                + "FROM customers;";
            System.out.println(query);
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in CustomerDAOImp.getCustomers() during SQL execution.");
        }
        //Iterate over the result set and insert customers into the Hashmap
        try{
            ResultSet rs = DBStatement.getResultSet();
            rs.beforeFirst();
            while(rs.next()){
                this.addCustomerToMap(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8));
                System.out.println("Customer added to map.");
            }
            System.out.println("Successfully iterated through result set for customers.");
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in CustomerDAOImp.getCustomers() during record set iteration.");
        }
        
    }
    
    /**
     * This method sends an UPDATE query to the database to update the customer
     * with customerId number.
     * 
     * @param customerId    The id of the customer to update.
     * @param name          The String with customer's name.
     * @param address       The String with the customer's address.
     * @param postalCode    The String with the customer's postal code.
     * @param phone         The String with the customer's phone number.
     * @param divisionName  The customer's location as a division id number.
     */
    public void updateCustomer(int customerId, String name, String address, String postalCode, String phone, String divisionName){
        
        //Retrieve the customer in its current, unupdated state to compare to the suggested updates
        Customer cust = this.customerMap.get(customerId);
        HashMap<String, String> varToChange = new HashMap<String, String>();
        
        //Reverse lookup the division ID
        int divisionId = 0;
        //O(n) search, could be improved later.
        division.DivisionDAO divDao = data.DAOCollection.getDivDao();
        for(var key : divDao.getDivMapKeys()){
            Division division = divDao.getDivision((int) key);
            if(division.getDivName().equals(divisionName)) divisionId = division.getDivId();
        }
        
        if (divisionId == 0){
            SceneController.loadAlert("Division not found!");
            return;
        }
        
        //if a field is being changed, add it to the map
        if (!name.equals(cust.getName())) varToChange.put("Customer_Name", name);
        if (!address.equals(cust.getAddress())) varToChange.put("Address", address);
        if (!postalCode.equals(cust.getPostalCode())) varToChange.put("Postal_Code", postalCode);
        if (!phone.equals(cust.getPhone())) varToChange.put("Phone", phone);
        if (divisionId != cust.getDivisionId()) varToChange.put("Division_ID", String.valueOf(divisionId));
        
        //Append the SET clause variables
        int counter = 0;
        String queryAppend = "";
        for (String key : varToChange.keySet()){
            if (counter == 0){
                queryAppend += key + " = \"" + varToChange.get(key) + "\"";
            }else{
                queryAppend += ", " + key + " = \"" + varToChange.get(key) + "\"";
            }
            counter++;
        }
        
        //Construct and execute the query
        try{
            String userName = data.DAOCollection.getUserDao().getUser(data.DAOCollection.getUserDao().getCurrentUserId()).getUserName();
            String query = "UPDATE customers SET " + queryAppend + ", Last_Updated_By = \"" + userName + "\" WHERE Customer_ID = " + String.valueOf(customerId) + ";";
            System.out.println(query);
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQL Exception in executing query in CustomerDAOImp.updateCustomer().");
        }
    }
    
    //--------------Customer Edit Specific Methods----------------
    
    /**
     * When the edit button is pressed on the CustomerView, the controller
     * passes the customer to edit to this DAO, indicating the customer that
     * will undergo editing...this method returns that customer number.
     * 
     * @return The id of the customer to edit.
     */
    public int getCustomerToEdit(){
        return this.custToEditId;
    }
    
    /**
     * When the edit button is pressed on the CustomerView, the controller
     * passes the customer to edit to this DAO, indicating the customer that
     * will undergo editing...this method sets that customer number.
     * 
     * @param custToEdit The id of the customer to edit.
     */
    public void setCustomerToEdit(int custToEdit){
        this.custToEditId = custToEdit;
        System.out.println("Successful CustomerDAOImp.setCustomerToEdit()");
    }
    
    //--------------Other Customer Methods------------------
    
    /**
     * This method returns a customer by id number from the HashMap.
     * 
     * @param custNumber    The id of the customer.
     * @return              The Customer to return.
     */
    public Customer getCustomer(int custNumber){
        return this.customerMap.get(custNumber);
    }
    
    /**
     * Returns the set of all customer ids currently in the HashMap. This is
     * typically used in conjunction with getCustomer(int) to iterate over the
     * HashMap.
     * 
     * @return The Set containing the customer ids.
     */
    public Set<Integer> getCustomerKeys(){
        return this.customerMap.keySet();
    }
}
