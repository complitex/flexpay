ALTER TABLE ab_building_attribute_types_tbl
	drop column type,
	comment = 'Building attribute type (number, bulk, etc.)';

update common_version_tbl set last_modified_date='2008-07-17', date_version=0;
