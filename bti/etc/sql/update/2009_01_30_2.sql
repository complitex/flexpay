
alter table bti_building_attribute_types_tbl
	add column unique_code varchar(255) comment 'Internal unique code';

update common_version_tbl set last_modified_date='2009-01-30', date_version=2;
