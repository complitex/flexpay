insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (4, 0, 0, 'ATTR_NUMBER_TENANTS', 0, 'simple', null);
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (5, 0, 0, 'ATTR_NUMBER_REGISTERED_TENANTS', 0, 'simple', null);
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (6, 0, 0, 'ATTR_TOTAL_SQUARE', 0, 'simple', null);
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (7, 0, 0, 'ATTR_LIVE_SQUARE', 0, 'simple', null);
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (8, 0, 0, 'ATTR_HEATING_SQUARE', 0, 'simple', null);
select @cons_attr_type_number_tenats:=4;
select @cons_attr_type_number_registered_tenats:=5;
select @cons_attr_type_total_square:=6;
select @cons_attr_type_live_square:=7;
select @cons_attr_type_heating_square:=8;
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Количество проживающих', @ru_id, @cons_attr_type_number_tenats);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Количество зарегистрированных', @ru_id, @cons_attr_type_number_registered_tenats);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Общая площадь (приведенная)', @ru_id, @cons_attr_type_total_square);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Жилая площадь', @ru_id, @cons_attr_type_live_square);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Отапливаемая площадь', @ru_id, @cons_attr_type_heating_square);

update common_version_tbl set last_modified_date='2010-08-26', date_version=0;