package elizabeth.assigment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Test {
	private static ArrayList<Double> timeList = new ArrayList<Double>();
	private static final String file = "cacm/cacm.all";
	//private static final String file = "cacm/cacm.small2";

	static HashMap<Integer, DocumentRecord> documents = new HashMap<Integer, DocumentRecord>();
	static TreeMap<String, DictionaryRecord> dictionary = new TreeMap<String, DictionaryRecord>();
	static TreeMap<String, TreeMap<Integer, Posting>> postingsList = new TreeMap<String, TreeMap<Integer, Posting>>();

	public static double calculateAverage() {
		if (timeList.size() == 0)
			return 0;
		double total = 0;
		for (double time : timeList) {
			total += time;
		}
		return total / timeList.size();
	}

	public static void main(String[] args) {
		boolean useStemming = false;
		boolean useStopWords = false;
		long startTime = System.nanoTime();
		System.out.println("Welcome!");

		Invert inverter = new Invert(file);
		double idfThreshold = 1.0;
		int maxResults = 15;

		Options options = new Options();
		options.addOption("stem", false, "Enable stemming");
		options.addOption("stopwords", false, "Enable stop word removal");
		options.addOption("create", false, "Create index input files");
		options.addOption(OptionBuilder.withLongOpt("idf").withDescription("Idf threshold").withType(Number.class)
				.hasArg().withArgName("argname").create());
		options.addOption(
				OptionBuilder.withLongOpt("maxresults").withDescription("Maximum number of entries to retrieve")
						.withType(Number.class).hasArg().withArgName("argname").create());
		options.addOption("help", "Show this menu");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("stem")) {
				inverter.setUseStemming(true);
				useStemming = true;
			}
			if (cmd.hasOption("stopwords")) {
				inverter.setUseStopWords(true);
				useStopWords = true;
			}
			if (cmd.hasOption("create")) {
				inverter.setCreateNewIndex(true);
			}
			if (cmd.hasOption("idf")) {
				idfThreshold = ((Number) cmd.getParsedOptionValue("idf")).doubleValue();
			}
			if (cmd.hasOption("maxresults")) {
				maxResults = ((Number) cmd.getParsedOptionValue("maxresults")).intValue();
			}
			if (cmd.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("Test for Invert", options);
				System.exit(0);
			}
		} catch (ParseException e) {
			System.err.println("Parsing failed.  Reason: " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Test for Invert", options);
			System.exit(0);
		}

		inverter.initializeIndex(documents, dictionary, postingsList);
		documents = inverter.getDocuments();
		dictionary = inverter.getDictionary();
		postingsList = inverter.getPostingsList();

		long duration = System.nanoTime() - startTime;
		double seconds = (double) duration / 1000000000;
		System.out.println("Index creation finished. Time: " + seconds + "s");

		//TODO: Change the main program to use Search instead of Test.
		Search search = new Search(documents, dictionary, postingsList, useStemming, useStopWords, idfThreshold,
				maxResults);
		System.out.println("===========================================================================");
		System.out.println("Enter search term: ");

		try (Scanner scan = new Scanner(System.in)) {
			while (scan.hasNext()) {
				String query = scan.nextLine().trim();
				if (query.equals("zzend")) {
					System.out.println("Program exiting...");
					System.out.println(
							"Average query time for " + timeList.size() + " queries: " + calculateAverage() + "s");
					System.exit(0);
				} else {
					startTime = System.nanoTime();
					search.displayResults(query);
					duration = System.nanoTime() - startTime;
					seconds = (double) duration / 1000000000;
					System.out.println("Query time: " + seconds + "s");
					timeList.add(seconds);
					System.out.println("===========================================================================");
					System.out.println("Enter search term: ");
				}
			}
		}
	}
}
