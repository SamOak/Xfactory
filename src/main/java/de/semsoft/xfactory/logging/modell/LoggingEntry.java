package de.semsoft.xfactory.logging.modell;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Logging")
public class LoggingEntry {
	
	public final static String TRIGGER_HTTP = "HTTP";
	public final static String TRIGGER_FILE = "FILE";
	public final static String TRIGGER_GUI  = "GUI";
	
	@Id
	@GeneratedValue
	private Long id;
	
	
	@Column( name = "uuid")
	private String uuid;

	
	@Column( name = "trigger")
	private String trigger;
	
	@Column( name = "starttime" )
	private LocalDateTime  startTime;
	
	@Column( name = "finishtime" )
	private LocalDateTime  finishTime;

	@Column( name = "status")
	private String status;
	
	@Column( name = "info")
	private String info;

	@Column( name = "contentLengthSource")
	private long contentLengthSource;

	@Column( name = "contentLengthTarget")
	private long contentLengthTarget;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
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

