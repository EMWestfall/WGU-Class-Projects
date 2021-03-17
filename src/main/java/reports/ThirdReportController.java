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
import user.UserDAOImp;

/**
 * This is the controller for the ThirdReportView. This view holds the third
 * report. The third report reports all the appointments for the given user
 * chosen in the user choice box.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class ThirdReportController implements Initializable{
    
//----------------Variables------------------------------------------------------------------
    //Data DAO
    private static final AppointmentDAOImp apptDao = data.DAOCollection.getAppointmentDao();
    private static final CustomerDAOImp custDao = data.DAOCollection.getCustomerDao();
    private static final ContactDAO contDao = data.DAOCollection.getContDao();
    private static final UserDAOImp userDao = data.DAOCollection.getUserDao();
    private HashMap<String, Integer> reverseUserMap = new HashMap<>();
    private ObservableList<String> userList = FXCollections.observableArrayList();
    
    //----------FXML Variables------------
    @FXML private ChoiceBox<String> userChoice;
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
    @FXML private TableColumn<Appointment, String> apptContact;

    
//------------------Methods----------------------------------------------------------------
    
    
    //-----------Handler Methods---------------
    /**
     * This method loads the Reports scene when the user clicks the back button.
     */
    @FXML private void handleBack (){
        SceneController.loadScene("Reports");
        System.out.println("Successful handle back in ReportsController.");
    }
    
    /**
     * This method updates the tableview when the user selects a new user to
     * report on from the choice box.
     */
    @FXML private void handleUserChoice (){
        this.displayAppointments();
    }
   

    //--------------TableView Methods---------------
    
    /**
     * This method binds the tableview to appointments.
     */
    private void bindTableView(){
        //User appt table
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
    }
    
    /**
     * This method binds the columns to their appropriate data.
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
        this.apptContact.setCellValueFactory(data -> new SimpleStringProperty(contDao.getContact(data.getValue().getContactId()).getContName()));
        System.out.println("Successful binding contact appt columns.");
    }
    
    /**
     * This method calls the data from the appointment DAO and then calls
     * the bind functions on the tableview.
     */
    private void displayAppointments(){
        int contactId = this.reverseUserMap.get(this.userChoice.getValue().toString());
        this.apptDao.getUserAppointments("all", contactId);
        this.bindTableView();
        this.bindColumns();
        System.out.println("Successful displayAppointments()");
    }
    
    //---------------Initialize---------------
    
    /**
     * This method initializes the starting settings for the third report.
     * It defaults the user list to the first user and then the setValue()
     * method calls displayAppointments().
     * 
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        //Initialize Time Frame ChoiceBox
        
        for (var key : this.userDao.getUserKeys()){
            String userName = this.userDao.getUser((int) key).getUserName();
            this.userList.add(userName);
            this.reverseUserMap.put(userName, (int) key);
        }
        this.userChoice.setItems(userList);
        this.userChoice.setValue(userList.get(0)); //setting the value triggers the choice box's action event, which includes displayAppointments();
        
        System.out.println("Successful initialize appointment controller method.");
    }    
}
