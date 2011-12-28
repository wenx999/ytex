package my.mas.jdl.data.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.xml.parsers.ParserConfigurationException;

import my.mas.jdl.common.FileUtil;
import my.mas.jdl.test.Resources;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(Theories.class)
public final class DomUtilTest {
	@DataPoint
	public static String L1C = Resources.LOAD1C;
	@DataPoint
	public static String L1X = Resources.LOAD1X;
	@DataPoint
	public static String L2C = Resources.LOAD2C;
	@DataPoint
	public static String L2X = Resources.LOAD2X;

	@Test
	public void getEmptyDocument() throws ParserConfigurationException {
		assertThat(DomUtil.getEmptyDocument().getDocumentElement(), nullValue());
	}

	@Test
	public void strToDocument() {
		Document document = DomUtil.strToDocument("<root />");
		Element element = document.getDocumentElement();
		assertThat(element.getTagName(), is("root"));
	}

	@Theory
	public void srcToDocument(String xml) {
		xml = FileUtil.getFile(xml).toString();
		Document document = DomUtil.srcToDocument(xml);
		Element element = document.getDocumentElement();
		assertThat(element.getTagName(), is(Resources.ROOT_LOAD));
	}

	@Theory
	public void nodeToStr(String xml) {
		xml = FileUtil.getFile(xml).toString();
		Document document = DomUtil.srcToDocument(xml);
		Element element = document.getDocumentElement();
		assertThat(StringUtils.startsWith(DomUtil.nodeToStr(element), "<" + Resources.ROOT_LOAD), is(true));
		assertThat(StringUtils.endsWith(DomUtil.nodeToStr(element).trim(), "</" + Resources.ROOT_LOAD + ">"), is(true));
	}
}
