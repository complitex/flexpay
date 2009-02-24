-- create master index data source
-- hope there is 2002-th is not accupied
INSERT INTO common_data_source_descriptions_tbl (id, description) VALUES (2002, 'Master-Index');

drop table common_master_index_bounds_tbl;
drop table common_master_index_tbl;

update common_version_tbl set last_modified_date='2009-02-22', date_version=0;
