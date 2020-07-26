/*
 * Log File class
 */
package Data_Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author c. parrott
 */
public class Log_File {

//Create log file    
public static void createLog() {
    try{
        File logFile = new File("uLog.txt");
        if(logFile.createNewFile()){
            System.out.println("New log file created");
            }
        else{
            System.out.println("File already exists");  
        }
    }
    catch(IOException e){
            System.out.println("Error creating log file");
    }
}

//Write successful login
public static void writeValid(String uName){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Successful Login By: " + uName);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }  
}

//Write failed login
public static void writeInvalid(String uName, String pass){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Failed Login By: " + uName);
        out.write(" with password: " + pass);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }    
}

//Write Add Customer
public static void writeAddCust(String uName, int cID){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Customer added by " + uName);
        out.write(" with customer ID: " + cID);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }        
}

//Write Edit Customer
public static void writeEditCust(String uName, int cID){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Customer edited by " + uName);
        out.write(" with customer ID: " + cID);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }        
}

//Write Delete Customer
public static void writeDelCust(String uName, int cID){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Customer deleted by " + uName);
        out.write(" with customer ID: " + cID);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }        
}

//Write Add Appointment
public static void writeAddAppt(String uName, int aID){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Appointment added by " + uName);
        out.write(" with appointment ID: " + aID);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }        
}

//Write Edit Appointment
public static void writeEditAppt(String uName, int aID){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Appointment edited by " + uName);
        out.write(" with appointment ID: " + aID);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }        
}

//Write Delete Appointment
public static void writeDelAppt(String uName, int aID){
    try{   
        FileWriter out = new FileWriter("uLog.txt", true);
        out.write("Appointment deleted by " + uName);
        out.write(" with appointment ID: " + aID);
        out.write(" at: " + LocalDateTime.now() + "\n");
        out.close();      
    }
    catch(IOException e){
        System.out.println("Error writing to log");
    }        
}

}
