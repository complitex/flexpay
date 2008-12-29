create table eirc_consumer_infos_tbl (
	id bigint not null auto_increment,
	status integer not null comment 'ConsumerInfo status',
	first_name varchar(255) comment 'Prividers consumer first name',
	middle_name varchar(255) comment 'Prividers consumer middle name',
	last_name varchar(255) comment 'Prividers consumer last name',
	city_name varchar(255) comment 'Prividers consumer city name',
	street_type_name varchar(255) comment 'Prividers consumer street type name',
	street_name varchar(255) comment 'Prividers consumer street name',
	building_number varchar(255) comment 'Prividers consumer building number',
	building_bulk varchar(255) comment 'Prividers consumer building bulk',
	apartment_number varchar(255) comment 'Prividers consumer apartment number',
	primary key (id)
);

alter table eirc_consumers_tbl
	add column consumer_info_id bigint comment 'Service providers consumer details';

alter table eirc_consumers_tbl
	add index FK_eirc_consumers_tbl_consumer_info_id (consumer_info_id),
	add constraint FK_eirc_consumers_tbl_consumer_info_id
	foreign key (consumer_info_id)
	references eirc_consumer_infos_tbl (id);

ALTER TABLE eirc_registry_records_tbl MODIFY COLUMN service_code VARCHAR(255) NOT NULL;

ALTER TABLE eirc_registry_records_tbl
 DROP COLUMN service_type_id,
 DROP INDEX FK_eirc_registry_record_service_type,
 DROP FOREIGN KEY FK_eirc_registry_record_service_type;

ALTER TABLE eirc_registries_tbl DROP COLUMN containers;
ALTER TABLE eirc_registry_records_tbl DROP COLUMN containers;

create table eirc_registry_containers_tbl (
	id bigint not null auto_increment,
	data varchar(2048) not null comment 'Registry container data',
	order_weight integer not null comment 'Order of the container in a registry',
	registry_id bigint not null comment 'Registry reference',
	primary key (id)
);

create table eirc_registry_record_containers_tbl (
	id bigint not null auto_increment,
	data varchar(2048) not null comment 'Container data',
	order_weight integer not null comment 'Order of the container in a registry record',
	record_id bigint not null comment 'Registry record reference',
	primary key (id)
);

alter table eirc_registry_containers_tbl
	add index FK_eirc_registry_containers_tbl_registry_id (registry_id),
	add constraint FK_eirc_registry_containers_tbl_registry_id
	foreign key (registry_id)
	references eirc_registries_tbl (id);

alter table eirc_registry_record_containers_tbl
	add index FK_eirc_registry_record_containers_tbl_record_id (record_id),
	add constraint FK_eirc_registry_record_containers_tbl_record_id
	foreign key (record_id)
	references eirc_registry_records_tbl (id);
