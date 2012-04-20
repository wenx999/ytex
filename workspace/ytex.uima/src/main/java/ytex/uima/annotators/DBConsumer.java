package ytex.uima.annotators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.SAXException;

import ytex.uima.ApplicationContextHolder;
import ytex.uima.mapper.DocumentMapperService;

/**
 * Store the document text, cas, and annotations in the database. Delegates to
 * DocumentMapperService. This is an annotator and not a consumer because
 * according to the uima docs the Consumer interface is deprecated. Config
 * parameters:
 * <ul>
 * <li>xmiOutputDirectory - String - directory where the xmi serialized cas
 * should be stored. Leave empty if you don't want to store the xmi. Defaults to
 * empty.
 * <li>analysisBatch - String - Document group/analysis batch, stored in
 * document.analysis_batch. Defaults to current date/time.
 * <li>storeDocText - boolean - should the document text be stored in the DB?
 * defaults to true
 * <li>storeCAS - boolean - should the serialized xmi cas be stored in the DB?
 * defaults to true
 * <li>typesToIngore - multivalued String - uima types not to be saved.
 * </ul>
 * 
 * @author vijay
 * 
 */
public class DBConsumer extends JCasAnnotator_ImplBase {
	private static final Log log = LogFactory.getLog(DBConsumer.class);
	private DocumentMapperService documentMapperService;
	private String xmiOutputDirectory;
	private String analysisBatch;
	private boolean bStoreDocText;
	private boolean bStoreCAS;
	private Set<String> setTypesToIgnore = new HashSet<String>();
	/**
	 * read config parameters
	 */
	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		xmiOutputDirectory = (String) aContext
				.getConfigParameterValue("xmiOutputDirectory");
		analysisBatch = (String) aContext
				.getConfigParameterValue("analysisBatch");
		Boolean boolStoreDocText = (Boolean) aContext
				.getConfigParameterValue("storeDocText");
		Boolean boolStoreCAS = (Boolean) aContext
				.getConfigParameterValue("storeCAS");
		String typesToIgnore[] = (String[]) aContext
				.getConfigParameterValue("typesToIgnore");
		if (typesToIgnore != null)
			setTypesToIgnore.addAll(Arrays.asList(typesToIgnore));
		bStoreDocText = boolStoreDocText == null ? true : boolStoreDocText
				.booleanValue();
		bStoreCAS = boolStoreCAS == null ? true : boolStoreCAS.booleanValue();
		documentMapperService = (DocumentMapperService) ApplicationContextHolder
				.getApplicationContext().getBean("documentMapperService");
	}

	/**
	 * call the documentMapperService to save the document. if the
	 * xmiOutputDirectory is defined, write the document to an xmi file. use the
	 * name corresponding to the documentID.
	 */
	@Override
	public void process(JCas jcas) {
		Integer documentID = documentMapperService.saveDocument(jcas,
				analysisBatch, bStoreDocText, bStoreCAS, setTypesToIgnore);
		if (documentID != null && xmiOutputDirectory != null
				&& xmiOutputDirectory.length() > 0) {
			File dirOut = new File(xmiOutputDirectory);
			if (!dirOut.exists() && !dirOut.isDirectory()) {
				log.error(xmiOutputDirectory + " does not exist");
			} else {
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(
							xmiOutputDirectory + File.separatorChar
									+ documentID.toString() + ".xmi"));
					XmiCasSerializer ser = new XmiCasSerializer(
							jcas.getTypeSystem());
					XMLSerializer xmlSer = new XMLSerializer(writer, false);
					ser.serialize(jcas.getCas(), xmlSer.getContentHandler());
				} catch (IOException e) {
					log.error("error writing xmi, documentID=" + documentID, e);
				} catch (SAXException e) {
					log.error("error writing xmi, documentID=" + documentID, e);
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}
	}

}
