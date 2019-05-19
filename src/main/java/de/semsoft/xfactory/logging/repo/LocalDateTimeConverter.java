package de.semsoft.xfactory.logging.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime arg0) {
		return arg0 == null ? null : java.sql.Timestamp.valueOf(arg0);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp arg0) {
		return arg0 == null ? null : arg0.toLocalDateTime();
	}

}
