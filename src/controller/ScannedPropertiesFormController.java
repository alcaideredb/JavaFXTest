package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import exception.DataAccessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import model.PropertiesProcessor;
import util.FormUtils;

public class ScannedPropertiesFormController {
	private Set<String> includedProperties;
	private Set<String> excludedProperties;
	private Set<String> inputFilePaths;
	private ObservableList<String> includedData;
	private ObservableList<String> excludedData;

	@FXML
	AnchorPane rootPane;

	@FXML
	ListView<String> includedListView;
	@FXML
	ListView<String> excludedListView;

	@FXML
	TextField outputDirectoryField;

	@FXML
	TitledPane excludedTitle;
	@FXML
	TitledPane includedTitle;

	public Set<String> getIncludedProperties() {
		if (includedProperties == null) {
			includedProperties = new TreeSet<>();
		}

		return includedProperties;
	}

	public Set<String> getExcludedProperties() {
		if (excludedProperties == null) {
			excludedProperties = new TreeSet<>();
		}
		return excludedProperties;
	}

	public void setProperties(Set<String> included, Set<String> excluded) {
		this.includedProperties = new TreeSet<>(included);
		this.excludedProperties = new TreeSet<>(excluded);

	}

	public void setInputFilePaths(Collection<String> filePaths) {
		this.inputFilePaths = new TreeSet<>(filePaths);
	}

	@FXML
	public void initialize() {
		includedData = FXCollections.observableArrayList(getIncludedProperties());
		excludedData = FXCollections.observableArrayList(getExcludedProperties());
		includedListView.setItems(includedData);
		excludedListView.setItems(excludedData);
		includedListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		excludedListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		includedTitle.setText("Included Properties: " + getIncludedProperties().size());
		excludedTitle.setText("Excluded Properties: " + getExcludedProperties().size());
	}

	public void chooseOutputDirectory(ActionEvent ae) {
		DirectoryChooser chooser = new DirectoryChooser();
		File file = chooser.showDialog(rootPane.getScene().getWindow());
		if (file != null) {
			outputDirectoryField.setText(file.getPath());
		} else {
			FormUtils.errorMessage("No Output directory specified: ");
			return;
		}
	}

	@FXML
	public void generateTrimmedOutputFiles(ActionEvent ae) {
		if (!FormUtils.isNullOrEmpty(outputDirectoryField.getText())) {
			Alert confirmationDialog = new Alert(AlertType.CONFIRMATION, "Go ahead and generate trimmed files?",
					ButtonType.YES, ButtonType.CANCEL);
			confirmationDialog.showAndWait().filter(type -> ButtonType.YES == type).ifPresent(s -> trimProperties());

		} else {
			FormUtils.errorMessage("Output file was not specified!");
		}

	}

	public void trimProperties() {
		try {
			for (String filePath : inputFilePaths) {
				Properties prevProperties = PropertiesProcessor.getPropertiesFromFilePath(filePath);
				Properties trimmedProp = PropertiesProcessor.trimProperties(prevProperties, excludedProperties);
				String outputFilePath = generateOutputFilePath(outputDirectoryField.getText(), filePath);
				PropertiesProcessor.writePropertyTo(trimmedProp, outputFilePath);
			}
		} catch (IOException e) {
			FormUtils.errorMessage("Problem in processing one of the files.");
			throw new DataAccessException("Problem in processing one of the files. " + e.getMessage());
		}

		Alert alert = new Alert(AlertType.INFORMATION, "File/s successfully trimmed! "
				+ "The file/s have been generated in the following directory: " + outputDirectoryField.getText());
		alert.showAndWait();

	}

	private String generateOutputFilePath(String outputDir, String filePath) {
		Path outputDirPath = Paths.get(outputDir);
		Path outputFileName = Paths.get(filePath).getFileName();
		Path resolvedPath = outputDirPath.resolve(outputFileName);
		return resolvedPath.toString();
	}

}
