delete FROM eirc_service_type_name_translations_tbl;
delete FROM eirc_service_descriptions_tbl;
delete FROM eirc_services_tbl where parent_service_id is not null;
delete FROM eirc_services_tbl;
delete FROM eirc_service_types_tbl;

delete FROM eirc_organisation_descriptions_tbl;
delete FROM eirc_organisation_names_tbl ;
delete FROM eirc_service_provider_descriptions_tbl ;

delete FROM eirc_registry_record_containers_tbl;
delete FROM eirc_registry_records_tbl;
delete FROM eirc_registries_tbl;
delete FROM eirc_registry_files_tbl;

delete FROM eirc_service_providers_tbl;
delete FROM eirc_organisations_tbl;

select @ru_id:=id from languages_tbl where lang_iso_code='ru';

-- Init organisations
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, unique_id)
	VALUES (0, '-------', '123', '0');
SELECT @organisation_eirc:=last_insert_id();
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Eirc itself', @ru_id, @organisation_eirc);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('EIRC', @ru_id, @organisation_eirc);
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, unique_id)
	VALUES (0, '123123123', '123', '2');
SELECT @organisation_zhko:=last_insert_id();
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Test organisation', @ru_id, @organisation_zhko);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('ЖКО', @ru_id, @organisation_zhko);
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, unique_id)
	VALUES (0, '456456456', '56', '3');
SELECT @organisation_tszh:=last_insert_id();
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Test organisation 2', @ru_id, @organisation_tszh);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('ТСЖ', @ru_id, @organisation_tszh);
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, unique_id)
	VALUES (0, '1111111', '56', '10');
SELECT @organisation_cn:=last_insert_id();
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Calculation center', @ru_id, @organisation_cn);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('ЦН', @ru_id, @organisation_cn);

-- Init service providers
INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('Источник - Тестовые данные ПУ из ЦН');
SELECT @source_description_id:=last_insert_id();
INSERT INTO eirc_service_providers_tbl(organisation_id, data_source_description_id)
	VALUES (@organisation_cn, @source_description_id);
SELECT @service_provider_cn:=last_insert_id();
INSERT INTO eirc_service_provider_descriptions_tbl (name, language_id, service_provider_id)
	VALUES ('ПУ ЦН', @ru_id, @service_provider_cn);

-- Init service types
INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 11);
SELECT @service_vodootvedenie:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоотведение', '', @ru_id, @service_vodootvedenie);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 2);
SELECT @service_dogs:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание собак', '', @ru_id, @service_dogs);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 3);
SELECT @service_garage:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_garage);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 4);
SELECT @service_heating:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Отопление', '', @ru_id, @service_heating);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 5);
SELECT @service_water_cooling:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Подогрев воды', '', @ru_id, @service_water_cooling);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 6);
SELECT @service_cold_water:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Холодная вода', '', @ru_id, @service_cold_water);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 7);
SELECT @service_hot_water:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Горячая вода', '', @ru_id, @service_hot_water);

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

-- Init services
INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '1', @service_kvarplata, '1900-01-01', '2100-12-31');
SELECT @service_kvarplata_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Кварплата', @ru_id, @service_kvarplata_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '10', @service_territory_cleaning, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка территории', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '20', @service_cleaning_garbagecollectors, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка мусоросборников', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '30', @service_cleaning_ext, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка подвалов, тех. этажей, крыш', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '40', @service_TBO, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Вывоз и утилизация ТБО', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '50', @service_to_elevators, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО лифтов', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '60', @service_to_dispetchering, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем диспетчеризации', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '70', @service_to_water_supply, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоснабжения', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '80', @service_to_vodootvedenie, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоотведения', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '90', @service_to_systems_warmproviding, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем теплоснабжения', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '100', @service_to_system_hot_water_providing, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем горячего водоснабжения', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '110', @service_to_boliers, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО бойлеров', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '120', @service_fog_canals, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Обслуживание дымовент. каналов', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '130', @service_cleaning_toilets, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка дворовых туалетов', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '140', @service_lighting, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Освещение мест общего пользования', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '150', @service_waterproviding_energy, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабж. для подкачки воды', @ru_id, @service_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '160', @service_elevators_energy, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабжение для лифтов', @ru_id, @service_id);
