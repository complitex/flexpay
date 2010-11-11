alter table common_certificates_tbl
		add column blocked bit comment 'Certificate blocked';

update common_version_tbl set last_modified_date='2010-11-11', date_version=0;