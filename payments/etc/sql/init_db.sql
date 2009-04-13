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

-- kvarplata
INSERT INTO payments_service_types_tbl (status, code) VALUES (0, 1);
SELECT @service_kvarplata:=last_insert_id();
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Кварплата', '', @ru_id, @service_kvarplata);

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

