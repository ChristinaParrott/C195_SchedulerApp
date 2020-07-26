/*
 * Address database methods
 */
package Database;

import Data_Model.Address;
import static Database.Connect.CONN;
import java.sql.*;
import static scheduler.GUI.Log_In_Controller.currUser;

/**
 *
 * @author c.parrott
 */
public class Address_Entity {
    
    //Returns address with given address ID
    public static Address getAddressFromID(int addressID){
        Address foundAddress = new Address();
        try{
            PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM address WHERE addressId=" + addressID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                foundAddress.setAddressId(rs.getInt("addressId"));
                foundAddress.setAddress(rs.getString("address"));
                foundAddress.setAddress2(rs.getString("address2"));
                foundAddress.setCityId(rs.getInt("cityId"));
                foundAddress.setPostalCode(rs.getString("postalCode"));
                foundAddress.setPhone(rs.getString("phone"));
            }
        }
        catch(SQLException e){
            System.out.println("Error in address list");
        }
        return foundAddress;
    }
    
    //Returns address ID with given address and address 2 Strings
    public static int getAddressId(String address, String address2){
    int addressId = -1;
    try{
        PreparedStatement stmt = CONN.prepareStatement("SELECT addressId FROM address WHERE address = ? AND address2 = ?");
        stmt.setString(1, address);
        stmt.setString(2, address2);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            addressId = rs.getInt("addressId");
        }  
    }
    catch(SQLException e){
        System.out.println("Error getting address");
    }
    return addressId;
}

//Adds address    
public static int addAddress(Address address){
    int addressId = 0;
    try{
        //Find max address ID and add 1 to get next available ID
        PreparedStatement stmt = CONN.prepareStatement("SELECT MAX(addressId) FROM address");
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            addressId = rs.getInt("MAX(addressId)");
            addressId++;
        }
        stmt = CONN.prepareStatement("INSERT INTO address VALUES(?,?,?,?,?,?,NOW(),?,NOW(),?)");
        stmt.setInt(1,addressId);
        stmt.setString(2,address.getAddress());
        stmt.setString(3,address.getAddress2());
        stmt.setInt(4,address.getCityId());
        stmt.setString(5,address.getPostalCode());
        stmt.setString(6,address.getPhone());
        stmt.setString(7,currUser.getUserName());
        stmt.setString(8,currUser.getUserName());
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error adding address");
    }
    return addressId;
}

//Update address with given address ID with values in address argument
public static void updateAddress(int addrId, Address address){
    try{
        PreparedStatement stmt = CONN.prepareStatement("UPDATE address SET address=?, address2=?,"+
                    "cityId=?, postalCode=?, phone=?, lastUpdate=NOW(), lastUpdateBy=?"+
                    "WHERE addressId=?");
        stmt.setString(1,address.getAddress());
        stmt.setString(2,address.getAddress2());
        stmt.setInt(3,address.getCityId());
        stmt.setString(4,address.getPostalCode());
        stmt.setString(5,address.getPhone());
        stmt.setString(6,currUser.getUserName());
        stmt.setInt(7,addrId);
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error updating address");
    }
}

//Get City ID for given address ID
public static int getCityId(int addressId){
    Address address = new Address();
    address = getAddressFromID(addressId);
    return address.getCityId();
    
}
    
}
