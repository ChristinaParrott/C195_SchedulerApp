/*
 * Customer class
 */
package Data_Model;

import static Database.Address_Entity.*;
import static Database.City_Entity.*;
import static Database.Country_Entity.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author c.parrott
 */
public class Customer {
    private int customerId;
    private String customerName;
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String city;
    private int countryId;
    private String country;
    private String postalCode;
    private String phone;           
    private Boolean active;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    
//Customer constructors
public Customer(){
}

public Customer(int cId, String cName, int addressId, String address, String address2, int cityId, int countryId, String country,
    String postalCode, String phone, boolean act, LocalDateTime createDate, String createdBy){
    this.customerId = cId;
    this.customerName = cName;
    this.addressId = addressId;
    this.address = address;
    this.address2 = address2;
    this.cityId = cityId;
    this.countryId = countryId;
    this.postalCode = postalCode;
    this.phone = phone;
    this.active = act;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = Timestamp.valueOf(createDate);
    this.lastUpdateBy = createdBy;
}

//Setters
public void setCustomerId(int customerId){
    this.customerId = customerId;
}

public void setCustomerName(String customerName){
    this.customerName = customerName;
}

public void setAddressId(int addressId){
    this.addressId = addressId;
}

public void setAddressInfo(int addressId){
    Address custAddr = new Address();
    City custCity = new City();
    Country custCountry = new Country();
    custAddr = getAddressFromID(addressId);
    this.address = custAddr.getAddress();
    this.address2 = custAddr.getAddress2();
    this.cityId = custAddr.getCityId();
    this.postalCode = custAddr.getPostalCode();
    this.phone = custAddr.getPhone();
    custCity = getCityFromID(this.cityId);
    this.city = custCity.getCity();
    this.countryId = custCity.getCountryId();
    custCountry = getCountryFromID(this.countryId);
    this.country = custCountry.getCountry();
}

public void setActive(boolean active){
    this.active = active;
}

public void setCreateDate(LocalDateTime createDate){
    this.createDate = createDate;
}

public void setCreatedBy(String createdBy){
    this.createdBy = createdBy;
}

public void setLastUpdate(Timestamp lastUpdate){
    this.lastUpdate = lastUpdate;
}

public void setLastUpdateBy(String lastUpdateBy){
    this.lastUpdateBy = lastUpdateBy;
}

//Getters
public int getCustomerId(){
    return this.customerId;
}

public String getCustomerName(){
    return this.customerName;
}

public int getAddressId(){
    return this.addressId;
}

public String getAddress(){
    return this.address;
}

public String getAddress2(){
    return this.address2;
}

public int getCityID(){
    return this.cityId;
}

public String getCity(){
    return this.city;
}

public int getCountryID(){
    return this.countryId;
}

public String getCountry(){
    return this.country;
}

public String getPostalCode(){
    return this.postalCode;
}

public String getPhone(){
    return this.phone;
}

public boolean getActive(){
    return this.active;
}

public LocalDateTime getCreateDate(){
    return this.createDate;
}

public String getCreatedBy(){
    return this.createdBy;
}

public Timestamp getLastUpdate(){
    return this.lastUpdate;
}

public String getLastUpdateBy(){
    return this.lastUpdateBy;
}

//Get Properties
public IntegerProperty getCIDProp(){
    IntegerProperty i = new SimpleIntegerProperty();
    i.setValue(this.customerId);
    return i;
    
}
public StringProperty getCNameProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.customerName);
    return s;
    
}
public BooleanProperty getCActiveProp(){
    BooleanProperty a = new SimpleBooleanProperty();
    a.setValue(this.active);
    return a;
}

public StringProperty getCAddrProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.address);
    return s;
}

public StringProperty getCAddr2Prop(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.address2);
    return s;
}

public StringProperty getCCityProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.city);
    return s;
}

public StringProperty getCCountryProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.country);
    return s;
}

public StringProperty getCPostalCodeProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.postalCode);
    return s;
}

public StringProperty getCPhoneProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.phone);
    return s;
}
//Exception handling for Customer
public static boolean checkCust(Customer cust, Address add, City city, Country coun) throws cException {
    if(cust.getCustomerName().isEmpty()){
        throw new cException("Error: customer name is required.");
    }
    if(add.getAddress().isEmpty()){
        throw new cException("Error: Customer address is required.");
    }
    if(add.getPostalCode().isEmpty()){
        throw new cException("Error: Postal code is required.");
    }
    if(add.getPhone().isEmpty()){
        throw new cException("Error: Phone number is required.");
    }
    if(city.getCity().isEmpty()){
        throw new cException("Error: City is required");
    }
    if(coun.getCountry().isEmpty()){
        throw new cException("Error: Country is required.");
    }
    //returns true if input is valid
    return true;
}



}    