package de.semsoft.xfactory.ui;

import java.io.File;

public class SlotMetric {

	private final String slotName;
	private final String basePath;
	
	private final String areaIn;
	private final String areaOut;
	private final String areaDone;
	private final String areaError;
	
	private int countIN; 
	private int countOUT;
	private int countDONE;
	private int countERROR;

	private final String lib;
	private boolean transformationReady;
	
	public SlotMetric(String basePath, String slotName, String in, String out, String done, String error, String lib) {
		this.basePath = basePath;
		this.slotName = slotName;
		this.areaIn = in;
		this.areaOut = out;
		this.areaDone = done; 
		this.areaError = error;
		this.lib = lib;
		execute();
	}
	
	public void execute() {
		
		this.countIN    = countFiles(areaIn);
		this.countOUT   = countFiles(areaOut);
		this.countDONE  = countFiles(areaDone);
		this.countERROR = countFiles(areaError);
		
		// check if xsl file for this slot available or not
		if( new File(basePath + "/" + this.lib + "/" + slotName + ".xsl").exists() ) {
			this.transformationReady = true;
		} else {
			this.transformationReady = false;
		}
		
	}
	
	private int countFiles(String area) {
		if( new File(basePath + "/" + area + "/" + slotName).exists() == true ) {
			return  new File(basePath + "/" + area + "/" + slotName).list().length;
		} 
		return 0;
	}

	public String getSlotName() {
		return slotName;
	}

	public String getBasePath() {
		return basePath;
	}

	public int getCountIN() {
		return countIN;
	}

	public int getCountOUT() {
		return countOUT;
	}

	public int getCountDONE() {
		return countDONE;
	}

	public int getCountERROR() {
		return countERROR;
	}

	public boolean isTransformationReady() {
		return transformationReady;
	}


	
	
	
}
