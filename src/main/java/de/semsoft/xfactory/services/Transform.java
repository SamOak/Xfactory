package de.semsoft.xfactory.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.logging.modell.LoggingEntry;
import de.semsoft.xfactory.restcontroller.databean.TransformControllerData;
import de.semsoft.xfactory.services.file.TransformFile;
import de.semsoft.xfactory.util.XSLTransformer;

@Service
public class Transform {

	private static final Logger LOG = LoggerFactory.getLogger(Transform.class);

	@Value("${xfactory.path.basepath}")
	private String basePath;

	@Value("${xfactory.path.lib}")
	private String libPath;

	@Value("${xfactory.path.area.out}")
	private String outPath;

	@Autowired
	LoggingService loggingService;

	TransformMetric metric = null;

	public TransformControllerData transform(TransformControllerData data) {

		LOG.info("Transform on controlldata ...");
		metric = new TransformMetric();
		final XSLTransformer transformer = new XSLTransformer();

		String xsltFileName = basePath + "/" + libPath + "/" + data.getXsltName();
		if (xsltFileName.toUpperCase().endsWith("XSL") == false) {
			xsltFileName = xsltFileName + ".xsl";
		}

		if (new File(xsltFileName).exists() == false) {
			metric.finish(TransformMetric.STATUS_ERROR);
			metric.setInfo("XsltFile (" + xsltFileName + ") do not exists");
		} else {
			metric.setInfo("take XsltFile (" + xsltFileName + ") for transformation");
			metric.setContentLengthSource(data.getSource().length());
			data.setTarget(transformer.process(data.getSource(), new File(xsltFileName)));
			if (data.getTarget() == null) {
				metric.finish(TransformMetric.STATUS_ERROR);
			} else {
				metric.finish();
				metric.setContentLengthTarget(data.getTarget().length());
			}
		}

		data.setMetric(metric);
		loggingService.addLoggingEntry(metric.getLoggingEntry(LoggingEntry.TRIGGER_HTTP));

		LOG.info("Tranformation done");
		return data;
	}

	@Async
	public void transformFile(TransformFile file) {

		String xsltFileName = null;
		String contentNewFile = null;
		String newFileName = null;

		File xsltFile = null;

		final XSLTransformer transformer = new XSLTransformer();
		metric = new TransformMetric();

		metric.setInfo(file.getFullFileName());

		/*
		 * check if source file exists
		 */
		LOG.info("Transform (" + file.getFileName() + ") with xsl file (" + file.getSlot() + ".xlst).... ");
		if (!file.getFile().exists()) {
			LOG.error("Cannot find file (" + file.getFileName() + ") in slot (" + file.getSlot() + ")");
			file.setStatus(TransformFile.STATUS_ERROR, metric.getUuid());
			metric.finish(TransformMetric.STATUS_ERROR);
			return;
		}

		// set status
		file.setStatus(TransformFile.STATUS_JOB);

		/*
		 * check if xsl-File exists
		 */
		xsltFileName = basePath + "/" + libPath + "/" + file.getSlot() + ".xsl";
		xsltFile = new File(xsltFileName);

		if (xsltFile.exists() == false) {

			LOG.error("Xlst file (" + xsltFile.getName() + ") for slot (" + file.getSlot() + ") does not exists");
			file.setStatus(TransformFile.STATUS_ERROR, metric.getUuid());
			metric.finish(TransformMetric.STATUS_ERROR);
			return;

		}

		/*
		 * transform file....
		 */
		LOG.info("About to transform file (" + file.getFullFileName() + ")...");
		try {

			final String contentOldFile = file.getContent();
			contentNewFile = transformer.process(contentOldFile, new File(xsltFileName));

			metric.setContentLengthSource(contentOldFile.length());
			metric.setContentLengthTarget(contentNewFile.length());

		} catch (final IOException e1) {

			LOG.error("Cannot transform xml file. Error=(" + e1.getMessage() + ")");
			file.setStatus(TransformFile.STATUS_ERROR, metric.getUuid());
			metric.finish(TransformMetric.STATUS_ERROR);
			return;

		}
		LOG.info("Transformation of file (" + file.getFullFileName() + ") done. Content-length=("
				+ contentNewFile.length() + ")");

		/*
		 * write target file
		 */
		newFileName = basePath + "/" + outPath + "/" + file.getSlot() + "/" + file.getFileName();
		LOG.info("About to write new file to (" + newFileName + ")");
		try {

			Files.createDirectories(Paths.get(basePath + "/" + outPath + "/" + file.getSlot()));
			newFileName = basePath + "/" + outPath + "/" + file.getSlot() + "/" + file.getFileName();

			FileUtils.writeStringToFile(new File(newFileName), contentNewFile, StandardCharsets.UTF_8);

		} catch (final IOException e) {

			LOG.error("Cannot write xml to file (" + newFileName + "). Error=(" + e.getMessage() + ")");
			file.setStatus(TransformFile.STATUS_ERROR, metric.getUuid());
			metric.finish(TransformMetric.STATUS_ERROR);
			return;

		}

		file.setStatus(TransformFile.STATUS_DONE, metric.getUuid());
		metric.finish();
		loggingService.addLoggingEntry(metric.getLoggingEntry(LoggingEntry.TRIGGER_FILE));
		return;

	}

	public TransformMetric getMetric() {
		return metric;
	}

}
