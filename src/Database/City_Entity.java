/*
 * City database functions
 */
package Database;

import Data_Model.City;
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
public class City_Entity {
    //Return city with given city ID
    public static City getCityFromID(int cityID){
    City foundCity = new City();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM city WHERE cityId=" + cityID);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            foundCity.setCityId(rs.getInt("cityId"));
            foundCity.setCity(rs.getString("city"));
            foundCity.setCountryId(rs.getInt("countryId"));
        }
    }
    catch(SQLException e){
        System.out.println("Error in city list");
    }
    return foundCity;
}
   
//Return list of all cities    
public static ObservableList<City> getAllCities(){
    ObservableList<City> cityList = FXCollections.observableArrayList();
    City city = new City();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM city");
        ResultSet rs = stmt.executeQuery();
       while(rs.next()){
           city.setCityId(rs.getInt("cityId"));
           city.setCity(rs.getString("city"));
           city.setCountryId(rs.getInt("countryId"));
           cityList.add(city);
       } 
    }
    catch(SQLException e){
        System.out.println("Error getting countries.");
    }
    return cityList;
}   

//Return list of all city names (for displaying in combo boxes)
public static ObservableList<String> getAllCityNames(){
    ObservableList<String> cityList = FXCollections.observableArrayList();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM city");
        ResultSet rs = stmt.executeQuery();
       while(rs.next()){
           String city = rs.getString("city");
           cityList.add(city);
       } 
    }
    catch(SQLException e){
        System.out.println("Error getting cities.");
    }
    return cityList;
}  

//Return city ID for given city name
public static int getCityId(String cityName){
    int cityId = -1;
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT cityId FROM city WHERE city = ?");
        stmt.setString(1, cityName);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            cityId = rs.getInt("cityId");
        }  
    }
    catch(SQLException e){
        System.out.println("Error getting city");
    }
    return cityId;
}

//Add city
public static int addCity(City city){
    int cityId = 0;
    try{
        //Get max city ID and add 1 to get next available ID
        PreparedStatement stmt = CONN.prepareStatement("SELECT MAX(cityId) FROM city");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            cityId = rs.getInt("MAX(cityId)");
        }
        cityId++;
        
        stmt = CONN.prepareStatement("INSERT INTO city VALUES(?,?,?,NOW(),?,NOW(),?)");
        stmt.setInt(1,cityId);
        stmt.setString(2,city.getCity());
        stmt.setInt(3, city.getCountryId());
        stmt.setString(4,currUser.getUserName());
        stmt.setString(5,currUser.getUserName());
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error finding city");
    }
    return cityId;
}

//Update city with given cityID
public static void updateCity(int cityId, City city){
    try{
        PreparedStatement stmt = CONN.prepareStatement("UPDATE city SET city=?, countryId=?,"+
                    "lastUpdate=NOW(), lastUpdateBy=? WHERE cityId=?");
        stmt.setString(1,city.getCity());
        stmt.setInt(2,city.getCountryId());
        stmt.setString(3,currUser.getUserName());
        stmt.setInt(4,cityId);
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error updating city");
    }
}

//Get country ID for given cityID
public static int getCountryId(int cityId){
    City city = new City();
    city = getCityFromID(cityId);
    return city.getCountryId();
    
}
}
