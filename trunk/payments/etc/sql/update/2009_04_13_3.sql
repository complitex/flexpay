create table payments_document_addition_type_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	type_id bigint not null comment 'Operation document addition type reference',
	primary key (id),
	unique (language_id, type_id)
) comment='Operation document addition type translations';

create table payments_document_addition_types_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	primary key (id)
) comment='Operation document addition types';

create table payments_document_additions_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	date_value datetime comment 'Optional date value',
	int_value integer comment 'Optional int value',
	bool_value bit comment 'Optional boolean value',
	long_value bigint comment 'Optional long value',
	string_value varchar(255) comment 'Optional string value',
	double_value double precision comment 'Optional double value',
	value_type integer not null comment 'Value type discriminator',
	addition_type_id bigint not null comment 'Addition type reference',
	document_id bigint not null comment 'Document reference',
	operation_id bigint not null,
	primary key (id)
) comment='Operation document additions';

create table payments_operation_addition_type_translations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	type_id bigint not null comment 'Operation addition type reference',
	primary key (id),
	unique (language_id, type_id)
) comment='Operation addition type translations';

create table payments_operation_addition_types_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	primary key (id)
) comment='Operation addition types';

create table payments_operation_additions_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	date_value datetime comment 'Optional date value',
	int_value integer comment 'Optional int value',
	bool_value bit comment 'Optional boolean value',
	long_value bigint comment 'Optional long value',
	string_value varchar(255) comment 'Optional string value',
	double_value double precision comment 'Optional double value',
	value_type integer not null comment 'Value type discriminator',
	addition_type_id bigint not null comment 'Addition type reference',
	operation_id bigint not null comment 'Operation reference',
	primary key (id)
) comment='Operation additions';

alter table payments_document_addition_type_translations_tbl
	add index FK_payments_document_add_type_translations_tbl_type_id (type_id),
	add constraint FK_payments_document_add_type_translations_tbl_type_id
	foreign key (type_id)
	references payments_document_addition_types_tbl (id);

alter table payments_document_addition_type_translations_tbl
	add index FK_payments_document_add_type_translations_tbl_lang_id (language_id),
	add constraint FK_payments_document_add_type_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_document_additions_tbl
	add index FK_payments_document_additions_tbl_document_id (document_id),
	add constraint FK_payments_document_additions_tbl_document_id
	foreign key (document_id)
	references payments_documents_tbl (id);

alter table payments_document_additions_tbl
	add index FK_payments_document_additions_tbl_addition_type_id (addition_type_id),
	add constraint FK_payments_document_additions_tbl_addition_type_id
	foreign key (addition_type_id)
	references payments_document_addition_types_tbl (id);

alter table payments_operation_addition_type_translations_tbl
	add index FK_payments_operation_add_type_translations_tbl_type_id (type_id),
	add constraint FK_payments_operation_add_type_translations_tbl_type_id
	foreign key (type_id)
	references payments_operation_addition_types_tbl (id);

alter table payments_operation_addition_type_translations_tbl
	add index FK_payments_operation_add_type_translations_tbl_lang_id (language_id),
	add constraint FK_payments_operation_add_type_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_operation_additions_tbl
	add index FK_payments_operation_additions_tbl_addition_type_id (addition_type_id),
	add constraint FK_payments_operation_additions_tbl_addition_type_id
	foreign key (addition_type_id)
	references payments_operation_addition_types_tbl (id);

alter table payments_operation_additions_tbl
	add index FK_payments_operation_additions_tbl_operation (operation_id),
	add constraint FK_payments_operation_additions_tbl_operation
	foreign key (operation_id)
	references payments_operations_tbl (id);

alter table payments_document_status_translations_tbl
		drop index status_id,
		drop index language_id_2,
		drop foreign key payments_document_status_translations_tbl_ibfk_1,
		drop foreign key payments_document_status_translations_tbl_ibfk_2;

alter table payments_document_type_translations_tbl
		drop index type_id,
		drop index language_id_2,
		drop foreign key payments_document_type_translations_tbl_ibfk_1,
		drop foreign key payments_document_type_translations_tbl_ibfk_2;

alter table payments_documents_tbl
		drop index debtor_organization_id,
		drop index creditor_organization_id,
		drop index reference_document_id,
		drop index status_id,
		drop index service_id;

alter table payments_documents_tbl
		drop foreign key payments_documents_tbl_ibfk_1,
		drop foreign key payments_documents_tbl_ibfk_2,
		drop foreign key payments_documents_tbl_ibfk_3,
		drop foreign key payments_documents_tbl_ibfk_4,
		drop foreign key payments_documents_tbl_ibfk_5;

alter table payments_document_status_translations_tbl
	add index FK_payments_document_status_translations_tbl_status_id (status_id),
	add constraint FK_payments_document_status_translations_tbl_status_id
	foreign key (status_id)
	references payments_document_statuses_tbl (id);

alter table payments_document_status_translations_tbl
	add index FK_payments_document_status_translations_tbl_lang_id (language_id),
	add constraint FK_payments_document_status_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_document_type_translations_tbl
	add index FK_payments_document_type_translations_tbl_type_id (type_id),
	add constraint FK_payments_document_type_translations_tbl_type_id
	foreign key (type_id)
	references payments_document_types_tbl (id);

alter table payments_document_type_translations_tbl
	add index FK_payments_document_type_translations_tbl_lang_id (language_id),
	add constraint FK_payments_document_type_translations_tbl_lang_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_debt_org_id (debtor_organization_id),
	add constraint FK_payments_documents_tbl_debt_org_id
	foreign key (debtor_organization_id)
	references orgs_organizations_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_credit_org_id (creditor_organization_id),
	add constraint FK_payments_documents_tbl_credit_org_id
	foreign key (creditor_organization_id)
	references orgs_organizations_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_ref_doc_id (reference_document_id),
	add constraint FK_payments_documents_tbl_ref_doc_id
	foreign key (reference_document_id)
	references payments_documents_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_status_id (status_id),
	add constraint FK_payments_documents_tbl_status_id
	foreign key (status_id)
	references payments_document_statuses_tbl (id);

alter table payments_documents_tbl
	add index FK_payments_documents_tbl_service_id (service_id),
	add constraint FK_payments_documents_tbl_service_id
	foreign key (service_id)
	references payments_services_tbl (id);

update common_version_tbl set last_modified_date='2009-04-14', date_version=3;
