/*
 * Add customer controller
 */
package scheduler.GUI;

import Data_Model.Address;
import Data_Model.City;
import Data_Model.Country;
import Data_Model.Customer;
import static Data_Model.Customer.checkCust;
import Data_Model.Log_File;
import Data_Model.cException;
import Database.Address_Entity;
import static Database.Address_Entity.*;
import Database.City_Entity;
import static Database.City_Entity.*;
import Database.Country_Entity;
import static Database.Country_Entity.*;
import Database.Customer_Entity;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static scheduler.GUI.Log_In_Controller.currUser;


/**
 * FXML Controller class
 *
 * @author c.parrott
 */
public class Add_Cust_Controller implements Initializable {
    @FXML
    private Button saveCustBtn;
    
    @FXML
    private Button clearCustBtn;
    
    @FXML
    private Button backCustBtn;
    
    @FXML
    private TextField cNameTxt;
    
    @FXML
    private TextField cAddrTxt;
    
    @FXML
    private TextField cAddr2Txt;
  
    @FXML
    private ComboBox cCityBox;
    
    @FXML
    private ComboBox cCountryBox;

    @FXML
    private TextField cPostTxt;
    
    @FXML
    private TextField cPhoneTxt;    
       
    @FXML
    private TextField cIDTxt;  
    
    @FXML
    private RadioButton aYBtn;
    
    @FXML
    private RadioButton aNBtn;
    
    
    private final Customer cust = new Customer();
    private final Address addr = new Address();
    private final City city = new City();
    private final Country country = new Country();
    
    //Save customer information when user clicks save button
    @FXML
    void saveCustBtnAction(ActionEvent saveCustBtnClick) throws IOException{
        int addressId;
        int cityId;
        int countryId;
        int customerId;
        
        //Check if city and country are selected
        if (cCityBox.getSelectionModel().getSelectedItem() == null || cCountryBox.getSelectionModel().getSelectedItem() == null){
            Alert selAlert = new Alert(ERROR, "Missing required field(s)! Country and city are required.");
            selAlert.setTitle("Missing Required Fields");
            selAlert.setHeaderText("Missing Required Fields");
            selAlert.showAndWait();
        }
        else{
            //Save user input strings    
            cust.setCustomerName(cNameTxt.getText());
            addr.setAddress(cAddrTxt.getText());
            addr.setAddress2(cAddr2Txt.getText());
            addr.setPostalCode(cPostTxt.getText());
            addr.setPhone(cPhoneTxt.getText());
            city.setCity(cCityBox.getSelectionModel().getSelectedItem().toString());
            country.setCountry(cCountryBox.getSelectionModel().getSelectedItem().toString());
            cust.setActive(aYBtn.isSelected());

            try{
                //Check for missing fields. Returns true when required fields are present
                if(checkCust(cust,addr,city,country)){

                    //Find country ID based on country name string
                    countryId = getCountryId(country.getCountry());

                    //getCountryId returns -1 when no country is found - create new country
                    if(countryId == -1){
                        countryId = Country_Entity.addCountry(country);
                    }

                    //Link country to city
                    country.setCountryId(countryId);
                    city.setCountryId(countryId);

                    //Find city ID based on city name string
                    cityId = getCityId(city.getCity());

                    //getCityId returns -1 when no city is found - add new city
                    if(cityId == -1 ){
                        cityId = City_Entity.addCity(city);
                    }

                    //Link city to address
                    city.setCityId(cityId);
                    addr.setCityId(cityId);

                    //Find address Id based on address 1 and 2 strings
                    addressId = getAddressId(addr.getAddress(),addr.getAddress2());

                    //getAddressId returns -1 when no address is found - add new address
                    if(addressId == -1){
                        addressId = Address_Entity.addAddress(addr);
                    }

                    //Link address and customer
                    addr.setAddressId(addressId);
                    cust.setAddressId(addressId);

                    customerId = Customer_Entity.addCustomer(cust);
                    Log_File.writeAddCust(currUser.getUserName(), customerId);

                    //Display new customer ID
                    cIDTxt.setText(Integer.toString(customerId));

                    //Return to main screen
                    Alert saveAlert = new Alert(INFORMATION, "Customer data saved");
                    saveAlert.setHeaderText("Saved");
                    saveAlert.setTitle("Saved");
                    saveAlert.showAndWait();        
                    Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
                    Stage stage = (Stage)saveCustBtn.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    }
            }
            //Display error message for missing fields
            catch(cException c){
                Alert emptyAlert = new Alert(ERROR, "Missing required field(s)! " + c.getMessage());
                emptyAlert.setTitle("Missing Required Fields");
                emptyAlert.setHeaderText("Missing Required Fields");
                emptyAlert.showAndWait();
    }
    }
    }
    
    //Set fields to initial values if user clicks clear
    @FXML
    void clearCustBtnAction(ActionEvent clearCustBtnClick) throws IOException{
        Alert clearAlert = new Alert(CONFIRMATION, "Are you sure you want to clear?");
        clearAlert.setHeaderText("Confirm clear");
        clearAlert.setTitle("Confirm clear");
        clearAlert.showAndWait();
        if (clearAlert.getResult() == ButtonType.OK){
            cNameTxt.setText("");
            cAddrTxt.setText("");
            cAddr2Txt.setText("");
            cPostTxt.setText("");
            cPhoneTxt.setText("");
            cCityBox.setValue("");
            cCountryBox.setValue("");
            
        }
    }
    
    //return to main screen
    @FXML
    void backCustBtnAction(ActionEvent backBtnClick) throws IOException{     
        Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
        Stage stage = (Stage)backCustBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cCountryBox.setItems(getAllCountryNames());
        cCityBox.setItems(getAllCityNames());
    }    


    
}
