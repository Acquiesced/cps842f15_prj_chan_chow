package src.project;

import java.util.ArrayList;
import java.util.Arrays;
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
		adjacencyMatrixInit(citationList);
	}

	private void adjacencyMatrixInit(ArrayList<CitationRecord> citationList) {
		int matrixBounds = documentRecordList.size();
		int[][] aMatrix = new int[matrixBounds][matrixBounds];
		for (int[] row : aMatrix) {
			Arrays.fill(row, 0);
		}

		for (CitationRecord citRecord : citationList) {
			aMatrix[citRecord.getDocumentID() - 1][citRecord.getOutLinkDocumentID() - 1] = 1;
			//System.out.println(citRecord.getDocumentID() + " " + citRecord.getOutLinkDocumentID() + " " +  aMatrix[citRecord.getDocumentID() - 1][citRecord.getOutLinkDocumentID() - 1] );
		}
	}

	public void printCitation() {
		for (CitationRecord citRecord : citationList) {
			System.out.println(citRecord.getOutLinkDocumentID() + " " + citRecord.getDocumentID());
		}
	}
}
