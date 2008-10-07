alter table eirc_quittance_details_tbl
	modify column rate decimal(19,5) comment 'Rate';

update common_version_tbl set last_modified_date='2008-10-06', date_version=0;
