alter table common_registries_tbl
		add column module_id bigint not null comment 'Module reference';

update common_version_tbl set last_modified_date='2009-10-23', date_version=1;
