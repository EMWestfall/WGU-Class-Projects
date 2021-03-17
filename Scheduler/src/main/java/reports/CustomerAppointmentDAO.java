package reports;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class CustomerAppointmentDAO {
    private HashMap<Integer, CustomerAppointment> custApptMap; 
    
    /**
     * Constructor for the CustomerAppointmentDAO.
     */
    public CustomerAppointmentDAO(){
        this.custApptMap = new HashMap<>();
    }
    
    /**
     * Does a SELECT query that returns a count(*) grouped by customer and
     * month and puts it in the HashMap with customer_name to
     * CustomerAppointment key/value pairs. 
     * The query is inner joined with customers to return customer name
     * instead of customer id.
     */
    public void getCustApptsFromDatabase(){
        try{
            String query = "SELECT Customer_Name, MONTH(Start), count(*) "
                    + "FROM appointments "
                    + "INNER JOIN customers "
                    + "ON appointments.Customer_ID = customers.Customer_ID "
                    + "GROUP BY Customer_Name, MONTH(Start);";
            System.out.println(query);
            DBStatement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQLException in query execution in CustomerAppointmentDAO.getCustAppts().");
        }
        
        try{
            int lineCount = 1;
            ResultSet rs = DBStatement.getResultSet();
            rs.beforeFirst();
            while(rs.next()){
                CustomerAppointment ca = new CustomerAppointment(lineCount, rs.getString(1), Month.of(rs.getInt(2)), rs.getInt(3));
                this.custApptMap.put(lineCount, ca);
                lineCount++;
            }
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQLException in result set read in CustomerAppointmentDAO.getCustAppts().");
        }
    }
    
    /**
     * Returns the key set of the CustomerAppointments HashMap.
     * 
     * @return The Set of CustomerAppointments keys, which are strings.
     */
    public Set getKeys(){
        return this.custApptMap.keySet();
    }
    
    /**
     * This method returns a CustomerAppointment by its index number.
     * It's used to iterate over the HashMap by integers.
     * 
     * @param lineNumber    The index number in the HashMap.
     * @return              The CustomerAppointment to return.
     */
    public CustomerAppointment getCustomerAppointment(int lineNumber){
        return this.custApptMap.get(lineNumber);
    }
}
