alter table eirc_service_types_tbl add column code integer not null;

UPDATE common_version_tbl SET last_modified_date='2007-03-07', date_version=1;