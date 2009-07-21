alter table payments_document_additions_tbl drop column operation_id;

update common_version_tbl set last_modified_date='2009-07-21', date_version=0;
