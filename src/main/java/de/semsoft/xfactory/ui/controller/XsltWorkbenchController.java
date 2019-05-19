package de.semsoft.xfactory.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import de.semsoft.xfactory.ui.services.XmlBeautifierService;
import de.semsoft.xfactory.util.XSLTransformer;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class XsltWorkbenchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Textbox sourceXML;

	@Wire
	private Textbox xslContent;

	@Wire
	private Textbox infoText;

	@WireVariable("XmlBeautifierService")
	private XmlBeautifierService xmlBeautifier;

	@Listen("onUpload = #toolbarAddXML")
	public void addXMLFile(UploadEvent event) {

		String xml = "";

		if (event.getMedia().getContentType().startsWith("text")) {
			xml = event.getMedia().getStringData();
		} else {
			// read stream...
			final InputStream inputStream = event.getMedia().getStreamData();
			try {
				xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
			} catch (final IOException e) {
				Messagebox.show("Cannot download data .... (" + e.getMessage() + ")");
				clearSourceXML();
				e.printStackTrace();
			}
		}
		sourceXML.setValue(xml);
		infoText.setValue("");

	}

	@Listen("onUpload = #toolbarAddXSL")
	public void addXSDFile(UploadEvent event) {

		String xml = "";

		if (event.getMedia().getContentType().startsWith("text")) {
			xml = event.getMedia().getStringData();
		} else {
			// read stream...
			final InputStream inputStream = event.getMedia().getStreamData();
			try {
				xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
			} catch (final IOException e) {
				Messagebox.show("Cannot download data .... (" + e.getMessage() + ")");
				clearSourceXML();
				e.printStackTrace();
			}
		}
		xslContent.setValue(xml);
		infoText.setValue("");

	}

	@Listen("onClick = #toolbarClearXML")
	public void clearSourceXML() {
		sourceXML.setValue("");
		xslContent.setValue("");
		infoText.setValue("");
	}

	@Listen("onClick = #toolbarBeautifyXML")
	public void beautifyXML() {

		xmlBeautifier.setXml(sourceXML.getValue());

		if (xmlBeautifier.process() == true) {
			sourceXML.setValue(xmlBeautifier.getResult());
		}

	}

	@Listen("onClick = #toolbarBeautifyXSL")
	public void beautifyXSL() {

		xmlBeautifier.setXml(xslContent.getValue());

		if (xmlBeautifier.process() == true) {
			xslContent.setValue(xmlBeautifier.getResult());
		}

	}

	@Listen("onClick = #toolbarStart")
	public void transform() {

		final XSLTransformer transformer = new XSLTransformer();

		String result = transformer.process(sourceXML.getValue(), xslContent.getValue());

		if (result != null) {

			xmlBeautifier.setXml(result);
			if (xmlBeautifier.process() == true) {
				result = xmlBeautifier.getResult();
			}
			infoText.setValue("");

			final HashMap<String, String> map = new HashMap<String, String>();
			map.put("content", result);
			map.put("filename", "Result...");

			Executions.createComponents("~./zul/ViewPopup.zul", null, map);
		} else {
			infoText.setValue(transformer.getErrorMessage());
		}

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);

	}

}
