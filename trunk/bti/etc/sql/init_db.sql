INSERT INTO common_flexpay_modules_tbl (name) VALUES ('bti');
SELECT @module_bti:=last_insert_id();

insert into bti_building_temp_attributes_tbl (id, building_id, begin_date, end_date, attribute_name, attribute_value)
	values (1, @building_ivanova_27_id, '1900-01-01', '2100-12-31', 'color', 'серо-буро-малиновый');
insert into bti_building_temp_attributes_tbl (id, building_id, begin_date, end_date, attribute_name, attribute_value)
	values (2, @building_ivanova_27_id, '1900-01-01', '2100-12-31', 'build_date', '1989/06/25');
