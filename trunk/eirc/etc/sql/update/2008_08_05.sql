ALTER TABLE eirc_service_organizations_tbl
	DROP FOREIGN KEY FK_eirc_service_organization_district;
alter table eirc_service_organizations_tbl drop column district_id;

update common_version_tbl set last_modified_date='2008-08-05', date_version=0;
