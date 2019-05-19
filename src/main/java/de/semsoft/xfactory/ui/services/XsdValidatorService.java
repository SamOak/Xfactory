package de.semsoft.xfactory.ui.services;

import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

@Service(value = "XsdValidatorService")
public class XsdValidatorService {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(XsdValidatorService.class);

	private String xml = null;
	private String xsd = null;

	private boolean error = false;

	private String errorMessage = null;

	private long duration = 0;

	public XsdValidatorService() {

	}

	public XsdValidatorService(String xml, String xsd) {
		this.xml = xml;
		this.xsd = xsd;
	}

	public boolean process(String xml, String xsd) {
		this.xml = xml;
		this.xsd = xsd;
		return process();
	}

	public boolean process() {

		long start = System.currentTimeMillis();

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {

			Schema schema = schemaFactory.newSchema(new SAXSource(new InputSource(new StringReader(this.xsd))));

			Validator validator = schema.newValidator();
			validator.validate(new SAXSource(new InputSource(new StringReader(this.xml))));

			this.error = false;
			this.errorMessage = "";
			this.duration = (System.currentTimeMillis() - start) / 1000;

			return true;

		} catch (Exception e) {

			this.error = true;
			this.errorMessage = e.getMessage();
			this.duration = (System.currentTimeMillis() - start) / 1000;

			return false;
		}

	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}

	public boolean isError() {
		return error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public long getDuration() {
		return duration;
	}

}
