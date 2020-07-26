/*
 * Add appointment controller
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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import static scheduler.GUI.Log_In_Controller.currUser;

/**
 * FXML Controller class
 *
 * @author c.parrott
 */
public class Add_Appt_Controller implements Initializable {
    
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
    
    //Add appointment when user clicks save button
    @FXML
    void saveApptAction(ActionEvent saveApptBtnClick) throws IOException{
        Appointment appt = new Appointment();
        //Check for required fields and display error if any are missing
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
        //Read in user input
        else{
            appt.setTitle(titleTxt.getText());
            appt.setDescription(descrTxt.getText());
            appt.setLocation(locTxt.getText());
            appt.setContact(conTxt.getText());
            appt.setType(typeTxt.getText());
            appt.setURL(URLTxt.getText());
            
            //read start and end dates in to strings
            String start = sDatePick.getValue().toString();
            String end = eDatePick.getValue().toString();
            
            //read start and end times in to strings
            String sHr = sHourBox.getSelectionModel().getSelectedItem().toString();
            String sMin = sMinBox.getSelectionModel().getSelectedItem().toString();
            String eHr = eHourBox.getSelectionModel().getSelectedItem().toString();
            String eMin = eMinBox.getSelectionModel().getSelectedItem().toString();
            
            //Concat date/time strings into local date time format and parse to local date time
            start = start + "T" + sHr + ":" + sMin + ":00";
            end = end +"T" + eHr + ":" + eMin + ":00";
            LocalDateTime startDT = LocalDateTime.parse(start);
            LocalDateTime endDT = LocalDateTime.parse(end);

            //Parse localdatetime to zoneddatetime
            ZonedDateTime startZDT = startDT.atZone(ZoneId.systemDefault());
            ZonedDateTime endZDT = endDT.atZone(ZoneId.systemDefault());
            appt.setStart(startZDT);
            appt.setEnd(endZDT);
            
            //Convert selected user name and customer name to corresponding IDs
            String uName = userBox.getSelectionModel().getSelectedItem().toString();
            appt.setUserId(getUIdFromName(uName));
            String cName = custBox.getSelectionModel().getSelectedItem().toString();
            appt.setCustomerId(getCIdFromName(cName));
            
            //Advanced Error Checking - check appointment times against business hours (0800-1800 EST)
            if(Appointment.checkBusHrs(appt)){
                //Advanced Error Checking - check for overlap
                if(Appointment_Entity.checkOverlap(appt)){
                    int apptID = addAppt(appt);
                    Log_File.writeAddAppt(currUser.getUserName(), apptID);

                    Alert saveAlert = new Alert(INFORMATION, "Appointment data saved");
                    saveAlert.setHeaderText("Saved");
                    saveAlert.setTitle("Saved");
                    saveAlert.showAndWait();

                    //Go back to main screen
                    Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
                    Stage stage = (Stage)saveApptBtn.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
            } 
            }
            else {   
            }
        }
    }
    
    //Clear all fields when user clicks clear button
    @FXML
    void clearApptAction(ActionEvent clearApptBtnClick) throws IOException{
        Alert clearAlert = new Alert(CONFIRMATION, "Are you sure you want to clear?");
        clearAlert.setHeaderText("Confirm clear");
        clearAlert.setTitle("Confirm clear");
        clearAlert.showAndWait();
        if(clearAlert.getResult() == ButtonType.OK){
            titleTxt.setText("");
            descrTxt.setText("");
            locTxt.setText("");
            conTxt.setText("");
            typeTxt.setText("");
            URLTxt.setText("");
            sDatePick.setValue(LocalDate.MIN);
            eDatePick.setValue(LocalDate.MIN);
            sHourBox.getSelectionModel().select("");
            eHourBox.getSelectionModel().select("");
            sMinBox.getSelectionModel().select("");
            eMinBox.getSelectionModel().select("");      
            custBox.getSelectionModel().select("");
            userBox.getSelectionModel().select("");
        }
    }
    
    //go back to main screen
    @FXML
    void backApptAction(ActionEvent backApptBtnClick) throws IOException{     
        Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
        Stage stage = (Stage)backApptBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    //Get list of hours of 24 hr scale
    public static ObservableList<String> hourList(){
        ObservableList<String> hourList = FXCollections.observableArrayList();
        for(int i=0; i<10; i++){
             hourList.add("0" + Integer.toString(i));
        }
        for(int i=10; i<24; i++){
            hourList.add(Integer.toString(i));
        }
        return hourList;
    }
    
    //Get list of minutes in an hour
    public static ObservableList<String> minList(){
        ObservableList<String> minList = FXCollections.observableArrayList();
        for(int i=0; i<10; i++){
             minList.add("0" + Integer.toString(i));
        }
        for(int i=10; i<60; i++){
            minList.add(Integer.toString(i));
        }
        return minList;
    }
    

    /**
     * Initializes the controller class.
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        custBox.setItems(getAllCustNames());
        userBox.setItems(getAllUserNames());
        sHourBox.setItems(hourList());
        eHourBox.setItems(hourList());
        sMinBox.setItems(minList());
        eMinBox.setItems(minList());
    }
    
}
