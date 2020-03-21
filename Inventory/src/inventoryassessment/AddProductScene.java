/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Eric
 */
public class AddProductScene {
    private final HBox root;
    private final VBox leftPanel;
    private Text addProductText;
    //left product box
    private final GridPane productBox;
    private Text idText;
    private Text idField;
    private Text nameText;
    private TextField nameField;
    private Text invText;
    private TextField invField;
    private Text priceText;
    private TextField priceField;
    private Text maxText;
    private TextField maxField;
    private Text minText;
    private TextField minField;
    //right parts box
    private final GridPane partsPane;
    private Button searchButton;
    private TextField searchField;
    private TableView<Part> topTable;
    private Button addButton;
    private TableView<Part> bottomTable;
    private Button deleteButton;
    private Button saveButton;
    private Button cancelButton;
    //scene and stage
    private final Scene scene;
    private final Stage stage;
    
    public AddProductScene(Stage stage){
        this.root = new HBox();
        this.root.setId("root");
        this.leftPanel = new VBox();
        this.root.getChildren().add(this.leftPanel);
        this.createAddProductText();
        Text spacer = new Text();
        this.leftPanel.getChildren().add(spacer);
        //left products box
        this.productBox = new GridPane();
        this.leftPanel.getChildren().add(productBox);
        this.createIdRow();
        this.createNameRow();
        this.createInvRow();
        this.createCostRow();
        this.createMinMaxRow();
        //right parts box
        this.partsPane = new GridPane();
        this.root.getChildren().add(partsPane);
        this.createSearchRow();
        this.createTopTable();
        this.createAddButton();
        this.createBottomTable();
        this.createDeleteButton();
        this.createSaveButton();
        this.createCancelButton();        
        //scene and stage
        this.scene = new Scene(root, Settings.getWindowWidth(), Settings.getWindowHeight());
        this.scene.getStylesheets().add(getClass().getResource(Settings.getStylesheet()).toString());
        this.stage = stage;
        this.stage.setScene(this.scene);
        this.stage.show();
    }
    private void createAddProductText(){
        this.addProductText = new Text("Add Product");
        this.addProductText.setId("title1");
        this.leftPanel.getChildren().add(this.addProductText);
    }
    private void createIdRow(){
        this.idText = new Text("ID");
        this.idField = new Text("Auto Gen - Disabled");
        this.productBox.add(idText, 0, 0);
        this.productBox.add(idField, 1, 0, 2, 1);
    }
    private void createNameRow(){
        this.nameText = new Text("Name");
        this.nameField = new TextField("Product Name");
        this.productBox.add(nameText, 0, 1);
        this.productBox.add(nameField, 1, 1, 2, 1);
    }
    private void createInvRow(){
        this.invText = new Text("Inv Level");
        this.invField = new TextField("Inv Level");
        this.productBox.add(invText, 0, 2);
        this.productBox.add(invField, 1, 2, 2, 1);
    }
    private void createCostRow(){
        this.priceText = new Text("Price/product");
        this.priceField = new TextField("Price");
        this.productBox.add(priceText, 0, 3);
        this.productBox.add(priceField, 1, 3, 2, 1);
    }
    private void createMinMaxRow(){
        this.maxText = new Text("Max");
        this.maxField = new TextField("Max");
        this.minText = new Text("Min");
        this.minField = new TextField("Min");
        this.productBox.add(maxText, 0, 4);
        this.productBox.add(maxField, 1, 4);
        this.productBox.add(minText, 2, 4);
        this.productBox.add(minField, 3, 4);
    }
    private void createSearchRow(){
        this.searchButton = new Button("Search");
        this.searchButton.setOnAction((event)->{
            for(Part part : this.topTable.getItems()){
                if(this.searchField.getText().equals(part.getName())){
                    this.topTable.getSelectionModel().select(part);
                    this.topTable.scrollTo(part);
                    return;
                }
            }
            ErrorBox notFound = new ErrorBox("Item not found");
        });        
    }
    private void createTopTable(){
        this.topTable = new TableView<>();
        
        Inventory.getAllParts().forEach((invPart)->{
           this.topTable.getItems().add(invPart);
        });
        
        TableColumn<Part, Integer> partIDColumn = new TableColumn<>("Part ID");
        partIDColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        TableColumn<Part, String> partNameColumn = new TableColumn<>("Part Name");
        partNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Part, Integer> partInventoryColumn = new TableColumn<>("Inv Level");
        partInventoryColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        TableColumn<Part, Double> partPriceColumn = new TableColumn<>("Price/Part");
        partPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        
        this.topTable.getColumns().add(partIDColumn);
        this.topTable.getColumns().add(partNameColumn);
        this.topTable.getColumns().add(partInventoryColumn);
        this.topTable.getColumns().add(partPriceColumn);
        
        this.partsPane.add(this.topTable, 0, 1, 5, 4);
    }
    private void createAddButton(){
        this.addButton = new Button("Add");
        this.addButton.setOnAction((event)->{
            for(Part part : this.topTable.getSelectionModel().getSelectedItems()){
                this.bottomTable.getItems().add(part);
            }
        });
        this.partsPane.add(this.addButton, 0, 5);
    }
    private void createBottomTable(){
        this.bottomTable = new TableView<>();
        
        TableColumn<Part, Integer> partIDColumn = new TableColumn<>("Part ID");
        partIDColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        TableColumn<Part, String> partNameColumn = new TableColumn<>("Part Name");
        partNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Part, Integer> partInventoryColumn = new TableColumn<>("Inv Level");
        partInventoryColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        TableColumn<Part, Double> partPriceColumn = new TableColumn<>("Price/Part");
        partPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        
        this.bottomTable.getColumns().add(partIDColumn);
        this.bottomTable.getColumns().add(partNameColumn);
        this.bottomTable.getColumns().add(partInventoryColumn);
        this.bottomTable.getColumns().add(partPriceColumn);
        
        this.partsPane.add(this.bottomTable, 0, 6, 5, 4);
    }
    private void createDeleteButton(){
        this.deleteButton = new Button("Delete");
        this.deleteButton.setOnAction((event)->{
            for(Part part: this.bottomTable.getSelectionModel().getSelectedItems()){
                this.bottomTable.getItems().remove(part);
            }
        });
        this.partsPane.add(this.deleteButton, 0, 10);
    }
    private void createSaveButton(){
        this.saveButton = new Button("Save");
        this.saveButton.setOnAction((event)->{  
            try{
                String name = this.nameField.getText();
                Double price = Double.parseDouble(this.priceField.getText());
                int inv = Integer.parseInt(this.invField.getText());
                int min = Integer.parseInt(this.minField.getText());
                int max = Integer.parseInt(this.maxField.getText());
                //check empty field exception - other fields are handled automatically
                if(this.bottomTable.getItems().isEmpty() || name.equals("")) throw new NullPointerException();
                //check min/max/inv exception
                if(min > max || inv > max || inv < min || min < 0 || max < 1){
                    throw new IllegalArgumentException();
                }
                //check price exception
                List<Part> associatedParts = this.bottomTable.getItems();
                double sum = 0.0;
                for(Part part : associatedParts) sum += part.getPrice();
                if(sum > price) throw new RuntimeException();
                //add new product and save
                Product newProduct = new Product(name, price, inv, min, max);
                associatedParts.forEach((part)->newProduct.addAssociatedPart(part));
                Inventory.addProduct(newProduct);
                MainScene mainScene = new MainScene(this.stage);
            }catch(NullPointerException | NumberFormatException e){
                ErrorBox error = new ErrorBox("You must fill out\nall fields, including adding\nat least 1 part.");
                return;
            }catch (IllegalArgumentException e){
                ErrorBox error = new ErrorBox("You entered improper\nmin, max, or inv\nvalues.");
                return;
            }catch (RuntimeException e){
                ErrorBox error = new ErrorBox("The sum of the part prices\nmust be less than the\nproduct price.");
                return;
            }
        });
        this.partsPane.add(this.saveButton, 8, 11);
    }
    private void createCancelButton(){
        this.cancelButton = new Button("Cancel");
        this.cancelButton.setOnAction((event)->{
           MainScene mainScene = new MainScene(this.stage); 
        });
        this.partsPane.add(this.cancelButton, 9, 11);
    } 
}
