-- initialize correction for a test attributes of CN building
insert into common_data_corrections_tbl(external_object_id, internal_object_id, object_type, data_source_description_id)
	values ('105471645', @buildings_ivanova_27, 0x1007, @source_description_test_data);
