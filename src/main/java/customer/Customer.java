package customer;
/**
 * The class representing the Customer object.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class Customer {
    private final int customerId; //auto-incremented so never changes
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String createdBy;
    private String lastUpdatedBy;
    private int divisionId;
    
    /**
     * Constructor for the Customer object.
     * 
     * @param customerId    The customer id.
     * @param name          The String of the customer's name.
     * @param address       The String of the customer's address.
     * @param postalCode    The String of the customer's postal code.
     * @param phone         The String of the customer's phone number.
     * @param createdBy     The String of the user that created this customer.
     * @param lastUpdatedBy The String of the last user to update.
     * @param divisionId    The division id of the customer's location.
     */
    public Customer(int customerId, String name, String address, String postalCode, String phone, String createdBy, String lastUpdatedBy, int divisionId){
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        System.out.println("Successfully created new Customer.");
    }
    
    //--------Setters-----------
    /**
     * Setter for the customer's name.
     * 
     * @param name The String of the customer's name.
     */
    public void setName(String name){
        this.name = name;
    };
    
    /**
     * Setter for the customer's address.
     * 
     * @param address The String of the customer's address.
     */
    public void setAddress(String address){
        this.address = address;
    };
    
    /**
     * Setter for the customer's postal code.
     * 
     * @param postal The String of the customer's postal code.
     */
    public void setPostalCode(String postal){
        this.postalCode = postal;
    };
    
    /**
     * Setter for the customer's phone number.
     * 
     * @param phoneNum The String of the customer's phone number.
     */
    public void setPhone(String phoneNum){
        this.phone = phoneNum;
    };
    
    /**
     * Setter for the customer record's creator.
     * 
     * @param userName The String of the customer record's creator.
     */
    public void setCreatedBy(String userName){
        this.createdBy = userName;
    };
    
    /**
     * Setter for the customer record's last updated user.
     * 
     * @param userName The String of the customer record's last updated user.
     */
    public void setLastUpdatedBy(String userName){
        this.lastUpdatedBy = userName;
    };
    
    /**
     * Setter for the customer's division id.
     * 
     * @param division The id for the division of the customer's location.
     */
    public void setDivisionId(int division){
        this.divisionId = division;
    };
    
    //----------Getters------------
    
    /**
     * Returns the customer's id.
     * 
     * @return The customer's id number.
     */
    public int getCustomerId(){
        return this.customerId;
    }
    
    /**
     * Returns the customer's name.
     * 
     * @return The String of the customer's name.
     */
    public String getName(){
        return this.name;
    };
    
    /**
     * Returns the customer's address.
     * 
     * @return The String of the customer's address.
     */
    public String getAddress(){
        return this.address;
    };
    
    /**
     * Returns the customer's postal code.
     * 
     * @return The String of the customer's postal code.
     */
    public String getPostalCode(){
        return this.postalCode;
    };
    
    /**
     * Returns the customer's phone number.
     * 
     * @return The String of the customer's phone number.
     */
    public String getPhone(){
        return this.phone;
    };
    
    /**
     * Returns the customer record's creator.
     * 
     * @return The String of the customer record's creator.
     */
    public String getCreatedBy(){
        return this.createdBy;
    };
    
    /**
     * Returns the customer record's last update user.
     * 
     * @return The String of the customer record's last update user.
     */
    public String getLastUpdatedBy(){
        return this.lastUpdatedBy;
    };
    
    /**
     * Returns the customer's division id.
     * 
     * @return The customer's division id.
     */
    public int getDivisionId(){
        return this.divisionId;
    };
}
