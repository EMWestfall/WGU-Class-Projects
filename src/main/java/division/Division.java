package division;

/**
 *
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class Division {
    private int divId;
    private String divName;
    private int countryId;
    
    /**
     * This is the constructor for Division.
     * 
     * @param divId     The division's id number.
     * @param divName   The String of the division's name.
     * @param countryId The division's country's id.
     */
    public Division(int divId, String divName, int countryId){
        this.divId = divId;
        this.divName = divName;
        this.countryId = countryId;
    }
    
    /**
     * Getter for the division's id.
     * 
     * @return The division's id.
     */
    public int getDivId(){
        return this.divId;
    }
    
    /**
     * Getter for the division's name.
     * 
     * @return The String of the division's name.
     */
    public String getDivName(){
        return this.divName;
    }
    
    /**
     * Getter for the division's country's id.
     * 
     * @return The division's country's id.
     */
    public int getCountryId(){
        return this.countryId;
    }
    
    /**
     * Setter for the division's id.
     * 
     * @param divId The division's id.
     */
    public void setDivId(int divId){
        this.divId = divId;
    }
    
    /**
     * Setter for the division's name.
     * 
     * @param divName The String of the division's name.
     */
    public void setDivName(String divName){
        this.divName = divName;
    }
    
    /**
     * Setter for the division's country's id.
     * 
     * @param countryId The division's country's id.
     */
    public void setCountryId(int countryId){
        this.countryId = countryId;
    }
}
