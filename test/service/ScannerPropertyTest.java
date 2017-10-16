package service;

import org.junit.Test;

import service.impl.AhoCorasickScanner;
import service.impl.BoyerMooreScanner;
import service.impl.NaiveScanner;
import service.interfaces.PropertyScanner;

public class ScannerPropertyTest {
	PropertyScanner ahoCorasickScan = new AhoCorasickScanner();
	PropertyScanner boyerMooreScan = new BoyerMooreScanner();
	PropertyScanner naiveScan = new NaiveScanner();

	@Test
	public void testScanner() {
		String filePath1 = "C:\\Users\\Redan\\Desktop\\NISServiceBundle_ar.properties";
		String filePath2 = "C:\\Users\\Redan\\Desktop\\NISServiceBundle.properties";
		String filePath3 = "C:\\JDeveloper\\local\\siapp\\SIService\\src\\ValidationMessages.properties";
		// PropertyCount pCount1 =
		// PropertyCount.getPropertiesCountFromFile(filePath1);
		// PropertyCount pCount2 =
		// PropertyCount.getPropertiesCountFromFile(filePath2);
		// PropertyCount pCount3 =
		// PropertyCount.getPropertiesCountFromFile(filePath3);
		// pCount1.add(pCount2);
		// pCount1.add(pCount3);
		// Set<String> ahoCorasick = ahoCorasickScan.searchForUsages(pCount1,
		// "C:\\JDeveloper\\local\\siapp");
		// Set<String> boyerMoore = boyerMooreScan.searchForUsages(pCount1,
		// "C:\\JDeveloper\\local\\siapp");
		// Set<String> naive = naiveScan.searchForUsages(pCount1,
		// "C:\\JDeveloper\\local\\siapp");
		// assertEquals(ahoCorasick,boyerMoore);
		// assertEquals(ahoCorasick,naive);
	}
	// @Test
	// public void testScanner2() {
	// String filePath1 =
	// "C:\\Users\\Redan\\Desktop\\NISServiceBundle_ar.properties";
	// String filePath2 =
	// "C:\\Users\\Redan\\Desktop\\NISServiceBundle.properties";
	// String filePath3 =
	// "C:\\JDeveloper\\local\\siapp\\SIService\\src\\ValidationMessages.properties";
	// String[] keys = { "C:", "Users", "Redan", "Desktop", "SIN" };
	// Trie trie = Trie.builder().addKeywords(keys).build();
	// Collection<Emit> emits = trie.parseText(filePath1);
	// emits.forEach(s -> System.out.println(s.getKeyword() ) );
	// }

}
