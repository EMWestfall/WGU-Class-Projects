package reports;

import java.time.Month;

/**
 * This is the class representing the CustomerAppointment object.
 * It stores the results of a grouped by SELECT query in CustomerAppointmentDAO.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class CustomerAppointment {
    private int lineNumber;
    private String customerName;
    private Month month;
    private int numberAppts;
    
    /**
     * The constructor for the CustomerAppointment object.
     * 
     * @param lineNumber    Line number; primarily for iterating over a set.
     * @param customerName  The String of the customer's name.
     * @param month         The Month being reported on.
     * @param numAppts      The number of appts the customer has in the month.
     */
    public CustomerAppointment(int lineNumber, String customerName, Month month, int numAppts){
        this.lineNumber = lineNumber;
        this.customerName = customerName;
        this.month = month;
        this.numberAppts = numAppts;
    }
    
    /**
     * Getter for line number.
     * 
     * @return The line number of the customer appointment report.
     */
    public int getLineNumber(){
        return this.lineNumber;
    }
    
    /**
     * Getter for customer name.
     * 
     * @return The customer's name.
     */
    public String getCustomerName(){
        return this.customerName;
    }
    
    /**
     * Getter for the month being reported on.
     * 
     * @return The Month being reported on.
     */
    public Month getMonth(){
        return this.month;
    }
    
    /**
     * Getter for the number of appointments in the given month for a customer.
     * 
     * @return The number of appointments in the given month for a customer.
     */
    public int getNumberAppts(){
        return this.numberAppts;
    }
    
    /**
     * Setter for line number.
     * 
     * @param lineNum The line number.
     */
    public void setLineNumber(int lineNum){
        this.lineNumber = lineNum;
    }
    
    /**
     * Setter for customer name.
     * 
     * @param custName The customer's name.
     */
    public void setCustomerName(String custName){
        this.customerName = custName;
    }
    
    /**
     * Setter for the month being reported on.
     * 
     * @param month The Month being reported on.
     */
    public void setMonth(Month month){
        this.month = month;
    }
    
    /**
     * Setter for the number of appointments for this customer in this month.
     * 
     * @param numberAppts The number of appts for this customer in this month.
     */
    public void setNumAppts(int numberAppts){
        this.numberAppts = numberAppts;
    }
}
