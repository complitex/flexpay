-- rollback payments update for 2009_04_10_0
drop table payments_service_descriptions_tbl;
drop table payments_service_type_name_translations_tbl;
drop table payments_services_tbl;
drop table payments_service_types_tbl;


-- drop old foreign keys
alter table eirc_service_descriptions_tbl
	drop index FK_eirc_service__description_service,
	drop foreign key FK_eirc_service__description_service;

alter table eirc_service_descriptions_tbl
	drop index FK_eirc_service_desciption_language,
	drop foreign key FK_eirc_service_desciption_language;

alter table eirc_service_type_name_translations_tbl
	drop index FK_eirc_service_type_name_translation_service_type,
	drop foreign key FK_eirc_service_type_name_translation_service_type;

alter table eirc_service_type_name_translations_tbl
	drop index FK_eirc_service_type_name_translation_language,
	drop foreign key FK_eirc_service_type_name_translation_language;

alter table eirc_services_tbl
	drop index INDX_eirc_service_external_code;

alter table eirc_services_tbl
	drop index FK_eirc_services_tbl_measure_unit_id,
	drop foreign key FK_eirc_services_tbl_measure_unit_id;

alter table eirc_services_tbl
	drop index FK_eirc_service_parent_service_id,
	drop foreign key FK_eirc_service_parent_service_id;

alter table eirc_services_tbl
	drop index FK_eirc_service_service_provider,
	drop foreign key FK_eirc_service_service_provider;

alter table eirc_services_tbl
	drop index FK_eirc_service_service_type,
	drop foreign key FK_eirc_service_service_type;

rename table eirc_service_descriptions_tbl to payments_service_descriptions_tbl;
rename table eirc_service_type_name_translations_tbl to payments_service_type_name_translations_tbl;
rename table eirc_service_types_tbl to payments_service_types_tbl;
rename table eirc_services_tbl to payments_services_tbl;

alter table payments_service_descriptions_tbl
	add index FK_payments_service_desciption_language (language_id),
	add constraint FK_payments_service_desciption_language
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_service_type_name_translations_tbl
	add index FK_payments_service_type_name_translation_service_type (service_type_id),
	add constraint FK_payments_service_type_name_translation_service_type
	foreign key (service_type_id)
	references payments_service_types_tbl (id);

alter table payments_service_type_name_translations_tbl
	add index FK_payments_service_type_name_translation_language (language_id),
	add constraint FK_payments_service_type_name_translation_language
	foreign key (language_id)
	references common_languages_tbl (id);

create index INDX_payments_service_external_code on payments_services_tbl (external_code);

alter table payments_services_tbl
	add index FK_payments_services_tbl_measure_unit_id (measure_unit_id),
	add constraint FK_payments_services_tbl_measure_unit_id
	foreign key (measure_unit_id)
	references common_measure_units_tbl (id);

alter table payments_services_tbl
	add index FK_payments_service_parent_service_id (parent_service_id),
	add constraint FK_payments_service_parent_service_id
	foreign key (parent_service_id)
	references payments_services_tbl (id);

alter table payments_services_tbl
	add index FK_payments_service_service_provider (provider_id),
	add constraint FK_payments_service_service_provider
	foreign key (provider_id)
	references orgs_service_providers_tbl (id);

alter table payments_services_tbl
	add index FK_payments_service_service_type (type_id),
	add constraint FK_payments_service_service_type
	foreign key (type_id)
	references payments_service_types_tbl (id);

update common_version_tbl set last_modified_date='2009-04-10', date_version=1;
