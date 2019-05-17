package de.semsoft.xfactory.ui;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.ui.services.XsltFileService;


@Service(value="xlstFileServiceImpl")
public class FileServiceImpl implements XsltFileService {


    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
	
 
	@Value("${xfactory.path.basepath}")
	private String basePath;

	
	@Value("${xfactory.path.lib}")
	private String lib;
	
	
	@Override
	public List<LibFile> findAll() {
		
		return readFilesWithFilter(".*");
		
	}

	public String getPath() {
		return basePath + "/" + lib;
	}

	@Override
	public List<LibFile> search(String keyword) {

		return readFilesWithFilter(keyword);
		
	}
	
	
	
	private List<LibFile> readFilesWithFilter(String filter) {
		
		List<LibFile> result = new ArrayList<LibFile>(); 
		
		final Pattern p = Pattern.compile(filter);
		File[] fileList = new File(basePath + "/" + lib ).listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File f) {
		       	return p.matcher(f.getName()).matches();
		    }
		});	
		
		if( fileList != null ) {
			if(fileList.length > 0 ) {
				for (File file : fileList) {
					result.add(new LibFile(file));
				}		
			} 
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean addNewFile(InputStream data, String fileName) {

		try {
			java.nio.file.Files.copy(
					data, 
				    Paths.get(this.getPath() + "/" + fileName), 
				    StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
			 
		IOUtils.closeQuietly(data);
		
		return true;
	}

	
	@Override
	public boolean deleteFile(LibFile file) {

		return file.getFile().delete();
		
	}


	
}
