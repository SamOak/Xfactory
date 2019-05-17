package de.semsoft.xfactory.services.file.worker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.semsoft.xfactory.services.file.TransformFile;
import de.semsoft.xfactory.services.file.TransformFileService;

@Component
public class Cleaner {

    private static final Logger LOG = LoggerFactory.getLogger(Cleaner.class);

	@Value("${xfactory.path.basepath}")
	private String basePath;
	
	@Value("${xfactory.filter.done.regex}")
	private String fileFilterRegexDone;
	
	@Value("${xfactory.filter.job.regex}")
	private String fileFilterRegexJob;
	
	@Value("${xfactory.cleaner.job.maxdelay}")
	private int jobMaxDelay;
	
	@Value("${xfactory.path.area.in}")
	private String inAreaName;

	@Value("${xfactory.path.area.done}")
	private String doneAreaName;
		
	@Autowired
	private TransformFileService transformFileService;
	
	@Scheduled(fixedRateString = "${xfactory.cleaner.movetodone.sleep}")
    public void cleanINArea() {
        //LOG.info("Clean finshed files .... ");
        
        // move done files 
        Collection<TransformFile> doneFileList = transformFileService.getFiles(inAreaName, fileFilterRegexDone);
		for (TransformFile doneFile : doneFileList) {
			
			LOG.info("Found (" + doneFile.getFileName() + ") as done");
			try {
				TransformFile orgFile = doneFile.getOriginalFile();
				
				Files.createDirectories(Paths.get(basePath + "/" + doneAreaName + "/" + orgFile.getSlot()));
				Files.move(orgFile.getPath(),  Paths.get(basePath + "/" + doneAreaName + "/" + orgFile.getSlot() + "/" + orgFile.getFileName()) );
				doneFile.delete();
				LOG.info("File (" + doneFile.getFileName() + ") successful moved to done");
				
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("Cannot move file (" + doneFile.getFileName() + ") to done. Error=(" + e.getMessage() + ")");
			}
		}
		
		// kill too old jobfile
		if( jobMaxDelay > 0 ) {
			Collection<TransformFile> jobFileList = transformFileService.getFiles(inAreaName, fileFilterRegexDone);
			for (TransformFile jobFile : jobFileList) {
				if( jobFile.getAgeMilliSecond() > jobMaxDelay ) {
					LOG.info("Jobfile (" + jobFile.getFullFileName() + ") is (" + jobFile.getAgeMilliSecond() + ") milliseconds old. Maxvalue is (" + jobMaxDelay + "). Probably job is dead. We delete the jobfile to rerun");
					jobFile.delete();
				}
			}
		}
		
	}
    
    
    
    
}