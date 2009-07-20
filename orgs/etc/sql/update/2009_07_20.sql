alter table orgs_service_providers_tbl
	add column email varchar(255) comment 'E-mail';

update common_version_tbl set last_modified_date='2009-07-20', date_version=0;