package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
}
