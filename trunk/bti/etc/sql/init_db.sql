INSERT INTO common_flexpay_modules_tbl (name) VALUES ('bti');
SELECT @module_bti:=last_insert_id();

-- init simple building attributes
insert into bti_building_attribute_types_tbl (id, discriminator) values (1, 'simple');
select @attr_type_color:=1;
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Цвет здания', @attr_type_color, @ru_id);
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Building color', @attr_type_color, @en_id);

insert into bti_building_attribute_types_tbl (id, discriminator) values (2, 'simple');
select @attr_type_build_date:=2;
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Дата постройки', @attr_type_build_date, @ru_id);
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Build date', @attr_type_build_date, @en_id);

-- init enum building attributes
insert into bti_building_attribute_types_tbl (id, discriminator) values (3, 'enum');
select @attr_type_material:=3;
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Материал стен', @attr_type_material, @ru_id);
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Wall material', @attr_type_material, @en_id);
insert into bti_building_attribute_type_enum_values_tbl (attribute_type_enum_id, order_value, value)
	values (@attr_type_material, 1, 'Кирпич');
insert into bti_building_attribute_type_enum_values_tbl (attribute_type_enum_id, order_value, value)
	values (@attr_type_material, 2, 'Фанера');
insert into bti_building_attribute_type_enum_values_tbl (attribute_type_enum_id, order_value, value)
	values (@attr_type_material, 17, 'Пластилин');
insert into bti_building_attribute_type_enum_values_tbl (attribute_type_enum_id, order_value, value)
	values (@attr_type_material, 35, 'Дуб');
insert into bti_building_attribute_type_enum_values_tbl (attribute_type_enum_id, order_value, value)
	values (@attr_type_material, 101, 'Воздух');
insert into bti_building_attribute_type_enum_values_tbl (attribute_type_enum_id, order_value, value)
	values (@attr_type_material, 4, 'Лед');

-- init some building attributes
insert into bti_building_attributes_tbl (id, discriminator, building_id, begin_date, end_date, attribute_type_id, attribute_value)
	values (1, 'tmp', @building_ivanova_27_id, '1900-01-01', '2100-12-31', @attr_type_color, 'серо-буро-малиновый');
insert into bti_building_attributes_tbl (id, discriminator, building_id, begin_date, end_date, attribute_type_id, attribute_value)
	values (2, 'tmp', @building_ivanova_27_id, '1900-01-01', '2100-12-31', @attr_type_build_date, '1989/06/25');
insert into bti_building_attributes_tbl (id, discriminator, building_id, attribute_type_id, attribute_value)
	values (3, 'normal', @building_ivanova_27_id, @attr_type_material, 'Фанера');
