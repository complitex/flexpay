
alter table accounting_documents_tbl
		add column registry_record_id bigint comment 'Optional registry record reference';

alter table accounting_documents_tbl
	add index FK_accounting_documents_tbl_registry_record_id (registry_record_id),
	add constraint FK_accounting_documents_tbl_registry_record_id
	foreign key (registry_record_id)
	references common_registry_records_tbl (id);

update common_version_tbl set last_modified_date='2009-04-09', date_version=0;
