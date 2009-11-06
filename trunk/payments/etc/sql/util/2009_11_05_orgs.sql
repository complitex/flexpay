SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
select @ds:=id from common_data_source_descriptions_tbl where description='Master-index';
select @common_base:=0x6000 + 0;
select @ab_base:=0x1000 + 0;
select @bti_base:=0x2000 + 0;
select @eirc_base:=0x5000 + 0;
select @orgs_base:=0x4000 + 0;
select @payments_base:=0x3000 + 0;
select @instId:='090-';

delete from orgs_organization_names_tbl;
delete from orgs_organization_descriptions_tbl;
delete from orgs_organizations_tbl;


-- EIRC is the first one, ID=1
INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('EIRC DS');
select @sd:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp, data_source_description_id)
	VALUES (1, 0, 0, 'ADDR', 'ADDR', '-------', '123', @sd);
SELECT @organization_eirc:=1;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Eirc itself', @ru_id, @organization_eirc);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('EIRC', @ru_id, @organization_eirc);



-- Megabank
insert into common_data_source_descriptions_tbl (id, description)
	values (@eirc_base + 1, 'МегаБАНК');
select @ds_megabank:=@eirc_base + 1;
insert into orgs_organizations_tbl (id, version, status, data_source_description_id, individual_tax_number, juridical_address, kpp, postal_address)
	values (@eirc_base + 1, 0, 0, @ds_megabank, '', '', '', '');
select @org_megabank:=@eirc_base + 1;
insert into orgs_organization_names_tbl (name, organization_id, language_id)
	values ('МегаБАНК', @org_megabank, @ru_id);
insert into orgs_organization_descriptions_tbl (name, organization_id, language_id)
	values ('МегаБАНК банк', @org_megabank, @ru_id);

-- KP "VODA"
insert into common_data_source_descriptions_tbl (description) values ('ПУ Вода');
select @source_description_id:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, data_source_description_id, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (@orgs_base + 2, 0, 0, @source_description_id, '', '', '321123321', '89');
SELECT @org_kp_voda:=@orgs_base + 2;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Тестовая организация для Мега Банка (КП "Вода")', @ru_id, @org_kp_voda);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('КП "Вода"', @ru_id, @org_kp_voda);

INSERT INTO orgs_service_providers_tbl(id, status, version, organization_id)
	VALUES (@orgs_base + 2, 0, 0, @org_kp_voda);
SELECT @sp_voda:=@orgs_base + 2;
INSERT INTO orgs_service_provider_descriptions_tbl (version, name, language_id, service_provider_id)
	VALUES (0, 'ПУ Вода', @ru_id, @sp_voda);

SELECT @service_t_cold_water:=7;
INSERT INTO payments_services_tbl (id, provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (50, @sp_voda, null, null, @service_t_cold_water, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_cold_water:=50;
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
		VALUES ('Холодная вода', @ru_id, @service_cold_water);

-- correction from megabank org id to internal
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	values (@sp_voda, '958', @orgs_base + 0x003, @ds_megabank);

-- ZHKS
insert into common_data_source_descriptions_tbl (description) values ('ПУ ЖКС');
select @source_description_id:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, data_source_description_id, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (@orgs_base + 1, 0, 0, @source_description_id, '', '', '123321123', '78');
SELECT @org_gks:=@orgs_base + 1;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Тестовая организация для Мега Банка (ЖКС)', @ru_id, @org_gks);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ЖКС', @ru_id, @org_gks);

INSERT INTO orgs_service_providers_tbl(id, status, version, organization_id)
	VALUES (@orgs_base + 1, 0, 0, @org_gks);
SELECT @sp_gks:=@orgs_base + 1;
INSERT INTO orgs_service_provider_descriptions_tbl (version, name, language_id, service_provider_id)
	VALUES (0, 'ЖИЛКОМСЕРВИС', @ru_id, @sp_gks);

SELECT @service_t_kvartplata:=1;

INSERT INTO payments_service_types_tbl (id, status, code) VALUES (10, 0, 10);
SELECT @service_territory_cleaning:=10;
INSERT INTO payments_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка территории', 'Описание', @ru_id, @service_territory_cleaning);

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

select @unit_grn_m2:=m.id
from common_measure_units_tbl m
		left outer join common_mesuare_unit_names_tbl n on n.measure_unit_id=m.id 
where n.language_id=@ru_id and n.name='грн/м2';

select @unit_square_meter:=m.id
from common_measure_units_tbl m
		left outer join common_mesuare_unit_names_tbl n on n.measure_unit_id=m.id
where n.language_id=@ru_id and n.name='кв.м.';

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, version, status)
	VALUES (@sp_gks, '1', @unit_square_meter, @service_t_kvartplata, '1900-01-01', '2100-12-31', 0, 0);
SELECT @service_kvartplata_id:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Квартплата', @ru_id, @service_kvartplata_id);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '10', @unit_grn_m2, @service_territory_cleaning, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_10:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка территории', @ru_id, @service_10);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '20', @unit_grn_m2, @service_cleaning_garbagecollectors, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_20:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка мусоросборников', @ru_id, @service_20);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '30', @unit_grn_m2, @service_cleaning_ext, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_30:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Уборка подвалов, тех. этажей, крыш', @ru_id, @service_30);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '40', @unit_grn_m2, @service_TBO, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_40:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Вывоз и утилизация ТБО', @ru_id, @service_40);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '50', @unit_grn_m2, @service_to_elevators, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_50:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО лифтов', @ru_id, @service_50);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '60', @unit_grn_m2, @service_to_dispetchering, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_60:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем диспетчеризации', @ru_id, @service_60);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '70', @unit_grn_m2, @service_to_water_supply, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_70:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоснабжения', @ru_id, @service_70);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '80', @unit_grn_m2, @service_to_vodootvedenie, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_80:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем водоотведения', @ru_id, @service_80);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '90', @unit_grn_m2, @service_to_systems_warmproviding, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_90:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем теплоснабжения', @ru_id, @service_90);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '100', @unit_grn_m2, @service_to_system_hot_water_providing, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_100:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО систем горячего водоснабжения', @ru_id, @service_100);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '110', @unit_grn_m2, @service_to_boliers, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_110:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('ТО бойлеров', @ru_id, @service_110);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '120', @unit_grn_m2, @service_fog_canals, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_120:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Обслуживание дымовент. каналов', @ru_id, @service_120);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '130', @unit_grn_m2, @service_cleaning_toilets, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_130:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Очистка дворовых туалетов', @ru_id, @service_130);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '140', @unit_grn_m2, @service_lighting, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_140:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Освещение мест общего пользования', @ru_id, @service_140);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '150', @unit_grn_m2, @service_waterproviding_energy, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_150:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабж. для подкачки воды', @ru_id, @service_150);

INSERT INTO payments_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date, parent_service_id, version, status)
	VALUES (@sp_gks, '160', @unit_grn_m2, @service_elevators_energy, '1900-01-01', '2100-12-31', @service_kvartplata_id, 0, 0);
SELECT @service_160:=last_insert_id();
INSERT INTO payments_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Энергоснабжение для лифтов', @ru_id, @service_160);


-- correction from megabank org id to internal is commented, registries are accepted in FP format
-- insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
--  	values (@sp_gks, '1033', @orgs_base + 0x003, @ds_megabank);

-- remove old master index for service types
delete from common_data_corrections_tbl where object_type=@payments_base + 0x002 and data_source_description_id=@ds;
delete from common_data_corrections_tbl where object_type=@orgs_base + 0x001 and data_source_description_id=@ds;

insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x001, @ds from orgs_organizations_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x003, @ds from orgs_service_providers_tbl);

-- corrections payments objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @payments_base + 0x0201, @ds from payments_services_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @payments_base + 0x002, @ds from payments_service_types_tbl);
