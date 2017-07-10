package service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.PropertyCount;
import service.interfaces.*;

public class PropertyScannerImpl implements PropertyScanner {
	@Override
	public PropertyCount getPropertiesCountFromFile(String file) {
		Path path = Paths.get(file);
		try {
			List<String> lines = Files.lines(path).filter(s -> !s.startsWith("#") && !s.isEmpty())
					.collect(Collectors.toList());
			Map<String, Long> propertyCountMap = lines.stream()
					.collect(Collectors.groupingBy(s -> s.toString().split("=")[0], Collectors.counting()));
			PropertyCount propertyCount = new PropertyCount(propertyCountMap);
			return propertyCount;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new PropertyCount();
	}

	@Override
	public void searchForUsages( PropertyCount pCount , String... string) {
		for (String searchPath : string) {
			Path path = Paths.get(searchPath);
			Set<String> includedProperties = new HashSet<>();
			try {
				List<String> filePaths = Files.walk(path).filter(f -> f.toString().endsWith(".java") || f.toString().endsWith(".jsf") || f.toString().endsWith(".jsff"))
						.map(Path::toString)
						.collect(Collectors.toList());
				for ( String  key  : pCount.getKeySet()) {
 					System.out.println("PCOUNTkey: " + key + " {"); 
 					boolean found = false;
 					found = filePaths.stream().anyMatch(fPath -> fileContainsText(key, fPath));
 					
//					for (String fileName : filePaths) {
//						if (fileContainsText(key, fileName)) {
//							includedProperties.add(key);
//							System.out.println("found in " + fileName);
//							found = true;
//						}  
// 	 				}
					if (found) {
						System.out.println("Key was found. Do not delete");
					} else {
						System.out.println("Key was not found. Safe to Delete");

					}
					
 					System.out.println("} PCOUNTkey: " + key + " : END"); 
				}
//				System.out.println(filePaths);
			} catch (IOException e) {
 				e.printStackTrace();
			}
		}
	}
	
	   private static boolean fileContainsText(String pattern, String fileName)
	             {
		   try {
			   String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
		       return text.contains(pattern);
		   } catch(IOException e) {
			   throw new RuntimeException(e);
		   }
	    }
}
