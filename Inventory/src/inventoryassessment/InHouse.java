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
public class InHouse extends Part{
    private int machineId;
    public InHouse(String name, double price, int stock, int min, int max, int machineId){
        super(name, price, stock, min, max);
        this.machineId = machineId;
    }
    void setMachineId(int machineId){
        this.machineId = machineId;
    }
    int getMachineId(){
        return this.machineId;
    }
    boolean isInHouse(){
        return true;
    }
}
