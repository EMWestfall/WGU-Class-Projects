package login;


import appointment.Appointment;
import appointment.AppointmentDAOImp;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import scenecontroller.SceneController;

/**
 * This is the controller for LoginView.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class LoginController implements Initializable{
    
    private final user.UserDAOImp dao = data.DAOCollection.getUserDao();
    ResourceBundle resource;
    
    @FXML private Text loginMessageBox;
    @FXML private TextField userText;
    @FXML private TextField passwordText;
    @FXML private Label localeInformation;
    @FXML private Label userLabel;
    @FXML private Label passwordLabel;
    @FXML private Text welcomeLabel;
    @FXML private Button submitButton;
    
    /**
     * This button handles what happens when the user hits the submit button.
     * It checks the user's name/password combination and also throws the
     * popup box that reports whether the user has an appointment in the next
     * 15 minutes.
     */
    @FXML private void handleSubmitButtonAction(){
        int currentUserId = 1;
        int minutesAheadToCheckAppts = 15;
        //Check if user/pw combo is in the user set
        int userNum = checkUserPassword(); //checks the user/password combo and sets the user number if successful
        if(userNum != -1){ //If the user/password combo is found
            this.dao.setCurrentUserId(userNum);
            System.out.println("Successful login, setting active user = " + this.dao.getCurrentUserId());

            //Get Appointments to check if there's one for this user within half an hour
            data.DAOCollection.getAppointmentDao().getUserAppointments("week");
            //Check half hour login
            this.checkAppointments(minutesAheadToCheckAppts);
            this.recordLoginActivity(this.userText.getText(), true);

            SceneController.loadScene("MainMenu");
        }else{
            this.recordLoginActivity(this.userText.getText(), false);
            this.loginMessageBox.setText(this.resource.getString("WrongLoginInput"));
        }
    }
    
    /**
     * This method checks whether the user/password combination exists and
     * returns the user's id number if it does. Otherwise, it returns -1
     * if no such user/password combination is found.
     * 
     * @return The user's id or -1 if the user/password is not found.
     */
    private int checkUserPassword(){
        for (var key : this.dao.getUserKeys()){
            String userName = dao.getUser((int) key).getUserName();
            String userPassword = dao.getUser((int) key).getUserPassword();
            if(this.userText.getText().equals(userName) && this.passwordText.getText().equals(userPassword)){
                return (int) key;
            }
        }
        return -1;
    }
    
    /**
     * This method records login activity to the login_activity.txt file.
     * 
     * @param user          The String of the user name attempting login.
     * @param isSuccessful  The boolean of the success of the login.
     */
    private void recordLoginActivity(String user, boolean isSuccessful){
        try{
            String filePath = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
            if(!Files.exists(Paths.get(filePath + "/login_activity.txt"))){
                Files.createFile(Paths.get(filePath + "/login_activity.txt"));
            }
            FileWriter fileWriter = new FileWriter(filePath + "/login_activity.txt", true);
            fileWriter.write("User: " + user + ", Time: " + Instant.now().toString() + ", Successful: " + isSuccessful + "\n");
            fileWriter.close();
            System.out.println("Login activity successfully recorded.");
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    /**
     * This method checks the user's appointments to see if there is on in the
     * next given minutes (15 per assignment requirements).
     * 
     * @param minutes The minutes ahead to check.
     */
    private void checkAppointments(int minutes){ //check this many minutes ahead to see if the user has an appointment and then give them a popup if they do
        AppointmentDAOImp apptDao = data.DAOCollection.getAppointmentDao();
        for(var key : apptDao.getAppointmentKeys()){
            Appointment appt = apptDao.getAppointment((int) key);
            LocalDateTime startTime = appt.getStartTime();
            if(startTime.isBefore(LocalDateTime.now().plusMinutes(minutes))
                    && startTime.isAfter(LocalDateTime.now().minusMinutes(minutes))){
                SceneController.loadAlert("You have an upcoming appointment\nwithin the next 15 minutes!\nId: " + appt.getAppointmentId()  + "\nTitle: " + appt.getTitle()
                        + "\nDate: " + appt.getStartTime().toLocalDate().toString() + "\nTime: " + appt.getStartTime().toLocalTime().toString());
                return;
            }
        }
        SceneController.loadAlert("You have no appointments in\nthe next 15 minutes.");
    }
    
    /**
     * Sets the initial conditions of the login screen, including language
     * translation in the case French is used.
     * 
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        ZoneId thisZone = ZoneId.systemDefault();
        Locale thisLocale = new Locale(Locale.getDefault().toLanguageTag(), Locale.getDefault().getCountry());
        System.out.println(Locale.getDefault().toLanguageTag() + " " + Locale.getDefault().getCountry());
        System.out.println(thisLocale.getLanguage() + " " + thisLocale.getCountry());
        
        this.resource = ResourceBundle.getBundle("locale.MessagesBundle", thisLocale);
        this.localeInformation.setText(this.resource.getString("Location") + ": " + thisZone.toString());
        this.userLabel.setText(this.resource.getString("User") + ": ");
        this.passwordLabel.setText(this.resource.getString("Password") + ": ");
        this.welcomeLabel.setText(this.resource.getString("Greetings"));
        this.submitButton.setText(this.resource.getString("SubmitButton"));
    }
}
