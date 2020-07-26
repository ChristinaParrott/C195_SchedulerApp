/*
 * Modify appointment controller
 */
package scheduler.GUI;

import Data_Model.Appointment;
import Data_Model.Log_File;
import Database.Appointment_Entity;
import static Database.Appointment_Entity.*;
import static Database.Customer_Entity.*;
import static Database.User_Entity.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static scheduler.GUI.Add_Appt_Controller.hourList;
import static scheduler.GUI.Add_Appt_Controller.minList;
import static scheduler.GUI.Log_In_Controller.currUser;
import static scheduler.GUI.Main_Screen_Controller.selAppt;

/**
 * FXML Controller class
 *
 * @author c.parrott
 */
public class Modify_Appt_Controller implements Initializable {

    @FXML
    private Button saveMApptBtn;
    
    @FXML
    private Button revMApptBtn;
    
    @FXML
    private Button backMApptBtn;
    
    @FXML
    private Button saveApptBtn;
    
    @FXML
    private Button clearApptBtn;
    
    @FXML
    private Button backApptBtn;
    
    @FXML
    private ComboBox custBox;
    
    @FXML
    private ComboBox userBox;
    
    @FXML
    private TextField titleTxt;
    
    @FXML
    private TextField descrTxt;
    
    @FXML
    private TextField locTxt;
    
    @FXML
    private TextField conTxt;
    
    @FXML
    private TextField typeTxt;
    
    @FXML
    private TextField URLTxt;
    
    @FXML
    private DatePicker sDatePick;
    
    @FXML
    private ComboBox sHourBox;

    @FXML
    private ComboBox sMinBox;  
    
    @FXML
    private DatePicker eDatePick;  
    
    @FXML
    private ComboBox eHourBox;

    @FXML
    private ComboBox eMinBox;     
    
    //Save the modified appointment when user clicks save button
    @FXML
    void saveMApptAction(ActionEvent saveMApptBtnClick) throws IOException{
        Appointment appt = new Appointment();
        //Check if required fields are missing and display error message if any are missing
        if(userBox.getSelectionModel().getSelectedItem() == null || 
                custBox.getSelectionModel().getSelectedItem() == null || 
                titleTxt.getText().isEmpty() || 
                sDatePick.getValue() == null || 
                eDatePick.getValue() == null ||
                sMinBox.getSelectionModel().getSelectedItem() == null || 
                eMinBox.getSelectionModel().getSelectedItem() == null){
                        Alert emptyAlert = new Alert(ERROR, "Appointment title, user, customer, start and end date/time are all required fields");
                        emptyAlert.setTitle("Missing Required Fields");
                        emptyAlert.setHeaderText("Missing Required Fields");
                        emptyAlert.showAndWait();
        }
        else{
            
            //Load user input to appointment
            appt.setAppointmentId(selAppt.getAppointmentId());
            appt.setTitle(titleTxt.getText());
            appt.setDescription(descrTxt.getText());
            appt.setLocation(locTxt.getText());
            appt.setContact(conTxt.getText());
            appt.setType(typeTxt.getText());
            appt.setURL(URLTxt.getText());
            
            String start = sDatePick.getValue().toString();
            String end = eDatePick.getValue().toString();
            String sHr = sHourBox.getSelectionModel().getSelectedItem().toString();
            String sMin = sMinBox.getSelectionModel().getSelectedItem().toString();
            String eHr = eHourBox.getSelectionModel().getSelectedItem().toString();
            String eMin = eMinBox.getSelectionModel().getSelectedItem().toString();
            
            //Format user input strings to be parsed to LocalDateTime
            start = start + "T" + sHr + ":" + sMin + ":00";
            end = end +"T" + eHr + ":" + eMin + ":00";
            LocalDateTime startDT = LocalDateTime.parse(start);
            LocalDateTime endDT = LocalDateTime.parse(end);

            //Get ZonedDateTime at user's location    
            ZonedDateTime startZDT = startDT.atZone(ZoneId.systemDefault());
            ZonedDateTime endZDT = endDT.atZone(ZoneId.systemDefault());
            appt.setStart(startZDT);
            appt.setEnd(endZDT);
            
            //Get user ID from selection
            String uName = userBox.getSelectionModel().getSelectedItem().toString();
            appt.setUserId(getUIdFromName(uName));
            String cName = custBox.getSelectionModel().getSelectedItem().toString();
            appt.setCustomerId(getCIdFromName(cName));
            
            //Revision - check against business hours
            if(Appointment.checkBusHrs(appt)){
                //Revision - check for overlap
                if(Appointment_Entity.checkOverlap(appt)){
                    updateAppt(selAppt.getAppointmentId(), appt);  
                    Log_File.writeEditAppt(currUser.getUserName(), selAppt.getAppointmentId());

                    Alert saveAlert = new Alert(INFORMATION, "Appointment data saved");
                    saveAlert.setHeaderText("Saved");
                    saveAlert.setTitle("Saved");
                    saveAlert.showAndWait();

                    //Back to main screen
                    Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
                    Stage stage = (Stage)saveMApptBtn.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                }
                }
        }
    }
    
    //Revert all changes made - reinitializes the fields
    @FXML
    void revMApptAction(ActionEvent revMApptBtnClick) throws IOException{
        Alert revAlert = new Alert(CONFIRMATION, "Are you sure you want to revert changes?");
        revAlert.setHeaderText("Confirm revert");
        revAlert.setTitle("Confirm revert");
        revAlert.showAndWait();
        if (revAlert.getResult() == ButtonType.OK){
            titleTxt.setText(selAppt.getTitle());
            descrTxt.setText(selAppt.getDescription());
            locTxt.setText(selAppt.getLocation());
            conTxt.setText(selAppt.getContact());
            typeTxt.setText(selAppt.getType());
            URLTxt.setText(selAppt.getURL());
            int cId = selAppt.getCustomerId();
            int uId = selAppt.getUserId();
            custBox.getSelectionModel().select(getCustomerFromId(cId).getCustomerName());
            userBox.getSelectionModel().select(getUserFromId(uId).getUserName());

            String startStr = selAppt.getStart().toString();
            String endStr = selAppt.getEnd().toString();
            String startDateStr = startStr.substring(0,10);
            String endDateStr = endStr.substring(0,10);
            String startHrStr = startStr.substring(11,13);
            String endHrStr = endStr.substring(11,13);
            String startMinStr = startStr.substring(14,16);
            String endMinStr = endStr.substring(14,16);

            sHourBox.getSelectionModel().select(startHrStr);
            eHourBox.getSelectionModel().select(endHrStr);
            sMinBox.getSelectionModel().select(startMinStr);
            eMinBox.getSelectionModel().select(endMinStr);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            sDatePick.setValue(startDate);
            eDatePick.setValue(endDate);
        }
    }
    
    //Go back to main screen
    @FXML
    void backMApptAction(ActionEvent backMApptBtnClick) throws IOException{     
        Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
        Stage stage = (Stage)backMApptBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
            
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Populate choices for comboboxes
        custBox.setItems(getAllCustNames());
        userBox.setItems(getAllUserNames());
        sHourBox.setItems(hourList());
        eHourBox.setItems(hourList());
        sMinBox.setItems(minList());
        eMinBox.setItems(minList());
        
        //Populate textfields with sleected appointment data
        titleTxt.setText(selAppt.getTitle());
        descrTxt.setText(selAppt.getDescription());
        locTxt.setText(selAppt.getLocation());
        conTxt.setText(selAppt.getContact());
        typeTxt.setText(selAppt.getType());
        URLTxt.setText(selAppt.getURL());
        int cId = selAppt.getCustomerId();
        int uId = selAppt.getUserId();
        
        //Concat ID to the end of user/customer name. Used to differentiate between duplicate names
        custBox.getSelectionModel().select(getCustomerFromId(cId).getCustomerName() + " (ID: " + cId + ")");
        userBox.getSelectionModel().select(getUserFromId(uId).getUserName() + " (ID: " + uId + ")");
        
        //Get substrings for date, hour, and minute
        String startStr = selAppt.getStart().toString();
        String endStr = selAppt.getEnd().toString();
        String startDateStr = startStr.substring(0,10);
        String endDateStr = endStr.substring(0,10);
        String startHrStr = startStr.substring(11,13);
        String endHrStr = endStr.substring(11,13);
        String startMinStr = startStr.substring(14,16);
        String endMinStr = endStr.substring(14,16);
        
        //Populate comboboxes with hour/min substrings
        sHourBox.getSelectionModel().select(startHrStr);
        eHourBox.getSelectionModel().select(endHrStr);
        sMinBox.getSelectionModel().select(startMinStr);
        eMinBox.getSelectionModel().select(endMinStr);
     
        //Set Date Pickers with appointment data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        sDatePick.setValue(startDate);
        eDatePick.setValue(endDate);
      
    }    
    
}
