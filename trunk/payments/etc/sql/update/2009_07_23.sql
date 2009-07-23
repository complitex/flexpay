alter table payments_operations_tbl add column cashier_fio varchar(255) comment 'Cashier full name';

update common_version_tbl set last_modified_date='2009-07-23', date_version=0;