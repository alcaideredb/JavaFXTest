package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exception.DataAccessException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PropertyCount;
import service.impl.AhoCorasickScanner;
import service.interfaces.PropertyScanner;
import util.FormUtils;

public class InternationalizedPropertyFileController {
	@FXML
	GridPane gridPane;
	@FXML
	TextField fileRoot;
	@FXML
	Button startScan;
	
	private boolean wasRowAdded;
	private PropertyScanner propertyScanner;
	
	@FXML
	protected void addResourceBundleFileField(ActionEvent event) {
		Button button = new Button("Choose File... ");
		Button removeButton = new Button("Remove ");
		button.setOnAction(this::chooseFile);
		removeButton.setOnAction((ActionEvent event2) -> {
			deleteRow(gridPane, GridPane.getRowIndex(button));
		});
		TextField field = new TextField();

		addRow(button, field, removeButton);
	}

	@FXML
	protected void chooseFile(ActionEvent event) {
		Button button = (Button) event.getSource();
		int rowNum = GridPane.getRowIndex(button);
		TextField field = (TextField) getNodeByRowColumnIndex(rowNum, 1, gridPane);
		
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PROPERTIES files (*.properties)",
				"*.properties");
		chooser.getExtensionFilters().add(extFilter);

		File file = chooser.showOpenDialog(gridPane.getScene().getWindow());
		if (file != null) {
			field.setText(file.getPath());
		}
 	}

	public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();

		for (Node node : childrens) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				result = node;
				break;
			}
		}

		return result;
	}

	private void addRow(Button button, TextField field, Button removeButton) {
		if (wasRowAdded == true) {
			RowConstraints row = new RowConstraints(60);
			gridPane.getRowConstraints().add(row);
		}
		
		int currIndex = gridPane.getRowConstraints().size() - 1;
		gridPane.add(button, 0, currIndex);
		gridPane.add(field, 1, currIndex);
		gridPane.add(removeButton, 2, currIndex);
	 
		GridPane.setMargin(button, new Insets(0, 0, 0, 20));
		GridPane.setMargin(field, new Insets(0, 10, 0, 20));
		GridPane.setMargin(removeButton, new Insets(0, 10, 0, 20));

		wasRowAdded = true;
	}

	static void deleteRow(GridPane grid, final int row) {
		Set<Node> deleteNodes = new HashSet<>();
		for (Node child : grid.getChildren()) {
			// get index from child
			Integer rowIndex = GridPane.getRowIndex(child);

			// handle null values for index=0
			int r = rowIndex == null ? 0 : rowIndex;

			if (r > row) {
				// decrement rows for rows after the deleted row
				GridPane.setRowIndex(child, r - 1);
			} else if (r == row) {
				// collect matching rows for deletion
				deleteNodes.add(child);
			}
		}
		grid.getChildren().removeAll(deleteNodes);
		grid.getRowConstraints().remove(row);
		System.out.println("ROW NUM " + row + "ROW SIZE: " + grid.getRowConstraints().size());
	}

	public void initialize() {
		wasRowAdded = false;
		propertyScanner = new AhoCorasickScanner();
		initializeRowConstraints();
	}	

	private void initializeRowConstraints() {
		RowConstraints firstRow = gridPane.getRowConstraints().get(0);
		firstRow.setPrefHeight(50);
		firstRow.setMaxHeight(50);
		firstRow.setVgrow(Priority.ALWAYS);
		firstRow.setMinHeight(50);
	}
	
	@FXML
	public void startScan(ActionEvent event) {
		startScan.setDisable(true);
		List<String> filePaths = getAllFilePathsFromFields();
		if (!FormUtils.isNullOrEmpty(fileRoot.getText()) && !filePaths.isEmpty()) {
			scanPropertiesFromFilePaths(filePaths);
		} else {
        	FormUtils.errorMessage("A file field was blank!");
		}
		startScan.setDisable(false);
	}
	
	
	private void scanPropertiesFromFilePaths(List<String> filePaths) {
		try {
			PropertyCount propertyCount = PropertyCount.getPropertiesCountFromFile(filePaths.toArray(new String[0]));
	 		Set<String>  included= propertyScanner.searchForUsages(propertyCount, fileRoot.getText());
	 		Set<String> excluded =  propertyCount.getExcludedProperties(included);
	 		loadScannedPropertiesForm(included, excluded, filePaths);
 		} catch (DataAccessException io) {
 			FormUtils.errorMessage(io.getMessage());
 		}	
	}
	
	private void loadScannedPropertiesForm(Set<String> included, Set<String> excluded, List<String> filePaths) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/form/ScannedPropertiesForm.fxml"));     

		Parent root;
		try {
			ScannedPropertiesFormController controller = new ScannedPropertiesFormController();
			controller.setProperties(included, excluded);
			controller.setInputFilePaths(filePaths);
			fxmlLoader.setController(controller);
			root = (Parent)fxmlLoader.load();
			Stage stage = new Stage();
            stage.setScene(new Scene(root));  
            stage.show();
		} catch (IOException e) {
	        	e.printStackTrace();
		}          
		
	}

	@FXML
	public void chooseFileRoot(ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		File file = chooser.showDialog(gridPane.getScene().getWindow());
		if (file != null) {
			fileRoot.setText(file.getPath());
		} else {
			FormUtils.errorMessage("No file root specified: ");
			return;
		}
	}
	
	public List<String> getAllFilePathsFromFields() {
		List<String> filePaths = new ArrayList<>();
		for (Node node : gridPane.getChildren()) {
 		    if (node instanceof TextField) {
 		    	if (node.getId() == "fileRoot") {
 		    		continue;
 		    	}
 		    	
 		        TextField field = ((TextField)node);
 		        if (!FormUtils.isNullOrEmpty(field.getText())) {
 		        	filePaths.add(field.getText());
 		        } else {
 		        	filePaths.clear();
 		        	break;
 		        }
		    }
		}
		return filePaths;

	}
}
