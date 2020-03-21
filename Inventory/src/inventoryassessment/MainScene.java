/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class MainScene {
    
//objects in the main scene
    private VBox root;
    private Text mainTitle;
    private HBox mainContentBox;
    private HBox spacer;
    //part pane
    private GridPane partsPane;
    private Text partsText;
    private Button partsSearchButton;
    private TextField partsSearchField;
    private TableView<Part> parts;
    private Button addPart;
    private Button modifyPart;
    private Button deletePart;
    //product pane
    private GridPane productsPane;
    private Text productsText;
    private Button productsSearchButton;
    private TextField productsSearchField;
    private TableView<Product> products;
    private Button addProduct;
    private Button modifyProduct;
    private Button deleteProduct;
    //exit button
    private Button exitButton;
    //stage and scene
    private final Scene mainScene;
    private final Stage mainStage;

//constructor    
    public MainScene(Stage stage){
        //root, title, mainContent
        this.createRoot();
        this.createMainTitleText();
        this.createSpacer();
        this.createMainContentBox();
        //parts pane
        this.createPartsPane();
        this.createPartsText();
        this.createPartsSearchButton();
        this.createPartsSearchField();
        this.createPartsTable();
        this.createAddPartButton();
        this.createModifyPartButton();
        this.createDeletePartButton();
        //product pane
        this.createProductsPane();
        this.createProductsText();
        this.createProductsSearchButton();
        this.createProductsSearchField();
        this.createProductsTable();
        this.createAddProductButton();
        this.createModifyProductButton();
        this.createDeleteProductButton();
        //exit button
        this.createExitButton();       
        //scene and stage
        this.mainScene = new Scene(root, Settings.getWindowWidth(), Settings.getWindowHeight());
        this.mainScene.getStylesheets().add(getClass().getResource(Settings.getStylesheet()).toString());
        this.mainStage = stage;
        this.mainStage.setScene(this.mainScene);
        this.mainStage.show();
    }

//The methods in this section are intended to organize the constructor's code.
    private void createRoot(){
        this.root = new VBox();
        this.root.setId("root");
    }
    private void createMainTitleText(){
        this.mainTitle = new Text("Inventory Management System");
        this.mainTitle.setId("title1");
        this.root.getChildren().add(this.mainTitle);
    }
    private void createSpacer(){
        this.spacer = new HBox();
        this.spacer.prefWidthProperty().bind(this.root.widthProperty());
        this.spacer.prefHeight(30);
    }
    private void createMainContentBox(){
        this.mainContentBox = new HBox();
        this.mainContentBox.setId("box");
        this.root.getChildren().add(this.mainContentBox);
    }
    //parts pane
    private void createPartsPane(){
        this.partsPane = new GridPane();
        this.partsPane.setId("box");
        this.mainContentBox.getChildren().add(this.partsPane);
    }
    private void createPartsText(){
        this.partsText = new Text("Parts");
        this.partsText.setId("title2");
        this.partsPane.add(this.partsText, 0, 0);
    }
    private void createPartsSearchButton(){
        this.partsSearchButton = new Button("Search");
        this.partsSearchButton.setOnAction((event)->{
            for(Part part : this.parts.getItems()){
                if(this.partsSearchField.getText().toLowerCase().equals(part.getName().toLowerCase())){
                    this.parts.getSelectionModel().select(part);
                    this.parts.scrollTo(part);
                    return;
                }
            }
            ErrorBox notFound = new ErrorBox("Part not found");
        });
        this.partsPane.add(this.partsSearchButton, 0, 1);
    }
    private void createPartsSearchField(){
        this.partsSearchField = new TextField();
        this.partsPane.add(this.partsSearchField, 1, 1, 2, 1);
    }
    private void createPartsTable(){
        this.parts = new TableView<>();
        this.parts.prefWidthProperty().bind(this.root.widthProperty().multiply(0.4));
        
        try{
            this.parts.getItems().clear();
            for(Part inventoryPart : Inventory.getAllParts()){
                this.parts.getItems().add(inventoryPart);
            }
        }catch(NullPointerException e){}
        
        TableColumn<Part, Integer> partIDColumn = new TableColumn<>("ID");
        partIDColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        
        TableColumn<Part, String> partNameColumn = new TableColumn<>("Name");
        partNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Part, Integer> partInventoryColumn = new TableColumn<>("Inv Level");
        partInventoryColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        
        TableColumn<Part, Double> partPriceColumn = new TableColumn<>("Price/Part");
        partPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        
        partIDColumn.prefWidthProperty().bind(this.parts.widthProperty().multiply(0.15));
        partNameColumn.prefWidthProperty().bind(this.parts.widthProperty().multiply(0.45));
        partInventoryColumn.prefWidthProperty().bind(this.parts.widthProperty().multiply(0.15));
        partPriceColumn.prefWidthProperty().bind(this.parts.widthProperty().multiply(0.25));        
        
        this.parts.getColumns().add(partIDColumn);
        this.parts.getColumns().add(partNameColumn);
        this.parts.getColumns().add(partInventoryColumn);
        this.parts.getColumns().add(partPriceColumn);
             
        this.parts.setPlaceholder(new Label("No parts to display."));
        
        this.partsPane.add(this.parts, 0, 2, 11, 3);
    }
    private void createAddPartButton(){
        this.addPart = new Button("Add");
        this.addPart.setOnAction((event)->{
           AddPartScene addPartScene = new AddPartScene(this.mainStage);
        });
        this.partsPane.add(this.addPart, 3, 5);
    }
    private void createModifyPartButton(){
        this.modifyPart = new Button("Modify");
        this.modifyPart.setOnAction((event)->{
            try{
                Part selectedPart = this.parts.getSelectionModel().getSelectedItem();
                ModifyPartScene modifyPartScene = new ModifyPartScene(this.mainStage, selectedPart);
            }catch(NullPointerException e){
                ErrorBox mustSelect = new ErrorBox("You must select\nan item.");
            }
        });
        this.partsPane.add(this.modifyPart, 4, 5);
    }
    private void createDeletePartButton(){
        this.deletePart = new Button("Delete");
        this.deletePart.setOnAction((event)->{
           try{
            Part selectedPart = this.parts.getSelectionModel().getSelectedItem();
            this.parts.getItems().remove(selectedPart);
            Inventory.deletePart(selectedPart);
           }catch(NullPointerException e){
               ErrorBox mustSelect = new ErrorBox("You must select\nan item.");
           }
        });
        this.partsPane.add(this.deletePart, 5, 5);
    }
    //product pane
    private void createProductsPane(){
        this.productsPane = new GridPane();
        this.productsPane.setId("box");
        this.mainContentBox.getChildren().add(this.productsPane);
    }
    private void createProductsText(){
        this.productsText = new Text("Products");
        this.productsText.setId("title2");
        this.productsPane.add(this.productsText, 0, 0);
    }
    private void createProductsSearchButton(){
        this.productsSearchButton = new Button("Search");
        this.productsSearchButton.setOnAction((event)->{
            for(Product product : this.products.getItems()){
                if(this.productsSearchField.getText().toLowerCase().equals(product.getName().toLowerCase())){
                    this.products.getSelectionModel().select(product);
                    this.products.scrollTo(product);
                    return;
                }
            }
            ErrorBox notFound = new ErrorBox("Product not found");
        });
        this.productsPane.add(this.productsSearchButton, 0, 1);
    }
    private void createProductsSearchField(){
        this.productsSearchField = new TextField();
        this.productsPane.add(this.productsSearchField, 1, 1, 2, 1);
    }
    private void createProductsTable(){
        this.products = new TableView<>();
        this.products.prefWidthProperty().bind(this.root.widthProperty().multiply(0.4));

        try{
            this.products.getItems().clear();
            for(Product inventoryProduct : Inventory.getAllProducts()){
                this.products.getItems().add(inventoryProduct);
            }
        }catch(NullPointerException e){}
        
        TableColumn<Product, Integer> productIDColumn = new TableColumn<>("ID");
        productIDColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        
        TableColumn<Product, String> productNameColumn = new TableColumn<>("Name");
        productNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Product, Integer> productInventoryColumn = new TableColumn<>("Inv Level");
        productInventoryColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        
        TableColumn<Product, Double> productPriceColumn = new TableColumn<>("Price/Prod");
        productPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        
        productIDColumn.prefWidthProperty().bind(this.products.widthProperty().multiply(0.15));
        productNameColumn.prefWidthProperty().bind(this.products.widthProperty().multiply(0.45));
        productInventoryColumn.prefWidthProperty().bind(this.products.widthProperty().multiply(0.15));
        productPriceColumn.prefWidthProperty().bind(this.products.widthProperty().multiply(0.25));
        
        this.products.getColumns().add(productIDColumn);
        this.products.getColumns().add(productNameColumn);
        this.products.getColumns().add(productInventoryColumn);
        this.products.getColumns().add(productPriceColumn);
        
        this.products.setPlaceholder(new Label("No products to show."));
        
        this.productsPane.add(this.products, 0, 2, 11, 3);
    }
    private void createAddProductButton(){
        this.addProduct = new Button("Add");
        this.addProduct.setOnAction((event)->{
            AddProductScene addProductScene = new AddProductScene(this.mainStage);
        });
        this.productsPane.add(this.addProduct, 3, 5);
    }
    private void createModifyProductButton(){
        this.modifyProduct = new Button("Modify");
        this.modifyProduct.setOnAction((event)->{
            try{
                Product selectedProduct = this.products.getSelectionModel().getSelectedItem();
                ModifyProductScene modifyProductScene = new ModifyProductScene(this.mainStage, selectedProduct);
            }catch(NullPointerException e){
                ErrorBox mustSelect = new ErrorBox("You must select\nan item.");
            }
        });
        this.productsPane.add(this.modifyProduct, 4, 5);
    }
    private void createDeleteProductButton(){
        this.deleteProduct = new Button("Delete");
        this.deleteProduct.setOnAction((event)->{
            try{
                Product selectedProduct = this.products.getSelectionModel().getSelectedItem();
                this.products.getItems().remove(selectedProduct);
                Inventory.deleteProduct(selectedProduct);
            }catch(NullPointerException e){
                ErrorBox mustSelect = new ErrorBox("You must select\nan item.");
            }
        });
        this.productsPane.add(this.deleteProduct, 5, 5);
    }
    private void createExitButton(){
        this.exitButton = new Button("Exit");
        this.exitButton.setOnAction((event)->{
            System.exit(0);
        });
        Text space = new Text("");
        this.productsPane.add(space, 6, 6);
        this.productsPane.add(this.exitButton, 6, 7);
    }
}