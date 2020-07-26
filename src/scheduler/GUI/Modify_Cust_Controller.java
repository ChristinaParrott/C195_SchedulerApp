/*
 * Modify customer controller
 */
package scheduler.GUI;

import Data_Model.Address;
import Data_Model.City;
import Data_Model.Country;
import Data_Model.Customer;
import Data_Model.Log_File;
import Database.Address_Entity;
import static Database.Address_Entity.getAddressId;
import Database.City_Entity;
import static Database.City_Entity.getAllCityNames;
import static Database.City_Entity.getCityId;
import Database.Country_Entity;
import static Database.Country_Entity.getAllCountryNames;
import static Database.Country_Entity.getCountryId;
import static Database.Customer_Entity.updateCustomer;
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
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static scheduler.GUI.Log_In_Controller.currUser;
import static scheduler.GUI.Main_Screen_Controller.selCust;

/**
 * FXML Controller class
 *
 * @author c.parrott
 */
public class Modify_Cust_Controller implements Initializable {
    @FXML
    private Button saveMCustBtn;
    
    @FXML
    private Button revMCustBtn;
    
    @FXML
    private Button backMCustBtn;
    
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
    private TextField cIdTxt;
    
    @FXML
    private RadioButton aYBtn;
    
    @FXML
    private RadioButton aNBtn;
    
    private final Customer cust = new Customer();
    private final Address addr = new Address();
    private final City city = new City();
    private final Country country = new Country();
    
    //Save modified customer data when user clicks save button
    @FXML
    void saveMCustAction(ActionEvent saveMCustBtnClick) throws IOException{
        int addrId;
        int cityId;
        int countryId;

        //Get country ID for user input string
        country.setCountry(cCountryBox.getSelectionModel().getSelectedItem().toString());
        countryId = getCountryId(country.getCountry());
        
        //getCountryId returns -1 when country string does not exist- so add new country
        if(countryId == -1){
            countryId = Country_Entity.addCountry(country);
        }
        
        //Establish link between country and city
        country.setCountryId(countryId);
        city.setCountryId(countryId);
        
        //Get city ID for user input string
        city.setCity(cCityBox.getSelectionModel().getSelectedItem().toString());         
        cityId = getCityId(city.getCity());
        
        //getCityId returns -1 when city string does not exist- so add new city
        if(cityId == -1 ){
            cityId = City_Entity.addCity(city);
        }
        
        //Establish link between city and address
        city.setCityId(cityId);
        addr.setCityId(cityId);
        
        //Set address data based on user input
        addr.setAddress(cAddrTxt.getText());
        addr.setAddress2(cAddr2Txt.getText());
        addr.setPostalCode(cPostTxt.getText());
        addr.setPhone(cPhoneTxt.getText());
        
        //Find address Id from user input strings
        addrId = getAddressId(addr.getAddress(),addr.getAddress2());
        
        //getAddressId returns -1 when address does not exist - so add new address
        if(addrId == -1){
            addrId = Address_Entity.addAddress(addr);
        }

        //Establish link between address and customer
        addr.setAddressId(addrId);
        cust.setAddressId(addrId);
        
        //Get remainder of customer information from user input
        cust.setCustomerId(selCust.getCustomerId());
        cust.setCustomerName(cNameTxt.getText());
        cust.setActive(aYBtn.isSelected());
        
        updateCustomer(cust.getCustomerId(),cust);
        
        Log_File.writeEditCust(currUser.getUserName(), cust.getCustomerId());
        
        Alert saveAlert = new Alert(INFORMATION, "Customer data saved");
        saveAlert.setHeaderText("Saved");
        saveAlert.setTitle("Saved");
        saveAlert.showAndWait();
        
        //Return to main screen
        Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
        Stage stage = (Stage)saveMCustBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    //Revert fields to initial data when user clicks revert button
    @FXML
    void revMCustAction(ActionEvent revMCustBtnClick) throws IOException{
        Alert revAlert = new Alert(CONFIRMATION, "Are you sure you want to revert changes?");
        revAlert.setHeaderText("Confirm revert");
        revAlert.setTitle("Confirm revert");
        revAlert.showAndWait();
        if(revAlert.getResult() == ButtonType.OK){
            cNameTxt.setText(selCust.getCustomerName());
            cAddrTxt.setText(selCust.getAddress());
            cAddr2Txt.setText(selCust.getAddress2());
            cPostTxt.setText(selCust.getPostalCode());
            cPhoneTxt.setText(selCust.getPhone());
            cIdTxt.setText(Integer.toString(selCust.getCustomerId()));
            aYBtn.setSelected(selCust.getActive());       
            cCountryBox.setItems(getAllCountryNames());
            cCountryBox.setValue(selCust.getCountry());
            cCityBox.setItems(getAllCityNames());
            cCityBox.setValue(selCust.getCity());
        }
    }
    
    //Go back to main screen
    @FXML
    void backMCustAction(ActionEvent backMCustBtnClick) throws IOException{     
        Parent root = FXMLLoader.load(Main_Screen_Controller.class.getResource("Main_Screen.fxml"));
        Stage stage = (Stage)backMCustBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cNameTxt.setText(selCust.getCustomerName());
        cAddrTxt.setText(selCust.getAddress());
        cAddr2Txt.setText(selCust.getAddress2());
        cPostTxt.setText(selCust.getPostalCode());
        cPhoneTxt.setText(selCust.getPhone());
        cIdTxt.setText(Integer.toString(selCust.getCustomerId()));
        aYBtn.setSelected(selCust.getActive());       
        cCountryBox.setItems(getAllCountryNames());
        cCountryBox.setValue(selCust.getCountry());
        cCityBox.setItems(getAllCityNames());
        cCityBox.setValue(selCust.getCity());
        }    
    
}
