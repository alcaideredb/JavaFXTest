package service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.Test;

import model.SortedProperties;

public class LoadPropertiesTest {

	public Properties getPropertyFromFilePath(String filePath) throws IOException {
		Path inputPath = Paths.get("C:\\JDeveloper\\local\\siapp\\common-target.properties");
		Properties prop = new Properties();
		Reader reader = Files.newBufferedReader(inputPath);
		prop.load(reader);
		return prop;
	}

	@Test
	public void test() throws IOException {
		String filePath = "C:\\JDeveloper\\local\\siapp\\common-target.properties";
		String outputFilePath = "C:\\JDeveloper\\testMe.properties";
		Properties prop = getPropertyFromFilePath(filePath);
		writePropertyTo(prop, outputFilePath);
	}

	@Test
	public void testFileConcat() {
		String outputFilePath = "C:\\Desktop\\t";
		String inputFilePath = "C:\\JDeveloper\\testMe.properties";
		generateOutputFilePath(outputFilePath, inputFilePath);
	}

	public void writePropertyTo(Properties prop, String filePath) throws IOException {
		Properties p = new Properties();
		p.putAll(prop);
		Path outputPath = Paths.get(filePath);
		Writer writer = Files.newBufferedWriter(outputPath);
		Properties sp = new SortedProperties();
		sp.putAll(p);

		sp.store(writer, "Auto generated property list from property trimmer app.");
	}

	private String generateOutputFilePath(String outputDir, String filePath) {
		Path outputDirPath = Paths.get(outputDir);
		Path outputFileName = Paths.get(filePath).getFileName();
		Path resolvedPath = outputDirPath.resolve(outputFileName);
		System.out.println(resolvedPath);
		return resolvedPath.toString();
	}

	@Test
	public void testDiffCharsetFile() {
		String filePath = "C:\\JDeveloper\\local\\hrpayrollapp\\HRPayrollService\\src\\NotificationUtil.properties";
		// PropertyCount propertyCount =
		// PropertyCount.getPropertiesCountFromFile(filePath);
		System.out.println("SAD");
		// System.out.println(propertyCount);
	}

}
