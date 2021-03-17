package tester;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import scenecontroller.SceneController;

/**
 * This is a class that was used for testing.
 * 
 * @author Eric Westfall
 * @deprecated Was only used for testing purposes during development.
 */
public class TesterController implements Initializable {

   @FXML
    private void loadAlertScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadAlert("Testing the alert popup.");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful alert load.");
    }

    @FXML
    private void loadAppointmentScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("Appointment");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful appointment load.");
    }
    
    @FXML
    private void loadAppointmentEditScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("AppointmentEdit");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful appointment edit load.");
    }
    
    @FXML
    private void loadCustomerScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("Customer");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful customer load.");
    }
    
    @FXML
    private void loadCustomerEditScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("CustomerEdit");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful customer edit load.");
    }
    
    @FXML
    private void loadLoginScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("Login");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful login load.");
    }
    
    @FXML
    private void loadMainScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("MainMenu");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful main menu load.");
    }
    
    @FXML
    private void loadUserEditScene(ActionEvent event) throws Exception{
        try{
            SceneController.loadScene("UserEdit");
        }catch(Exception e){
        System.out.println(e);
        }
        System.out.println("Successful user edit load.");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
