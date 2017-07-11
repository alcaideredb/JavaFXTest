package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
 
public class MainController {
	@FXML private AnchorPane fillPane;
	
	@FXML protected void loadIntlProj(ActionEvent event) {
 		try {
			AnchorPane newLoadedPane =  FXMLLoader.load(getClass().getResource("/form/InternationalizedPropertyFileForm.fxml"));
 			fillPane.getChildren().clear();
			fillPane.setLeftAnchor(newLoadedPane, 0.0);
			fillPane.setRightAnchor(newLoadedPane, 0.0);
			fillPane.setTopAnchor(newLoadedPane, 0.0);
			fillPane.setBottomAnchor(newLoadedPane, 0.0);
			fillPane.getChildren().add(newLoadedPane);
			
 		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
	}
}