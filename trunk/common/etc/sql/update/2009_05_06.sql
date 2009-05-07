alter table common_history_records_tbl
	add column field_key2 varchar(255) comment 'Optional second key for field value',
	add column field_key3 varchar(255) comment 'Optional third key for field value';

update common_version_tbl set last_modified_date='2009-05-06', date_version=0;
