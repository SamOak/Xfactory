package de.semsoft.xfactory.logging.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.semsoft.xfactory.logging.modell.LoggingEntry;

@Repository(value="LoggingRepository")
public interface LoggingRepository extends JpaRepository<LoggingEntry, Long> { 
}
