create table common_registries_tbl (
	id bigint not null auto_increment,
	version integer not null,
	registry_number bigint,
	records_number bigint,
	creation_date datetime,
	from_date datetime,
	till_date datetime,
	sender_code bigint,
	recipient_code bigint,
	amount decimal(19,2),
	registry_type_id bigint not null comment 'Registry type reference',
	file_id bigint comment 'Registry file reference',
	registry_status_id bigint not null comment 'Registry status reference',
	archive_status_id bigint not null comment 'Registry archive status reference',
	primary key (id)
);

create table common_registry_archive_statuses_tbl (
	id bigint not null auto_increment,
	code integer not null unique,
	primary key (id)
);

create table common_registry_containers_tbl (
	id bigint not null auto_increment,
	data varchar(2048) not null comment 'Registry container data',
	order_weight integer not null comment 'Order of the container in a registry',
	registry_id bigint not null comment 'Registry reference',
	primary key (id)
);

create table common_registry_properties_tbl (
	id bigint not null auto_increment comment 'Primary key',
	props_type varchar(255) not null comment 'Hierarchy discriminator, all entities should have the same value',
	version integer not null comment 'Optimistic lock version',
	registry_id bigint comment 'Registry reference',
	primary key (id)
);

create table common_registry_record_containers_tbl (
	id bigint not null auto_increment,
	data varchar(2048) not null comment 'Container data',
	order_weight integer not null comment 'Order of the container in a registry record',
	record_id bigint not null comment 'Registry record reference',
	primary key (id)
);

create table common_registry_record_properties_tbl (
	id bigint not null auto_increment comment 'Primary key',
	props_type varchar(255) not null comment 'Hierarchy discriminator, all entities should have the same value',
	version integer not null comment 'Optimistic lock version',
	record_id bigint comment 'Registry record reference',
	primary key (id)
);

create table common_registry_record_statuses_tbl (
	id bigint not null auto_increment,
	code integer not null unique comment 'Registry record status code',
	primary key (id)
);

create table common_registry_records_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	service_code varchar(255) not null,
	personal_account_ext varchar(255) not null,
	city varchar(255),
	street_type varchar(255),
	street_name varchar(255),
	building_number varchar(255),
	bulk_number varchar(255),
	apartment_number varchar(255),
	first_name varchar(255),
	middle_name varchar(255),
	last_name varchar(255),
	operation_date datetime not null,
	unique_operation_number bigint,
	amount decimal(19,2),
	registry_id bigint not null comment 'Registry reference',
	record_status_id bigint comment 'Record status reference',
	import_error_id bigint comment 'Import error reference',
	primary key (id)
);

create table common_registry_statuses_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic locking version',
	code integer not null unique comment 'Registry status code',
	primary key (id)
);

create table common_registry_types_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic locking version',
	code integer not null unique comment 'Registry type code',
	primary key (id)
);


alter table common_registries_tbl
	add index FK_common_registries_tbl_file_id (file_id),
	add constraint FK_common_registries_tbl_file_id
	foreign key (file_id)
	references common_files_tbl (id);

alter table common_registries_tbl
	add index FK_common_registries_tbl_archive_status_id (archive_status_id),
	add constraint FK_common_registries_tbl_archive_status_id
	foreign key (archive_status_id)
	references common_registry_archive_statuses_tbl (id);

alter table common_registries_tbl
	add index FK_common_registries_tbl_status_id (registry_status_id),
	add constraint FK_common_registries_tbl_status_id
	foreign key (registry_status_id)
	references common_registry_statuses_tbl (id);

alter table common_registries_tbl
	add index FK_common_registries_tbl_registry_type_id (registry_type_id),
	add constraint FK_common_registries_tbl_registry_type_id
	foreign key (registry_type_id)
	references common_registry_types_tbl (id);

alter table common_registry_containers_tbl
	add index FK_common_registry_containers_tbl_registry_id (registry_id),
	add constraint FK_common_registry_containers_tbl_registry_id
	foreign key (registry_id)
	references common_registries_tbl (id);

alter table common_registry_properties_tbl
	add index FK_common_registry_properties_tbl_registry_id (registry_id),
	add constraint FK_common_registry_properties_tbl_registry_id
	foreign key (registry_id)
	references common_registries_tbl (id);

alter table common_registry_record_containers_tbl
	add index FK_common_registry_record_containers_tbl_record_id (record_id),
	add constraint FK_common_registry_record_containers_tbl_record_id
	foreign key (record_id)
	references common_registry_records_tbl (id);

alter table common_registry_record_properties_tbl
	add index FK_common_registry_properties_tbl_record_id (record_id),
	add constraint FK_common_registry_properties_tbl_record_id
	foreign key (record_id)
	references common_registry_records_tbl (id);

alter table common_registry_records_tbl
	add index FK_common_registry_records_tbl_registry_id (registry_id),
	add constraint FK_common_registry_records_tbl_registry_id
	foreign key (registry_id)
	references common_registries_tbl (id);

alter table common_registry_records_tbl
	add index FK_common_registry_records_tbl_import_error_id (import_error_id),
	add constraint FK_common_registry_records_tbl_import_error_id
	foreign key (import_error_id)
	references common_import_errors_tbl (id);

alter table common_registry_records_tbl
	add index FK_common_registry_records_tbl_record_status_id (record_status_id),
	add constraint FK_common_registry_records_tbl_record_status_id
	foreign key (record_status_id)
	references common_registry_record_statuses_tbl (id);

update common_version_tbl set last_modified_date='2009-04-06', date_version=0;
