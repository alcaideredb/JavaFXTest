package service.impl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import exception.DataAccessException;
import model.SafeFileReader;
import service.interfaces.PostFilterProcessor;

public class FilePostInclusionFilterProcessor implements PostFilterProcessor {
	private List<String> prefixInclusionFilters;
	private String fileName;

	public FilePostInclusionFilterProcessor(String fileName) {
		try {
			String fileContent = SafeFileReader.createLenientBufferedReader(Paths.get(fileName)).lines()
					.collect(Collectors.joining());
			prefixInclusionFilters = Arrays.stream(fileContent.split(",")).filter(x -> x != null).map(x -> x.trim())
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void filter(Set<String> included, Set<String> excluded) {
		for (String prefix : prefixInclusionFilters) {
			Set<String> swapper = excluded.stream().filter(x -> x.startsWith(prefix)).collect(Collectors.toSet());
			included.addAll(swapper);
			excluded.removeAll(swapper);
		}
	}

}
