/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Eric
 */
public class AddPartScene {
    private final GridPane root;
    private Text addPartText;
    //radios
    private ToggleGroup radioGroup;
    private RadioButton inHouse;
    private RadioButton outSourced;
    //text fields + labels
    private Text idText;
    private Text idField;
    private Text nameText;
    private TextField nameField;
    private Text invText;
    private TextField invField;
    private Text costText;
    private TextField costField;
    private Text maxText;
    private TextField maxField;
    private Text minText;
    private TextField minField;
    private Text companyText;
    private TextField companyField;
    private Text machineText;
    private TextField machineField;
    //buttons
    private Button saveButton;
    private Button cancelButton;
    //scene and stage
    private final Scene scene;
    private final Stage stage;
    
    //constructor
    public AddPartScene(Stage stage){
        this.root = new GridPane();
        this.root.setId("root");
        this.createAddPartText();
        this.createRadioButtons();
        this.createIdRow();
        this.createNameRow();
        this.createInvRow();
        this.createCostRow();
        this.createMinMaxRow();
        this.defineToggleRow();
        this.setToggleRow();
        this.createSaveButton();
        this.createCancelButton();
        
        //scene and stage
        this.scene = new Scene(root, Settings.getWindowWidth(), Settings.getWindowHeight());
        this.scene.getStylesheets().add(getClass().getResource(Settings.getStylesheet()).toString());
        this.stage = stage;
        this.stage.setScene(this.scene);
        this.stage.show();
    }
    
    //methods here used to organize constructor
    private void createAddPartText(){
        this.addPartText = new Text("Add Part");
        this.addPartText.setId("title1");
        this.root.add(this.addPartText, 0, 0);
    }
    private void createRadioButtons(){
        this.radioGroup = new ToggleGroup();
        this.inHouse = new RadioButton("In-House");
        this.outSourced = new RadioButton("Outsourced");
        this.inHouse.setToggleGroup(radioGroup);
        this.inHouse.setSelected(true);
        this.outSourced.setToggleGroup(radioGroup);
        this.inHouse.setOnAction((event)->{
            this.inHouse.setSelected(true);
            setToggleRow();
        });
        this.outSourced.setOnAction((event)->{
            this.outSourced.setSelected(true);
            setToggleRow();
        });
        
        this.root.add(this.inHouse, 0, 1);
        this.root.add(this.outSourced, 2, 1);
    }
    private void createIdRow(){
        this.idText = new Text("ID");
        this.idField = new Text("Auto Gen - Disabled");
        this.root.add(this.idText, 0, 2);
        this.root.add(this.idField, 1, 2, 2, 1);
    }
    private void createNameRow(){
        this.nameText = new Text("Name");
        this.nameField = new TextField("Part Name");
        this.root.add(this.nameText, 0, 3);
        this.root.add(this.nameField, 1, 3, 2, 1);
    }
    private void createInvRow(){
        this.invText = new Text("Inv Level");
        this.invField = new TextField("Inv Level");
        this.root.add(this.invText, 0, 4);
        this.root.add(this.invField, 1, 4, 2, 1);
    }
    private void createCostRow(){
        this.costText = new Text("Price/Part");
        this.costField = new TextField("Price/Part");
        this.root.add(this.costText, 0, 5);
        this.root.add(this.costField, 1, 5, 2, 1);
    }
    private void createMinMaxRow(){
        this.maxText = new Text("Max");
        this.minText = new Text("Min");
        this.maxField = new TextField("Max");
        this.minField = new TextField("Min");
        this.root.add(this.maxText, 0, 6);
        this.root.add(this.maxField, 1, 6);
        this.root.add(this.minText, 2, 6);
        this.root.add(this.minField, 3, 6);
    }
    private void createSaveButton(){
        Text spacer = new Text();
        this.root.add(spacer, 0, 8);
        this.saveButton = new Button("Save");
        this.saveButton.setOnAction((event)->{
            try{
                String name = this.nameField.getText().trim();
                int inv = Integer.parseInt(this.invField.getText().trim());
                double cost = Double.parseDouble(this.costField.getText().trim());
                int max = Integer.parseInt(this.maxField.getText().trim());
                int min = Integer.parseInt(this.minField.getText().trim());
                //check min/max/inv exception
                if(min > max || inv > max || inv < min || min < 0 || max < 1){
                    throw new IllegalArgumentException();
                }
                //check empty field exception - other fields handled automatically
                if(name.equals("")) throw new NullPointerException();
                //check In House/Outsourced and add part accordingly
                if(this.inHouse.isSelected()){
                    int machineId = Integer.parseInt(this.machineField.getText().trim());
                    InHouse newPart = new InHouse(name, cost, inv, min, max, machineId);
                    Inventory.addPart(newPart);
                }else if (this.outSourced.isSelected()){
                    String company = this.companyField.getText().trim();
                    Outsourced newPart = new Outsourced(name, cost, inv, min, max, company);
                    Inventory.addPart(newPart);
                }
            }catch(NullPointerException | NumberFormatException e){
                ErrorBox error = new ErrorBox("You must fill out\nall fields.");
                return;
            }catch (IllegalArgumentException e){
                ErrorBox error = new ErrorBox("You entered improper\nmin, max, or inv\nvalues.");
                return;
            }
            MainScene mainScene = new MainScene(this.stage);
        });
        this.root.add(this.saveButton, 4, 9);
    }
    private void createCancelButton(){
        this.cancelButton = new Button("Cancel");
        this.cancelButton.setOnAction((event)->{
           MainScene mainScene = new MainScene(this.stage); 
        });
        this.root.add(this.cancelButton, 5, 9);
    }
    private void defineToggleRow(){
        this.companyText = new Text("Company Name");
        this.machineText = new Text("Machine ID");
        this.companyField = new TextField("Company Name");
        this.machineField = new TextField("Machine ID");
    }    
    private void setToggleRow(){
        if (this.inHouse.isSelected()){
            try{
                root.getChildren().remove(this.companyText);
                root.getChildren().remove(this.companyField);
            }catch (NullPointerException e){}
            root.add(this.machineText, 0, 7);
            root.add(this.machineField, 1, 7, 2, 1);
        }else if(this.outSourced.isSelected()){
            try{
                root.getChildren().remove(this.machineText);
                root.getChildren().remove(this.machineField);
            }catch (NullPointerException e){}
            root.add(this.companyText, 0, 7);
            root.add(this.companyField, 1, 7, 2, 1);
        }
    }
}
