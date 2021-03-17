package customer;

import countries.CountryDAO;
import data.DBStatement;
import division.DivisionDAO;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import scenecontroller.SceneController;

/**
 * This is the controller for the CustomerEditView.
 *
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class CustomerEditController implements Initializable {
    
    private final CustomerDAOImp dao = data.DAOCollection.getCustomerDao();
    private final DivisionDAO divDao = data.DAOCollection.getDivDao();
    private final CountryDAO countryDao = data.DAOCollection.getCountryDao();
    private HashMap<String, Integer> reverseCountryMap = new HashMap<>();
    private ObservableList<String> custDivList = FXCollections.observableArrayList();
    private ObservableList<String> countryList = FXCollections.observableArrayList();
    
    @FXML private Customer custToEdit;
    @FXML private TextField custId;
    @FXML private TextField custName;
    @FXML private TextField custAddress;
    @FXML private TextField custPostal;
    @FXML private TextField custPhone;
    @FXML private ChoiceBox country;
    @FXML private ChoiceBox custDivision;

    //--------------Handle Methods---------------
    
    /**
     * This method handles a user change to the country choice box.
     * Based on the user's choice, this method updates the custDivList to only
     * list divisions based on the user's chosen country location.
     * <p>
     * The lambda here iterates through a forEach in the Collections API, using
     * keys to pass to the method getDivision in the DivisionDAO to retrieve
     * divisions. The forEach method in the Collections API is a natural place
     * for lambdas and is setup to accommodate them, thus why it's used here.
     */
    @FXML private void handleCountry(){
        this.custDivList.clear();
        System.out.println("Cleared customer list.");
        this.divDao.getDivMapKeys().forEach(key -> {
            if(this.divDao.getDivision((int)key).getCountryId() == 
                    (int) this.reverseCountryMap.get(this.country.getValue().toString())) //If this division is in the selected country, then add it to the list to display
                this.custDivList.add(this.divDao.getDivision
                                                    ((int) key).getDivName());
        });
        this.custDivision.setItems(this.custDivList);
        System.out.println("Set division list.");
    }
    
    /**
     * This method returns to the Customer scene when the user hits cancel.
     */
    @FXML private void handleCancel(){
        SceneController.loadScene("Customer");
    }
    
    /**
     * This method handles the action to take when the user clicks accept. It
     * passes the customer information to the DAO, directs it to do an
     * INSERT INTO or an UPDATE, and then loads the CustomerView again.
     */
    @FXML private void handleAccept(){
        System.out.println("Entered handle accept.");
        String name = this.custName.getText();
        String address = this.custAddress.getText();
        String postal = this.custPostal.getText();
        String phone = this.custPhone.getText();
        String division = this.custDivision.getValue()
                                           .toString();
        System.out.println("Before query.");
        if(this.dao.getCustomerToEdit() == 0){
            this.dao.addCustomerToDatabase(name, address, postal, phone, division);
        }else{
            this.dao.updateCustomer(this.dao.getCustomerToEdit(), name, address, postal, phone, division);
        }
        this.dao.setCustomerToEdit(0);
        SceneController.loadScene("Customer");
    }
    
    /**
     * This method initializes the starting conditions of the CustomerEditView.
     * The lambda used here is a forEach method from the Collections API, which
     * is naturally set up to be used in a lambda to iterate over the
     * collection, thus why it is used here.
     * 
     * @param url Unused.
     * @param rb  Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {

        //Set the Division choices
        this.countryDao.getKeys().forEach(key -> {
            String name = this.countryDao.getCountry
                                        ((int) key).getCountryName();
            this.countryList.add(name);
            this.reverseCountryMap.put(name, (int)key);
        });
        this.country.setItems(this.countryList);
        System.out.println("Successfully set the country list.");
        
        //Detect whether this is an add or an edit.
        if(this.dao.getCustomerToEdit() == 0){
            //If this is an add, this branch will activate. Grab the highest number in Customer_ID to add 1 later and get the next customer number.
            try{
                String query = "SELECT MAX(Customer_ID) FROM customers;";
                System.out.println(query);
                DBStatement.executeQuery(query);
            }catch(SQLException e){
                System.out.println(e);
                System.out.println("Exception getting MAX(Customer_ID) in CustomerEditController.initialize().");
            }
            try{
                ResultSet rs = DBStatement.getResultSet();
                if(rs.next()){
                    this.custId.setText(String.valueOf
                                                (rs.getInt(1) + 1));
                }
            }catch(SQLException sqle){
                System.out.println(sqle);
                System.out.println("Exception getting result set in CustomerEditController.initialize().");
            }
            //Set the default country ID to U.S
            String countryName = this.countryDao.getCountry(1).getCountryName();
            System.out.println("Initialize country ID: " + countryName);
            this.country.setValue(countryName);
        }else{
            //If this is an edit, grab the customer to edit's information.
            this.custToEdit = this.dao.getCustomer
                                            (this.dao.getCustomerToEdit());
            this.custId.setText(String.valueOf
                                            (this.custToEdit.getCustomerId()));
            this.custName.setText(this.custToEdit.getName());
            this.custAddress.setText(this.custToEdit.getAddress());
            this.custPostal.setText(this.custToEdit.getPostalCode());
            this.custPhone.setText(this.custToEdit.getPhone());
            this.country.setValue(this.countryDao.getCountry
                                                    (this.divDao.getDivision
                                                        (this.custToEdit.getDivisionId())
                                                                        .getCountryId())
                                                                        .getCountryName());
            this.custDivision.setValue(this.divDao.getDivision
                                                        (this.custToEdit.getDivisionId())
                                                                        .getDivName());
        }
    }

    //Setters
    
    /**
     * This method sets the incoming customer chosen from the CustomerView if
     * this is to be an update rather than add.
     * 
     * @param cust The Customer to edit.
     */
    public void setIncomingCustomer(Customer cust){
        this.custToEdit = cust;
        System.out.println("Successful set incoming customer.");
    }

}
