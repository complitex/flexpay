INSERT INTO common_flexpay_modules_tbl (name) VALUES ('bti');
SELECT @module_bti:=last_insert_id();

-- init building attribute group
insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (1, 0, 0);
select @attribute_group_1:=1;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Другие', @ru_id, @attribute_group_1);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Other', @en_id, @attribute_group_1);

-- init simple building attributes
insert into bti_building_attribute_types_tbl (id, discriminator, group_id) values (1, 'simple', @attribute_group_1);
select @attr_type_color:=1;
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Цвет здания', @attr_type_color, @ru_id);
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Building color', @attr_type_color, @en_id);

insert into bti_building_attribute_types_tbl (id, discriminator, group_id) values (2, 'simple', @attribute_group_1);
select @attr_type_build_date:=2;
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Дата постройки', @attr_type_build_date, @ru_id);
insert into bti_building_attribute_type_names_tbl (name, attribute_type_id, language_id)
	values ('Build date', @attr_type_build_date, @en_id);

-- init enum building attributes
insert into bti_building_attribute_types_tbl (id, discriminator, group_id) values (3, 'enum', @attribute_group_1);
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
insert into bti_building_attributes_tbl (id, discriminator, building_id, attribute_type_id)
	values (1, 'tmp', @building_ivanova_27_id, @attr_type_color);
select @building_attr_1:=1;
insert into bti_building_attribute_temp_values_tbl (attribute_id, begin_date, end_date, attribute_value)
	values (@building_attr_1, '1900-01-01', '2100-12-31', 'серо-буро-малиновый');
insert into bti_building_attributes_tbl (id, discriminator, building_id, attribute_type_id)
	values (2, 'tmp', @building_ivanova_27_id, @attr_type_build_date);
select @building_attr_2:=2;
insert into bti_building_attribute_temp_values_tbl (attribute_id, begin_date, end_date, attribute_value)
	values (@building_attr_2, '1900-01-01', '2100-12-31', '1989/06/25');
insert into bti_building_attributes_tbl (id, discriminator, building_id, attribute_type_id, normal_attribute_value)
	values (3, 'normal', @building_ivanova_27_id, @attr_type_material, 'Фанера');
