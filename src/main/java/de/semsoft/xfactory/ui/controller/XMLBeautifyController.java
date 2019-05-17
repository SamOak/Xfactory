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



@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class XMLBeautifyController extends SelectorComposer<Component> {
 
    private static final long serialVersionUID = 1L;
    
    @Wire
    private Textbox sourceXML;
   
    @Wire
    private Textbox resultText;
    
    @Wire
    private South southErrorText;
    
    @Wire
    private Textbox errorText;
    
    
    @WireVariable("XmlBeautifierService")
    private XmlBeautifierService xmlBeautifier;
    

    @Listen("onUpload = #toolbarAddXML")
    public void addFile(UploadEvent event){
    	
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
    	errorText.setValue("");
    	
    }  
       
    @Listen("onClick = #toolbarClearXML")
    public void clearSourceXML() {
    	sourceXML.setValue("");
    	resultText.setValue("");
    	errorText.setValue("");
    }    
    
    @Listen("onClick = #toolbarStart")
    public void executeBeautify() {

    	xmlBeautifier.setXml(sourceXML.getValue());
    	southErrorText.setOpen(false);
    	
    	if( xmlBeautifier.process() == true ) {
    		resultText.setValue(xmlBeautifier.getResult());
    		errorText.setValue("");
    		errorText.setValue("Info: beautified in (" + xmlBeautifier.getDuration() + ") milliseconds");
    	} else {
    		southErrorText.setOpen(true);
    		errorText.setValue(xmlBeautifier.getErrorMessage());
    	}
    	
	
    }
    
    
	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
	

	}
    

	
	
}

