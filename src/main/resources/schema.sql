create table Logging (
	ID					BIGINT,
	UUID				VARCHAR(50),
	trigger				VARCHAR(30),
	starttime			TIMESTAMP,
	finishtime			TIMESTAMP,
	status      		VARCHAR(25),
	info        		VARCHAR(1000),
	contentLengthSource BIGINT,
	contentLengthTarget BIGINT,
);


