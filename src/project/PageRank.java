package src.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class PageRank {
	final static double DAMPINGFACTOR = 0.85;
	HashMap<Integer, DocumentRecord> documentRecordList;
	TreeMap<Integer, DocumentRecord> sortedDocumentRecordList;
	// ArrayList<CitationRecord> citationList = new ArrayList<CitationRecord>();
	Set<CitationRecord> citationList = new HashSet<CitationRecord>();

	public PageRank(HashMap<Integer, DocumentRecord> documentRecordList) {
		this.documentRecordList = documentRecordList;
		this.sortedDocumentRecordList = new TreeMap<Integer, DocumentRecord>(
				documentRecordList);
	}

	public void citationListInit() {
		for (Entry<Integer, DocumentRecord> docEntry : documentRecordList
				.entrySet()) {
			// System.out.println(docEntry.getValue().getCitationList());
			String[] recordCitationArr = docEntry.getValue().getCitationList()
					.split("h\n");
			for (String citation : recordCitationArr) {
				String[] citationStringArr = citation.split("\\s+");
				Integer outLinkDocumentID = Integer
						.valueOf(citationStringArr[0]);
				Integer documentID = Integer.valueOf(citationStringArr[2]);
				// System.out.println(outLinkDocumentID + " " + documentID);
				citationList.add(new CitationRecord(outLinkDocumentID,
						documentID));
			}
		}
		double[][] pMatrix = probabilityMatrixInit(adjacencyMatrixInit(citationList));
		randomSurfer(pMatrix);
	}

	private double[][] adjacencyMatrixInit(Set<CitationRecord> citationList) {
		int matrixBounds = sortedDocumentRecordList.lastKey();
		double[][] aMatrix = new double[matrixBounds][matrixBounds];
		for (double[] row : aMatrix) {
			Arrays.fill(row, 0);
		}

		for (CitationRecord citRecord : citationList) {
			aMatrix[citRecord.getDocumentID() - 1][citRecord
					.getOutLinkDocumentID() - 1] = 1;
		}
		return aMatrix;
	}

	private double[][] probabilityMatrixInit(double[][] aMatrix) {
		int rowLinkCount = 0;
		int matrixSize = sortedDocumentRecordList.lastKey();

		for (double[] aMatrixRow : aMatrix) {
			for (int i = 0; i < aMatrixRow.length; i++) {
				if (aMatrixRow[i] == 1.0) {
					rowLinkCount++;
				}
			}
			if (rowLinkCount > 0) {
				for (int i = 0; i < aMatrixRow.length; i++) {
					aMatrixRow[i] = aMatrixRow[i] / rowLinkCount;
					aMatrixRow[i] *= (1 - DAMPINGFACTOR);
					aMatrixRow[i] += (DAMPINGFACTOR / matrixSize);
				}
			} else {
				// teleport
				for (int i = 0; i < aMatrixRow.length; i++) {
					aMatrixRow[i] = 1 / matrixSize;
				}
			}
			rowLinkCount = 0;
		}
		return aMatrix;
	}

	private double[][] randomSurfer(double[][] pMatrix) {
		int matrixSize = sortedDocumentRecordList.lastKey();
		double[][] pVector = new double[1][matrixSize];
		Arrays.fill(pVector[0], 0);
		pVector[0][0] = 1.0;
		for (int i = 0; i < 10; i++) {
			pVector = multiplyMatrix(pVector, pMatrix);
		}
		double[][] pageRank = pVector;
		for(int i =0; i<pageRank[0].length; i++){
			System.out.printf("%.10f ",pageRank[0][i]);

		}
		return pageRank;
	}

	private double[][] multiplyMatrix(double[][] pVector, double[][] pMatrix) {
		int pVectorRows = pVector.length;
		int pVectorcols = pVector[0].length;
		int pMatrixrows = pMatrix.length;
		int pMatrixcols = pMatrix[0].length;
		if (pVectorcols != pMatrixrows)
			throw new RuntimeException("Illegal matrix dimensions.");
		double[][] result = new double[pVectorRows][pMatrixcols];
		for (int i = 0; i < pVectorRows; i++) {
			for (int j = 0; j < pMatrixcols; j++) {
				for (int k = 0; k < pVectorcols; k++) {
					result[i][j] += pVector[i][k] * pMatrix[k][j];
				}
			}
		}
		return result;

	}

	public void printCitation() {
		for (CitationRecord citRecord : citationList) {
			System.out.println(citRecord.getOutLinkDocumentID() + " "
					+ citRecord.getDocumentID());
		}
	}
}
