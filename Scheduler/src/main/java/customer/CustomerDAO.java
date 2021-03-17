package customer;


/**
 * This interface is for customer data in the DAO format. It is implemented by CustomerDAOImp.
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public interface CustomerDAO {
    /**
     * This method adds a customer to the DAO's customer HashMap.
     * 
     * @param customerId    The customer id.
     * @param name          The String with the customer's name.
     * @param address       The String with the customer's address.
     * @param postalCode    The String with the customer's postal code.
     * @param phone         The String with the customer's phone number.
     * @param createdBy     The String with the customer's creator.
     * @param lastUpdatedBy The String with the customer's last updated user.
     * @param divisionId    The division id that is the customer's location.
     */
    public void addCustomerToMap(int customerId, String name, String address, String postalCode, String phone, String createdBy, String lastUpdatedBy, int divisionId);
    
    /**
     * This method does an INSERT INTO query to insert a customer.
     * @param name          The String with the customer's name.
     * @param address       The String with the customer's address.
     * @param postalCode    The String with the customer's postal code.
     * @param phone         The String with the customer's phone number.
     * @param divisionId    The division id that is the customer's location. 
     */
    public void addCustomerToDatabase(String name, String address, String postalCode, String phone, String divisionId);
    
    /**
     * This method does a DELETE query on the customer with ID = customerId.
     * @param customerId The id of the customer to delete.
     */
    public void deleteCustomer(int customerId);
    
    /**
     * This method does a SELECT query to retrieve customers from the database.
     */
    public void getCustomers();
    
    /**
     * This method does an UPDATE query on the customer in the database.
     * @param customerId    The id of the customer.
     * @param name          The String with the customer's name.
     * @param address       The String with the customer's address.
     * @param postalCode    The String with the customer's postal code.
     * @param phone         The String with the customer's phone number.
     * @param divisionId    The division id that is the customer's location. 
     */
    public void updateCustomer(int customerId, String name, String address, String postalCode, String phone, String divisionId);
}
