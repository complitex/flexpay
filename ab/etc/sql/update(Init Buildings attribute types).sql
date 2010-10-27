SELECT @ru_id:=(select id from common_languages_tbl where lang_iso_code='ru');
SELECT @en_id:=(select id from common_languages_tbl where lang_iso_code='en');

-- Init Buildings attribute types
INSERT INTO ab_building_address_attribute_types_tbl (id, status) VALUES (1, 0);
SELECT @attr_type_home_number_id:=1;
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Номер дома', 'д', @attr_type_home_number_id, @ru_id);
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Home number', '', @attr_type_home_number_id, @en_id);

INSERT INTO ab_building_address_attribute_types_tbl (id, status) VALUES (2, 0);
SELECT @attr_type_bulk_id:=2;
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Корпус', 'к', @attr_type_bulk_id, @ru_id);
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Bulk', '', @attr_type_bulk_id, @en_id);

INSERT INTO ab_building_address_attribute_types_tbl (id, status) VALUES (3, 0);
SELECT @attr_type_part_id:=3;
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Часть', 'ч', @attr_type_part_id, @ru_id);
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Part', 'p', @attr_type_part_id, @en_id);

select @source_description_master_index:=(select id from common_data_source_descriptions_tbl where description='Master-Index');

-- add master correction for the address attribute types
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@attr_type_home_number_id, 0x1000 + 0x0B, concat('COMMON_INSTANCE-', @attr_type_home_number_id), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@attr_type_bulk_id, 0x1000 + 0x0B, concat('COMMON_INSTANCE-', @attr_type_bulk_id), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@attr_type_part_id, 0x1000 + 0x0B, concat('COMMON_INSTANCE-', @attr_type_part_id), @source_description_master_index);
