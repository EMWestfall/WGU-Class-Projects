package contact;

/**
 * This is the class for the Contact object.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class Contact {
    private int contId;
    private String contName;
    
    /**
     * This is the constructor for the Contact object.
     * 
     * @param contId    The contact's id.
     * @param contName  The String with the contact's name.
     */
    public Contact(int contId, String contName){
        this.contId = contId;
        this.contName = contName;
    }
    
    /**
     * Setter for the contact's id.
     * 
     * @param contId The contact's id number.
     */
    public void setContId(int contId){
        this.contId = contId;
    }
    
    /**
     * Setter for the contact's name.
     * 
     * @param contName The String with the contact's name.
     */
    public void setContName(String contName){
        this.contName = contName;
    }
    
    /**
     * Getter for the contact's id.
     * 
     * @return The contact's id.
     */
    public int getContId(){
        return this.contId;
    }
    
    /**
     * Getter for the contact's name.
     * 
     * @return The String of the contact's name.
     */
    public String getContName(){
        return this.contName;
    }
}
