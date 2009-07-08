alter table payments_operations_tbl drop column uid;

update common_version_tbl set last_modified_date='2009-07-08', date_version=0;