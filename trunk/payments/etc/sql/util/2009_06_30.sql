-- AHTUNG!!! TEST DATA Additional service types 
SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
SELECT @service_provider_cn:=1;

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1002, 0, 1002);
SELECT @service_t_electricity:=1002;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Электроэнергия', 'Описание', @ru_id, @service_t_electricity);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1003, 0, 1003);
SELECT @service_t_shed:=1003;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Сарай', 'Описание', @ru_id, @service_t_shed);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1004, 0, 1004);
SELECT @service_t_larder:=1004;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Кладовая', 'Описание', @ru_id, @service_t_larder);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1005, 0, 1005);
SELECT @service_t_household_consumptions:=1005;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Хозрасходы', 'Описание', @ru_id, @service_t_household_consumptions);



INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_electricity, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_electricity:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Электроэнергия', @ru_id, @service_electricity);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_shed, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_shed:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Сарай', @ru_id, @service_shed);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_larder, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_larder:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Кладовая', @ru_id, @service_larder);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_household_consumptions, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_household_consumptions:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Хозрасходы', @ru_id, @service_household_consumptions);

-- MegaBank integration data
select @eirc_base:=0x5000 + 0;
select @orgs_base:=0x4000 + 0;

insert into common_data_source_descriptions_tbl (id, description)
	values (@eirc_base + 1, 'МегаБАНК');
select @ds_megabank:=@eirc_base + 1;

-- add correction from Megabank's КП "ЖИЛКОМСЕРВИС"=1033
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	values (@service_provider_cn, '1033', @orgs_base + 0x003, @ds_megabank);

-- updated 2009-07-02 (added more service types)
SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
SELECT @service_provider_cn:=1;

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1006, 0, 1006);
SELECT @service_t_sewerage:=1006;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Канализация', '', @ru_id, @service_t_sewerage);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_sewerage, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_sewerage:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Канализация', @ru_id, @service_sewerage);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1007, 0, 1007);
SELECT @service_t_coocking_gas:=1007;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Газ варочный', '', @ru_id, @service_t_coocking_gas);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_coocking_gas, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_coocking_gas:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Газ варочный', @ru_id, @service_coocking_gas);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1008, 0, 1008);
SELECT @service_t_heating_gas:=1008;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Газ отопительный', '', @ru_id, @service_t_heating_gas);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_heating_gas, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_heating_gas:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Газ отопительный', @ru_id, @service_heating_gas);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1009, 0, 1009);
SELECT @service_t_radio:=1009;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Радио', '', @ru_id, @service_t_radio);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_radio, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_radio:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Радио', @ru_id, @service_radio);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1010, 0, 1010);
SELECT @service_t_antenna:=1010;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Антенна', '', @ru_id, @service_t_antenna);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_antenna, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_antenna:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Антенна', @ru_id, @service_antenna);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1011, 0, 1011);
SELECT @service_t_phone:=1011;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Телефон', '', @ru_id, @service_t_phone);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_phone, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_phone:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Телефон', @ru_id, @service_phone);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1012, 0, 1012);
SELECT @service_t_cesspool_cleaning:=1012;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Ассенизация', '', @ru_id, @service_t_cesspool_cleaning);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_cesspool_cleaning, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_cesspool_cleaning:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Ассенизация', @ru_id, @service_cesspool_cleaning);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1013, 0, 1013);
SELECT @service_t_lift:=1013;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Лифт', '', @ru_id, @service_t_lift);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_lift, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_lift:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Лифт', @ru_id, @service_lift);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1014, 0, 1014);
SELECT @service_t_ground_tax:=1014;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Налог на землю', '', @ru_id, @service_t_ground_tax);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_ground_tax, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_ground_tax:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Налог на землю', @ru_id, @service_ground_tax);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1015, 0, 1015);
SELECT @service_t_repeat_turn_on:=1015;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Повторное подключение', '', @ru_id, @service_t_repeat_turn_on);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_repeat_turn_on, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_repeat_turn_on:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Повторное подключение', @ru_id, @service_repeat_turn_on);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1016, 0, 1016);
SELECT @service_t_acts_payment:=1016;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Оплата по актам', '', @ru_id, @service_t_acts_payment);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_acts_payment, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_acts_payment:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Оплата по актам', @ru_id, @service_acts_payment);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1017, 0, 1017);
SELECT @service_t_counters_repair:=1017;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Ремонт счетчиков', '', @ru_id, @service_t_counters_repair);
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_counters_repair, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_counters_repair:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Ремонт счетчиков', @ru_id, @service_counters_repair);

SELECT @service_t_hot_water:=8;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_hot_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_hot_water:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Горячая вода', @ru_id, @service_hot_water);

SELECT @service_t_cold_water:=7;
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_cold_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_cold_water:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Холодная вода', @ru_id, @service_cold_water);

