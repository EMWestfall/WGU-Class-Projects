package appointment;

import java.time.LocalDateTime;


/**
 * This interface defines the DAO part of the appointments data.
 * @author bdudu
 * @version 1.0, 01/22/21
 */
public interface AppointmentDAO { 
    
    /**
     * This method adds an appointment to the appointment HashMap.
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
    public void addAppointmentToMap(int appId, String title, String description, String location,
                String type, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createDate, String createdByUser,
                LocalDateTime lastUpdate, String lastUpdatedUser, int custId, int userId, int contactId);
    
    /**
     * This method does an INSERT INTO to add the appointment to the database.
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
    public void addAppointmentToDatabase(String title, String description, String location,
                String type, String startTime, String endTime, int custId, int userId, int contactId);
    
    /**
     * This method does an UPDATE query for the appointment with ID appId.
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
    public void updateAppointment(int appId, String title, String description, String location,
                String type, String startTime, String endTime, int custId, int userId, int contactId);
    
    /**
     * This method does a DELETE query for the appointment with ID appointmentID
     * @param appointmentId The appointment's id number.
     */
    public void deleteAppointment(int appointmentId);
}
