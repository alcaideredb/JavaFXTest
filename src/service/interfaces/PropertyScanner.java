package service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.PropertyCount;

public interface PropertyScanner {
 
	Set<String> searchForUsages(PropertyCount pCount, String root);

}
