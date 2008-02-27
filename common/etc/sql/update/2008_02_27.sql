create table sequences_tbl (
	id bigint not null auto_increment,
	counter bigint not null,
	description varchar(255),
	primary key (id)
);

-- Init Sequences table
INSERT INTO sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');