package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/form/MainApplication.fxml"));

		Scene scene = new Scene(root, 600, 600);

		stage.setTitle("WELCOMEEEE");
		stage.setScene(scene);
		stage.show();
	}
}
