/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Eric
 */
public class InventoryAssessment extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System");
        MainScene mainScene = new MainScene(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}