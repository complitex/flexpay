-- add QUITTANCES registry type
insert into eirc_registry_types_tbl (code) values (11);

ALTER TABLE eirc_quittance_details_tbl
	MODIFY COLUMN payment DECIMAL(19,5) DEFAULT NULL COMMENT 'Payments amount for previous period';

ALTER TABLE eirc_registry_record_statuses_tbl
	MODIFY COLUMN code INTEGER NOT NULL UNIQUE COMMENT 'Registry record status code';

ALTER TABLE eirc_registry_statuses_tbl
	MODIFY COLUMN code INTEGER NOT NULL UNIQUE COMMENT 'Registry status code',
	ADD COLUMN version INTEGER NOT NULL COMMENT 'Optimistic locking version';

ALTER TABLE eirc_registry_types_tbl
	MODIFY COLUMN code INTEGER NOT NULL UNIQUE COMMENT 'Registry status code',
	ADD COLUMN version INTEGER NOT NULL COMMENT 'Optimistic locking version';
