alter table common_users_tbl add column cash_box_id bigint comment 'User cash box id';

update common_version_tbl set last_modified_date='2010-10-28', date_version=0;
