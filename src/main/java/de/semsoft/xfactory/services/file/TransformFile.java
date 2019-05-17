package de.semsoft.xfactory.services.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.StreamSupport;
import org.apache.commons.io.FileUtils;
import ch.qos.logback.core.util.FileUtil;
import de.semsoft.xfactory.util.FileSemaphore;


public class TransformFile extends FileSemaphore{


	private String area;
	private String slot;
	private String fileName;
	private String fullFileName;
	private long   age;
	private int bytesRead;
	
	
	public TransformFile() {
		
	}
	public TransformFile(String fileName) {
		this.file = new File(fileName);
		setAttributes();
	}
	public TransformFile(File file) {
		this.file = file;
		setAttributes();
	}

	private void setAttributes() {
		if( this.file != null ) {
			this.fileName = this.file.getName();
			this.fullFileName = this.file.getAbsolutePath();
			String[] dirs = splitPath(this.file.getAbsolutePath());
			if( dirs.length >= 2) {
				this.slot = dirs[dirs.length-2];
				this.area = dirs[dirs.length-3];
			}
			this.age = (new Date().getTime() - this.file.lastModified());
		}
	}
	
	private String[] splitPath(String pathString) {
		Path path = Paths.get(pathString);
	    return StreamSupport.stream(path.spliterator(), false).map(Path::toString).toArray(String[]::new);
	}
	
	public Path getPath() {
		return Paths.get(this.fullFileName);
		
	}
	
	public void delete() {
		new File(this.getFullFileName()).delete();
	}
	
	public TransformFile getOriginalFile() {

		for (String status : TransformFile.STATUSLIST) {
			if( this.fullFileName.endsWith(status) ) {
				String orgFileName = this.fullFileName.substring(0, this.fullFileName.length() - status.length() - 1 );
				return new TransformFile(orgFileName);
			}
		}
		return null;
	}
	
	public String getContent() throws IOException {
		return  FileUtils.readFileToString(this.file, StandardCharsets.UTF_8);
	}
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFullFileName() {
		return fullFileName;
	}
	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public long getAgeSecond() {
		return age;
	}
	public long getAgeMilliSecond() {
		return age/1000;
	}
	

	
	
}


