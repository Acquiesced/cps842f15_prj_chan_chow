package src.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Posting implements Serializable, Comparable<Posting> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6131111517174826906L;

	// Term frequency for each word
	private HashMap<String, Integer> termFrequency = new HashMap<String, Integer>();

	private int id;
	private ArrayList<Integer> positions;

	public Posting(int id, int position, String term) {
		this.id = id;
		this.termFrequency.put(term, 1);
		this.positions = new ArrayList<Integer>();
		this.positions.add(position);
	}

	public ArrayList<Integer> getPositions() {
		return positions;
	}

	public String printPositions() {
		StringBuilder position = new StringBuilder();
		for (int p : positions) {
			position.append(p).append(", ");
		}
		return position.length() > 0 ? position.substring(0, position.length() - 2) : "";
	}

	public void setPosition(int pos) {
		positions.add(pos);
	}

	public int getTermFirstOccurrence() {
		return positions.get(0);
	}

	public HashMap<String, Integer> getTermFrequencyList() {
		return termFrequency;
	}
	
	public int getTermFrequency(String key) {
		return termFrequency.get(key);
	}
	
	public void setTermFrequency(String key, int value) {
		this.termFrequency.put(key, value);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Posting [termFrequency=" + termFrequency + ", id=" + id + "]";
	}

	// compareTo is needed to implement the TreeSet
	@Override
	public int compareTo(Posting o) {
		return this.id - o.id;
	}

}
