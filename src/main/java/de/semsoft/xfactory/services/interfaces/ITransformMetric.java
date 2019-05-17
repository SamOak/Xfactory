package de.semsoft.xfactory.services.interfaces;

import java.time.LocalDateTime;

public interface ITransformMetric {
	
	public void start();
	public void finish();
	
	public long getDuration();

}
