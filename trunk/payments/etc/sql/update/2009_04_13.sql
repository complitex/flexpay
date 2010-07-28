drop table accounting_documents_tbl;
drop table accounting_operations_tbl;
drop table accounting_document_types_tbl;
drop table accounting_eirc_subjects_tbl;

create table payments_operation_level_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	level_id bigint not null comment 'Operation level reference',
	primary key (id),
	unique (language_id, level_id)
) comment='Operation level translations';

create table payments_operation_levels_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	code integer not null unique comment 'Level code',
	primary key (id)
) comment='Operation levels';

create table payments_operation_status_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	status_id bigint not null comment 'Operation status reference',
	primary key (id),
	unique (language_id, status_id)
) comment='Operation status translations';

create table payments_operation_statuses_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	code integer not null unique comment 'Status code',
	primary key (id)
) comment='Operation statuses';

create table payments_operation_type_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	type_id bigint not null comment 'Operation type reference',
	primary key (id),
	unique (language_id, type_id)
) comment='Operation type translations';

create table payments_operation_types_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	code integer not null unique comment 'Type code',
	primary key (id)
) comment='Operation types';

create table payments_operations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	operation_summ decimal(19,2) not null comment 'Operation sum',
	operation_input_summ decimal(19,2) comment 'Operation input sum',
	change_summ decimal(19,2) comment 'Change',
	creator varchar(255) not null comment 'Creator username',
	creation_date datetime not null comment 'Creation date',
	confirmator varchar(255) comment 'Confirmator username',
	confirmation_date datetime comment 'Confirmation date',
	level_id bigint not null comment 'Operation level reference',
	status_id bigint not null comment 'Operation status reference',
	type_id bigint not null comment 'Operation type reference (operation code)',
	creator_organization_id bigint not null comment 'Organization operation created in',
	confirmator_organization_id bigint comment 'Organization operation confirmed in',
	registry_record_id bigint comment 'Registry record',
	parent_operation_id bigint comment 'Optional parent operation reference',
	primary key (id)
) comment='Operations';

alter table payments_operation_level_translations_tbl
	add index FK_payments_operation_level_translations_tbl_type_id (level_id),
	add constraint FK_payments_operation_level_translations_tbl_type_id
	foreign key (level_id)
	references payments_operation_levels_tbl (id);

alter table payments_operation_level_translations_tbl
	add index FK_payments_operation_level_translations_tbl_lang_id (language_id),
	add constraint FK_payments_operation_level_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_operation_status_translations_tbl
	add index FK_payments_operation_status_translations_tbl_type_id (status_id),
	add constraint FK_payments_operation_status_translations_tbl_type_id
	foreign key (status_id)
	references payments_operation_statuses_tbl (id);

alter table payments_operation_status_translations_tbl
	add index FK_payments_operation_status_translations_tbl_lang_id (language_id),
	add constraint FK_payments_operation_status_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_operation_type_translations_tbl
	add index FK_payments_operation_type_translations_tbl_type_id (type_id),
	add constraint FK_payments_operation_type_translations_tbl_type_id
	foreign key (type_id)
	references payments_operation_types_tbl (id);

alter table payments_operation_type_translations_tbl
	add index FK_payments_operation_type_translations_tbl_lang_id (language_id),
	add constraint FK_payments_operation_type_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_registry_record_id (registry_record_id),
	add constraint FK_payments_operations_tbl_registry_record_id
	foreign key (registry_record_id)
	references common_registry_records_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_status_id (status_id),
	add constraint FK_payments_operations_tbl_status_id
	foreign key (status_id)
	references payments_operation_statuses_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_confirmator_organization_id (confirmator_organization_id),
	add constraint FK_payments_operations_tbl_confirmator_organization_id
	foreign key (confirmator_organization_id)
	references orgs_organizations_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_level_id (level_id),
	add constraint FK_payments_operations_tbl_level_id
	foreign key (level_id)
	references payments_operation_levels_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_type_id (type_id),
	add constraint FK_payments_operations_tbl_type_id
	foreign key (type_id)
	references payments_operation_types_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_creator_organization_id (creator_organization_id),
	add constraint FK_payments_operations_tbl_creator_organization_id
	foreign key (creator_organization_id)
	references orgs_organizations_tbl (id);

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_parent_id (parent_operation_id),
	add constraint FK_payments_operations_tbl_parent_id
	foreign key (parent_operation_id)
	references payments_operations_tbl (id);

update common_version_tbl set last_modified_date='2009-04-13', date_version=0;
