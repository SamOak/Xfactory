package de.semsoft.xfactory.services.file.worker;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.services.ApplicationControl;
import de.semsoft.xfactory.services.Transform;
import de.semsoft.xfactory.services.file.TransformFile;
import de.semsoft.xfactory.services.file.TransformFileService;

@Service
public class PickupAndStart {

	private static final Logger LOG = LoggerFactory.getLogger(PickupAndStart.class);

	@Autowired
	private Transform transformer;

	@Autowired
	private TransformFileService transformFileService;

	@Value("${xfactory.path.basepath}")
	private String basePath;

	@Value("${xfactory.filter.process.regex}")
	private String fileFilterRegex;

	@Value("${xfactory.path.area.in}")
	private String inAreaName;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	ApplicationControl appctrl;

	public PickupAndStart() {
		LOG.info("pick up ....");
	}

	@Scheduled(fixedRateString = "${xfactory.pickup.sleep}", initialDelayString = "${xfactory.pickup.startup.delay}")
	public void pickItUp() {

		if (appctrl.isBlocked()) {
			LOG.info("Application blocked.");
			return;
		} else {
			LOG.info("| Searching for xml files. Path=(" + basePath + "/" + inAreaName + ")...");
		}

		final Collection<TransformFile> fileList = transformFileService.getFiles(inAreaName, fileFilterRegex);
		for (final TransformFile transformFile : fileList) {

			if (taskExecutor.getActiveCount() >= taskExecutor.getMaxPoolSize()) {
				LOG.info("Probably executor queue is full. I try later or you adjust executer properties.");
				return;
			}

			if (transformFile.isNewAndValid()) {

				try {
					LOG.info("		Processing file (" + transformFile.getFileName() + ") in slot ("
							+ transformFile.getSlot() + ") ...");

					appctrl.increaseJobCounter();
					transformer.transformFile(transformFile);

					LOG.info("		Processing started");

				} catch (final Exception e) {
					e.printStackTrace();
					return;
				}

			} else {

				LOG.info("Slot (" + transformFile.getSlot() + "): File (" + transformFile.getFileName()
						+ ") is not (yet) valid or blocked");

			}
		}

	}

}
