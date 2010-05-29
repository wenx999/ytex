package ytex.uima.mapper;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.XMLSerializer;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ytex.model.Document;
import ytex.model.DocumentAnnotation;
import ytex.model.UimaType;

public class DocumentMapperServiceImpl implements DocumentMapperService,
		InitializingBean {
	private static final Log log = LogFactory
			.getLog(DocumentMapperServiceImpl.class);
	private SessionFactory sessionFactory;
	private PlatformTransactionManager transactionManager;

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * date format for analysis batch.
	 */
	private static final ThreadLocal<DateFormat> tlAnalysisBatchDateFormat = new ThreadLocal<DateFormat>() {
		public DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};
	/**
	 * map of uima annotation class name to mapper class name
	 */
	private Map<String, String> documentAnnotationMappers;
	/**
	 * thread local cache of mappers. instantiate them on-demand so that we
	 * don't run into trouble with trying to access Uima Annotations that are
	 * not in the type system.
	 */
	private static final ThreadLocal<Map<String, AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation>>> mappers = new ThreadLocal<Map<String, AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation>>>() {
		@Override
		protected Map<String, AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation>> initialValue() {
			return new HashMap<String, AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation>>();
		}
	};

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private String getDefaultAnalysisBatch() {
		return tlAnalysisBatchDateFormat.get().format(new Date());
	}

	private Document createDocument(JCas jcas, String analysisBatch) {
		Document doc = new Document();
		doc.setDocText(jcas.getDocumentText());
		doc.setAnalysisBatch(analysisBatch == null
				|| analysisBatch.length() == 0 ? getDefaultAnalysisBatch()
				: analysisBatch);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream zipOut = new GZIPOutputStream(out);
			XmiCasSerializer ser = new XmiCasSerializer(jcas.getTypeSystem());
			XMLSerializer xmlSer = new XMLSerializer(zipOut, false);
			ser.serialize(jcas.getCas(), xmlSer.getContentHandler());
			zipOut.close();
			doc.setCas(out.toByteArray());
		} catch (Exception saxException) {
			log.error("error serializing document cas", saxException);
		}
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ytex.dao.mapper.DocumentMapperService#saveDocument(org.apache.uima.jcas
	 * .JCas, java.lang.String)
	 */
	public Integer saveDocument(JCas jcas, String analysisBatch) {
		Document doc = createDocument(jcas, analysisBatch);
		this.sessionFactory.getCurrentSession().save(doc);
		AnnotationIndex annoIdx = jcas
				.getAnnotationIndex(Annotation.typeIndexID);
		FSIterator annoIterator = annoIdx.iterator();
		while (annoIterator.hasNext()) {
			saveDocumentAnnotation((Annotation) annoIterator.next(), doc);
		}
		return doc.getDocumentID();
	}

	/**
	 * save an annotation.
	 * 
	 * @param annotation
	 *            uima annotation
	 * @param document
	 *            document to which it belongs
	 * @return null if the annotation is not mapped
	 */
	private DocumentAnnotation saveDocumentAnnotation(Annotation annotation,
			Document document) {
		AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation> mapper = this
				.getMapperForAnnotation(annotation.getClass().getName());
		if (mapper != null) {
			DocumentAnnotation docAnno = (DocumentAnnotation) mapper
					.mapAnnotation(annotation, document, this
							.getSessionFactory());
			return docAnno;
		} else {
			return null;
		}
	}

	/**
	 * instantiate/get mapper for given uima annotation
	 * 
	 * @param uimaClassName
	 *            uima annotation
	 * @return mapper or null if not mapped
	 */
	@SuppressWarnings("unchecked")
	private AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation> getMapperForAnnotation(
			String uimaClassName) {
		AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation> mapper = null;
		String mapperClassName = this.documentAnnotationMappers
				.get(uimaClassName);
		if (mapperClassName != null) {
			mapper = mappers.get().get(mapperClassName);
			if (mapper == null) {
				try {
					mapper = (AbstractDocumentAnnotationMapper<? extends DocumentAnnotation, ? extends Annotation>) Class
							.forName(mapperClassName).newInstance();
					mappers.get().put(mapperClassName, mapper);
				} catch (Exception e) {
					log.error("Error instantiating mapper: " + mapperClassName,
							e);
					throw new RuntimeException(e);
				}
			}
		}
		return mapper;

	}

	/**
	 * load the map of uima annotation class name to mapper class name from the
	 * database.
	 * 
	 * For some reason this is not getting executed within a transaction.
	 * Manually wrap the db access in a transaction.
	 * 
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() {
		documentAnnotationMappers = new HashMap<String, String>();
		TransactionTemplate txTemplate = new TransactionTemplate(this
				.getTransactionManager());
		txTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		txTemplate.execute(new TransactionCallback<Object>() {

			@Override
			public Object doInTransaction(TransactionStatus arg0) {
				Query q = getSessionFactory().getCurrentSession()
						.getNamedQuery("getUimaTypes");
				List<UimaType> uimaTypes = q.list();
				for (UimaType uimaType : uimaTypes) {
					documentAnnotationMappers.put(uimaType.getUimaTypeName(),
							uimaType.getMapperName());
				}
				return null;
			}
		});
		//		
		// documentAnnotationMappers.put(Sentence.type,
		// new DocumentAnnotationMapper<SentenceAnnotation, Sentence>(
		// SentenceAnnotation.class, Sentence.class));
		// documentAnnotationMappers.put(Segment.type,
		// new DocumentAnnotationMapper<SegmentAnnotation, Segment>(
		// SegmentAnnotation.class, Segment.class));
		// documentAnnotationMappers
		// .put(
		// DocumentKey.type,
		// new DocumentAnnotationMapper<DocumentKeyAnnotation, DocumentKey>(
		// DocumentKeyAnnotation.class, DocumentKey.class));
		// documentAnnotationMappers
		// .put(
		// DocumentDate.type,
		// new DocumentAnnotationMapper<DocumentDateAnnotation, DocumentDate>(
		// DocumentDateAnnotation.class,
		// DocumentDate.class));
		// documentAnnotationMappers.put(NamedEntity.type,
		// new NamedEntityDocumentAnnotationMapper());
		// // the context-dependent types do not specify any attributes beyond
		// // those of the
		// // plain-vanilla Annotation
		// // use DocumentAnnotation class to persist these annotations
		// documentAnnotationMappers.put(RomanNumeralAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(FractionAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(DateAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(TimeAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(RangeAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(MeasurementAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(PersonTitleAnnotation.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
		// documentAnnotationMappers.put(DocumentTitle.type,
		// new DocumentAnnotationMapper<DocumentAnnotation, Annotation>(
		// DocumentAnnotation.class, Annotation.class));
	}

}
