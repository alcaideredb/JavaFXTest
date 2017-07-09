package service.impl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import model.PropertyCount;
import service.interfaces.*;
public class PropertyScannerImpl implements PropertyScanner {
	@Override
	public PropertyCount getPropertiesCountFromFile(String file) {
		Path path = Paths.get(file);
		try {
			List<String> lines = Files.lines(path).filter(s -> !s.startsWith("#") && !s.isEmpty()).collect(Collectors.toList());
			Map<String, Long> propertyCountMap = lines.stream().collect(
					Collectors.groupingBy(s -> s.toString().split("=")[0],Collectors.counting()));
			PropertyCount propertyCount = new PropertyCount(propertyCountMap);
 			return propertyCount;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new PropertyCount();
	}
}
