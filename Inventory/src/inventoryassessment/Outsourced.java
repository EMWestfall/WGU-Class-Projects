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
public class Outsourced extends Part{
    private String companyName;
    public Outsourced(String name, double price, int stock, int min, int max, String companyName){
        super(name, price, stock, min, max);
        this.companyName = companyName;
    }
    void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    String getCompanyName(){
        return this.companyName;
    }
    boolean isOutsourced(){
        return true;
    }
}
