/*
 * Main
 */
package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.GUI.Log_In_Controller;


/**
 *
 * @author c.parrott
 */
public class Scheduler extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        //Load login screen
        FXMLLoader loader = new FXMLLoader(Log_In_Controller.class.getResource("Log_In.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Schedule Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
