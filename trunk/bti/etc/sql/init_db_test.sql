-- initialize correction for a test attributes of CN building
insert into common_data_corrections_tbl(external_object_id, internal_object_id, object_type, data_source_description_id)
	values ('105471645', @buildings_ivanova_27, 0x1007, @source_description_test_data);

-- initialize some building attributes
insert into bti_building_attributes_tbl (attribute_type_id, building_id, begin_date, end_date, temporal_flag, string_value, value_type)
	values (@building_attr_type_build_year, @building_ivanova_27_id, '1960-06-05', '2000-12-31', 1, '1960', @TYPE_STRING);
insert into bti_building_attributes_tbl (attribute_type_id, building_id, begin_date, end_date, temporal_flag, int_value, value_type)
	values (@building_attr_type_build_year, @building_ivanova_27_id, '2001-01-01', '2100-12-31', 1, 1950, @TYPE_INT);
insert into bti_building_attributes_tbl (attribute_type_id, building_id, begin_date, end_date, temporal_flag, int_value, value_type)
	values (@building_attr_type_apartments_number, @building_ivanova_27_id, '1960-06-05', '2100-12-31', 1, 330, @TYPE_INT);
