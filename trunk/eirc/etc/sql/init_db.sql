INSERT INTO common_flexpay_modules_tbl (name) VALUES ('eirc');
SELECT @module_eirc:=last_insert_id();

-- Init Sequences table
INSERT INTO common_sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');

-- Consumer attribute types
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (1, 0, 0, 'ATTR_ERC_ACCOUNT', 0, 'simple', null);
select @cons_attr_type_erc_account:=1;
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Счет ЕРЦ (Мегабанк)', @ru_id, @cons_attr_type_erc_account);
