package de.semsoft.xfactory.ui.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Service(value = "XpathEvaluatorService")
public class XpathEvaluatorService {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(XpathEvaluatorService.class);

	private String xml = null;
	private boolean error = false;
	private String xPath = null;

	private String result = null;
	private String errorMessage = null;

	private long duration = 0;

	public XpathEvaluatorService() {

	}

	public XpathEvaluatorService(String xPath, String xml) {
		this.xPath = xPath;
		this.xml = xml;
	}

	public boolean process(String xPath, String xml) {
		this.xPath = xPath;
		this.xml = xml;
		return process();
	}

	public boolean process() {

		long start = System.currentTimeMillis();

		InputStream inputStream;
		Document xmlDocument;
		XPath xPathObj;

		try {

			inputStream = new ByteArrayInputStream(this.xml.getBytes());

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			xmlDocument = builder.parse(inputStream);

			xPathObj = XPathFactory.newInstance().newXPath();

		} catch (Exception eBuildPath) {
			this.errorMessage = eBuildPath.getMessage();
			this.error = true;
			this.result = "";
			this.duration = (System.currentTimeMillis() - start) / 1000;
			return false;
		}
		try {
			NodeList nodeList = (NodeList) xPathObj.compile(this.xPath).evaluate(xmlDocument, XPathConstants.NODESET);
			this.result = nodeListToString(nodeList);

		} catch (Exception eEvaluate) {
			this.errorMessage = eEvaluate.getMessage();
			this.error = true;
			this.result = "";
			this.duration = (System.currentTimeMillis() - start) / 1000;
			return false;
		}

		this.duration = (System.currentTimeMillis() - start) / 1000;
		return true;
	}

	private String nodeListToString(NodeList nodes) {
		DOMSource source = new DOMSource();
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			for (int i = 0; i < nodes.getLength(); ++i) {
				source.setNode(nodes.item(i));
				transformer.transform(source, result);
				writer.append('\n');
			}

		} catch (Exception e) {
			return "";
		}

		return writer.toString();
	}

	public long getDuration() {
		return duration;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public boolean isError() {
		return error;
	}

	public String getResult() {
		return result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
