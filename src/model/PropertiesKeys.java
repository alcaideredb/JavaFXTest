package model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import exception.DataAccessException;

public class PropertiesKeys {

	// Map of property and counter
	private Set<String> propertiesKeys;

	public PropertiesKeys() {
		propertiesKeys = new HashSet<>();
	}

	public PropertiesKeys(Set<String> propertiesCount) {
		this.propertiesKeys = propertiesCount;
	}

	public Set<String> getPropertiesKeys() {
		return propertiesKeys;
	}
 

	public void addProperty(String property) {
		propertiesKeys.add(property);
	}

	public void add(PropertiesKeys other) {
		for (String e : other.propertiesKeys) {
			propertiesKeys.add(e);
		}
	}

	public Set<String> getExcludedProperties(Set<String> included) {
		Set<String> excluded = new HashSet<>(propertiesKeys);
		excluded.removeAll(included);
		return excluded;
	}

	@Override
	public String toString() {
		return "PropertyCount [propertiesCount=" + propertiesKeys + "]";
	}

	public static PropertiesKeys retrieveFromFile(String... files) {
		PropertiesKeys accumulator = new PropertiesKeys();
		for (String file : files) {
			PropertiesKeys propertyCount = retrieveFromFile(file);
			accumulator.add(propertyCount);
		}
		return accumulator;
	}

	public static PropertiesKeys retrieveFromFile(String file) {
		Path path = Paths.get(file);
		try {
			List<String> lines = SafeFileReader.lines(path).filter(s -> !s.startsWith("#") && !s.isEmpty())
					.collect(Collectors.toList());
			Set<String> propertyCountMap = lines.stream().map(x -> getKeyFromProperty(x)).collect(Collectors.toSet());
			PropertiesKeys propertyCount = new PropertiesKeys(propertyCountMap);
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
