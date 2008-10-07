insert into common_data_source_descriptions_tbl (id, description)
	values (2, 'Харьковский центр начислений');
SELECT @source_description_id:=2;

-- Init Sequences table
INSERT INTO common_sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');

-- Init service providers registry types
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (1, 1);
select @registry_type:=1;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (2, 2);
select @registry_type:=2;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (3, 3);
select @registry_type:=3;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (4, 4);
select @registry_type:=4;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (5, 5);
select @registry_type:=5;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (6, 6);
select @registry_type:=6;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (7, 7);
select @registry_type:=7;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (8, 8);
select @registry_type:=8;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (9, 9);
select @registry_type:=9;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (10, 10);
select @registry_type:=10;
INSERT INTO eirc_registry_types_tbl (id, code) VALUES (11, 11);
select @registry_type_info:=11;

-- Init RegistryStatuses
INSERT INTO eirc_registry_statuses_tbl (code) VALUES (0);
SELECT @registry_status_loading:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (1);
SELECT @registry_status_loaded:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (2);
SELECT @registry_status_loading_canceled:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (3);
SELECT @registry_status_loaded_with_error:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (4);
SELECT @registry_status_processing:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (5);
SELECT @registry_status_processing_with_error:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (6);
SELECT @registry_status_processed:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (7);
SELECT @registry_status_processed_with_error:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (8);
SELECT @registry_status_processing_canceled:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (9);
SELECT @registry_status_rollbacking:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (10);
SELECT @registry_status_rollbacked:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (11);
SELECT @registry_status_creating:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (12);
SELECT @registry_status_created:=last_insert_id();

INSERT INTO eirc_registry_statuses_tbl (code) VALUES (13);
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

-- Init organisations
-- EIRC is the first one, ID=1
-- CN is the fourth one, ID=4
INSERT INTO eirc_organisations_tbl (id, status, individual_tax_number, kpp)
	VALUES (1, 0, '-------', '123');
SELECT @organisation_eirc:=1;
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Eirc itself', @ru_id, @organisation_eirc);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('EIRC', @ru_id, @organisation_eirc);
INSERT INTO eirc_organisations_tbl (id, status, individual_tax_number, kpp)
	VALUES (2, 0, '123123123', '123');
SELECT @organisation_zhko:=2;
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Test organisation', @ru_id, @organisation_zhko);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('ЖКО', @ru_id, @organisation_zhko);
INSERT INTO eirc_organisations_tbl (id, status, individual_tax_number, kpp)
	VALUES (3, 0, '456456456', '56');
SELECT @organisation_tszh:=3;
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Test organisation 2', @ru_id, @organisation_tszh);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('ТСЖ', @ru_id, @organisation_tszh);
INSERT INTO eirc_organisations_tbl (id, status, individual_tax_number, kpp)
	VALUES (4, 0, '1111111', '56');
SELECT @organisation_cn:=4;
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Calculation center', @ru_id, @organisation_cn);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('ЦН', @ru_id, @organisation_cn);

-- Init subdivisions
INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, '3-я серверная стойка', null, @organisation_eirc, @organisation_eirc);
SELECT @subdivision_eirc_it:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('АйТи', @ru_id, @subdivision_eirc_it);
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('IT', @en_id, @subdivision_eirc_it);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Отдел информационных технологий', @ru_id, @subdivision_eirc_it);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Informational technoligies department', @en_id, @subdivision_eirc_it);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, '1-я серверная стойка', @subdivision_eirc_it, @organisation_eirc, null);
SELECT @subdivision_eirc_it_java:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Java', @ru_id, @subdivision_eirc_it_java);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Жабный сектор', @ru_id, @subdivision_eirc_it_java);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, '2-я серверная стойка', @subdivision_eirc_it, @organisation_eirc, null);
SELECT @subdivision_eirc_it_web:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Web', @ru_id, @subdivision_eirc_it_web);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Вэббизнес', @ru_id, @subdivision_eirc_it_web);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, 'Кабинет направо', null, @organisation_eirc, @organisation_eirc);
SELECT @subdivision_eirc_buch:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Бухгалтерия', @ru_id, @subdivision_eirc_buch);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Бухгалтерский отдел', @ru_id, @subdivision_eirc_buch);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, 'Центр клининг-услуг', null, @organisation_eirc, @organisation_cn);
SELECT @subdivision_eirc_cleaning:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Уборщики', @ru_id, @subdivision_eirc_cleaning);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('сектор Очистки помещений', @ru_id, @subdivision_eirc_cleaning);


-- Init banks
INSERT INTO eirc_banks_tbl (status, organisation_id, bank_identifier_code, corresponding_account)
	VALUES (0, @organisation_cn, '044525957', '30101810600000000957');
SELECT @bank_cn:=last_insert_id();
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('Мега Банк', @ru_id, @bank_cn);
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('Mega Bank', @en_id, @bank_cn);
INSERT INTO eirc_banks_tbl (status, organisation_id, bank_identifier_code, corresponding_account)
	VALUES (0, @organisation_eirc, '1233455', '30101810600000000455');
SELECT @bank_eirc:=last_insert_id();
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('ЕИРЦ Банк', @ru_id, @bank_eirc);
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('EIRC Bank', @en_id, @bank_eirc);


-- Init service providers
INSERT INTO eirc_service_providers_tbl(id, organisation_id, data_source_description_id)
	VALUES (1, @organisation_cn, @source_description_id);
SELECT @service_provider_cn:=1;
INSERT INTO eirc_service_provider_descriptions_tbl (name, language_id, service_provider_id)
	VALUES ('ПУ ЦН', @ru_id, @service_provider_cn);

-- Init service organisations
INSERT INTO eirc_organisations_tbl (id, status, individual_tax_number, kpp)
	VALUES (5, 0, '', '56');
SELECT @organisation_service_org_1:=5;
INSERT INTO eirc_organisation_descriptions_tbl (name, language_id, organisation_id)
	VALUES ('Тестовая обсл. организация', @ru_id, @organisation_service_org_1);
INSERT INTO eirc_organisation_names_tbl (name, language_id, organisation_id)
	VALUES ('Участок-45', @ru_id, @organisation_service_org_1);

INSERT INTO eirc_service_organisations_tbl(id, status, organisation_id)
	VALUES (1, 0, @organisation_service_org_1);
SELECT @service_org_1:=1;
INSERT INTO eirc_service_organisation_descriptions_tbl (name, language_id, service_organisation_id)
	VALUES ('ЖКО', @ru_id, @service_org_1);

-- Setup service organisation
update ab_buildings_tbl set eirc_service_organisation_id=@service_org_1 where id=@building_ivanova_27_id;

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

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 240);
SELECT @service_type_240:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Погреба', '', @ru_id, @service_type_250);

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

-- Init services
INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '1', @unit_square_meter, @service_kvarplata, '1900-01-01', '2100-12-31');
SELECT @service_kvarplata_id:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Кварплата', @ru_id, @service_kvarplata_id);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '10', @unit_grn_m2, @service_territory_cleaning, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_10:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка территории', @ru_id, @service_10);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '20', @unit_grn_m2, @service_cleaning_garbagecollectors, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_20:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка мусоросборников', @ru_id, @service_20);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '30', @unit_grn_m2, @service_cleaning_ext, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_30:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка подвалов, тех. этажей, крыш', @ru_id, @service_30);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '40', @unit_grn_m2, @service_TBO, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_40:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Вывоз и утилизация ТБО', @ru_id, @service_40);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '50', @unit_grn_m2, @service_to_elevators, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_50:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО лифтов', @ru_id, @service_50);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '60', @unit_grn_m2, @service_to_dispetchering, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_60:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем диспетчеризации', @ru_id, @service_60);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '70', @unit_grn_m2, @service_to_water_supply, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_70:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоснабжения', @ru_id, @service_70);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '80', @unit_grn_m2, @service_to_vodootvedenie, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_80:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоотведения', @ru_id, @service_80);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '90', @unit_grn_m2, @service_to_systems_warmproviding, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_90:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем теплоснабжения', @ru_id, @service_90);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '100', @unit_grn_m2, @service_to_system_hot_water_providing, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_100:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем горячего водоснабжения', @ru_id, @service_100);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '110', @unit_grn_m2, @service_to_boliers, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_110:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО бойлеров', @ru_id, @service_110);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '120', @unit_grn_m2, @service_fog_canals, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_120:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Обслуживание дымовент. каналов', @ru_id, @service_120);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '130', @unit_grn_m2, @service_cleaning_toilets, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_130:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка дворовых туалетов', @ru_id, @service_130);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '140', @unit_grn_m2, @service_lighting, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_140:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Освещение мест общего пользования', @ru_id, @service_140);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '150', @unit_grn_m2, @service_waterproviding_energy, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_150:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабж. для подкачки воды', @ru_id, @service_150);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '160', @unit_grn_m2, @service_elevators_energy, '1900-01-01', '2100-12-31', @service_kvarplata_id);
SELECT @service_160:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабжение для лифтов', @ru_id, @service_160);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '12', @unit_cubometr, @service_t_vodosnabzhenie, '1900-01-01', '2100-12-31');
SELECT @service_12:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Водоснабжение', @ru_id, @service_12);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '13', @unit_cubometr, @service_t_vodootvedenie, '1900-01-01', '2100-12-31');
SELECT @service_13:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Водоотведение', @ru_id, @service_13);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '2', null, @service_t_dogs, '1900-01-01', '2100-12-31');
SELECT @service_2:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Содержание собак', @ru_id, @service_2);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '3', @unit_square_meter, @service_t_garage, '1900-01-01', '2100-12-31');
SELECT @service_3:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Гараж', @ru_id, @service_3);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '4', @unit_gcalories, @service_t_heating, '1900-01-01', '2100-12-31');
SELECT @service_4:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Отопление', @ru_id, @service_4);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '240', null, @service_type_240, '1900-01-01', '2100-12-31');
SELECT @service_240:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Погреба', @ru_id, @service_240);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '250', null, @service_type_250, '1900-01-01', '2100-12-31');
SELECT @service_250:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Погреба', @ru_id, @service_250);


-- Init EIRC accounts
INSERT INTO eirc_eirc_accounts_tbl (id, version, status, apartment_id, person_id, account_number)
	VALUES (1, 0, 0, @apartment_ivanova_329_id, @person_id, '09012345067');
SELECT @account_id_1:=1;
INSERT INTO eirc_eirc_accounts_tbl (id, version, status, apartment_id, person_id, account_number)
	VALUES (2, 0, 0, @apartment_ivanova_330_id, @person_id, '09076543021');
SELECT @account_id_2:=2;

-- Init registry
INSERT INTO eirc_registries_tbl (id, version, registry_type_id, registry_status_id, archive_status_id)
	values (1, 0, @registry_type_info, @registry_status_loaded, @sp_registry_archive_status_none);
select @eirc_registry:=1;

-- Init registry records
INSERT INTO eirc_registry_records_tbl (id, version, registry_id, operation_date) 
	values (1, 0, @eirc_registry, '2008-01-01');
select @eirc_registry_rec:=1;

-- Init Consumer infos
insert into eirc_consumer_infos_tbl (id, status, first_name, middle_name, last_name,
	city_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
	values (1, 0, 'М', 'А', 'Иванофф',
	'Н-ск', 'ул', 'ИВОНОВА', '27-', '', '330');
select @consumer_info:=1;

-- Init consumers
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (1, 0, '123123123', @service_kvarplata_id,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_1:=1;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (3, 0, '123123123', @service_2,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_2:=3;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (4, 0, '123123123', @service_3,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_3:=4;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (5, 0, '123123123', @service_4,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_4:=5;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (14, 0, '123123123', @service_12,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_12:=14;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (6, 0, '123123123', @service_13,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_13:=6;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (7, 0, '123123123', @service_10,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_10:=7;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (8, 0, '123123123', @service_20,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_20:=8;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (15, 0, '123123123', @service_30,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_30:=15;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (9, 0, '123123123', @service_40,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_40:=9;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (10, 0, '123123123', @service_50,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_50:=10;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (11, 0, '123123123', @service_60,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_60:=11;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (12, 0, '123123123', @service_70,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_70:=12;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (13, 0, '123123123', @service_80,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_80:=13;

insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (2, 0, '67676767', @service_kvarplata_id,
	@person_id, @apartment_ivanova_329_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_2:=2;

-- Init quittance details
-- Quittance details for consumer_1 (kvarplata)
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (1, @consumer_1_1, @eirc_registry_rec,
	'0.00', '40.34', '40.34', '50.34', '1.1', '-4.0', '-5.0', '-1.0', '0.0', '2007-12-01');
select @quittance_details_1:=1;
-- Quittance details for consumer_1_1 (kvarplata)
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (2, @consumer_1_1, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '123', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_2:=2;
-- Quittance details for consumer_1_10
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (5, @consumer_1_10, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '10.10', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_10:=5;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (6, @consumer_1_2, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '2.0', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_2:=6;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (7, @consumer_1_3, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '3.003', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_3:=7;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (8, @consumer_1_4, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '4.04', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_4:=8;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (9, @consumer_1_12, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '12.012', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_12:=9;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (10, @consumer_1_13, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '13.13', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_13:=10;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (11, @consumer_1_10, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '10.010', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_10:=11;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (12, @consumer_1_20, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '20.20', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_20:=12;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (13, @consumer_1_30, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '30.030', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_30:=13;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (14, @consumer_1_40, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '40.040', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_40:=14;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (15, @consumer_1_50, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '50.050', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_50:=15;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (16, @consumer_1_60, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '60.060', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_60:=16;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (17, @consumer_1_70, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '70.070', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_70:=17;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (18, @consumer_1_80, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '80.80', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_80:=18;

insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (3, @consumer_2, @eirc_registry_rec,
	'0.00', '40.34', '40.34', '50.34', '2.02', '-4.0', '-5.0', '-1.0', '0.0', '2007-12-01');
select @quittance_details_3:=3;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (4, @consumer_2, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '2.2', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_4:=4;


-- Init quittances

-- Unique quittance details
insert into eirc_quittances_tbl (id, service_organisation_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (1, @service_org_1, @account_id_1, 1, '2007-12-01', '2007-12-31', '2008-01-05');
select @quittance_1:=1;
insert into eirc_quittance_details_quittances_tbl (id, quittance_details_id, quittance_id)
	values (1, @quittance_details_1, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_10, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_20, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_30, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_40, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_50, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_60, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_70, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_80, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_2, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_3, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_4, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_12, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_13, @quittance_1);

-- Quittance with 2 details
insert into eirc_quittances_tbl (id, service_organisation_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (2, @service_org_1, @account_id_1, 2, '2007-12-01', '2008-01-31', '2008-02-05');
select @quittance_2:=2;
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1, @quittance_2);
