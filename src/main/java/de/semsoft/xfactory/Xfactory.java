package de.semsoft.xfactory;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

import de.semsoft.xfactory.services.ApplicationControl;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@Controller
public class Xfactory {

	private static final Logger LOG = LoggerFactory.getLogger(Xfactory.class);

	@Value("${xfactory.thread.queuesize}")
	private int queueSize;

	@Value("${xfactory.thread.poolsize}")
	private int poolSize;

	@Value("${xfactory.path.basepath}")
	private String basePath;

	@Value("${xfactory.path.area.in}")
	private String areaIn;

	@Value("${xfactory.path.area.done}")
	private String areaDone;

	@Value("${xfactory.path.area.out}")
	private String areaOut;

	@Value("${xfactory.path.area.error}")
	private String areaError;

	@Value("${xfactory.path.lib}")
	private String areaLib;

	@Autowired
	ApplicationControl appctrl;

	public static void main(String[] args) {
		SpringApplication.run(Xfactory.class, args);
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(poolSize);
		executor.setMaxPoolSize(poolSize);
		executor.setQueueCapacity(queueSize);
		executor.setThreadNamePrefix("Transformer");
		executor.initialize();
		return executor;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void checkFileSystem() {

		final String[] dirList = { basePath, basePath + "/" + areaIn, basePath + "/" + areaDone,
				basePath + "/" + areaError, basePath + "/" + areaOut, basePath + "/" + areaLib };

		LOG.info("*** Basedir (" + basePath + ") - check if available...");
		for (final String dir : dirList) {
			if (createDirectoryIfNotExists(dir) == false) {
				LOG.error("Cannot create directory (" + dir + ")!!!!!");
			}
		}
		LOG.info("Check done!");

	}

	private boolean createDirectoryIfNotExists(String path) {

		final File directory = new File(path);
		if (directory.exists() == false) {
			if (directory.mkdirs() == false) {
				return false;
			}
		}
		return true;
	}

}
