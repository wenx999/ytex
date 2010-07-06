package ytex.vacs.uima;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import ytex.uima.ApplicationContextHolder;
import ytex.vacs.uima.types.DocumentKey;

/**
 * 
 * Read documents from db.
 * @TODO more doc
 * @author vijay
 *
 */
public class DBCollectionReader extends CollectionReader_ImplBase {
	private static final Log log = LogFactory.getLog(DBCollectionReader.class);

	String queryGetDocumentKeys;
	String queryGetDocument;

	DataSource dataSource;
	SimpleJdbcTemplate simpleJdbcTemplate;
	NamedParameterJdbcTemplate namedJdbcTemplate;
	List<Map<String, Object>> listDocumentIds;
	int i = 0;

	@Override
	public void initialize() throws ResourceInitializationException {
		super.initialize();
		ProcessingResourceMetaData metaData = getProcessingResourceMetaData();
		ConfigurationParameterSettings paramSettings = metaData
				.getConfigurationParameterSettings();
		this.queryGetDocumentKeys = (String) paramSettings
				.getParameterValue("queryGetDocumentKeys");
		this.queryGetDocument = (String) paramSettings
				.getParameterValue("queryGetDocument");
		dataSource = (DataSource) ApplicationContextHolder
				.getApplicationContext().getBean("dataSource");
		simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
		namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		if (listDocumentIds == null) {
			listDocumentIds = simpleJdbcTemplate
					.queryForList(queryGetDocumentKeys);
			i = 0;
		}
	}

	@Override
	public void getNext(final CAS aCAS) throws IOException, CollectionException {
		if (i < listDocumentIds.size()) {
			final Map<String, Object> id = listDocumentIds.get(i++);
			if (log.isDebugEnabled()) {
				log.debug("loading document with id = " + id);
			}
			namedJdbcTemplate.query(queryGetDocument, id,
					new RowCallbackHandler() {
						boolean bFirstRowRead = false;

						@Override
						public void processRow(ResultSet rs)
								throws SQLException {
							if (!bFirstRowRead) {
								LobHandler lobHandler = new DefaultLobHandler();
								String clobText = lobHandler.getClobAsString(
										rs, 1);
								aCAS.setDocumentText(clobText);
							} else {
								log.error("Multiple documents for document key: " + id);
							}
						}
					});
			try {
				DocumentKey docKey = new DocumentKey(aCAS.getJCas());
				if(id.get("studyid") != null)
					docKey.setStudyID((Integer) id.get("studyid"));
				if(id.get("uid") != null)
					docKey.setUid((Integer) id.get("uid"));
				if(id.get("document_type_id") != null)
					docKey.setDocumentType((Integer) id.get("document_type_id"));
				if (id.get("site_id") != null)
					docKey.setSiteID((String) id.get("site_id"));
				docKey.addToIndexes();
			} catch (CASException ce) {
				throw new CollectionException(ce);
			}
		} else {
			// shouldn't get here?
			throw new CollectionException("no documents to process",
					new Object[] {});
		}
	}

	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(i, listDocumentIds.size(),
				Progress.ENTITIES) };
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return i < listDocumentIds.size();
	}

	@Override
	public void close() throws IOException {
		this.listDocumentIds = null;
		this.i = 0;
	}

}
