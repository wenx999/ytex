package ytex.model;

import java.util.Date;

public class DocumentSearchResult {
	@Override
	public String toString() {
		return "DocumentSearchResult [cuiText=" + cuiText + ", documentID="
				+ documentID + ", sentenceText=" + sentenceText + "]";
	}
	public int getDocumentID() {
		return documentID;
	}
	public void setDocumentID(int documentID) {
		this.documentID = documentID;
	}
	public String getSentenceText() {
		return sentenceText;
	}
	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public String getDocumentTitle() {
		return documentTitle;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	int documentID;
	String sentenceText;
	Date documentDate;
	String documentTitle;
	String documentTypeName;
	String cuiText;
	public String getCuiText() {
		return cuiText;
	}
	public void setCuiText(String cuiText) {
		this.cuiText = cuiText;
	}
}
