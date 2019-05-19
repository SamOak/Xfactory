package de.semsoft.xfactory.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Component;

import de.semsoft.xfactory.logging.modell.LoggingEntry;
import de.semsoft.xfactory.services.interfaces.ITransformMetric;

@Component
public class TransformMetric implements ITransformMetric {

	public final static String STATUS_ERROR = "ERROR";
	public final static String STATUS_OK = "OK";

	private String uuid;
	private LocalDateTime startTime = null;
	private LocalDateTime finishTime = null;
	private String status = null;
	private String info = null;

	private long contentLengthSource = 0;
	private long contentLengthTarget = 0;

	public TransformMetric() {
		uuid = UUID.randomUUID().toString();
		this.startTime = LocalDateTime.now();
		this.status = TransformMetric.STATUS_OK;
	}

	@Override
	public void start() {
		startTime = LocalDateTime.now();
		this.status = STATUS_OK;
	}

	@Override
	public void finish() {
		finish(null);
	}

	public void finish(String status) {

		if (status != null) {
			this.status = status;
		}
		finishTime = LocalDateTime.now();
	}

	public LoggingEntry getLoggingEntry(String trigger) {

		final LoggingEntry loggingEntry = new LoggingEntry();
		loggingEntry.setUuid(uuid);
		loggingEntry.setTrigger(trigger);
		loggingEntry.setStartTime(startTime);
		loggingEntry.setFinishTime(finishTime);
		loggingEntry.setStatus(status);
		loggingEntry.setInfo(info);
		loggingEntry.setContentLengthSource(contentLengthSource);
		loggingEntry.setContentLengthTarget(contentLengthTarget);

		return loggingEntry;

	}

	@Override
	public long getDuration() {
		return ChronoUnit.MICROS.between(startTime, finishTime);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalDateTime finishTime) {
		this.finishTime = finishTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getContentLengthSource() {
		return contentLengthSource;
	}

	public void setContentLengthSource(long contentLengthSource) {
		this.contentLengthSource = contentLengthSource;
	}

	public long getContentLengthTarget() {
		return contentLengthTarget;
	}

	public void setContentLengthTarget(long contentLengthTarget) {
		this.contentLengthTarget = contentLengthTarget;
	}

}
