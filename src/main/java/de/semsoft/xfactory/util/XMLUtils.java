package de.semsoft.xfactory.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import de.semsoft.xfactory.services.file.TransformFile;

public class XMLUtils {


	public static boolean isValidXMLFile (File file ) {
		
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			@SuppressWarnings("unused")
			Document doc = dBuilder.parse(file);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	
	
}
