package de.semsoft.xfactory.ui.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Repository;

import de.semsoft.xfactory.ui.FsFile;

@Repository
public interface XsltFileService {

	public List<FsFile> findAllLib();
	public List<FsFile> searchLib(String keyword);

	public List<FsFile> findAllSlot(String slot, String area);
	public List<FsFile> searchSlot(String slot, String area, String keyword);
	
	
	public String addNewFile(InputStream data, String fileName, String path);

	public boolean deleteFile(FsFile file);
	
	public boolean deleteSlot(String slotName);
	public boolean addSlot(String slotName);

}
