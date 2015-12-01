package src.project;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Query {

	HashMap<String, Double> weights;
	private double normalizedLength;
	private int id;
	private String queryText;
	private static final String STOPWORDSFILE = "stopwords.txt";
	private List<String> stopWordsList = new ArrayList<String>();

	@Override
	public String toString() {
		return "Query [id=" + id + ", queryText=" + queryText + "]";
	}

	public Query(String queryText) {
		this.queryText = queryText;
		weights = new HashMap<String, Double>();
	}

	public Query(int id) {
		this.id = id;
		weights = new HashMap<String, Double>();
	}

	public Collection<String> getTerms() {
		return weights.keySet();
	}

	public Collection<Double> getWeights() {
		return weights.values();
	}

	public double getWeight(String term) {
		return (weights.get(term) == null) ? 0 : weights.get(term);
	}

	public double getNormalizedLength() {
		return normalizedLength;
	}

	public void setNormalizedLength(double normalizedLength) {
		this.normalizedLength = normalizedLength;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public void initializeStopWordsList() {
		try {
			stopWordsList = Files.readAllLines(new File(STOPWORDSFILE).toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void calculateQueryWeights(TreeMap<String, DictionaryRecord> dictionary, boolean useStopWords, double idfThreshold) {
		// the weight of the query term is the idf
		String[] words = this.queryText.split("[^\\w/'-]+");

		Set<String> uniqueWords = new HashSet<String>(Arrays.asList(words));

		HashMap<String, Integer> termFrequencies = new HashMap<String, Integer>();

		if (useStopWords) {
			initializeStopWordsList();
		}

		for (String word : words) {
			if (useStopWords && stopWordsList.contains(word)) {
				uniqueWords.remove(word);
				continue;
			}

			if (dictionary.containsKey(word)) {
				DictionaryRecord dictRec = dictionary.get(word);
				double docFreq = dictRec.getDocumentFrequency();
				double idf = Math.log10(dictionary.size() / docFreq);
				if (idf < idfThreshold) {
					uniqueWords.remove(word);
					continue;
				}
			}

			if (!termFrequencies.containsKey(word)) {
				termFrequencies.put(word, 1);
			} else {
				termFrequencies.put(word, termFrequencies.get(word) + 1);
			}
		}

		for (String word : uniqueWords) {
			int freq = termFrequencies.get(word);
			double weight = 1 + Math.log10(freq);
			this.weights.put(word, 1 + Math.log10(weight));
			this.normalizedLength += weight * weight;
		}

		this.normalizedLength = Math.sqrt(normalizedLength);
	}

}
