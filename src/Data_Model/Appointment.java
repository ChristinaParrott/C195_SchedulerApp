/*
 * Appointment class
 */
package Data_Model;

import static Database.Customer_Entity.getCustomerFromId;
import static Database.User_Entity.getUserFromId;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 *
 * @author c.parrott
 */
public class Appointment {
    private int appointmentId;
    private int customerId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String URL;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
//Appointment constructors
public Appointment(){  
} 

public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, 
        String contact, String type, String URL, ZonedDateTime start, ZonedDateTime end, LocalDateTime createDate, String createdBy){
    this.appointmentId = appointmentId;
    this.customerId = customerId;
    this.userId = userId;
    this.title = title;
    this.description = description;
    this.location = location;
    this.contact = contact;
    this.type = type;
    this.URL = URL;
    this.start = start;
    this.end = end;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = Timestamp.valueOf(createDate);
    this.lastUpdateBy = createdBy;
}

//Setters
public void setAppointmentId(int appointmentId){
    this.appointmentId = appointmentId;
}

public void setCustomerId(int customerId){
    this.customerId = customerId;
}

public void setUserId(int userId){
    this.userId = userId;
}

public void setTitle(String title){
    this.title = title;
}

public void setDescription(String description){
    this.description = description;
}

public void setLocation(String location){
    this.location = location;
}

public void setContact(String contact){
    this.contact = contact;
}

public void setType(String type){
    this.type = type;
}

public void setURL(String URL){
    this.URL = URL;
}

public void setStart(ZonedDateTime start){
    this.start = start;
}

public void setEnd(ZonedDateTime end){
    this.end = end;
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
public int getAppointmentId(){
    return this.appointmentId;
}

public int getCustomerId(){
    return this.customerId;
}

public int getUserId(){
    return this.userId;
}

public String getTitle(){
    return this.title;
}

public String getDescription(){
    return this.description;
}

public String getLocation(){
    return this.location;
}

public String getContact(){
    return this.contact;
}

public String getType(){
    return this.type;
}

public String getURL(){
    return this.URL;
}

public ZonedDateTime getStart(){
    return this.start;
}

public ZonedDateTime getEnd(){
    return this.end;
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
public StringProperty getTitleProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.title);
    return s;
}

public StringProperty getDescrProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.description);
    return s;    
}

public StringProperty getLocProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.location);
    return s;    
}

public StringProperty getContactProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.contact);
    return s;    
}

public StringProperty getTypeProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.type);
    return s;    
}

public StringProperty getURLProp(){
    StringProperty s = new SimpleStringProperty();
    s.setValue(this.URL);
    return s;    
}

public StringProperty getStartProp(){
    StringProperty s = new SimpleStringProperty();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    s.setValue(this.start.format(formatter));
    return s;    
}

public StringProperty getEndProp(){
    StringProperty s = new SimpleStringProperty();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    s.setValue(this.end.format(formatter));
    return s;    
}

public StringProperty getCustNameProp(){
    Customer cust = new Customer();
    cust = getCustomerFromId(this.customerId);
    return cust.getCNameProp();
}

public StringProperty getUserNameProp(){
    StringProperty s = new SimpleStringProperty();
    User user = new User();
    user = getUserFromId(this.userId);
    s.setValue(user.getUserName());
    return s;
}

//Advanced Error Checking - checks if appointment is within business hours (0800 to 1800 EST) 
public static boolean checkBusHrs(Appointment appt){
    int open = 8;
    int close = 18;
    //Get start and end times in UTC
    ZonedDateTime startEST = ZonedDateTime.ofInstant(appt.getStart().toInstant(), ZoneId.of("America/New_York"));
    ZonedDateTime endEST = ZonedDateTime.ofInstant(appt.getEnd().toInstant(), ZoneId.of("America/New_York"));
    //Get day, hour and minute portions of time
    int sHr = startEST.getHour();
    int eMin = endEST.getMinute();
    int eHr = endEST.getHour();
    int sDay = startEST.getDayOfMonth();
    int eDay = endEST.getDayOfMonth();
    //Check if hours are within business hours
    if (sHr < open){
        Alert errAlert = new Alert(ERROR, "Appointment outside of business hours. Business hours are between 0800 and 1800 EST."
                        + " Too early!");
        errAlert.setHeaderText("Outside of Business Hours Error.");
        errAlert.setTitle("Outside of Business Hours Error.");
        errAlert.showAndWait();
        return false;
    }
    if (eHr > close){
        Alert errAlert = new Alert(ERROR, "Appointment outside of business hours. Business hours are between 0800 and 1800 EST."
                        + " Too late!");
        errAlert.setHeaderText("Outside of Business Hours Error.");
        errAlert.setTitle("Outside of Business Hours Error.");
        errAlert.showAndWait();
        return false;
    }
    if (eMin > 0 && eHr == close){
        Alert errAlert = new Alert(ERROR, "Appointment outside of business hours. Business hours are between 0800 and 1800 EST."
                        + " Too late!");
        errAlert.setHeaderText("Outside of Business Hours Error.");
        errAlert.setTitle("Outside of Business Hours Error.");
        errAlert.showAndWait();
        return false;
    }
    //Appointment that lasts longer than one day will necessarily be outside of business hours
    if (eDay != sDay){
        Alert errAlert = new Alert(ERROR, "Appointment outside of business hours. Business hours are between 0800 and 1800 EST."
                        + " Appointment is longer than a day!");
        errAlert.setHeaderText("Outside of Business Hours Error.");
        errAlert.setTitle("Outside of Business Hours Error.");
        errAlert.showAndWait();
        return false;
    }
    else{
        return true;
    }
   
}

    
}
