create table common_external_history_packs_tbl (
	id bigint not null auto_increment,
	receive_date datetime not null comment 'Packet recieve time',
	source_instance_id varchar(255) not null comment 'Source instance id',
	consumption_group_id bigint not null comment 'Consumption group id',
	file_id bigint not null comment 'File containing records reference',
	primary key (id),
	unique (source_instance_id, consumption_group_id)
) comment='Received history records pack';

create table common_history_unpack_data_tbl (
	id bigint not null auto_increment,
	source_instance_id varchar(255) not null unique comment 'Source instance id',
	last_pack_id bigint not null comment 'Reference to last history pack',
	primary key (id)
) comment='Data for unpacking process';


alter table common_external_history_packs_tbl
	add index FK_common_external_history_packs_tbl (file_id),
	add constraint FK_common_external_history_packs_tbl
	foreign key (file_id)
	references common_files_tbl (id);

alter table common_history_unpack_data_tbl
	add index FK_common_history_unpack_data_tbl_last_pack_id (last_pack_id),
	add constraint FK_common_history_unpack_data_tbl_last_pack_id
	foreign key (last_pack_id)
	references common_external_history_packs_tbl (id);




update common_version_tbl set last_modified_date='2009-05-26', date_version=0;
