package src.project;

public class CitationRecord {
	
	Integer outLinkDocumentID;
	Integer documentID;
	
	public CitationRecord(Integer outLinkDocumentID, Integer documentID){
		this.documentID = documentID;
		this.outLinkDocumentID = outLinkDocumentID;
	}
	
	public Integer getOutLinkDocumentID() {
		return outLinkDocumentID;
	}

	public void setOutLinkDocumentID(Integer outLinkDocumentID) {
		this.outLinkDocumentID = outLinkDocumentID;
	}

	public Integer getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Integer documentID) {
		this.documentID = documentID;
	}



}
