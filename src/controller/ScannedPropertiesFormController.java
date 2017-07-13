package controller;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import exception.DataAccessException;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import model.SortedProperties;
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

	public void initialize() {
		includedData = FXCollections.observableArrayList(getIncludedProperties());
		excludedData = FXCollections.observableArrayList(getExcludedProperties());

		includedListView.setItems(includedData);
		excludedListView.setItems(excludedData);
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
				Properties prevProperties = getPropertiesFromFilePath(filePath);
				Properties trimmedProp = trimProperties(prevProperties);
				String outputFilePath = generateOutputFilePath(outputDirectoryField.getText(), filePath);
				writePropertyTo(trimmedProp, outputFilePath);
			}
		} catch (IOException e) {
			FormUtils.errorMessage("Problem in processing one of the files.");
			throw new DataAccessException("Problem in processing one of the files.");
		}

		Alert alert = new Alert(AlertType.INFORMATION, "File/s successfully trimmed! "
				+ "The file/s have been generated in the following directory: " + outputDirectoryField.getText());
		alert.showAndWait();
		
	}

	private String generateOutputFilePath(String outputDir, String filePath) {
		Path outputDirPath = Paths.get(outputDir);
		Path outputFileName = Paths.get(filePath).getFileName();
		Path resolvedPath = outputDirPath.resolve(outputFileName);
		System.out.println(resolvedPath);
		return resolvedPath.toString();
	}

	public Properties trimProperties(Properties propertiesToTrim) {
		Properties trimmedProp = new Properties();
		trimmedProp.putAll(propertiesToTrim);
		System.out.println("props totrim " + propertiesToTrim);
		System.out.println("trimmed " + trimmedProp);
		for (String excludedKey : excludedProperties) {
			String currProp = propertiesToTrim.getProperty(excludedKey);
			if (currProp != null) {
				System.out.println("curprop " + currProp);
				trimmedProp.remove(excludedKey);
			}
		}
		return trimmedProp;
	}

	public Properties getPropertiesFromFilePath(String filePath) throws IOException {
		Path inputPath = Paths.get(filePath);
		Properties prop = new Properties();
		Reader reader = Files.newBufferedReader(inputPath);
		prop.load(reader);
		return prop;
	}

	public void writePropertyTo(Properties prop, String filePath) throws IOException {
		Path outputPath = Paths.get(filePath);
		Writer writer = Files.newBufferedWriter(outputPath);
		Properties sp = new SortedProperties();
		sp.putAll(prop);	
		sp.store(writer, "Auto generated property list from property trimmer app.");
	}

}
