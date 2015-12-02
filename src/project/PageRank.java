package src.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class PageRank {
	final static double DAMPINGFACTOR = 0.85;
	HashMap<Integer, DocumentRecord> documentRecordList;
	ArrayList<CitationRecord> citationList = new ArrayList<CitationRecord>();

	public PageRank(HashMap<Integer, DocumentRecord> documentRecordList) {
		this.documentRecordList = documentRecordList;
	}

	public void citationListInit() {
		for (Entry<Integer, DocumentRecord> docEntry : documentRecordList.entrySet()) {
			// System.out.println(docEntry.getValue().getCitationList());
			String[] recordCitationArr = docEntry.getValue().getCitationList().split("\n");
			for (String citation : recordCitationArr) {
				String[] citationStringArr = citation.split("\\s+");
				Integer outLinkDocumentID = Integer.valueOf(citationStringArr[0]);
				Integer documentID = Integer.valueOf(citationStringArr[2]);
				// System.out.println(outLinkDocumentID + " " + documentID);
				citationList.add(new CitationRecord(outLinkDocumentID, documentID));
			}
		}
	}

	public void printCitation() {
		for (CitationRecord citEntry : citationList) {
			System.out.println(citEntry.getOutLinkDocumentID() + " " + citEntry.getDocumentID());
		}
	}
}
