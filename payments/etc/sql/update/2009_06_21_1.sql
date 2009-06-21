alter table payments_operations_tbl
	drop foreign key FK_payments_cashboxes_tbl_cashbox_id;

drop table payments_cashbox_name_translations_tbl;

drop table payments_cashboxes_tbl;

update common_version_tbl set last_modified_date='2009-06-21', date_version=1;