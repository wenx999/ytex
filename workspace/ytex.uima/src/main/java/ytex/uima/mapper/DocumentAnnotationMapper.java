package ytex.uima.mapper;

import org.apache.uima.jcas.tcas.Annotation;

import ytex.model.Document;
import ytex.model.DocumentAnnotation;

public interface DocumentAnnotationMapper<D extends DocumentAnnotation, T extends Annotation> {

	/**
	 * Default implementation for mapping uima annotation to db annotation:
	 * simply copy whatever properties have the same name.
	 * 
	 * @param anno
	 * @param uimaAnno
	 * @param doc
	 */
	public abstract void mapAnnotationProperties(D anno, Annotation uimaAnno,
			Document doc);
}