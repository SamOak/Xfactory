package de.semsoft.xfactory.services.interfaces;

import java.util.Collection;

import de.semsoft.xfactory.services.file.TransformFile;

public interface ITransformFileService {
	
	public Collection<TransformFile> getFiles(String area, String filter);

}
