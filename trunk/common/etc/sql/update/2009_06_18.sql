alter table common_history_consumption_groups_tbl
	add column send_tries integer not null comment 'Number of tries group file was sent',
	add column group_status integer not null comment 'Number of tries group file was sent',
	add column file_id bigint comment 'History group data file reference',
	add column postpone_time datetime comment 'Last postpone timestamp';

alter table common_history_consumption_groups_tbl 
	add index FK_common_history_consumption_groups_tbl_file_id (file_id),
	add constraint FK_common_history_consumption_groups_tbl_file_id
	foreign key (file_id)
	references common_files_tbl (id);


update common_version_tbl set last_modified_date='2009-06-18', date_version=0;
