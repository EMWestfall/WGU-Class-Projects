package customer;

import division.DivisionDAO;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scenecontroller.SceneController;

/**
 * This is the controller for the CustomerView.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class CustomerController implements Initializable {
    
    //Data DAO
    private CustomerDAOImp dao = data.DAOCollection.getCustomerDao();
    private DivisionDAO divDao = data.DAOCollection.getDivDao();
    
    //TableView Variables
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> custId;
    @FXML private TableColumn<Customer, String> custName;
    @FXML private TableColumn<Customer, String> custAddress;
    @FXML private TableColumn<Customer, String> custPostal;
    @FXML private TableColumn<Customer, String> custPhone;
    @FXML private TableColumn<Customer, String> custDivision;
    
    
    //Handle Methods
    
    /**
     * This method loads the CustomerEdit scene for an add after the user clicks
     * the add button. The CustomerEditController knows it's an add because
     * this method does not set the customerToEdit in the
     * CustomerEditController.
     */
    @FXML private void handleAdd(){
        SceneController.loadScene("CustomerEdit");
        System.out.println("Successful change to customer edit screen.");
    }
    
    /**
     * This method loads the CustomerEdit scene for an edit after the user
     * clicks the edit button. The CustomerEditController knows it's an edit
     * because this method passes the Customer to edit to the
     * CustomerEditController.
     */
    @FXML private void handleEdit(){
        this.dao.setCustomerToEdit(customerTable.getSelectionModel().getSelectedItem().getCustomerId());
        SceneController.loadScene("CustomerEdit");
    }
    
    /**
     * This method sends a delete request to the CustomerDAO when the user
     * clicks the delete button.
     */
    @FXML private void handleDelete(){
        int customerId = this.customerTable.getSelectionModel().getSelectedItem().getCustomerId();
        SceneController.loadAlert("Deleting customer #" + customerId + "\nName: " + this.customerTable.getSelectionModel().getSelectedItem().getName());
        this.dao.deleteCustomer(customerId);
        this.displayCustomers();
        System.out.println("Deleted record.");
    }
    
    /**
     * This method loads the Main Menu when the user clicks the cancel button.
     */
    @FXML private void handleCancel(){
        SceneController.loadScene("MainMenu");
        System.out.println("Cancelled, went back one scene.");
    }
    
    //--------------Initialize Method----------------
    
    /**
     * This method initializes the CustomerController and loads the customers to
     * the tableview.
     * 
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        this.dao.setCustomerToEdit(0);
        this.displayCustomers();
        System.out.println("Successful initialize of CustomerController.");
    }    
    
    //----------------TableView Methods-----------------------
    
    /**
     * This method binds Customers to the tableview.
     */
    private void bindTableView(){
        try{
            this.customerTable.getItems().clear();
        }catch(NullPointerException e){
            System.out.println("TableView is already clear; bypassing NullPointerException.");
        }
        Set keys = dao.getCustomerKeys();
        for(Object key : keys){
            this.customerTable.getItems().add(this.dao.getCustomer((int)key));
        }
        System.out.println("Successful binding table view in CustomerController.");
    }
    
    /**
     * This method binds each column in the tableview to its appropriate data.
     * This method is a series of lambdas that populate the various columns.
     * Lambdas are easy to use here and make the code easy to read because
     * the setCellValueFactory() methods are factories that take an iterated
     * series of constructor methods as input. The alternative would be a series
     * of for loops, which are much more tedious and less readable.
     */
    private void bindColumns(){
        this.custId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()).asObject());
        this.custName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        this.custAddress.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
        this.custPostal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPostalCode()));
        this.custPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        this.custDivision.setCellValueFactory(data -> new SimpleStringProperty(divDao.getDivision(data.getValue().getDivisionId()).getDivName()));
        System.out.println("Successful binding columns in CustomerController.");
    }
    
    /**
     * This method calls the data in the customer DAO to bind to the tableview.
     */
    private void displayCustomers(){
        this.dao.getCustomers();
        this.bindTableView();
        this.bindColumns();
    }
    
    //-----------Getters-----------------------
    /**
     * Getter method for the customer DAO this controller uses.
     * 
     * @return The CustomerDAOImp instance being used.
     */
    public CustomerDAOImp getDao(){
        return this.dao;
    }
}
