/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

/**
 *
 * @author Eric
 */
public abstract class Settings {
    private static int WINDOW_HEIGHT;
    private static int WINDOW_WIDTH;
    private static String STYLESHEET;
    
    static{
        WINDOW_HEIGHT = 750;
        WINDOW_WIDTH = 900;
        STYLESHEET = "stylesheets/inventory.css";
    }
    public static void setWindowHeight(int newHeight){
        WINDOW_HEIGHT = newHeight;
    }
    public static void setWindowWidth(int newWidth){
        WINDOW_WIDTH = newWidth;
    }
    public static void setStylesheet(String newStylesheet){
        STYLESHEET = newStylesheet;
    }
    public static int getWindowHeight(){
        return WINDOW_HEIGHT;
    }
    public static int getWindowWidth(){
        return WINDOW_WIDTH;
    }
    public static String getStylesheet(){
        return STYLESHEET;
    }
}
