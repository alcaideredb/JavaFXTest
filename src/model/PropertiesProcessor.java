package model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

public class PropertiesProcessor {
	public static Properties trimProperties(Properties propertiesToTrim, Set<String> excludedProperties) {
		Properties trimmedProp = new Properties();
		trimmedProp.putAll(propertiesToTrim); 
		for (String excludedKey : excludedProperties) {
			String currProp = trimmedProp.getProperty(excludedKey);
 
			if (currProp != null) {
 				trimmedProp.remove(excludedKey);
			}
		}
 
		return trimmedProp;
	}

	public static Properties getPropertiesFromFilePath(String filePath) throws IOException {
		Path inputPath = Paths.get(filePath);
		Properties prop = new Properties();
		Reader reader = SafeFileReader.createLenientBufferedReader(inputPath);
		prop.load(reader);
		reader.close();
		return prop;
	}

	public static void writePropertyTo(Properties prop, String filePath) throws IOException {
		Path outputPath = Paths.get(filePath);
		Writer writer = Files.newBufferedWriter(outputPath);
		Properties sp = new SortedProperties();
		sp.putAll(prop);	
		sp.store(writer, "Auto generated property list from property trimmer app.");
		writer.close();
	}

}
