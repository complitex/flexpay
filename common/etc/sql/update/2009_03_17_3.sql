
alter table common_history_records_tbl
	add column old_bool_value bit comment 'Optional old boolean value',
	add column new_bool_value bit comment 'Optional new bool value';


update common_version_tbl set last_modified_date='2009-03-17', date_version=2;
