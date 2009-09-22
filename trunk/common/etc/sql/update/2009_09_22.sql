alter table common_diffs_tbl
		add column error_message varchar(255) comment 'Processing error message';

update common_version_tbl set last_modified_date='2009-09-22', date_version=0;
