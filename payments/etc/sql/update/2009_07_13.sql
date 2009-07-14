alter table payments_document_addition_types_tbl add column code integer comment 'Document addition type code';

update common_version_tbl set last_modified_date='2009-07-13', date_version=0;