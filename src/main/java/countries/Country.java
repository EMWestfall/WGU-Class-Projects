package countries;

/**
 * This is the class that holds the Country object.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class Country {
    private int countryId;
    private String countryName;
    
    /**
     * This is the constructor for the Country object.
     * 
     * @param countryId     The country's id number.
     * @param countryName   The String of the country's name.
     */
    public Country(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    /**
     * Setter for the country's id.
     * 
     * @param countryId The country's id.
     */
    public void setCountryId(int countryId){
        this.countryId = countryId;
    }
    
    /**
     * Setter for the country's name.
     * 
     * @param countryName The String of the country's name.
     */
    public void setCountryName(String countryName){
        this.countryName = countryName;
    }
    
    /**
     * The getter for the country's id.
     * 
     * @return The country's id number.
     */
    public int getCountryId(){
        return this.countryId;
    }
    
    /**
     * The getter for the country's name.
     * 
     * @return The String of the country's name.
     */
    public String getCountryName(){
        return this.countryName;
    }
}
