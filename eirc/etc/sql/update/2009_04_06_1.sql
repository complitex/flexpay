
alter table common_registry_properties_tbl
		drop index FK_common_registry_properties_tbl_registry_id,
		drop foreign key FK_common_registry_properties_tbl_registry_id;

alter table common_registry_record_properties_tbl
		drop index FK_common_registry_properties_tbl_record_id,
		drop foreign key FK_common_registry_properties_tbl_record_id;

-- first clear common module created tables
drop table common_registry_record_containers_tbl;
drop table common_registry_records_tbl;
drop table common_registry_containers_tbl;
drop table common_registries_tbl;
drop table common_registry_archive_statuses_tbl;
drop table common_registry_record_statuses_tbl;
drop table common_registry_statuses_tbl;
drop table common_registry_types_tbl;

alter table common_registry_properties_tbl
	add column service_provider_id bigint comment 'Service provider reference',
	add column sender_organisation_id bigint comment 'Sender organization reference',
	add column recipient_organisation_id bigint comment 'Recipient organization reference';

alter table common_registry_record_properties_tbl
	add column consumer_id bigint comment 'Consumer reference',
	add column person_id bigint comment 'Person reference',
	add column apartment_id bigint comment 'Apartment reference',
	add column service_id bigint comment 'Service reference';

-- migrate data
insert into common_registry_properties_tbl (props_type, version, registry_id,
		service_provider_id, sender_organisation_id, recipient_organisation_id)
	(select 'common', 0, id, service_provider_id, sender_id, recipient_id from eirc_registries_tbl);

insert into common_registry_record_properties_tbl (props_type, version, record_id,
		consumer_id, person_id, apartment_id, service_id)
	(select 'common', 0, id, consumer_id, person_id, apartment_id, service_id from eirc_registry_records_tbl);

alter table eirc_registries_tbl
	drop index FK_eirc_registry_service_provider,
	drop foreign key FK_eirc_registry_service_provider;

alter table eirc_registries_tbl
	drop index FK_eirc_registry_archive_status,
	drop foreign key FK_eirc_registry_archive_status;

alter table eirc_registries_tbl
	drop index FK_eirc_registry_sender,
	drop foreign key FK_eirc_registry_sender;

alter table eirc_registries_tbl
	drop index FK_eirc_registry_status,
	drop foreign key FK_eirc_registry_status;

alter table eirc_registries_tbl
	drop index FK_eirc_registry_recipient,
	drop foreign key FK_eirc_registry_recipient;

alter table eirc_registries_tbl
	drop index FK_eirc_registry_registry_type,
	drop foreign key FK_eirc_registry_registry_type;

alter table eirc_registries_tbl
	drop index FK_eirc_registry_file,
	drop foreign key FK_eirc_registry_file;

alter table eirc_registry_containers_tbl
	drop index FK_eirc_registry_containers_tbl_registry_id,
	drop foreign key FK_eirc_registry_containers_tbl_registry_id;

alter table eirc_registry_record_containers_tbl
	drop index FK_eirc_registry_record_containers_tbl_record_id,
	drop foreign key FK_eirc_registry_record_containers_tbl_record_id;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_record_registry,
	drop foreign key FK_eirc_registry_record_registry;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_record_person_id,
	drop foreign key FK_eirc_registry_record_person_id;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_record_apartment_id,
	drop foreign key FK_eirc_registry_record_apartment_id;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_record_consumer,
	drop foreign key FK_eirc_registry_record_consumer;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_record_import_error,
	drop foreign key FK_eirc_registry_record_import_error;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_record_record_status,
	drop foreign key FK_eirc_registry_record_record_status;

alter table eirc_registry_records_tbl
	drop index FK_eirc_registry_records_tbl_service_id,
	drop foreign key FK_eirc_registry_records_tbl_service_id; 

alter table eirc_registries_tbl
		change sp_file_id file_id bigint comment 'Registry file reference',
		drop column service_provider_id,
		drop column sender_id,
		drop column recipient_id;

alter table eirc_registry_records_tbl
		drop column consumer_id,
		drop column person_id,
		drop column apartment_id,
		drop column service_id;


rename table eirc_registries_tbl to common_registries_tbl;
rename table eirc_registry_archive_statuses_tbl to common_registry_archive_statuses_tbl;
rename table eirc_registry_containers_tbl to common_registry_containers_tbl;
rename table eirc_registry_record_containers_tbl to common_registry_record_containers_tbl;
rename table eirc_registry_record_statuses_tbl to common_registry_record_statuses_tbl;
rename table eirc_registry_records_tbl to common_registry_records_tbl;
rename table eirc_registry_statuses_tbl to common_registry_statuses_tbl;
rename table eirc_registry_types_tbl to common_registry_types_tbl;

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
	add index FK_common_registry_properties_tbl_service_provider_id (service_provider_id),
	add constraint FK_common_registry_properties_tbl_service_provider_id
	foreign key (service_provider_id)
	references orgs_service_providers_tbl (id);

alter table common_registry_properties_tbl
	add index FK_common_registry_properties_tbl_registry_id (registry_id),
	add constraint FK_common_registry_properties_tbl_registry_id
	foreign key (registry_id)
	references common_registries_tbl (id);

alter table common_registry_properties_tbl
	add index FK_common_registry_properties_tbl_recipient_organisation_id (recipient_organisation_id),
	add constraint FK_common_registry_properties_tbl_recipient_organisation_id
	foreign key (recipient_organisation_id)
	references orgs_organizations_tbl (id);

alter table common_registry_properties_tbl
	add index FK_common_registry_properties_tbl_sender_organisation_id (sender_organisation_id),
	add constraint FK_common_registry_properties_tbl_sender_organisation_id
	foreign key (sender_organisation_id)
	references orgs_organizations_tbl (id);

alter table common_registry_record_containers_tbl
	add index FK_common_registry_record_containers_tbl_record_id (record_id),
	add constraint FK_common_registry_record_containers_tbl_record_id
	foreign key (record_id)
	references common_registry_records_tbl (id);

alter table common_registry_record_properties_tbl
	add index FK_common_registry_record_properties_tbl_person_id (person_id),
	add constraint FK_common_registry_record_properties_tbl_person_id
	foreign key (person_id)
	references ab_persons_tbl (id);

alter table common_registry_record_properties_tbl
	add index FK_common_registry_record_properties_tbl_apartment_id (apartment_id),
	add constraint FK_common_registry_record_properties_tbl_apartment_id
	foreign key (apartment_id)
	references ab_apartments_tbl (id);

alter table common_registry_record_properties_tbl
	add index FK_common_registry_record_properties_tbl_consumer_id (consumer_id),
	add constraint FK_common_registry_record_properties_tbl_consumer_id
	foreign key (consumer_id)
	references eirc_consumers_tbl (id);

alter table common_registry_record_properties_tbl
	add index FK_common_registry_record_properties_tbl_service_id (service_id),
	add constraint FK_common_registry_record_properties_tbl_service_id
	foreign key (service_id)
	references eirc_services_tbl (id);

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


update common_version_tbl set last_modified_date='2009-04-06', date_version=1;
