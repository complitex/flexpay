alter table common_diffs_tbl
	add column old_decimal_value decimal(19,5) comment 'Optional old decimal value',
	add column new_decimal_value decimal(19,5) comment 'Optional new decimal value';

update common_version_tbl set last_modified_date='2009-08-11', date_version=0;
