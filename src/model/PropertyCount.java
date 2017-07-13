package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import exception.DataAccessException;

public class PropertyCount {

	// Map of property and counter
	private Map<String, Long> propertiesCount;

	public PropertyCount() {
		propertiesCount = new HashMap<>();
	}

	public PropertyCount(Map<String, Long> propertiesCount) {
		setPropertiesCount(propertiesCount);
	}
	public Set<String> getKeySet() {
		return propertiesCount.keySet();
	}
	public Map<String, Long> getPropertiesCount() {
		return propertiesCount;
	}

	public void setPropertiesCount(Map<String, Long> propertiesCount) {
		this.propertiesCount = propertiesCount;
	}

	public void addProperty(String property) {
		long count = propertiesCount.getOrDefault(property, 0L);
		count++;
		propertiesCount.put(property, count);
	}

	public void add(PropertyCount other) {
		for (Map.Entry<String, Long> e : other.propertiesCount.entrySet()) {
			long thisCount = propertiesCount.getOrDefault(e.getKey(), 0L);
			long otherCount = e.getValue(); 
  			
			propertiesCount.put(e.getKey(), thisCount + otherCount);
		}
	}

	public Set<String> getExcludedProperties(Set<String> included) {
		Set<String> excluded = new HashSet<>(propertiesCount.keySet());
		excluded.removeAll(included);
		return excluded;
	}
	
	@Override
	public String toString() {
		return "PropertyCount [propertiesCount=" + propertiesCount + "]";
	}
	
	public static PropertyCount getPropertiesCountFromFile(String... files) {
		PropertyCount accumulator = new PropertyCount();
		for (String file : files) {
			PropertyCount propertyCount = getPropertiesCountFromFile(file);
			accumulator.add(propertyCount);
		}
		return accumulator;
	}
	
 	public static PropertyCount getPropertiesCountFromFile(String file) {
		Path path = Paths.get(file);
		try {
			List<String> lines = SafeFileReader.lines(path).filter(s -> !s.startsWith("#") && !s.isEmpty()).collect(Collectors.toList());
			Map<String, Long> propertyCountMap = lines.stream()
					.collect(Collectors.groupingBy(s -> getKeyFromProperty(s), Collectors.counting()));
			PropertyCount propertyCount = new PropertyCount(propertyCountMap);
			return propertyCount;
		} catch (IOException e) {
			throw new DataAccessException("There was an error processing the file!: " + path.toString());
		}
	}
 	
 	private static String getKeyFromProperty(String propString) {
 		String[] keys = propString.split("="); 
		return keys[0].trim();
  	}
}
