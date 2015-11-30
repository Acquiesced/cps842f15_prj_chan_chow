package src.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class Parse {
	private BufferedReader reader;

	public Parse(String file) {
		try {
			this.reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class Field {
		private static final char PREFIX = '.';
		private static final char ID = 'I';
		private static final char TITLE = 'T';
		private static final char ABSTRACT = 'W';
		private static final char DATE = 'B';
		private static final char AUTHORLIST = 'A';
		private static final char CITATIONLIST = 'X';
	}

	public void parseFile(HashMap<Integer, DocumentRecord> documents) {
		DocumentRecord document = null;
		String line = "";
		StringBuilder title = new StringBuilder();
		StringBuilder author = new StringBuilder();
		StringBuilder abstractText = new StringBuilder();
		StringBuilder date = new StringBuilder();
		StringBuilder citation = new StringBuilder();
		
		char state = 0;
		try {
			while ((line = reader.readLine()) != null) {
				if ((line = line.trim()).isEmpty()) {
					continue;
				}
				// Start of a new field type
				if (line.charAt(0) == Field.PREFIX) {
					state = line.charAt(1);

					// Start of a new entry
					if (state == Field.ID) {
						int id = Integer.parseInt(line.substring(2).trim());
						document = new DocumentRecord(id);

						// Save document to HashMap
						documents.put(id, document);

						// Reset all fields
						title.setLength(0);
						author.setLength(0);
						abstractText.setLength(0);
						date.setLength(0);
						citation.setLength(0);
					}
				} else {
					switch (state) {
					case Field.TITLE: {
						title.append(line).append("\n");
						document.setTitle(title.toString().trim());
						break;
					}
					case Field.ABSTRACT: {
						abstractText.append(line).append("\n");
						document.setAbstract(abstractText.toString().trim());
						break;
					}
					case Field.DATE: {
						date.append(line).append("\n");
						document.setDate(date.toString().trim());
						break;
					}
					case Field.AUTHORLIST: {
						author.append(line).append("\n");
						document.setAuthorList(author.toString().trim());
						break;
					}
					case Field.CITATIONLIST: {
						citation.append(line).append("\n");
						document.setCitationList(citation.toString().trim());
						break;
					}
					}
				}
			}
			// Save the last document to the hashmap
			documents.put(document.getId(), document);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void parseQueryFile(HashMap<Integer, Query> queries) {
		Query query = null;
		String line = "";
		StringBuilder queryText = new StringBuilder();
		StringBuilder source = new StringBuilder();

		char state = 0;
		try {
			while ((line = reader.readLine()) != null) {
				if ((line = line.trim()).isEmpty()) {
					continue;
				}
				// Start of a new field type
				if (line.charAt(0) == Field.PREFIX) {
					state = line.charAt(1);

					// Start of a new entry
					if (state == Field.ID) {
						int id = Integer.parseInt(line.substring(2).trim());
						query = new Query(id);
						// Save document to HashMap
						queries.put(id, query);
						// Reset all fields
						queryText.setLength(0);
						source.setLength(0);
					}
				} else {
					switch (state) {
					case Field.ABSTRACT: {
						queryText.append(line).append("\n");
						query.setQueryText(queryText.toString().trim());
						break;
					}
					}
				}
			}
			// Save the last query to the hashmap
			queries.put(query.getId(), query);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void parseqrels(TreeMap<Integer, Set<Integer>> qrels) {
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				if ((line = line.trim()).isEmpty()) {
					continue;
				}
				String[] tokens = line.split("\\s+");
				int queryId = Integer.parseInt(tokens[0]);
				int document = Integer.parseInt(tokens[1]);

				Set<Integer> relevantDocs = qrels.get(queryId);
				if (relevantDocs == null) {
					relevantDocs = new HashSet<Integer>();
				}
				relevantDocs.add(document);
				qrels.put(queryId, relevantDocs);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
