package appointment;

import contact.ContactDAO;
import customer.CustomerDAOImp;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scenecontroller.SceneController;
import user.UserDAOImp;

/**
This class is the controller for AppointmentView.
* @author Eric Westfall
* @version 1.0, 01/22/21
*/
public class AppointmentController implements Initializable {

//----------------Variables------------------------------------------------------------------
    //Data DAO
    private static final AppointmentDAOImp dao = data.DAOCollection.getAppointmentDao();
    private static final CustomerDAOImp custDao = data.DAOCollection.getCustomerDao();
    private static final ContactDAO contDao = data.DAOCollection.getContDao();
    private static final UserDAOImp userDao = data.DAOCollection.getUserDao();
    //----------FXML Variables------------
    @FXML private ChoiceBox<String> appointmentChoiceBox;
    @FXML private Label loggedInAsLabel;
    //Table View Variables
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, Integer> apptId;
    @FXML private TableColumn<Appointment, String> apptContact;
    @FXML private TableColumn<Appointment, String> apptTitle;
    @FXML private TableColumn<Appointment, String> apptDescription;
    @FXML private TableColumn<Appointment, String> apptLocation;
    @FXML private TableColumn<Appointment, String> apptType;
    @FXML private TableColumn<Appointment, String> apptStart;
    @FXML private TableColumn<Appointment, String> apptEnd;
    @FXML private TableColumn<Appointment, String> apptCustomerId;
    @FXML private TableColumn<Appointment, String> apptUser;
    
//------------------Methods----------------------------------------------------------------
    
    
    //-----------Handler Methods---------------
    
    /**
     * Handles what happens when the user clicks Add in AppointmentView.
     * Loads the appointment edit view with no data.
     */
    @FXML private void handleAdd(){
        SceneController.loadScene("AppointmentEdit");
        System.out.println("Successful handle add in appointment.");
    }
    
    /**
     * Handles what happens when the user clicks Edit in AppointmentView.
     * Passes the appointment's id to the appointment DAO and then loads
     * the appointment edit view with the selected appointment's data.
     */
    @FXML private void handleEdit(){
        this.dao.setAppointmentToEdit(appointmentsTable.getSelectionModel().getSelectedItem().getAppointmentId());
        SceneController.loadScene("AppointmentEdit");
    }
    
    /**
     * Handles what happens when the user clicks Delete in AppointmentView.
     * Deletes the selected appointment.
     */
    @FXML private void handleDelete(){
        Appointment appt = this.dao.getAppointment(this.appointmentsTable.getSelectionModel().getSelectedItem().getAppointmentId());
        SceneController.loadAlert("Canceled\nAppt#: " + appt.getAppointmentId() + "\nTitle: " + appt.getTitle() + "\nType: " + appt.getType());
        this.dao.deleteAppointment(appt.getAppointmentId());
        this.displayAppointments();
        System.out.println("Successful deleting appointment in AppointmentController.handleDelete().");
    }
    
    /**
     * Handles what happens when the user clicks Cancel in AppointmentView.
     * Takes the user back to the main menu.
     */
    @FXML private void handleCancel(ActionEvent e){
        SceneController.loadScene("MainMenu");
        System.out.println("Successful handle cancel in appointment.");
    }
    
    /**
     * Refreshes the appointments table when the user chooses a new time frame
     * in the choice box.
     */
    @FXML private void apptChoiceBoxHandle(){
        this.displayAppointments();
    }
    
    //--------------TableView Methods---------------
    
    /**
     * Binds the tableview to Appointments from the AppointmentDAO.
     */
    private void bindTableView(){
        try{
            this.appointmentsTable.getItems().clear();
        }catch(NullPointerException e){
            System.out.println("TableView is already clear; bypassing NullPointerException.");
        }
        Set keys = dao.getAppointmentKeys();
        for(Object key : keys){
            this.appointmentsTable.getItems().add(this.dao.getAppointment((int)key));
        }
        System.out.println("Successful binding table view.");
    }
    
    /**
     * Binds the table view's columns to their matching data.
     * <p>
     * This method is a series of lambdas that populate the various columns.
     * Lambdas are easy to use here and make the code easy to read because
     * the setCellValueFactory() methods are factories that take an iterated
     * series of constructor methods as input. The alternative would be a series
     * of for loops, which are much more tedious and less readable.
     */
    private void bindColumns(){
        this.apptId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAppointmentId()).asObject());
        this.apptContact.setCellValueFactory(data -> new SimpleStringProperty(contDao.getContact(data.getValue().getContactId()).getContName()));
        this.apptTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        this.apptDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        this.apptLocation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        this.apptType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        this.apptStart.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartDateTimeString()));
        this.apptEnd.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndDateTimeString()));
        this.apptCustomerId.setCellValueFactory(data -> new SimpleStringProperty(custDao.getCustomer(data.getValue().getCustomerId()).getName()));
        this.apptUser.setCellValueFactory(data -> new SimpleStringProperty(userDao.getUser(data.getValue().getUserId()).getUserName()));
        System.out.println("Successful binding columns.");
    }
    
    /**
     * Displays the appointments in the table view.
     * Retrieves appointments from the appointment DAO using the time mode
     * selected and then displays them in the appointment table view.
     */
    private void displayAppointments(){
        String mode;
        if (this.appointmentChoiceBox.getValue().equals("This Week")){
            mode = "week";
        }else if (this.appointmentChoiceBox.getValue().equals("This Month")){
            mode = "month";
        }else{
            mode = "all";
        }
        this.dao.getUserAppointments(mode);
        this.bindTableView();
        this.bindColumns();
        System.out.println("Successful displayAppointments()");
    }
    
    
    /**
     * Initializes the appointment view.
     * 
     * @param url   Unused.
     * @param rb    Unused.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        //Initialize Time Frame ChoiceBox
        ObservableList<String> list = FXCollections.observableArrayList("This Week", "This Month", "All");
        this.appointmentChoiceBox.setItems(list);
        this.appointmentChoiceBox.setValue(list.get(0)); //setting the value triggers the choice box's action event, which includes displayAppointments();
        
        //Reset appointment to edit so that if add is pressed, the appointment edit screen will do an edit rather than an edit
        this.dao.setAppointmentToEdit(0);
        
        this.loggedInAsLabel.setText("Seeing Appointments For User: " + this.userDao.getUser(this.userDao.getCurrentUserId()).getUserName());
        System.out.println("Successful initialize appointment controller method.");
    }    

    //-------------Getter Methods---------------
    /**
     * Returns the appointment DAO.
     * 
     * @return The AppointmentDAOImp returned.
     */
    public AppointmentDAOImp getDao(){
        return this.dao;
    }
}
