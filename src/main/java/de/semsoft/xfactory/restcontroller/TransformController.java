package de.semsoft.xfactory.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.semsoft.xfactory.restcontroller.databean.TransformControllerData;
import de.semsoft.xfactory.services.Transform;


@RestController
public class TransformController {
	
	@Autowired
	private Transform transformer;
	
	
	
	@RequestMapping(method = RequestMethod.POST, value="/transform/{xsltname}/json")
	public TransformControllerData transformRESTJSON(@PathVariable(value = "xsltname") String xsltFileName, @Valid @RequestBody String source) {
		
		TransformControllerData data = new TransformControllerData();
		data.setSource(source);
		data.setXsltName(xsltFileName);
		data = transformer.transform(data);
		
		return data;
		
	}	

	
	@RequestMapping(method = RequestMethod.POST, value="/transform/{xsltname}/xml")
	public String transformRESTXML(@PathVariable(value = "xsltname") String xsltFileName, @Valid @RequestBody String source) {
		
		TransformControllerData data = new TransformControllerData();
		data.setSource(source);
		data.setXsltName(xsltFileName);
		data = transformer.transform(data);
		
		return data.getTarget();
		
	}	

	
}
