alter table common_certificates_tbl add column begin_date date;

alter table common_certificates_tbl add column end_date date;

update common_version_tbl set last_modified_date='2010-04-08', date_version=0;