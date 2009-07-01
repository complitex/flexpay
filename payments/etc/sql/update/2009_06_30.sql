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
