create table accounting_document_types_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	name varchar(255) not null comment 'Type name',
	code varchar(255) not null unique comment 'Type unique code',
	primary key (id)
) comment='Operation document types';

create table accounting_documents_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	summ decimal(19,2) not null comment 'Summ',
	status integer not null comment 'Document status',
	subject_debet_id bigint not null comment 'Debet subject reference',
	subject_credit_id bigint not null comment 'Credit subject reference',
	operation_id bigint not null comment 'Operation reference',
	registry_record_id bigint comment 'Optional registry record reference',
	type_id bigint not null comment 'Document type reference',
	primary key (id)
) comment='Operation document';

create table accounting_eirc_subjects_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	eirc_account_id bigint comment 'Optional eirc account reference',
	organization_id bigint not null comment 'Subject organisation reference',
	primary key (id)
) comment='Operation document subjects';

create table accounting_operations_tbl (
	id bigint not null auto_increment comment 'Primary key',
	PAYMENT_TYPE varchar(255) not null comment 'Various operation types descriminator',
	version integer not null comment 'Optimistic lock version',
	operation_summ decimal(19,2) not null comment 'Operation summ',
	operation_input_summ decimal(19,2) not null comment 'Operation input summ',
	change_summ decimal(19,2) not null comment 'Change',
	creator varchar(255) not null comment 'Creator username',
	creation_date datetime not null comment 'Creation date',
	confirmator varchar(255) not null comment 'Confirmator username',
	confirmation_date datetime not null comment 'Confirmation date',
	level integer not null comment 'Operation level',
	status integer not null comment 'Operation status',
	creator_organization_id bigint not null comment 'Organization operation created in',
	confirmator_organization_id bigint not null comment 'Organization operation confirmed in',
	registry_record_id bigint not null comment 'Registry record',
	parent_operation_id bigint comment 'Optional parent operation reference',
	primary key (id)
) comment='Operations';

alter table accounting_documents_tbl
	add index FK_accounting_documents_tbl_subject_debet_id (subject_debet_id),
	add constraint FK_accounting_documents_tbl_subject_debet_id
	foreign key (subject_debet_id)
	references accounting_eirc_subjects_tbl (id);

alter table accounting_documents_tbl
	add index FK_accounting_documents_tbl_subject_credit_id (subject_credit_id),
	add constraint FK_accounting_documents_tbl_subject_credit_id
	foreign key (subject_credit_id)
	references accounting_eirc_subjects_tbl (id);

alter table accounting_documents_tbl
	add index FK_accounting_documents_tbl_registry_record_id (registry_record_id),
	add constraint FK_accounting_documents_tbl_registry_record_id
	foreign key (registry_record_id)
	references eirc_registry_records_tbl (id);

alter table accounting_documents_tbl
	add index FK_accounting_documents_tbl_document_type_id (type_id),
	add constraint FK_accounting_documents_tbl_document_type_id
	foreign key (type_id)
	references accounting_document_types_tbl (id);

alter table accounting_documents_tbl
	add index FK_accounting_documents_tbl_accounting_operation_id (operation_id),
	add constraint FK_accounting_documents_tbl_accounting_operation_id
	foreign key (operation_id)
	references accounting_operations_tbl (id);

alter table accounting_eirc_subjects_tbl
	add index FK_accounting_eirc_subjects_tbl_organization_id (organization_id),
	add constraint FK_accounting_eirc_subjects_tbl_organization_id
	foreign key (organization_id)
	references eirc_organizations_tbl (id);

alter table accounting_eirc_subjects_tbl
	add index FK_accounting_eirc_subjects_tbl_account_id (eirc_account_id),
	add constraint FK_accounting_eirc_subjects_tbl_account_id
	foreign key (eirc_account_id)
	references eirc_eirc_accounts_tbl (id);

alter table accounting_operations_tbl
	add index FK_accounting_operations_tbl_registry_record_id (registry_record_id),
	add constraint FK_accounting_operations_tbl_registry_record_id
	foreign key (registry_record_id)
	references eirc_registry_records_tbl (id);

alter table accounting_operations_tbl
	add index FK_accounting_operations_tbl_confirmator_organization_id (confirmator_organization_id),
	add constraint FK_accounting_operations_tbl_confirmator_organization_id
	foreign key (confirmator_organization_id)
	references eirc_organizations_tbl (id);

alter table accounting_operations_tbl
	add index FK_accounting_operations_tbl_creator_organization_id (creator_organization_id),
	add constraint FK_accounting_operations_tbl_creator_organization_id
	foreign key (creator_organization_id)
	references eirc_organizations_tbl (id);

alter table accounting_operations_tbl
	add index FK_accounting_operations_tbl_parent_id (parent_operation_id),
	add constraint FK_accounting_operations_tbl_parent_id
	foreign key (parent_operation_id)
	references accounting_operations_tbl (id);

update common_version_tbl set last_modified_date='2009-03-27', date_version=0;
