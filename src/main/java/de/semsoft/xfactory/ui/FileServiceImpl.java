package de.semsoft.xfactory.ui;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.ui.services.XsltFileService;

@Service(value = "xlstFileServiceImpl")
public class FileServiceImpl implements XsltFileService {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(FileServiceImpl.class);

	final private String[] areaList = {"in","out","done","error"};
	
	@Value("${xfactory.path.basepath}")
	private String basePath;

	@Value("${xfactory.path.lib}")
	private String lib;

	@Value("${xfactory.path.area.in}")
	private String areaIn;

	@Value("${xfactory.path.area.out}")
	private String areaOut;

	@Value("${xfactory.path.area.done}")
	private String areaDone;

	@Value("${xfactory.path.area.error}")
	private String areaError;
	
	
	public String getLibPath() {
		return basePath + "/" + lib;
	}
	
	public String getAreaPathName(String areaName) {
		
		String path = "";
		
		switch( areaName.toLowerCase() ) {
			case "in"    : path = areaIn; break;
			case "out"   : path = areaOut; break;
			case "done"  : path = areaDone; break;
			case "error" : path = areaError; break;
		}
		
		return path;
	}
	
	@Override
	public List<FsFile> findAllLib() {
		return readFilesWithFilter(getLibPath(), ".*");
	}

	@Override
	public List<FsFile> searchLib(String keyword) {
		return readFilesWithFilter(getLibPath(),keyword);
	}

	@Override
	public List<FsFile> findAllSlot(String slot, String area) {
		return readFilesWithFilter(basePath + "/" + getAreaPathName(area) + "/" + slot, ".*");
	}


	@Override
	public List<FsFile> searchSlot(String slot, String area, String keyword) {
		return readFilesWithFilter(basePath + "/" + getAreaPathName(area) + "/" + slot, keyword);
	}


	
	
	private List<FsFile> readFilesWithFilter(String path, String filter) {

		final List<FsFile> result = new ArrayList<FsFile>();

		final Pattern p = Pattern.compile(filter);
		final File[] fileList = new File(path).listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return p.matcher(f.getName()).matches();
			}
		});

		if (fileList != null) {
			if (fileList.length > 0) {
				for (final File file : fileList) {
					result.add(new FsFile(file));
				}
			}
		}
		return result;
	}

	public boolean addNewFile(InputStream data, String fileName, String slot, String area) {
		return addNewFile( data,  fileName, basePath + "/" + getAreaPathName(area) + "/" + slot);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean addNewFile(InputStream data, String fileName, String path) {

		String targetPath;
		if( path == null ) {
			targetPath = getLibPath();
		} else {
			targetPath = path;
		}
		
		try {
			java.nio.file.Files.copy(data, Paths.get(targetPath + "/" + fileName),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}

		IOUtils.closeQuietly(data);

		return true;
	}

	@Override
	public boolean deleteFile(FsFile file) {

		return file.getFile().delete();

	}

	@Override
	public boolean deleteSlot(String slotName) {

		for (final String areaName : areaList) {

			final String path = basePath + "/" + getAreaPathName(areaName) + "/" + slotName; 
			try {
				FileUtils.deleteDirectory(new File(path));
			} catch (final IOException e) {
				return false;
			}
		}
		return true;
		
	}

	@Override
	public boolean addSlot(String slotName) {

		final String path = basePath + "/" + getAreaPathName("in") + "/" + slotName;
		new File(path).mkdirs();
		
		return true;
	}

}
