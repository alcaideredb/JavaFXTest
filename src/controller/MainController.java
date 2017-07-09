package controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.PropertyCount;
import service.interfaces.PropertyScanner;
import service.impl.PropertyScannerImpl;
 
public class MainController {
    @FXML private TextField actiontarget1;
    @FXML private TextField actiontarget2;
    File file1;
    File file2;
    PropertyScanner pScan = new PropertyScannerImpl();
    
    @FXML protected void handleSubmitButtonAction1(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
        file1 = fileChooser.showOpenDialog(null);
        if (file1 != null) {
        	actiontarget1.setText(file1.getPath());
        }
     }
    @FXML protected void handleSubmitButtonAction2(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
        file2 = fileChooser.showOpenDialog(null);
        if (file2 != null) {
        	actiontarget2.setText(file2.getPath());
        }
     }
    
    @FXML protected void scanFile(ActionEvent event) {
    	if (file1 == null || file2 == null) {
    		Alert alert = new Alert(AlertType.ERROR, "No file chosen to be scan", ButtonType.CLOSE);
    		alert.showAndWait() ;
    	}
    	PropertyCount propCount1 = pScan.getPropertiesCountFromFile(file1.getPath());
    	PropertyCount propCount2 = pScan.getPropertiesCountFromFile(file2.getPath());
    	propCount1.add(propCount2);
    	
    	System.out.println(propCount1);
      }

}