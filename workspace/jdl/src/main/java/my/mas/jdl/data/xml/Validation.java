package my.mas.jdl.data.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import my.mas.jdl.common.FileUtil;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Validation between Schema and XML.
 * 
 * @author mas
 */
public class Validation {
	private Document document;
	private String srcXml;
	private Validator validator;
	private String error;

	/**
	 * @param schema
	 *            the schema
	 */
	public Validation(final Schema schema) {
		setSchema(schema);
	}

	/**
	 * @param srcXsd
	 *            the srcXsd
	 */
	public Validation(final String srcXsd) {
		setSchema(srcXsd);
	}

	/**
	 * @param schema
	 *            the schema
	 * @param document
	 *            the document
	 */
	public Validation(final Schema schema, final Document document) {
		setSchema(schema);
		setDocument(document);
	}

	/**
	 * @param schema
	 *            the schema
	 * @param srcXml
	 *            the srcXsd
	 */
	public Validation(final Schema schema, final String srcXml) {
		setSchema(schema);
		setDocument(srcXml);
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public final void setSchema(final Schema schema) {
		validator = schema.newValidator();
		error = null;
	}

	/**
	 * @param srcXsd
	 *            the srcXsd to set
	 */
	public final void setSchema(final String srcXsd) {
		setSchema(SchemaUtil.srcToSchema(srcXsd));
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public final void setDocument(final Document document) {
		this.document = document;
		error = null;
		srcXml = null;
	}

	/**
	 * @param srcXml
	 *            the srcXml to set
	 */
	public final void setDocument(final String srcXml) {
		DomUtil.srcToDocument(FileUtil.getFile(srcXml).toString());
		this.srcXml = srcXml;
		error = null;
		document = null;
	}

	/**
	 * @return the error
	 */
	public final String getError() {
		return error;
	}

	private boolean succeed(final Source source) {
		try {
			validator.validate(source);
			error = null;
			return true;
		} catch (SAXException e) {
			error = e.getMessage();
		} catch (IOException e) {
			error = e.getMessage();
		}
		return false;
	}

	private boolean domSucceed() {
		return succeed(new DOMSource(document));
	}

	private boolean saxSucceed() {
		try {
			return succeed(new SAXSource(new InputSource(new FileInputStream(srcXml))));
		} catch (FileNotFoundException e) {
			error = e.getMessage();
		}
		return false;
	}

	/**
	 * @return result of validation
	 */
	public final boolean succeed() {
		return (document == null) ? saxSucceed() : domSucceed();
	}
}
