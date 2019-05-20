package de.semsoft.xfactory.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.ui.services.SlotMetricList;



@Service(value = "SlotMetricListImpl")
public class SlotMetricListImpl implements SlotMetricList {
	
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

	@Override
	public List<SlotMetric> getMetricList() {

		final List<SlotMetric> result = new ArrayList<SlotMetric>();
		
		// read all subdirectories under "in" .. these are the "slots"
		final File inFolder = new File(basePath + "/" + areaIn);
		final File[] slotList = inFolder.listFiles( new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                if(file.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
		
		if( slotList.length == 0 ) {
			return null;
		}
		
		for (final File slot : slotList) {
			result.add( new SlotMetric(basePath, slot.getName(), areaIn, areaOut, areaDone, areaError, lib) );
		}
		
		return result;
	}

	
	
	
}
