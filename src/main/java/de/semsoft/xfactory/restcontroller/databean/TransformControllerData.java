package de.semsoft.xfactory.restcontroller.databean;

import de.semsoft.xfactory.services.TransformMetric;

public class TransformControllerData {

	private String source;
	private String target;
	private String xsltName;
	private TransformMetric metric;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getXsltName() {
		return xsltName;
	}

	public void setXsltName(String xsltName) {
		this.xsltName = xsltName;
	}

	public TransformMetric getMetric() {
		return metric;
	}

	public void setMetric(TransformMetric metric) {
		this.metric = metric;
	}

}
