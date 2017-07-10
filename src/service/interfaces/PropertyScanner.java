package service.interfaces;

import java.util.List;
import java.util.Map;

import model.PropertyCount;

public interface PropertyScanner {
	 PropertyCount getPropertiesCountFromFile(String file);

 
	void searchForUsages(PropertyCount pCount, String... string);

 }
