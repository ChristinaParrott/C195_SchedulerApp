/*
 * User database methods
 */
package Database;

import Data_Model.User;
import static Database.Connect.CONN;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 *  @author c.parrott
 */
public class User_Entity {
    

       
public User_Entity(){ 
} 

//Method to get list of active users from databse    
public static ObservableList<User> activeUsers(){
    try{
        CONN = Connect.connect();
        ObservableList<User> activeUsers =  FXCollections.observableArrayList();
        PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM user WHERE active = 1");
        ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                User addUser = new User();
                addUser.setUserId(rs.getInt("userId"));
                addUser.setUserName(rs.getString("userName"));
                addUser.setPassword(rs.getString("password"));
                activeUsers.add(addUser);
            }
        System.out.println(activeUsers);
        return activeUsers;
        }
        catch(SQLException e){
            System.out.println("SQL error in get users.");
        }
        return null;
    }

//Get user from corresponding User ID
    public static User getUserFromId(int uId){
        User user = new User();
        try{
            PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM user WHERE userId = ?");
            stmt.setInt(1, uId);
            ResultSet rs =  stmt.executeQuery();
            while(rs.next()){
               user.setUserId(rs.getInt("userId"));
               user.setUserName(rs.getString("userName"));
               user.setActive(rs.getBoolean("active"));
            }
        }
        catch(SQLException e){
            System.out.println("Error deleting customer.");
        }
        return user;      
    }

//Get user ID from corresponding user name (to be used with combo box lists) 
    public static int getUIdFromName(String name){
        //Find indexes of ID section start and end
        int startIndex = name.indexOf("ID:") + 4;
        int endIndex = name.indexOf(")");
        String iDStr = name.substring(startIndex, endIndex);
        int uID = Integer.parseInt(iDStr);
        return uID; 
    }
    
  //Get all user names in a list of strings (to be used for combo box lists)
    public static ObservableList<String> getAllUserNames(){
    ObservableList<String> userList = FXCollections.observableArrayList();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT userId, userName FROM user");
        ResultSet rs = stmt.executeQuery();
       while(rs.next()){
           String addUser = rs.getString("userName") + " (ID: " + rs.getString("userId") + ")";
           userList.add(addUser);
       } 
    }
    catch(SQLException e){
        System.out.println("Error getting users.");
    }
    return userList;
}    
}
