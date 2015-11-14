package elizabeth.assigment1;
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
	
	@Override
	public String toString() {
		return "DictionaryRecord [idf=" + idf + ", documentFrequency=" + documentFrequency + "]";
	}

}
