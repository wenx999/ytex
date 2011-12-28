package my.mas.jdl.data.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * To parse DOM.
 * 
 * @author mas
 */
public class DomParser {
	private String srcXml;
	private Document dom;

	/**
	 * @param srcXml
	 *            the srcXml
	 */
	public DomParser(final String srcXml) {
		setSrcXml(srcXml);
	}

	/**
	 * DOM parser.
	 */
	private final void parse() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		// factory.setValidating(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			dom = builder.parse(srcXml);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the srcXml
	 */
	public final String getSrcXml() {
		return srcXml;
	}

	/**
	 * @param srcXml
	 *            the srcXml to set
	 */
	public final void setSrcXml(final String srcXml) {
		this.srcXml = srcXml;
		parse();
	}

	/**
	 * @return the root
	 */
	public final Element getRoot() {
		return dom.getDocumentElement();
	}
}
