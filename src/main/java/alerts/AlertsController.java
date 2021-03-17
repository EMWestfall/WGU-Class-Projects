package alerts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import scenecontroller.SceneController;

/**
 * This is the controller for AlertsView.fxml.
 *
 * @author bdudu
 * @version 1.0, 01/22/21
 */
public class AlertsController implements Initializable {
    
    private static String alertMessage;
    @FXML private Label alertText;
    
    /**
     * This method handles the action when the user hit's the Ok button.
     */
    @FXML private void handleOkButton(){
        SceneController.closeAlert();
        System.out.println("Alert closed.");
    }
    
    /**
     * This method sets the alert message when the alert view is loaded.
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        this.alertText.setText(AlertsController.alertMessage);
        System.out.println("Alert load successful, message: \n" + AlertsController.alertMessage);
    }

    /**
     * This method changes the alert's message text.
     * @param alertMessage  The String to set the text to.
     */
    public static void setAlertText(String alertMessage){
        AlertsController.alertMessage = alertMessage;
        System.out.println("Alert text set.");
    }

}
