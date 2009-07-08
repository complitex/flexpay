alter table payments_document_additions_tbl add column decimal_value decimal(19,5) comment 'Optional decimal value';
alter table payments_operation_additions_tbl add column decimal_value decimal(19,5) comment 'Optional decimal value';
update common_version_tbl set last_modified_date='2009-07-08', date_version=2;