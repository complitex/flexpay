
rename table ab_buildingses_tbl to ab_building_addresses_tbl;
rename table ab_building_attributes_tbl to ab_building_address_attributes_tbl;
rename table ab_building_attribute_types_tbl to ab_building_address_attribute_types_tbl;
rename table ab_building_attribute_type_translations_tbl to ab_building_adress_attribute_type_translations_tbl;

update common_version_tbl set last_modified_date='2009-01-22', date_version=0;
