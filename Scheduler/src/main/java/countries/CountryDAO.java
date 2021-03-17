/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package countries;

import data.DBStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * This class passes SQL queries to the database statement class to interact
 * with country data.
 * 
 * @author Eric Westfall
 * @version 1.0, 01/22/21
 */
public class CountryDAO {
    HashMap<Integer, Country> countryMap;
    
    /**
     * The constructor for the Country DAO controller.
     */
    public CountryDAO(){
        this.countryMap = new HashMap<>();
        this.getCountries();
    }
    
    /**
     * Adds a country to the HashMap.
     * 
     * @param country The Country to add to the HashMap.
     */
    private void addCountryToMap(Country country){
        this.countryMap.put(country.getCountryId(), country);
    }
    
    /**
     * Gets all the countries from the database using a SELECT query.
     */
    private void getCountries(){
        try{
            String query = "SELECT Country_ID, Country FROM countries;";
            System.out.println(query);
            DBStatement.executeQuery(query);
            ResultSet rs = DBStatement.getResultSet();
            rs.beforeFirst();
            while(rs.next()){
                this.addCountryToMap(new Country(rs.getInt(1), rs.getString(2)));
            }
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("SQLException in CountryDAO.getCountries().");
        }
    }
    
    /**
     * Returns all the country keys in the HashMap. Usually this is used for
     * iterating over the HashMap and returning all the countries.
     * 
     * @return The Set of country ids.
     */
    public Set getKeys(){
        return this.countryMap.keySet();
    }
    
    /**
     * Gets a country with the specified id from the HashMap.
     * 
     * @param countryId The id of the country.
     * @return          The Country object returned.
     */
    public Country getCountry(int countryId){
        return this.countryMap.get(countryId);
    }
}
