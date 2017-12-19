package model;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
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
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath.toFile()));
		// Writer writer = Files.newBuffered(outputPath);
		Properties sp = new SortedProperties();
		// prop = escapePropertiesEncoding(prop);
		sp.putAll(prop);
		System.out.println("WA" + sp);
		sp.store(outputStream, "Auto generated property list from property trimmer app.");
		outputStream.close();
	}

}
