package appointment;

import contact.ContactDAO;
import customer.CustomerDAOImp;
import data.DBStatement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import scenecontroller.SceneController;
import user.UserDAOImp;

/**
 * This is the controller for the AppointmentEditView.
 *
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class AppointmentEditController implements Initializable {
    
    private final AppointmentDAOImp dao = data.DAOCollection.getAppointmentDao();
    private final CustomerDAOImp custDao = data.DAOCollection.getCustomerDao();
    private final ContactDAO contDao = data.DAOCollection.getContDao();
    private final UserDAOImp userDao = data.DAOCollection.getUserDao();
    
    @FXML private Appointment apptToEdit;
    @FXML private TextField appointmentID;
    @FXML private TextField appointmentTitle;
    @FXML private TextArea appointmentDescription;
    @FXML private TextField appointmentLocation;
    @FXML private TextField appointmentType;
    @FXML private DatePicker appointmentStartDate;
    @FXML private TextField appointmentStartTime;
    @FXML private DatePicker appointmentEndDate;
    @FXML private TextField appointmentEndTime;
    @FXML private ChoiceBox appointmentContact;
    @FXML private ChoiceBox userChoice;
    @FXML private ChoiceBox appointmentCustomer;
    @FXML private Label userToEnterLabel;
    
    
    /**
     * This method handles what happens when the user clicks the cancel button.
     * Returns the user to the AppointmentView.
     */
    @FXML public void handleCancel(){
        SceneController.loadScene("Appointment");
        System.out.println("Successful handle cancel in appointment edit.");
    }
    
    /**
     * This method handles what happens when the user clicks the accept button.
     * First, this method checks whether the customerId and contactId
     * entered are in the database and displays a custom exception control 
     * if they're not. It then validates the times entered for being within
     * office hours, in the correct format, and not colliding with one another.
     */
    @FXML public void handleAccept(){
        String title = this.appointmentTitle.getText();
        String description = this.appointmentDescription.getText();
        String location = this.appointmentLocation.getText();
        String type = this.appointmentType.getText();
        String startDate;
        String endDate;
        String customer = this.appointmentCustomer.getValue().toString();
        String contact = this.appointmentContact.getValue().toString();
        int customerId = 0;
        int contactId = 0;
        int userId = 0;
        for(var key : this.userDao.getUserKeys()){
            if(this.userDao.getUser((int) key).getUserName().equals(this.userChoice.getValue().toString()))
                userId = (int)key;
        }
        ResultSet rs;
        
        //Get customer ID based on customer name.
        //First check if customer exists.
        try{
            System.out.println("Customer = " + customer);
            DBStatement.executeQuery("SELECT Customer_ID FROM customers WHERE customers.Customer_Name = \"" + customer + "\";");
        }catch(SQLException e){ //If not found, alert the user and return to the edit screen
            SceneController.loadAlert("Customer not found!");
            return;
        }
        try{
            rs = DBStatement.getResultSet();
            rs.next();
            customerId = rs.getInt(1);
        }catch(SQLException e){
            System.out.println("Exception getting result set for customers in AppointmentEditController.handleAccept.");
        }
        
        //Get contact ID based on contact name.
        //First check if contact exists.
        try{
            DBStatement.executeQuery("SELECT Contact_ID FROM contacts WHERE contacts.Contact_Name = \"" + contact + "\";");
        }catch(SQLException e){ //If not found, alert the user and return to the edit screen
            SceneController.loadAlert("Contact not found!");
            return;
        }
        try{
            rs = DBStatement.getResultSet();
            rs.next();
            contactId = rs.getInt(1);
        }catch(SQLException e){
            System.out.println("Exception getting result set for contacts in AppointmentEditController.handleAccept.");
        }
        
        
        //Validate DateTimes
        try{
            startDate = this.appointmentStartDate.getValue().toString() + " " + this.validateTime(this.appointmentStartTime.getText());
            endDate = this.appointmentEndDate.getValue().toString() + " " + this.validateTime(this.appointmentEndTime.getText());
        }catch(DateTimeParseException | NullPointerException e){
            //SceneController.loadAlert("Invalid time format. Must be hh:mm format.");
            return;
        }
        
        //Validate Appointment Start and End Times including Office Hours
        try{
            if((!this.validateOfficeHours(startDate, endDate) || !this.validateCollisionTimes(startDate, endDate)) ||
                    !this.validateStartBeforeEnd(startDate, endDate)){
                //SceneController.loadAlert("Invalid time: outside office\nhours or collides with another\nappointment for this customer.");
                return;
            } else {
            }
            System.out.println("Dates successfully validated.");
        }catch(DateTimeParseException | NullPointerException e){
            System.out.println(e);
        }
        
        //Execute Query depending on edit status
        if(this.dao.getAppointmentToEdit() == 0){
            System.out.println("getAppointmentToEdit() = " + this.dao.getAppointmentToEdit() + ", adding.");
            this.dao.addAppointmentToDatabase(title, description, location, type, startDate, endDate, customerId, userId, contactId);
        }else{
            System.out.println("getAppointmentToEdit() = " + this.dao.getAppointmentToEdit() + ", updating.");
            this.dao.updateAppointment(this.dao.getAppointmentToEdit(), title, description, location, type, startDate, endDate, customerId, userId, contactId);
        }
        System.out.println("Handled accept in appointment edit.");
        
        //Go back to appointments scene
        SceneController.loadScene("Appointment");
    }
    
    //Validations
    
    /**
     * This method validates that the time is in one of the acceptable formats 
     * and returns the String in its absolute correct format.
     * 
     * @param timeString                The String of dateTime to validate.
     * @return                          The String in the absolute format.
     * @throws DateTimeParseException   DateTimeParseExceptions to be handled.
     * @throws NullPointerException     NullPointerExceptions to be handled.
     */
    public String validateTime(String timeString) throws DateTimeParseException, NullPointerException{
        try{ //validates hh:mm
            LocalTime.parse(timeString + ":00");
            System.out.println("Time string is valid.");
            return timeString + ":00";
        }catch(DateTimeParseException | NullPointerException e1){
            try{ //validates h:mm
                LocalTime.parse("0" + timeString + ":00");
                System.out.println("Time string is valid.");
                return "0" + timeString + ":00";
            }catch(DateTimeParseException | NullPointerException e2){
                //validates hh:mm:ss
                LocalTime.parse(timeString);
                System.out.println("Time string is valid.");
                return timeString;
            }  
        }
    }
    
    
    /**
     * This method validates that the start time is before the end time.
     * 
     * @param start                     The String of the start datetime.
     * @param end                       The String of the end datetime.
     * @return                The boolean indicating if the start is before end.
     * @throws DateTimeParseException   DateTimeParseExceptions to be handled.
     * @throws NullPointerException     NullPointerExceptions to be handled.
     */
    public boolean validateStartBeforeEnd(String start, String end) throws DateTimeParseException, NullPointerException{
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, format);
        LocalDateTime endTime = LocalDateTime.parse(end, format);
        if(endTime.isBefore(startTime)){
            System.out.println("Invalid time entry: End is before start.");
            SceneController.loadAlert("Invalid time entry:\nEnd is before start.");
            return false;
        }
        System.out.println("Validated: Start is before end.");
        return true;
    }
    
    /**
     * Overloaded function that passes a default value of America/New_York to
     * the last parameter.
     * 
     * @param start The String of the start time.
     * @param end   The String of the end time.
     * @return      The boolean result of the overloaded function call.
     */
    public boolean validateOfficeHours(String start, String end){
        return this.validateOfficeHours(start, end, ZoneId.of("America/New_York"));
    }
    
    /**
     * Validates whether the given appointment start/end times are within
     * office hours at the given Zone.
     * 
     * @param start         The String of the start time to check. 
     * @param end           The String of the end time to check.
     * @param officeZone    The ZoneId of the zone to check.
     * @return              The boolean result of if the times are in office hrs
     * @throws DateTimeParseException   DateTimeParseExceptions to be handled.
     * @throws NullPointerException     NullPointerExceptions to be handled.
     */
    public boolean validateOfficeHours(String start, String end, ZoneId officeZone) throws DateTimeParseException, NullPointerException{
        //Convert the start local time to the office's local time zone
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localHere = LocalDateTime.parse(start, format);
        ZonedDateTime zonedHere = localHere.atZone(ZoneId.systemDefault());
        ZonedDateTime startInEST = zonedHere.withZoneSameInstant(officeZone);
        
        //Convert the end local time to the office's local time zone
        localHere = LocalDateTime.parse(end, format);
        zonedHere = localHere.atZone(ZoneId.systemDefault());
        ZonedDateTime endInEST = zonedHere.withZoneSameInstant(officeZone);
        
        //Check validities
        if(startInEST.getDayOfWeek().equals(DayOfWeek.SATURDAY) || 
            startInEST.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
            endInEST.getDayOfWeek().equals(DayOfWeek.SATURDAY) || 
            endInEST.getDayOfWeek().equals(DayOfWeek.SUNDAY)){ //Check if start or end falls on a weekend
                SceneController.loadAlert("Cannot add appointment:\nThis appointment falls on a weekend.");
                System.out.println("Invalid appointment: weekend.");
                return false;
        }else if(startInEST.getHour() >= 22 || startInEST.getHour() <= 8 || endInEST.getHour() >= 22 || startInEST.getHour() <= 8){ //If either time falls between 22:00 and 8:00
            SceneController.loadAlert("Cannot add appointment:\nThis appointment falls outside work hours.");
            System.out.println("Invalid appointment: outside working hours.");
            return false;
        }else if(endInEST.isAfter(startInEST.toLocalDate().atTime(22, 0).atZone(officeZone))){ //If the end time falls after 22:00 the same day as the start time, it's invalid
            SceneController.loadAlert("Cannot add appointment:\nThis appointment falls outside work hours.");
            System.out.println("End time goes into next day.");
            return false;
        }
        System.out.println("Validated: within office hours.");
        return true;
    }
    
    /**
     * Validates whether the input times collide with another appointment
     * already in the database.
     * 
     * @param start     The String of the start time to validate.
     * @param end       The String of the end time to validate.
     * @return          The boolean result of the appt colliding with another.
     * @throws DateTimeParseException   DateTimeParseExceptions to be handled.
     * @throws NullPointerException     NullPointerExceptions to be handled.
     */
    public boolean validateCollisionTimes(String start, String end) throws DateTimeParseException, NullPointerException{
        
        //Get the set of all appointments involving this customer and add them to a set
        HashSet<Appointment> appts = new HashSet<>();
        for(var key : this.dao.getAppointmentKeys()){
            Appointment app = this.dao.getAppointment((int) key);
            if(this.custDao.getCustomer(app.getCustomerId()).getName().equals(this.appointmentCustomer.getValue().toString())){
                System.out.println("Appointment matches customer: " + app.getCustomerName());
                appts.add(app);
            }else{
                System.out.println("Appointment doesn't match customer: " + app.getCustomerName());
            }
        }
        
        //Iterate over each other appointment to find potential collisions
        for(Appointment appointment1 : appts){
            LocalDateTime appointment1Start = appointment1.getStartTime();
            LocalDateTime appointment1End = appointment1.getEndTime();
            
            //If this is the same appointment as itself, skip it
            if(appointment1.getAppointmentId() == Integer.parseInt(this.appointmentID.getText().toString())){
                System.out.println("Same appointment: app1 = " + appointment1.getAppointmentId() + " == appointmentID.getText() = " + this.appointmentID.getText().toString());
                continue;
            }
            
            //Format the incoming datetime strings
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime appointmentStart = LocalDateTime.parse(start, format);
            LocalDateTime appointmentEnd = LocalDateTime.parse(end, format);
            
            //For debugging - prints the datetimes that are compared in the next conditional
            System.out.println("Appt1Start: " + appointment1Start.toString() + "\nAppt1End: " + appointment1End.toString() + 
                    "\nApptComparisonStart: " + appointmentStart.toString() + "\nApptComparisonEnd: " + appointmentEnd.toString());
            
            //Compares datetimes
            if(((appointment1Start.isBefore(appointmentStart) && appointment1End.isBefore(appointmentStart)) || //Either both of appointment 1's times are before both of appointment 2's times...
                            (appointment1Start.isAfter(appointmentEnd) && appointment1End.isAfter(appointmentEnd)))){ //...or both are after appointment 2's times...else is invalid.
                continue;
            }else{ //if a collision is found, return false
                System.out.println("Invalid time entry: collides with another appointment.");
                SceneController.loadAlert("Invalid time entry: collides\nwith another appointment.");
                return false;
            }
        }
        //If no collisions, return true
        System.out.println("Valid time entry: does not collide.");
        return true;
    }
    
    
    /**
     * Initializes the AppointmentEditController.
     * 
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        
        this.userToEnterLabel.setText("Entering This Appt. as User: " + this.userDao.getUser(this.userDao.getCurrentUserId()).getUserName());
        
        //Initialize Contact List Values to display on the contact dropdown
        ObservableList<String> contList = FXCollections.observableArrayList();
        for(var key : this.contDao.getContactMapKeys()){
            contList.add(this.contDao.getContact((int) key).getContName());
        }
        this.appointmentContact.setItems(contList);
        
        //Initialize Customer List values to display on the customer dropdown
        ObservableList<String> custList = FXCollections.observableArrayList();
        for(var key : this.custDao.getCustomerKeys()){
            custList.add(this.custDao.getCustomer((int) key).getName());
        }
        this.appointmentCustomer.setItems(custList);
        
        //Initialize User List values to display on the user dropdown
        ObservableList<String> userList = FXCollections.observableArrayList();
        for(var key: this.userDao.getUserKeys()){
            userList.add(this.userDao.getUser((int) key).getUserName());
        }
        this.userChoice.setItems(userList);
        
        //if an edit, not an add:
        if (this.dao.getAppointmentToEdit() != 0){//This will equal zero if there is no appointment to edit; ie, this is an add appointment.
            //Gets the entered values
            this.apptToEdit = this.dao.getAppointment(this.dao.getAppointmentToEdit());
            this.appointmentID.setText(String.valueOf(this.apptToEdit.getAppointmentId()));
            this.appointmentTitle.setText(this.apptToEdit.getTitle());
            this.appointmentDescription.setText(this.apptToEdit.getDescription());
            this.appointmentLocation.setText(this.apptToEdit.getLocation());
            this.appointmentType.setText(this.apptToEdit.getType());
            this.appointmentStartDate.setValue(this.apptToEdit.getStartDateForPicker());
            this.appointmentStartTime.setText(this.apptToEdit.getStartTimeString());
            this.appointmentEndDate.setValue(this.apptToEdit.getEndDateForPicker());
            this.appointmentEndTime.setText(this.apptToEdit.getEndTimeString());
            this.appointmentContact.setValue(this.apptToEdit.getContactName());
            this.appointmentCustomer.setValue(this.apptToEdit.getCustomerName());
            this.userChoice.setValue(this.userDao.getUser(this.apptToEdit.getUserId()).getUserName());
        }else{
            try{
                //Gets the next appointment_ID
                DBStatement.executeQuery("SELECT MAX(Appointment_ID) FROM appointments;");
            }catch(SQLException e){
                System.out.println(e);
                System.out.println("Exception getting MAX(Appointment_ID) in AppointmentEditController.initialize().");
            }
            //Get the next appointment ID number
            try{
                ResultSet rs = DBStatement.getResultSet();
                if(rs.next()){
                    this.appointmentID.setText(String.valueOf(rs.getInt(1) + 1));
                }
            }catch(SQLException e){
                System.out.println(e);
                System.out.println("Exception in getting result set in AppointmentEditController.initialize().");
            }
        }
    }    
    
    
    /**
     * Sets the appointment that this controller will edit, if any.
     * 
     * @param appt  The Appointment to be edited.
     */
    public void setIncomingAppointment(Appointment appt){
        this.apptToEdit = appt;
        System.out.println("Successful set incoming appointment.");
    }

}
