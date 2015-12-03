package src.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

public class Invert {

	private static final String STOPWORDSFILE = "stopwords.txt";
	private static final String DICTIONARYFILE = "dictionary.txt";
	private static final String POSTINGSFILE = "postings.txt";
	private static final String DOCUMENTSFILE = "documents.txt";
	private static final String PROPERTIESFILE = "index.properties";

	private static final String INPUT1FOLDER = "input1/"; // folder for no
															// stemming and no
															// stop word removal
	private static final String INPUT2FOLDER = "input2/"; // folder for stemming
															// and stop word
															// removal
	private static final String INPUT3FOLDER = "input3/"; // folder for stemming
															// and no stop word
															// removal
	private static final String INPUT4FOLDER = "input4/"; // folder for stop
															// word removal and
															// no stemming

	private String collectionFile;
	private String dirName;

	private HashMap<Integer, DocumentRecord> documents;
	private TreeMap<String, DictionaryRecord> dictionary;
	private TreeMap<String, TreeMap<Integer, Posting>> postingsList;

	private List<String> stopWordsList = new ArrayList<String>();

	boolean useStemming = false;
	boolean useStopWords = false;
	boolean createNewIndex = false;

	public boolean getUseStemming() {
		return useStemming;
	}

	public void setUseStemming(boolean useStemming) {
		this.useStemming = useStemming;
	}

	public boolean getUseStopWords() {
		return useStopWords;
	}

	public void setUseStopWords(boolean useStopWords) {
		this.useStopWords = useStopWords;
	}

	public boolean createNewIndex() {
		return createNewIndex;
	}

	public void setCreateNewIndex(boolean createNewIndex) {
		this.createNewIndex = createNewIndex;
	}

	public HashMap<Integer, DocumentRecord> getDocuments() {
		return documents;
	}

	public TreeMap<String, DictionaryRecord> getDictionary() {
		return dictionary;
	}

	public TreeMap<String, TreeMap<Integer, Posting>> getPostingsList() {
		return postingsList;
	}

	public Invert(String file) {
		this.collectionFile = file;
	}

	public void initializeIndex(HashMap<Integer, DocumentRecord> documents,
			TreeMap<String, DictionaryRecord> dictionary, TreeMap<String, TreeMap<Integer, Posting>> postingsList) {
		this.documents = documents;
		this.dictionary = dictionary;
		this.postingsList = postingsList;
		this.dirName = getDirectoryName();
		if (this.createNewIndex) {
			createIndex();
		} else {
			readFromIndex();
		}
	}

	private void readFromIndex() {
		if (savedFilesExist()) {
			System.out.println("Reading from existing input files...");
			this.dictionary = (TreeMap<String, DictionaryRecord>) readFromFile(this.dirName + DICTIONARYFILE,
					this.dictionary);
			this.postingsList = (TreeMap<String, TreeMap<Integer, Posting>>) readFromFile(this.dirName + POSTINGSFILE,
					this.postingsList);
			this.documents = (HashMap<Integer, DocumentRecord>) readFromFile(this.dirName + DOCUMENTSFILE,
					this.documents);
			readProperties();
			showPropertiesStatus();
			getDictionarySize();
		} else {
			System.out.println("Input files missing, creating new index.");
			createIndex();
		}
	}

	private void showPropertiesStatus() {
		String stopwords = "Stop word removal is ";
		String stemming = "Stemming is ";
		if (this.useStopWords) {
			stopwords += "enabled.";
		} else {
			stopwords += "disabled.";
		}
		if (this.useStemming) {
			stemming += "enabled.";
		} else {
			stemming += "disabled.";
		}
		System.out.println(stopwords);
		System.out.println(stemming);
	}

	private boolean savedFilesExist() {
		File dictionaryFile = new File(this.dirName + DICTIONARYFILE);
		File postingsFile = new File(this.dirName + POSTINGSFILE);
		File documentsFile = new File(this.dirName + DOCUMENTSFILE);
		return (dictionaryFile.isFile() && dictionaryFile.canRead() && postingsFile.isFile() && postingsFile.canRead()
				&& documentsFile.isFile() && documentsFile.canRead());
	}

	// There's no type safety!
	private void writeToFile(String fileName, Map<?, ?> map) {
		try {
			File targetFile = new File(fileName);
			File toWrite = targetFile.getParentFile();
			if (!toWrite.exists() && !toWrite.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: " + toWrite);
			}
			FileOutputStream fos = new FileOutputStream(targetFile);
			FSTObjectOutput out = new FSTObjectOutput(fos);
			out.writeObject(map);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// No type safety!
	private Map<?, ?> readFromFile(String fileName, Map<?, ?> map) {
		try {
			File toRead = new File(fileName);
			FileInputStream fis = new FileInputStream(toRead);
			FSTObjectInput in = new FSTObjectInput(fis);
			map = (Map<?, ?>) in.readObject();
			in.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	private void writeProperties(String directoryName) {
		Properties prop = new Properties();
		File targetFile = new File(directoryName + PROPERTIESFILE);
		File toWrite = targetFile.getParentFile();
		if (!toWrite.exists() && !toWrite.mkdirs()) {
			throw new IllegalStateException("Couldn't create dir: " + toWrite);
		}
		try {
			FileOutputStream fos = new FileOutputStream(targetFile);
			prop.setProperty("useStemming", String.valueOf(this.useStemming));
			prop.setProperty("useStopWords", String.valueOf(this.useStopWords));
			prop.store(fos, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readProperties() {
		Properties prop = new Properties();
		File toRead = new File(this.dirName + PROPERTIESFILE);
		try {
			FileInputStream fos = new FileInputStream(toRead);
			prop.load(fos);
			this.useStemming = Boolean.valueOf(prop.getProperty("useStemming"));
			this.useStopWords = Boolean.valueOf(prop.getProperty("useStopWords"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getDictionarySize() {
		System.out.println("Dictionary size is: " + dictionary.size() + " terms.");
	}

	private String getDirectoryName() {
		if (!useStemming && !useStopWords) {
			// save no stop word removal and no stemming files to the input1
			// folder
			return INPUT1FOLDER;
		}
		if (useStemming) {
			if (useStopWords) {
				// stemming and stop word removal
				return INPUT2FOLDER;
			} else {
				// stemming and no stop word removal
				return INPUT3FOLDER;
			}
		} else {
			// stop word removal and no stemming
			return INPUT4FOLDER;
		}
	}

	private void createIndex() {
		System.out.println("Creating index...");
		try {
			this.stopWordsList = Files.readAllLines(new File(STOPWORDSFILE).toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Print out a message to specify if stemming or stop word removal are
		// enabled/disabled.
		showPropertiesStatus();

		Parse parser = new Parse(collectionFile);
		parser.parseFile(documents);

		for (DocumentRecord d : documents.values()) {
			buildIndex(d.getId(), d.toString());
		}
		
		// Set the PageRank for each document
		PageRank pageRanker = new PageRank(documents);
		double[] pageRanks = pageRanker.citationListInit();
		pageRanks = pageRanker.normalizePageRank(pageRanks);
		for (int i = 0; i < pageRanks.length; i++) {
			documents.get(i+1).setPageRank(pageRanks[i]);
		}
		
		getDictionarySize();

		writeToFile(this.dirName + DICTIONARYFILE, dictionary);
		writeToFile(this.dirName + POSTINGSFILE, postingsList);
		writeToFile(this.dirName + DOCUMENTSFILE, documents);

		// Save whether stemming and stop words are enabled/disabled
		writeProperties(this.dirName);
	}

	private void buildIndex(int id, String text) {
		String document = text.toLowerCase().trim();
		String[] words = document.split("[^\\w/'-]+"); // split on any
														// punctuation except
														// for apostrophes,
														// hyphens, forward
														// slashes.
		Posting posting = null;
		DictionaryRecord dictionaryRecord = null;
		TreeMap<Integer, Posting> postings = null;
		Set<String> uniqueWords = new HashSet<String>();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			int position = i;

			// Apply stemming if it is enabled.
			if (useStemming) {
				Stemmer s = new Stemmer();
				word = s.stem(word);
			}

			// If current word is a stop word, ignore it and continue.
			if (useStopWords && stopWordsList.contains(word)) {
				continue;
			}

			// Add word to the hashset to calculate the document frequency
			uniqueWords.add(word);

			// Add the word to the dictionary if it is unique
			if (!dictionary.containsKey(word)) {
				dictionaryRecord = new DictionaryRecord();
				dictionary.put(word, dictionaryRecord);
				postings = new TreeMap<Integer, Posting>();
				posting = new Posting(id, position, word);
				postings.put(id, posting);
				postingsList.put(word, postings);
			}

			// The term is already in the dictionary
			else {
				// The term should already have a posting list if it is in the
				// dictionary.
				if (postingsList.containsKey(word)) {

					// Get the existing postings list associated with this term
					postings = postingsList.get(word);

					// Get the last posting from the list
					Entry<Integer, Posting> postingEntry = postings.lastEntry();
					posting = postingEntry.getValue();

					// If the document id of the most recently added posting is
					// the same, increment the term frequency because we're on
					// the same document
					if (posting.getId() == id) {
						int freq = posting.getTermFrequency(word);
						posting.setTermFrequency(word, ++freq);
						// Add the position of the new term
						posting.setPosition(position);
					} else {
						// Otherwise, make a new posting and add it to the
						// posting list
						posting = new Posting(id, position, word);
						postings.put(id, posting);
					}
				}
			}
		}
		calculateDocumentFrequency(uniqueWords);
	}

	private void calculateDocumentFrequency(Set<String> uniqueWords) {
		for (String s : uniqueWords) {
			if (dictionary.containsKey(s)) {
				double freq = dictionary.get(s).getDocumentFrequency();
				dictionary.put(s, new DictionaryRecord(++freq));
			}
		}

	}
}