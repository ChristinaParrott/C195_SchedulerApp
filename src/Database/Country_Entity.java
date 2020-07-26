/*
 * Country database functions
 */
package Database;

import Data_Model.Country;
import static Database.Connect.CONN;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static scheduler.GUI.Log_In_Controller.currUser;

/**
 *
 * @author c.parrott
 */
public class Country_Entity {
    //Returns country with given country ID
    public static Country getCountryFromID(int countryID){
    Country foundCountry = new Country();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM country WHERE countryId=" + countryID);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            foundCountry.setCountryId(rs.getInt("countryId"));
            foundCountry.setCountry(rs.getString("country"));
        }
    }
    catch(SQLException e){
        System.out.println("Error in country list");
    }
    return foundCountry;
}

//Returns list of all countries
public static ObservableList<Country> getAllCountries(){
    ObservableList<Country> countryList = FXCollections.observableArrayList();
    Country country = new Country();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM country");
        ResultSet rs = stmt.executeQuery();
       while(rs.next()){
           country.setCountryId(rs.getInt("countryId"));
           country.setCountry(rs.getString("country"));
           countryList.add(country);
       } 
    }
    catch(SQLException e){
        System.out.println("Error getting countries.");
    }
    return countryList;
}    

//Returns list of all country names (for combo box display)
public static ObservableList<String> getAllCountryNames(){
    ObservableList<String> countryList = FXCollections.observableArrayList();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT country.country FROM country");
        ResultSet rs = stmt.executeQuery();
       while(rs.next()){
           String country = rs.getString("country");
           countryList.add(country);
       } 
    }
    catch(SQLException e){
        System.out.println("Error getting countries.");
    }
    return countryList;
} 

//Returns country name for given country ID. Returns -1 when country is not found
public static int getCountryId(String countryName){
    int countryId = -1;
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT countryId FROM country WHERE country = ?");
        stmt.setString(1, countryName);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            countryId = rs.getInt("countryId");
        }  
    }
    catch(SQLException e){
        System.out.println("Error getting country");
    }
    return countryId;
}

//Add country to database
public static int addCountry(Country country){
    int countryId = 0;
    try{
        //Get max country ID and add 1 to get next available country ID
        PreparedStatement stmt = CONN.prepareStatement("SELECT MAX(countryId) FROM country");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            countryId = rs.getInt("MAX(countryId)");
        }
        countryId++;
        
        stmt = CONN.prepareStatement("INSERT INTO country VALUES(?,?,NOW(),?,NOW(),?)");
        stmt.setInt(1,countryId);
        stmt.setString(2,country.getCountry());
        stmt.setString(3,currUser.getUserName());
        stmt.setString(4,currUser.getUserName());
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error adding country");
    }
    return countryId;
}

//Update country with given ID
public static void updateCountry(int countryId, Country country){
    try{
        PreparedStatement stmt = CONN.prepareStatement("UPDATE country SET country=?,lastUpdate=NOW(),"+
                    "lastUpdateBy=? WHERE countryId=?");
        stmt.setString(1,country.getCountry());
        stmt.setString(2,currUser.getUserName());
        stmt.setInt(3,countryId);
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error updating country");
    }
}
}
