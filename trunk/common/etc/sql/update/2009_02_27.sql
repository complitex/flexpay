alter table common_diffs_tbl
		add column instance_id varchar(255) not null comment 'Source application installation identifier';

update common_version_tbl set last_modified_date='2009-02-27', date_version=0;
