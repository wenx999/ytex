package ytex.uima.annotators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

public class DBConsumer extends JCasAnnotator_ImplBase {
	private static final Log log = LogFactory.getLog(DBConsumer.class);
	private DocumentMapperService documentMapperService;
	private String xmiOutputDirectory;
	private String analysisBatch;

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		xmiOutputDirectory = (String) aContext
				.getConfigParameterValue("xmiOutputDirectory");
		analysisBatch = (String) aContext
				.getConfigParameterValue("analysisBatch");
		documentMapperService = (DocumentMapperService) ApplicationContextHolder
				.getApplicationContext().getBean("documentMapperService");
	}

	@Override
	public void process(JCas jcas) {
		Integer documentID = documentMapperService.saveDocument(jcas,
				analysisBatch);
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
					XmiCasSerializer ser = new XmiCasSerializer(jcas
							.getTypeSystem());
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
