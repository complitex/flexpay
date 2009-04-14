ALTER TABLE payments_operations_tbl CHANGE COLUMN registerUser register_user varchar(255) comment 'Register username';

update common_version_tbl set last_modified_date='2009-04-14', date_version=1;