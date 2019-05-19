package de.semsoft.xfactory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.semsoft.xfactory.logging.modell.LoggingEntry;
import de.semsoft.xfactory.logging.repo.LoggingRepository;

@Service
public class LoggingService {

	@Autowired
	LoggingRepository loggingRepository;

	public LoggingEntry addLoggingEntry(LoggingEntry loggingentry) {
		return loggingRepository.save(loggingentry);
	}

}
