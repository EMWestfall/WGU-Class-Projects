/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Eric
 */
public class ErrorBox {
    private final VBox root;
    private final Text errorText;
    private final Button errorButton;
    private final Scene errorScene;
    private Stage errorStage;
    public ErrorBox(String text){
        this.root = new VBox();
        this.root.setId("errorRoot");
        this.errorText = new Text(text);
        this.errorText.setId("errorText");
        this.root.getChildren().add(errorText);
        this.errorButton = new Button("Close");
        this.errorButton.setOnAction((event)->{
            errorStage.close();
        });
        this.root.getChildren().add(errorButton);
        this.errorScene = new Scene(root, 200, 100);
        this.errorScene.getStylesheets().add(getClass().getResource(Settings.getStylesheet()).toString());
        this.errorStage = new Stage();
        this.errorStage.setTitle("Error");
        this.errorStage.setScene(this.errorScene);
        this.errorStage.show();
    }
}
