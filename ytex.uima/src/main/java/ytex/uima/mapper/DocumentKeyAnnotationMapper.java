package ytex.uima.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.jcas.tcas.Annotation;

import ytex.model.Document;
import ytex.vacs.model.DocumentKeyAnnotation;
import ytex.vacs.model.DocumentType;
import ytex.vacs.uima.types.DocumentKey;

public class DocumentKeyAnnotationMapper extends
		AbstractDocumentAnnotationMapper<DocumentKeyAnnotation, DocumentKey> {
	private static final Log log = LogFactory
			.getLog(DocumentKeyAnnotationMapper.class);

	DocumentKeyAnnotationMapper() {
		super(DocumentKeyAnnotation.class, DocumentKey.class);
	}

	@Override
	public void mapAnnotationProperties(DocumentKeyAnnotation anno,
			Annotation uimaAnno, Document doc) {
		DocumentKey docKey = (DocumentKey) uimaAnno;
		anno.setStudyID(docKey.getStudyID());
		anno.setUid(docKey.getUid());
		int docTypeId = docKey.getDocumentType();
		if (docTypeId < DocumentType.values().length) {
			anno.setDocumentType(DocumentType.values()[docTypeId]);
		} else {
			log.warn("invalid doc type, using progress note as document type.");
			anno.setDocumentType(DocumentType.PROGRESS_NOTE);
		}
		anno.setSiteID(docKey.getSiteID() != null ? docKey.getSiteID() : "");
	}

}
