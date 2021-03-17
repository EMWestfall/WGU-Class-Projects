package data;

/**
 * This class holds the instantiated DAOs used throughout the application.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class DAOCollection {
    private static final appointment.AppointmentDAOImp appointmentDaoImp = new appointment.AppointmentDAOImp();
    private static final customer.CustomerDAOImp customerDaoImp = new customer.CustomerDAOImp();
    private static final user.UserDAOImp userDaoImp = new user.UserDAOImp();
    private static final division.DivisionDAO divDAOImp = new division.DivisionDAO();
    private static final contact.ContactDAO contactDAO = new contact.ContactDAO();
    private static final countries.CountryDAO countryDAO = new countries.CountryDAO();
    private static final reports.CustomerAppointmentDAO custApptDao = new reports.CustomerAppointmentDAO();
    
    /**
     * Getter for the AppointmentDAO.
     * 
     * @return The AppointmentDAOImp instance to return.
     */
    public static appointment.AppointmentDAOImp getAppointmentDao(){
        return DAOCollection.appointmentDaoImp;
    }
    
    /**
     * Getter for the CustomerDAO.
     * 
     * @return The CustomerDAOImp instance to return.
     */
    public static customer.CustomerDAOImp getCustomerDao(){
        return DAOCollection.customerDaoImp;
    }
    
    /**
     * Getter for the UserDAO.
     * 
     * @return The UserDAOImp instance to return.
     */
    public static user.UserDAOImp getUserDao(){
        return DAOCollection.userDaoImp;
    }
    
    /**
     * Getter for the DivisionDAO.
     * 
     * @return the DivisionDAO instance to return.
     */
    public static division.DivisionDAO getDivDao(){
        return DAOCollection.divDAOImp;
    }
    
    /**
     * Getter for the ContactDAO.
     * 
     * @return The ContactDAO instance to return.
     */
    public static contact.ContactDAO getContDao(){
        return DAOCollection.contactDAO;
    }
    
    /**
     * Getter for the CountryDAO.
     * 
     * @return The CountryDAO instance to return.
     */
    public static countries.CountryDAO getCountryDao(){
        return DAOCollection.countryDAO;
    }
    
    /**
     * Getter for the CustomerAppointmentDAO (report on appointments/customer).
     * 
     * @return The CustomerAppointmentDAO instance to return.
     */
    public static reports.CustomerAppointmentDAO getCustApptDao(){
        return DAOCollection.custApptDao;
    }
}
