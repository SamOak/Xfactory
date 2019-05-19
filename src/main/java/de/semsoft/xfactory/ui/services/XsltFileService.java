package de.semsoft.xfactory.ui.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Repository;

import de.semsoft.xfactory.ui.LibFile;

@Repository
public interface XsltFileService {

	public List<LibFile> findAll();

	public List<LibFile> search(String keyword);

	public boolean addNewFile(InputStream data, String fileName);

	public boolean deleteFile(LibFile file);

}
