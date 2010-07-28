
create table payments_document_status_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	status_id bigint not null comment 'Operation document status reference',
	primary key (id),
	unique (language_id, status_id)
) comment='Operation document status translations';

create table payments_document_statuses_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	code integer not null unique comment 'Status unique code',
	primary key (id)
) comment='Operation document statuses';

create table payments_document_type_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	type_id bigint not null comment 'Operation document type reference',
	primary key (id),
	unique (language_id, type_id)
) comment='Operation document type translations';

create table payments_document_types_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	code varchar(255) not null unique comment 'Type unique code',
	primary key (id)
) comment='Operation document types';

create table payments_documents_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	sum decimal(19,2) not null comment 'Summ',
	address varchar(255) comment 'Payer address',
	payer_fio varchar(255) comment 'Payer first-middle-last names',
	creditor_id varchar(255) comment 'Kreditor key',
	debtor_id varchar(255) comment 'Debtor key',
	creditor_organization_id bigint not null comment 'Creditor organization reference',
	debtor_organization_id bigint not null comment 'Debtor organization reference',
	operation_id bigint not null comment 'Operation reference',
	reference_document_id bigint not null comment 'Reference docume reference',
	registry_record_id bigint comment 'Optional registry record reference',
	type_id bigint not null comment 'Document type reference',
	status_id bigint not null comment 'Document type reference',
	service_id bigint not null comment 'Service reference',
	primary key (id)
) comment='Operation document';

alter table payments_operations_tbl
	add column address varchar(255) comment 'Payer address',
	add column payer_fio varchar(255) comment 'Payer first-middle-last names';

alter table payments_document_status_translations_tbl
	add index  (status_id),
	add constraint
	foreign key (status_id)
	references payments_document_statuses_tbl (id);

alter table payments_document_status_translations_tbl
	add index  (language_id),
	add constraint
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_document_type_translations_tbl
	add index  (type_id),
	add constraint
	foreign key (type_id)
	references payments_document_types_tbl (id);

alter table payments_document_type_translations_tbl
	add index  (language_id),
	add constraint
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_documents_tbl
	add index  (debtor_organization_id),
	add constraint
	foreign key (debtor_organization_id)
	references orgs_organizations_tbl (id);

alter table payments_documents_tbl
	add index  (creditor_organization_id),
	add constraint
	foreign key (creditor_organization_id)
	references orgs_organizations_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_registry_record_id (registry_record_id),
	add constraint FK_payments_documents_tbl_registry_record_id
	foreign key (registry_record_id)
	references common_registry_records_tbl (id);

alter table payments_documents_tbl
	add index  (reference_document_id),
	add constraint
	foreign key (reference_document_id)
	references payments_documents_tbl (id);

alter table payments_documents_tbl
	add index  (status_id),
	add constraint
	foreign key (status_id)
	references payments_document_statuses_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_document_type_id (type_id),
	add constraint FK_payments_documents_tbl_document_type_id
	foreign key (type_id)
	references payments_document_types_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_payments_operation_id (operation_id),
	add constraint FK_payments_documents_tbl_payments_operation_id
	foreign key (operation_id)
	references payments_operations_tbl (id);

alter table payments_documents_tbl
	add index  (service_id),
	add constraint
	foreign key (service_id)
	references payments_services_tbl (id);

update common_version_tbl set last_modified_date='2009-04-13', date_version=1;
