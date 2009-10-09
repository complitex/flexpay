alter table common_registries_tbl
		add column errors_number integer default -1 not null comment 'Cached errors number value, -1 is not init';

update common_version_tbl set last_modified_date='2009-10-07', date_version=0;
