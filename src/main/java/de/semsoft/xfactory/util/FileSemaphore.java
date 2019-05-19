package de.semsoft.xfactory.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

public class FileSemaphore {

	// Pick up for processing
	public final static String STATUS_JOB = "job";
	public final static String STATUS_PROCESSING = "processing"; // processing
	public final static String STATUS_DONE = "done"; // done .... ready for clean
	public final static String STATUS_ERROR = "error"; //

	public final static String[] STATUSLIST = { STATUS_DONE, STATUS_JOB, STATUS_PROCESSING, STATUS_ERROR };

	public File file = null;

	public boolean isNew() {

		for (final String statusName : STATUSLIST) {
			final String fileName = this.file.getAbsolutePath() + "." + statusName;
			if (new File(fileName).exists()) {
				return false;
			}
		}
		return true;
	}

	public boolean isNewAndValid() {

		return isNew() && XMLUtils.isValidXMLFile(file);

	}

	public void setStatus(final String status, String id) {

		releaseAllBlocks();

		final File semfile = new File(file.getAbsoluteFile() + "." + status);
		try {
			semfile.createNewFile();
			if (id != null) {
				FileUtils.writeStringToFile(semfile, id, StandardCharsets.UTF_8, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setStatus(final String status) {

		setStatus(status, null);

	}

	public void releaseAllBlocks() {
		for (final String statusName : STATUSLIST) {
			(new File(file.getAbsoluteFile() + "." + statusName)).delete();
		}
	}

}
