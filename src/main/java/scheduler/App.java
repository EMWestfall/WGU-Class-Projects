package scheduler;

import data.DBConnection;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

import scenecontroller.SceneController;

/**
 * The class that starts the application.
 * 
 * @author bdudu
 * @version 1.0, 01/22/21
 */
public class App extends Application {
    
    /**
     * The method to start the application. Sets the initial scene (login).
     * 
     * @param primaryStage  The Stage to set the initial scene.
     * @throws Exception    Required by JavaFX.
     */
    @Override public void start(Stage primaryStage) throws Exception {  
        SceneController.setInitialScene(primaryStage);
    }
    
    /**
     * The main Java method. Starts and closes the DBConnection.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {        
        System.out.println("Application start.");
        DBConnection.makeConnection();
        launch();
        DBConnection.closeConnection();
        System.out.println("Application close.");
    }
}