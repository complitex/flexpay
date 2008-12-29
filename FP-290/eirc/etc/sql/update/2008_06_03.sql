ALTER TABLE eirc_services_tbl
	ADD COLUMN parent_service_id BIGINT COMMENT 'If parent service reference present service is a subservice';

alter table eirc_services_tbl
	add index FK_eirc_service_parent_service_id (parent_service_id),
	add constraint FK_eirc_service_parent_service_id
	foreign key (parent_service_id)
	references eirc_services_tbl (id);
