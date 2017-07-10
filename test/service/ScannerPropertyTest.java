package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.PropertyCount;
import service.impl.PropertyScannerImpl;
import service.interfaces.PropertyScanner;
 public class ScannerPropertyTest {
	PropertyScanner pScan = new PropertyScannerImpl();
	@Test
	public void testScanner() {
		String filePath1 = "C:\\Users\\Redan\\Desktop\\NISServiceBundle_ar.properties";
		String filePath2 = "C:\\Users\\Redan\\Desktop\\NISServiceBundle.properties";
		String filePath3 = "C:\\JDeveloper\\local\\siapp\\SIService\\src\\ValidationMessages.properties";
 		PropertyCount pCount1 = pScan.getPropertiesCountFromFile(filePath1);
 		PropertyCount pCount2 = pScan.getPropertiesCountFromFile(filePath2);
 		PropertyCount pCount3 = pScan.getPropertiesCountFromFile(filePath3);
 		pCount1.add(pCount2);
 		pCount1.add(pCount3);
 		List<PropertyCount> pCounts = new ArrayList<>();
 		pScan.searchForUsages(pCount1, "C:\\JDeveloper\\local\\siapp");
 	}
	
	
}
