alter table common_users_tbl add column payment_collector_id bigint comment 'User payment collector id';

update common_version_tbl set last_modified_date='2009-07-28', date_version=0;