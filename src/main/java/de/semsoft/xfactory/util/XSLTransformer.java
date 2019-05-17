package de.semsoft.xfactory.util;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;

import org.w3c.dom.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class XSLTransformer {
	
  private TransformerFactory factory;
  private String errorMessage;


public XSLTransformer() {
	System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
	// -Djava.xml.transform.TransformerFactory=net.sf.saxon.TransformerFactoryImpl
    factory =  TransformerFactory.newInstance();
  }

  public void process(Reader xmlFile, Reader xslFile,
                      Writer output)
                throws TransformerException {
    process(new StreamSource(xmlFile),
            new StreamSource(xslFile),
            new StreamResult(output));
  }

  public void process(File xmlFile, File xslFile,
                      Writer output)
                throws TransformerException {
    process(new StreamSource(xmlFile),
            new StreamSource(xslFile),
            new StreamResult(output));
  }

  public void process(File xmlFile, File xslFile,
                      OutputStream out)
                 throws TransformerException {
    process(new StreamSource(xmlFile),
            new StreamSource(xslFile),
            new StreamResult(out));
  }
 
  public void process(Source xml, Source xsl, Result result)
                throws TransformerException {
    try {
      Templates template = factory.newTemplates(xsl);
      Transformer transformer = template.newTransformer();
      transformer.transform(xml, result);
    } catch(TransformerConfigurationException tce) {
        throw new TransformerException(
                    tce.getMessageAndLocation());
    } catch (TransformerException te) {
      throw new TransformerException(
                  te.getMessageAndLocation());
    }
  }
  
  public String process(String xml, File xslFileName) {
		
	  try {
			return process(xml, new String(Files.readAllBytes(xslFileName.toPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
  }

  	
  public String process(String xml, String xsl) {
	  
	  String result = null;
	  try {
		    StringReader reader = new StringReader(xml);
		    StringWriter writer = new StringWriter();
		    TransformerFactory tFactory =  new net.sf.saxon.TransformerFactoryImpl();
		    Transformer transformer = tFactory.newTransformer(
		            new javax.xml.transform.stream.StreamSource(new StringReader(xsl)));
	
		    transformer.transform(
		            new javax.xml.transform.stream.StreamSource(reader), 
		            new javax.xml.transform.stream.StreamResult(writer));
	
		    result = writer.toString();
		} catch (Exception e) {
		    this.errorMessage = e.getMessage();
		    result = null;
		}
	  
	  	return result;
  }
  
  
  public String doc2String(Document doc) {
	  

	  try {
		  StringWriter writer = new StringWriter();
		  
		  TransformerFactory transformerFactory = TransformerFactory.newInstance();
		  Transformer transformer = transformerFactory.newTransformer();
		  
		  transformer.transform(new DOMSource(doc), new javax.xml.transform.stream.StreamResult(writer));
		  return writer.getBuffer().toString();
		  
		} catch (Exception e) {
			e.printStackTrace();
		}
	  
	  return null;
	  
  }
  
  

  public String getErrorMessage() {
	return errorMessage;
  } 
  
}