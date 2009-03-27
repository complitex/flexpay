INSERT INTO common_flexpay_modules_tbl (name) VALUES ('eirc');
SELECT @module_eirc:=last_insert_id();

insert into common_data_source_descriptions_tbl (id, description)
	values (2, 'Харьковский центр начислений');
SELECT @source_description_id:=2;

-- Init Sequences table
INSERT INTO common_sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');

-- Init service providers registry types
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (1, 0, 1);
select @registry_type:=1;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (2, 0, 2);
select @registry_type:=2;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (3, 0, 3);
select @registry_type:=3;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (4, 0, 4);
select @registry_type:=4;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (5, 0, 5);
select @registry_type:=5;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (6, 0, 6);
select @registry_type:=6;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (7, 0, 7);
select @registry_type:=7;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (8, 0, 8);
select @registry_type:=8;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (9, 0, 9);
select @registry_type:=9;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (10, 0, 10);
select @registry_type:=10;
INSERT INTO eirc_registry_types_tbl (id, version, code) VALUES (11, 0, 11);
select @registry_type_info:=11;

-- Init RegistryStatuses
INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 0);
SELECT @registry_status_loading:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 1);
SELECT @registry_status_loaded:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 2);
SELECT @registry_status_loading_canceled:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 3);
SELECT @registry_status_loaded_with_error:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 4);
SELECT @registry_status_processing:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 5);
SELECT @registry_status_processing_with_error:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 6);
SELECT @registry_status_processed:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 7);
SELECT @registry_status_processed_with_error:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 8);
SELECT @registry_status_processing_canceled:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 9);
SELECT @registry_status_rollbacking:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 10);
SELECT @registry_status_rollbacked:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 11);
SELECT @registry_status_creating:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 12);
SELECT @registry_status_created:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (version, code) VALUES (0, 13);
SELECT @registry_status_creating_canceled:=last_insert_id();

-- Init RegistryArchiveStatuses
INSERT INTO eirc_registry_archive_statuses_tbl (code) VALUES (0);
SELECT @sp_registry_archive_status_none:=last_insert_id();

INSERT INTO eirc_registry_archive_statuses_tbl (code) VALUES (1);
SELECT @sp_registry_archive_status_archiving:=last_insert_id();

INSERT INTO eirc_registry_archive_statuses_tbl (code) VALUES (2);
SELECT @sp_registry_archive_status_archived:=last_insert_id();

INSERT INTO eirc_registry_archive_statuses_tbl (code) VALUES (3);
SELECT @sp_registry_archive_status_canceled:=last_insert_id();

-- Init RegistryRecordStatus
INSERT INTO eirc_registry_record_statuses_tbl (id, code) VALUES (1, 1);
select @record_status_loaded:=1;
INSERT INTO eirc_registry_record_statuses_tbl (id, code) VALUES (2, 2);
select @record_status_error:=2;
INSERT INTO eirc_registry_record_statuses_tbl (id, code) VALUES (3, 3);
select @record_status_error_fixed:=3;
INSERT INTO eirc_registry_record_statuses_tbl (id, code) VALUES (4, 4);
select @record_status_processed:=4;

-- Init organizations
-- EIRC is the first one, ID=1
-- CN is the fourth one, ID=4
INSERT INTO eirc_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (1, 0, 0, '', '', '-------', '123');
SELECT @organization_eirc:=1;
INSERT INTO eirc_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Eirc itself', @ru_id, @organization_eirc);
INSERT INTO eirc_organization_names_tbl (name, language_id, organization_id)
	VALUES ('EIRC', @ru_id, @organization_eirc);

-- Init service types
INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 12);
SELECT @service_t_vodosnabzhenie:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоснабжение', '', @ru_id, @service_t_vodosnabzhenie);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 13);
SELECT @service_t_vodootvedenie:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоотведение', '', @ru_id, @service_t_vodootvedenie);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 2);
SELECT @service_t_dogs:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание собак', '', @ru_id, @service_t_dogs);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 3);
SELECT @service_t_garage:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_t_garage);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 4);
SELECT @service_t_heating:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Отопление', '', @ru_id, @service_t_heating);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 5);
SELECT @service_t_water_cooling:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Подогрев воды', '', @ru_id, @service_t_water_cooling);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 6);
SELECT @service_cold_water:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Холодная вода', '', @ru_id, @service_cold_water);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 7);
SELECT @service_hot_water:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Горячая вода', '', @ru_id, @service_hot_water);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 220);
SELECT @service_type_220:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_type_220);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 230);
SELECT @service_type_230:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Сарай', '', @ru_id, @service_type_230);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 240);
SELECT @service_type_240:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Погреба', '', @ru_id, @service_type_240);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 250);
SELECT @service_type_250:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание животных', '', @ru_id, @service_type_250);

-- kvarplata
INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 1);
SELECT @service_kvarplata:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Кварплата', '', @ru_id, @service_kvarplata);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 10);
SELECT @service_territory_cleaning:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка территории', '', @ru_id, @service_territory_cleaning);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 20);
SELECT @service_cleaning_garbagecollectors:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Очистка мусоросборников', '', @ru_id, @service_cleaning_garbagecollectors);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 30);
SELECT @service_cleaning_ext:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка подвалов, тех.этажей, крыш', '', @ru_id, @service_cleaning_ext);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 40);
SELECT @service_TBO:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Вывоз и утилизация ТБО', '', @ru_id, @service_TBO);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 50);
SELECT @service_to_elevators:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО лифтов', '', @ru_id, @service_to_elevators);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 60);
SELECT @service_to_dispetchering:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем диспетчеризации', '', @ru_id, @service_to_dispetchering);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 70);
SELECT @service_to_water_supply:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоснабжения', '', @ru_id, @service_to_water_supply);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 80);
SELECT @service_to_vodootvedenie:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоотведения', '', @ru_id, @service_to_vodootvedenie);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 90);
SELECT @service_to_systems_warmproviding:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем теплоснабжения', '', @ru_id, @service_to_systems_warmproviding);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 100);
SELECT @service_to_system_hot_water_providing:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем горячего водоснабжения', '', @ru_id, @service_to_system_hot_water_providing);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 110);
SELECT @service_to_boliers:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО бойлеров', '', @ru_id, @service_to_boliers);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 120);
SELECT @service_fog_canals:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Обслуживание дымоотв. каналов', '', @ru_id, @service_fog_canals);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 130);
SELECT @service_cleaning_toilets:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Очистка дворовых туалетов', '', @ru_id, @service_cleaning_toilets);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 140);
SELECT @service_lighting:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Освещение мест общего пльзования', '', @ru_id, @service_lighting);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 150);
SELECT @service_waterproviding_energy:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Энергоснабж. для подкачки воды', '', @ru_id, @service_waterproviding_energy);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 160);
SELECT @service_elevators_energy:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Энергоснабжение для лифтов', '', @ru_id, @service_elevators_energy);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 170);
SELECT @service_staircases_cleaning:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка лестничных клеток', '', @ru_id, @service_staircases_cleaning);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 180);
SELECT @service_180:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Дератизация и дезинфекция', '', @ru_id, @service_180);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 190);
SELECT @service_:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО бытовых электроплит', '', @ru_id, @service_);


insert into accounting_document_types_tbl (id, code, name)
	values (1, 'CASH_PAYMENT', 'accounting.document.type.cash_payment');
insert into accounting_document_types_tbl (id, code, name)
	values (2, 'CASH_RETURN', 'accounting.document.type.cash_payment_return');
insert into accounting_document_types_tbl (id, code, name)
	values (3, 'CASHLESS_PAYMENT', 'accounting.document.type.cashless_payment');
insert into accounting_document_types_tbl (id, code, name)
	values (4, 'CASHLESS_PAYMENT_RETURN', 'accounting.document.type.cashless_payment_return');
