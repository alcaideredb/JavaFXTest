package service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.PropertiesKeys;

public interface PropertyScanner {
 
	Set<String> searchForUsages(PropertiesKeys pCount, String root);

}
