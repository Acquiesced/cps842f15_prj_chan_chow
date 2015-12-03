package src.project;

public class CitationRecord {
	
	Integer inLinkDocumentID;
	Integer documentID;
	
	public CitationRecord(Integer inLinkDocumentID, Integer documentID){
		this.documentID = documentID;
		this.inLinkDocumentID = inLinkDocumentID;
	}
	
	public Integer getInLinkDocumentID() {
		return inLinkDocumentID;
	}

	public void setInLinkDocumentID(Integer outLinkDocumentID) {
		this.inLinkDocumentID = outLinkDocumentID;
	}

	public Integer getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Integer documentID) {
		this.documentID = documentID;
	}
}
