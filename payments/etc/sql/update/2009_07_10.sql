alter table payments_documents_tbl add column debt decimal(19,2) comment 'Debt sum';

update common_version_tbl set last_modified_date='2009-07-10', date_version=0;