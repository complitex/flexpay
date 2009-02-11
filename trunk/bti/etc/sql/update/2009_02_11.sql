alter table bti_building_attribute_types_tbl
	add column is_temporal integer not null comment 'Temporal flag';

update bti_building_attribute_types_tbl set is_temporal=0;

update common_version_tbl set last_modified_date='2009-02-11', date_version=0;
