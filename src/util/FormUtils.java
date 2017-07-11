package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FormUtils {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	public static void errorMessage(String errMsg) {
		Alert alert = new Alert(AlertType.ERROR, errMsg);
		alert.showAndWait();
	}
}
