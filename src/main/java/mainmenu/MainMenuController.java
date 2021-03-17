package mainmenu;


import javafx.fxml.FXML;
import scenecontroller.SceneController;

/**
 * This is the controller for the MainMenuView.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class MainMenuController {
 
    /**
     * This method loads the Customer scene when the user clicks the customers
     * button.
     */
    @FXML private void handleCustomersButton() {
        try{
            SceneController.loadScene("Customer");
            System.out.println("Successful handle customers main menu button.");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * This method loads Appointments scene when My Appointments is clicked.
     */
    @FXML private void handleAppointmentsButton(){
        try{
            SceneController.loadScene("Appointment");
            System.out.println("Successful handle appointments main menu button.");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * This method loads Reports scene when Reports is clicked.
     */
    @FXML private void handleReportsButton(){
        try{
            SceneController.loadScene("Reports");
            System.out.println("Successful handle reports main menu button.");
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
