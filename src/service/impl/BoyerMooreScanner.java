package service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import model.PropertyCount;
import service.interfaces.*;

public class BoyerMooreScanner implements PropertyScanner {
	 
 
	@Override
 	public Set<String> searchForUsages (PropertyCount pCount, String root) {
		Set<String> keysFound = null;
		Set<String> includedProperties = new HashSet<>();

		List<String> filePaths = retrieveFilePaths(root);
 	 
		for (String fPath : filePaths) {
			keysFound = pCount.getKeySet().parallelStream().filter(key -> fileContainsText(key, fPath))
					.collect(Collectors.toSet());
			includedProperties.addAll(keysFound);
		}
		return includedProperties;
	}
	private List<String> retrieveFilePaths(String root) {
		Path path = Paths.get(root);
		List<String> filePaths = new ArrayList<>();

		try {
			filePaths = Files.walk(path).parallel().filter(f -> f.toString().endsWith(".java") || f.toString().endsWith(".jsf")
					|| f.toString().endsWith(".jsff")).map(Path::toString).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("Error in retrieving files");
		}
		return filePaths;
	}

	private static boolean fileContainsText(String pattern, String fileName) {
		try {
			String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			return new BoyerMoore(pattern).contains(text);
 		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	 
}
