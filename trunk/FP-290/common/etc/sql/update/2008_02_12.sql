-- Create version table
CREATE TABLE common_version_tbl (
	last_modified_date date NOT NULL,
	date_version int NOT NULL
);

INSERT INTO common_version_tbl (last_modified_date, date_version) VALUES ('2007-02-12', 0);

ALTER TABLE common_data_corrections_tbl MODIFY COLUMN data_source_description_id BIGINT(20);
