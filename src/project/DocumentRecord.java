package src.project;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DocumentRecord implements Serializable, Comparable<DocumentRecord> {

	private static final long serialVersionUID = 1558811368963529136L;
	private int id;
	private String title = "";
	private String abstr = "";
	private String date = "";
	private String authorList = "";
	private String citationList = "";
	private double normalizedLength;
	private double similarity;

	public DocumentRecord(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstract() {
		return abstr;
	}

	public void setAbstract(String abstr) {
		this.abstr = abstr;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthorList() {
		return authorList;
	}

	public void setAuthorList(String authorList) {
		this.authorList = authorList;
	}
	
	public String getCitationList() {
		return citationList;
	}

	public void setCitationList(String citationList) {
		this.citationList = citationList;
	}

	public double getNormalizedLength() {
		return normalizedLength;
	}

	public void setNormalizedLength(double normalizedLength) {
		this.normalizedLength = normalizedLength;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public String toStringDebug() {
		return "DocumentRecord [id=" + id + ", title=" + title + ", abstr=" + abstr + ", date=" + date + ", authorList="
				+ authorList + ", citationList=" + citationList +", similarity=" + similarity + "]";
	}

	public Set<String> getUniqueTerms() {
		String documentText = this.toString().toLowerCase().trim();
		String[] words = documentText.split("[^\\w/'-]+");
		Set<String> uniqueWords = new HashSet<String>(Arrays.asList(words));
		return uniqueWords; 
	}

	public String displayDocumentInfo() {
		StringBuilder resultString = new StringBuilder();
		resultString.append("ID: " + id + "\n" + "Title: " + title + "\n" + "Authors: " + authorList + "\n"
				+ "Citations:" + citationList + "\n" + "Relevance Score: " + similarity + "\n");
		return resultString.toString().trim();
	}

	@Override
	public String toString() {
		return (!abstr.equals("")) ? title + " " + abstr : title;
	}

	@Override
	public int compareTo(DocumentRecord other) {
		if (this.similarity < other.similarity)
			return 1;
		if (this.similarity > other.similarity)
			return -1;
		return 0;
	}

}
