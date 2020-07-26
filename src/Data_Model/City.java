/*
 * City class
 */
package Data_Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author c.parrott
 */
public class City {
    private int cityId;
    private String city;
    private int countryId;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
//City constructors
public City(){
}

public City(int cityId, String city, int countryId, LocalDateTime createDate, String createdBy){
    this.cityId = cityId;
    this.city = city;
    this.countryId = countryId;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = Timestamp.valueOf(createDate);
    this.lastUpdateBy = createdBy;
}

//Setters
public void setCityId(int cityId){
    this.cityId = cityId;
}

public void setCity(String city){
    this.city = city;
}

public void setCountryId(int countryId){
    this.countryId = countryId;
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
public int getCityId(){
    return this.cityId;
}

public String getCity(){
    return this.city;
}

public int getCountryId(){
    return this.countryId;
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
