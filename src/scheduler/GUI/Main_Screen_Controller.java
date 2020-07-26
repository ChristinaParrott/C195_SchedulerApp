/*
 * Main screen controller
 */
package scheduler.GUI;

import Data_Model.Appointment;
import Data_Model.Customer;
import Data_Model.Log_File;
import static Database.Appointment_Entity.*;
import static Database.Connect.CONN;
import static Database.Connect.disconnect;
import static Database.Customer_Entity.*;
import static Database.User_Entity.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static scheduler.GUI.Log_In_Controller.currUser;

/**
 * FXML Controller class
 *
 * @author c.parrott
 */
public class Main_Screen_Controller implements Initializable {
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab custTab;
    
    @FXML
    private TableView<Customer> custTable;
    
    @FXML
    private TableColumn<Customer,String> cNameCol;
    
    @FXML
    private TableColumn<Customer,String> cAddrCol;
    
    @FXML
    private TableColumn<Customer,String> cAddrCol2;
   
    @FXML
    private TableColumn<Customer,String> cCityCol;
    
    @FXML
    private TableColumn<Customer,String> cPostCol;
    
    @FXML
    private TableColumn<Customer,String> cCountryCol;
    
    @FXML
    private TableColumn<Customer,String> cPhoneCol;
    
    @FXML
    private Button addCustBtn;
    
    @FXML
    private Button updateCustBtn;
    
    @FXML
    private Button delCustBtn;
    
    @FXML
    private Tab calTab;
    
    @FXML
    private RadioButton weekRadBtn;
    
    @FXML
    private RadioButton monthRadBtn;
    
    @FXML 
    private ToggleGroup calBtnGrp;
    
    @FXML
    private TableView<Appointment> calTable;
    
    @FXML
    private TableColumn<Appointment,String> aTitleCol;
    
    @FXML
    private TableColumn<Appointment,String>  aDescCol;
    
    @FXML
    private TableColumn<Appointment,String>  aLocCol;
    
    @FXML
    private TableColumn<Appointment,String>  aContCol;    
    
    @FXML
    private TableColumn<Appointment,String>  aTypeCol;    
    
    @FXML
    private TableColumn<Appointment,String>  aURLCol;    
    
    @FXML
    private TableColumn<Appointment,String>  aStartCol;    
    
    @FXML
    private TableColumn<Appointment,String>  aEndCol; 
    
    @FXML
    private TableColumn<Appointment,String>  aCustCol;
    
    @FXML
    private TableColumn<Appointment,String>  aUserCol;
    
    @FXML
    private Button addApptBtn;
    
    @FXML
    private Button updateApptBtn;
    
    @FXML
    private Button delApptBtn;
    
    @FXML
    private Tab reportTab;
    
    @FXML
    private TextArea reportTxt;
    
    @FXML
    private Label rTypeLbl;
    
    @FXML
    private Label rDetailLbl;
    
    @FXML
    private ComboBox rTypeBox;
    
    @FXML
    private ComboBox rDetailBox;
    
    @FXML
    private Button rGenBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    public static Customer selCust;
    
    @FXML
    public static Appointment selAppt;
    
    public static String typeByMonth = "Appointment Type Count by Month";
    public static String schedule = "Monthly Schedule for Consultant";
    public static String apptByCustomer = "All Appointments for Customer";
    
    //Load add customer screen
    @FXML
    void addCustAction(ActionEvent addCustBtnClick) throws IOException{
        Parent root = FXMLLoader.load(Add_Cust_Controller.class.getResource("Add_Cust.fxml"));
        Stage stage = (Stage)addCustBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
   
    //Load modify customer screen
    @FXML
    void updateCustAction(ActionEvent addCustBtnClick) throws IOException{
        selCust = custTable.getSelectionModel().getSelectedItem();
        if(selCust != null){
            Parent root = FXMLLoader.load(Modify_Cust_Controller.class.getResource("Modify_Cust.fxml"));
            Stage stage = (Stage)updateCustBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        //Display error message if no customer selected
        else{
            Alert edAlert = new Alert(ERROR, "No customer selected. Select a customer and try again.");
            edAlert.setTitle("Edit Customer Failed");
            edAlert.setHeaderText("Edit Customer Failed");
            edAlert.showAndWait();
        }
    }
    
    //Delete selected customer
    @FXML
    void delCustAction(ActionEvent delCustBtnClick) throws IOException{
        selCust = custTable.getSelectionModel().getSelectedItem();
        if(selCust != null){           
            Alert delAlert = new Alert(CONFIRMATION, "Are you sure you want to delete this customer? This aciton will delete all appointments associated with this customer.");
            delAlert.setHeaderText("Confirm Delete");
            delAlert.setTitle("Confirm Delete");
            delAlert.showAndWait();
            int cId = selCust.getCustomerId();
            if (delAlert.getResult() == ButtonType.OK){
                //delCustomer returns true if successful delete
               if(delCustomer(selCust)){
                   Log_File.writeDelCust(currUser.getUserName(), cId);
                   Alert succAlert = new Alert(INFORMATION, "Customer successfully deleted.");
                   succAlert.setTitle("Deleted Sucessful.");
                   succAlert.setHeaderText("Delete Successful");
                   succAlert.showAndWait();
                   custTable.setItems(getCustomers()); 
               }
               //Display error message if delete customer fails
               else{
                Alert failAlert = new Alert(ERROR, "Delete customer failed. Try again or contact your system administrator.");
                failAlert.setTitle("Delete Customer Failed");
                failAlert.setHeaderText("Delete Customer Failed");
                failAlert.showAndWait();
               }
            }
        }
        //Disply error message if no customer selected
        else{
            Alert delAlert = new Alert(ERROR, "No customer selected. Select a customer and try again.");
            delAlert.setTitle("Delete Customer Failed");
            delAlert.setHeaderText("Delete Customer Failed");
            delAlert.showAndWait();
        }
    } 
      
    //Load the appropriate calendar view based on radio button selection
    @FXML
    public void calBtnAction(ActionEvent calBtnAction) throws IOException{
        if(calBtnGrp.getSelectedToggle() == weekRadBtn){
            calTable.setItems(getWeeklyAppt());           
        }
        if(calBtnGrp.getSelectedToggle() == monthRadBtn){
            calTable.setItems(getMonthlyAppt());            
        }
    }
        
    //Load add appointment screen
    @FXML
    void addApptAction(ActionEvent addApptBtnClick) throws IOException{
        Parent root = FXMLLoader.load(Add_Appt_Controller.class.getResource("Add_Appt.fxml"));
        Stage stage = (Stage)addApptBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    //Load update appointment screen
    @FXML
    void updateApptAction(ActionEvent updateApptBtnClick) throws IOException{
        selAppt = calTable.getSelectionModel().getSelectedItem();
        if(selAppt != null){
            Parent root = FXMLLoader.load(Modify_Appt_Controller.class.getResource("Modify_Appt.fxml"));
            Stage stage = (Stage)updateApptBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        //Display error message if no appointment selected
        else{
            Alert edAlert = new Alert(ERROR, "No appointment selected. Select an appointment and try again.");
            edAlert.setTitle("Edit Appointment Failed");
            edAlert.setHeaderText("Edit Appointment Failed");
            edAlert.showAndWait();
        }
    }
    
    //Delete selected appointment
    @FXML
    void delApptAction(ActionEvent delApptBtnClick) throws IOException{
        selAppt = calTable.getSelectionModel().getSelectedItem();     
        if(selAppt != null){           
            Alert delAlert = new Alert(CONFIRMATION, "Are you sure you want to delete this appointment?");
            delAlert.setHeaderText("Confirm Delete");
            delAlert.setTitle("Confirm Delete");
            delAlert.showAndWait();
            int aId = selAppt.getAppointmentId();
            if (delAlert.getResult() == ButtonType.OK){
               //DelAppt returns true if delete is successful
                if(delAppt(selAppt)){
                   Log_File.writeDelAppt(currUser.getUserName(), aId);
                   Alert succAlert = new Alert(INFORMATION, "Appointment successfully deleted.");
                   succAlert.setTitle("Deleted Sucessful.");
                   succAlert.setHeaderText("Delete Successful");
                   succAlert.showAndWait();
                   calTable.setItems(getWeeklyAppt()); 
               }
               //Display error message if delete fails 
               else{
                Alert failAlert = new Alert(ERROR, "Delete appointment failed. Try again or contact your system administrator.");
                failAlert.setTitle("Delete Appointment Failed");
                failAlert.setHeaderText("Delete Appointment Failed");
                failAlert.showAndWait();
               }
            }
        }
        //Display error message if no appointment selected
        else{
            Alert delAlert = new Alert(ERROR, "No customer selected. Select a customer and try again.");
            delAlert.setTitle("Delete Customer Failed");
            delAlert.setHeaderText("Delete Customer Failed");
            delAlert.showAndWait();
        }      
    }         
    
    //Populate report combo boxes based on user selection
    @FXML
    public void reportBoxAction(ActionEvent reportTypeBox) throws IOException{
        ObservableList<String> reportMonthList = FXCollections.observableArrayList();
        reportMonthList.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        String selReport = rTypeBox.getSelectionModel().getSelectedItem().toString();
        //Populate combo box with list of months
        if(selReport.equals(typeByMonth)){
            rDetailLbl.setText("Select Month:");
            rDetailBox.setItems(reportMonthList);
        }
        //Populate combo box with user names
        if(selReport.equals(schedule))
        {
            rDetailLbl.setText("Select Consultant:");
            rDetailBox.setItems(getAllUserNames());
        }
        //Populate combo box with customer names
        if(selReport.equals(apptByCustomer)){
            rDetailLbl.setText("Select Customer:");
            rDetailBox.setItems(getAllCustNames());
        }
    }
    
    //Generate report based on user selections
    @FXML
    void genReportAction(ActionEvent genReportBtnClick) throws IOException{
 
        String selReport = rTypeBox.getSelectionModel().getSelectedItem().toString();
        Object selDetail = rDetailBox.getSelectionModel().getSelectedItem();
        if(selReport == null){
            Alert selAlert = new Alert(ERROR, "No report selected. Select a report type and try again.");
            selAlert.setHeaderText("Report Failed.");
            selAlert.setTitle("Report Failed.");
            selAlert.showAndWait();
        }
        else {
            //Display # of appointments of each type in the selected month
            if(selReport.equals(typeByMonth)){
                if(selDetail != null){
                    reportTxt.clear();
                    reportTxt.appendText(selDetail.toString() + " Appointments by Type \n");
                    ObservableList<String> typeCount = getTypeCountByMonth(selDetail.toString());
                    for(int i=0; i<typeCount.size(); i=i+2){
                        reportTxt.appendText(typeCount.get(i) + "\t");
                        reportTxt.appendText(typeCount.get(i+1) + "\n");
                    }
                }
                //Display error message if no month is selected
                else if(selDetail == null){
                    Alert selAlert = new Alert(ERROR, "No month selected. Select a month and try again.");
                    selAlert.setHeaderText("Report Failed.");
                    selAlert.setTitle("Report Failed.");
                    selAlert.showAndWait();

                }
            }
            //Display selected consultant (user) schedule
            if(selReport.equals(schedule))
                    if(selDetail != null){
                    reportTxt.clear();
                    reportTxt.appendText("Appointments for Consultant: " + selDetail.toString() + "\n");
                    ObservableList<Appointment> appt = getConsultSchedule(selDetail.toString());
                    //Lambda to iterate through appointment in list to simply generate report
                    appt.forEach((a)-> {
                        reportTxt.appendText("Title: " + a.getTitle() + "\n");
                        reportTxt.appendText("Start: " + a.getStart().toString() + "\t");
                        reportTxt.appendText("End: " + a.getEnd().toString() + "\n");
                    });
                    }
                //Display error message if no user selected    
                else if(selDetail == null){
                    Alert selAlert = new Alert(ERROR, "No consultant selected. Select a consultant and try again.");
                    selAlert.setHeaderText("Report Failed.");
                    selAlert.setTitle("Report Failed.");
                    selAlert.showAndWait();

                }
            //Display all appointments for selected customer
            if(selReport.equals(apptByCustomer)){
                if(selDetail != null){
                reportTxt.clear();
                reportTxt.appendText("Appointments for Customer: " + selDetail.toString() + "\n");
                ObservableList<Appointment> appt = getApptForCust(selDetail.toString());
                //Lambda to iterate through each appointment in list to simply generate report   
                appt.forEach((a)-> {
                        reportTxt.appendText("title: " + a.getTitle() + "\n");
                        reportTxt.appendText("start: " + a.getStart().toString() + "\t");
                        reportTxt.appendText("end: " + a.getEnd().toString() + "\n");
                    });
                }
                //Display error message if no customer selected
                else if(selDetail == null){
                    Alert selAlert = new Alert(ERROR, "No month selected. Select a month and try again.");
                    selAlert.setHeaderText("Report Failed.");
                    selAlert.setTitle("Report Failed.");
                    selAlert.showAndWait();

                }
            }
        } 
    }
    
    //Exit application - closes database connection
    @FXML
    void exitBtnAction(ActionEvent exitBtnClick){
        Alert exitAlert = new Alert(CONFIRMATION, "Are you sure you want to exit?");
        exitAlert.setHeaderText("Confirm Exit");
        exitAlert.setTitle("Confirm Exit");
        exitAlert.showAndWait();
        
        if (exitAlert.getResult() == ButtonType.OK){
            try{
                disconnect(CONN);
            }
            catch(SQLException e){
                System.out.println("Error disconnecting from database.");
            }
            Stage stage = (Stage)exitBtn.getScene().getWindow();
            stage.close();
        }
        else{
            exitAlert.close();
        }
    }
    
    public Main_Screen_Controller(){
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    //Lambdas used to conveniently set cell value factories for table views
    //Initialize table view for default tab - customers
    cNameCol.setCellValueFactory(cellData -> cellData.getValue().getCNameProp());
    cAddrCol.setCellValueFactory(cellData -> cellData.getValue().getCAddrProp());
    cAddrCol2.setCellValueFactory(cellData -> cellData.getValue().getCAddr2Prop());
    cCityCol.setCellValueFactory(cellData -> cellData.getValue().getCCityProp());
    cCountryCol.setCellValueFactory(cellData -> cellData.getValue().getCCountryProp());
    cPostCol.setCellValueFactory(cellData -> cellData.getValue().getCPostalCodeProp());
    cPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getCPhoneProp());
    custTable.setItems(getCustomers());   
    
    //Initialize table view for appointments
    aTitleCol.setCellValueFactory(cellData -> cellData.getValue().getTitleProp());
    aDescCol.setCellValueFactory(cellData -> cellData.getValue().getDescrProp());
    aLocCol.setCellValueFactory(cellData -> cellData.getValue().getLocProp());
    aContCol.setCellValueFactory(cellData -> cellData.getValue().getContactProp());
    aTypeCol.setCellValueFactory(cellData -> cellData.getValue().getTypeProp());
    aURLCol.setCellValueFactory(cellData -> cellData.getValue().getURLProp());
    aStartCol.setCellValueFactory(cellData -> cellData.getValue().getStartProp());
    aEndCol.setCellValueFactory(cellData -> cellData.getValue().getEndProp());
    aCustCol.setCellValueFactory(cellData -> cellData.getValue().getCustNameProp());
    aUserCol.setCellValueFactory(cellData -> cellData.getValue().getUserNameProp());
    calTable.setItems(getWeeklyAppt());

    //Initialize combo boxes for report list
    ObservableList<String> reportList = FXCollections.observableArrayList();
    reportList.addAll(typeByMonth, schedule, apptByCustomer);
    rTypeBox.setItems(reportList);                       
    }  
    
}
