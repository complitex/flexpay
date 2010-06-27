 create table eirc_process_registry_variable_instance_tbl (
	id bigint not null auto_increment,
	version integer not null,
	process_id bigint not null unique,
	registry_id bigint not null comment 'Processing registry',
	char_point bigint,
	last_processed_registry_record bigint,
	processed_count_lines integer,
	processed_count_records integer,
	primary key (id)
);

alter table eirc_process_registry_variable_instance_tbl
	add index FK_common_registry_tbl_process_variable_instance_id (registry_id),
	add constraint FK_common_registry_tbl_process_variable_instance_id
	foreign key (registry_id)
	references common_registries_tbl (id);

update common_version_tbl set last_modified_date='2010-06-18', date_version=0;
