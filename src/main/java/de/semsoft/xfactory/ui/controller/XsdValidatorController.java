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
import de.semsoft.xfactory.ui.services.XsdValidatorService;



@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class XsdValidatorController extends SelectorComposer<Component> {
 
    private static final long serialVersionUID = 1L;
    
    @Wire
    private Textbox sourceXML;
   
    @Wire
    private Textbox xsdContent;
    
    @Wire
    private Textbox resultText;
    
        
    @WireVariable("XmlBeautifierService")
    private XmlBeautifierService xmlBeautifier;
    
    @WireVariable("XsdValidatorService")
    private XsdValidatorService xsdValidator;
      
    

    @Listen("onUpload = #toolbarAddXML")
    public void addXMLFile(UploadEvent event){
    	
    	String xml = "";
    	
    	if(event.getMedia().getContentType().startsWith("text") ) {
    		xml = event.getMedia().getStringData();
    	} else {
    		// read stream... 
    		InputStream inputStream = event.getMedia().getStreamData();
    		try {
				xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
			} catch (IOException e) {
				Messagebox.show("Cannot download data .... (" + e.getMessage() + ")");
				clearSourceXML();
				e.printStackTrace();
			}
    	}
    	sourceXML.setValue(xml);
    	resultText.setValue("");
   	
    }  

    
    @Listen("onUpload = #toolbarAddXSD")
    public void addXSDFile(UploadEvent event){
    	
    	String xml = "";
    	
    	if(event.getMedia().getContentType().startsWith("text") ) {
    		xml = event.getMedia().getStringData();
    	} else {
    		// read stream... 
    		InputStream inputStream = event.getMedia().getStreamData();
    		try {
				xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
			} catch (IOException e) {
				Messagebox.show("Cannot download data .... (" + e.getMessage() + ")");
				clearSourceXML();
				e.printStackTrace();
			}
    	}
    	xsdContent.setValue(xml);
    	resultText.setValue("");
   	
    }  

    
    @Listen("onClick = #toolbarClearXML")
    public void clearSourceXML() {
    	sourceXML.setValue("");
    	xsdContent.setValue("");
    	resultText.setValue("");
    }    
    
    @Listen("onClick = #toolbarStart")
    public void verify() {
	
    	xsdValidator.setXml(sourceXML.getValue());
    	xsdValidator.setXsd(xsdContent.getValue());
    	
    	xsdValidator.process();
    	resultText.setValue(xsdValidator.getErrorMessage());
    	
    	
    }
    
    
    @Listen("onClick = #toolbarBeautifyXML")
    public void beautifyXML() {
    	
    	xmlBeautifier.setXml(sourceXML.getValue());
    	
    	if( xmlBeautifier.process() == true ) {
    		sourceXML.setValue(xmlBeautifier.getResult());
    	} 
    	
	
    }
    
    @Listen("onClick = #toolbarBeautifyXSD")
    public void beautifyXSD() {

    	xmlBeautifier.setXml(xsdContent.getValue());
    	
    	if( xmlBeautifier.process() == true ) {
    		xsdContent.setValue(xmlBeautifier.getResult());
    	} 
    	
    }
    
    
	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
	

	}
    

	
	
}

