/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 * This class is the User object.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class User {
    private int userId;
    private String userName;
    private String userPassword;
    
    /**
     * The constructor for the User object.
     * 
     * @param userId        The user id to set.
     * @param userName      The String of the user name to set.
     * @param userPassword  The String of the user's password.
     */
    public User(int userId, String userName, String userPassword){
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
    }
    
    /**
     * Sets the user id.
     * 
     * @param userId The user id to set.
     */
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    /**
     * Set the user name.
     * 
     * @param userName The String of the user name to set.
     */
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    /**
     * Sets the user password.
     * 
     * @param userPassword The String of the user password to set.
     */
    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }
    
    /**
     * Getter for the user's id.
     * 
     * @return The id of the user to return.
     */
    public int getUserId(){
        return this.userId;
    }
    
    /**
     * Getter for the user's name.
     * 
     * @return The String of the user's name.
     */
    public String getUserName(){
        return this.userName;
    }
    
    /**
     * Getter for the user's password.
     * 
     * @return The String of the user's password.
     */
    public String getUserPassword(){
        return this.userPassword;
    }
}
