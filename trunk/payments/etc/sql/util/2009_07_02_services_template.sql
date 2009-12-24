-- AHTUNG!!! TEST DATA Additional service types 
SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
select @eirc_base:=0x5000 + 0;
select @orgs_base:=0x4000 + 0;
select @ds_megabank:=@eirc_base + 1;

SELECT @service_provider:=;
SELECT @provider_ext_id:=;
-- add correction from Megabank's enterprize
delete from common_data_corrections_tbl
where internal_object_id=@service_provider and
		external_object_id=@provider_ext_id and
		object_type=@orgs_base + 0x003 and
		data_source_description_id=@ds_megabank;
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	values (@service_provider, @provider_ext_id, @orgs_base + 0x003, @ds_megabank);


SELECT @service_t_electricity:=1002;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_electricity, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_electricity:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Электроэнергия', @ru_id, @service_electricity);

SELECT @service_t_shed:=1003;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_shed, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_shed:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Сарай', @ru_id, @service_shed);

SELECT @service_t_larder:=1004;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_larder, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_larder:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Кладовая', @ru_id, @service_larder);

SELECT @service_t_household_consumptions:=1005;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_household_consumptions, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_household_consumptions:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Хозрасходы', @ru_id, @service_household_consumptions);

SELECT @service_t_sewerage:=1006;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_sewerage, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_sewerage:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Канализация', @ru_id, @service_sewerage);

SELECT @service_t_coocking_gas:=1007;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_coocking_gas, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_coocking_gas:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Газ варочный', @ru_id, @service_coocking_gas);

SELECT @service_t_heating_gas:=1008;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_heating_gas, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_heating_gas:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Газ отопительный', @ru_id, @service_heating_gas);

SELECT @service_t_radio:=1009;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_radio, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_radio:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Радио', @ru_id, @service_radio);

SELECT @service_t_antenna:=1010;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_antenna, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_antenna:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Антенна', @ru_id, @service_antenna);

SELECT @service_t_phone:=1011;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_phone, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_phone:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Телефон', @ru_id, @service_phone);

SELECT @service_t_cesspool_cleaning:=1012;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_cesspool_cleaning, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_cesspool_cleaning:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Ассенизация', @ru_id, @service_cesspool_cleaning);

SELECT @service_t_lift:=1013;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_lift, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_lift:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Лифт', @ru_id, @service_lift);

SELECT @service_t_ground_tax:=1014;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_ground_tax, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_ground_tax:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Налог на землю', @ru_id, @service_ground_tax);

SELECT @service_t_repeat_turn_on:=1015;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_repeat_turn_on, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_repeat_turn_on:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Повторное подключение', @ru_id, @service_repeat_turn_on);

SELECT @service_t_acts_payment:=1016;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_acts_payment, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_acts_payment:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Оплата по актам', @ru_id, @service_acts_payment);

SELECT @service_t_counters_repair:=1017;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_counters_repair, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_counters_repair:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Ремонт счётчиков', @ru_id, @service_counters_repair);

SELECT @service_t_pets:=3;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_pets, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_pets:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Содержание животных', @ru_id, @service_pets);

SELECT @service_t_garage:=4;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_garage, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_garage:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Гараж', @ru_id, @service_garage);

SELECT @service_t_heating:=5;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_heating, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_heating:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Отопление', @ru_id, @service_heating);

SELECT @service_t_cold_water:=7;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_cold_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_cold_water:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Холодная вода', @ru_id, @service_cold_water);

SELECT @service_t_hot_water:=8;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_hot_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_hot_water:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Горячая вода', @ru_id, @service_hot_water);

SELECT @service_t_kvarplata:=9;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_kvarplata, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_kvarplata:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Кварплата', @ru_id, @service_kvarplata);

SELECT @service_t_basement:=38;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider, null, null, @service_t_basement, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_basement:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Погреб', @ru_id, @service_basement);

