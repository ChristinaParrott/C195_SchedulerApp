/*
 *User class 
 */
package Data_Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author c.parrott
 */
public class User {
    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    
    //Constructors for User class
    public User(int userId, String userName, String password, boolean active, LocalDateTime createDate, String createdBy){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = Timestamp.valueOf(createDate);
        this.lastUpdateBy = createdBy;         
    }
    
    public User(){
    }
    
    //Setters
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public void setPassword(String password){
        this.password = password;
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
    public int getUserID(){
        return this.userId;
    }
    
    public String getUserName(){
        return this.userName;
    }
    
    public String getPassword(){
        return this.password;
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
}
