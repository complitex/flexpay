-- put here module initialization data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('payments'); 

-- Init service types
INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 12);
SELECT @service_t_vodosnabzhenie:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоснабжение', '', @ru_id, @service_t_vodosnabzhenie);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 13);
SELECT @service_t_vodootvedenie:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоотведение', '', @ru_id, @service_t_vodootvedenie);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 2);
SELECT @service_t_dogs:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание собак', '', @ru_id, @service_t_dogs);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 3);
SELECT @service_t_garage:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_t_garage);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 4);
SELECT @service_t_heating:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Отопление', '', @ru_id, @service_t_heating);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 5);
SELECT @service_t_water_cooling:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Подогрев воды', '', @ru_id, @service_t_water_cooling);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 6);
SELECT @service_cold_water:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Холодная вода', '', @ru_id, @service_cold_water);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 7);
SELECT @service_hot_water:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Горячая вода', '', @ru_id, @service_hot_water);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 220);
SELECT @service_type_220:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_type_220);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 230);
SELECT @service_type_230:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Сарай', '', @ru_id, @service_type_230);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 240);
SELECT @service_type_240:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Погреба', '', @ru_id, @service_type_240);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 250);
SELECT @service_type_250:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание животных', '', @ru_id, @service_type_250);

-- kvartplata
INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 1);
SELECT @service_kvartplata:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Квартплата', '', @ru_id, @service_kvartplata);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 10);
SELECT @service_territory_cleaning:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка территории', '', @ru_id, @service_territory_cleaning);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 20);
SELECT @service_cleaning_garbagecollectors:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Очистка мусоросборников', '', @ru_id, @service_cleaning_garbagecollectors);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 30);
SELECT @service_cleaning_ext:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка подвалов, тех.этажей, крыш', '', @ru_id, @service_cleaning_ext);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 40);
SELECT @service_TBO:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Вывоз и утилизация ТБО', '', @ru_id, @service_TBO);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 50);
SELECT @service_to_elevators:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО лифтов', '', @ru_id, @service_to_elevators);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 60);
SELECT @service_to_dispetchering:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем диспетчеризации', '', @ru_id, @service_to_dispetchering);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 70);
SELECT @service_to_water_supply:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоснабжения', '', @ru_id, @service_to_water_supply);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 80);
SELECT @service_to_vodootvedenie:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоотведения', '', @ru_id, @service_to_vodootvedenie);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 90);
SELECT @service_to_systems_warmproviding:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем теплоснабжения', '', @ru_id, @service_to_systems_warmproviding);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 100);
SELECT @service_to_system_hot_water_providing:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем горячего водоснабжения', '', @ru_id, @service_to_system_hot_water_providing);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 110);
SELECT @service_to_boliers:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО бойлеров', '', @ru_id, @service_to_boliers);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 120);
SELECT @service_fog_canals:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Обслуживание дымоотв. каналов', '', @ru_id, @service_fog_canals);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 130);
SELECT @service_cleaning_toilets:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Очистка дворовых туалетов', '', @ru_id, @service_cleaning_toilets);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 140);
SELECT @service_lighting:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Освещение мест общего пльзования', '', @ru_id, @service_lighting);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 150);
SELECT @service_waterproviding_energy:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Энергоснабж. для подкачки воды', '', @ru_id, @service_waterproviding_energy);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 160);
SELECT @service_elevators_energy:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Энергоснабжение для лифтов', '', @ru_id, @service_elevators_energy);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 170);
SELECT @service_staircases_cleaning:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка лестничных клеток', '', @ru_id, @service_staircases_cleaning);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 180);
SELECT @service_180:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Дератизация и дезинфекция', '', @ru_id, @service_180);

INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 190);
SELECT @service_:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО бытовых электроплит', '', @ru_id, @service_);


-- init document types
insert into payments_document_types_tbl (id, version, code)
	values (1, 0, 1);
select @doc_type_1:=1;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Оплата наличными', @ru_id, @doc_type_1);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cash payment', @en_id, @doc_type_1);
insert into payments_document_types_tbl (id, version, code)
	values (2, 0, 2);
select @doc_type_2:=2;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Возврат наличных', @ru_id, @doc_type_2);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cash return', @en_id, @doc_type_2);
insert into payments_document_types_tbl (id, version, code)
	values (3, 0, 3);
select @doc_type_3:=3;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Безналичная оплата', @ru_id, @doc_type_3);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cashless payment', @en_id, @doc_type_3);
insert into payments_document_types_tbl (id, version, code)
	values (4, 0, 4);
select @doc_type_4:=4;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Безналичный возврат', @ru_id, @doc_type_4);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cashless payment return', @en_id, @doc_type_4);

-- init document statuses
insert into payments_document_statuses_tbl (id, version, code)
	values (1, 0, 1);
select @doc_status_1:=1;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Создан', @ru_id, @doc_status_1);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Created', @en_id, @doc_status_1);
insert into payments_document_statuses_tbl (id, version, code)
	values (2, 0, 2);
select @doc_status_2:=2;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Проведен', @ru_id, @doc_status_2);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Registered', @en_id, @doc_status_2);
insert into payments_document_statuses_tbl (id, version, code)
	values (3, 0, 3);
select @doc_status_3:=3;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Удален', @ru_id, @doc_status_3);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Deleted', @en_id, @doc_status_3);
insert into payments_document_statuses_tbl (id, version, code)
	values (4, 0, 4);
select @doc_status_4:=4;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Возвращен', @ru_id, @doc_status_4);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Returned', @en_id, @doc_status_4);
insert into payments_document_statuses_tbl (id, version, code)
	values (5, 0, 5);
select @doc_status_5:=5;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Ошибка проводки', @ru_id, @doc_status_5);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Error', @en_id, @doc_status_5);

-- init operation levels
insert into payments_operation_levels_tbl (id, version, code)
	values (1, 0, 1);
select @operation_level_1:=1;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Низший', @ru_id, @operation_level_1);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Lowest', @en_id, @operation_level_1);
insert into payments_operation_levels_tbl (id, version, code)
	values (2, 0, 2);
select @operation_level_2:=2;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Низкий', @ru_id, @operation_level_2);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Low', @en_id, @operation_level_2);
insert into payments_operation_levels_tbl (id, version, code)
	values (3, 0, 3);
select @operation_level_3:=3;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Средний', @ru_id, @operation_level_3);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Average', @en_id, @operation_level_3);
insert into payments_operation_levels_tbl (id, version, code)
	values (4, 0, 4);
select @operation_level_4:=4;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Высший', @ru_id, @operation_level_4);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('High', @en_id, @operation_level_4);
insert into payments_operation_levels_tbl (id, version, code)
	values (5, 0, 5);
select @operation_level_5:=5;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Отложенный', @ru_id, @operation_level_5);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Suspended', @en_id, @operation_level_5);

-- init operation statuses
insert into payments_operation_statuses_tbl (id, version, code)
	values (1, 0, 1);
select @operation_status_1:=1;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Создана', @ru_id, @operation_status_1);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Created', @en_id, @operation_status_1);
insert into payments_operation_statuses_tbl (id, version, code)
	values (2, 0, 2);
select @operation_status_2:=2;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Проведена', @ru_id, @operation_status_2);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Registered', @en_id, @operation_status_2);
insert into payments_operation_statuses_tbl (id, version, code)
	values (3, 0, 3);
select @operation_status_3:=3;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Удалена', @ru_id, @operation_status_3);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Deleted', @en_id, @operation_status_3);
insert into payments_operation_statuses_tbl (id, version, code)
	values (4, 0, 4);
select @operation_status_4:=4;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Возвращена', @ru_id, @operation_status_4);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Returned', @en_id, @operation_status_4);
insert into payments_operation_statuses_tbl (id, version, code)
	values (5, 0, 5);
select @operation_status_5:=5;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Ошибка проводки', @ru_id, @operation_status_5);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Error', @en_id, @operation_status_5);

-- init operation types
insert into payments_operation_types_tbl (id, version, code)
	values (1, 0, 1);
select @operation_type_1:=1;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Оплата наличными услуги', @ru_id, @operation_type_1);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cash payment', @en_id, @operation_type_1);
insert into payments_operation_types_tbl (id, version, code)
	values (2, 0, 2);
select @operation_type_2:=2;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Безналичная оплата услуги', @ru_id, @operation_type_2);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cashless payment', @en_id, @operation_type_2);
insert into payments_operation_types_tbl (id, version, code)
	values (3, 0, 3);
select @operation_type_3:=3;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат оплаты услуги наличными', @ru_id, @operation_type_3);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cash payment return', @en_id, @operation_type_3);
insert into payments_operation_types_tbl (id, version, code)
	values (4, 0, 4);
select @operation_type_4:=4;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат безналичной оплаты услуги', @ru_id, @operation_type_4);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cashless payment return', @en_id, @operation_type_4);
insert into payments_operation_types_tbl (id, version, code)
	values (5, 0, 5);
select @operation_type_5:=5;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Оплата наличными квитанции', @ru_id, @operation_type_5);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cash payment', @en_id, @operation_type_5);
insert into payments_operation_types_tbl (id, version, code)
	values (6, 0, 6);
select @operation_type_6:=6;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Безналичная оплата квитанции', @ru_id, @operation_type_6);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cashless payment', @en_id, @operation_type_6);
insert into payments_operation_types_tbl (id, version, code)
	values (7, 0, 7);
select @operation_type_7:=7;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат оплаты квитанции наличными', @ru_id, @operation_type_7);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cash payment return', @en_id, @operation_type_7);
insert into payments_operation_types_tbl (id, version, code)
	values (8, 0, 8);
select @operation_type_8:=8;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат безналичной оплаты квитанции', @ru_id, @operation_type_8);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cashless payment return', @en_id, @operation_type_8);
