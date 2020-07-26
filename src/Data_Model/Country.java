/*
 * Country class
 */
package Data_Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author c.parrott
 */
public class Country {
    private int countryId;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
//constructors
public Country(){
}    

public Country(int countryId, String country, LocalDateTime createDate, String createdBy) {
    this.countryId = countryId;
    this.country = country;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = Timestamp.valueOf(createDate);
    this.lastUpdateBy = createdBy;
} 
//Setters
public void setCountryId(int countryId){
    this.countryId = countryId;
}

public void setCountry(String country){
    this.country = country;
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
public int getCountryId(){
    return this.countryId;
}

public String getCountry(){
    return this.country;
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
