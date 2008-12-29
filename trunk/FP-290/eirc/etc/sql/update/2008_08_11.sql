ALTER TABLE eirc_registry_records_tbl 
	add column service_id bigint comment 'Service reference';

alter table eirc_registry_records_tbl
	add index FK_eirc_registry_records_tbl_service_id (service_id),
	add constraint FK_eirc_registry_records_tbl_service_id
	foreign key (service_id)
	references eirc_services_tbl (id);

update common_version_tbl set last_modified_date='2008-08-11', date_version=0;
