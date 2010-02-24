-- Consumer attribute types
select @ru_id:=id from common_languages_tbl where lang_iso_code='ru';

insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (1, 0, 0, 'ATTR_ERC_ACCOUNT', 0, 'simple', null);
select @cons_attr_type_erc_account:=1;
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Счёт ЕРЦ (Мегабанк)', @ru_id, @cons_attr_type_erc_account);

update common_version_tbl set last_modified_date='2009-07-08', date_version=3;
