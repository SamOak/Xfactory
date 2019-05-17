package de.semsoft.xfactory.ui.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

@Service(value="XmlBeautifierService")
public class XmlBeautifierService {


	  private static final Logger LOG = LoggerFactory.getLogger(XmlBeautifierService.class);
	
	  private String xml = null;
      private boolean error = false; 
       
      private String result = null;
      private String errorMessage = null;
       
      private long duration = 0;
      
      public XmlBeautifierService() {
             
      }
      public XmlBeautifierService(String xml ) {
            this.xml = xml;
      }

      public boolean process(String xml ) {
            this.xml = xml;
           return process();
      }
      public boolean process() {

    	  long start = System.currentTimeMillis();
    	  this.errorMessage = "";
    	  this.error = false;
    	  
    	  try {
    		  Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();

    		  serializer.setOutputProperty(OutputKeys.INDENT, "yes");
    		  serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    		  serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    		  serializer.setOutputProperty("{http://xml.customer.org/xslt}indent-amount", "2");
    		  
    		  Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(this.xml.getBytes())));
    		  StreamResult res = new StreamResult(new ByteArrayOutputStream());

    		  serializer.transform(xmlSource, res);

    		  this.result =  new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
    		  this.duration = (System.currentTimeMillis() - start);
    		  
    	  } catch (Exception e) {
    		  this.errorMessage = e.getMessage();
    		  this.error = true;
    		  this.duration = (System.currentTimeMillis() - start);
              return false;
    	  }
    	  
          return true;
          
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
