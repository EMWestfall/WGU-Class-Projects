package appointment;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import scenecontroller.SceneController;

/**
 * This class is the Appointment object used throughout the application.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class Appointment {
    private final int APPOINTMENTID; //auto-incremented and so no setter method
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createDate;
    private String createdByUser;
    private LocalDateTime lastUpdate;
    private String lastUpdatedUser;
    private int customerId;
    private String customerName;
    private int userId;
    private String userName;
    private int contactId;
    private String contactName;
    
    /**
     * This is the constructor for the Appointment object.
     * 
     * @param appId             The appointment's id number.
     * @param title             The String of the appointment's title.
     * @param description       The String of the appointment's description.
     * @param location          The String of the appointment's location.
     * @param type              The String of the appointment's type.
     * @param startTime         The LocalDateTime of the appointment's start.
     * @param endTime           The LocalDateTime of the appointment's end.
     * @param createDate        The LocalDateTime of the appointment's creation.
     * @param createdByUser     The String of the appointment's creator.
     * @param lastUpdate        The LocalDateTime of the appt's last update.
     * @param lastUpdatedUser   The String of the appt's last user to update it.
     * @param custId            The appointment's customer's id.
     * @param userId            The appointment's user's id.
     * @param contactId         The appointment's contact's id.
     */
    public Appointment(int appId,
        String title, 
        String description,
        String location, 
        String type, 
        LocalDateTime startTime, 
        LocalDateTime endTime, 
        LocalDateTime createDate,
        String createdByUser,
        LocalDateTime lastUpdate,
        String lastUpdatedUser, 
        int custId, 
        int userId, 
        int contactId){
            this.APPOINTMENTID = appId;
            this.title = title;
            this.description = description;
            this.location = location;
            this.type = type;
            this.startTime = startTime;
            this.endTime = endTime;
            this.createDate = createDate;
            this.createdByUser = createdByUser;
            this.lastUpdate = lastUpdate;
            this.lastUpdatedUser = lastUpdatedUser;
            this.customerId = custId;
            this.userId = userId;
            this.contactId = contactId;
    }
    
    /**
     * This method returns the Appointment's info for printing to the console.
     * Used for debugging purposes.
     * 
     * @return The String with the Appointment's info.
     */
    @Override public String toString(){
        return "App ID: " + this.APPOINTMENTID + "\nTitle: " + this.title + "\nDescription: " + this.description + "\nLocation: " + this.location +
                "\nType: " + this.type + "\nStart: " + this.startTime.toString() + "\nEnd: " + this.endTime.toString() + "\nCreated: " + this.createDate.toString() +
                "\nCreated By: " + this.createdByUser + "\nLast Update: " + this.lastUpdate.toString() + "\nLast Updated By: " + this.lastUpdatedUser +
                "\nCustomer ID: " + this.customerId + "\nUser ID: " + this.userId + "\nContact ID: " + this.contactId + "\n";
    }

    
    //------------Getter Methods--------------
    
    /**
     * Returns the appointment's id.
     * 
     * @return The appointment's id.
     */    
    public int getAppointmentId(){
        return this.APPOINTMENTID;
    }
    
    /**
     * Returns the appointment's title.
     * 
     * @return The String representing the appointment's title.
     */ 
    public String getTitle(){
        return this.title;
    }
    
    /**
     * Returns the appointment's description.
     * 
     * @return The String representing the appointment's description.
     */ 
    public String getDescription(){
        return this.description;
    }
    
    public String getLocation(){
        return this.location;
    }
    
    /**
     * Returns the appointment's type.
     * 
     * @return The String representing the appointment's type.
     */ 
    public String getType(){
        return this.type;
    }
    
    /**
     * Returns the start LocalDateTime as a LocalDate.
     * Used primarily for the calendar date picker in AppointmentEditController.
     * 
     * @return The LocalDate of the start DateTime.
     */
    public LocalDate getStartDateForPicker(){
        return this.startTime.toLocalDate();
    }
    
    /**
     * Returns the end LocalDateTime as a LocalDate.
     * Used primarily for the calendar date picker in AppointmentEditController.
     * 
     * @return The LocalDate of the end DateTime.
     */
    public LocalDate getEndDateForPicker(){
        return this.endTime.toLocalDate();
    }
    
    /**
     * Returns the start's time as a String in (HH:mm) format.
     * 
     * @return The String of the appointment's start time.
     */
    public String getStartTimeString(){
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("HH:mm");
        return this.startTime.format(dtFormat);
    }
    
    /**
     * Returns the end's time as a String in (HH:mm) format.
     * 
     * @return The String of the appointment's end time.
     */
    public String getEndTimeString(){
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("HH:mm");
        return this.endTime.format(dtFormat);
    }
    
    /**
     * Returns the start's datetime as a string in (LLL dd, yyyy HH:mm) format,
     * which is intended to be user friendly and readable.
     * 
     * @return The String of the start's datetime.
     */
    public String getStartDateTimeString(){//Returns the start time in LLL dd, yyyy HH:mm format, which is end user friendly
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy HH:mm");
        return this.startTime.format(dtFormat);
    }
    
    /**
     * Returns the end's datetime as a string in (LLL dd, yyyy HH:mm) format,
     * which is intended to be user friendly and readable.
     * 
     * @return The String of the end's datetime.
     */
    public String getEndDateTimeString(){//Returns the end time in LLL dd, yyyy HH:mm format, which is end user friendly
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy HH:mm");
        return this.endTime.format(dtFormat);
    }
    
    /**
     * Return's the start's datetime directly in Unix format.
     * 
     * @return The LocalDateTime of the appointment's start.
     */
    public LocalDateTime getStartTime(){//Returns the start LocalDateTime directly in Unix format
        return this.startTime;
    }
    
    /**
     * Return's the end's datetime directly in Unix format.
     * 
     * @return The LocalDateTime of the appointment's end.
     */
    public LocalDateTime getEndTime(){//Returns the end LocalDateTime directly in Unix format
        return this.endTime;
    }
    
    public LocalDateTime getCreateDate(){
        return this.createDate;
    }
    
    public String getCreatedByUser(){
        return this.createdByUser;
    }
    
    public LocalDateTime getLastUpdate(){
        return this.lastUpdate;
    }
    
    public String getLastUpdatedUser(){
        return this.lastUpdatedUser;
    }
    
    /**
     * Returns the appointment's customer's id.
     * 
     * @return The id of the appointment's customer.
     */
    public int getCustomerId(){
        return this.customerId;
    }
    
    /**
     * This method does a SELECT query on the database and returns the customer
     * who has the id stored in this appointment.
     * 
     * @return The String of the customer's name.
     */
    public String getCustomerName(){
        if (this.customerName == null){
            ResultSet rs;
            try{
                DBStatement.executeQuery("SELECT Customer_Name FROM customers WHERE Customer_ID = " + this.customerId + ";");
                rs = DBStatement.getResultSet();
                if(rs.next()){
                    this.customerName = rs.getString(1);
                }
            }catch(SQLException e){
                SceneController.loadAlert("Could not find customer name.");
            }
        }
        return this.customerName;
    }
    
    /**
     * This method returns the user id of this appointment.
     * 
     * @return The user's id.
     */
    public int getUserId(){
        return this.userId;
    }
    
    /**
     * This method returns the contact id of this appointment.
     * 
     * @return The contact's id.
     */
    public int getContactId(){
        return this.contactId;
    }
    
    /**
     * This method does a SELECT query on the database and returns the contact
     * who has the id stored in this appointment.
     * 
     * @return The String of the contact's name.
     */
    public String getContactName(){
        if (this.contactName == null){
            ResultSet rs;
            try{
                DBStatement.executeQuery("SELECT Contact_Name FROM contacts WHERE Contact_ID = " + this.contactId + ";");
                rs = DBStatement.getResultSet();
                if(rs.next()){
                    this.contactName = rs.getString(1);
                }
            }catch(SQLException e){
                SceneController.loadAlert("Could not find contact name.");
            }
        }
        return this.contactName;
    }
    
    
    //----------Setter Methods--------------
    
    /**
     * Setter method for appointment's title.
     * 
     * @param title The String of the title to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setTitle(String title){
        try{
            this.title = title;
            System.out.println("Successful setting appointment title.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment title.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's description.
     * 
     * @param description The String of the description to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setDescription(String description){
        try{
            this.description = description;
            System.out.println("Successful setting appointment description.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment description.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's location.
     * 
     * @param loc The String of the location to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setLocation(String loc){
        try{
            this.location = loc;
            System.out.println("Successful setting appointment location.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment location.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's type.
     * 
     * @param type The String of the type to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setType(String type){
        try{
            this.type = type;
            System.out.println("Successful setting appointment type.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment type.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's start time.
     * 
     * @param date The LocalDateTime of the start time to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setStartTime(LocalDateTime date){
        try{
            this.startTime = date;
            System.out.println("Successful setting appointment start date.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment start date.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's end time.
     * 
     * @param date The LocalDateTime of the date to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setEndTime(LocalDateTime date){
        try{
            this.endTime = date;
            System.out.println("Successful setting appointment end date.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment end date.");
            return false;
        }
    }
    
    /**
     * Overloaded function that sets a default parameter of now() to the
     * created by time setter.
     */
    public void setCreatedByTime(){ //With no parameters this overloaded function sets the created by time to the current system time.
        setCreatedByTime(LocalDateTime.now());
    }
    
    /**
     * Setter method for appointment's created by time..
     * 
     * @param dateTime The LocalDateTime of the created by time to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setCreatedByTime(LocalDateTime dateTime){
        try{
            this.createDate = dateTime;
            System.out.println("Successful setting appointment created by time.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment created by time.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's created by user.
     * 
     * @param user The String of the user name to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setCreatedByUser(String user){
        try{
            this.createdByUser = user;
            System.out.println("Successful setting appointment created by user.");        
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment created by user.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's last update datetime.
     * 
     * @param dateTime The LocalDateTime of the last update datetime to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setLastUpdated(LocalDateTime dateTime){
        try{
            this.lastUpdate = dateTime;
            System.out.println("Successful setting appointment last updated time.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment last updated time.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's last user to update.
     * 
     * @param user The String of the last user to update to set.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setLastUpdatedUser(String user){
        try{
            this.lastUpdatedUser = user;
            System.out.println("Successful setting appointment last updated user.");
            return true;
        }catch (Exception e){
            System.out.println("Failed to set appointment last updated user.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's customer id.
     * 
     * @param custId The id of the customer for this appointment.
     * @return  The boolean indicating whether the set was successful.
     */
    public boolean setCustomerId(int custId){
        try{
            this.customerId = custId;
            System.out.println("Successful setting appointment customer ID.");
            return true;
        }catch (Exception e){
            System.out.println("Failed setting appointment customer ID.");
            return false;
        }
    }
    
    /**
     * Overloaded setter method that defaults to the current active user and
     * passes it to setUserId(int userId).
     * 
     * @return The boolean indicated whether the set was successful.
     */
    public boolean setUserId(){
        try{
            this.setUserId(data.DAOCollection.getUserDao().getCurrentUserId());
            System.out.println("Successful setting user id in Appointment.");
            return true;
        }catch (Exception e){
            System.out.println("Failed setting user id in Appointment.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's user id.
     * 
     * @param userId The id of the user for this appointment.
     * @return       The boolean indicating whether the set was successful.
     */
    public boolean setUserId(int userId){
        try{
            this.userId = userId;
            System.out.println("Successful setting appointment user ID.");
            return true;
        }catch (Exception e){
            System.out.println("Failed setting appointment user ID.");
            return false;
        }
    }
    
    /**
     * Setter method for appointment's contact id.
     * 
     * @param contactId The id of the contact for this appointment.
     * @return          The boolean indicating whether the set was successful.
     */
    public boolean setContactId(int contactId){
        try{
            this.contactId = contactId;
            System.out.println("Successful setting contact ID.");
            return true;
        }catch (Exception e){
            System.out.println("Failed setting appointment contact ID.");
            return false;
        }
    }
}
