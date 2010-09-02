-- Add indexes
alter table eirc_eirc_accounts_tbl
		add unique index I_eirc_eirc_accounts_account_number (account_number);

alter table eirc_consumer_attributes_tbl
		add unique index I_stringvalue_begindate_enddate_consumerid_typeid (string_value, begin_date, end_date, consumer_id, type_id);

INSERT INTO common_flexpay_modules_tbl (name) VALUES ('eirc');
SELECT @module_eirc:=last_insert_id();

-- Init Sequences table
INSERT INTO common_sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');

-- Consumer attribute types
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (1, 0, 0, 'ATTR_ERC_ACCOUNT', 0, 'simple', null);
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
select @cons_attr_type_erc_account:=1;
select @cons_attr_type_number_tenats:=4;
select @cons_attr_type_number_registered_tenats:=5;
select @cons_attr_type_total_square:=6;
select @cons_attr_type_live_square:=7;
select @cons_attr_type_heating_square:=8;
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Счёт ЕРЦ (Мегабанк)', @ru_id, @cons_attr_type_erc_account);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Количество проживающих', @ru_id, @cons_attr_type_number_tenats);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Количество зарегистрированных', @ru_id, @cons_attr_type_number_registered_tenats);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Общая площадь (приведённая)', @ru_id, @cons_attr_type_total_square);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Жилая площадь', @ru_id, @cons_attr_type_live_square);
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Отапливаемая площадь', @ru_id, @cons_attr_type_heating_square);

