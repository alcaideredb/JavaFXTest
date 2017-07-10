package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class InternationalizedPropertyFileController {
	@FXML
	GridPane gridPane;
	private boolean wasRowAdded;
	private static int counter = 0;
	@FXML
	protected void addResourceBundleFileField(ActionEvent event) {
		counter++;
		Button button = new Button("Counter " + counter);
		button.setOnAction(this::chooseFile);
		
		TextField field = new TextField();
		addRow(button, field);
 	}

	@FXML
	protected void chooseFile(ActionEvent event) {
		Button button = (Button) event.getSource();
		int rowNum = GridPane.getRowIndex(button);
		System.out.println("TEST " + button.getText() + " rowNum: " + rowNum);
 	}
	
	
	
	
	private void addRow(Button button, TextField field) {
		if (wasRowAdded == true) {
			RowConstraints row = new RowConstraints(50);
			gridPane.getRowConstraints().add(row);
		}
		gridPane.add(button, 0, gridPane.getRowConstraints().size() - 1);
		gridPane.add(field, 1, gridPane.getRowConstraints().size() - 1);
		GridPane.setMargin(button, new Insets(0, 0, 0, 20));
		GridPane.setMargin(field, new Insets(0, 10, 0, 20));
		wasRowAdded = true;
	}

	private int getRowCount() {
		int maxIndex = gridPane.getChildren().stream().mapToInt(n -> {
			Integer row = GridPane.getRowIndex(n);
			Integer rowSpan = GridPane.getRowSpan(n);

			// default values are 0 / 1 respecively
			return (row == null ? 0 : row) + (rowSpan == null ? 0 : rowSpan - 1);
		}).max().orElse(-1);

		return maxIndex;
	}

	public void initialize() {
		wasRowAdded = false;
		RowConstraints firstRow = gridPane.getRowConstraints().get(0);
		firstRow.setPrefHeight(50);
		firstRow.setMaxHeight(50);
		firstRow.setVgrow(Priority.ALWAYS);
		firstRow.setMinHeight(50);

		System.out.println("INITIALIZED");
	}
}
