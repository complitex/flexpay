select @ru_id:=id from common_languages_tbl where lang_iso_code='ru';

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1002, 0, 1002);
SELECT @service_t_electricity:=1002;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Электроэнергия', 'Описание', @ru_id, @service_t_electricity);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1, 0, 1);
SELECT @service_t_kvartplata:=1;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Квартплата', 'Описание', @ru_id, @service_t_kvartplata);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (2, 0, 2);
SELECT @service_t_dogs:=2;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание собак', 'Описание', @ru_id, @service_t_dogs);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (3, 0, 3);
SELECT @service_t_garage:=3;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', 'Описание', @ru_id, @service_t_garage);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (4, 0, 4);
SELECT @service_t_heating:=4;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Отопление', 'Описание', @ru_id, @service_t_heating);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (8, 0, 8);
SELECT @service_t_hot_water:=8;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Горячая вода', 'Описание', @ru_id, @service_t_hot_water);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (7, 0, 7);
SELECT @service_t_cold_water:=7;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Холодная вода', 'Описание', @ru_id, @service_t_cold_water);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1006, 0, 1006);
SELECT @service_t_sewerage:=1006;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Канализация', '', @ru_id, @service_t_sewerage);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1007, 0, 1007);
SELECT @service_t_coocking_gas:=1007;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Газ варочный', '', @ru_id, @service_t_coocking_gas);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1009, 0, 1009);
SELECT @service_t_radio:=1009;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Радио', '', @ru_id, @service_t_radio);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1010, 0, 1010);
SELECT @service_t_antenna:=1010;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Антенна', '', @ru_id, @service_t_antenna);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1011, 0, 1011);
SELECT @service_t_phone:=1011;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Телефон', '', @ru_id, @service_t_phone);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1012, 0, 1012);
SELECT @service_t_cesspool_cleaning:=1012;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Ассенизация', '', @ru_id, @service_t_cesspool_cleaning);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1013, 0, 1013);
SELECT @service_t_lift:=1013;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Лифт', '', @ru_id, @service_t_lift);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1014, 0, 1014);
SELECT @service_t_ground_tax:=1014;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Налог на землю', '', @ru_id, @service_t_ground_tax);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1015, 0, 1015);
SELECT @service_t_repeat_turn_on:=1015;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Повторное подключение', '', @ru_id, @service_t_repeat_turn_on);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1016, 0, 1016);
SELECT @service_t_acts_payment:=1016;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Оплата по актам', '', @ru_id, @service_t_acts_payment);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1017, 0, 1017);
SELECT @service_t_counters_repair:=1017;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Ремонт счетчиков', '', @ru_id, @service_t_counters_repair);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (38, 0, 38);
SELECT @service_type_240:=38;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Погреба', 'Описание', @ru_id, @service_type_240);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1003, 0, 230);
SELECT @service_t_shed:=1003;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Сарай', 'Описание', @ru_id, @service_t_shed);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1004, 0, 1004);
SELECT @service_t_larder:=1004;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Кладовая', 'Описание', @ru_id, @service_t_larder);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1008, 0, 1008);
SELECT @service_t_heating_gas:=1008;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Газ отопительный', '', @ru_id, @service_t_heating_gas);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1005, 0, 1005);
SELECT @service_t_household_consumptions:=1005;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Хозрасходы', 'Описание', @ru_id, @service_t_household_consumptions);


# init mega bank services mapping
insert into config_payments_mbservices_tbl (mb_service_code, service_type_id, mb_service_name, version) values
		('1', @service_t_electricity, 'Электроэнергия', 0), --
		('2', @service_t_kvartplata, 'Квартплата (эксплуатационные расходы)', 0), --
		('3', @service_t_heating, 'Отопление', 0), --
		('4', @service_t_hot_water, 'Горячая вода', 0), --
		('5', @service_t_cold_water, 'Холодная вода', 0), --
		('6', @service_t_sewerage, 'Канализация', 0), --
		('7', @service_t_coocking_gas, 'Газ варочный', 0), --
		('8', @service_t_heating_gas, 'Газ отопительный', 0), --
		('9', @service_t_radio, 'Радио', 0), --
		('10', @service_t_antenna, 'Антенна', 0), --
		('11', @service_t_dogs, 'Содержание животных', 0), --
		('12', @service_t_garage, 'Гараж', 0), --
		('13', @service_type_240, 'Погреб', 0), --
		('14', @service_t_shed, 'Сарай', 0), --
		('15', @service_t_larder, 'Кладовка', 0), --
		('16', @service_t_phone, 'Телефон', 0), --
		('19', @service_t_cesspool_cleaning, 'Ассенизация', 0), --
		('20', @service_t_lift, 'Лифт', 0), --
		('21', @service_t_household_consumptions, 'Хозрасходы', 0),
		('22', @service_t_ground_tax, 'Налог на землю', 0), --
		('23', @service_t_repeat_turn_on, 'Повторное подключение', 0), --
		('24', @service_t_acts_payment, 'Оплата по актам', 0), --
		('25', @service_t_counters_repair, 'Ремонт счетчиков', 0); --
