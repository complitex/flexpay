alter table common_diffs_tbl
		add column object_type_name varchar(255) comment 'Object type class name';

update common_version_tbl set last_modified_date='2009-08-07', date_version=0;
