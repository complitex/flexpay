alter table common_users_tbl
	add column payments_payment_point_id bigint comment 'User payment point';

update common_version_tbl set last_modified_date='2009-07-17', date_version=2;
