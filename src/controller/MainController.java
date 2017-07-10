package controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.PropertyCount;
import service.interfaces.PropertyScanner;
import service.impl.PropertyScannerImpl;
 
public class MainController {
	@FXML private AnchorPane fillPane;
	
	@FXML protected void loadIntlProj(ActionEvent event) {
		System.out.println(fillPane.getPadding());
		try {
			GridPane newLoadedPane =  FXMLLoader.load(getClass().getResource("/form/InternationalizedPropertyFileForm.fxml"));
			fillPane.getChildren().clear();
			fillPane.getChildren().add(newLoadedPane);
 		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
	}
}