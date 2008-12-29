ALTER TABLE apartment_numbers_tbl RENAME TO ab_apartment_numbers_tbl;
ALTER TABLE apartments_tbl RENAME TO ab_apartments_tbl;

ALTER TABLE buildingses_tbl RENAME TO ab_buildingses_tbl;
ALTER TABLE buildings_tbl RENAME TO ab_buildings_tbl;
ALTER TABLE building_statuses_tbl RENAME TO ab_building_statuses_tbl;
ALTER TABLE building_attributes_tbl RENAME TO ab_building_attributes_tbl;
ALTER TABLE building_attribute_types_tbl RENAME TO ab_building_attribute_types_tbl;
ALTER TABLE building_attribute_type_translations_tbl RENAME TO ab_building_attribute_type_translations_tbl;

ALTER TABLE ab_apartment_numbers_tbl
	MODIFY COLUMN value VARCHAR(255) NOT NULL COMMENT 'Apartment number value',
	MODIFY COLUMN apartment_id BIGINT(20) NOT NULL COMMENT 'Apartment reference';

ALTER TABLE ab_building_attributes_tbl
	MODIFY COLUMN value VARCHAR(255) NOT NULL COMMENT 'Building attribute value';

create index indx_value on ab_apartment_numbers_tbl (value);
create index indx_value on ab_building_attributes_tbl (value);

update common_version_tbl set last_modified_date='2008-07-04', date_version=1;
