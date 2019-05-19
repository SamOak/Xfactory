package de.semsoft.xfactory.ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;

public class LibFile {

	private String fileName;
	private long fileSize;
	private String fileSizeFormated;
	private String timestamp;
	private long lastModified;

	private File file;

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public LibFile() {
	}

	public LibFile(File file) {
		this.file = file;
		this.fileName = file.getName();
		this.fileSize = file.length();
		this.fileSizeFormated = FileUtils.byteCountToDisplaySize(this.fileSize);
		this.timestamp = sdf.format(file.lastModified());
		this.lastModified = file.lastModified();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFileSizeFormated() {
		return fileSizeFormated;
	}

	public void setFileSizeFormated(String fileSizeFormated) {
		this.fileSizeFormated = fileSizeFormated;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastMofified) {
		this.lastModified = lastMofified;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getContent() {
		try {
			return new String(Files.readAllBytes(Paths.get(this.file.getAbsolutePath())), StandardCharsets.UTF_8);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
