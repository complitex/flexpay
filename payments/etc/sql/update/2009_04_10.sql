create table payments_service_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	language_id bigint not null comment 'Language reference',
	service_id bigint not null comment 'Service reference',
	primary key (id),
	unique (language_id, service_id)
);

create table payments_service_type_name_translations_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null,
	description varchar(255) not null,
	language_id bigint not null comment 'Language reference',
	service_type_id bigint not null comment 'Service type reference',
	primary key (id),
	unique (language_id, service_type_id)
);

create table payments_service_types_tbl (
	id bigint not null auto_increment,
	status integer not null,
	code integer not null,
	primary key (id)
);

create table payments_services_tbl (
	id bigint not null auto_increment,
	external_code varchar(255) comment 'Service providers internal service code',
	begin_date date not null comment 'The Date service is valid from',
	end_date date not null comment 'The Date service is valid till',
	provider_id bigint not null comment 'Service provider reference',
	type_id bigint not null comment 'Service type reference',
	measure_unit_id bigint comment 'Measure unit reference',
	parent_service_id bigint comment 'If parent service reference present service is a subservice',
	primary key (id)
);

alter table payments_service_descriptions_tbl
	add index FK_payments_service_description_service (service_id),
	add constraint FK_payments_service_description_service
	foreign key (service_id)
	references payments_services_tbl (id);

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

update common_version_tbl set last_modified_date='2009-04-10', date_version=0;
