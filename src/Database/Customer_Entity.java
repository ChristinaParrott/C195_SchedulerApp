/*
 * Customer database functions
 */
package Database;

import Data_Model.Customer;
import static Database.Connect.CONN;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static scheduler.GUI.Log_In_Controller.currUser;

/**
 *
 * @author c. parrott
 */
public class Customer_Entity {
    
    //Returns list of all customers
    public static ObservableList<Customer> getCustomers(){
       ObservableList<Customer> custList = FXCollections.observableArrayList();
       try{
           PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM customer");
           ResultSet rs = stmt.executeQuery();
           
           while(rs.next()){
               Customer addCust = new Customer();
               addCust.setCustomerId(rs.getInt("customerId"));
               addCust.setCustomerName(rs.getString("customerName"));
               addCust.setAddressId(rs.getInt("addressId"));
               addCust.setAddressInfo(addCust.getAddressId());
               addCust.setActive(rs.getBoolean("active"));
               custList.add(addCust);
           }
       }
       catch(SQLException e){
           System.out.println("Error in getting all customers.");
       }
       return custList;
    }
    
    //Returns customer with given customer ID
    public static Customer getCustomerFromId(int cId){
        Customer cust = new Customer();
        try{
            PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM customer WHERE customerId = ?");
            stmt.setInt(1, cId);
            ResultSet rs =  stmt.executeQuery();
            while(rs.next()){
               cust.setCustomerId(rs.getInt("customerId"));
               cust.setCustomerName(rs.getString("customerName"));
               cust.setAddressId(rs.getInt("addressId"));
               cust.setAddressInfo(cust.getAddressId());
               cust.setActive(rs.getBoolean("active"));
            }
        }
        catch(SQLException e){
            System.out.println("Error deleting customer.");
        }
        return cust;      
    }    
    
    //Returns customer name with given ID (To be used with names formatted for combo box only)
    public static int getCIdFromName(String name){
    //Find indexes of ID section start and end
    int startIndex = name.indexOf("ID:") + 4;
    int endIndex = name.indexOf(")");
    String iDStr = name.substring(startIndex, endIndex);
    int cID = Integer.parseInt(iDStr);
    return cID; 
    }
    
    //Returns list of all customer names for combo box display
    public static ObservableList<String> getAllCustNames(){
    ObservableList<String> custList = FXCollections.observableArrayList();
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT customerId, customerName FROM customer");
        ResultSet rs = stmt.executeQuery();
       while(rs.next()){
           //Append customer ID to avoid duplicate name errors
           String addCust = rs.getString("customerName") + " (ID: " + rs.getString("customerId") + ")";
           custList.add(addCust);
       } 
    }
    catch(SQLException e){
        System.out.println("Error getting customers.");
    }
    return custList;
} 
    
    //Delete customer with given customer ID
    public static boolean delCustomer(Customer delCust){
        try{
            PreparedStatement stmt = CONN.prepareStatement("DELETE FROM appointment WHERE customerId = ?");
            stmt.setInt(1, delCust.getCustomerId());
            stmt.executeUpdate();
            stmt = CONN.prepareStatement("DELETE FROM customer WHERE customerId = ?");
            stmt.setInt(1, delCust.getCustomerId());
            //Returns true if delete is successful
            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e){
            System.out.println("Error deleting customer.");
            return false;
        }
    }
    
    //Add customer to database. Returns new customer ID
    public static int addCustomer(Customer addCust){
        int custId = 0;
        try{
            //Get max customer ID and add one to get next available customer ID
            PreparedStatement stmt = CONN.prepareStatement("SELECT MAX(customerId) FROM customer");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                custId = rs.getInt("MAX(customerId)");
            }
            custId++;

            stmt = CONN.prepareStatement("INSERT INTO customer VALUES(?,?,?,?,NOW(),?,NOW(),?)");
            stmt.setInt(1,custId);
            stmt.setString(2,addCust.getCustomerName());
            stmt.setInt(3, addCust.getAddressId());
            stmt.setBoolean(4,addCust.getActive());
            stmt.setString(5, currUser.getUserName());
            stmt.setString(6, currUser.getUserName());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Error adding customer.");
        }
        return custId;
    }
    
    //Update customer at given customer ID with values in customer argument
    public static void updateCustomer(int custId, Customer customer){
    try{
        PreparedStatement stmt = CONN.prepareStatement("UPDATE customer SET customerName=?, addressId=?, active=?,"+
                    "lastUpdate=NOW(), lastUpdateBy=? WHERE customerId=?");
        stmt.setString(1,customer.getCustomerName());
        stmt.setInt(2, customer.getAddressId());
        stmt.setBoolean(3,customer.getActive());
        stmt.setString(4,currUser.getUserName());
        stmt.setInt(5,custId);
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error updating address");
    }
}
    
    
}
