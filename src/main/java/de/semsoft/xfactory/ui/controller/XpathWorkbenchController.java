package de.semsoft.xfactory.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.South;
import org.zkoss.zul.Textbox;

import de.semsoft.xfactory.ui.services.XmlBeautifierService;
import de.semsoft.xfactory.ui.services.XpathEvaluatorService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class XpathWorkbenchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Textbox xpathExpression;

	@Wire
	private Textbox sourceXML;

	@Wire
	private Textbox resultText;

	@Wire
	private Image startButton;

	@Wire
	private South southErrorText;

	@Wire
	private Textbox errorText;

	@WireVariable("XpathEvaluatorService")
	private XpathEvaluatorService xPathEvaluator;

	@WireVariable("XmlBeautifierService")
	private XmlBeautifierService xmlBeautifier;

	@Listen("onUpload = #toolbarAddXML")
	public void addFile(UploadEvent event) {

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
		resultText.setValue("");
		errorText.setValue("");

	}

	@Listen("onClick = #toolbarClearXML")
	public void clearSourceXML() {
		sourceXML.setValue("");
		resultText.setValue("");
		errorText.setValue("");
	}

	@Listen("onOK = #xpathExpression")
	public void hitEnter() {
		executeXPath();
	}

	@Listen("onChanging = #xpathExpression")
	public void changeXPath() {
		southErrorText.setOpen(false);
	}

	@Listen("onClick=#toolbarBeautify")
	public void beautify() {

		xmlBeautifier.setXml(sourceXML.getValue());

		if (xmlBeautifier.process() == true) {
			sourceXML.setValue(xmlBeautifier.getResult());
		}

	}

	@Listen("onClick = #toolbarStart")
	public void executeXPath() {

		xPathEvaluator.setXml(sourceXML.getValue());
		xPathEvaluator.setxPath(xpathExpression.getValue());

		if (xPathEvaluator.process() == true) {
			resultText.setValue(xPathEvaluator.getResult());
			errorText.setValue("");
		} else {
			southErrorText.setOpen(true);
			errorText.setValue(xPathEvaluator.getErrorMessage());
		}

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);

	}

}
