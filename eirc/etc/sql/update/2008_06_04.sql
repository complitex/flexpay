create table eirc_eirc_accounts_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	status integer not null,
	account_number varchar(255) not null comment 'EIRC account number',
	apartment_id bigint not null comment 'Apartment reference',
	person_id bigint not null comment 'Responsible person reference',
	primary key (id)
) comment='EIRC Personal accounts table';


alter table eirc_eirc_accounts_tbl
	add index FK_eirc_eirc_accounts_person_id (person_id),
	add constraint FK_eirc_eirc_accounts_person_id
	foreign key (person_id)
	references persons_tbl (id);

alter table eirc_eirc_accounts_tbl
	add index FK_eirc_eirc_accounts_apartment_id (apartment_id),
	add constraint FK_eirc_eirc_accounts_apartment_id
	foreign key (apartment_id)
	references apartments_tbl (id);

ALTER TABLE eirc_consumers_tbl
	ADD COLUMN begin_date DATE NOT NULL COMMENT 'Consumer begin date',
	ADD COLUMN end_date DATE NOT NULL COMMENT 'Consumer end date';

ALTER TABLE eirc_registry_records_tbl
	ADD COLUMN person_id BIGINT COMMENT 'Person reference',
	ADD COLUMN apartment_id BIGINT COMMENT 'Apartment reference';

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_person_id (person_id),
	add constraint FK_eirc_registry_record_person_id
	foreign key (person_id)
	references persons_tbl (id);

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_apartment_id (apartment_id),
	add constraint FK_eirc_registry_record_apartment_id
	foreign key (apartment_id)
	references apartments_tbl (id);

alter table eirc_registry_records_tbl
	add column version integer not null comment 'Optimistic lock version';

-- Replace CN synchronisation data source with a registry file source
insert into common_data_source_descriptions_tbl (description) values ('Источник данных - реестры ЦН');
select @source_id:=last_insert_id();
update eirc_service_providers_tbl set data_source_description_id=@source_id where organisation_id=4;

update common_data_source_descriptions_tbl set description = 'Источник данных - синхронизация с ЦН' where id=1;
