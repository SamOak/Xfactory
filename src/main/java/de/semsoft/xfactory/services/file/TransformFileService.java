package de.semsoft.xfactory.services.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.services.interfaces.ITransformFileService;

@Service
public class TransformFileService implements ITransformFileService {

	@Value("${xfactory.path.basepath}")
	private String basePath;
	
	@Override
	public Collection<TransformFile> getFiles(String area, String filter) {

		ArrayList<TransformFile>  coll = new ArrayList<TransformFile>();
		
		// Search all file in this area
		File[] slotList = new File(basePath + "/" + area).listFiles(File::isDirectory);
		if(slotList != null ) {
			for (File slot : slotList) {

				final Pattern p = Pattern.compile(filter);
				File[] fileList = new File(basePath + "/" + area + "/" + slot.getName()).listFiles(new FileFilter() {
				    @Override
				    public boolean accept(File f) {
				       	return p.matcher(f.getName()).matches();
				    }
				});	
				
				if( fileList != null ) {
					if(fileList.length > 0 ) {
						for (File file : fileList) {
							coll.add(new TransformFile(file));
						}		
					}
				}
			}
		}
		
		return coll;
	}

	
}
