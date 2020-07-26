/*
 * Appointment database methods
 */
package Database;

import Data_Model.Appointment;
import static Database.Customer_Entity.getCIdFromName;
import static Database.Connect.CONN;
import static Database.User_Entity.getUIdFromName;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static scheduler.GUI.Log_In_Controller.currUser;

/**
 *
 * @author c. parrott
 */
public class Appointment_Entity {
    
    public Appointment_Entity(){
    }
    //Get all appointments for the next week
    public static ObservableList<Appointment> getWeeklyAppt(){
       ObservableList<Appointment> apptList = FXCollections.observableArrayList();
       try{
           PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM appointment WHERE start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)");
           ResultSet rs = stmt.executeQuery();
           
           while(rs.next()){
            Appointment appt = new Appointment();            
            appt.setCustomerId(rs.getInt("customerId"));
            appt.setAppointmentId(rs.getInt("appointmentId"));
            appt.setUserId(rs.getInt("userId"));
            appt.setTitle(rs.getString("title"));
            appt.setDescription(rs.getString("description"));
            appt.setLocation(rs.getString("location"));
            appt.setContact(rs.getString("contact"));
            appt.setType(rs.getString("type"));
            appt.setURL(rs.getString("url"));
            
            //Get start Timestamp from database as LocalDateTime
            LocalDateTime sUTC = rs.getTimestamp("start").toLocalDateTime();
            //Convert to instant at UTC
            Instant iSUTC = sUTC.toInstant(ZoneOffset.UTC);
            //Convert to ZonedDateTime at system time zone for user display
            ZonedDateTime sZDT = ZonedDateTime.ofInstant(iSUTC, ZoneId.systemDefault());
            appt.setStart(sZDT);
            
            //Get end Timestamp from database as LocalDateTime
            LocalDateTime eUTC = rs.getTimestamp("end").toLocalDateTime();
            //Convert to instant at UTC
            Instant iEUTC = eUTC.toInstant(ZoneOffset.UTC);
            //Convert to ZonedDateTime at system time zone for user display
            ZonedDateTime eZDT = ZonedDateTime.ofInstant(iEUTC, ZoneId.systemDefault());
            appt.setEnd(eZDT);
            
            apptList.add(appt);
           }
       }
       catch(SQLException e){
           System.out.println("Error in getting all appointments.");
       }
       return apptList;
    }
    
    //Get all appointments for the next month
    public static ObservableList<Appointment> getMonthlyAppt(){
       ObservableList<Appointment> apptList = FXCollections.observableArrayList();
       try{
           PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM appointment WHERE start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH)");
           ResultSet rs = stmt.executeQuery();
           
           while(rs.next()){
            Appointment appt = new Appointment();            
            appt.setCustomerId(rs.getInt("customerId"));
            appt.setAppointmentId(rs.getInt("appointmentId"));
            appt.setUserId(rs.getInt("userId"));
            appt.setTitle(rs.getString("title"));
            appt.setDescription(rs.getString("description"));
            appt.setLocation(rs.getString("location"));
            appt.setContact(rs.getString("contact"));
            appt.setType(rs.getString("type"));
            appt.setURL(rs.getString("url"));
            
            //Convert from database Timestamp to ZonedDateTime at user locale
            LocalDateTime sUTC = rs.getTimestamp("start").toLocalDateTime();
            Instant iSUTC = sUTC.toInstant(ZoneOffset.UTC);
            ZonedDateTime sZDT = ZonedDateTime.ofInstant(iSUTC, ZoneId.systemDefault());
            appt.setStart(sZDT);
            
            LocalDateTime eUTC = rs.getTimestamp("end").toLocalDateTime();
            Instant iEUTC = eUTC.toInstant(ZoneOffset.UTC);
            ZonedDateTime eZDT = ZonedDateTime.ofInstant(iEUTC, ZoneId.systemDefault());
            appt.setEnd(eZDT);
            
            apptList.add(appt);
           }
       }
       catch(SQLException e){
           System.out.println("Error in getting all appointments.");
       }
       return apptList;
    }    
    
//Get all appointments for the next 15 minutes for current user    
public static List<Appointment> appointSoon(int userID){
    List<Appointment> apptSoonList = new ArrayList<>();
    
    //Get current ZonedDateTime at user locale
    ZonedDateTime ZDT = ZonedDateTime.now(ZoneId.systemDefault());
    //Convert to UTC
    ZonedDateTime UTC = ZDT.withZoneSameInstant(ZoneId.of("UTC"));
    //Convert to LocalDateTime at UTC
    LocalDateTime LDT = UTC.toLocalDateTime();
    //Create Timestamp for interacting with database
    Timestamp nowStamp = Timestamp.valueOf(LDT);
    
    try{
       PreparedStatement stmt = CONN.prepareStatement("SELECT * FROM appointment WHERE start BETWEEN ? AND DATE_ADD(NOW(), INTERVAL 15 MINUTE)"
               + " AND userId = ?");
       stmt.setTimestamp(1, nowStamp);
       stmt.setInt(2,userID);
       ResultSet rs = stmt.executeQuery();
       
       while(rs.next()){
            Appointment apptSoon = new Appointment();            
            apptSoon.setCustomerId(rs.getInt("customerId"));
            apptSoon.setAppointmentId(rs.getInt("appointmentId"));
            apptSoon.setUserId(rs.getInt("userId"));
            apptSoon.setTitle(rs.getString("title"));
                       
            //Convert database timestamp to ZonedDateTime at user locale for display
            LocalDateTime sUTC = rs.getTimestamp("start").toLocalDateTime();
            Instant iSUTC = sUTC.toInstant(ZoneOffset.UTC);
            ZonedDateTime sZDT = ZonedDateTime.ofInstant(iSUTC, ZoneId.systemDefault());
            apptSoon.setStart(sZDT);
                       
            apptSoonList.add(apptSoon);
       }
    }
    catch(SQLException e){
        System.out.println("Error in finding appointment.");
    }
    return apptSoonList;
}

//Add appointment to database
public static int addAppt(Appointment appt){
        int apptId = 0;
        try{
            //Get highest # appointmentId and add 1 to get next available appointment Id
            PreparedStatement stmt = CONN.prepareStatement("SELECT MAX(appointmentId) FROM appointment");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                apptId = rs.getInt("MAX(appointmentId)");
            }
            apptId++;

            stmt = CONN.prepareStatement("INSERT INTO appointment VALUES(?,?,?,?,?,?,?,?,?,?,?,NOW(),?,NOW(),?)");
            stmt.setInt(1,apptId);
            stmt.setInt(2,appt.getCustomerId());
            stmt.setInt(3, appt.getUserId());
            stmt.setString(4,appt.getTitle());
            stmt.setString(5,appt.getDescription());
            stmt.setString(6,appt.getLocation());
            stmt.setString(7,appt.getContact());
            stmt.setString(8,appt.getType());
            stmt.setString(9,appt.getURL());
            
            //Convert start and end to UTC, convert to LDT, and create timestamps for database
            ZonedDateTime sZDT = appt.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime eZDT = appt.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp sStamp = Timestamp.valueOf(sZDT.toLocalDateTime());
            stmt.setTimestamp(10,sStamp);
            Timestamp eStamp = Timestamp.valueOf(eZDT.toLocalDateTime());
            stmt.setTimestamp(11, eStamp);
            
            stmt.setString(12, currUser.getUserName());
            stmt.setString(13, currUser.getUserName());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Error adding appointment.");
        }
        return apptId;
    }

//Update existing appointment Id with new value(s)
public static void updateAppt(int apptId, Appointment appt){
    try{
        PreparedStatement stmt = CONN.prepareStatement("UPDATE appointment SET customerId=?, userId=?, title=?, "
                + "description=?, location=?, contact=?, type=?, url=?, start=?, end=?, lastUpdate=NOW(), lastUpdateBy=?"
                + "WHERE appointmentId=?");
        stmt.setInt(1,appt.getCustomerId());
        stmt.setInt(2, appt.getUserId());
        stmt.setString(3,appt.getTitle());
        stmt.setString(4,appt.getDescription());
        stmt.setString(5,appt.getLocation());
        stmt.setString(6,appt.getContact());
        stmt.setString(7,appt.getType());
        stmt.setString(8,appt.getURL());
        
        //Convert start and end times to UTC, then LDT to create timestamps for database
        ZonedDateTime sZDT = appt.getStart().withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime eZDT = appt.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp sStamp = Timestamp.valueOf(sZDT.toLocalDateTime());
        stmt.setTimestamp(9,sStamp);
        Timestamp eStamp = Timestamp.valueOf(eZDT.toLocalDateTime());
        stmt.setTimestamp(10, eStamp);
        
        stmt.setString(11, currUser.getUserName());
        stmt.setInt(12, apptId);
        stmt.executeUpdate();
    }
    catch(SQLException e){
        System.out.println("Error modifying appointment.");
    }
}

//Delete appointment from database
 public static boolean delAppt(Appointment delAppt){
        try{
            PreparedStatement stmt = CONN.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
            stmt.setInt(1, delAppt.getAppointmentId());
            //returns true when delete is successful
            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e){
            System.out.println("Error deleting appointment.");
            return false;
        }
    }
 
 //Gather appointment type by month data for reporting
 public static ObservableList<String> getTypeCountByMonth(String month){
     ObservableList<String> typeByMonth = FXCollections.observableArrayList();
     try{
         //parse string month to localdatetime to convert to Timestamp
        String firstStr = "2020-"+ monthDigit(month) + "-01";
         LocalDateTime firstDay = getFirstDay(firstStr);   
         PreparedStatement stmt = CONN.prepareStatement("SELECT type, COUNT(*) FROM appointment WHERE start "
                 + "BETWEEN ? AND LAST_DAY(?) AND end BETWEEN ? AND LAST_DAY(?) GROUP BY type");
        
         //Convert to Timestamp for interacting with database
        ZonedDateTime sZDT = firstDay.atZone(ZoneId.systemDefault());
        ZonedDateTime sUTC = sZDT.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp sStamp = Timestamp.valueOf(sUTC.toLocalDateTime());
         
        stmt.setTimestamp(1, sStamp);
        stmt.setString(2, firstStr);
        stmt.setTimestamp(3, sStamp);
        stmt.setString(4, firstStr);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            typeByMonth.add(rs.getString("type"));
            typeByMonth.add(rs.getString("COUNT(*)"));
        }    
     }
     catch(SQLException e){
         System.out.println("Error getting appointment count by month.");
     }
     return typeByMonth;
 }
 
 //Gather consultant schedule information for reporting
  public static ObservableList<Appointment> getConsultSchedule(String user){
     ObservableList<Appointment> schedule = FXCollections.observableArrayList();
     int uID = getUIdFromName(user);
     try{  
        PreparedStatement stmt = CONN.prepareStatement("SELECT title, start, end FROM appointment WHERE userId = ?");
        stmt.setInt(1, uID);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            Appointment appt = new Appointment();
            appt.setTitle(rs.getString("title"));
            
             //Get start Timestamp from database as LocalDateTime
            LocalDateTime sUTC = rs.getTimestamp("start").toLocalDateTime();
            //Convert to instant at UTC
            Instant iSUTC = sUTC.toInstant(ZoneOffset.UTC);
            //Convert to ZonedDateTime at system time zone for user display
            ZonedDateTime sZDT = ZonedDateTime.ofInstant(iSUTC, ZoneId.systemDefault());
            appt.setStart(sZDT);
            
            //Get end Timestamp from database as LocalDateTime
            LocalDateTime eUTC = rs.getTimestamp("end").toLocalDateTime();
            //Convert to instant at UTC
            Instant iEUTC = eUTC.toInstant(ZoneOffset.UTC);
            //Convert to ZonedDateTime at system time zone for user display
            ZonedDateTime eZDT = ZonedDateTime.ofInstant(iEUTC, ZoneId.systemDefault());
            appt.setEnd(eZDT);

            schedule.add(appt);
            
        }    
     }
     catch(SQLException e){
         System.out.println("Error getting schedule.");
     }
     return schedule;
 }
  
  //Gather all appointments for a customer for reporting
  public static ObservableList<Appointment> getApptForCust(String cust){
     ObservableList<Appointment> appts = FXCollections.observableArrayList();
     int cID = getCIdFromName(cust);
     try{  
        PreparedStatement stmt = CONN.prepareStatement("SELECT title, start, end FROM appointment WHERE customerId = ?");
        stmt.setInt(1, cID);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            Appointment appt = new Appointment();
            appt.setTitle(rs.getString("title"));
            
            //Get start Timestamp from database as LocalDateTime
            LocalDateTime sUTC = rs.getTimestamp("start").toLocalDateTime();
            //Convert to instant at UTC
            Instant iSUTC = sUTC.toInstant(ZoneOffset.UTC);
            //Convert to ZonedDateTime at system time zone for user display
            ZonedDateTime sZDT = ZonedDateTime.ofInstant(iSUTC, ZoneId.systemDefault());
            appt.setStart(sZDT);
            
            //Get end Timestamp from database as LocalDateTime
            LocalDateTime eUTC = rs.getTimestamp("end").toLocalDateTime();
            //Convert to instant at UTC
            Instant iEUTC = eUTC.toInstant(ZoneOffset.UTC);
            //Convert to ZonedDateTime at system time zone for user display
            ZonedDateTime eZDT = ZonedDateTime.ofInstant(iEUTC, ZoneId.systemDefault());
            appt.setEnd(eZDT);

            appts.add(appt);
        }    
     }
     catch(SQLException e){
         System.out.println("Error getting appointments for customer.");
     }
     return appts;
 }
  
    //Advanced Exception Control - Check for overlap with another appointment
  public static boolean checkOverlap(Appointment appt){
      //Convert user time zone to UTC zone to compare to database
      ZonedDateTime start = ZonedDateTime.ofInstant(appt.getStart().toInstant(),ZoneId.of("UTC"));
      ZonedDateTime end = ZonedDateTime.ofInstant(appt.getEnd().toInstant(),ZoneId.of("UTC"));
      //Create timestamps to insert into query
      Timestamp sStamp = Timestamp.valueOf(start.toLocalDateTime());
      Timestamp eStamp = Timestamp.valueOf(end.toLocalDateTime());

      try{
          String query = "SELECT appointmentId, start, end FROM appointment WHERE userId=? AND "
          + "(((start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?))"
          + "OR (start >= ? AND end <= ?) OR (start <= ? AND end >=?))";
          PreparedStatement stmt = CONN.prepareStatement(query);
          stmt.setInt(1, appt.getUserId());
          stmt.setTimestamp(2,sStamp);
          stmt.setTimestamp(3,eStamp);
          stmt.setTimestamp(4,sStamp);
          stmt.setTimestamp(5,eStamp);
          stmt.setTimestamp(6,sStamp);
          stmt.setTimestamp(7,eStamp);
          stmt.setTimestamp(8,sStamp);
          stmt.setTimestamp(9,eStamp);
          ResultSet rs = stmt.executeQuery();
          while(rs.next()){
              //Check if appointment is self- in case of updating current appointment
              if (rs.getInt("appointmentId") != appt.getAppointmentId()){
                  ZonedDateTime sZDT = ZonedDateTime.ofInstant(rs.getTimestamp("start").toInstant(),ZoneId.systemDefault());
                  ZonedDateTime eZDT = ZonedDateTime.ofInstant(rs.getTimestamp("end").toInstant(),ZoneId.systemDefault());
                 
                  Alert overlap = new Alert(ERROR, "Overlapping appointment starting at: " + sZDT.toString()
                   + " and ending at: " + eZDT.toString());
                  overlap.setTitle("Overlapping appointment");
                  overlap.setHeaderText("Overlapping appointment");
                  overlap.showAndWait();
                  return false;
              }
              return true;
          }
          return true;
      }
      catch(SQLException e){
          System.out.println("Error in getting overlapping appointments.");
      }
      return false;
  }

//Convert first day of month from string to LocalDateTime
public static LocalDateTime getFirstDay(String month){
    String dateTime = month + " 00:00";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm");
    LocalDateTime firstDT = LocalDateTime.parse(dateTime, formatter);
    return firstDT;
}

//Convert string month to two-digit month for reporting
public static String monthDigit(String month){
    String monthNum = "";
    switch(month){
        case "January": monthNum = "01";
            break;
        case "February": monthNum = "02";
            break;
        case "March": monthNum = "03";
            break;
        case "April": monthNum = "04";
            break;
        case "May": monthNum = "05";
            break;
        case "June": monthNum = "06";
            break;
        case "July": monthNum = "07";
            break;
        case "August": monthNum = "08";
            break;
        case "September": monthNum = "09";
            break;
        case "October": monthNum = "10";
            break;
        case "November": monthNum = "11";
            break;
        case "December": monthNum = "12";
            break;
    }
    return monthNum;
}

}
