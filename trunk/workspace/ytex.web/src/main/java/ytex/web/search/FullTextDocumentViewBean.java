package ytex.web.search;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringEscapeUtils;

import ytex.dao.DocumentSearchDao;

/**
 * Jsf bean to view results of fullTextSearch.jspx.
 * Relies on documentID parameter.
 * @author vijay
 *
 */
public class FullTextDocumentViewBean {
	private String docText;
	private DocumentSearchDao documentSearchDao;
	private int documentID;

	public int getDocumentID() {
		this.loadDocument();
		return documentID;
	}

	public String getDocText() {
		this.loadDocument();
		return docText;
	}

	public DocumentSearchDao getDocumentSearchDao() {
		return documentSearchDao;
	}

	public void setDocumentSearchDao(DocumentSearchDao documentSearchDao) {
		this.documentSearchDao = documentSearchDao;
	}

	public void loadDocument() {
		if (this.docText == null) {
			String strDocumentID = (String) FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap().get(
							"documentID");
			documentID = Integer.parseInt(strDocumentID); 
			String docTextUnformatted = this.documentSearchDao
					.getFullTextSearchDocument(documentID);
			this.docText = StringEscapeUtils.escapeXml(docTextUnformatted).replaceAll("\n", "<br>");
		}
	}
}
