/*
 * Log in controller
 */
package scheduler.GUI;

import Data_Model.Appointment;
import Data_Model.User;
import Data_Model.Log_File;
import static Database.Appointment_Entity.appointSoon;
import Database.User_Entity;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author c.parrott
 */
public class Log_In_Controller implements Initializable {
    @FXML
    private Label uNameLbl;
    
    @FXML 
    private Label passLbl;
    
    @FXML 
    private TextField uNameTxt;
    
    @FXML
    private PasswordField passTxt;
    
    @FXML
    private Label titleTxt;
    
    @FXML
    private Button logInBtn;
    
    @FXML 
    private Button clearBtn;
    
    @FXML
    private Button exitBtn;
    
    public static User currUser = new User();
    public static Locale uLocale = Locale.getDefault();
    public static ResourceBundle rb = ResourceBundle.getBundle("Resources/lang",uLocale);
    
    @FXML
    void logInBtnAction(ActionEvent logInBtnClick) throws IOException{

        currUser.setUserName(uNameTxt.getText());
        currUser.setPassword(passTxt.getText());
        //User ID of -1 will be used for failed login attempts
        currUser.setUserId(-1);
        
        ObservableList<User> userList = FXCollections.observableArrayList();
        userList.addAll(User_Entity.activeUsers());
        Log_File.createLog();
       
       //Lambda to iterate through list of users 
       userList.forEach((u)->{
       //Check user input against database user list
            if (currUser.getUserName().equals(u.getUserName()) && currUser.getPassword().equals(u.getPassword()))
                {
                    currUser.setUserId(u.getUserID());
                }
        });
        if(currUser.getUserID() == -1){
            //Failed login attempts
            Log_File.writeInvalid(currUser.getUserName(), currUser.getPassword());
            Alert clearAlert = new Alert(INFORMATION, this.rb.getString("InvalidLogIn"));
            clearAlert.setHeaderText(this.rb.getString("InvalidLogIn_Header"));
            clearAlert.setTitle(this.rb.getString("InvalidLogIn_Header"));
            clearAlert.showAndWait();
            
            uNameTxt.setText("");
            passTxt.setText("");
            currUser.setUserName("");
            currUser.setPassword("");
            currUser.setUserId(-1);
        }
        else{
        //Get appointments within the next 15 minutes
        ObservableList<Appointment> upcomingAppts = FXCollections.observableArrayList();
        upcomingAppts.addAll(appointSoon(currUser.getUserID()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm");
        
        //Lambda to iterate through list of appointments
        upcomingAppts.forEach((a)->{               
                String startStr = a.getStart().format(formatter);
                String alert = this.rb.getString("UpcomingAppt") + " " + startStr;
                Alert apptAlert = new Alert(INFORMATION, alert);
                apptAlert.setHeaderText(this.rb.getString("UpcomingAppt_Header"));
                apptAlert.setTitle(this.rb.getString("UpcomingAppt_Header"));
                apptAlert.showAndWait();
        });
            }
        
        //Write sign in to log file and load main screen
        Log_File.writeValid(currUser.getUserName());    
        Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
        Stage stage = (Stage)logInBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();      
        }
    
    //Set all fields to initial values if user clicks clear
    @FXML
    void clearBtnAction(ActionEvent clearBtnClick){
        Alert clearAlert = new Alert(CONFIRMATION, this.rb.getString("ClearAlert"));
        clearAlert.setHeaderText(this.rb.getString("ClearAlert_Header"));
        clearAlert.setTitle(this.rb.getString("ClearAlert_Header"));
        clearAlert.showAndWait();
        
        if (clearAlert.getResult() == ButtonType.OK){
            uNameTxt.setText("");
            passTxt.setText("");
            currUser.setUserName("");
            currUser.setPassword("");
            currUser.setUserId(-1);
        }
        else{
            clearAlert.close();
        }
    }
    
    //Close if exit button clicked
    @FXML
    void exitBtnAction(ActionEvent exitBtnClick){
        Alert exitAlert = new Alert(CONFIRMATION, this.rb.getString("ExitAlert"));
        exitAlert.setHeaderText(this.rb.getString("ExitAlert_Header"));
        exitAlert.setTitle(this.rb.getString("ExitAlert_Header"));
        exitAlert.showAndWait();
        if (exitAlert.getResult() == ButtonType.OK){    
            Stage stage = (Stage)exitBtn.getScene().getWindow();
            stage.close();
        }
        else{
            exitAlert.close();
        }
    }
    /**
     * Initializes the controller class.
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Log_In_Controller.uLocale = Locale.getDefault();
        Log_In_Controller.rb = ResourceBundle.getBundle("Resources/lang",Log_In_Controller.uLocale);
        this.titleTxt.setText(Log_In_Controller.rb.getString("Heading"));
        this.uNameLbl.setText(Log_In_Controller.rb.getString("Username"));
        this.passLbl.setText(Log_In_Controller.rb.getString("Password"));
        this.logInBtn.setText(Log_In_Controller.rb.getString("LogIn_btn"));
        this.clearBtn.setText(Log_In_Controller.rb.getString("Clear_btn"));
        this.exitBtn.setText(Log_In_Controller.rb.getString("Exit_btn"));
    }    
    
}
