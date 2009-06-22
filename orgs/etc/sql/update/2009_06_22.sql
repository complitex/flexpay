alter table orgs_payments_collectors_tbl
	add column email varchar(255) comment 'Collector email address';

update common_version_tbl set last_modified_date='2009-06-22', date_version=0;