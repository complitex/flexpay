alter table bti_apartment_attribute_types_tbl
	add column status integer not null comment 'Enabled/Disabled status';

alter table bti_building_attribute_types_tbl
	add column status integer not null comment 'Enabled/Disabled status';

update common_version_tbl set last_modified_date='2009-10-28', date_version=0;
