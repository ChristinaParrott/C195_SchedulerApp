/*
 * Address Class
 */
package Data_Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author c.parrott
 */
public class Address {
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
//Address constructors
public Address(){    
}    

public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, LocalDateTime createDate, String createdBy){
    this.addressId = addressId;
    this.address = address;
    this.address2 = address2;
    this.cityId = cityId;
    this.postalCode = postalCode;
    this.phone = phone;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = Timestamp.valueOf(createDate);
    this.lastUpdateBy = createdBy;    
}    

//Setters
public void setAddressId(int addressId){
    this.addressId = addressId;
}

public void setAddress(String address){
    this.address = address;
}

public void setAddress2(String address2){
    this.address2 = address2;
}

public void setCityId(int cityId){
    this.cityId = cityId;
}

public void setPostalCode(String postalCode){
    this.postalCode = postalCode;
}

public void setPhone(String phone){
    this.phone = phone;
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
public int getAddressId(){
    return this.addressId;
}

public String getAddress(){
    return this.address;
}

public String getAddress2(){
    return this.address2;
}

public int getCityId(){
    return this.cityId;
}

public String getPostalCode(){
    return this.postalCode;
}

public String getPhone(){
    return this.phone;
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
}
