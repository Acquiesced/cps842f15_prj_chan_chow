package src.project;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Search {

	private HashMap<Integer, DocumentRecord> documents;
	private TreeMap<String, DictionaryRecord> dictionary;
	private TreeMap<String, TreeMap<Integer, Posting>> postingsList;

	private static final String STOPWORDSFILE = "stopwords.txt";

	private List<String> stopWordsList = new ArrayList<String>();
	private boolean useStemming;
	private boolean useStopWords;
	private double idfThreshold;
	private int maxResults;

	public Search(HashMap<Integer, DocumentRecord> documents, TreeMap<String, DictionaryRecord> dictionary,
			TreeMap<String, TreeMap<Integer, Posting>> postingsList, boolean useStemming, boolean useStopWords,
			double idfThreshold, int maxResults) {
		this.documents = documents;
		this.dictionary = dictionary;
		this.postingsList = postingsList;
		this.useStemming = useStemming;
		this.useStopWords = useStopWords;
		this.idfThreshold = idfThreshold;
		this.maxResults = maxResults;
		if (useStopWords) {
			try {
				stopWordsList = Files.readAllLines(new File(STOPWORDSFILE).toPath(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<DocumentRecord> getResultList(String query) {
		if (this.useStemming) {
			Stemmer s = new Stemmer();
			query = s.stem(query.trim());
		}

		Query q = new Query(query.toLowerCase());
		q.calculateQueryWeights(dictionary, useStopWords, idfThreshold);
		return getCosineSimilarity(q);
	}

	public void displayResults(String query) {
		List<DocumentRecord> results = getResultList(query);

		int resultNumber = 1;
		for (DocumentRecord d : results) {
			if (resultNumber <= maxResults && d != null) {
				System.out.println(resultNumber + ". " + d.displayDocumentInfo() + "\n");
				resultNumber++;
			}
		}
		
		if (results.isEmpty()) {
			System.out.println("No results found for query!");
		}
	}

	public List<DocumentRecord> getResultSubList(String query) {
		List<DocumentRecord> results = getResultList(query);
		List<DocumentRecord> subList = new ArrayList<DocumentRecord>();
		subList = results.subList(0, (maxResults - 1) > results.size() ? results.size() : maxResults);
		/*
		 * for (DocumentRecord d : results) { subList.add(d); if (subList.size()
		 * >= maxResults) break; }
		 */
		return subList;
	}

	// document at a time evaluation
	public List<DocumentRecord> getCosineSimilarity(Query query) {
		double sim = 0.0;
		double numerator = 0.0;
		double normalizedLength = 0.0;
		double weight = 1.0;
		DictionaryRecord dictRec = new DictionaryRecord();
		double collectionSize = documents.size();

		List<DocumentRecord> results = new ArrayList<DocumentRecord>();
		Set<Integer> relevantPostingIds = new HashSet<Integer>();

		// get a list of relevant posting ids for each term
		for (String term : query.weights.keySet()) {
			if (postingsList.containsKey(term)) {
				for (Posting p : postingsList.get(term).values()) {
					relevantPostingIds.add(p.getId());
				}
			}
		}

		// look at the documents for each relevant posting
		for (Integer id : relevantPostingIds) {
			DocumentRecord d = documents.get(id);
			if (results.contains(d))
				continue;

			// go through the unique terms in each document
			for (String term : d.getUniqueTerms()) {

				if (useStemming) {
					Stemmer s = new Stemmer();
					term = s.stem(term);
				}

				if (useStopWords && stopWordsList.contains(term)) {
					continue;
				}

				// if the term is in the dictionary, compute its weight and
				// similarity
				if (dictionary.containsKey(term)) {
					dictRec = dictionary.get(term);
					double docFreq = dictRec.getDocumentFrequency();
					double idf = Math.log10(collectionSize / docFreq);
					int freq = postingsList.get(term).get(d.getId()).getTermFrequency(term);

					double termFreq = 1 + Math.log10(freq);
					weight = termFreq * idf;
					numerator += weight * query.getWeight(term);
					normalizedLength += weight * weight;
				}
			}
			sim = numerator / (Math.sqrt(normalizedLength) * query.getNormalizedLength());
			d.setSimilarity(sim);

			numerator = 0;
			normalizedLength = 0.0;
			results.add(d);
		}
		Collections.sort(results);
		return results;
	}

	/* The following methods were used for assignment 1 only */

	public String searchDictionary(String query) {
		/*
		 * program should display the document frequency and all the documents
		 * which contain this term, for each document, it should display the
		 * document ID, the title, the term frequency, all the positions the
		 * term occurs in that document, and a summary of the document
		 * highlighting the first occurrence of this term with 10 terms in its
		 * context.
		 */

		StringBuilder resultString = new StringBuilder();
		if (dictionary.containsKey(query)) {
			resultString.append(
					"TERM: " + query + "\n" + "DOCUMENT FREQUENCY: " + dictionary.get(query).getDocumentFrequency()
							+ "\n" + "INVERSE DOCUMENT FREQUENCY: " + dictionary.get(query).getIDF() + "\n");
			for (Posting p : postingsList.get(query).values()) {
				int id = p.getId();
				DocumentRecord d = documents.get(id);
				resultString.append("DOCUMENT ID: " + id + "\n"
						+ "TITLE: " + d.getTitle() + "\n" + "TERM FREQUENCY: " + p.getTermFrequency(query) + "\n"
						+ "POSITIONS: " + p.printPositions() + "\n" 
						+ "SUMMARY: " + getContext(d.getTitle(), d.getAbstract(), query, p.getTermFirstOccurrence())
						+ "\n"
						+ "---------------------------------------------------------------------------\n");
			}
			return resultString.toString().trim();
		}
		return "Term not found!";
	}

	private String getContext(String titleText, String abstractText, String term, int position) {
		int titleLength = titleText.trim().split("\\s+").length;
		// Set the text based on whether the term occurs in the title or the
		// abstract.
		// We are only going to return the field that the term occurs in.
		String text = position < titleLength ? titleText : abstractText;

		if (text == abstractText) {
			position -= titleLength; // offset the position by the length of the
										// title text
		}

		String[] words = text.trim().split("\\s+");

		int length = words.length;
		int start = position - 5;
		int end = position + 5;
		if (start < 0 && end < length) {
			end = end + Math.abs(start);
		}
		else if (end > length && start > 0) {
			start = start - (end - length);
		}
		start = start > 0 ? start : 0;
		end = end < length ? end : length;

		StringBuilder context = new StringBuilder();
		for (int i = start; i < end; i++) {
			context.append(words[i]).append(" ");
		}

		return context.toString().trim();
	}

}
