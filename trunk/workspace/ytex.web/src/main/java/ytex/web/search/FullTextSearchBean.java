package ytex.web.search;

import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import ytex.dao.DocumentSearchDao;

/**
 * Jsf bean for full text search.
 * 
 * 
 * @author vijay
 *
 */
public class FullTextSearchBean {
	private String searchTerm;
	private List<Map<String,Object>> searchResultList;
	private DocumentSearchDao documentSearchDao;
	
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public List<Map<String, Object>> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<Map<String, Object>> searchResultList) {
		this.searchResultList = searchResultList;
	}

	public DocumentSearchDao getDocumentSearchDao() {
		return documentSearchDao;
	}

	public void setDocumentSearchDao(DocumentSearchDao documentSearchDao) {
		this.documentSearchDao = documentSearchDao;
	}

	public void searchListen(ActionEvent event) {
		if(searchTerm != null && searchTerm.trim().length() > 0)
			this.searchResultList = documentSearchDao.fullTextSearch(searchTerm);
	}
}
