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
	private double cosineSimilarity;
	private double pageRank;
	private double combinedScore;

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

	public double getCosineSimilarity() {
		return cosineSimilarity;
	}

	public void setCosineSimilarity(double similarity) {
		this.cosineSimilarity = similarity;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public double getCombinedScore() {
		return combinedScore;
	}

	public void setCombinedScore(double combinedScore) {
		this.combinedScore = combinedScore;
	}

	public String toStringDebug() {
		return "DocumentRecord [id=" + id + ", title=" + title + ", abstr=" + abstr + ", date=" + date + ", authorList="
				+ authorList + ", citationList=" + citationList +", similarity=" + cosineSimilarity + "]";
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
				+ "Citations:" + citationList + "\n" 
				+ "Cosine Similarity Score: " + cosineSimilarity + "\n"
				+ "PageRank Score: " + pageRank + "\n"
				+ "Combined Score: " + combinedScore + "\n");
		return resultString.toString().trim();
	}

	public String displayDocumentScoreInfo() {
		StringBuilder resultString = new StringBuilder();
		resultString.append("ID: " + id + "\n"  
				+ "Cosine Similarity Score: " + cosineSimilarity + "\n"
				+ "PageRank Score: " + pageRank + "\n"
				+ "Combined Score: " + combinedScore + "\n");
		return resultString.toString().trim();
	}
	
	@Override
	public String toString() {
		return (!abstr.equals("")) ? title + " " + abstr : title;
	}

	@Override
	public int compareTo(DocumentRecord other) {
		/*if (this.cosineSimilarity < other.cosineSimilarity)
			return 1;
		if (this.cosineSimilarity > other.cosineSimilarity)
			return -1; */
		if (this.combinedScore < other.combinedScore)
			return 1;
		if (this.combinedScore > other.combinedScore)
			return -1;
		return 0;
	}

}
