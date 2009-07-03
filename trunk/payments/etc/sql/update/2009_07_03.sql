alter table payments_operations_tbl add column uid bigint;

update common_version_tbl set last_modified_date='2009-07-03', date_version=1;
