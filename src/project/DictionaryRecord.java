package src.project;
import java.io.Serializable;

public class DictionaryRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5653406770688197918L;
	private double documentFrequency = 0.0;
	private double idf = 0;
	
	public DictionaryRecord() {
		
	}
	
	/**
	 * Constructor for Dictionary Record used for to store document frequency
	 * @param documentFrequency DocumentFrequency for the term related to the doc record
	 */
	public DictionaryRecord(double documentFrequency) {
		this.documentFrequency = documentFrequency;
	}

	public double getDocumentFrequency() {
		return documentFrequency;
	}

	public void setDocumentFrequency(int documentFrequency) {
		this.documentFrequency = documentFrequency;
	}

	public double getIDF() {
		return idf;
	}

	public void setIDF(double idf) {
		this.idf = idf;
	}
	
	/**
	 * This method returns a String containing the idf and documentfrequency that is formatted
	 * @return String 
	 */
	@Override
	public String toString() {
		return "DictionaryRecord [idf=" + idf + ", documentFrequency=" + documentFrequency + "]";
	}

}
