/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryassessment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Eric
 */
public class Inventory {
    private static List<Part> partList = new ArrayList<Part>();
    private static List<Product> productList = new ArrayList<Product>();
    private static ObservableList<Part> allParts = FXCollections.observableList(partList);
    private static ObservableList<Product> allProducts = FXCollections.observableList(productList);
    private static int deletedPartCounter = 0;
    private static int deletedProductCounter = 0;
    

    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    //TODO : Handle exceptions of next four methods
    public static Part lookupPart(int partId) 
            throws IllegalArgumentException{
        for (Part part : allParts){
            if (part.getId() == partId) return part;
        }
        throw new IllegalArgumentException("Could not find part.");
    }
    public static Product lookupProduct(int productId) 
            throws IllegalArgumentException{
        for (Product product : allProducts){
            if (product.getId() == productId) return product;
        }
        throw new IllegalArgumentException("Could not find product.");
    }
    public static Part lookupPart(String partName) 
            throws IllegalArgumentException{
        for (Part part : allParts){
            if (part.getName().equals(partName)) return part;
        }
        throw new IllegalArgumentException("Could not find part.");
    }
    public static Product lookupProduct(String productName) 
            throws IllegalArgumentException{
        for (Product product : allProducts){
            if (product.getName().equals(productName)) return product;
        }
        throw new IllegalArgumentException("Could not find product.");
    }
    public static void updateInHouse(Part oldPart, String name, int stock, 
            double price, int min, int max, int machineID){
        oldPart.setName(name);
        oldPart.setStock(stock);
        oldPart.setPrice(price);
        oldPart.setMin(min);
        oldPart.setMax(max);
        InHouse castPart = (InHouse)oldPart;
        castPart.setMachineId(machineID);
    }
    public static void updateOutsourced(Part oldPart, String name, int stock, 
            double price, int min, int max, String company){
        oldPart.setName(name);
        oldPart.setStock(stock);
        oldPart.setPrice(price);
        oldPart.setMin(min);
        oldPart.setMax(max);
        Outsourced castPart = (Outsourced) oldPart;
        castPart.setCompanyName(company);
    }
    public static void updateProduct(Product oldProduct, String name, double price, 
            int stock, int min, int max, List<Part> associatedParts){
        oldProduct.setName(name);
        oldProduct.setPrice(price);
        oldProduct.setStock(stock);
        oldProduct.setMin(min);
        oldProduct.setMax(max);
        oldProduct.getAllAssociatedParts().clear();
        for(Part part : associatedParts){
            oldProduct.addAssociatedPart(part);
        }
    }
    public static void deletePart(Part selectedPart){
        try{
        allParts.remove(selectedPart);
        }catch (NullPointerException e){
            e.printStackTrace();
            return;
        }
        deletedPartCounter++;
    }
    public static void deleteProduct(Product selectedProduct){
        try{
        allProducts.remove(selectedProduct);
        }catch (NullPointerException e){
            e.printStackTrace();
            return;
        }
        deletedProductCounter++;
    }
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
