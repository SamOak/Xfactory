package de.semsoft.xfactory.services;

import java.io.File;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service(value = "ApplicationControl")
public class ApplicationControl {

	@Value("${xfactory.path.basepath}")
	private String basePath;

	@Value("${xfactory.local.datetime.format}")
	private String dateTimeFormat;

	@Autowired
	private ApplicationContext context;

	private long startupTime;
	private long lastPingTime;
	private long jobCounter;

	private boolean blocked;

	public long getTotalDiskSpace() {
		return new File(basePath).getTotalSpace();
	}

	public long getFreeDiskSpace() {
		return new File(basePath).getFreeSpace();
	}

	public void shutdown() {
		SpringApplication.exit(context, () -> 0);
	}

	public void increaseJobCounter() {
		this.jobCounter++;
		this.lastPingTime = System.currentTimeMillis();
	}

	public void toggleBlocked() {
		this.blocked = this.blocked == true ? false : true;
	}

	public ApplicationControl() {
		startupTime = System.currentTimeMillis();
		lastPingTime = System.currentTimeMillis();
		blocked = false;
		jobCounter = 0;
	}

	public String getStartupTimeString() {
		final SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
		return formatter.format(this.startupTime);
	}

	public String getLastPingTimeString() {
		final SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
		return formatter.format(this.lastPingTime);
	}

	public long getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
	}

	public long getLastPingTime() {
		return lastPingTime;
	}

	public void setLastPingTime(long lastPingTime) {
		this.lastPingTime = lastPingTime;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public long getJobCounter() {
		return jobCounter;
	}

	public void setJobCounter(long jobCounter) {
		this.jobCounter = jobCounter;
	}

}
