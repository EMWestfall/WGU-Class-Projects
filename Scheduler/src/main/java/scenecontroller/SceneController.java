package scenecontroller;


import alerts.AlertsController;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Vector;
import javafx.fxml.LoadException;
import javafx.scene.layout.Pane;

/**
 * This class is the controller that loads and sets the scenes throughout the
 * application.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class SceneController {
    private static HashMap<String, String> screenMap;
    private static Stage stage;
    private static Scene scene;
    private static FXMLLoader loader;
    private static Stage alertStage;
    private static int currentSceneNumber;
    private static String currentSceneName;
    private static Vector<String> sceneHistory;
    
    /**
     * This method is called by the start method in App.java and it sets
     * the initial conditions as well as the HashMap holding name to location
     * pair of the various fxml view files to load.
     * 
     * @param primaryStage The Stage that serves as the primary stage.
     */
    public static void setInitialScene(Stage primaryStage){
        try{
            SceneController.stage = primaryStage;
            
            //Initialize Loader and Screen HashMap
            SceneController.screenMap = new HashMap<>();
            SceneController.sceneHistory = new Vector<>();
            SceneController.loader = new FXMLLoader();
        
            //Set initial stage attributes
            SceneController.stage.getIcons().add(new Image("/images/icon.jpg"));
            SceneController.stage.setTitle("Scheduler");
            SceneController.stage.setMaximized(true);
            
            //Initialize Screens
            SceneController.addScene("/appointment/AppointmentView.fxml", "Appointment");
            SceneController.addScene("/appointment/AppointmentEditView.fxml", "AppointmentEdit");
            SceneController.addScene("/customer/CustomerView.fxml", "Customer");
            SceneController.addScene("/customer/CustomerEditView.fxml", "CustomerEdit");
            SceneController.addScene("/login/LoginView.fxml", "Login");
            SceneController.addScene("/mainmenu/MainMenuView.fxml", "MainMenu");
            SceneController.addScene("/tester/TesterView.fxml", "Test");
            SceneController.addScene("/reports/ReportsView.fxml", "Reports");
            SceneController.addScene("/reports/ThirdReportView.fxml", "ThirdReport");
            //SceneController.addScene("/useredit/UserEditView.fxml", "UserEdit");
            
            //Set initial scene
            SceneController.scene = new Scene(SceneController.loader.load(SceneController.class.getResource(SceneController.screenMap.get("Login"))));
            SceneController.currentSceneNumber = 0;
            SceneController.currentSceneName = "Login";
            SceneController.sceneHistory.add("Login");
            SceneController.stage.setScene(SceneController.scene);
            SceneController.stage.show();
            
            System.out.println("Successful initial scene (login) load.");
        }
        catch(Exception exception){
            System.out.println(exception);
            System.out.println("Failed to initialize first scene, see stack trace above.");
        }
    }

    /**
     * This method adds a name to location fxml view pair to the HashMap.
     * 
     * @param fxmlTarget    The String of the relative location of the fxml view
     * @param name          The String of the fxml name.
     */
    protected static void addScene(String fxmlTarget, String name) {
        try{
            SceneController.screenMap.put(name, fxmlTarget);
            System.out.println("Successful adding scene " + name + " to scene HashMap.");
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Exception in addScene() for " + name + ".");
        }
    }
    
    /**
     * Removes a scene from the HashMap.
     * 
     * @param name  The String of the scene name to be removed.
     */
    protected static void removeScene(String name){
        try{
            SceneController.screenMap.remove(name);
            System.out.println("Successful removing scene from scene HashMap.");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Overloaded method to pass a default true value to loadScene(String, bool)
     * 
     * @param name The String of the scene name to load.
     */
    public static void loadScene(String name){
        SceneController.loadScene(name, true);
    }
    
    /**
     * Loads an fxml scene by its name.
     * 
     * @param name          The String of the name of the scene to load.
     * @param furthestScene The boolean of whether this is the furthest scene.
     */
    public static void loadScene(String name, boolean furthestScene){
        try{
            //currentSceneName is what is used as the key in screenMap to load a scene
            SceneController.currentSceneName = name;
            //if the user has chosen to go back one or more scenes, this loop will erase all the forward scenes and the new scene requested will be the furthest progress in scene history
            if (furthestScene == true){
                System.out.println("Furthest scene is true.");
                for (int index = 0; index < SceneController.sceneHistory.size(); index++){
                    if (index > SceneController.currentSceneNumber) SceneController.sceneHistory.remove(index);
                }
                //add the next scene to the history
                SceneController.sceneHistory.add(SceneController.currentSceneName);
                //I learned in Computer Architecture that this will probably be compiled as a pointer to the vector's address + offset
                SceneController.currentSceneNumber++;
            }
            //load the scene using the name of the scene at sceneHistory[currentSceneNumber]
            Pane root = null;
            try{
                root = (Pane) SceneController.loader.load(SceneController.class.getResource(SceneController.screenMap.get(SceneController.sceneHistory.get(SceneController.currentSceneNumber))));
                System.out.println("Successfully used FXMLLoader in SceneController.loadScene() on " + name);
            }catch(LoadException le){
                System.out.println(le);
                System.out.println("LoadException in SceneController.loadScene() while trying to load " + name);
            }
            SceneController.scene.setRoot(root);
            System.out.println("Successful scene root set - scene fully loaded.");
//debugging for the vector being out of bounds
        }catch(ArrayIndexOutOfBoundsException aiob){
            System.out.println("Array index out of bounds in SceneController.loadScene(String, bool) dealing with sceneHistory vector.");
            System.out.println(aiob);
        //all other exceptions
        }catch(Exception e){
            System.out.println("Exception in SceneController.loadScene() while loading " + name + ".");
            System.out.println(e);
        }
    }
    
    /**
     * Goes back one scene.
     * 
     * @deprecated Buggy code that was not used in version 1.0.
     */
    public static void goBackOneScene() {
        try{
            //If the current scene is the first in the history, the user cannot go back further, so we throw this exception
            if(SceneController.currentSceneNumber == 0) throw new ArrayIndexOutOfBoundsException();
            //Go back one scene and load it
            SceneController.currentSceneNumber--;
            SceneController.loadScene(SceneController.sceneHistory.get(SceneController.currentSceneNumber), false);
        }catch (ArrayIndexOutOfBoundsException aiob){
            //print for debug, load the alert box to warn the user that this is the first item in history
            System.out.println(aiob);
            SceneController.loadAlert("This is the first scene in the scene history. You cannot go back further.");
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Successful go back one scene method.");
    }
    
    /**
     * Goes forward one scene.
     * 
     * @deprecated Buggy code that was not used in version 1.0.
     */
    public static void goForwardOneScene(){
        try{
            //If the current scene is the last in the history, the user cannot go forward further, so we throw this exception
            if (SceneController.currentSceneNumber >= SceneController.sceneHistory.size()) throw new ArrayIndexOutOfBoundsException();
            //Go forward one scene and load it
            SceneController.currentSceneNumber++;
            SceneController.loadScene(SceneController.sceneHistory.get(SceneController.currentSceneNumber), false);
        }catch (ArrayIndexOutOfBoundsException aiob){
            //print for debug, load the alert box to warn the user that this is the last item in history
            System.out.println(aiob);
            SceneController.loadAlert("This is the last scene in the scene history. You cannot go forward any further.");
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Successful go forward one scene method.");
    }
    
    /**
     * Loads an alert popup with the given alert message.
     * 
     * @param alertMessage The String message to display in the alert.
     */
    public static void loadAlert(String alertMessage){
        try{
            AlertsController.setAlertText(alertMessage);
            SceneController.alertStage = new Stage();
            Pane alertRoot = SceneController.loader.load(SceneController.class.getResource("/alerts/AlertsView.fxml"));
            Scene alertScene = new Scene(alertRoot, 240, 160);
            alertStage.setScene(alertScene);
            alertStage.setAlwaysOnTop(true);
            alertStage.getIcons().add(new Image("/images/icon.jpg"));
            alertStage.setTitle("Alert");
            alertStage.setMaximized(false);
            alertStage.show();
            System.out.println("Successful load alert method.");
        }catch(Exception e){
            System.out.println("loadAlert");
            System.out.println(e);
        }
    }
    
    /**
     * Closes the alert stage.
     */
    public static void closeAlert(){
        SceneController.alertStage.close();
        System.out.println("Successful close alert method.");
    }
    
    /**
     * Closes the application's main stage.
     */
    public static void closeApp(){
        SceneController.stage.close();
        System.out.println("Successful app close method.");
    }
    
    
    /**
     * Returns the FXMLLoader used to load scenes.
     * 
     * @return The FXMLLoader used to load scenes.
     */
    public static FXMLLoader getLoader(){
        return SceneController.loader;
    }
}
