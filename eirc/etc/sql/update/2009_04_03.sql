
alter table accounting_documents_tbl
		drop index FK_accounting_documents_tbl_registry_record_id,
		drop foreign key FK_accounting_documents_tbl_registry_record_id,
		drop column registry_record_id;

alter table accounting_eirc_subjects_tbl
drop index FK_accounting_eirc_subjects_tbl_account_id,
drop foreign key FK_accounting_eirc_subjects_tbl_account_id,
		drop column eirc_account_id;

alter table accounting_operations_tbl
drop index FK_accounting_operations_tbl_registry_record_id,
drop foreign key FK_accounting_operations_tbl_registry_record_id,
		drop column registry_record_id;

drop table eirc_ticket_service_amounts_tbl;
drop table eirc_tickets_tbl;


update common_version_tbl set last_modified_date='2009-04-03', date_version=1;
