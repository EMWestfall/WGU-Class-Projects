package appointment;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;


/**
 * This class implements the AppointmentDAO interface and serves as the DAO
 * class that interacts with the database and translates the results into 
 * Appointment objects.
 * 
 * @author bdudu
 * @version 1.0, 01/22/21
 */
public class AppointmentDAOImp implements AppointmentDAO {

//--------------------Variables----------------------------
    private final HashMap<Integer, Appointment> appointmentMap;
    private int apptToEditId;
    
    
//-------------------Methods--------------------------------

    //-------------Constructor-----------------
    public AppointmentDAOImp(){
        this.appointmentMap = new HashMap<>();
    }
    
    //-------------Interface Methods-----------------
    
    /**
     * This method adds an appointment to the appointment HashMap.
     * 
     * @param appId             The appointment's id number.
     * @param title             The String for the appointment's title.
     * @param description       The String for the appointment's description.
     * @param location          The String for the appointment's location.
     * @param type              The String for the appointment's type.
     * @param startTime         The LocalDateTime for the appointment's start.
     * @param endTime           The LocalDateTime for the appointment's end.
     * @param createDate        The LocalDateTime for the appt's creation.
     * @param createdByUser     The String for the appointment's creation user.
     * @param lastUpdate        The LocalDateTime for the appt's last update.
     * @param lastUpdatedUser   The String for the appt's last update user.
     * @param custId            The appointment's customer's id.
     * @param userId            The appointment's user's id.
     * @param contactId         The appointment's contact's id.
     */
    public void addAppointmentToMap(int appId, String title, String description, String location, //creates appointments and adds them to an appointment HashMap
                String type, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createDate, String createdByUser,
                LocalDateTime lastUpdate, String lastUpdatedUser, int custId, int userId, int contactId){
        this.appointmentMap.put(appId, new Appointment(appId, title, 
            description, location, type, startTime, endTime, createDate,
            createdByUser, lastUpdate, lastUpdatedUser, custId, userId, contactId));
    }
    
    /**
     * This method does an INSERT INTO to add the appointment to the database.
     * 
     * @param title         The String for the appointment's title.
     * @param description   The String for the appointment's description.
     * @param location      The String for the appointment's location.
     * @param type          The String for the appointment's type.
     * @param startTime     The LocalDateTime for the appointment's start.
     * @param endTime       The LocalDateTime for the appointment's end.
     * @param custId        The appointment's customer's id.
     * @param userId        The appointment's user's id.
     * @param contactId     The appointment's contact's id.
     */
    public void addAppointmentToDatabase(String title, String description, String location, //creates appointments and adds them to the database
                String type, String startTime, String endTime, int custId, int userId, int contactId){
        
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Long start = ZonedDateTime.of(LocalDateTime.parse(startTime, format), ZoneId.systemDefault()).toEpochSecond(); //Converts start time to UTC
        Long end = ZonedDateTime.of(LocalDateTime.parse(endTime, format), ZoneId.systemDefault()).toEpochSecond(); //Converts end time to UTC
        
        String userName = data.DAOCollection.getUserDao().getUser(data.DAOCollection.getUserDao().getCurrentUserId()).getUserName();
        
        try{
            String query = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, "
                                        + "User_ID, Contact_ID)\n"
                                        + "VALUES (\"" + title + "\", \"" + description + "\", \"" + location + "\", \"" + type + "\", FROM_UNIXTIME(\"" + start
                                        + "\"), FROM_UNIXTIME(\"" + end + "\"), \"" + userName + "\", \"" + userName + "\", \"" 
                                        + custId + "\", \"" + userId + "\", \"" + contactId
                                        + "\");";
            System.out.println(query);
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("INSERT INTO in AppointmentDAOImp.addAppointmentToDatabase() failed.");
            return;
        }
        System.out.println("INSERT INTO in AppointmentDAOImp.addAppointmentToDatabase() successful.");
    }
    
    /**
     * This method does an UPDATE query for the appointment with ID appId.
     * 
     * @param appId         The appointment's id number.
     * @param title         The String for the appointment's title.
     * @param description   The String for the appointment's description.
     * @param location      The String for the appointment's location.
     * @param type          The String for the appointment's type.
     * @param startTime     The LocalDateTime for the appointment's start.
     * @param endTime       The LocalDateTime for the appointment's end.
     * @param custId        The appointment's customer's id.
     * @param userId        The appointment's user's id.
     * @param contactId     The appointment's contact's id.
     */
    public void updateAppointment(int appId, String title, String description, String location, //edits appointments
                String type, String startTime, String endTime, int custId, int userId, int contactId){
        
        
        //Retrieve the appointment in its current, unupdated state to compare to the suggested update
        Appointment appt = this.appointmentMap.get(appId);
        HashMap<String, String> variablesToChange = new HashMap<>();
        
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = String.valueOf(ZonedDateTime.of(LocalDateTime.parse(startTime, format), ZoneId.systemDefault()).toEpochSecond()); //Converts start time to UTC
        String end = String.valueOf(ZonedDateTime.of(LocalDateTime.parse(endTime, format), ZoneId.systemDefault()).toEpochSecond()); //Converts end time to UTC
        
        //If there is a change to a field, then it won't equal the current appointment's field. If that's the case, add it to the HashMap
        //and at the end, all changes in the Hashmap will be added to the update query
        if (!title.equals(appt.getTitle())) variablesToChange.put("Title", title);
        if (!description.equals(appt.getDescription())) variablesToChange.put("Description", description);
        if (!location.equals(appt.getLocation())) variablesToChange.put("Location", location);
        if (!type.equals(appt.getType())) variablesToChange.put("Type", type);
        if (!startTime.equals(appt.getStartTime().toString())) variablesToChange.put("Start", "FROM_UNIXTIME(" + start + ")");
        if (!endTime.equals(appt.getEndTime().toString())) variablesToChange.put("End", "FROM_UNIXTIME(" + end + ")");
        if (custId != appt.getCustomerId()) variablesToChange.put("Customer_ID", String.valueOf(custId));
        if (userId != appt.getUserId()) variablesToChange.put("User_ID", String.valueOf(userId));
        if (contactId != appt.getContactId()) variablesToChange.put("Contact_ID", String.valueOf(contactId));
        
        //Construct the update query
        int counter = 0;
        String concatColumnsToUpdate = "";
        for (String key : variablesToChange.keySet()){
            if(counter == 0){
                concatColumnsToUpdate += key + " = " + (variablesToChange.get(key).contains("UNIX")? variablesToChange.get(key) : "\"" + variablesToChange.get(key) + "\"");
            }else{
                concatColumnsToUpdate += ", " + key + " = " + (variablesToChange.get(key).contains("UNIX")? variablesToChange.get(key) : "\"" + variablesToChange.get(key) + "\"");
            }
            counter++;
        }
        String query = "UPDATE appointments SET " + concatColumnsToUpdate + ", Last_Updated_By = \"" 
                            + data.DAOCollection.getUserDao().getUser(data.DAOCollection.getUserDao().getCurrentUserId()).getUserName()
                            + "\" WHERE Appointment_ID = " + appId + ";";
        System.out.println("Query is: \n" + query);
        
        //Execute the Query
        try{
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQLException in AppointmentDAOImp.updateAppointment() while executing update query.");
        }
    }
    
    /**
     * This method does a DELETE query for the appointment with ID appointmentID
     * 
     * @param appointmentId The appointment's id number.
     */
    public void deleteAppointment(int appointmentId){ //deletes appointments from the appointment list and hashmap
        
        //Deleting is easy and just needs the appointment_id to delete
        try{
            DBStatement.executeQuery("DELETE FROM appointments WHERE Appointment_ID = " + appointmentId + ";");
        }catch(SQLException e){
            System.out.println("Could not find appointment_ID to delete in AppointmentDAOImp.deleteAppointment.");
        }
        this.appointmentMap.remove(appointmentId);
        System.out.println("Successfully deleted appointment, ID number = " + appointmentId + ".");
    }
    
    //---------Other Appointment Methods------------------
    
    /**
     * This method returns the appointment with the given id.
     * 
     * @param apptNumber    The appointment's id number.
     * @return              The Appointment at the given id.
     */
    public Appointment getAppointment(int apptNumber){
        return this.appointmentMap.get(apptNumber);
    }
    
    /**
     * This method returns the Set of all appointment keys in the HashMap.
     * 
     * @return  The Set of appointment keys.
     */
    public Set<Integer> getAppointmentKeys(){
        return this.appointmentMap.keySet();
    }
    
    /**
     * This method is the overloaded function that sets a default value.
     * 
     * @param mode The String representing the time mode for appointments.
     */
    public void getUserAppointments(String mode){
        this.getUserAppointments(mode, data.DAOCollection.getUserDao().getCurrentUserId());
    }
    
    /**
     * This method does a SELECT query on appointments and adds the results 
     * to the appointment map.
     * 
     * @param mode      The String of the time mode {"week", "month", "all"}.
     * @param userId    The user id to filter results in the WHERE clause.
     */
    public void getUserAppointments(String mode, int userId){
        String lastDay = "";
        String firstDay = "";
        if (!this.appointmentMap.isEmpty()){
            this.appointmentMap.clear();
            System.out.println("Successfully cleared appointment map.");
        }else{
            System.out.println("Appointment map already clear.");
        }
        
        //Construct SQL Query based off the time interval mode
        switch(mode){
            case "week":
                lastDay = "AND appointments.Start <= ADDDATE(Now(), 8 - DAYOFWEEK(Now()))\n";
                firstDay = "AND appointments.Start >= ADDDATE(Now(), -(DAYOFWEEK(Now())))\n";
                break;
            case "month":
                lastDay = "AND appointments.Start <= LAST_DAY(Now())\n";
                firstDay = "AND appointments.Start >= ADDDATE(LAST_DAY(ADDDATE(Now(), INTERVAL -1 MONTH)), 1)\n"; //This is the first day of the current month: take the current time,
                break;                                                                                          //subtract a month, take the last day of that month, then add one day
            case "all":
            default:
        }
        String query = "SELECT Appointment_ID, Title, Description, Location, Type, "
                            + "UNIX_TIMESTAMP(Start), UNIX_TIMESTAMP(End), UNIX_TIMESTAMP(Create_Date), "
                            + "Created_By, UNIX_TIMESTAMP(Last_Update), Last_Updated_By, "
                            + "Customer_ID, User_ID, Contact_ID\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = " + userId + "\n" +
                        lastDay + firstDay +
                        ";";
        System.out.println(query);
        
        //Read the dataset and write to the hashmap
        try{
            data.DBStatement.executeQuery(query);
            ResultSet resultSet = data.DBStatement.getResultSet();
            //id -1, title -2, description -3, location -4, type -5, startTime -6, endTime -7, create_date -8,
            //created_by -9, last_update -10, last_update_by -11, cust_id -12, user_id -13, contact_id -14
            resultSet.beforeFirst();
                      
            while(resultSet.next()){//All times are converted from UTC to LocalDateTime on the user's system
                this.addAppointmentToMap(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(6)), ZoneId.systemDefault()),
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(7)), ZoneId.systemDefault()), 
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(8)), ZoneId.systemDefault()), resultSet.getString(9), 
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(10)), ZoneId.systemDefault()), resultSet.getString(11), 
                    resultSet.getInt(12), resultSet.getInt(13), resultSet.getInt(14));
            }
            System.out.println("Successful getUserAppointments.");
        }catch (SQLException e){
            System.out.println(e);
            System.out.println("Failure retrieving all appointments result set.");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    
    //--------------For Reports-----------------------
    
    /**
     * This method does a SELECT query returning appointments by contact and
     * adds them to the appointments HashMap.
     * 
     * @param contactId     The contact's id to filter in the WHERE clause.
     */
    public void getContactAppointments(int contactId){
        int currentUser = data.DAOCollection.getUserDao().getCurrentUserId();
        if (!this.appointmentMap.isEmpty()){
            this.appointmentMap.clear();
            System.out.println("Successfully cleared appointment map.");
        }else{
            System.out.println("Appointment map already clear.");
        }
        
        String query = "SELECT Appointment_ID, Title, Description, Location, Type, "
                            + "UNIX_TIMESTAMP(Start), UNIX_TIMESTAMP(End), UNIX_TIMESTAMP(Create_Date), "
                            + "Created_By, UNIX_TIMESTAMP(Last_Update), Last_Updated_By, "
                            + "Customer_ID, User_ID\n" +
                        "FROM appointments\n" +
                        "WHERE Contact_ID = " + contactId + "\n" +
                        ";";
        System.out.println(query);
        
        //Read the dataset and write to the hashmap
        try{
            data.DBStatement.executeQuery(query);
            ResultSet resultSet = data.DBStatement.getResultSet();
            resultSet.beforeFirst();
                      
            while(resultSet.next()){//All times are converted from UTC to LocalDateTime on the user's system
                this.addAppointmentToMap(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(6)), ZoneId.systemDefault()),
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(7)), ZoneId.systemDefault()), 
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(8)), ZoneId.systemDefault()), resultSet.getString(9), 
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(resultSet.getLong(10)), ZoneId.systemDefault()), resultSet.getString(11), 
                    resultSet.getInt(12), resultSet.getInt(13), contactId);
            }
            System.out.println("Successful getContactAppointments.");
        }catch (SQLException e){
            System.out.println(e);
            System.out.println("Failure retrieving all appointments result set.");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    
    //--------------Appointment to Edit Methods------------------------
    
    /**
     * This method returns the id of the appointment that the 
     * AppointmentEditController will edit.
     * 
     * @return The appointment's id.
     */
    public int getAppointmentToEdit(){
        return this.apptToEditId;
    }
    
    /**
     * This method sets the id of the appointment that the
     * AppointmentEditController will edit.
     * 
     * @param apptToEdit    The id of the appointment to edit.
     */
    public void setAppointmentToEdit(int apptToEdit){
        this.apptToEditId = apptToEdit;
    }
    
}
