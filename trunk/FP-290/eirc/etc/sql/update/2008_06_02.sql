ALTER TABLE eirc_services_tbl
	ADD COLUMN begin_date DATE NOT NULL DEFAULT '1900/01/01' COMMENT 'The Date service is valid from',
	ADD COLUMN end_date DATE NOT NULL DEFAULT '2100/12/31' COMMENT 'The Date service is valid till',
	ADD COLUMN external_code VARCHAR(255) COMMENT 'Service providers internal service code';