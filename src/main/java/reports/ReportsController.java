package reports;

import appointment.Appointment;
import appointment.AppointmentDAOImp;
import contact.ContactDAO;
import customer.CustomerDAOImp;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scenecontroller.SceneController;

/**
 * This is the controller for the ReportsView.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class ReportsController implements Initializable{
    
//----------------Variables------------------------------------------------------------------
    //Data DAO
    private static final AppointmentDAOImp apptDao = data.DAOCollection.getAppointmentDao();
    private static final CustomerDAOImp custDao = data.DAOCollection.getCustomerDao();
    private static final ContactDAO contDao = data.DAOCollection.getContDao();
    private static final CustomerAppointmentDAO custApptDao = data.DAOCollection.getCustApptDao();
    private HashMap<String, Integer> reverseContactMap = new HashMap<>();
    private ObservableList<String> contList = FXCollections.observableArrayList();
    
    //----------FXML Variables------------
    
    @FXML private ChoiceBox<String> contactChoice;
    //Contact Appointments Table View Variables
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, Integer> apptId;
    @FXML private TableColumn<Appointment, String> apptTitle;
    @FXML private TableColumn<Appointment, String> apptDescription;
    @FXML private TableColumn<Appointment, String> apptLocation;
    @FXML private TableColumn<Appointment, String> apptType;
    @FXML private TableColumn<Appointment, String> apptStart;
    @FXML private TableColumn<Appointment, String> apptEnd;
    @FXML private TableColumn<Appointment, String> apptCustomerId;
    
    //Customer Appointments Count Table View Variables
    @FXML private TableView<CustomerAppointment> custApptTable;
    @FXML private TableColumn<CustomerAppointment, String> custApptColumn;
    @FXML private TableColumn<CustomerAppointment, String> custApptMonthColumn;
    @FXML private TableColumn<CustomerAppointment, Integer> numberApptsColumn;
    
//------------------Methods----------------------------------------------------------------
    
    
    //-----------Handler Methods---------------
    /**
     * Takes the user back to the main menu when they click the back button.
     */
    @FXML private void handleBack (){
        SceneController.loadScene("MainMenu");
        System.out.println("Successful handle back in ReportsController.");
    }
    
    /**
     * Updates the tableview when the user selects a new contact from the choice
     * box.
     */
    @FXML private void contactChoiceHandle(){
        this.displayAppointments();
    }
    
    /**
     * Takes the user to the page where the third report is.
     */
    @FXML private void handleThirdReport(){
        SceneController.loadScene("ThirdReport");
        System.out.println("Successful handle third report in ReportsController.");
    }
    
    //--------------TableView Methods---------------
    
    /**
     * Binds appointments to the first tableview and customerAppointments to
     * the second.
     */
    private void bindTableView(){
        //First table
        try{
            this.appointmentsTable.getItems().clear();
        }catch(NullPointerException e){
            System.out.println("TableView is already clear; bypassing NullPointerException.");
        }
        Set keys = this.apptDao.getAppointmentKeys();
        for(Object key : keys){
            this.appointmentsTable.getItems().add(this.apptDao.getAppointment((int)key));
        }
        System.out.println("Successful binding first table view.");
        
        //Second table
        try{
            this.custApptTable.getItems().clear();
        }catch(NullPointerException e){
            System.out.println("TableView is already clear; bypassing NullPointerException.");
        }
        Set custApptKeys = this.custApptDao.getKeys();
        for(var key : custApptKeys){
            this.custApptTable.getItems().add(this.custApptDao.getCustomerAppointment((int) key));
        }
        System.out.println("Successful binding second table view.");
    }
    
    /**
     * Binds the columns in both tables to their appropriate data sources.
     * This method is a series of lambdas that populate the various columns.
     * Lambdas are easy to use here and make the code easy to read because
     * the setCellValueFactory() methods are factories that take an iterated
     * series of constructor methods as input. The alternative would be a series
     * of for loops, which are much more tedious and less readable.
     */
    private void bindColumns(){
        
        //Contact Appointments table (first table)
        this.apptId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAppointmentId()).asObject());
        this.apptTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        this.apptDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        this.apptLocation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        this.apptType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        this.apptStart.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartDateTimeString()));
        this.apptEnd.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndDateTimeString()));
        this.apptCustomerId.setCellValueFactory(data -> new SimpleStringProperty(this.custDao.getCustomer(data.getValue().getCustomerId()).getName()));
        System.out.println("Successful binding contact appt columns.");
        
        //Customer Appointments table (second table)
        this.custApptColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));
        this.custApptMonthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMonth().toString()));
        this.numberApptsColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumberAppts()).asObject());
        System.out.println("Successful binding customer appt columns.");
    }
    
    /**
     * Calls data from the DAO objects and then calls the bind methods to
     * populate the table views with the data.
     */
    private void displayAppointments(){
        int contactId = this.reverseContactMap.get(this.contactChoice.getValue().toString());
        this.apptDao.getContactAppointments(contactId);
        this.custApptDao.getCustApptsFromDatabase();
        this.bindTableView();
        this.bindColumns();
        System.out.println("Successful displayAppointments()");
    }
    
    //---------------Initialize---------------
    
    /**
     * Initializes the starting conditions of the reports view.
     * The lambda here is a forEach iteration over the key set of a HashMap.
     * The forEach method makes it really easy to read and write functions
     * that iterate over a Collection object.
     * 
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        //Initialize Time Frame ChoiceBox
        ReportsController.contDao.getContactMapKeys().forEach((var key) ->{
            String contName = this.contDao.getContact((int) key).getContName();
            this.contList.add(contName);
            this.reverseContactMap.put(contName, (int) key);
        });
        this.contactChoice.setItems(contList);
        this.contactChoice.setValue(contList.get(0)); //setting the value triggers the choice box's action event, which includes displayAppointments();
        
        System.out.println("Successful initialize appointment controller method.");
    }    
}
