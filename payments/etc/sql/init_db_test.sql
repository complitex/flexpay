-- put here module test initialization data

-- Init service types
-- kvartplata
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

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (5, 0, 5);
SELECT @service_t_water_cooling:=5;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Подогрев воды', 'Описание', @ru_id, @service_t_water_cooling);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (7, 0, 7);
SELECT @service_t_cold_water:=7;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Холодная вода', 'Описание', @ru_id, @service_t_cold_water);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (8, 0, 8);
SELECT @service_t_hot_water:=8;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Горячая вода', 'Описание', @ru_id, @service_t_hot_water);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (10, 0, 10);
SELECT @service_territory_cleaning:=10;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка территории', 'Описание', @ru_id, @service_territory_cleaning);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (12, 0, 12);
SELECT @service_t_vodosnabzhenie:=12;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоснабжение', 'Описание', @ru_id, @service_t_vodosnabzhenie);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (13, 0, 13);
SELECT @service_t_vodootvedenie:=13;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоотведение', 'Описание', @ru_id, @service_t_vodootvedenie);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (14, 0, 20);
SELECT @service_cleaning_garbagecollectors:=14;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Очистка мусоросборников', 'Описание', @ru_id, @service_cleaning_garbagecollectors);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (15, 0, 30);
SELECT @service_cleaning_ext:=15;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка подвалов, тех.этажей, крыш', 'Описание', @ru_id, @service_cleaning_ext);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (16, 0, 40);
SELECT @service_TBO:=16;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Вывоз и утилизация ТБО', 'Описание', @ru_id, @service_TBO);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 50);
SELECT @service_to_elevators:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО лифтов', 'Описание', @ru_id, @service_to_elevators);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 60);
SELECT @service_to_dispetchering:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем диспетчеризации', 'Описание', @ru_id, @service_to_dispetchering);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 70);
SELECT @service_to_water_supply:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоснабжения', 'Описание', @ru_id, @service_to_water_supply);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 80);
SELECT @service_to_vodootvedenie:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоотведения', 'Описание', @ru_id, @service_to_vodootvedenie);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 90);
SELECT @service_to_systems_warmproviding:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем теплоснабжения', 'Описание', @ru_id, @service_to_systems_warmproviding);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 100);
SELECT @service_to_system_hot_water_providing:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем горячего водоснабжения', 'Описание', @ru_id, @service_to_system_hot_water_providing);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 110);
SELECT @service_to_boliers:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО бойлеров', 'Описание', @ru_id, @service_to_boliers);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 120);
SELECT @service_fog_canals:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Обслуживание дымоотв. каналов', 'Описание', @ru_id, @service_fog_canals);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 130);
SELECT @service_cleaning_toilets:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Очистка дворовых туалетов', 'Описание', @ru_id, @service_cleaning_toilets);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 140);
SELECT @service_lighting:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Освещение мест общего пльзования', 'Описание', @ru_id, @service_lighting);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 150);
SELECT @service_waterproviding_energy:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Энергоснабж. для подкачки воды', 'Описание', @ru_id, @service_waterproviding_energy);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 160);
SELECT @service_elevators_energy:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Энергоснабжение для лифтов', 'Описание', @ru_id, @service_elevators_energy);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 170);
SELECT @service_staircases_cleaning:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка лестничных клеток', 'Описание', @ru_id, @service_staircases_cleaning);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 180);
SELECT @service_180:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Дератизация и дезинфекция', 'Описание', @ru_id, @service_180);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 190);
SELECT @service_:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО бытовых электроплит', 'Описание', @ru_id, @service_);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 220);
SELECT @service_type_220:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', 'Описание', @ru_id, @service_type_220);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (38, 0, 38);
SELECT @service_type_240:=38;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Погреба', 'Описание', @ru_id, @service_type_240);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 250);
SELECT @service_type_250:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание животных', 'Описание', @ru_id, @service_type_250);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1002, 0, 1002);
SELECT @service_t_electricity:=1002;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Электроэнергия', 'Описание', @ru_id, @service_t_electricity);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1003, 0, 230);
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

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1006, 0, 1006);
SELECT @service_t_sewerage:=1006;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Канализация', 'Канализация', @ru_id, @service_t_sewerage);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1007, 0, 1007);
SELECT @service_t_coocking_gas:=1007;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Газ варочный', 'Газ варочный', @ru_id, @service_t_coocking_gas);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1008, 0, 1008);
SELECT @service_t_heating_gas:=1008;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Газ отопительный', 'Газ отопительный', @ru_id, @service_t_heating_gas);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1009, 0, 1009);
SELECT @service_t_radio:=1009;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Радио', 'Радио', @ru_id, @service_t_radio);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1010, 0, 1010);
SELECT @service_t_antenna:=1010;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Антенна', 'Антенна', @ru_id, @service_t_antenna);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1011, 0, 1011);
SELECT @service_t_phone:=1011;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Телефон', 'Телефон', @ru_id, @service_t_phone);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1012, 0, 1012);
SELECT @service_t_cesspool_cleaning:=1012;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Ассенизация', 'Ассенизация', @ru_id, @service_t_cesspool_cleaning);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1013, 0, 1013);
SELECT @service_t_lift:=1013;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Лифт', 'Лифт', @ru_id, @service_t_lift);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1014, 0, 1014);
SELECT @service_t_ground_tax:=1014;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Налог на землю', 'Налог на землю', @ru_id, @service_t_ground_tax);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1015, 0, 1015);
SELECT @service_t_repeat_turn_on:=1015;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Повторное подключение', 'Повторное подключение', @ru_id, @service_t_repeat_turn_on);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1016, 0, 1016);
SELECT @service_t_acts_payment:=1016;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Оплата по актам', 'Оплата по актам', @ru_id, @service_t_acts_payment);

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (1017, 0, 1017);
SELECT @service_t_counters_repair:=1017;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Ремонт счётчиков', 'Ремонт счётчиков', @ru_id, @service_t_counters_repair);

-- Init services
INSERT INTO payments_services_tbl (id, provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (1, @service_provider_cn, '1', @unit_square_meter, @service_t_kvartplata, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_kvartplata_id:=1;
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Квартплата', @ru_id, @service_kvartplata_id);

INSERT INTO payments_services_tbl (id, provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (2, @service_provider_cn, '10', @unit_grn_m2, @service_territory_cleaning, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_10:=2;
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка территории', @ru_id, @service_10);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '20', @unit_grn_m2, @service_cleaning_garbagecollectors, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_20:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка мусоросборников', @ru_id, @service_20);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '30', @unit_grn_m2, @service_cleaning_ext, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_30:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка подвалов, тех. этажей, крыш', @ru_id, @service_30);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '40', @unit_grn_m2, @service_TBO, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_40:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Вывоз и утилизация ТБО', @ru_id, @service_40);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '50', @unit_grn_m2, @service_to_elevators, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_50:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО лифтов', @ru_id, @service_50);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '60', @unit_grn_m2, @service_to_dispetchering, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_60:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем диспетчеризации', @ru_id, @service_60);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '70', @unit_grn_m2, @service_to_water_supply, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_70:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоснабжения', @ru_id, @service_70);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '80', @unit_grn_m2, @service_to_vodootvedenie, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_80:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоотведения', @ru_id, @service_80);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '90', @unit_grn_m2, @service_to_systems_warmproviding, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_90:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем теплоснабжения', @ru_id, @service_90);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '100', @unit_grn_m2, @service_to_system_hot_water_providing, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_100:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем горячего водоснабжения', @ru_id, @service_100);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '110', @unit_grn_m2, @service_to_boliers, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_110:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО бойлеров', @ru_id, @service_110);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '120', @unit_grn_m2, @service_fog_canals, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_120:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Обслуживание дымовент. каналов', @ru_id, @service_120);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '130', @unit_grn_m2, @service_cleaning_toilets, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_130:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка дворовых туалетов', @ru_id, @service_130);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '140', @unit_grn_m2, @service_lighting, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_140:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Освещение мест общего пользования', @ru_id, @service_140);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '150', @unit_grn_m2, @service_waterproviding_energy, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_150:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабж. для подкачки воды', @ru_id, @service_150);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@service_provider_cn, '160', @unit_grn_m2, @service_elevators_energy, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_160:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабжение для лифтов', @ru_id, @service_160);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '12', @unit_cubometr, @service_t_vodosnabzhenie, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_12:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Водоснабжение', @ru_id, @service_12);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '13', @unit_cubometr, @service_t_vodootvedenie, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_13:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Водоотведение', @ru_id, @service_13);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '2', null, @service_t_dogs, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_2:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Содержание собак', @ru_id, @service_2);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '3', @unit_square_meter, @service_t_garage, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_3:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Гараж', @ru_id, @service_3);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '4', @unit_gcalories, @service_t_heating, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_4:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Отопление', @ru_id, @service_4);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '5', null, @service_t_water_cooling, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_5:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Подогрев воды', @ru_id, @service_5);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '7', null, @service_t_cold_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_7:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Холодная вода', @ru_id, @service_7);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '8', null, @service_t_hot_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_8:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Горячая вода', @ru_id, @service_8);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '220', null, @service_type_220, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_220:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Гараж', @ru_id, @service_220);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '230', null, @service_t_shed, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_230:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Сарай', @ru_id, @service_230);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '13', null, @service_type_240, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_240:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Погреба', @ru_id, @service_240);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, '250', null, @service_type_250, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_250:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Содержание животных', @ru_id, @service_250);


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

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_sewerage, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_sewerage:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Канализация', @ru_id, @service_sewerage);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_coocking_gas, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_coocking_gas:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Газ варочный', @ru_id, @service_coocking_gas);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_heating_gas, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_heating_gas:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Газ отопительный', @ru_id, @service_heating_gas);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_radio, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_radio:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Радио', @ru_id, @service_radio);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_antenna, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_antenna:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Антенна', @ru_id, @service_antenna);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_phone, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_phone:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Телефон', @ru_id, @service_phone);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_cesspool_cleaning, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_cesspool_cleaning:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Ассенизация', @ru_id, @service_cesspool_cleaning);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_lift, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_lift:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Лифт', @ru_id, @service_lift);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_ground_tax, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_ground_tax:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Налог на землю', @ru_id, @service_ground_tax);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_repeat_turn_on, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_repeat_turn_on:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Повторное подключение', @ru_id, @service_repeat_turn_on);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@service_provider_cn, null, null, @service_t_acts_payment, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_acts_payment:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Оплата по актам', @ru_id, @service_acts_payment);

INSERT INTO payments_services_tbl (id, provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (1000, @service_provider_cn, null, null, @service_t_counters_repair, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_counters_repair:=1000;
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Ремонт счётчиков', @ru_id, @service_counters_repair);


-- init operations
INSERT INTO payments_operations_tbl (id, version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, payment_point_id, register_user, register_date, register_organization_id,
		operation_sum, operation_input_sum, change_sum, registry_record_id, reference_operation_id, cashier_fio, cashbox_id)
		VALUES (1, 0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_1, @operation_type_1,
				'asemenov', '2009-04-14 12:20', @organization_tszh, @payment_point_1, 'asemenov', '2009-04-14 12:21', @organization_tszh,
				1395.00, 1500.00, 105.00, null, null, 'Семёнов А.Н.', @cashbox_1_1);
SELECT @operation_1:=1;

INSERT INTO payments_operations_tbl (id, version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, payment_point_id, register_user, register_date, register_organization_id,
		operation_sum, operation_input_sum, change_sum, registry_record_id, reference_operation_id, cashier_fio, cashbox_id)
		VALUES (2, 0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_2, @operation_type_1,
				'asemenov', '2009-04-14 13:20', @organization_tszh, @payment_point_2, 'asemenov', '2009-04-14 13:21', @organization_tszh,
				113.00, 200.00, 87.00, null, null, 'Семёнов А.Н.', @cashbox_1_1);
SELECT @operation_2:=2;

INSERT INTO payments_operations_tbl (id, version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, payment_point_id, register_user, register_date, register_organization_id,
		operation_sum, operation_input_sum, change_sum, registry_record_id, reference_operation_id, cashier_fio, cashbox_id)
		VALUES (3, 0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_4, @operation_type_1,
				'asemenov', '2009-04-14 14:20', @organization_tszh, @payment_point_1, 'asemenov', '2009-04-14 14:21', @organization_tszh,
				220.00, 500.00, 280.00, null, null, 'Семёнов А.Н.', @cashbox_1_1);
SELECT @operation_3:=3;

-- Registered operation
INSERT INTO payments_operations_tbl (id, version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, payment_point_id, register_user, register_date, register_organization_id,
		operation_sum, operation_input_sum, change_sum, registry_record_id, reference_operation_id, cashier_fio, cashbox_id)
		VALUES (4, 0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_2, @operation_type_1,
				'asemenov', '2009-04-14 14:20', @organization_tszh, @payment_point_1, 'asemenov', '2009-04-14 14:41', @organization_tszh,
				220.00, 500.00, 280.00, null, null, 'Семёнов А.Н.', @cashbox_1_1);
SELECT @operation_4:=4;

-- init documents
INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, sum, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_1, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_1,
				@service_kvartplata_id, 1235.00, '123123123', '09012345067', @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_1:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, sum, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_1, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_1,
				@service_10, 90.00, '123123123', '09012345067', @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_2:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, sum, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_1, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_1,
				@service_20, 70.00, '123123123', '09012345067', @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_3:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, sum, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_2, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_2,
				@service_phone, 113.00, '123123123', '09012345067', @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_4:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, sum, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_3, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_4,
				@service_50, 220.00, '123123123', '09012345067', @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_5:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, sum, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_4, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_4,
				@service_50, 220.00, '123123123', '09012345067', @organization_zhko, @organization_tszh,
				null, null);

-- Init users payment points
update common_users_tbl set payments_payment_point_id=1
where id=@user_test;
update common_users_tbl set payments_payment_point_id=2
where id=@user_ivanova;

# init mega bank services mapping
insert into config_payments_mbservices_tbl (mb_service_code, service_type_id, mb_service_name, version) values
		('1', @service_t_electricity, 'Электроэнергия', 0),
		('2', @service_t_kvartplata, 'Квартплата (эксплуатационные расходы)', 0),
		('3', @service_t_heating, 'Отопление', 0),
		('4', @service_t_hot_water, 'Горячая вода', 0),
		('5', @service_t_cold_water, 'Холодная вода', 0),
		('6', @service_t_sewerage, 'Канализация', 0),
		('7', @service_t_coocking_gas, 'Газ варочный', 0),
		('8', @service_t_heating_gas, 'Газ отопительный', 0),
		('9', @service_t_radio, 'Радио', 0),
		('10', @service_t_antenna, 'Антенна', 0),
		('11', @service_t_dogs, 'Содержание животных', 0),
		('12', @service_t_garage, 'Гараж', 0),
		('13', @service_type_240, 'Погреб', 0),
		('14', @service_t_shed, 'Сарай', 0),
		('15', @service_t_larder, 'Кладовка', 0),
		('16', @service_t_phone, 'Телефон', 0),
		('19', @service_t_cesspool_cleaning, 'Ассенизация', 0),
		('20', @service_t_lift, 'Лифт', 0),
		('21', @service_t_household_consumptions, 'Хозрасходы', 0),
		('22', @service_t_ground_tax, 'Налог на землю', 0),
		('23', @service_t_repeat_turn_on, 'Повторное подключение', 0),
		('24', @service_t_acts_payment, 'Оплата по актам', 0),
		('25', @service_t_counters_repair, 'Ремонт счётчиков', 0);

-- master index data
select @ds:=id from common_data_source_descriptions_tbl where description='Master-index';
select @instId:='090-';
select @payments_base:=0x3000 + 0;

insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @payments_base + 0x0201, @ds from payments_services_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @payments_base + 0x002, @ds from payments_service_types_tbl);
