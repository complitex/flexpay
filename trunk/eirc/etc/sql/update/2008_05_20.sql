ALTER TABLE buildings_tbl
 DROP INDEX FKEA6AFBBEF77A6E1C,
 DROP INDEX FKEA6AFBBE2924A765,
 DROP FOREIGN KEY FKEA6AFBBE2924A765,
 DROP FOREIGN KEY FKEA6AFBBEF77A6E1C;

alter table eirc_account_records_tbl drop index FKE517FBC591349F59, drop foreign key FKE517FBC591349F59;
alter table eirc_account_records_tbl drop index FKE517FBC5E1B5250B, drop foreign key FKE517FBC5E1B5250B;
alter table eirc_account_records_tbl drop index FKE517FBC57F30FD59, drop foreign key FKE517FBC57F30FD59;

alter table eirc_consumers_tbl drop index FK9751FED27095AEAD, drop foreign key FK9751FED27095AEAD;
alter table eirc_consumers_tbl drop index FK9751FED2DEF75687, drop foreign key FK9751FED2DEF75687;
alter table eirc_consumers_tbl drop index FK9751FED258F3985B, drop foreign key FK9751FED258F3985B;

alter table eirc_organisations_tbl drop index FK9AA6756E1AE9F4D, drop foreign key FK9AA6756E1AE9F4D;

alter table eirc_registries_tbl drop index FKAFD3F6C48819126, drop foreign key FKAFD3F6C48819126;
alter table eirc_registries_tbl drop index FKAFD3F6C412902C71, drop foreign key FKAFD3F6C412902C71;
alter table eirc_service_organisations_tbl drop index FKB7C04C647F30FD59, drop foreign key FKB7C04C647F30FD59;

--alter table eirc_registries_tbl drop index FKAFD3F6C48819126, drop foreign key FKAFD3F6C48819126;
--alter table eirc_registries_tbl drop index FKAFD3F6C412902C71, drop foreign key FKAFD3F6C412902C71;
alter table eirc_registries_tbl drop index FKAFD3F6C4D1F3C974, drop foreign key FKAFD3F6C4D1F3C974;
ALTER TABLE eirc_registries_tbl
	DROP INDEX FK_archive_status,
	DROP INDEX FK_status,
	DROP INDEX FK_sender,
	DROP INDEX FK_recipient,
	DROP FOREIGN KEY FK_archive_status,
	DROP FOREIGN KEY FK_recipient,
	DROP FOREIGN KEY FK_sender,
	DROP FOREIGN KEY FK_status;

ALTER TABLE eirc_registries_tbl DROP INDEX FK8F6F49528819126, DROP INDEX FK8F6F49524B0FEDC6,
	DROP INDEX FK8F6F495212E06FB1, DROP INDEX FK8F6F495212902C71, DROP INDEX FK8F6F4952D1F3C974,
	DROP FOREIGN KEY FK8F6F49528819126, DROP FOREIGN KEY FK8F6F4952D1F3C974;

alter table eirc_registry_records_tbl drop index FK3E12B76991349F59, drop foreign key FK3E12B76991349F59;
ALTER TABLE eirc_registry_records_tbl DROP INDEX FKD41D677791349F59, DROP FOREIGN KEY FKD41D677791349F59;
ALTER TABLE eirc_registry_records_tbl
	DROP INDEX FK_registry, DROP INDEX FK_record_status,
	DROP INDEX FK_service_type, DROP INDEX FK_import_error,
	DROP FOREIGN KEY FK_import_error,
	DROP FOREIGN KEY FK_record_status,
	DROP FOREIGN KEY FK_registry,
	DROP FOREIGN KEY FK_service_type;

alter table eirc_service_organisations_tbl drop index FKB7C04C647F30FD59, drop foreign key FKB7C04C647F30FD59;

alter table eirc_service_providers_tbl drop index FK960743AD5BA789BB, drop foreign key FK960743AD5BA789BB;
alter table eirc_service_providers_tbl drop index FK960743AD7F30FD59, drop foreign key FK960743AD7F30FD59;

alter table eirc_service_type_name_translations_tbl drop index FKA057A0442C648686, drop foreign key FKA057A0442C648686;
alter table eirc_service_type_name_translations_tbl drop index FKA057A04461F37403, drop foreign key FKA057A04461F37403;

alter table eirc_services_tbl drop index FK4D78EA87A26D3B0, drop foreign key FK4D78EA87A26D3B0;
alter table eirc_services_tbl drop index FK4D78EA875A549E10, drop foreign key FK4D78EA875A549E10;

alter table eirc_ticket_service_amounts_tbl drop index FK4C259507DA8E0B59, drop foreign key FK4C259507DA8E0B59;
alter table eirc_ticket_service_amounts_tbl drop index FK4C25950791349F59, drop foreign key FK4C25950791349F59;

alter table eirc_tickets_tbl drop index FK9C84E2FC7095AEAD, drop foreign key FK9C84E2FC7095AEAD;
alter table eirc_tickets_tbl drop index FK9C84E2FCFCB9D686, drop foreign key FK9C84E2FCFCB9D686;
alter table eirc_tickets_tbl drop index FK9C84E2FCDEF75687, drop foreign key FK9C84E2FCDEF75687;

ALTER TABLE buildings_tbl MODIFY COLUMN eirc_service_organisation_id BIGINT(20) DEFAULT NULL COMMENT 'Service organisation reference';

ALTER TABLE eirc_account_records_tbl MODIFY COLUMN consumer_id BIGINT(20) NOT NULL COMMENT 'Consumer reference',
 MODIFY COLUMN organisation_id BIGINT(20) DEFAULT NULL COMMENT 'Reference to organisation performed operation',
 MODIFY COLUMN record_type_id BIGINT(20) NOT NULL COMMENT 'Account record type reference',
 MODIFY COLUMN source_registry_record_id BIGINT(20) DEFAULT NULL COMMENT 'Registry record reference';

ALTER TABLE eirc_consumers_tbl MODIFY COLUMN service_id BIGINT(20) NOT NULL COMMENT 'Service reference',
 MODIFY COLUMN person_id BIGINT(20) NOT NULL COMMENT 'Responsible person reference',
 MODIFY COLUMN apartment_id BIGINT(20) NOT NULL COMMENT 'Apartment reference';

create table eirc_organisation_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	language_id bigint not null comment 'Language reference',
	organisation_id bigint not null comment 'Organisation reference',
	primary key (id),
	unique (language_id, organisation_id)
);

create table eirc_organisation_names_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	language_id bigint not null comment 'Language reference',
	organisation_id bigint not null comment 'Organisation reference',
	primary key (id),
	unique (language_id, organisation_id)
);

select @default_lang_id:=min(id) from languages_tbl where is_default='1';
insert into eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	select description, @default_lang_id, id from eirc_organisations_tbl;
insert into eirc_organisation_names_tbl (name, language_id, organisation_id)
	select name, @default_lang_id, id from eirc_organisations_tbl;

ALTER TABLE eirc_organisations_tbl DROP COLUMN description;
ALTER TABLE eirc_organisations_tbl DROP COLUMN name;


ALTER TABLE eirc_registries_tbl MODIFY COLUMN registry_type_id BIGINT(20) NOT NULL COMMENT 'Registry type reference',
 MODIFY COLUMN sp_file_id BIGINT(20) COMMENT 'Registry file reference',
 MODIFY COLUMN service_provider_id BIGINT(20) DEFAULT NULL COMMENT 'Service provider reference';

ALTER TABLE eirc_registry_records_tbl MODIFY COLUMN consumer_id BIGINT(20) DEFAULT NULL COMMENT 'Consumer reference';

create table eirc_service_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	language_id bigint not null comment 'Language reference',
	service_id bigint not null comment 'Service reference',
	primary key (id),
	unique (language_id, service_id)
);
insert into eirc_service_descriptions_tbl (name, language_id, service_id)
	select description, @default_lang_id, id from eirc_services_tbl;

create table eirc_service_organisation_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	language_id bigint not null comment 'Language reference',
	service_organisation_id bigint not null comment 'Organisation reference',
	primary key (id),
	unique (language_id, service_organisation_id)
);

ALTER TABLE eirc_service_organisations_tbl MODIFY COLUMN organisation_id BIGINT(20) NOT NULL COMMENT 'Organisation reference',
	ADD COLUMN district_id BIGINT(20) NOT NULL COMMENT 'District reference organisation is servicing';

create table eirc_service_provider_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	language_id bigint not null comment 'Language reference',
	service_provider_id bigint not null comment 'Service provider reference',
	primary key (id),
	unique (language_id, service_provider_id)
);
insert into eirc_service_provider_descriptions_tbl (name, language_id, service_provider_id)
	select description, @default_lang_id, id from eirc_service_providers_tbl;

ALTER TABLE eirc_service_providers_tbl MODIFY COLUMN organisation_id BIGINT(20) NOT NULL COMMENT 'Organisation reference',
 MODIFY COLUMN data_source_description_id BIGINT(20) NOT NULL COMMENT 'Data source description reference',
 DROP COLUMN description;

ALTER TABLE eirc_service_type_name_translations_tbl MODIFY COLUMN language_id BIGINT(20) NOT NULL COMMENT 'Language reference',
 MODIFY COLUMN service_type_id BIGINT(20) NOT NULL COMMENT 'Service type reference';

ALTER TABLE eirc_services_tbl MODIFY COLUMN provider_id BIGINT(20) NOT NULL COMMENT 'Service provider reference',
 MODIFY COLUMN type_id BIGINT(20) NOT NULL COMMENT 'Service type reference',
 DROP COLUMN description;

ALTER TABLE eirc_ticket_service_amounts_tbl MODIFY COLUMN ticket_id BIGINT(20) DEFAULT NULL COMMENT 'Ticket reference',
 MODIFY COLUMN consumer_id BIGINT(20) NOT NULL COMMENT 'Consumer reference';

ALTER TABLE eirc_tickets_tbl MODIFY COLUMN creation_date DATETIME NOT NULL COMMENT 'Service organisation reference',
 MODIFY COLUMN person_id BIGINT(20) NOT NULL COMMENT 'Person reference',
 MODIFY COLUMN apartment_id BIGINT(20) NOT NULL COMMENT 'Apartment reference';

alter table buildings_tbl
	add index FK_eirc_building_service_organisation (eirc_service_organisation_id),
	add constraint FK_eirc_building_service_organisation
	foreign key (eirc_service_organisation_id)
	references eirc_service_organisations_tbl (id);

alter table eirc_account_records_tbl
	add index FK_eirc_account_record_consumer (consumer_id),
	add constraint FK_eirc_account_record_consumer
	foreign key (consumer_id)
	references eirc_consumers_tbl (id);
--alter table eirc_account_records_tbl
--	add index FK_eirc_account_record_consumer (consumer_id),
--	add constraint FK_eirc_account_record_consumer
--	foreign key (consumer_id)
--	references eirc_consumers_tbl (id);

alter table eirc_account_records_tbl
	add index FK_eirc_account_record_record_type (record_type_id),
	add constraint FK_eirc_account_record_record_type
	foreign key (record_type_id)
	references eirc_account_record_types_tbl (id);

alter table eirc_account_records_tbl
	add index FK_eirc_account_record_organisation (organisation_id),
	add constraint FK_eirc_account_record_organisation
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_account_records_tbl
	add index FK_eirc_account_record_registry_record (source_registry_record_id),
	add constraint FK_eirc_account_record_registry_record
	foreign key (source_registry_record_id)
	references eirc_registry_records_tbl (id);

alter table eirc_consumers_tbl
	add index FK_eirc_consumer_responsible_person (person_id),
	add constraint FK_eirc_consumer_responsible_person
	foreign key (person_id)
	references persons_tbl (id);

alter table eirc_consumers_tbl
	add index FK_eirc_consumer_apartment (apartment_id),
	add constraint FK_eirc_consumer_apartment
	foreign key (apartment_id)
	references apartments_tbl (id);

alter table eirc_consumers_tbl
	add index FK_eirc_consumer_service (service_id),
	add constraint FK_eirc_consumer_service
	foreign key (service_id)
	references eirc_services_tbl (id);

alter table eirc_organisation_descriptions_tbl
	add index FK_eirc_organisation_description_organisation (organisation_id),
	add constraint FK_eirc_organisation_description_organisation
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_organisation_descriptions_tbl
	add index FK_eirc_organisation_description_language (language_id),
	add constraint FK_eirc_organisation_description_language
	foreign key (language_id)
	references languages_tbl (id);

alter table eirc_organisation_names_tbl
	add index FK_eirc_organisation_name_organisation (organisation_id),
	add constraint FK_eirc_organisation_name_organisation
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_organisation_names_tbl
	add index FK_eirc_organisation_name_language (language_id),
	add constraint FK_eirc_organisation_name_language
	foreign key (language_id)
	references languages_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_service_provider (service_provider_id),
	add constraint FK_eirc_registry_service_provider
	foreign key (service_provider_id)
	references eirc_service_providers_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_archive_status (archive_status_id),
	add constraint FK_eirc_registry_archive_status
	foreign key (archive_status_id)
	references eirc_registry_archive_statuses_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_sender (sender_id),
	add constraint FK_eirc_registry_sender
	foreign key (sender_id)
	references eirc_organisations_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_status (registry_status_id),
	add constraint FK_eirc_registry_status
	foreign key (registry_status_id)
	references eirc_registry_statuses_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_recipient (recipient_id),
	add constraint FK_eirc_registry_recipient
	foreign key (recipient_id)
	references eirc_organisations_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_registry_type (registry_type_id),
	add constraint FK_eirc_registry_registry_type
	foreign key (registry_type_id)
	references eirc_registry_types_tbl (id);

alter table eirc_registries_tbl
	add index FK_eirc_registry_file (sp_file_id),
	add constraint FK_eirc_registry_file
	foreign key (sp_file_id)
	references eirc_registry_files_tbl (id);

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_registry (registry_id),
	add constraint FK_eirc_registry_record_registry
	foreign key (registry_id)
	references eirc_registries_tbl (id);

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_service_type (service_type_id),
	add constraint FK_eirc_registry_record_service_type
	foreign key (service_type_id)
	references eirc_service_types_tbl (id);

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_consumer (consumer_id),
	add constraint FK_eirc_registry_record_consumer
	foreign key (consumer_id)
	references eirc_consumers_tbl (id);

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_import_error (import_error_id),
	add constraint FK_eirc_registry_record_import_error
	foreign key (import_error_id)
	references common_import_errors_tbl (id);

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_record_record_status (record_status_id),
	add constraint FK_eirc_registry_record_record_status
	foreign key (record_status_id)
	references eirc_registry_record_statuses_tbl (id);

alter table eirc_service_descriptions_tbl
	add index FK_eirc_service__description_service (service_id),
	add constraint FK_eirc_service__description_service
	foreign key (service_id)
	references eirc_services_tbl (id);

alter table eirc_service_descriptions_tbl
	add index FK_eirc_service_desciption_language (language_id),
	add constraint FK_eirc_service_desciption_language
	foreign key (language_id)
	references languages_tbl (id);

alter table eirc_service_organisation_descriptions_tbl
	add index FK_eirc_service_organisation_description_service_organisation (service_organisation_id),
	add constraint FK_eirc_service_organisation_description_service_organisation
	foreign key (service_organisation_id)
	references eirc_service_organisations_tbl (id);

alter table eirc_service_organisation_descriptions_tbl
	add index FK_eirc_service_organisation_description_language (language_id),
	add constraint FK_eirc_service_organisation_description_language
	foreign key (language_id)
	references languages_tbl (id);

alter table eirc_service_organisations_tbl
	add index FK_eirc_service_organisation_district (district_id),
	add constraint FK_eirc_service_organisation_district
	foreign key (district_id)
	references districts_tbl (id);

alter table eirc_service_organisations_tbl
	add index FK_eirc_service_organisation_organisation (organisation_id),
	add constraint FK_eirc_service_organisation_organisation
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_service_provider_descriptions_tbl
	add index FK_eirc_service_provider_description_service_provider (service_provider_id),
	add constraint FK_eirc_service_provider_description_service_provider
	foreign key (service_provider_id)
	references eirc_service_providers_tbl (id);

alter table eirc_service_provider_descriptions_tbl
	add index FK_eirc_service_provider_description_language (language_id),
	add constraint FK_eirc_service_provider_description_language
	foreign key (language_id)
	references languages_tbl (id);

alter table eirc_service_providers_tbl
	add index FK_eirc_service_provider_data_source_description (data_source_description_id),
	add constraint FK_eirc_service_provider_data_source_description
	foreign key (data_source_description_id)
	references common_data_source_descriptions_tbl (id);

alter table eirc_service_providers_tbl
	add index FK_eirc_service_provider_organisation (organisation_id),
	add constraint FK_eirc_service_provider_organisation
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_service_type_name_translations_tbl
	add index FK_eirc_service_type_name_translation_service_type (service_type_id),
	add constraint FK_eirc_service_type_name_translation_service_type
	foreign key (service_type_id)
	references eirc_service_types_tbl (id);

alter table eirc_service_type_name_translations_tbl
	add index FK_eirc_service_type_name_translation_language (language_id),
	add constraint FK_eirc_service_type_name_translation_language
	foreign key (language_id)
	references languages_tbl (id);

alter table eirc_services_tbl
	add index FK_eirc_service_service_provider (provider_id),
	add constraint FK_eirc_service_service_provider
	foreign key (provider_id)
	references eirc_service_providers_tbl (id);

alter table eirc_services_tbl
	add index FK_eirc_service_service_type (type_id),
	add constraint FK_eirc_service_service_type
	foreign key (type_id)
	references eirc_service_types_tbl (id);

alter table eirc_ticket_service_amounts_tbl
	add index FK_eirc_ticket_service_amount_ticket (ticket_id),
	add constraint FK_eirc_ticket_service_amount_ticket
	foreign key (ticket_id)
	references eirc_tickets_tbl (id);

alter table eirc_ticket_service_amounts_tbl
	add index FK_eirc_ticket_service_amount_consumer (consumer_id),
	add constraint FK_eirc_ticket_service_amount_consumer
	foreign key (consumer_id)
	references eirc_consumers_tbl (id);

alter table eirc_tickets_tbl
	add index FK_eirc_ticket_person (person_id),
	add constraint FK_eirc_ticket_person
	foreign key (person_id)
	references persons_tbl (id);

alter table eirc_tickets_tbl
	add index FK_eirc_ticket_service_organisation (service_organisation_id),
	add constraint FK_eirc_ticket_service_organisation
	foreign key (service_organisation_id)
	references eirc_service_organisations_tbl (id);

alter table eirc_tickets_tbl
	add index FK_eirc_ticket_apartment (apartment_id),
	add constraint FK_eirc_ticket_apartment
	foreign key (apartment_id)
	references apartments_tbl (id);





