-- put here module test initialization data

-- Init services
INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '1', @unit_square_meter, @service_kvartplata, '1900-01-01', '2100-12-31');
SELECT @service_kvartplata_id:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Квартплата', @ru_id, @service_kvartplata_id);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '10', @unit_grn_m2, @service_territory_cleaning, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_10:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка территории', @ru_id, @service_10);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '20', @unit_grn_m2, @service_cleaning_garbagecollectors, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_20:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка мусоросборников', @ru_id, @service_20);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '30', @unit_grn_m2, @service_cleaning_ext, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_30:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка подвалов, тех. этажей, крыш', @ru_id, @service_30);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '40', @unit_grn_m2, @service_TBO, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_40:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Вывоз и утилизация ТБО', @ru_id, @service_40);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '50', @unit_grn_m2, @service_to_elevators, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_50:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО лифтов', @ru_id, @service_50);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '60', @unit_grn_m2, @service_to_dispetchering, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_60:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем диспетчеризации', @ru_id, @service_60);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '70', @unit_grn_m2, @service_to_water_supply, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_70:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоснабжения', @ru_id, @service_70);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '80', @unit_grn_m2, @service_to_vodootvedenie, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_80:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоотведения', @ru_id, @service_80);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '90', @unit_grn_m2, @service_to_systems_warmproviding, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_90:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем теплоснабжения', @ru_id, @service_90);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '100', @unit_grn_m2, @service_to_system_hot_water_providing, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_100:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем горячего водоснабжения', @ru_id, @service_100);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '110', @unit_grn_m2, @service_to_boliers, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_110:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО бойлеров', @ru_id, @service_110);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '120', @unit_grn_m2, @service_fog_canals, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_120:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Обслуживание дымовент. каналов', @ru_id, @service_120);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '130', @unit_grn_m2, @service_cleaning_toilets, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_130:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка дворовых туалетов', @ru_id, @service_130);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '140', @unit_grn_m2, @service_lighting, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_140:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Освещение мест общего пользования', @ru_id, @service_140);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '150', @unit_grn_m2, @service_waterproviding_energy, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_150:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабж. для подкачки воды', @ru_id, @service_150);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id)
	VALUES (@service_provider_cn, '160', @unit_grn_m2, @service_elevators_energy, '1900-01-01', '2100-12-31', @service_kvartplata_id);
SELECT @service_160:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабжение для лифтов', @ru_id, @service_160);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '12', @unit_cubometr, @service_t_vodosnabzhenie, '1900-01-01', '2100-12-31');
SELECT @service_12:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Водоснабжение', @ru_id, @service_12);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '13', @unit_cubometr, @service_t_vodootvedenie, '1900-01-01', '2100-12-31');
SELECT @service_13:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Водоотведение', @ru_id, @service_13);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '2', null, @service_t_dogs, '1900-01-01', '2100-12-31');
SELECT @service_2:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Содержание собак', @ru_id, @service_2);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '3', @unit_square_meter, @service_t_garage, '1900-01-01', '2100-12-31');
SELECT @service_3:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Гараж', @ru_id, @service_3);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '4', @unit_gcalories, @service_t_heating, '1900-01-01', '2100-12-31');
SELECT @service_4:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Отопление', @ru_id, @service_4);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '220', null, @service_type_220, '1900-01-01', '2100-12-31');
SELECT @service_220:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Гараж', @ru_id, @service_220);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '230', null, @service_type_230, '1900-01-01', '2100-12-31');
SELECT @service_230:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Сарай', @ru_id, @service_230);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '240', null, @service_type_240, '1900-01-01', '2100-12-31');
SELECT @service_240:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Погреба', @ru_id, @service_240);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '250', null, @service_type_250, '1900-01-01', '2100-12-31');
SELECT @service_250:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Содержание животных', @ru_id, @service_250);


-- init operations
INSERT INTO payments_operations_tbl (id, version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, register_user, register_date, register_organization_id,
		operation_summ, operation_input_summ, change_summ, registry_record_id, reference_operation_id)
		VALUES (1, 0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_1, @operation_type_1,
				'asemenov', '2009-04-14 12:20', @organization_tszh, 'asemenov', '2009-04-14 12:21', @organization_tszh,
				1395.00, 1500.00, 105.00, null, null);
SELECT @operation_1:=1;

INSERT INTO payments_operations_tbl (version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, register_user, register_date, register_organization_id,
		operation_summ, operation_input_summ, change_summ, registry_record_id, reference_operation_id)
		VALUES (0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_2, @operation_type_1,
				'asemenov', '2009-04-14 13:20', @organization_tszh, 'asemenov', '2009-04-14 13:21', @organization_tszh,
				113.00, 200.00, 87.00, null, null);
SELECT @operation_2:=last_insert_id();

INSERT INTO payments_operations_tbl (version, address, payer_fio, level_id, status_id, type_id,
		creator, creation_date, creator_organization_id, register_user, register_date, register_organization_id,
		operation_summ, operation_input_summ, change_summ, registry_record_id, reference_operation_id)
		VALUES (0, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @operation_level_3, @operation_status_4, @operation_type_1,
				'asemenov', '2009-04-14 14:20', @organization_tszh, 'asemenov', '2009-04-14 14:21', @organization_tszh,
				220.00, 500.00, 280.00, null, null);
SELECT @operation_3:=last_insert_id();

-- init documents
INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, summ, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_1, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_1,
				@service_kvartplata_id, 1235.00, null, null, @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_1:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, summ, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_1, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_1,
				@service_10, 90.00, null, null, @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_2:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, summ, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_1, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_1,
				@service_20, 70.00, null, null, @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_3:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, summ, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_2, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_2,
				@service_40, 113.00, null, null, @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_4:=last_insert_id();

INSERT INTO payments_documents_tbl (version, operation_id, address, payer_fio, type_id, status_id,
		service_id, summ, creditor_id, debtor_id, creditor_organization_id, debtor_organization_id,
		reference_document_id, registry_record_id)
		VALUES (0, @operation_3, 'ул. Иванова, д.27, кв.330', 'Федько М.А.', @doc_type_1, @doc_status_4,
				@service_50, 220.00, null, null, @organization_zhko, @organization_tszh,
				null, null);
SELECT @document_5:=last_insert_id();
