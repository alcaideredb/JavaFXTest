package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	
	
	
}
